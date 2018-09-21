package com.edonesoft.base;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	public TextView title;
	public Button rightBtn;
	public ImageButton leftBtn;

	public void leftClicked(View view) {
		finish();
	}

	public void showToast(String msgStr) {
		Toast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
	}
}
