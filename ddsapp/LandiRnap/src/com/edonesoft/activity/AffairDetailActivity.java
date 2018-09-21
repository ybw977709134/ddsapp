package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Affair;
import com.edonesoft.utils.DateUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint("HandlerLeak")
public class AffairDetailActivity extends BaseActivity implements OnClickListener {
	private ImageView affairIcon;
	private TextView affairName;
	private TextView affairCode;
	private TextView affairNameInfoBtn;
	private TextView affairSubmitCount;
	private RelativeLayout affairOrganizationRellay;
	private TextView affairOrganizationText;
	private TextView affairEffectiveDateText;
	private TextView affairSizeText;
	private TextView affairBackGroundText;
	private TextView affairPicScoreText;
	private TextView affairScreendpiText;
	private TextView affairFileSizeText;
	private TextView affairPriceText;
	private TextView affairPhotoFormatText;
	private ImageView takePicMethod1;
	private ImageView takePicMethod2;
	private ImageView takePicMethod3;

	private int affairId;
	private Affair affair;

	// private ImageLoader imageLoader;
	// private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		affairId = getIntent().getIntExtra("affairId", 0);
		setContentView(R.layout.activity_affair_detail);
		// initImageLoader();
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		getAffairInfoData();
	}

	private void findViews() {
		affairCode = (TextView) findViewById(R.id.affair_detail_code);
		affairIcon = (ImageView) findViewById(R.id.affair_detail_image);
		affairName = (TextView) findViewById(R.id.affair_detail_name);
		affairNameInfoBtn = (TextView) findViewById(R.id.affair_name_info_btn);
		affairSubmitCount = (TextView) findViewById(R.id.affair_submit_count);
		affairOrganizationRellay = (RelativeLayout) findViewById(R.id.affair_organization_rellay);
		affairOrganizationText = (TextView) findViewById(R.id.affair_organization_text);
		affairEffectiveDateText = (TextView) findViewById(R.id.affair_effective_date_text);
		affairSizeText = (TextView) findViewById(R.id.affair_size_text);
		affairBackGroundText = (TextView) findViewById(R.id.affair_background_text);
		affairPicScoreText = (TextView) findViewById(R.id.affair_score_text);
		affairScreendpiText = (TextView) findViewById(R.id.affair_screendpi_text);
		affairFileSizeText = (TextView) findViewById(R.id.affair_file_size_text);
		affairPriceText = (TextView) findViewById(R.id.affair_price_text);
		affairPhotoFormatText = (TextView) findViewById(R.id.affair_detail_photo_format);
		takePicMethod1 = (ImageView) findViewById(R.id.take_pic_method1);
		takePicMethod2 = (ImageView) findViewById(R.id.take_pic_method2);
		takePicMethod3 = (ImageView) findViewById(R.id.take_pic_method3);
	}

	private void initListeners() {
		affairNameInfoBtn.setOnClickListener(this);
		affairOrganizationRellay.setOnClickListener(this);
		takePicMethod1.setOnClickListener(this);
		takePicMethod2.setOnClickListener(this);
		takePicMethod3.setOnClickListener(this);
	}

	private void initAffairData() {
		affairCode.setText(affair.getAffair_code());
		affairName.setText(affair.getName());
		affairSubmitCount.setText("已提交人数：" + affair.getCurrentCustomerCount() + "人");
		affairBackGroundText.setText("白色");
		affairEffectiveDateText.setText(DateUtil.doubleTimeToString(affair.getEffective_date(), "yyyy.MM.dd"));
		affairFileSizeText.setText("<100k");
		affairScreendpiText.setText(affair.getMin_dpi() + "dpi");
		affairPicScoreText.setText(">" + affair.getMin_validate_score());
		affairPriceText.setText("0元");
		// imageLoader.displayImage(affair.getIcon_url(), affairIcon, options);
		if (affair.getCompany() != null) {
			affairOrganizationText.setText(affair.getCompany().getName());
		}
		if (affair.getPhoto_spec() != null) {
			affairPhotoFormatText.setText(affair.getPhoto_spec().getName());
			affairSizeText.setText(affair.getPhoto_spec().getPx_width() + " X " + affair.getPhoto_spec().getPx_height()
					+ " Pix");
			if (!affair.getPhoto_spec().getChannel_accepted().contains("1")) {
				takePicMethod1.setEnabled(false);
			}
			if (!affair.getPhoto_spec().getChannel_accepted().contains("2")) {
				takePicMethod2.setEnabled(false);
			}
			if (!affair.getPhoto_spec().getChannel_accepted().contains("3")) {
				takePicMethod3.setEnabled(false);
			}
		}
	}

	// @SuppressWarnings("deprecation")
	// public void initImageLoader() {
	// imageLoader = ImageLoader.getInstance();
	// options = new
	// DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
	// .cacheOnDisc(true).considerExifParams(true).build();
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.affair_name_info_btn: {
			Intent intent = new Intent(this, AffairTextActivity.class);
			intent.putExtra("title", affair.getName());
			intent.putExtra("info", affair.getAffair_intro());
			startActivity(intent);
		}
			break;
		case R.id.affair_organization_rellay: {
			if (affair != null && affair.getCompany() != null) {
				Intent intent = new Intent(this, AffairTextActivity.class);
				if (affair != null && affair.getCompany() != null) {
					intent.putExtra("title", affair.getCompany().getName());
					intent.putExtra("info", affair.getCompany().getCompany_intro());
				}
				startActivity(intent);
			}
		}
			break;
		case R.id.take_pic_method1: {
			Intent intent = new Intent(this, TakePictureActivity.class);
			if (affair != null && affair.getPhoto_spec() != null) {
				intent.putExtra("affairId", affair.getItemID());
				intent.putExtra("photoFormat", affair.getPhoto_spec());
				intent.putExtra("affairName", affair.getName());
			}
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.take_pic_method2: {
			Intent intent = new Intent(this, SearchStationActivity.class);
			if (affair != null && affair.getPhoto_spec() != null) {
				intent.putExtra("spec_id", affair.getPhoto_spec().getItemID());
			}
			startActivity(intent);
		}
			break;
		case R.id.take_pic_method3: {
			Intent intent = new Intent(this, HistoryPhotoActivity.class);
			intent.putExtra("isSelectPhoto", true);
			intent.putExtra("affair", affair);
			startActivityForResult(intent, 0);
		}
			break;
		}
	}

	private void getAffairInfoData() {
		WebDataRequest.requestGet(0, handler, "affair/detail?id=" + affairId);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				try {
					affair = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
							new TypeReference<Affair>() {
							});
					if (affair != null) {
						initAffairData();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
}
