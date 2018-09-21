package com.edonesoft.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint("HandlerLeak")
public class FreebackActivity extends BaseActivity {
	// private EditText freebackTitle;
	private EditText freebackContent;
	private TextView sendBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freeback);
		initViews();
	}

	private void initViews() {
		freebackContent = (EditText) findViewById(R.id.freeback_content);
		// freebackTitle = (EditText) findViewById(R.id.freeback_title);
		sendBtn = (TextView) findViewById(R.id.send_btn);

		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = freebackContent.getText().toString();

				if (StringUtil.isNullOrEmpty(content)) {
					showToast("请输入反馈内容！");
				}
				WebDataRequest.requestPost(0, handler, "system/advice/submit", "{\"AdviceContent\":\"" + content
						+ "\",\"AdviceTitle\":\"\"}");
			}
		});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				showToast("反馈成功!");
				finish();
			}
			super.handleMessage(msg);
		}
	};
}
