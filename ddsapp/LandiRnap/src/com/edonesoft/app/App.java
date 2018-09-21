package com.edonesoft.app;

import java.io.File;

import com.edonesoft.utils.FileUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import android.app.Application;
import android.content.Context;

public class App extends Application {

	public static Context appContext;
	public static boolean isAppForeground;

	private static App mInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();
		isAppForeground = true;
		mInstance = this;
		initImageLoader(getApplicationContext());
	}

	public static App getInstance() {
		return mInstance;
	}

	/**
	 * 初始化一下 读取图片的类
	 */
	@SuppressWarnings("deprecation")
	public static void initImageLoader(Context context) {
		File cacheDir = new File(FileUtil.getDataPath() + "imageCache/");

		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.memoryCacheExtraOptions(480, 800);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 用MD5保存图片名称
		config.diskCacheSize(30 * 1024 * 1024); // 30 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs();
		config.threadPoolSize(4);// 线程池大小
		config.discCache(new UnlimitedDiscCache(cacheDir));

		ImageLoader.getInstance().init(config.build());
	}
}
