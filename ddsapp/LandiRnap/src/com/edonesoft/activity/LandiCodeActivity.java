package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Photo;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint("HandlerLeak")
public class LandiCodeActivity extends BaseActivity {
	private ImageView checkQRCodeBtn;
	private EditText landiCodeEt;
	private TextView commitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landi_code);
		initViews();
	}

	private void initViews() {
		checkQRCodeBtn = (ImageView) findViewById(R.id.check_qrcode);
		landiCodeEt = (EditText) findViewById(R.id.landi_code_edittext);
		commitBtn = (TextView) findViewById(R.id.landi_code_okbtn);

		commitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppConfig.sharedInstance().isUserLogin()) {
					checkLandiCode();
				} else {
					showToast("请在登录后查询蓝证码!");
				}
			}
		});
		checkQRCodeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LandiCodeActivity.this, CheckCodeActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	private void checkLandiCode() {
		String landiCode = landiCodeEt.getText().toString();
		if (StringUtil.isNullOrEmpty(landiCode)) {
			showToast("请输入蓝证码！");
			return;
		}
		WebDataRequest.requestGet(0, handler, "order/paragraphcode/check?code=" + landiCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (data != null) {
				String code = data.getStringExtra("code");
				landiCodeEt.setText(code);
			}
		}
		if (requestCode == 1 && resultCode == 1) {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				Photo photo = null;
				try {
					photo = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
							new TypeReference<Photo>() {
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (photo.getOrder_id() == 0) {
					Intent intent = new Intent(LandiCodeActivity.this, SelectTakePicMethodActivity.class);
					intent.putExtra("photographCode", photo.getPhotograph_code());
					intent.putExtra("affairId", photo.getAffair_id());
					intent.putExtra("channelAccepted", photo.getChannel_accepted());
					intent.putExtra("affairName", photo.getName());
					startActivityForResult(intent, 1);
				} else {
					Intent intent = new Intent(LandiCodeActivity.this, OrderFormActivity.class);
					intent.putExtra("orderId", photo.getOrder_id());
					intent.putExtra("isShowHome", false);
					startActivity(intent);
				}
			}
			super.handleMessage(msg);
		}
	};
}
