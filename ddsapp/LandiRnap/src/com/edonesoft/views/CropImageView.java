package com.edonesoft.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by H3c on 12/13/14.
 */
public class CropImageView extends ImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
	private float BORDERD_ISTANCE_WIDTH = 0;
	private float BORDERD_ISTANCE_HEIGHT = 0;

	public static final float DEFAULT_MAX_SCALE = 4.0f;
	public static final float DEFAULT_MID_SCALE = 2.0f;
	public static final float DEFAULT_MIN_SCALE = 0.8f;

	private float minScale = DEFAULT_MIN_SCALE;
	private float midScale = DEFAULT_MID_SCALE;
	private float maxScale = DEFAULT_MAX_SCALE;

	private MultiGestureDetector multiGestureDetector;
	private boolean isIniting;// 正在初始化

	private Matrix defaultMatrix = new Matrix();// 初始化的图片矩阵，控制图片撑满屏幕及显示区域
	private Matrix dragMatrix = new Matrix();// 拖拽放大过程中动态的矩阵
	private Matrix finalMatrix = new Matrix();// 最终显示的矩阵
	private Matrix rotateMatrix = new Matrix();// 最终旋转的矩阵
	private final RectF displayRect = new RectF();// 图片的真实大小
	private final float[] matrixValues = new float[9];

	private int borderWidth;
	private int borderHeight;

	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setScaleType(ScaleType.MATRIX);
		setOnTouchListener(this);
		multiGestureDetector = new MultiGestureDetector(context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	@Override
	public void onGlobalLayout() {
		if (isIniting) {
			return;
		}
		// 调整视图位置
		initBmpPosition();
	}

	/**
	 * 初始化图片位置
	 */
	public void initBmpPosition() {
		isIniting = true;
		super.setScaleType(ScaleType.MATRIX);
		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		final float viewWidth = getWidth();
		final float viewHeight = getHeight();
		final int drawableWidth = drawable.getIntrinsicWidth();
		final int drawableHeight = drawable.getIntrinsicHeight();

		borderWidth = (int) (viewWidth - 2 * BORDERD_ISTANCE_WIDTH);
		borderHeight = (int) (viewHeight - 2 * BORDERD_ISTANCE_HEIGHT);

		float screenScale = 1f;
		screenScale = (float) viewWidth / drawableWidth;

		defaultMatrix.setScale(screenScale, screenScale);

		float heightOffset = (viewHeight - drawableHeight * screenScale) / 2.0f;
		defaultMatrix.postTranslate(0, heightOffset);

		resetMatrix();
	}

	/**
	 * Resets the Matrix back to FIT_CENTER, and then displays it.s
	 */
	private void resetMatrix() {
		if (dragMatrix == null || rotateMatrix == null) {
			return;
		}
		dragMatrix.reset();
		rotateMatrix.reset();
		setImageMatrix(getDisplayMatrix());
	}

	private Matrix getDisplayMatrix() {
		finalMatrix.set(defaultMatrix);
		finalMatrix.postConcat(dragMatrix);
		finalMatrix.postConcat(rotateMatrix);
		return finalMatrix;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		return multiGestureDetector.onTouchEvent(motionEvent);
	}

	private class MultiGestureDetector extends GestureDetector.SimpleOnGestureListener implements
			ScaleGestureDetector.OnScaleGestureListener {

		private final ScaleGestureDetector scaleGestureDetector;
		private final GestureDetector gestureDetector;
		private final float scaledTouchSlop;

		private VelocityTracker velocityTracker;
		private boolean isDragging;

		private float lastTouchX;
		private float lastTouchY;
		private float lastPointerCount;// 上一次是几个手指事件

		public MultiGestureDetector(Context context) {
			scaleGestureDetector = new ScaleGestureDetector(context, this);
			gestureDetector = new GestureDetector(context, this);
			gestureDetector.setOnDoubleTapListener(this);

			final ViewConfiguration configuration = ViewConfiguration.get(context);
			scaledTouchSlop = configuration.getScaledTouchSlop();
		}

		@Override
		public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
			float scale = getScale();
			float scaleFactor = scaleGestureDetector.getScaleFactor();
			if (getDrawable() != null
					&& ((scale < maxScale && scaleFactor > 1.0f) || (scale > minScale && scaleFactor < 1.0f))) {
				if (scaleFactor * scale < minScale) {
					scaleFactor = minScale / scale;
				}
				if (scaleFactor * scale > maxScale) {
					scaleFactor = maxScale / scale;
				}
				dragMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
				checkAndDisplayMatrix();
			}
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
		}

		public boolean onTouchEvent(MotionEvent event) {
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}

			scaleGestureDetector.onTouchEvent(event);

			/*
			 * Get the center x, y of all the pointers
			 */
			float x = 0, y = 0;
			final int pointerCount = event.getPointerCount();
			for (int i = 0; i < pointerCount; i++) {
				x += event.getX(i);
				y += event.getY(i);
			}
			x = x / pointerCount;
			y = y / pointerCount;

			/*
			 * If the pointer count has changed cancel the drag
			 */
			if (pointerCount != lastPointerCount) {
				isDragging = false;
				if (velocityTracker != null) {
					velocityTracker.clear();
				}
				lastTouchX = x;
				lastTouchY = y;
				lastPointerCount = pointerCount;
			}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (velocityTracker == null) {
					velocityTracker = VelocityTracker.obtain();
				} else {
					velocityTracker.clear();
				}
				velocityTracker.addMovement(event);

				lastTouchX = x;
				lastTouchY = y;
				isDragging = false;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				lastPointerCount = 0;
				if (velocityTracker != null) {
					velocityTracker.recycle();
					velocityTracker = null;
				}
				break;
			case MotionEvent.ACTION_MOVE: {
				final float dx = x - lastTouchX, dy = y - lastTouchY;

				if (isDragging == false) {
					// Use Pythagoras to see if drag length is larger than
					// touch slop
					isDragging = Math.sqrt((dx * dx) + (dy * dy)) >= scaledTouchSlop;
				}

				if (isDragging) {
					if (getDrawable() != null) {
						dragMatrix.postTranslate(dx, dy);
						checkAndDisplayMatrix();
					}

					lastTouchX = x;
					lastTouchY = y;

					if (velocityTracker != null) {
						velocityTracker.addMovement(event);
					}
				}
				break;
			}
			}

			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			try {
				float scale = getScale();
				float x = getWidth() / 2;
				float y = getHeight() / 2;

				if (scale < midScale) {
					post(new AnimatedZoomRunnable(scale, midScale, x, y));
				} else if ((scale >= midScale) && (scale < maxScale)) {
					post(new AnimatedZoomRunnable(scale, maxScale, x, y));
				} else {// 双击缩小小于最小值
					post(new AnimatedZoomRunnable(scale, minScale, x, y));
				}
			} catch (Exception e) {
				// Can sometimes happen when getX() and getY() is called
			}

			return true;
		}
	}

	private class AnimatedZoomRunnable implements Runnable {
		// These are 'postScale' values, means they're compounded each iteration
		static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
		static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;

		private final float focalX, focalY;
		private final float targetZoom;
		private final float deltaScale;

		public AnimatedZoomRunnable(final float currentZoom, final float targetZoom, final float focalX,
				final float focalY) {
			this.targetZoom = targetZoom;
			this.focalX = focalX;
			this.focalY = focalY;

			if (currentZoom < targetZoom) {
				deltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
			} else {
				deltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
			}
		}

		@Override
		public void run() {
			dragMatrix.postScale(deltaScale, deltaScale, focalX, focalY);
			checkAndDisplayMatrix();

			final float currentScale = getScale();

			if (((deltaScale > 1f) && (currentScale < targetZoom))
					|| ((deltaScale < 1f) && (targetZoom < currentScale))) {
				// We haven't hit our target scale yet, so post ourselves
				// again
				postOnAnimation(CropImageView.this, this);

			} else {
				// We've scaled past our target zoom, so calculate the
				// necessary scale so we're back at target zoom
				final float delta = targetZoom / currentScale;
				dragMatrix.postScale(delta, delta, focalX, focalY);
				checkAndDisplayMatrix();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void postOnAnimation(View view, Runnable runnable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.postOnAnimation(runnable);
		} else {
			view.postDelayed(runnable, 16);
		}
	}

	/**
	 * Returns the current scale value
	 * 
	 * @return float - current scale value
	 */
	public final float getScale() {
		dragMatrix.getValues(matrixValues);
		return matrixValues[Matrix.MSCALE_X];
	}

	/**
	 * Helper method that simply checks the Matrix, and then displays the result
	 */
	private void checkAndDisplayMatrix() {
		checkMatrixBounds();
		setImageMatrix(getDisplayMatrix());
	}

	private void checkMatrixBounds() {
		final RectF rect = getDisplayRect(getDisplayMatrix());
		if (null == rect) {
			return;
		}

		float deltaX = 0, deltaY = 0;
		final float viewWidth = getWidth();
		final float viewHeight = getHeight();
		// 判断移动或缩放后，图片显示是否超出裁剪框边界
		final float heightBorder = (viewHeight - borderHeight) / 2;
		final float widthBorder = (viewWidth - borderWidth) / 2;
		if (rect.top > heightBorder) {
			deltaY = heightBorder - rect.top;
		}
		if (rect.bottom < (viewHeight - heightBorder)) {
			deltaY = viewHeight - heightBorder - rect.bottom;
		}
		if (rect.left > widthBorder) {
			deltaX = widthBorder - rect.left;
		}
		if (rect.right < viewWidth - widthBorder) {
			deltaX = viewWidth - widthBorder - rect.right;
		}
		// Finally actually translate the matrix
		dragMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 获取图片相对Matrix的距离
	 * 
	 * @param matrix
	 *            - Matrix to map Drawable against
	 * @return RectF - Displayed Rectangle
	 */
	private RectF getDisplayRect(Matrix matrix) {
		Drawable d = getDrawable();
		if (null != d) {
			displayRect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(displayRect);
			return displayRect;
		}
		return null;
	}

	/**
	 * 剪切图片，返回剪切后的bitmap对象
	 * 
	 * @return
	 */
	public Bitmap clipImage() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		return Bitmap.createBitmap(bitmap, (getWidth() - borderWidth) / 2, (getHeight() - borderHeight) / 2,
				borderWidth, borderHeight);
	}

	public void rotate(int degrees) {
		rotateMatrix.reset();
		rotateMatrix.postRotate(degrees, getWidth() / 2, getHeight() / 2);
		checkAndDisplayMatrix();
	}

	public void updateClipRect(float left, float top) {
		BORDERD_ISTANCE_WIDTH = left;
		BORDERD_ISTANCE_HEIGHT = top;
		borderWidth = (int) (getWidth() - 2 * BORDERD_ISTANCE_WIDTH);
		borderHeight = (int) (getHeight() - 2 * BORDERD_ISTANCE_HEIGHT);
	}
}
