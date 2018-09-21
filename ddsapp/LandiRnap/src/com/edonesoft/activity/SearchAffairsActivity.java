package com.edonesoft.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.adapter.AffairTextAdapter;
import com.edonesoft.adapter.HotAffairListAdapter;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.AffairListInfo;
import com.edonesoft.utils.FileUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;

@SuppressLint("HandlerLeak")
public class SearchAffairsActivity extends BaseActivity implements OnItemClickListener {
	private ScrollView scrollView;
	private GridView hotSearchListView;
	private ListView historySearchListView;
	private ListView edittextSearchListView;
	private EditText searchEditText;

	private List<AffairListInfo> affairList;
	private List<String> hotAffairList;
	private List<AffairListInfo> historySearchList;
	private AffairTextAdapter historySearchListAdapter;
	private AffairTextAdapter edittextSearchListAdapter;
	private HotAffairListAdapter hotAffairListAdapter;
	private String historySearchDataPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_work);
		affairList = new ArrayList<AffairListInfo>();
		hotAffairList = new ArrayList<String>();
		historySearchList = new ArrayList<AffairListInfo>();
		historySearchDataPath = FileUtil.getDataPath() + "imageCache/affairHistoryList.txt";
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();

		edittextSearchListAdapter = new AffairTextAdapter(this);
		edittextSearchListView.setAdapter(edittextSearchListAdapter);
		historySearchListAdapter = new AffairTextAdapter(this);
		historySearchListView.setAdapter(historySearchListAdapter);
		hotAffairListAdapter = new HotAffairListAdapter(this);
		hotSearchListView.setAdapter(hotAffairListAdapter);
		getHotListView();
		initHistoryListView();
	}

	private void findViews() {
		scrollView = (ScrollView) findViewById(R.id.search_work_scrollview);
		searchEditText = (EditText) findViewById(R.id.search_work_keyword);
		historySearchListView = (ListView) findViewById(R.id.history_search_listview);
		hotSearchListView = (GridView) findViewById(R.id.hot_search_listview);
		edittextSearchListView = (ListView) findViewById(R.id.edittext_search_listview);
	}

	private void initListeners() {
		searchEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					onEditActionSearch();
					return true;
				}
				return false;
			}
		});
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String keywords = s.toString();
				if (keywords.length() == 0) {
					scrollView.setVisibility(View.VISIBLE);
					edittextSearchListView.setVisibility(View.GONE);
					initHistoryListView();
					affairList.clear();
					edittextSearchListAdapter.getDataList().clear();
					edittextSearchListAdapter.notifyDataSetChanged();
				}
			}
		});
		historySearchListView.setOnItemClickListener(this);
		edittextSearchListView.setOnItemClickListener(this);
		hotSearchListView.setOnItemClickListener(this);
	}

	private void searchAffairByKeywords(String keywords) {
		WebDataRequest.requestGet(0, handler, "affair/list?page_size=50&page_index=0&keywords=" + keywords
				+ "&is_public=-1");
		for (int i = 0; i < historySearchList.size(); i++) {
			if (historySearchList.get(i).getName().equals(keywords)) {
				return;
			}
		}
		AffairListInfo affair = new AffairListInfo();
		affair.setName(keywords);
		historySearchList.add(affair);
		FileUtil.writeFile(historySearchDataPath, JSON.toJSONString(historySearchList));
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
				switch (msg.arg1) {
				case 0:
					List<AffairListInfo> affairs = JSON.parseObject(responseJson.optString("Detail"),
							new TypeReference<List<AffairListInfo>>() {
							});
					if (affairs != null) {
						if (affairs.size() == 0) {
							showToast("没有搜索到事务!");
						} else {
							affairList.clear();
							affairList.addAll(affairs);
							initAffairListView();
						}
					}
					break;
				case 1:
					hotAffairList = JSON.parseObject(responseJson.optString("Detail"),
							new TypeReference<List<String>>() {
							});
					if (hotAffairList != null && hotAffairList.size() != 0) {
						initHotListView();
					}
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	private void initAffairListView() {
		edittextSearchListAdapter.getDataList().clear();
		edittextSearchListAdapter.getDataList().addAll(affairList);
		edittextSearchListAdapter.notifyDataSetChanged();
	}

	private void initHistoryListView() {
		if (historySearchList.size() == 0) {
			historySearchList.addAll(JSON.parseObject(FileUtil.readerFile(historySearchDataPath),
					new TypeReference<List<AffairListInfo>>() {
					}));
		}
		if (historySearchList.size() != 0) {
			historySearchListAdapter.getDataList().clear();
			historySearchListAdapter.getDataList().addAll(historySearchList);
			historySearchListAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(historySearchListView);
		}
	}

	private void getHotListView() {
		WebDataRequest.requestGet(1, handler, "affair/keyword/list");
	}

	private void initHotListView() {
		hotAffairListAdapter.getDataList().addAll(hotAffairList);
		hotAffairListAdapter.notifyDataSetChanged();
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = listAdapter.getCount();
		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == R.id.edittext_search_listview) {
			Intent intent = new Intent(this, AffairDetailActivity.class);
			intent.putExtra("affairId", affairList.get(position).getItemID());
			startActivityForResult(intent, 0);
		} else if (parent.getId() == R.id.history_search_listview) {
			searchEditText.setText(historySearchList.get(position).getName());
			onEditActionSearch();
		} else if (parent.getId() == R.id.hot_search_listview) {
			searchEditText.setText(hotAffairList.get(position));
			onEditActionSearch();
		}
	}

	private void onEditActionSearch() {
		scrollView.setVisibility(View.GONE);
		edittextSearchListView.setVisibility(View.VISIBLE);
		searchAffairByKeywords(searchEditText.getText().toString());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (searchEditText.getText().toString().length() > 0) {
				searchEditText.setText("");
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
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
