package com.edonesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;

public class SelectTakePicMethodActivity extends BaseActivity implements OnClickListener {
	private ImageView takePicMethod1Btn;
	private ImageView takePicMethod2Btn;
	private ImageView takePicMethod3Btn;
	private String photographCode;
	private int affairId;
	private String affairName;
	private String channelAccepted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_takepic_method);
		photographCode = getIntent().getStringExtra("photographCode");
		affairId = getIntent().getIntExtra("affairId", 0);
		channelAccepted = getIntent().getStringExtra("channelAccepted");
		affairName = getIntent().getStringExtra("affairName");
		initViews();
	}

	private void initViews() {
		takePicMethod1Btn = (ImageView) findViewById(R.id.take_pic_method1);
		takePicMethod2Btn = (ImageView) findViewById(R.id.take_pic_method2);
		takePicMethod3Btn = (ImageView) findViewById(R.id.take_pic_method3);
		takePicMethod1Btn.setOnClickListener(this);
		takePicMethod2Btn.setOnClickListener(this);
		takePicMethod3Btn.setOnClickListener(this);

		if (!channelAccepted.contains("1")) {
			takePicMethod1Btn.setEnabled(false);
		}
		if (!channelAccepted.contains("2")) {
			takePicMethod2Btn.setEnabled(false);
		}
		if (!channelAccepted.contains("3")) {
			takePicMethod3Btn.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_pic_method1: {
			Intent intent = new Intent(this, TakePictureActivity.class);
			intent.putExtra("photographCode", photographCode);
			intent.putExtra("affairId", affairId);
			intent.putExtra("affairName", affairName);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.take_pic_method2: {
			Intent intent = new Intent(this, SearchStationActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.take_pic_method3: {
			Intent intent = new Intent(this, HistoryPhotoActivity.class);
			intent.putExtra("isSelectPhoto", true);
			startActivityForResult(intent, 0);
		}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			setResult(1);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
