package com.edonesoft.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edonesoft.app.App;
import com.edonesoft.app.AppConfig;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class WebDataRequestHelper {

	public static void processWebRequest(int tag, Handler handler, String httpmethod, String url, String body) {
		final Handler myhandler = handler;
		final int mytag = tag;
		final String method = httpmethod;
		String requestUrl = url;
		if (url.contains("{token}")) {
			requestUrl = url.replace("{token}", AppConfig.sharedInstance().Token);
		}
		final String myurl = requestUrl;
		final String postbody = body;
		Runnable runable = new Runnable() {
			@Override
			public void run() {
				Message msg = null;
				if (method.equalsIgnoreCase("GET")) {
					Log.d("Http Request", myurl);
					msg = processGetRequest(myurl);
				} else {
					Log.d("Http Request", myurl);
					msg = processPostRequest(myurl, postbody);
					Log.d("Http Request", postbody);
				}
				if (msg != null && myhandler != null) {
					msg.arg1 = mytag;
					myhandler.sendMessage(msg);
				}
			}
		};
		new Thread(runable).start();
	}

	public static Message processResponse(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		Message msg = new Message();
		Bundle bundle = new Bundle();
		String result = null;
		HttpEntity httpEntity = response.getEntity();
		int length = (int) httpEntity.getContentLength();
		if (length < 0)
			length = 100000;
		StringBuffer stringBuffer = new StringBuffer(length);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent(), HTTP.UTF_8);
			char buffer[] = new char[length];
			int count;
			while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {
				stringBuffer.append(buffer, 0, count);
			}
			result = stringBuffer.toString();
			msg.what = 0;

		} catch (UnsupportedEncodingException e) {
			result = e.getMessage();
			msg.what = 1;
			Log.e("WEB API", e.getMessage());
		} catch (IllegalStateException e) {
			result = e.getMessage();
			msg.what = 1;
			Log.e("WEB API", e.getMessage());
		} catch (IOException e) {
			result = e.getMessage();
			msg.what = 1;
			Log.e("WEB API", e.getMessage());
		}
		bundle.putString("ResponseText", result);
		Log.d("Http Response", result);
		if (statusCode == 200) {
			msg.what = 0;
		} else {
			msg.what = 1;
		}
		msg.setData(bundle);
		return msg;
	}

	public static Message processGetRequest(String url) {
		return processGetRequest(url, new DefaultHttpClient(new BasicHttpParams()));
	}

	public static Message processGetRequest(String url, DefaultHttpClient client) {
		Message msg = null;
		String requestUrl = url;
		HttpGet httpGet = new HttpGet(requestUrl);

		httpGet.addHeader("Content-Type", "application/json");
		httpGet.addHeader("Authorization", WebDataRequestHelper.generateAuthorizationHeader());
		try {
			Log.d("Http Request", "Start Reponse");
			HttpResponse httpResponse = client.execute(httpGet);
			Log.d("Http Request", "End Reponse");

			msg = processResponse(httpResponse);
		} catch (Exception e) {
			msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("ResponseText", e.getMessage());
			msg.setData(bundle);
			msg.what = 2;
			Log.d("Http Request", e.getMessage());
		} finally {
			httpGet.abort();
			Log.d("Http Request", "abort");
		}
		return msg;
	}

	public static Message processPostRequest(String url, String body) {
		return processPostRequest(url, body, new DefaultHttpClient(new BasicHttpParams()));
	}

	public static Message processPostRequest(String url, String body, DefaultHttpClient client) {
		Message msg = null;
		String requestBody = body;
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("Authorization", WebDataRequestHelper.generateAuthorizationHeader());
		try {
			Log.d("Http Request", "Start Reponse");
			StringEntity postEntity = new StringEntity(requestBody, "UTF-8");
			postEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(postEntity);
			HttpResponse httpResponse = client.execute(httpPost);
			Log.d("Http Request", "End Reponse");
			msg = processResponse(httpResponse);
		} catch (Exception e) {
			msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("ResponseText", e.getMessage());
			msg.setData(bundle);
			msg.what = 2;
			Log.d("Http Request", e.getMessage());
		} finally {
			httpPost.abort();
			Log.d("Http Request", "abort");
		}
		return msg;
	}

	public static void processUploadRequest(int tag, Handler handler, String string, String url, byte[] file) {
		final Handler myhandler = handler;
		final int mytag = tag;
		final String myurl = url;
		final byte[] myfile = file;
		Runnable runable = new Runnable() {
			@Override
			public void run() {
				Message msg = null;
				msg = processUploadRequest(myurl, myfile);
				if (msg != null && myhandler != null) {
					msg.arg1 = mytag;
					myhandler.sendMessage(msg);
				}
			}

		};
		new Thread(runable).start();
	}

	public static Message processUploadRequest(String myurl, byte[] myfile) {
		DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
		Message msg = null;
		HttpPost httpPost = new HttpPost(myurl);
		httpPost.addHeader("Authorization", WebDataRequestHelper.generateAuthorizationHeader());
		try {
			ByteArrayEntity fileEntity = new ByteArrayEntity(myfile);
			fileEntity.setContentType("application/octet-stream");
			httpPost.setEntity(fileEntity);
			HttpResponse httpResponse = client.execute(httpPost);
			msg = processResponse(httpResponse);
		} catch (Exception e) {
			msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("ResponseText", e.getMessage());
			msg.setData(bundle);
			msg.what = 2;
		} finally {
			httpPost.abort();
		}
		return msg;
	}

	public static JSONObject getJsonObject(String jsonStr) {
		JSONObject result = null;
		try {
			result = new JSONObject(jsonStr);
		} catch (JSONException e) {
			Log.d("Not Valid Json String", jsonStr);
		} catch (Exception e) {
			Log.d("Pass json error", e.getMessage());
		}
		return result;
	}

	public static JSONArray getJsonArray(String jsonStr) {
		JSONArray result = null;
		try {
			result = new JSONArray(jsonStr);
		} catch (JSONException e) {
			Log.d("Not Valid Json String", jsonStr);
		} catch (Exception e) {
			Log.d("Pass json error", e.getMessage());
		}
		return result;
	}

	public static boolean validateJsonResponse(Message message) {
		String responseText = message.getData().getString("ResponseText");
		if (responseText == null) {
			Toast.makeText(App.appContext, "网络错误", Toast.LENGTH_SHORT).show();
			return false;
		}
		JSONObject object = getJsonObject(responseText);
		if (object == null) {
			Toast.makeText(App.appContext, "网络错误", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (object != null) {
			try {
				if (!object.has("Code")) {
					Toast.makeText(App.appContext, object.getString("Message"), Toast.LENGTH_SHORT).show();
					return false;
				}
				if (object.getInt("Code") != 10000) {
					String error = object.getString("Message");
					Toast.makeText(App.appContext, error, Toast.LENGTH_SHORT).show();
					return false;
				}
			} catch (Exception ex) {
				return false;
			}
		}
		return true;
	}

	public static boolean validateJsonObject(JSONObject json) {
		try {
			if (json.getInt("Code") == 10000) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String generateAuthorizationHeader() {
		AppConfig config = AppConfig.sharedInstance();
		String header = String.format("android<>%s<>%f<>%f<>CN", config.Token, config.Latitude, config.Longitude);
		Log.d("Http Request", header);
		return header;
	}
}
