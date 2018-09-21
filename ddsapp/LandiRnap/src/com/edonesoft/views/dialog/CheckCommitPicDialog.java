package com.edonesoft.views.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.edonesoft.base.BaseDialog;
import com.edonesoft.landi.rnap.activity.R;

public class CheckCommitPicDialog extends BaseDialog {
	private TextView scoreTv;
	private TextView nextBtn;
	private TextView backBtn;
	private TextView contentTv;

	private int score;
	private Context context;

	public CheckCommitPicDialog(Context context, int score) {
		super(context);
		this.score = score;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_check_commit_pic);
		init();
	}

	private void init() {
		scoreTv = (TextView) findViewById(R.id.check_pic_score);
		nextBtn = (TextView) findViewById(R.id.check_pic_next_btn);
		backBtn = (TextView) findViewById(R.id.check_pic_back_btn);
		contentTv = (TextView) findViewById(R.id.check_commit_textview);

		Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansHans-Thin-Windows.otf");
		scoreTv.setTypeface(typeFace);
		scoreTv.setText(score + "");
		if (score == 0) {
			scoreTv.setBackgroundResource(R.drawable.check_pic_score_fail);
			contentTv.setText("因相片质量原因，无法获得比对分值");
			scoreTv.setText("");
		} else if (score < 60) {
			scoreTv.setBackgroundResource(R.drawable.check_pic_score_red_bg);
			scoreTv.setTextColor(context.getResources().getColor(R.color.red));
		}
		nextBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_pic_next_btn:
			mConfirmClickListener.onClick(this);
			break;
		case R.id.check_pic_back_btn:
			mCancelClickListener.onClick(this);
			break;
		}
	}
}
