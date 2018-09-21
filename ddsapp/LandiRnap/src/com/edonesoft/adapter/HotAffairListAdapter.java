package com.edonesoft.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;

public class HotAffairListAdapter extends BaseListAdapter<String> {

	public HotAffairListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_hot_affair, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.hot_affair_list_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(getDataList().get(position));
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
	}
}
