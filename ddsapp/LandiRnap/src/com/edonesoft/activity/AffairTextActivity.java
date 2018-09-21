package com.edonesoft.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;

public class AffairTextActivity extends BaseActivity {
	private TextView titleTv;
	private TextView infoTv;

	private String title;
	private String info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		title = getIntent().getStringExtra("title");
		info = getIntent().getStringExtra("info");
		setContentView(R.layout.activity_affair_text);
		initViews();
	}

	private void initViews() {
		titleTv = (TextView) findViewById(R.id.affair_text_title);
		infoTv = (TextView) findViewById(R.id.affair_text_info);
		titleTv.setText(title);
		infoTv.setText(info);
	}
}
