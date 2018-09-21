package com.edonesoft.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.AddressItem;
import com.edonesoft.utils.DensityUtil;
import com.edonesoft.views.PinnedSectionListView.PinnedSectionListAdapter;

public class SelectAddressListAdapter extends BaseListAdapter<AddressItem> implements PinnedSectionListAdapter {
	public SelectAddressListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_select_address, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.select_address_item_name);
		TextView number = (TextView) convertView.findViewById(R.id.select_address_item_number);
		AddressItem item = getDataList().get(position);
		name.setText(item.getText());
		if (item.type == 1) {
			name.setBackgroundResource(R.color.gray);
			RelativeLayout.LayoutParams params = (LayoutParams) name.getLayoutParams();
			params.height = DensityUtil.dip2px(context, 25);
			name.setLayoutParams(params);
		} else {
			number.setText(item.getNumber());
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return getDataList().get(position).type;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == 1;
	}
}
