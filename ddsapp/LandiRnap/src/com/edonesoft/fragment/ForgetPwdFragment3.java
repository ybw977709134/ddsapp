package com.edonesoft.fragment;

import com.edonesoft.activity.ForgetPasswordActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.SuccessDialog;

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
public class ForgetPwdFragment3 extends Fragment {
	private ForgetPasswordActivity mActivity;
	private EditText newPwdEt;
	private EditText rePwdEt;
	private TextView commitBtn;

	private String phoneNumber;
	private String verCode;

	public ForgetPwdFragment3() {
		super();
	}

	public ForgetPwdFragment3(ForgetPasswordActivity mActivity, String phoneNumber, String verCode) {
		super();
		this.mActivity = mActivity;
		this.phoneNumber = phoneNumber;
		this.verCode = verCode;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forget_pwd3, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		newPwdEt = (EditText) view.findViewById(R.id.forget_pwd_newpwd);
		rePwdEt = (EditText) view.findViewById(R.id.forget_pwd_repwd);
		commitBtn = (TextView) view.findViewById(R.id.forget_pwd_commit);

		commitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commitNewPwd();
			}
		});
	}

	private void commitNewPwd() {
		String newPwd = newPwdEt.getText().toString();
		String rePwd = rePwdEt.getText().toString();

		if (StringUtil.isNullOrEmpty(newPwd)) {
			mActivity.showToast("请输入新密码！");
			return;
		}
		if (StringUtil.isNullOrEmpty(rePwd)) {
			mActivity.showToast("请输入确认密码！");
			return;
		}
		if (!newPwd.equals(rePwd)) {
			mActivity.showToast("两次输入的密码不一致！");
			return;
		}
		if (!newPwd.matches("[0-9A-Za-z]{6,20}$")) {
			mActivity.showToast("请输入正确的密码格式！");
			return;
		}
		WebDataRequest.requestPost(0, handler, "sso/password/reset", "{\"MobilePhone\":\"" + phoneNumber
				+ "\",\"Password\":\"" + newPwd + "\",\"Vcode\":\"" + verCode + "\"}");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				if (WebDataRequestHelper.validateJsonResponse(msg)) {
					commitSuccess();
				}
				break;
			case 1:
				mActivity.finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void commitSuccess() {
		SuccessDialog dialog = new SuccessDialog(mActivity, "设置成功！", true);
		dialog.show();
		Message message = new Message();
		message.arg1 = 1;
		handler.sendMessageDelayed(message, 2000);
	}
}
