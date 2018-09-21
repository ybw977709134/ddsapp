package com.edonesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;

public class UserSafeActivity extends BaseActivity {
	private TextView phoneNumberTv;
	private TextView changePwdBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_safe);
		initViews();
	}

	private void initViews() {
		phoneNumberTv = (TextView) findViewById(R.id.user_safe_bind_phone);
		changePwdBtn = (TextView) findViewById(R.id.user_safe_change_pwd);
		String phoneNumber = AppConfig.sharedInstance().CurrentPhone;
		phoneNumberTv.setText(phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7));
		changePwdBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserSafeActivity.this, ChangePasswordActivity.class);
				startActivity(intent);
			}
		});
	}
}
