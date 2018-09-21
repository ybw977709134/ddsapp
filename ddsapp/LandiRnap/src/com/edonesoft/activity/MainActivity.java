package com.edonesoft.activity;

import org.json.JSONException;
import com.alibaba.fastjson.JSON;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.UserInfo;
import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.utils.DensityUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class MainActivity extends BaseActivity implements OnClickListener {
	private ImageView tabBtnTakePic;
	private ImageView tabBtnLandiCode;
	private ImageView tabBtnCommitPic;
	private ImageView menuBtn;
	private RelativeLayout leftMenuOrderRellay;
	private RelativeLayout leftMenuLbsRellay;
	private RelativeLayout leftMenuFreebackRellay;
	private RelativeLayout leftMenuHelpRellay;
	private RelativeLayout leftMenuAboutRellay;
	private RelativeLayout leftMenuPhotoRellay;
	private ImageView userIcon;
	private TextView userNameTv;

	private SlidingMenu slidingMenu;
	private View leftMenuView;
	private long exitTime = 0;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initImageLoader();
		initViews();
	}

	@Override
	protected void onResume() {
		if (AppConfig.sharedInstance().isUserLogin()) {
			getUserInfo();
		} else {
			userIcon.setImageResource(R.drawable.user_default_icon);
			userNameTv.setText("点击登录，体验更多");
		}
		super.onResume();
	}

	private void initViews() {
		initLeftMenu();
		findViews();
		initListeners();
	}

	private void initLeftMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindWidth(DensityUtil.dip2px(this, 250));
		leftMenuView = LayoutInflater.from(this).inflate(R.layout.left_menu, null);
		slidingMenu.setMenu(leftMenuView);
		slidingMenu.setFadeEnabled(true);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setBehindScrollScale(0.333f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	}

	private void findViews() {
		tabBtnTakePic = (ImageView) findViewById(R.id.main_tab_btn2);
		tabBtnLandiCode = (ImageView) findViewById(R.id.main_tab_btn1);
		tabBtnCommitPic = (ImageView) findViewById(R.id.main_tab_btn3);
		menuBtn = (ImageView) findViewById(R.id.main_menu_btn);
		leftMenuOrderRellay = (RelativeLayout) leftMenuView.findViewById(R.id.left_menu_order_rellay);
		leftMenuLbsRellay = (RelativeLayout) leftMenuView.findViewById(R.id.left_menu_lbs_rellay);
		leftMenuFreebackRellay = (RelativeLayout) leftMenuView.findViewById(R.id.left_menu_freeback_rellay);
		leftMenuHelpRellay = (RelativeLayout) leftMenuView.findViewById(R.id.left_menu_help_rellay);
		leftMenuAboutRellay = (RelativeLayout) leftMenuView.findViewById(R.id.left_menu_about_rellay);
		leftMenuPhotoRellay = (RelativeLayout) leftMenuView.findViewById(R.id.left_menu_photo_rellay);
		userIcon = (ImageView) leftMenuView.findViewById(R.id.left_menu_usericon);
		userNameTv = (TextView) leftMenuView.findViewById(R.id.left_menu_username);
	}

	private void initListeners() {
		tabBtnCommitPic.setOnClickListener(this);
		tabBtnTakePic.setOnClickListener(this);
		tabBtnLandiCode.setOnClickListener(this);
		menuBtn.setOnClickListener(this);
		leftMenuOrderRellay.setOnClickListener(this);
		leftMenuLbsRellay.setOnClickListener(this);
		leftMenuFreebackRellay.setOnClickListener(this);
		leftMenuHelpRellay.setOnClickListener(this);
		leftMenuAboutRellay.setOnClickListener(this);
		leftMenuPhotoRellay.setOnClickListener(this);
		userIcon.setOnClickListener(this);
	}

	private void initUserData() {
		userNameTv.setText(userInfo.getName());
		imageLoader.displayImage(userInfo.getPortrait(), userIcon, options);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_tab_btn2: {
			if (AppConfig.sharedInstance().isUserLogin()) {
				Intent intent = new Intent(this, TakePictureActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
		}
			break;
		case R.id.main_tab_btn1: {
			Intent intent = new Intent(this, LandiCodeActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.main_tab_btn3: {
			Intent intent = new Intent(this, SelectAffairActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.main_menu_btn:
			slidingMenu.toggle();
			break;
		case R.id.left_menu_order_rellay: {
			if (AppConfig.sharedInstance().isUserLogin()) {
				Intent intent = new Intent(this, OrderListActivity.class);
				startActivity(intent);
			} else {
				showToast("服务需要您先登录!");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
		}
			break;
		case R.id.left_menu_lbs_rellay: {
			Intent intent = new Intent(this, SearchStationActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.left_menu_freeback_rellay: {
			Intent intent = new Intent(this, FreebackActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.left_menu_help_rellay: {
			Intent intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.left_menu_about_rellay: {
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.left_menu_photo_rellay: {
			if (AppConfig.sharedInstance().isUserLogin()) {
				Intent intent = new Intent(this, HistoryPhotoActivity.class);
				startActivity(intent);
			} else {
				showToast("服务需要您先登录!");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
		}
			break;
		case R.id.left_menu_usericon:
			if (AppConfig.sharedInstance().isUserLogin()) {
				Intent intent = new Intent(this, ProfileActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (slidingMenu.isMenuShowing()) {
				slidingMenu.toggle();
				return true;
			} else {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					showToast("再次点击退出");
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(DensityUtil.dip2px(this, 50)))
				.showImageForEmptyUri(R.drawable.user_default_icon).showImageOnLoading(R.drawable.user_default_icon)
				.showImageOnFail(R.drawable.user_default_icon).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	private void getUserInfo() {
		WebDataRequest.requestGet(1, handler, "profile/detail");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				try {
					userInfo = JSON.parseObject(WebDataRequestHelper.getJsonObject(responseText).getString("Detail"),
							UserInfo.class);
					AppConfig appConfig = AppConfig.sharedInstance();
					appConfig.CurrentIconUrl = userInfo.getPortrait();
					appConfig.CurrentDisplayName = userInfo.getName();
					appConfig.realName = userInfo.getName();
					appConfig.idCard = userInfo.getId_card();
					appConfig.CurrentPhone = userInfo.getMobilephone();
					AppConfig.sharedInstance().saveToPreference();
					initUserData();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			super.handleMessage(msg);
		}
	};
}
