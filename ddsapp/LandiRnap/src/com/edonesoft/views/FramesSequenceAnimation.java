package com.edonesoft.views;

import java.io.InputStream;
import java.lang.ref.SoftReference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

public class FramesSequenceAnimation {

	private int[] animationFrames; // animation frames
	private int currentFrames; // current frame
	private boolean shouldRun; // true if the animation should continue running.
								// Used to stop the animation
	private boolean isRunning; // true if the animation currently running.
								// prevents starting the animation twice
	private SoftReference<ImageView> mSoftReferenceImageView; // Used to prevent
																// holding
																// ImageView
																// when it
																// should be
																// dead.
	private Handler handler;
	private int delayMillis; // the gap of frames
	private int framesResourceID;
	private boolean oneShot = false;
	private Context context;
	private FramesSequenceAnimationListener onAnimationStoppedListener;
	private Bitmap bitmap = null;
	private BitmapFactory.Options bitmapOptions;
	private int fps;
	private ImageView imageView;

	public FramesSequenceAnimation(Context context, ImageView imageView, int framesResourceID, int fps) {
		this.context = context;
		this.framesResourceID = framesResourceID;
		this.imageView = imageView;
		this.fps = fps;
		initAnimation();
	}

	public void initAnimation() {
		currentFrames = -1;
		shouldRun = false;
		isRunning = false;
		delayMillis = 1000 / fps;
		mSoftReferenceImageView = new SoftReference<ImageView>(imageView);
		handler = new Handler();
		getFramesResource();
		imageView.setImageResource(animationFrames[0]);

		bitmapOptions = newOptions();
		bitmap = decode(bitmapOptions, getNext());
		bitmapOptions.inBitmap = bitmap;
	}

	@SuppressWarnings("deprecation")
	private BitmapFactory.Options newOptions() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inMutable = true;
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		return options;
	}

	private Bitmap decode(BitmapFactory.Options options, int imageRes) {
		return BitmapFactory.decodeResource(mSoftReferenceImageView.get().getResources(), imageRes, bitmapOptions);
	}

	/**
	 * set animation oneshot
	 * 
	 * @param oneShot
	 */
	public void setOneShot(boolean oneShot) {
		this.oneShot = oneShot;
	}

	/**
	 * get next frame
	 * 
	 * @return
	 */
	private int getNext() {
		currentFrames++;
		if (currentFrames >= animationFrames.length) {
			if (oneShot) {
				shouldRun = false;
				currentFrames = animationFrames.length - 1;
				
			} else {
				currentFrames = 0;
			}
		}
		return animationFrames[currentFrames];
	}

	/**
	 * Starts the animation
	 */
	public synchronized void start() {
		shouldRun = true;
		if (isRunning) {
			return;
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				ImageView imageView = mSoftReferenceImageView.get();
				if (!shouldRun || imageView == null) {
					isRunning = false;
					if (onAnimationStoppedListener != null) {
						onAnimationStoppedListener.AnimationStopped();
					}
					return;
				}

				isRunning = true;
				handler.postDelayed(this, delayMillis);

				if (imageView.isShown()) {
					int imageRes = getNext();
					if (bitmap != null) {
						try {
							InputStream is = imageView.getResources().openRawResource(imageRes);
							bitmap = BitmapFactory.decodeStream(is, null, bitmapOptions);
						} catch (Exception e) {
							e.printStackTrace();
							bitmap.recycle();
							bitmap = null;
						}
						if (bitmap != null) {
							imageView.setImageBitmap(bitmap);
						} else {
							imageView.setImageResource(imageRes);
							bitmap.recycle();
							bitmap = null;
						}
					} else {
						imageView.setImageResource(imageRes);
					}
				}

			}
		};

		handler.post(runnable);
	}

	/**
	 * Stops the animation
	 */
	public synchronized void stop() {
		shouldRun = false;
	}

	private void getFramesResource() {
		TypedArray typedArray = context.getResources().obtainTypedArray(framesResourceID);
		int longth = typedArray.length();
		animationFrames = new int[longth];
		for (int index = 0; index < longth; index++) {
			int feedResId = typedArray.getResourceId(index, 0);
			animationFrames[index] = feedResId;
		}
		typedArray.recycle();
	}

	public void setFramesSequenceAnimationListener(FramesSequenceAnimationListener onAnimationStoppedListener) {
		this.onAnimationStoppedListener = onAnimationStoppedListener;
	}

	public interface FramesSequenceAnimationListener {
		public void AnimationStopped();

		public void AnimationStarted();
	}

}
