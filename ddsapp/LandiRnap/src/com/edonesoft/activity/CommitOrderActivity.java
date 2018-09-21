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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderCommitModel;
import com.edonesoft.utils.ImageUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.LoadingDialog;

@SuppressLint("HandlerLeak")
public class CommitOrderActivity extends BaseActivity implements OnClickListener {
	private ImageView commitPic;
	private TextView orderNameTv;
	private TextView photoFormatTv;
	private TextView fileSizeTv;
	private TextView commitBtn;
	private TextView backHomeBtn;
	private TextView service1PriceTv;
	private TextView service1LastPriceTv;
	private TextView service2PriceTv;
	private TextView service2LastPriceTv;
	private RelativeLayout service1PriceRellay;
	private RelativeLayout service2PriceRellay;

	private OrderCommitModel orderCommitModel;
	private Bitmap commenBitmap;
	private Bitmap finalBitmap;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit_order);
		orderCommitModel = getIntent().getParcelableExtra("orderCommitModel");
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		initData();
	}

	private void findViews() {
		photoFormatTv = (TextView) findViewById(R.id.commit_order_photo_format);
		fileSizeTv = (TextView) findViewById(R.id.commit_order_file_size);
		backHomeBtn = (TextView) findViewById(R.id.back_btn);
		commitPic = (ImageView) findViewById(R.id.commit_order_image);
		orderNameTv = (TextView) findViewById(R.id.commit_order_name);
		commitBtn = (TextView) findViewById(R.id.commit_btn);
		service1PriceTv = (TextView) findViewById(R.id.commit_order_service1_price);
		service1LastPriceTv = (TextView) findViewById(R.id.commit_order_service1_last_price);
		service1PriceRellay = (RelativeLayout) findViewById(R.id.commit_order_price1_rellay);
		service2PriceTv = (TextView) findViewById(R.id.commit_order_service2_price);
		service2LastPriceTv = (TextView) findViewById(R.id.commit_order_service2_last_price);
		service2PriceRellay = (RelativeLayout) findViewById(R.id.commit_order_price2_rellay);
	}

	private void initListeners() {
		commitBtn.setOnClickListener(this);
		backHomeBtn.setOnClickListener(this);
	}

	private void initData() {
		initPhoto();
		if (orderCommitModel.getRequested_services().contains("1")) {
			service1PriceRellay.setVisibility(View.VISIBLE);
			service1LastPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		if (orderCommitModel.getRequested_services().contains("2")) {
			service2PriceRellay.setVisibility(View.VISIBLE);
			service2LastPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		orderNameTv.setText(orderCommitModel.getName());
		photoFormatTv.setText("像素尺寸:" + orderCommitModel.getPx_width() + " X " + orderCommitModel.getPx_height()
				+ " Pix");
		fileSizeTv.setText("文件大小:" + orderCommitModel.getFile_size() + "k");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_btn:
			if (commenBitmap != null && finalBitmap != null) {
				showLoadingDialog();
				submitOrder();
			}
			break;
		case R.id.back_btn:
			setResult(1);
			finish();
			break;
		}
	}

	private void submitOrder() {
		String commenPhotoData = Base64.encodeToString(ImageUtil.BitmapToBytes(commenBitmap, "jpg", 100),
				Base64.NO_WRAP);
		String finalPhotoData = Base64.encodeToString(ImageUtil.BitmapToBytes(finalBitmap, "jpg", 100), Base64.NO_WRAP);
		String photographCode;
		if (orderCommitModel.getPhotograph_code() == null || orderCommitModel.getPhotograph_code().length() == 0) {
			photographCode = "";
		} else {
			photographCode = orderCommitModel.getPhotograph_code();
		}

		String requestBody = "{\"Affair_id\":" + orderCommitModel.getAffair_id() + ",\"Photograph_code\":\""
				+ photographCode + "\",\"Name\":\"" + orderCommitModel.getName() + "\",\"Photo_common\":\""
				+ commenPhotoData + "\",\"Photo_final\":\"" + finalPhotoData + "\",\"Photo_spec_id\":"
				+ orderCommitModel.getPhoto_spec_id() + ",\"Requested_services\":\""
				+ orderCommitModel.getRequested_services() + "\",\"Validate_score\":"
				+ orderCommitModel.getValidate_score() + ",\"Photo_info\":\"移动端拍摄:" + android.os.Build.MODEL + "\"}";
		WebDataRequest.requestPost(0, handler, "order/submit", requestBody);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.cancel();
			}
			String responseText = msg.getData().getString("ResponseText");
			switch (msg.arg1) {
			case 0:
				if (WebDataRequestHelper.validateJsonResponse(msg)) {
					JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
					JSONObject json;
					try {
						json = responseJson.getJSONObject("Detail");
						int orderId = json.getInt("OrderId");
						if (orderId != 0) {
							showToast("事务提交成功!");
							Intent intent = new Intent(CommitOrderActivity.this, OrderFormActivity.class);
							intent.putExtra("orderId", orderId);
							if (!orderCommitModel.getRequested_services().contains("2")) {
								intent.putExtra("orderStatus", 20);
							}
							intent.putExtra("isShowHome", true);
							startActivityForResult(intent, 0);
						} else {
							showToast("数据解析错误！");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case 1:
				if (commenBitmap != null) {
					commitPic.setImageBitmap(finalBitmap);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			setResult(1);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initPhoto() {
		new Thread() {
			@Override
			public void run() {
				if (orderCommitModel.getPhoto_common().contains("http://")) {
					commenBitmap = ImageUtil.downloadBitmap(orderCommitModel.getPhoto_common(), 800, 600);
					finalBitmap = ImageUtil.downloadBitmap(orderCommitModel.getPhoto_final(), 800, 600);
				} else {
					commenBitmap = ImageUtil.getRotateBitmap(orderCommitModel.getPhoto_common());
					finalBitmap = ImageUtil.getRotateBitmap(orderCommitModel.getPhoto_final());
				}
				Message message = new Message();
				message.arg1 = 1;
				handler.sendMessage(message);
				super.run();
			}
		}.start();
	}

	private void showLoadingDialog() {
		loadingDialog = new LoadingDialog(this);
		loadingDialog.show();
	}

	@Override
	public void leftClicked(View view) {
		Intent data = new Intent();
		if (orderCommitModel.getRequested_services().contains("1")) {
			data.putExtra("isPhotoChecked", true);
			data.putExtra("sroce", orderCommitModel.getValidate_score());
			setResult(2, data);
		}
		super.leftClicked(view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// Intent data = new Intent();
			// if (orderCommitModel.getRequested_services().contains("1")
			// &&! orderCommitModel.getRequested_services().contains("2")) {
			// data.putExtra("isPhotoChecked", true);
			// data.putExtra("score", orderCommitModel.getValidate_score());
			// setResult(2, data);
			// }
			// finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
