package com.edonesoft.fragment;

import com.edonesoft.activity.ForgetPasswordActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.DensityUtil;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class ForgetPwdFragment2 extends Fragment {
	private ForgetPasswordActivity mActivity;
	private String phoneNumber;
	private int countdownNum = 60;

	private EditText phoneNumberEt;
	private EditText verCodeEt;
	private TextView sendVercodeBtn;
	private TextView nextBtn;
	private TextView countDownTv;
	private RelativeLayout countDownRellay;

	public ForgetPwdFragment2() {
		super();
	}

	public ForgetPwdFragment2(ForgetPasswordActivity mActivity, String phoneNumber) {
		super();
		this.mActivity = mActivity;
		this.phoneNumber = phoneNumber;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forget_pwd2, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		phoneNumberEt = (EditText) view.findViewById(R.id.forget_pwd_phone_number);
		verCodeEt = (EditText) view.findViewById(R.id.forget_pwd_vercode);
		sendVercodeBtn = (TextView) view.findViewById(R.id.forget_pwd_send_vercode);
		nextBtn = (TextView) view.findViewById(R.id.forget_pwd_next);
		countDownTv = (TextView) view.findViewById(R.id.forget_pwd_countdown);
		countDownRellay = (RelativeLayout) view.findViewById(R.id.forget_pwd_countdown_rellay);

		sendVercodeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendVerCode();
			}
		});
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkVerCode();
			}
		});
		phoneNumberEt.setText(phoneNumber);
		sendCountDownMessage();
	}

	private void checkVerCode() {
		String verCode = verCodeEt.getText().toString();
		if (StringUtil.isNullOrEmpty(phoneNumber)) {
			mActivity.showToast("请输入手机号！");
			return;
		}
		if (StringUtil.isNullOrEmpty(verCode)) {
			mActivity.showToast("请输入验证码！");
			return;
		}
		WebDataRequest.requestGet(0, handler, "system/vcode/verify/sms?mp=" + phoneNumber + "&vcode=" + verCode);
	}

	private void sendVerCode() {
		requestSendVerCode();
		sendCountDownMessage();
	}

	private void sendCountDownMessage() {
		sendVercodeBtn.setText("重新获取");
		sendVercodeBtn.setTextColor(getResources().getColor(R.color.line_color));
		sendVercodeBtn.setGravity(Gravity.CENTER_VERTICAL);
		sendVercodeBtn.setPadding(DensityUtil.dip2px(mActivity, 22), 0, 0, 0);
		sendVercodeBtn.setEnabled(false);
		countDownRellay.setVisibility(View.VISIBLE);
		countDownTv.setText(countdownNum + "");
		Message message = new Message();
		message.arg1 = 1;
		handler.sendMessageDelayed(message, 1000);
	}

	private void requestSendVerCode() {
		WebDataRequest.requestGet(0, handler, "system/vcode/send/sms?mp=" + phoneNumber);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				if (WebDataRequestHelper.validateJsonResponse(msg)) {
					mActivity.writePwd(phoneNumber, verCodeEt.getText().toString());
				}
				break;
			case 1:
				countdownNum--;
				if (countdownNum == 0) {
					countdownNum = 60;
					sendVercodeBtn.setGravity(Gravity.CENTER);
					sendVercodeBtn.setPadding(0, 0, 0, 0);
					sendVercodeBtn.setText("重新获取验证码");
					sendVercodeBtn.setTextColor(getResources().getColor(R.color.login_btn_bg));
					sendVercodeBtn.setEnabled(true);
					countDownRellay.setVisibility(View.GONE);
					return;
				}
				countDownTv.setText(countdownNum + "");
				Message message = new Message();
				message.arg1 = 1;
				this.sendMessageDelayed(message, 1000);
				break;
			}
			super.handleMessage(msg);
		}
	};
}
