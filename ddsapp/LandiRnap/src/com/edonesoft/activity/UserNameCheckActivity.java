package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;

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
public class UserNameCheckActivity extends BaseActivity {
	private EditText realNameEt;
	private EditText idCardEt;
	private TextView commitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_check);
		initViews();
	}

	private void initViews() {
		realNameEt = (EditText) findViewById(R.id.name_check_name_text);
		idCardEt = (EditText) findViewById(R.id.name_check_idcard_text);
		commitBtn = (TextView) findViewById(R.id.commit_btn);

		commitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestCheckName();
			}
		});
	}

	private void requestCheckName() {
		String realName = realNameEt.getText().toString();
		String idCard = idCardEt.getText().toString();
		if (StringUtil.isNullOrEmpty(idCard)) {
			showToast("请输入身份证号！");
			return;
		}
		if (StringUtil.isNullOrEmpty(idCard)) {
			showToast("请输入真实姓名！");
			return;
		}
		WebDataRequest.requestPost(0, handler, "order/identify/realname", "{\"IDCard\":\"" + idCard
				+ "\",\"ImageData\":\"\",\"Name\":\"" + realName + "\"}");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
				JSONObject json;
				try {
					json = responseJson.getJSONObject("Detail");
					int isPassed = json.getInt("Is_passed");
					if (isPassed == 1) {
						showToast("认证成功");
					}
					setResult(1);
					finish();
				} catch (JSONException e) {
					showToast("数据解析错误!");
				}
			}
			super.handleMessage(msg);
		}
	};
}
