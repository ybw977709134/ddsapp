/*
 *  COPYRIGHT NOTICE  
 *  Copyright (C) 2015, ticktick <lujun.hust@gmail.com>
 *  https://github.com/Jhuster/ImageCropper
 *   
 *  @license under the Apache License, Version 2.0 
 *
 *  @file    CropImageView.java
 *  @brief   Draw ImageView and CropWindow
 *  
 *  @version 1.0     
 *  @author  ticktick
 *  @date    2015/01/09    
 */
package com.edonesoft.views;

import com.edonesoft.utils.DensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CropImageRectView extends View {
	private static final float CROP_WINDOW_PAINTER_WIDTH = 4.0f;
	private static final float OUTSIDE_WINDOW_PAINTER_WIDTH = 1.0f;

	private Paint mCropPainter;
	private Paint mOutsidePainter;
	private Paint mBlueLinePainter;

	public CropParam mCropParam;
	public CropWindow mCropWindow;

	public CropImageRectView(Context context) {
		super(context);
		createPainter();
	}

	public CropImageRectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		createPainter();
	}

	private void createPainter() {
		mCropPainter = new Paint();
		mCropPainter.setAntiAlias(true);
		mCropPainter.setStyle(Style.STROKE);
		mCropPainter.setStrokeWidth(CROP_WINDOW_PAINTER_WIDTH);
		mCropPainter.setColor(Color.WHITE);

		mOutsidePainter = new Paint();
		mOutsidePainter.setAntiAlias(true);
		mOutsidePainter.setStyle(Style.FILL);
		mOutsidePainter.setARGB(125, 50, 50, 50);
		mOutsidePainter.setStrokeWidth(OUTSIDE_WINDOW_PAINTER_WIDTH);

		mBlueLinePainter = new Paint();
		mBlueLinePainter.setAntiAlias(true);
		mBlueLinePainter.setStyle(Style.FILL);
		mBlueLinePainter.setStrokeWidth(OUTSIDE_WINDOW_PAINTER_WIDTH);
		mBlueLinePainter.setColor(Color.parseColor("#598eea"));
		mBlueLinePainter.setAlpha(125);
	}

	public void initialize(CropParam param) {
		mCropParam = param;
		RectF border = new RectF(0, 0, getWidth(), getHeight());
		mCropWindow = new CropWindow(border, mCropParam);
		invalidate();
	}

	private void drawOutsideCropArea(Canvas canvas) {
		RectF[] rects = mCropWindow.getOutWindowRects();
		for (RectF rect : rects) {
			canvas.drawRect(rect, mOutsidePainter);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		if (mCropWindow != null) {
			RectF windowRectF = mCropWindow.getWindowRectF();
			canvas.drawRect(windowRectF, mCropPainter);
			drawOutsideCropArea(canvas);
			if (mCropParam.head_top != 0) {
				canvas.drawRect(windowRectF.left + 3, windowRectF.top + windowRectF.height() / mCropParam.mAspectY
						* mCropParam.head_top - DensityUtil.dip2px(getContext(), 5), windowRectF.right - 4,
						windowRectF.top + windowRectF.height() / mCropParam.mAspectY * mCropParam.head_top
								+ DensityUtil.dip2px(getContext(), 5), mBlueLinePainter);
			}
			if (mCropParam.chin_bottom != 0) {
				canvas.drawRect(windowRectF.left + 3, windowRectF.bottom - windowRectF.height() / mCropParam.mAspectY
						* mCropParam.chin_bottom - DensityUtil.dip2px(getContext(), 5), windowRectF.right - 3,
						windowRectF.bottom - windowRectF.height() / mCropParam.mAspectY * mCropParam.chin_bottom
								+ DensityUtil.dip2px(getContext(), 5), mBlueLinePainter);
			}

		}
		canvas.restore();
		super.onDraw(canvas);
	}

	public static class CropParam {
		public int head_top;
		public int chin_bottom;
		public int mAspectX = 0;
		public int mAspectY = 0;
		public int mOutputX = 0;
		public int mOutputY = 0;
		public int mMaxOutputX = 0;
		public int mMaxOutputY = 0;
	}
}
