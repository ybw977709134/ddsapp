package com.edonesoft.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.edonesoft.base.BaseDialog;
import com.edonesoft.landi.rnap.activity.R;

public class DeleteDialog extends BaseDialog {

	public DeleteDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_delete);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		TextView leftBtn = (TextView) findViewById(R.id.dialog_delete_left_btn);
		TextView rightBtn = (TextView) findViewById(R.id.dialog_delete_right_btn);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.dialog_delete_left_btn) {
			mConfirmClickListener.onClick(this);
		}
		if (v.getId() == R.id.dialog_delete_right_btn) {
			mCancelClickListener.onClick(this);
		}
	}
}
