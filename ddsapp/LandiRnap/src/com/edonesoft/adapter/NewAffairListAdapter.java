package com.edonesoft.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.AffairListInfo;

public class NewAffairListAdapter extends BaseListAdapter<AffairListInfo> {

	public NewAffairListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_new_work, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.new_work_title);
			viewHolder.organization = (TextView) convertView.findViewById(R.id.new_work_organization);
			viewHolder.eyes = (TextView) convertView.findViewById(R.id.new_work_eyes);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		AffairListInfo affair = getDataList().get(position);
		viewHolder.title.setText(affair.getName());
		viewHolder.eyes.setText(affair.getMax_customer_count() + "");
		viewHolder.organization.setText(affair.getAffair_intro());
		return convertView;
	}

	private class ViewHolder {
		public TextView title;
		public TextView eyes;
		public TextView organization;
	}
}
