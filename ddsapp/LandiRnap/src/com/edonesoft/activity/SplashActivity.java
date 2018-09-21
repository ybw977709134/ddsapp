package com.edonesoft.activity;

import java.util.ArrayList;
import java.util.List;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.adapter.SplashGuidePagerAdapter;
import com.edonesoft.app.AppConfig;
import com.viewpagerindicator.CirclePageIndicator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends Activity implements OnClickListener {
	private ViewPager splashViewPager;
	private CirclePageIndicator viewPagerIndicator;
	private TextView jumpTv;
	private List<View> viewList;
	private TextView nextBtn;

	private boolean isJumpFromAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isJumpFromAbout = getIntent().getBooleanExtra("isJumpFromAbout", false);
		if (AppConfig.sharedInstance().isFirstLoad() || isJumpFromAbout) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// 设置全屏
			getWindow()
					.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.activity_splash);
			initViews();
		} else {
			jumpToMain();
		}
	}

	private void initViews() {
		splashViewPager = (ViewPager) findViewById(R.id.splash_viewpager);
		viewPagerIndicator = (CirclePageIndicator) findViewById(R.id.splash_viewpager_indicator);
		jumpTv = (TextView) findViewById(R.id.splash_jump);

		splashViewPager.setVisibility(View.VISIBLE);
		viewPagerIndicator.setVisibility(View.VISIBLE);

		viewList = new ArrayList<View>();
		viewList.add(LayoutInflater.from(this).inflate(R.layout.splash_page1, null));
		viewList.add(LayoutInflater.from(this).inflate(R.layout.splash_page2, null));
		View splashView3 = LayoutInflater.from(this).inflate(R.layout.splash_page3, null);
		nextBtn = (TextView) splashView3.findViewById(R.id.next_btn);
		nextBtn.setVisibility(View.GONE);
		viewList.add(splashView3);
		SplashGuidePagerAdapter pagerAdapter = new SplashGuidePagerAdapter(viewList);
		splashViewPager.setAdapter(pagerAdapter);
		viewPagerIndicator.setViewPager(splashViewPager);

		nextBtn.setVisibility(View.VISIBLE);
		jumpTv.setVisibility(View.VISIBLE);
		jumpTv.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.splash_jump || v.getId() == R.id.next_btn) {
			if (isJumpFromAbout) {
				finish();
			} else {
				jumpToMain();
			}
		}
	}

	private void jumpToMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
		AppConfig.sharedInstance().IsFirstLoad = false;
		AppConfig.sharedInstance().saveToPreference();
	}
}
