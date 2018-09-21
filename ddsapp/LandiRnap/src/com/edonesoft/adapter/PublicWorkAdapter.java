package com.edonesoft.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.AffairListInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PublicWorkAdapter extends BaseListAdapter<AffairListInfo> {
	public ImageLoader imageLoader;
	public DisplayImageOptions options;

	public PublicWorkAdapter(Activity context) {
		super(context);
		initImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_public_work, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.public_affair_name);
		TextView company = (TextView) convertView.findViewById(R.id.public_affair_company);
		ImageView icon = (ImageView) convertView.findViewById(R.id.public_affair_icon);

		AffairListInfo affair = getDataList().get(position);
		name.setText(affair.getName());
		imageLoader.displayImage(affair.getIcon_url(), icon, options);
		return convertView;
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.showImageOnLoading(R.drawable.select_work_tab1).showImageForEmptyUri(R.drawable.select_work_tab1)
				.showImageOnFail(R.drawable.select_work_tab1).build();
	}
}
