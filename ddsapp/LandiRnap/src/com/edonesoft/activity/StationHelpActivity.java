package com.edonesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;

public class StationHelpActivity extends BaseActivity implements OnClickListener {
	private TextView helpListText1;
	private TextView helpListText2;
	private TextView helpListText3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_help);
		initViews();
	}

	private void initViews() {
		helpListText1 = (TextView) findViewById(R.id.help_list_text1);
		helpListText2 = (TextView) findViewById(R.id.help_list_text2);
		helpListText3 = (TextView) findViewById(R.id.help_list_text3);

		helpListText1.setOnClickListener(this);
		helpListText2.setOnClickListener(this);
		helpListText3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, HelpDetailActivity.class);
		switch (v.getId()) {
		case R.id.help_list_text1:
			intent.putExtra("bulletinName", "办事点");
			break;
		case R.id.help_list_text2:
			intent.putExtra("bulletinName", "自助机");
			break;
		case R.id.help_list_text3:
			intent.putExtra("bulletinName", "照相馆");
			break;
		}
		startActivity(intent);
	}
}
