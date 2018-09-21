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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView registerBtn;
	private EditText phoneNumberEt;
	private EditText pwdEt;
	private TextView forgetPwd;
	private TextView loginBtn;
	private ImageView thirdPartyWeixin;
	private ImageView thirdPartyQQ;

	private boolean isFinish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		isFinish=getIntent().getBooleanExtra("isFinish", true);
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();

		if (!AppConfig.sharedInstance().CurrentPhone.equals("anonymous")) {
			phoneNumberEt.setText(AppConfig.sharedInstance().CurrentPhone);
		}
	}

	private void findViews() {
		registerBtn = (TextView) findViewById(R.id.register_btn);
		phoneNumberEt = (EditText) findViewById(R.id.login_phone_number);
		pwdEt = (EditText) findViewById(R.id.login_password);
		forgetPwd = (TextView) findViewById(R.id.login_forget_pwd);
		loginBtn = (TextView) findViewById(R.id.login_ok_btn);
		thirdPartyQQ = (ImageView) findViewById(R.id.login_third_party_qq);
		thirdPartyWeixin = (ImageView) findViewById(R.id.login_third_party_weixin);
	}

	private void initListeners() {
		registerBtn.setOnClickListener(this);
		forgetPwd.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		thirdPartyQQ.setOnClickListener(this);
		thirdPartyWeixin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn: {
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.login_forget_pwd: {
			Intent intent = new Intent(this, ForgetPasswordActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.login_ok_btn:
			String phoneNumber = phoneNumberEt.getText().toString();
			String password = pwdEt.getText().toString();

			if (StringUtil.isNullOrEmpty(phoneNumber)) {
				showToast("请输入手机号!");
				return;
			}
			if (StringUtil.isNullOrEmpty(password)) {
				showToast("请输入密码!");
				return;
			}

			login(phoneNumber, password);
			break;
		case R.id.login_third_party_qq:

			break;
		case R.id.login_third_party_weixin:

			break;
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
				JSONObject json;
				try {
					json = responseJson.getJSONObject("Detail");
					AppConfig config = AppConfig.sharedInstance();
					config.CurrentUID = json.getInt("UserID");
					config.Token = json.getString("Token");
					config.saveToPreference();
				} catch (JSONException e) {
					showToast("数据解析错误!");
				}
				setResult(1);
				finish();
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 发送登录请求
	 */
	public void login(String phoneNumber, String password) {
		String requestBody = String.format(
				"{\"MobilePhone\":\"%s\",\"Password\":\"%s\",\"DeviceRegID\":\"\",\"ClientName\":\"android\"}",
				phoneNumber, password);// 请求参数
		WebDataRequest.requestPost(0, handler, "sso/login", requestBody);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			if(isFinish){
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
