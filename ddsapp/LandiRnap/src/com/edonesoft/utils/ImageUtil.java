package com.edonesoft.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class ImageUtil {

	/**
	 * 加载本地图片
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmap(String filePath) {
		Bitmap bm = BitmapFactory.decodeFile(filePath);
		return bm;
	}

	/**
	 * Bitmap转成byte
	 * 
	 * @param bm
	 * @param suffix
	 * @param quality
	 *            图片质量 0~100
	 * @return
	 */
	public static byte[] BitmapToBytes(Bitmap bm, String suffix, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap.CompressFormat format = null;
		if (suffix == null || (suffix != null && suffix.length() == 0)) {
			format = Bitmap.CompressFormat.JPEG;
		} else if (suffix.toLowerCase().endsWith("jpg") || suffix.toLowerCase().endsWith("jpeg")) {
			format = Bitmap.CompressFormat.JPEG;
		} else if (suffix.toLowerCase().endsWith("png")) {
			format = Bitmap.CompressFormat.PNG;
		} else {
			format = Bitmap.CompressFormat.JPEG;
		}
		bm.compress(format, quality, baos);
		return baos.toByteArray();
	}

	public static Bitmap getRotateBitmap(String filePath) {
		Bitmap bm = getBitmap(filePath);
		if (bm == null) {
			return null;
		}
		int degree = readPictureDegree(filePath);
		bm = rotateBitmap(bm, degree);
		return bm;
	}

	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	/**
	 * 根据url地址获取图片本地Stream</br>
	 * 
	 * @param url
	 *            图片的地址
	 * @return 本地图片的Stream，否则返回null
	 */
	public static InputStream getBitmapStream(String url) {
		InputStream is = null;
		try {
			try {
				is = new FileInputStream(new File(url));
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (is == null || is.available() <= 0) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("BitmapUtil", "读取图片流出错" + e.toString());
		}
		return is;
	}

	/**
	 * 根据指定的宽高设置相关参数，避免出现OOM现象</br>
	 * 
	 * @param url
	 *            图片得url地址
	 * @param width
	 *            期望图片的宽
	 * @param height
	 *            期望图片的高
	 * @return BitmapFactory.Options对象
	 */
	private static BitmapFactory.Options getScaleBitmapOptions(String url, int width, int height) {
		InputStream inputStream = getBitmapStream(url);
		if (inputStream == null) {
			return null;
		}
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(inputStream, null, bmpFactoryOptions);

			int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / height);
			int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / width);

			/*
			 * If both of the ratios are greater than 1, one of the sides of the
			 * image is greater than the screen
			 */
			if (heightRatio > 1 && widthRatio > 1) {
				if (heightRatio > widthRatio) {
					// Height ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = heightRatio;
				} else {
					// Width ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = widthRatio;
				}
			}

			// Decode it for real
			bmpFactoryOptions.inJustDecodeBounds = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 关闭java 层的stream
		closeInputStream(inputStream);

		return bmpFactoryOptions;
	}

	/**
	 * 从SD卡上获取图片。如果不存在则返回null</br>
	 * 
	 * @param url
	 *            图片的url地址
	 * @param width
	 *            期望图片的宽
	 * @param height
	 *            期望图片的高
	 * @return 代表图片的Bitmap对象
	 */
	public static Bitmap getBitmapFromSDCard(String url, int width, int height) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(url));
			if (inputStream != null && inputStream.available() > 0) {
				Bitmap bitmap = BitmapFactory
						.decodeStream(inputStream, null, getScaleBitmapOptions(url, width, height));
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照标准尺寸自动剪裁图片
	 * 
	 * @param commenPath
	 * @param isCheck
	 * @return
	 */
	public static Bitmap automaticCropBitmap(Bitmap commenPath, boolean isCheck) {
		Bitmap tmpBmp = commenPath.copy(Bitmap.Config.RGB_565, true);
		FaceDetector faceDet = new FaceDetector(tmpBmp.getWidth(), tmpBmp.getHeight(), 1);
		FaceDetector.Face[] faceList = new FaceDetector.Face[1];
		faceDet.findFaces(tmpBmp, faceList);

		FaceDetector.Face face = faceList[0];
		if (face != null) {
			float leftRatio = 2.5f;
			float headRatio = 3.0f;
			float bodyRatio = 3.5f;

			PointF pf = new PointF();
			face.getMidPoint(pf);

			int cropLeft = (int) (pf.x - face.eyesDistance() * leftRatio);
			int cropTop = (int) (pf.y - face.eyesDistance() * headRatio);

			cropLeft = cropLeft >= 0 ? cropLeft : 0;
			cropTop = cropTop >= 0 ? cropTop : 0;
			int cropWidth = (int) (leftRatio * face.eyesDistance() * 2) + cropLeft < commenPath.getWidth() ? (int) (leftRatio
					* face.eyesDistance() * 2)
					: (int) (commenPath.getWidth() - cropLeft - 1);
			int cropHeight = (int) ((headRatio + bodyRatio) * face.eyesDistance()) + cropTop < commenPath.getHeight() ? (int) ((headRatio + bodyRatio) * face
					.eyesDistance()) : (int) (commenPath.getHeight() - cropTop - 1);
			Log.e("=====================", "cropLeft:" + cropLeft + "cropTop:" + cropTop);
			Log.e("=====================", "cropwidth:" + cropWidth + "cropheight:" + cropHeight);
			Log.e("=====================", "width:" + commenPath.getWidth() + "height:" + commenPath.getHeight());

			tmpBmp = Bitmap.createBitmap(commenPath, cropLeft, cropTop, cropWidth, cropHeight);

			if (!isCheck) {
				return tmpBmp;
			}
			byte[] data = BitmapToBytes(tmpBmp, "jpg", 100);
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			try {
				ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
				bmpFactoryOptions.inJustDecodeBounds = true;
				try {
					BitmapFactory.decodeStream(arrayInputStream, null, bmpFactoryOptions);
					int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / 280);
					int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / 210);
					if (heightRatio > 1 && widthRatio > 1) {
						if (heightRatio > widthRatio) {
							bmpFactoryOptions.inSampleSize = heightRatio;
						} else {
							bmpFactoryOptions.inSampleSize = widthRatio;
						}
					}
					bmpFactoryOptions.inJustDecodeBounds = false;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				Bitmap finalBitMap = BitmapFactory.decodeByteArray(data, 0, data.length, bmpFactoryOptions);
				compressImage(finalBitMap);
				return finalBitMap;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 调整bitmap的质量，控制在100k以下
	 * 
	 * @param image
	 * @return
	 */
	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1000 / 16 >= 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 1;// 每次都减少1
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 下载图片并保存到相册
	 */
	public static boolean saveImageToGallery(Context context, String url) {
		// 首先保存图片
		File appDir = null;
		if (FileUtil.checkSDCard()) {
			appDir = new File(FileUtil.SDCard + "/Landi/");
		} else {
			return false;
		}
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);

		try {
			InputStream inputStream = new URL(url).openStream();
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			outputStream.flush();
			closeOutputStream(outputStream);
			closeInputStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media
					.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, "Landi");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
				+ Environment.getExternalStorageDirectory())));
		return true;
	}

	/**
	 * 根据图片的URL地址下载图片，并将下载的图片根据宽高缩放。注意：该方法使用单线程的方式下载图片。</br>
	 * 
	 * @param url
	 *            图片的url地址
	 * @param width
	 *            图片的宽
	 * @param height
	 *            图片的高
	 * @return 图片的Bitmap对象
	 */
	public static Bitmap downloadBitmap(String url, int width, int height) {
		Bitmap bitmap = null;

		File appDir = new File(FileUtil.getDataPath() + "imageCache/");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);

		try {
			InputStream inputStream = new URL(url).openStream();
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			outputStream.flush();
			closeOutputStream(outputStream);
			closeInputStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bitmap = BitmapFactory.decodeStream(getBitmapStream(file.getAbsolutePath()), null,
				getScaleBitmapOptions(file.getAbsolutePath(), width, height));
		return bitmap;
	}

	 public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
         int w = bitmap.getWidth();
         int h = bitmap.getHeight();
         Matrix matrix = new Matrix();
         float scaleWidth = ((float) width / w);
         float scaleHeight = ((float) height / h); 
         matrix.reset();
         matrix.postScale(scaleWidth, scaleHeight);
         Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
         return newbmp;
 }

	/**
	 * 关闭输出流</br>
	 * 
	 * @param outputStream
	 *            输出流
	 */
	private static void closeOutputStream(OutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭输入流</br>
	 * 
	 * @param inputStream
	 *            输入流
	 */
	private static void closeInputStream(InputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
