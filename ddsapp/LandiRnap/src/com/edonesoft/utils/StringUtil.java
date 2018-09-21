package com.edonesoft.utils;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class StringUtil {
	/**
	 * 判断是否是null或长度为0
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null)
			return true;
		if (str.length() == 0)
			return true;
		return false;
	}
}
