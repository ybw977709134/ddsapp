package com.edonesoft.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.PhotoFormat;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoFormatGridViewAdapter extends BaseListAdapter<PhotoFormat> {
	public ImageLoader imageLoader;
	public DisplayImageOptions options;

	public PhotoFormatGridViewAdapter(Activity context) {
		super(context);
		initImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_photoformat_gridview, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.select_photoformat_gridview_item_text);
		ImageView icon = (ImageView) convertView.findViewById(R.id.select_photoformat_gridview_item_img);

		PhotoFormat photoFormat = getDataList().get(position);
		name.setText(photoFormat.getName());
		imageLoader.displayImage(photoFormat.getIcon_url(), icon, options);
		return convertView;
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();
	}
}
