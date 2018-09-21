package com.edonesoft.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Advertisement;
import com.edonesoft.model.AffairListInfo;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.adapter.AdvertisementAdapter;
import com.edonesoft.adapter.NewAffairListAdapter;
import com.edonesoft.adapter.PublicWorkAdapter;
import com.edonesoft.base.BaseActivity;
import com.viewpagerindicator.CirclePageIndicator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class SelectAffairActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private ImageButton searchWorkBtn;
	private RelativeLayout allRellay;
	private ViewPager viewPager;
	private CirclePageIndicator pageIndicator;
	private ListView newWorkListView;
	private GridView publicWorkGridView;
	private TextView allNewAffairBtn;

	private List<Advertisement> advertisementList;
	private List<AffairListInfo> affairList;
	private List<AffairListInfo> publicAffairList;
	private NewAffairListAdapter newAffairListAdapter;
	private AdvertisementAdapter advertisementAdapter;
	private PublicWorkAdapter publicWorkAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_work);
		advertisementList = new ArrayList<Advertisement>();
		affairList = new ArrayList<AffairListInfo>();
		publicAffairList = new ArrayList<AffairListInfo>();
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		getViewPagerImage();
		getPublicAffairData();
		getNewWorkData();
	}

	private void findViews() {
		searchWorkBtn = (ImageButton) findViewById(R.id.search_btn);
		allRellay = (RelativeLayout) findViewById(R.id.select_work_all_rellay);
		viewPager = (ViewPager) findViewById(R.id.select_work_viewpager);
		pageIndicator = (CirclePageIndicator) findViewById(R.id.select_work_viewpager_indicator);
		newWorkListView = (ListView) findViewById(R.id.select_work_listview);
		publicWorkGridView = (GridView) findViewById(R.id.select_public_work_gridview);
		allNewAffairBtn = (TextView) findViewById(R.id.select_allwork_newtext);
	}

	private void initListeners() {
		allRellay.setOnClickListener(this);
		searchWorkBtn.setOnClickListener(this);
		newWorkListView.setOnItemClickListener(this);
		publicWorkGridView.setOnItemClickListener(this);
		allNewAffairBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_btn: {
			Intent intent = new Intent(this, SearchAffairsActivity.class);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.select_work_all_rellay: {
			Intent intent = new Intent(this, ShowAllAffairActivity.class);
			intent.putExtra("isPublic", 1);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.select_allwork_newtext: {
			Intent intent = new Intent(this, ShowAllAffairActivity.class);
			intent.putExtra("isPublic", 0);
			startActivityForResult(intent, 0);
		}
			break;
		}
	}

	private void getPublicAffairData() {
		WebDataRequest.requestGet(2, handler, "affair/list?page_size=20&page_index=0&keywords=&is_public=1");
	}

	private void getNewWorkData() {
		WebDataRequest.requestGet(0, handler, "affair/list?page_size=20&page_index=0&keywords=&is_public=0");
	}

	private void getViewPagerImage() {
		WebDataRequest.requestGet(1, handler, "system/advertisement/list?position=10000");
	}

	private void initViewPager() {
		advertisementAdapter = new AdvertisementAdapter(this, advertisementList);
		viewPager.setAdapter(advertisementAdapter);
		if(advertisementAdapter.getCount()>1){
			pageIndicator.setViewPager(viewPager);
		}
	}

	private void initAffairListView() {
		newAffairListAdapter = new NewAffairListAdapter(this);
		newAffairListAdapter.getDataList().addAll(affairList);
		newWorkListView.setAdapter(newAffairListAdapter);
		setListViewHeightBasedOnChildren(newWorkListView);
	}

	private void initPublicAffairGridview() {
		publicWorkAdapter = new PublicWorkAdapter(this);
		publicWorkAdapter.getDataList().addAll(publicAffairList);
		publicWorkGridView.setAdapter(publicWorkAdapter);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
				switch (msg.arg1) {
				case 1:
					List<Advertisement> advertisements = JSON.parseObject(responseJson.optString("Detail"),
							new TypeReference<List<Advertisement>>() {
							});
					if (advertisements != null) {
						advertisementList.addAll(advertisements);
						initViewPager();
					}
					break;
				case 0: {
					List<AffairListInfo> affairs = JSON.parseObject(responseJson.optString("Detail"),
							new TypeReference<List<AffairListInfo>>() {
							});
					if (affairs != null) {
						if (affairs.size() == 0) {
							showToast("没有搜索到新事务!");
						} else {
							affairList.addAll(affairs);
							initAffairListView();
						}
					}
				}
					break;
				case 2: {
					List<AffairListInfo> affairs = JSON.parseObject(responseJson.optString("Detail"),
							new TypeReference<List<AffairListInfo>>() {
							});
					if (affairs != null) {
						if (affairs.size() == 0) {
							showToast("没有搜索到公共事务!");
						} else {
							publicAffairList.addAll(affairs);
							initPublicAffairGridview();
						}
					}
				}
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AffairListInfo affair = null;
		if (parent.getId() == R.id.select_public_work_gridview) {
			affair = publicAffairList.get(position);
		} else if (parent.getId() == R.id.select_work_listview) {
			affair = affairList.get(position);
		}
		Intent intent = new Intent(this, AffairDetailActivity.class);
		intent.putExtra("affairId", affair.getItemID());
		startActivityForResult(intent, 0);
	}

	@SuppressWarnings("deprecation")
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = listAdapter.getCount();
		for (int i = 0; i < count; i++) {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			View listItem = listAdapter.getView(i, null, listView);
			listItem.setLayoutParams(layoutParams);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
