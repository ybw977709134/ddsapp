package com.edonesoft.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Studio;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;

@SuppressLint("HandlerLeak")
public class SearchStationActivity extends BaseActivity implements OnMarkerClickListener, OnInfoWindowClickListener,
		InfoWindowAdapter, AMapLocationListener, SensorEventListener, LocationSource, OnCheckedChangeListener {
	private AMap aMap;
	private MapView mapView;
	private LocationManagerProxy mAMapLocationManager;
	private OnLocationChangedListener mListener;
	private SensorManager mSensorManager;
	private Sensor mSensor;

	private Marker mGPSMarker;
	private long lastTime = 0;
	private final int TIME_SENSOR = 100;
	private float mAngle;
	private boolean isFirstLocation = true;

	private List<Marker> markerList = new ArrayList<Marker>();
	private List<Studio> studioList = new ArrayList<Studio>();

	private EditText searchEt;
	private ImageView locationBtn;
	private ImageButton helpBtn;
	private CheckBox studioTypeCb1;
	private CheckBox studioTypeCb2;
	private CheckBox studioTypeCb3;

	private int spec_id;
	private int service_type;
	private InputMethodManager imm;
	private LatLng locationLatLng;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spec_id = getIntent().getIntExtra("spec_id", 0);
		service_type = getIntent().getIntExtra("service_type", 0);
		setContentView(R.layout.activity_search_station);
		mapView = (MapView) findViewById(R.id.search_station_map);
		mapView.onCreate(savedInstanceState);
		// 初始化传感器
		mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initViews();
		startLocation();
	}

	private void initViews() {
		findViews();
		initListeners();
	}

	private void findViews() {
		aMap = mapView.getMap();
		helpBtn = (ImageButton) findViewById(R.id.help_btn);
		searchEt = (EditText) findViewById(R.id.search_station_keywords);
		locationBtn = (ImageView) findViewById(R.id.search_station_location_btn);
		studioTypeCb1 = (CheckBox) findViewById(R.id.search_station_studio_type1);
		studioTypeCb2 = (CheckBox) findViewById(R.id.search_station_studio_type2);
		studioTypeCb3 = (CheckBox) findViewById(R.id.search_station_studio_type3);
	}

	private void startLocation() {
		mGPSMarker = aMap.addMarker(new MarkerOptions().icon(
				BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.location_marker))).anchor(0.5f, 0.5f));

		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	}

	private void initListeners() {
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		locationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (locationLatLng != null) {
					aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15));
				}
			}
		});
		searchEt.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (v.getText().toString().length() > 0) {
						getPointData(v.getText().toString());
						searchEt.setText("");
						imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
						return true;
					}
				}
				return false;
			}
		});
		helpBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchStationActivity.this, StationHelpActivity.class);
				startActivity(intent);
			}
		});
		studioTypeCb1.setOnCheckedChangeListener(this);
		studioTypeCb2.setOnCheckedChangeListener(this);
		studioTypeCb3.setOnCheckedChangeListener(this);
	}

	private void getPointData(String keywords) {
		String requestUrl = "studio/list?page_size=0&page_index=0&keywords=" + keywords;
		if (service_type != 0) {
			requestUrl += "&service_type=" + service_type;
		}
		if (spec_id != 0) {
			requestUrl += "&spec_id=" + spec_id;
		}
		WebDataRequest.requestGet(0, handler, requestUrl);
	}

	private void getPointDataByStudioType(String studioType) {
		String requestUrl = "studio/list?page_size=0&page_index=0&studio_type=" + studioType;
		if (service_type != 0) {
			requestUrl += "&service_type=" + service_type;
		}
		if (spec_id != 0) {
			requestUrl += "&spec_id=" + spec_id;
		}
		WebDataRequest.requestGet(0, handler, requestUrl);
	}

	private void initMarkers(List<Studio> studioList) {
		aMap.clear();
		markerList.clear();
		for (int i = 0; i < studioList.size(); i++) {
			Studio studio = studioList.get(i);
			LatLng latLng = new LatLng(studio.getLocLatitude(), studio.getLocLongitude());
			MarkerOptions options = new MarkerOptions();
			options.position(latLng);
			options.anchor(0.5f, 1);
			options.draggable(false);
			options.zIndex(9);
			options.title(studio.getName());
			if (studio.getStudio_type().contains("2")) {
				options.icon(BitmapDescriptorFactory.fromResource(R.drawable.search_station_icon3));
			} else if (studio.getStudio_type().contains("1")) {
				options.icon(BitmapDescriptorFactory.fromResource(R.drawable.search_station_icon1));
			} else {
				options.icon(BitmapDescriptorFactory.fromResource(R.drawable.search_station_icon2));
			}
			markerList.add(aMap.addMarker(options));
		}
		if (studioList.size() > 0) {
			if (isFirstLocation) {
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15));
				isFirstLocation = false;
			}
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(studioList.get(0).getLocLatitude(), studioList
					.get(0).getLocLongitude()), 15));
		} else {
			if (locationLatLng != null) {
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15));
			}
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
				switch (msg.arg1) {
				case 0:
					List<Studio> list = JSON.parseObject(responseJson.optString("Detail"),
							new TypeReference<List<Studio>>() {
							});
					if (list != null) {
						if (list.size() == 0) {
							showToast("附近没有搜索到网点!");
						} else {
							studioList.clear();
							studioList.addAll(list);
							initMarkers(studioList);
						}
					}
				}
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		registerSensorListener();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 清除所有Marker
	 */
	public void clearMarker() {
		aMap.clear();
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View view = LayoutInflater.from(this).inflate(R.layout.map_window_station_detail, null);
		ImageView icon = (ImageView) view.findViewById(R.id.map_window_icon);
		TextView text = (TextView) view.findViewById(R.id.map_window_text);
		final Studio studio = studioList.get(markerList.indexOf(marker));
		if (studio.getStudio_type().contains("2")) {
			icon.setImageResource(R.drawable.search_station_type3);
		} else if (studio.getStudio_type().contains("1")) {
			icon.setImageResource(R.drawable.search_station_type1);
		} else {
			icon.setImageResource(R.drawable.search_station_type2);
		}
		text.setText(studio.getName());
		return view;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Studio studio = studioList.get(markerList.indexOf(marker));
		Intent intent = new Intent(SearchStationActivity.this, StationDetailActivity.class);
		intent.putExtra("studio", studio);
		startActivity(intent);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker.isInfoWindowShown()) {
			Studio studio = studioList.get(markerList.indexOf(marker));
			Intent intent = new Intent(SearchStationActivity.this, StationDetailActivity.class);
			intent.putExtra("studio", studio);
			startActivity(intent);
		} else {
			marker.showInfoWindow();
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);
			locationLatLng = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
			mGPSMarker.setPosition(locationLatLng);
			if (isFirstLocation) {
				AppConfig.sharedInstance().Latitude = aLocation.getLatitude();
				AppConfig.sharedInstance().Longitude = aLocation.getLongitude();
				AppConfig.sharedInstance().saveToPreference();
				getPointData("");
			}
		}
	}

	public void registerSensorListener() {
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void unRegisterSensorListener() {
		mSensorManager.unregisterListener(this, mSensor);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
			return;
		}
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ORIENTATION: {
			float x = event.values[0];

			x += getScreenRotationOnPhone(this);
			x %= 360.0F;
			if (x > 180.0F)
				x -= 360.0F;
			else if (x < -180.0F)
				x += 360.0F;
			if (Math.abs(mAngle - 90 + x) < 3.0f) {
				break;
			}
			mAngle = x;
			if (mGPSMarker != null) {
				mGPSMarker.setRotateAngle(-mAngle);
				aMap.invalidate();
			}
			lastTime = System.currentTimeMillis();
		}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	/**
	 * 获取当前屏幕旋转角度
	 * 
	 * @param activity
	 * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
	 */
	public static int getScreenRotationOnPhone(Context context) {
		final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		switch (display.getRotation()) {
		case Surface.ROTATION_0:
			return 0;

		case Surface.ROTATION_90:
			return 90;

		case Surface.ROTATION_180:
			return 180;

		case Surface.ROTATION_270:
			return -90;
		}
		return 0;
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 5000, 10, this);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (mAMapLocationManager == null) {
						deactivate();
					}
				}
			}, 13000);// 设置超过13秒还没有定位到就停止定位
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
		unRegisterSensorListener();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String studioType = "";
		if (studioTypeCb2.isChecked()) {
			studioType += ",0";
		}
		if (studioTypeCb3.isChecked()) {
			studioType += ",1";
		}
		if (studioTypeCb1.isChecked()) {
			studioType += ",2";
		}
		getPointDataByStudioType(studioType);
	}
}
