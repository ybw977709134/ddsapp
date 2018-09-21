package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Bulletin;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint({ "HandlerLeak", "SetJavaScriptEnabled" })
public class AffairKnowledgeActivity extends BaseActivity {
	private WebView webView;
	private Bulletin bulletin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affair_knowledge);
		initViews();
	}

	private void initViews() {
		webView = (WebView) findViewById(R.id.affair_knowledge_webview);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		WebDataRequest.requestGet(0, handler, "system/bulletin/detail?tag=办证知识");
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
						initWebViewData();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			super.handleMessage(msg);
		}
	};

	private void initWebViewData() {
		webView.loadData(bulletin.getRichtext_content(), "text/html; charset=UTF-8", null);
	}
}
