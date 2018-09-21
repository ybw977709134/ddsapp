package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Bulletin;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint("HandlerLeak")
public class HelpDetailActivity extends BaseActivity {
	private TextView tvTitle;
	private WebView tvContent;
	private String bulletinName;
	private Bulletin bulletin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_detail);
		bulletinName = getIntent().getStringExtra("bulletinName");
		initViews();
	}

	private void initViews() {
		tvTitle = (TextView) findViewById(R.id.title);
		tvContent = (WebView) findViewById(R.id.help_detail_content);

		WebDataRequest.requestGet(0, handler, "system/bulletin/detail?tag=" + bulletinName);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				try {
					bulletin = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
							new TypeReference<Bulletin>() {
							});
					if (bulletin != null) {
						tvTitle.setText(bulletin.getBulletin_title());
						tvContent.loadData("<style>img {width:100%%;}</style>" + bulletin.getRichtext_content(),
								"text/html; charset=UTF-8", null);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			super.handleMessage(msg);
		}
	};
}
