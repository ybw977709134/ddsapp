package com.edonesoft.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edonesoft.base.BaseDialog;
import com.edonesoft.landi.rnap.activity.R;

public class SuccessDialog extends BaseDialog {
	private String successText;
	private boolean isSuccess;

	public SuccessDialog(Context context, String text, boolean isSuccess) {
		super(context);
		this.successText = text;
		this.isSuccess = isSuccess;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_forget_pwd_success);
		setCancelable(true);
		setCanceledOnTouchOutside(true);

		TextView successTv = (TextView) findViewById(R.id.dialog_success_text);
		successTv.setText(successText);
		ImageView icon = (ImageView) findViewById(R.id.dialog_success_image);
		if (!isSuccess) {
			icon.setImageResource(R.drawable.fail_img);
		}
	}

	@Override
	public void onClick(View v) {

	}
}
