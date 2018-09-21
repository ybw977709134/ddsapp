package com.edonesoft.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edonesoft.base.BaseListAdapter;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Photo;
import com.edonesoft.utils.DateUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HistoryPhotoListAdapter extends BaseListAdapter<Photo> {
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private boolean isList;

	public HistoryPhotoListAdapter(Activity context, boolean isList) {
		super(context);
		this.isList = isList;
		initImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if (isList) {
				convertView = inflater.inflate(R.layout.adapter_item_history_photo_list, null);
			} else {
				convertView = inflater.inflate(R.layout.adapter_item_history_photo_grid, null);
			}
		}
		TextView size = (TextView) convertView.findViewById(R.id.history_photo_size);
		TextView time = (TextView) convertView.findViewById(R.id.history_photo_time);
		TextView score = (TextView) convertView.findViewById(R.id.history_photo_score);
		ImageView image = (ImageView) convertView.findViewById(R.id.history_photo_image);
		ImageView isRecent = (ImageView) convertView.findViewById(R.id.history_photo_is_recent_icon);

		Photo photo = getDataList().get(position);
		imageLoader.displayImage(photo.getPhoto_final(), image, options);
		time.setText(DateUtil.doubleTimeToString(photo.getTaken_date(), "yyyy.MM.dd"));
		if (photo.getValidate_score() != 0) {
			score.setText("比对分值:" + photo.getValidate_score());
		}
		if (photo.isIsRecentPhoto()) {
			isRecent.setVisibility(View.VISIBLE);
		}
		size.setText(photo.getName());
		return convertView;
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}
}
