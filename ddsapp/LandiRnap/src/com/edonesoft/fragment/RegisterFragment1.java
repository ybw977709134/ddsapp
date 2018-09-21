package com.edonesoft.fragment;

import com.edonesoft.activity.RegisterActivity;
import com.edonesoft.activity.SelectAddressActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class RegisterFragment1 extends Fragment implements OnClickListener {
	private RegisterActivity mActivity;

	private TextView loginBtn;
	private TextView reigsterBtn;
	private TextView selectCountryBtn;
	private EditText phoneNumberEt;
	private ImageButton backBtn;
	private TextView countryTv;

	private String phoneNumber;

	public RegisterFragment1() {
		super();
	}

	public RegisterFragment1(RegisterActivity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register1, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		backBtn = (ImageButton) view.findViewById(R.id.back_btn);
		loginBtn = (TextView) view.findViewById(R.id.login_btn);
		reigsterBtn = (TextView) view.findViewById(R.id.register_ok_btn);
		selectCountryBtn = (TextView) view.findViewById(R.id.register_country);
		phoneNumberEt = (EditText) view.findViewById(R.id.register_phone_number);
		countryTv = (TextView) view.findViewById(R.id.register_country_text);
		loginBtn.setOnClickListener(this);
		reigsterBtn.setOnClickListener(this);
		selectCountryBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	private void requestSendVerCode() {
		WebDataRequest.requestGet(0, handler, "system/vcode/send/sms?mp=" + phoneNumber);
	}

	public void setCountry(String country) {
		countryTv.setText(country);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
		case R.id.login_btn:
			mActivity.finish();
			break;
		case R.id.register_country: {
			Intent intent = new Intent(mActivity, SelectAddressActivity.class);
			mActivity.startActivityForResult(intent, 0);
		}
			break;
		case R.id.register_ok_btn:
			phoneNumber = phoneNumberEt.getText().toString();
			if (StringUtil.isNullOrEmpty(phoneNumber)) {
				mActivity.showToast("请输入手机号！");
				return;
			}
			requestSendVerCode();
			break;
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				mActivity.writeVerCode(phoneNumber);
			}
			super.handleMessage(msg);
		}
	};
}
