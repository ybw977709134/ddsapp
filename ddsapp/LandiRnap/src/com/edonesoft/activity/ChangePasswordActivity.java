package com.edonesoft.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.SuccessDialog;

@SuppressLint("HandlerLeak")
public class ChangePasswordActivity extends BaseActivity {
	private EditText newPwdEt;
	private EditText rePwdEt;
	private EditText oldPwdEt;
	private TextView okBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		initViews();
	}

	private void initViews() {
		newPwdEt = (EditText) findViewById(R.id.change_pwd_newpwd);
		rePwdEt = (EditText) findViewById(R.id.change_pwd_repwd);
		oldPwdEt = (EditText) findViewById(R.id.change_pwd_oldpwd);
		okBtn = (TextView) findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commitNewPwd();
			}
		});
	}

	private void commitNewPwd() {
		String newPwd = newPwdEt.getText().toString();
		String rePwd = rePwdEt.getText().toString();
		String oldPwd = oldPwdEt.getText().toString();

		if (StringUtil.isNullOrEmpty(oldPwd)) {
			showToast("请输入旧密码！");
			return;
		}
		if (StringUtil.isNullOrEmpty(newPwd)) {
			showToast("请输入新密码！");
			return;
		}
		if (StringUtil.isNullOrEmpty(rePwd)) {
			showToast("请输入确认密码！");
			return;
		}
		if (!newPwd.equals(rePwd)) {
			showToast("两次输入的密码不一致！");
			return;
		}
		if (!newPwd.matches("[0-9A-Za-z]{6,20}$")) {
			showToast("请输入正确的密码格式！");
			return;
		}
		WebDataRequest.requestPost(0, handler, "sso/password/change", "{\"NewPassword\":\"" + newPwd
				+ "\",\"OldPassword\":\"" + oldPwd + "\"}");
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
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void commitSuccess() {
		SuccessDialog dialog = new SuccessDialog(this, "设置成功！", true);
		dialog.show();
		Message message = new Message();
		message.arg1 = 1;
		handler.sendMessageDelayed(message, 2000);
	}
}
