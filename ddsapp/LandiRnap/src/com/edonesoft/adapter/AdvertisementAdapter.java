package com.edonesoft.adapter;

import java.util.List;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edonesoft.base.BaseImagePagerAdapter;
import com.edonesoft.model.Advertisement;

public class AdvertisementAdapter extends BaseImagePagerAdapter<Advertisement> {

	public AdvertisementAdapter(Context context, List<Advertisement> dataList) {
		super(context, dataList);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		ImageView imageView = viewList.get(position);
		container.addView(imageView, 0);
		imageLoader.displayImage(dataList.get(position).getImageURL(), imageView, options);
		return imageView;
	}
}
