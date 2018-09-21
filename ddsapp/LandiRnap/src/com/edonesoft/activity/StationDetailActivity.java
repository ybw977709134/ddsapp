package com.edonesoft.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Studio;
import com.edonesoft.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StationDetailActivity extends BaseActivity implements OnClickListener {
	private Studio studio;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private ImageButton shareBtn;
	private ImageButton collectionBtn;
	private ImageView stationIcon;
	private RatingBar stationRating;
	private TextView stationName;
	private TextView stationAddress;
	private TextView stationPhone;
	private TextView stationPrice;
	private TextView stationMethod;
	private TextView stationIntro;
	private RelativeLayout navigationRellay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		studio = (Studio) getIntent().getSerializableExtra("studio");
		setContentView(R.layout.activity_station_detail);
		initImageLoader();
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		initData();
	}

	private void findViews() {
		shareBtn = (ImageButton) findViewById(R.id.share_btn);
		collectionBtn = (ImageButton) findViewById(R.id.collection_btn);
		stationIcon = (ImageView) findViewById(R.id.station_image);
		stationName = (TextView) findViewById(R.id.station_name);
		stationAddress = (TextView) findViewById(R.id.station_address_text);
		stationPhone = (TextView) findViewById(R.id.station_phone_text);
		stationMethod = (TextView) findViewById(R.id.station_method_text);
		stationPrice = (TextView) findViewById(R.id.station_price_text);
		stationRating = (RatingBar) findViewById(R.id.station_rating);
		navigationRellay = (RelativeLayout) findViewById(R.id.station_address_rellay);
		stationIntro = (TextView) findViewById(R.id.station_intro_text);
	}

	private void initListeners() {
		shareBtn.setOnClickListener(this);
		collectionBtn.setOnClickListener(this);
		navigationRellay.setOnClickListener(this);
	}

	private void initData() {
		if (studio != null) {
			stationName.setText(studio.getName());
			stationAddress.setText(studio.getLoc_address());
			stationPhone.setText(studio.getPhone_number());
			if (studio.getAverage_price() == 0) {
				stationPrice.setText("现场");
			} else {
				stationPrice.setText(studio.getAverage_price() + "元");
			}
			stationRating.setRating(studio.getStar_count());
			if (studio.getStudio_type().contains("2")) {
				stationMethod.setText("办事点");
			} else if (studio.getStudio_type().contains("1")) {
				stationMethod.setText("照相馆");
			} else {
				stationMethod.setText("自助机");
			}
			imageLoader.displayImage(studio.getIcon_url(), stationIcon, options);
		}
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_btn:

			break;
		case R.id.collection_btn:

			break;
		case R.id.station_address_rellay: {
			// if (studio.getLoc_address().length() != 0) {
			// Intent intent = new Intent(this,
			// NavigationStationActivity.class);
			// startActivity(intent);
			// }
		}
			break;
		}
	}
}
