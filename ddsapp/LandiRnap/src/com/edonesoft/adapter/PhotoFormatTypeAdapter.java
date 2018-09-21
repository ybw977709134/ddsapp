package com.edonesoft.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.PhotoFormat;

public class PhotoFormatTypeAdapter extends BaseListAdapter<PhotoFormat> {
	private int selectIndex = 0;

	public PhotoFormatTypeAdapter(Activity context) {
		super(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_photoformat_type, null);
			viewHolder = new ViewHolder();
			viewHolder.type = (TextView) convertView.findViewById(R.id.select_photoformat_type_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.type.setText(getDataList().get(position).getName());
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

	private class ViewHolder {
		public TextView type;
	}
}
