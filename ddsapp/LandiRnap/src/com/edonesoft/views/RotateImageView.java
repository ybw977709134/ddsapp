package com.edonesoft.views;

import com.edonesoft.listeners.OnDegreesChangeListener;
import com.edonesoft.utils.DensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class RotateImageView extends View {
	private Context context;
	private Paint mLinePaint;
	private Paint mTextPaint;
	public int degrees = -15;

	private float mLastMotionX;
	private float mInitialMotionX;
	private float lineWidth;

	public OnDegreesChangeListener degreesChangeListener;

	public RotateImageView(Context context) {
		super(context);
		this.context = context;
		createPainter();
	}

	public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		createPainter();
	}

	public RotateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		createPainter();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE: {
			mInitialMotionX = event.getX();
			float degrees = (mInitialMotionX - mLastMotionX) / lineWidth * 3;
			setDegrees(this.degrees - (int) degrees);
			mLastMotionX = event.getX();
			return true;
		}

		case MotionEvent.ACTION_DOWN: {
			mLastMotionX = event.getX();
			return true;
		}
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = (int) degrees, j = 0; i < (int) degrees + 31; i++, j++) {
			float startX = j * lineWidth + DensityUtil.dip2px(context, 40);
			if (i % 5 == 0) {
				canvas.drawLine(startX, 0, startX, DensityUtil.dip2px(context, 13), mLinePaint);
				canvas.drawText(i + "", startX, DensityUtil.dip2px(context, 25), mTextPaint);
			} else {
				canvas.drawLine(startX, 0, startX, DensityUtil.dip2px(context, 7), mLinePaint);
			}
		}
		super.onDraw(canvas);
	}

	@SuppressWarnings("deprecation")
	private void createPainter() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int windowWidth = wm.getDefaultDisplay().getWidth();
		lineWidth = (windowWidth - DensityUtil.dip2px(context, 80)) / 30.0f;

		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStyle(Style.STROKE);
		mLinePaint.setStrokeWidth(2);
		mLinePaint.setColor(Color.WHITE);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Style.STROKE);
		mTextPaint.setStrokeWidth(1);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setTextSize(DensityUtil.dip2px(context, 8));
	}

	public void setDegrees(int degrees) {
		this.degrees = degrees;
		if (degreesChangeListener != null) {
			degreesChangeListener.onDegreesChanged(degrees);
		}
		invalidate();
	}

	public void setDegreesChangeListener(OnDegreesChangeListener degreesChangeListener) {
		this.degreesChangeListener = degreesChangeListener;
	}
}
