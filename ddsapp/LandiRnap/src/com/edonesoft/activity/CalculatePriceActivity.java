package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.base.BaseDialog;
import com.edonesoft.base.BaseDialog.OnDialogClickListener;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderCommitModel;
import com.edonesoft.model.UserInfo;
import com.edonesoft.utils.ImageUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.CheckCommitPicDialog;
import com.edonesoft.views.dialog.LoadingDialog;

@SuppressLint("HandlerLeak")
public class CalculatePriceActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	private TextView nextBtn;
	private ImageView orderCommitImage;
	private TextView orderCommitSizeTv;
	private TextView orderCommitMemoryTv;
	private TextView orderPriceTv;
	private TextView orderLastPriceTv;
	private TextView orderCommitName;

	private LoadingDialog loadingDialog;

	private OrderCommitModel orderCommitModel;
	private Bitmap commenBitmap = null;
	private Bitmap finalBitmap = null;
	private CheckBox selectServiceCheckBox1;
	private CheckBox selectServiceCheckBox2;
	private ImageView selectServiceContent1;
	private ImageView selectServiceContent2;

	private int score = 0;
	private boolean isPhotoChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate_price);
		orderCommitModel = getIntent().getParcelableExtra("orderCommitModel");
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		initPic();
		loadingDialog = new LoadingDialog(this);
		orderPriceTv.setText("￥0");
		orderCommitName.setText(orderCommitModel.getName());
		orderLastPriceTv.setText("￥5");
		orderLastPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
	}

	private void findViews() {
		orderCommitName = (TextView) findViewById(R.id.order_commit_pic_name);
		orderLastPriceTv = (TextView) findViewById(R.id.order_commit_last_price);
		nextBtn = (TextView) findViewById(R.id.next_btn);
		orderCommitImage = (ImageView) findViewById(R.id.order_commit_pic_img);
		orderCommitMemoryTv = (TextView) findViewById(R.id.order_commit_pic_memory);
		orderCommitSizeTv = (TextView) findViewById(R.id.order_commit_pic_size);
		orderPriceTv = (TextView) findViewById(R.id.order_commit_price);
		selectServiceCheckBox1 = (CheckBox) findViewById(R.id.select_service_checkbox1);
		selectServiceCheckBox2 = (CheckBox) findViewById(R.id.select_service_checkbox2);
		selectServiceContent1 = (ImageView) findViewById(R.id.select_service_content1);
		selectServiceContent2 = (ImageView) findViewById(R.id.select_service_content2);
	}

	private void initListeners() {
		nextBtn.setOnClickListener(this);
		selectServiceContent1.setOnClickListener(this);
		selectServiceContent2.setOnClickListener(this);
		selectServiceCheckBox1.setOnCheckedChangeListener(this);
		selectServiceCheckBox2.setOnCheckedChangeListener(this);
	}

	private void initPic() {
		commenBitmap = ImageUtil.getRotateBitmap(orderCommitModel.getPhoto_common());
		finalBitmap = ImageUtil.getRotateBitmap(orderCommitModel.getPhoto_final());
		orderCommitImage.setImageBitmap(finalBitmap);
		orderCommitMemoryTv.setText("文件大小:" + (finalBitmap.getByteCount() / 1000 / 16) + "k");
		orderCommitSizeTv.setText("像素尺寸:" + orderCommitModel.getPx_width() + " X " + orderCommitModel.getPx_height()
				+ " Pix");
		orderCommitModel.setFile_size(finalBitmap.getByteCount() / 1000 / 16);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn: {
			if ((!selectServiceCheckBox1.isChecked()) && (!selectServiceCheckBox2.isChecked())) {
				showToast("请至少选择一项服务!");
				return;
			}
			if (selectServiceCheckBox2.isChecked()) {
				Intent intent = new Intent(CalculatePriceActivity.this, CommitOrderActivity.class);
				orderCommitModel.setValidate_score(0);
				if (selectServiceCheckBox2.isChecked()) {
					orderCommitModel.setRequested_services("[1,2]");
				} else {
					orderCommitModel.setRequested_services("[2]");
				}
				intent.putExtra("orderCommitModel", orderCommitModel);
				startActivityForResult(intent, 1);
			} else {
				if (isPhotoChecked) {
					showCheckPicScoreDialog();
				} else {
					checkCommitPic();
				}
			}
		}
			break;
		case R.id.select_service_content1: {
			Intent intent = new Intent(this, HelpDetailActivity.class);
			intent.putExtra("bulletinName", "相片核验");
			startActivity(intent);
		}
			break;
		case R.id.select_service_content2: {
			Intent intent = new Intent(this, HelpDetailActivity.class);
			intent.putExtra("bulletinName", "制作处理");
			startActivity(intent);
		}
			break;
		}
	}

	private void checkCommitPic() {
		if (!AppConfig.sharedInstance().isUserLogin()) {
			showToast("服务需要您先登录!");
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("isFinish", false);
			startActivityForResult(intent, 0);
			return;
		}
		String idcard = AppConfig.sharedInstance().idCard;
		if (idcard == null || idcard.length() == 0) {
			showToast("服务需要您先做实名认证!");
			return;
		}
		loadingDialog.show();

		Bitmap checkBitMap = ImageUtil.automaticCropBitmap(commenBitmap, true);
		byte[] imageBytes = ImageUtil.BitmapToBytes(checkBitMap, "jpg", 100);
		String imagedata = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
		WebDataRequest.requestPost(0, handler, "order/identify/verify", "{\"IDCard\":\"" + idcard + "\",\"Name\":\""
				+ AppConfig.sharedInstance().realName + "\",\"ImageData\":\"" + imagedata + "\",\"AffairID\":"
				+ orderCommitModel.getAffair_id() + "}");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loadingDialog.isShowing()) {
				loadingDialog.cancel();
			}
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				switch (msg.arg1) {
				case 0:
					JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
					JSONObject json;
					try {
						json = responseJson.getJSONObject("Detail");
						score = json.getInt("Photo_score");
						showCheckPicScoreDialog();
					} catch (JSONException e) {
						showToast("数据解析错误!");
					}
					break;
				case 1:
					try {
						UserInfo userInfo = JSON.parseObject(WebDataRequestHelper.getJsonObject(responseText)
								.getString("Detail"), UserInfo.class);
						AppConfig appConfig = AppConfig.sharedInstance();
						appConfig.CurrentIconUrl = userInfo.getPortrait();
						appConfig.CurrentDisplayName = userInfo.getName();
						appConfig.realName = userInfo.getName();
						appConfig.idCard = userInfo.getId_card();
						appConfig.CurrentPhone = userInfo.getMobilephone();
						AppConfig.sharedInstance().saveToPreference();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				}
			} else {
				if (msg.arg1 == 0) {
					showCheckPicScoreDialog();
				}
			}
			super.handleMessage(msg);
		}
	};

	private void showCheckPicScoreDialog() {
		CheckCommitPicDialog dialog = new CheckCommitPicDialog(this, score);
		dialog.setCancelClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				setResult(2);
				finish();
			}
		});
		dialog.setConfirmClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				Intent intent = new Intent(CalculatePriceActivity.this, CommitOrderActivity.class);
				orderCommitModel.setValidate_score(score);
				orderCommitModel.setRequested_services("[1]");
				intent.putExtra("orderCommitModel", orderCommitModel);
				startActivityForResult(intent, 1);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			getUserData();
		}
		if (requestCode == 1) {
			if (resultCode == 1) {
				setResult(1);
				finish();
			} else if (resultCode == 2) {
				isPhotoChecked = data.getBooleanExtra("isPhotoChecked", false);
				score = data.getIntExtra("score", 0);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getUserData() {
		WebDataRequest.requestGet(1, handler, "/profile/detail");
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (selectServiceCheckBox1.isChecked() && selectServiceCheckBox2.isChecked()) {
			orderLastPriceTv.setText("￥25");
			nextBtn.setEnabled(true);
			nextBtn.setTextColor(getResources().getColor(R.color.text_blue));
		} else if (!selectServiceCheckBox1.isChecked() && selectServiceCheckBox2.isChecked()) {
			orderLastPriceTv.setText("￥5");
			nextBtn.setEnabled(true);
			nextBtn.setTextColor(getResources().getColor(R.color.text_blue));
		} else if (selectServiceCheckBox1.isChecked() && !selectServiceCheckBox2.isChecked()) {
			orderLastPriceTv.setText("￥20");
			nextBtn.setEnabled(true);
			nextBtn.setTextColor(getResources().getColor(R.color.text_blue));
		} else {
			orderLastPriceTv.setText("￥0");
			nextBtn.setEnabled(false);
			nextBtn.setTextColor(getResources().getColor(R.color.line_color));
		}
	}
}
