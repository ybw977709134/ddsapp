package com.edonesoft.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.edonesoft.fragment.ForgetPwdFragment1;
import com.edonesoft.fragment.ForgetPwdFragment2;
import com.edonesoft.fragment.ForgetPwdFragment3;
import com.edonesoft.landi.rnap.activity.R;

public class ForgetPasswordActivity extends FragmentActivity {
	private ImageButton backBtn;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		initViews();
	}

	private void initViews() {
		backBtn = (ImageButton) findViewById(R.id.back_btn);

		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getVerCode();
	}

	private void getVerCode() {
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		ForgetPwdFragment1 fragment1 = new ForgetPwdFragment1(this);
		fragmentTransaction.add(R.id.frameLayout, fragment1).commit();
	}

	public void writeVerCode(String phoneNumber) {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		ForgetPwdFragment2 fragment2 = new ForgetPwdFragment2(this, phoneNumber);
		fragmentTransaction.add(R.id.frameLayout, fragment2).commit();
	}

	public void writePwd(String phoneNumber, String verCode) {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		ForgetPwdFragment3 fragment3 = new ForgetPwdFragment3(this, phoneNumber, verCode);
		fragmentTransaction.add(R.id.frameLayout, fragment3).commit();
	}

	public void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}
