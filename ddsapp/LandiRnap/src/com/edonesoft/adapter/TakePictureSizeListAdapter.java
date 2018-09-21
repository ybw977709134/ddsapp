package com.edonesoft.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.PhotoFormat;

public class TakePictureSizeListAdapter extends BaseListAdapter<PhotoFormat> {
	private int selectIndex = 0;

	public TakePictureSizeListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_take_pic_size, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.take_pic_size_text);
		text.setText(dataList.get(position).getName());
		if (position == selectIndex) {
			convertView.setSelected(true);
		} else {
			convertView.setSelected(false);
		}
		return convertView;
	}

	public void setSelectIndex(int i) {
		selectIndex = i;
	}

	public int getSelectIndex() {
		return selectIndex;
	}
}
