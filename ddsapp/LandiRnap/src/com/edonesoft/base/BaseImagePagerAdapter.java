package com.edonesoft.base;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BaseImagePagerAdapter<T> extends PagerAdapter {
	public List<ImageView> viewList = new ArrayList<ImageView>();
	public List<T> dataList = null;
	public int count = 0;
	public ImageLoader imageLoader;
	public DisplayImageOptions options;

	public BaseImagePagerAdapter(Context context, List<T> dataList) {
		super();
		this.dataList = dataList;
		if (dataList != null) {
			this.count = dataList.size();
			for (int i = 0; i < count; i++) {
				ImageView imageView = new ImageView(context);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				viewList.add(imageView);
			}
		}
		initImageLoader();
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(null).showImageForEmptyUri(null)
				.showImageOnFail(null).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.build();
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewList.get(position));
	}

	public List<T> getDataList() {
		return dataList;
	}
}
