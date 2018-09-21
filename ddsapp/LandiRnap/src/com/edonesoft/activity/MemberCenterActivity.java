package com.edonesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;

public class MemberCenterActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout profileRellay;
	private RelativeLayout myPhotoRellay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_center);
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
	}

	private void findViews() {
		profileRellay = (RelativeLayout) findViewById(R.id.membercenter_profile_rellay);
		myPhotoRellay = (RelativeLayout) findViewById(R.id.membercenter_myphoto_rellay);
	}

	private void initListeners() {
		profileRellay.setOnClickListener(this);
		myPhotoRellay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.membercenter_profile_rellay: {
			Intent intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.membercenter_myphoto_rellay: {
			Intent intent = new Intent(this, HistoryPhotoActivity.class);
			startActivity(intent);
		}
			break;
		}
	}
}
