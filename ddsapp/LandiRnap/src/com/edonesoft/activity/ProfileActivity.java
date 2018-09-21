package com.edonesoft.activity;

import org.json.JSONException;

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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.UserInfo;
import com.edonesoft.utils.DensityUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("HandlerLeak")
public class ProfileActivity extends BaseActivity implements OnClickListener {
	private TextView userNameTv;
	private TextView landiCodeTv;
	// private TextView countryTv;
	private TextView phoneNumberTv;
	private TextView nameCheckTv;
	private ImageView nameCheckIcon;
	private RelativeLayout userNameRellay;
	private RelativeLayout safeRellay;
	private RelativeLayout phoneNumberRellay;

	private RelativeLayout nameCheckRellay;
	private RelativeLayout landiCodeRellay;
	private ImageView userIcon;
	private TextView logoutBtn;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initImageLoader();
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		getUserData();
	}

	private void findViews() {
		userNameTv = (TextView) findViewById(R.id.profile_username);
		landiCodeTv = (TextView) findViewById(R.id.profile_code);
		// countryTv = (TextView) findViewById(R.id.profile_country);
		logoutBtn = (TextView) findViewById(R.id.logout_btn);
		phoneNumberTv = (TextView) findViewById(R.id.profile_phone_number);
		nameCheckTv = (TextView) findViewById(R.id.profile_namecheck);
		safeRellay = (RelativeLayout) findViewById(R.id.profile_safe_rellay);
		userNameRellay = (RelativeLayout) findViewById(R.id.profile_nickname_rellay);
		phoneNumberRellay = (RelativeLayout) findViewById(R.id.profile_phone_rellay);
		nameCheckRellay = (RelativeLayout) findViewById(R.id.profile_namecheck_rellay);
		landiCodeRellay = (RelativeLayout) findViewById(R.id.profile_code_rellay);
		userIcon = (ImageView) findViewById(R.id.profile_usericon);
		nameCheckIcon = (ImageView) findViewById(R.id.profile_namecheck_icon);
	}

	private void initListeners() {
		safeRellay.setOnClickListener(this);
		userNameRellay.setOnClickListener(this);
		phoneNumberRellay.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		nameCheckRellay.setOnClickListener(this);
		landiCodeRellay.setOnClickListener(this);
	}

	private void getUserData() {
		WebDataRequest.requestGet(0, handler, "/profile/detail");
	}

	private void loginOut() {
		WebDataRequest.requestGet(1, handler, "sso/logout");
	}

	private void initUserData() {
		phoneNumberTv.setText(userInfo.getMobilephone().substring(0, 3) + "****"
				+ userInfo.getMobilephone().substring(7));
		userNameTv.setText(userInfo.getName());
		imageLoader.displayImage(userInfo.getPortrait(), userIcon, options);
		landiCodeTv.setText(userInfo.getItemID() + "");
		if (userInfo.getId_card() != null && userInfo.getId_card().length() > 0) {
			showNameCheckIcon();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				switch (msg.arg1) {
				case 0:
					try {
						userInfo = JSON.parseObject(WebDataRequestHelper.getJsonObject(responseText)
								.getString("Detail"), UserInfo.class);
						initUserData();
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
				case 1:
					showToast("注销成功!");
					AppConfig.sharedInstance().userLogout();
					finish();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.user_default_icon)
				.displayer(new RoundedBitmapDisplayer(DensityUtil.dip2px(this, 42)))
				.showImageForEmptyUri(R.drawable.user_default_icon).showImageOnFail(R.drawable.user_default_icon)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profile_safe_rellay: {
			Intent intent = new Intent(this, UserSafeActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.profile_nickname_rellay: {
			Intent intent = new Intent(this, UserIconActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.profile_phone_rellay: {
			Intent intent = new Intent(this, UserBindPhoneNumberActivity.class);
			intent.putExtra("phoneNumber", userInfo.getMobilephone());
			startActivity(intent);
		}
			break;
		case R.id.profile_code_rellay: {
			Intent intent = new Intent(this, UserCodeActivity.class);
			intent.putExtra("itemId", userInfo.getItemID());
			startActivity(intent);
		}
			break;
		case R.id.profile_namecheck_rellay: {
			if (userInfo.getId_card() == null || userInfo.getId_card().length() == 0) {
				Intent intent = new Intent(this, UserNameCheckActivity.class);
				startActivityForResult(intent, 0);
			}
		}
			break;
		case R.id.logout_btn:
			loginOut();
			break;
		}
	}

	private void showNameCheckIcon() {
		nameCheckIcon.setVisibility(View.VISIBLE);
		int idCardLength = userInfo.getId_card().length();
		nameCheckTv.setText(userInfo.getId_card().substring(0, 1) + "****************"
				+ userInfo.getId_card().substring(idCardLength - 1, idCardLength));
		nameCheckTv.setTextColor(getResources().getColor(R.color.text_gray));
		RelativeLayout.LayoutParams params = (LayoutParams) nameCheckTv.getLayoutParams();
		params.rightMargin = DensityUtil.dip2px(this, 40);
		nameCheckTv.setLayoutParams(params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && requestCode == 1) {
			getUserData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
