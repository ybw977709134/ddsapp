/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edonesoft.views;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/*
 * Modified from original in AOSP.
 */
public class RotateBitmap {

	private Bitmap bitmap;
	private int rotation;

	public RotateBitmap(Bitmap bitmap, int rotation) {
		this.bitmap = bitmap;
		this.rotation = rotation % 360;
	}

	public void setRotation(int rotation) {
		if (rotation > 180) {
			this.rotation = 180;
			return;
		}
		if (rotation < -180) {
			this.rotation = -180;
			return;
		}
		this.rotation = rotation;
	}

	public int getRotation() {
		return rotation;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Matrix getRotateMatrix() {
		Matrix matrix = new Matrix();
		if (bitmap != null && rotation != 0) {
			matrix.postRotate(rotation, getWidth() / 2, getHeight() / 2);
		}
		return matrix;
	}

	public int getHeight() {
		if (bitmap == null)
			return 0;
		return bitmap.getHeight();

	}

	public int getWidth() {
		if (bitmap == null)
			return 0;
		return bitmap.getWidth();
	}

	public void recycle() {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}
}
