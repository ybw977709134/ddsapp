package com.edonesoft.fragment;

import com.edonesoft.activity.ForgetPasswordActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class ForgetPwdFragment1 extends Fragment {
	private ForgetPasswordActivity mActivity;
	private String phoneNumber;

	private TextView sendVercodeBtn;
	private EditText phoneNumberEt;

	public ForgetPwdFragment1() {
		super();
	}

	public ForgetPwdFragment1(ForgetPasswordActivity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forget_pwd1, container,
				false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		phoneNumberEt = (EditText) view
				.findViewById(R.id.forget_pwd_phone_number);
		sendVercodeBtn = (TextView) view
				.findViewById(R.id.forget_pwd_send_vercode);

		sendVercodeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				phoneNumber = phoneNumberEt.getText().toString();
				if (StringUtil.isNullOrEmpty(phoneNumber)) {
					mActivity.showToast("请输入手机号！");
					return;
				}
				requestSendVerCode();
			}
		});
	}

	private void requestSendVerCode() {
		WebDataRequest.requestGet(0, handler, "system/vcode/send/sms?mp="
				+ phoneNumber);
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
