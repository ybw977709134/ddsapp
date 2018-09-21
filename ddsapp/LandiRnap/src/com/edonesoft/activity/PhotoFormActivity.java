package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.base.BaseDialog;
import com.edonesoft.base.BaseDialog.OnDialogClickListener;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderInfo;
import com.edonesoft.utils.DateUtil;
import com.edonesoft.utils.ImageUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.DeleteDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("HandlerLeak")
public class PhotoFormActivity extends BaseActivity implements OnClickListener {
	private int orderId;
	private int photoId;
	private String photoUrl;
	private OrderInfo orderInfo;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private ImageView photoDetailCommenPhoto;
	private TextView photoDetailAffairName;
	private TextView photoFileSizeTv;
	private TextView photoScoreTv;
	private TextView photoSizeTv;
	private TextView photoDetailCommitDateTv;
	private TextView photoDetailLandiCodeTv;
	private TextView photoDetailAddressTv;
	private TextView showAffairDetailBtn;
	private TextView downloadBtn;
	private RelativeLayout landiCodeRellay;
	private TextView deleteBtn;
	private TextView photoDetailAffairCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_form);
		orderId = getIntent().getIntExtra("orderId", 0);
		photoId = getIntent().getIntExtra("photoId", 0);
		photoUrl = getIntent().getStringExtra("photoUrl");
		initImageLoader();
		initViews();
		getorderInfoData();
	}

	private void initViews() {
		findViews();
		initListeners();
	}

	private void findViews() {
		landiCodeRellay = (RelativeLayout) findViewById(R.id.photo_detail_landi_code_rellay);
		photoDetailCommenPhoto = (ImageView) findViewById(R.id.photo_detail_commen_photo);
		photoFileSizeTv = (TextView) findViewById(R.id.photo_detail_file_size);
		photoSizeTv = (TextView) findViewById(R.id.photo_detail_photo_size);
		photoDetailAffairName = (TextView) findViewById(R.id.photo_detail_affair_name);
		photoScoreTv = (TextView) findViewById(R.id.photo_detail_photo_score);
		photoDetailCommitDateTv = (TextView) findViewById(R.id.photo_detail_commit_date);
		photoDetailLandiCodeTv = (TextView) findViewById(R.id.photo_detail_landi_code);
		photoDetailAddressTv = (TextView) findViewById(R.id.photo_detail_commit_address);
		showAffairDetailBtn = (TextView) findViewById(R.id.photo_detail_show_affair_detail);
		downloadBtn = (TextView) findViewById(R.id.download_btn);
		deleteBtn = (TextView) findViewById(R.id.delete_btn);
		photoDetailAffairCode = (TextView) findViewById(R.id.photo_detail_affair_code);
	}

	private void initListeners() {
		downloadBtn.setOnClickListener(this);
		landiCodeRellay.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
	}

	private void initPhotoData() {
		imageLoader.displayImage(orderInfo.getPhoto_valid_url(), photoDetailCommenPhoto, options);
		// photoFileSizeTv.setText("100k");
		if (orderInfo.getValidate_score() != 0) {
			photoScoreTv.setText("比对分值：" + orderInfo.getValidate_score());
		} else {
			photoScoreTv.setBackgroundResource(R.color.transparent);
		}
		if (orderInfo.getPhoto_info().length() == 0) {
			photoDetailAddressTv.setText("移动端拍摄");
		} else {
			photoDetailAddressTv.setText(orderInfo.getPhoto_info());
		}
		photoDetailCommitDateTv.setText(DateUtil.doubleTimeToString(orderInfo.getTake_date(), "yyyy.MM.dd"));
		photoDetailLandiCodeTv.setText(orderInfo.getPhotograph_code());
		photoDetailAffairName.setText(orderInfo.getAffairName());
		photoDetailAffairCode.setText(orderInfo.getAffairCode());
		if (orderInfo.getPhoto_spec() != null) {
			photoSizeTv.setText(orderInfo.getPhoto_spec().getPx_width() + " X "
					+ orderInfo.getPhoto_spec().getPx_height() + " Pix");
			showAffairDetailBtn.setText(orderInfo.getPhoto_spec().getName());
		}
		if (orderInfo.isIsRecentPhoto()) {
			deleteBtn.setEnabled(false);
			deleteBtn.setBackgroundResource(R.drawable.login_btn_bg_pressed);
			deleteBtn.setTextColor(getResources().getColor(R.color.line_color));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.download_btn:
			downloadPhoto();
			break;
		case R.id.photo_detail_landi_code_rellay: {
			if (orderInfo.getPhotograph_code().length() != 0) {
				Intent intent = new Intent(this, RnapCodeActivity.class);
				intent.putExtra("itemId", orderInfo.getPhotograph_code());
				startActivity(intent);
			}
		}
			break;
		case R.id.delete_btn:
			showDeleteDialog();
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	private void getorderInfoData() {
		WebDataRequest.requestGet(0, handler, "order/detail?id=" + orderId);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");

			switch (msg.arg1) {
			case 0:
				if (WebDataRequestHelper.validateJsonResponse(msg)) {
					try {
						orderInfo = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
								new TypeReference<OrderInfo>() {
								});
						if (orderInfo != null) {
							initPhotoData();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case 1:
				if (WebDataRequestHelper.validateJsonResponse(msg)) {
					showToast("证照删除成功！");
					setResult(1);
					finish();
				}
				break;
			case 2:
				showToast("保存成功！");
				break;
			case 3:
				showToast("保存失败！");
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void showDeleteDialog() {
		DeleteDialog deleteDialog = new DeleteDialog(this);
		deleteDialog.setConfirmClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				deletePhoto();
			}
		});
		deleteDialog.setCancelClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				dialog.cancel();
			}
		});
		deleteDialog.show();
	}

	private void deletePhoto() {
		WebDataRequest.requestGet(1, handler, "order/photo/delete?id=" + photoId);
	}

	private void downloadPhoto() {
		new Thread() {
			@Override
			public void run() {
				Message message = new Message();
				if (ImageUtil.saveImageToGallery(getApplicationContext(), photoUrl)) {
					message.arg1 = 2;
				} else {
					message.arg1 = 3;
				}
				handler.sendMessage(message);
				super.run();
			}
		}.start();
	}
}
