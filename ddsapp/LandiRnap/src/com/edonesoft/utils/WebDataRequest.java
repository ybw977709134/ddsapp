package com.edonesoft.utils;

import android.os.Handler;
import android.util.Log;

public class WebDataRequest {
	public static String serviceRootPath = "http://idphoto.edonesoft.com/clientapi/";

	// public static String serviceRootPath = "http://192.168.1.31/rnap_api/";

	public static void requestPost(int tag, Handler handler, String url, String body) {
		Log.d("POST", serviceRootPath + url);
		Log.d("Body", body);
		WebDataRequestHelper.processWebRequest(tag, handler, "POST", serviceRootPath + url, body);
	}

	public static void requestGet(int tag, Handler handler, String url) {
		Log.d("Start GET", serviceRootPath + url);
		WebDataRequestHelper.processWebRequest(tag, handler, "GET", serviceRootPath + url, null);
	}

	public static void requestUploadFile(int tag, Handler handler, byte[] file) {
		WebDataRequestHelper.processUploadRequest(tag, handler, "POST", serviceRootPath
				+ "/system/upload/image?ext=jpg", file);
	}
}
