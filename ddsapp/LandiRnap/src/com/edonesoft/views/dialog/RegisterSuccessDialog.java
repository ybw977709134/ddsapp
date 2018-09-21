package com.edonesoft.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.edonesoft.base.BaseDialog;
import com.edonesoft.landi.rnap.activity.R;

public class RegisterSuccessDialog extends BaseDialog {
	private TextView backBtn;
	private TextView writeProfileBtn;

	public RegisterSuccessDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_register_success);
		init();
	}

	private void init() {
		backBtn = (TextView) findViewById(R.id.dialog_register_success_back);
		writeProfileBtn = (TextView) findViewById(R.id.dialog_register_success_profile);
		backBtn.setOnClickListener(this);
		writeProfileBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_register_success_back:
			mCancelClickListener.onClick(this);
			break;
		case R.id.dialog_register_success_profile:
			mConfirmClickListener.onClick(this);
			break;
		}
	}
}
