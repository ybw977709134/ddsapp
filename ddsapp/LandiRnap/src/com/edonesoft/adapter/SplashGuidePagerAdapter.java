package com.edonesoft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class SplashGuidePagerAdapter extends PagerAdapter {
	private List<View> views = new ArrayList<View>();

	public SplashGuidePagerAdapter(List<View> views) {
		super();
		this.views = views;
	}

	// 销毁position位置的界面
	@Override
	public void destroyItem(ViewGroup v, int position, Object arg2) {
		v.removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position), 0);
		return views.get(position);
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}
