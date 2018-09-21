package com.edonesoft.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected List<T> dataList;
	protected LayoutInflater inflater;
	protected Activity context;

	public BaseListAdapter(Activity context) {
		super();
		this.context = context;
		init();
	}

	private void init() {
		dataList = new ArrayList<T>();
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void clearDataList() {
		dataList.clear();
	}
}
