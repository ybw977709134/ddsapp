package com.edonesoft.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.views.dialog.LoadingDialog;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class AboutActivity extends BaseActivity implements OnClickListener {
	private TextView splashTv;
	private TextView tvAboutContent;
	private TextView checkVersionsTv;
	private TextView phoneNumberTv;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initViews();
	}

	private void initViews() {
		checkVersionsTv = (TextView) findViewById(R.id.about_check_versions);
		tvAboutContent = (TextView) findViewById(R.id.about_content_text);
		splashTv = (TextView) findViewById(R.id.about_splash);
		phoneNumberTv = (TextView) findViewById(R.id.about_phone_number);
		checkVersionsTv.setOnClickListener(this);
		tvAboutContent.setOnClickListener(this);
		splashTv.setOnClickListener(this);
		phoneNumberTv.setOnClickListener(this);

		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				if (loadingDialog != null) {
					loadingDialog.dismiss();
				}
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(getApplicationContext(), updateInfo);
					break;
				case UpdateStatus.No: // has no update
					showToast("当前版本为最新，不需要更新!");
					break;
				case UpdateStatus.NoneWifi: // none wifi
					showToast("请在wifi连接下进行更新!");
					break;
				case UpdateStatus.Timeout: // time out
					showToast("检测更新超时!");
					break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_splash: {
			Intent intent = new Intent(this, SplashActivity.class);
			intent.putExtra("isJumpFromAbout", true);
			startActivity(intent);
		}
			break;
		case R.id.about_content_text: {
			Intent intent = new Intent(this, AboutContentActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.about_check_versions: {
			showLoadingDialog();
			UmengUpdateAgent.update(this);
		}
			break;
		case R.id.about_phone_number: {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-820-3010"));
			startActivity(intent);
		}
			break;
		}
	}

	private void showLoadingDialog() {
		loadingDialog = new LoadingDialog(this);
		loadingDialog.show();
	}
}
