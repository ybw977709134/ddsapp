package com.edonesoft.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderListInfo;
import com.edonesoft.utils.DateUtil;
import com.edonesoft.views.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderListAdapter extends BaseListAdapter<OrderListInfo> {
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public OrderListAdapter(Activity context) {
		super(context);
		initImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_order_list, null);
			viewHolder = new ViewHolder();
			viewHolder.status = (TextView) convertView.findViewById(R.id.order_list_item_status);
			viewHolder.name = (TextView) convertView.findViewById(R.id.order_list_item_name);
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.order_list_item_icon);
			viewHolder.time = (TextView) convertView.findViewById(R.id.order_list_item_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (getDataList().get(position).getStatus() == 0) {
			viewHolder.icon.setBackgroundResource(R.drawable.order_list_status1);
			viewHolder.status.setText("处理中");
			viewHolder.status.setTextColor(convertView.getResources().getColor(R.color.orange));
		} else if (getDataList().get(position).getStatus() == 20) {
			viewHolder.icon.setBackgroundResource(R.drawable.order_list_stauts2);
			viewHolder.status.setText("已完成");
			viewHolder.status.setTextColor(convertView.getResources().getColor(R.color.green));
		}
		imageLoader.displayImage(getDataList().get(position).getPhotoUrl(), viewHolder.icon, options);
		viewHolder.name.setText(getDataList().get(position).getAffairName());
		viewHolder.time.setText(DateUtil.doubleTimeToString(getDataList().get(position).getCreateDate(),
				"yyyy/MM/dd HH:mm"));

		return convertView;
	}

	private class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView time;
		public TextView status;
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer(0)).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
	}

}
