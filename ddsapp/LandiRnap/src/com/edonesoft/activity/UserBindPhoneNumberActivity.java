package com.edonesoft.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.edonesoft.fragment.UserBindPhoneNumberFragment1;
import com.edonesoft.fragment.UserBindPhoneNumberFragment2;
import com.edonesoft.fragment.UserBindPhoneNumberFragment3;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;

@SuppressLint("HandlerLeak")
public class UserBindPhoneNumberActivity extends FragmentActivity {
	private String phoneNumber;
	private FragmentManager fragmentManager;

	public void leftClicked(View view) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_bind_phonenumber);
		phoneNumber = getIntent().getStringExtra("phoneNumber");
		initFragment();
	}

	private void initFragment() {
		fragmentManager = getSupportFragmentManager();
		if (StringUtil.isNullOrEmpty(phoneNumber)) {
			showPhoneNumber();
		} else {
			getVerCode();
		}
	}

	public void showPhoneNumber() {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		UserBindPhoneNumberFragment1 bindPhoneNumberFragment1 = new UserBindPhoneNumberFragment1(this);
		fragmentTransaction.add(R.id.frameLayout, bindPhoneNumberFragment1).commit();
	}

	public void getVerCode() {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		UserBindPhoneNumberFragment2 bindPhoneNumberFragment2 = new UserBindPhoneNumberFragment2(this);
		fragmentTransaction.add(R.id.frameLayout, bindPhoneNumberFragment2).commit();
	}

	public void bindPhoneNumber(String phoneNumber) {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		UserBindPhoneNumberFragment3 bindPhoneNumberFragment3 = new UserBindPhoneNumberFragment3(this);
		fragmentTransaction.add(R.id.frameLayout, bindPhoneNumberFragment3).commit();
	}

	public void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}
