package com.edonesoft.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.adapter.AffairTextAdapter;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.AffairListInfo;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint("HandlerLeak")
public class ShowAllAffairActivity extends BaseActivity implements OnItemClickListener {
	private List<AffairListInfo> affairList;
	private int isPublic;

	private ListView listview;
	private AffairTextAdapter textAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_all_affair);
		isPublic = getIntent().getIntExtra("isPublic", 0);
		affairList = new ArrayList<AffairListInfo>();
		initViews();
	}

	private void initViews() {
		listview = (ListView) findViewById(R.id.show_all_affair_list);
		textAdapter = new AffairTextAdapter(this);
		listview.setAdapter(textAdapter);
		listview.setOnItemClickListener(this);
		WebDataRequest.requestGet(0, handler, "affair/list?page_size=50&page_index=0&keywords=&is_public=" + isPublic);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
				List<AffairListInfo> affairs = JSON.parseObject(responseJson.optString("Detail"),
						new TypeReference<List<AffairListInfo>>() {
						});
				if (affairs != null) {
					if (affairs.size() == 0) {
						showToast("没有搜索到事务!");
					} else {
						affairList.addAll(affairs);
						initAffairListView();
					}
				}
			}
			super.handleMessage(msg);
		}
	};

	private void initAffairListView() {
		textAdapter.getDataList().addAll(affairList);
		textAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, AffairDetailActivity.class);
		intent.putExtra("affairId", affairList.get(position).getItemID());
		startActivityForResult(intent, 0);
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
