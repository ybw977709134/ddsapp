/*
 *  COPYRIGHT NOTICE  
 *  Copyright (C) 2015, ticktick <lujun.hust@gmail.com>
 *  https://github.com/Jhuster/ImageCropper
 *   
 *  @license under the Apache License, Version 2.0 
 *
 *  @file    CropWindow.java
 *  @brief   Manage the position and size of crop window
 *  
 *  @version 1.0     
 *  @author  ticktick
 *  @date    2015/01/09    
 */
package com.edonesoft.views;

import com.edonesoft.views.CropImageRectView.CropParam;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public class CropWindow {
	private float mLeft;
	private float mTop;
	private float mWidth;
	private float mHeight;
	private RectF mImageRect;

	public CropWindow(RectF imageRect, CropParam params) {
		mWidth = Math.min(imageRect.width(), imageRect.height());
		mHeight = mWidth;

		if (params.mOutputX != 0 && params.mOutputY != 0) {
			mWidth = params.mOutputX;
			mHeight = params.mOutputY;
		} else {
			if (params.mMaxOutputX != 0 && params.mMaxOutputY != 0) {
				mWidth = params.mMaxOutputX;
				mHeight = params.mMaxOutputY;
			}
			if (params.mAspectX != 0 && params.mAspectY != 0) {
				if (params.mAspectX > params.mAspectY) {
					mHeight = mWidth * params.mAspectY / params.mAspectX;
				} else {
					mWidth = mHeight * params.mAspectX / params.mAspectY;
				}
			}
		}
		mLeft = imageRect.left + (imageRect.width() - mWidth) / 2;
		mTop = imageRect.top + (imageRect.height() - mHeight) / 2;

		mImageRect = imageRect;
	}

	public float left() {
		return mLeft;
	}

	public float right() {
		return (mLeft + mWidth);
	}

	public float top() {
		return mTop;
	}

	public float bottom() {
		return (mTop + mHeight);
	}

	public float width() {
		return mWidth;
	}

	public float height() {
		return mHeight;
	}

	public Rect getWindowRect() {
		return new Rect((int) left(), (int) top(), (int) right(), (int) bottom());
	}

	public RectF getWindowRectF() {
		return new RectF(left(), top(), right(), bottom());
	}

	public Rect getWindowRect(float scale) {
		int width = (int) (width() / scale);
		int height = (int) (height() / scale);
		int xoffset = (int) ((left() - mImageRect.left) / scale);
		int yoffset = (int) ((top() - mImageRect.top) / scale);
		return new Rect(xoffset, yoffset, xoffset + width, yoffset + height);
	}

	public RectF[] getOutWindowRects() {
		RectF[] rects = new RectF[4];
		Rect window = getWindowRect();
		rects[0] = new RectF(mImageRect.left, mImageRect.top, mImageRect.right, window.top);
		rects[1] = new RectF(mImageRect.left, window.top, window.left, window.bottom);
		rects[2] = new RectF(window.right, window.top, mImageRect.right, window.bottom);
		rects[3] = new RectF(mImageRect.left, window.bottom, mImageRect.right, mImageRect.bottom);
		return rects;
	}

	public Point[] getDragPoints() {
		Point[] points = new Point[4];
		Rect window = getWindowRect();
		points[0] = new Point(window.left, window.centerY()); // Left
		points[1] = new Point(window.centerX(), window.top); // Top
		points[2] = new Point(window.right, window.centerY()); // Right
		points[3] = new Point(window.centerX(), window.bottom); // Bottom
		return points;
	}
}
