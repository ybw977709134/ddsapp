package com.edonesoft.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.edonesoft.base.BaseDialog;
import com.edonesoft.landi.rnap.activity.R;

public class CommenDialog extends BaseDialog {
	private String titleText;
	private String contentText;
	private TextView titleTv;
	private TextView contentTv;

	public CommenDialog(Context context) {
		super(context);
	}

	public void setTitleText(String title) {
		this.titleText = title;
	}

	public void setContentText(String content) {
		this.contentText = content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_commen);
		TextView okBtn = (TextView) findViewById(R.id.commen_dialog_right_btn);
		TextView cancelBtn = (TextView) findViewById(R.id.commen_dialog_left_btn);
		titleTv = (TextView) findViewById(R.id.commen_dialog_title);
		contentTv = (TextView) findViewById(R.id.commen_dialog_content);
		titleTv.setText(titleText);
		contentTv.setText(contentText);

		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.commen_dialog_right_btn) {
			mConfirmClickListener.onClick(this);
		} else if (v.getId() == R.id.commen_dialog_left_btn) {
			mCancelClickListener.onClick(this);
		}
	}
}
