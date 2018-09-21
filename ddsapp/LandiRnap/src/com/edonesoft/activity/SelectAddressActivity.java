package com.edonesoft.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.adapter.SelectAddressListAdapter;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.AddressItem;
import com.edonesoft.views.PinnedSectionListView;

public class SelectAddressActivity extends BaseActivity implements OnItemClickListener {
	private PinnedSectionListView listView;
	private List<AddressItem> addressItems;
	private SelectAddressListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address);
		addressItems = new ArrayList<AddressItem>();
		initViews();
	}

	private void initViews() {
		listView = (PinnedSectionListView) findViewById(R.id.select_address_listview);
		listAdapter = new SelectAddressListAdapter(this);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);

		getAddressListData();
	}

	private void getAddressListData() {
		try {
			InputStream is = getAssets().open("json/select_address_json.txt");
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(reader);
			StringBuffer buffer = new StringBuffer("");
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
				buffer.append("\n");
			}
			addressItems = JSON.parseObject(buffer.toString(), new TypeReference<List<AddressItem>>() {
			});
			listAdapter.getDataList().addAll(addressItems);
			listAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AddressItem addressItem = addressItems.get(position);
		if (addressItem.type == 0) {
			Intent data = new Intent();
			data.putExtra("address", addressItem);
			setResult(1, data);
			finish();
		}
	}
}
