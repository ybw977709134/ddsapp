package com.edonesoft.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.edonesoft.base.BaseDialog;
import com.edonesoft.landi.rnap.activity.R;

public class ForgetPwdSuccessDialog extends BaseDialog {

	public ForgetPwdSuccessDialog(Context context) {
		super(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_forget_pwd_success);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {

	}
}
