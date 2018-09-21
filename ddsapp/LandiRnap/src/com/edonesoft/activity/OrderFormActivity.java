package com.edonesoft.activity;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.base.BaseDialog;
import com.edonesoft.base.BaseDialog.OnDialogClickListener;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderInfo;
import com.edonesoft.utils.DateUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.DeleteDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("HandlerLeak")
public class OrderFormActivity extends BaseActivity implements OnClickListener {
	private int orderId;
	private int orderStatus;
	private boolean isShowHome;
	private OrderInfo orderInfo;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private ImageView orderCommenPhoto;
	private TextView orderAffairName;
	private TextView photoFileSizeTv;
	private TextView photoScoreTv;
	private TextView photoSizeTv;
	private TextView orderNumberTv;
	private TextView orderCommitDateTv;
	private TextView orderLandiCodeTv;
	private TextView orderAddressTv;
	private TextView showAffairDetailBtn;
	private TextView commitBtn;
	private ProgressBar orderStatusProgressBar;
	private RelativeLayout landiCodeRellay;
	private TextView orderAffairCode;

	private TextView backHomeBtn;
	private ImageButton backBtn;
	private TextView deleteBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_form);
		orderId = getIntent().getIntExtra("orderId", 0);
		orderStatus = getIntent().getIntExtra("orderStatus", 0);
		isShowHome = getIntent().getBooleanExtra("isShowHome", true);
		initImageLoader();
		initViews();
		getOrderInfoData();
	}

	private void initViews() {
		findViews();
		initListeners();
		if (isShowHome) {
			backHomeBtn.setVisibility(View.VISIBLE);
			deleteBtn.setVisibility(View.GONE);
		} else {
			backBtn.setVisibility(View.VISIBLE);
		}
		if (orderStatus != 20) {
			deleteBtn.setEnabled(false);
			deleteBtn.setBackgroundResource(R.drawable.login_btn_bg_pressed);
			deleteBtn.setTextColor(getResources().getColor(R.color.line_color));
		}
	}

	private void findViews() {
		landiCodeRellay = (RelativeLayout) findViewById(R.id.order_landi_code_rellay);
		orderCommenPhoto = (ImageView) findViewById(R.id.order_commen_photo);
		photoFileSizeTv = (TextView) findViewById(R.id.order_file_size);
		photoSizeTv = (TextView) findViewById(R.id.order_photo_size);
		orderAffairName = (TextView) findViewById(R.id.order_affair_name);
		photoScoreTv = (TextView) findViewById(R.id.order_photo_score);
		orderNumberTv = (TextView) findViewById(R.id.order_number_text);
		orderCommitDateTv = (TextView) findViewById(R.id.order_commit_date);
		orderLandiCodeTv = (TextView) findViewById(R.id.order_landi_code);
		orderAddressTv = (TextView) findViewById(R.id.order_commit_address);
		showAffairDetailBtn = (TextView) findViewById(R.id.order_show_affair_detail);
		commitBtn = (TextView) findViewById(R.id.commit_btn);
		orderStatusProgressBar = (ProgressBar) findViewById(R.id.order_status_progressbar);
		backHomeBtn = (TextView) findViewById(R.id.back_home_btn);
		deleteBtn = (TextView) findViewById(R.id.delete_btn);
		backBtn = (ImageButton) findViewById(R.id.back_btn);
		orderAffairCode = (TextView) findViewById(R.id.order_affair_code);
	}

	private void initListeners() {
		commitBtn.setOnClickListener(this);
		backHomeBtn.setOnClickListener(this);
		landiCodeRellay.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
	}

	private void initOrderData() {
		imageLoader.displayImage(orderInfo.getPhoto_valid_url(), orderCommenPhoto, options);
		orderAffairName.setText(orderInfo.getAffairName());
		orderAffairCode.setText(orderInfo.getAffairCode());
		// photoFileSizeTv.setText("100k");
		if (orderInfo.getValidate_score() != 0) {
			photoScoreTv.setText("比对分值：" + orderInfo.getValidate_score());
		} else {
			photoScoreTv.setBackgroundResource(R.color.transparent);
		}
		orderNumberTv.setText(orderInfo.getOrderNo());
		if (orderInfo.getPhoto_info().length() == 0) {
			orderAddressTv.setText("移动端拍摄");
		} else {
			orderAddressTv.setText(orderInfo.getPhoto_info());
		}

		orderCommitDateTv.setText(DateUtil.doubleTimeToString(orderInfo.getTake_date(), "yyyy.MM.dd"));
		orderLandiCodeTv.setText(orderInfo.getPhotograph_code());
		if (orderStatus == 0) {
			orderStatusProgressBar.setProgress(50);
		} else if (orderStatus == 20) {
			orderStatusProgressBar.setProgress(100);
		}
		if (orderInfo.getPhoto_spec() != null) {
			showAffairDetailBtn.setText(orderInfo.getPhoto_spec().getName());
			photoSizeTv.setText(orderInfo.getPhoto_spec().getPx_width() + " X "
					+ orderInfo.getPhoto_spec().getPx_height() + " Pix");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_btn:
			setResult(1);
			finish();
			break;
		case R.id.back_home_btn:
			setResult(1);
			finish();
			break;
		case R.id.order_landi_code_rellay: {
			if (orderInfo.getPhotograph_code().length() != 0) {
				Intent intent = new Intent(this, RnapCodeActivity.class);
				intent.putExtra("itemId", orderInfo.getPhotograph_code());
				startActivity(intent);
			}
		}
			break;
		case R.id.delete_btn:
			showDeleteDialog();
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	private void getOrderInfoData() {
		WebDataRequest.requestGet(0, handler, "order/detail?id=" + orderId);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				switch (msg.arg1) {
				case 0:
					try {
						orderInfo = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
								new TypeReference<OrderInfo>() {
								});
						if (orderInfo != null) {
							initOrderData();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 1:
					showToast("事务删除成功！");
					setResult(2);
					finish();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	private void showDeleteDialog() {
		DeleteDialog deleteDialog = new DeleteDialog(this);
		deleteDialog.setConfirmClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				deleteOrder();
			}
		});
		deleteDialog.setCancelClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				dialog.cancel();
			}
		});
		deleteDialog.show();
	}

	private void deleteOrder() {
		WebDataRequest.requestGet(1, handler, "order/delete?id=" + orderId);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isShowHome) {
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
