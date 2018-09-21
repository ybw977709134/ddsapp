package com.edonesoft.activity;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.adapter.OrderListAdapter;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderListInfo;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.SuccessDialog;

@SuppressLint("HandlerLeak")
public class OrderListActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private OrderListAdapter ListAdapter;
	private List<OrderListInfo> orderList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		initViews();
	}

	private void initViews() {
		listView = (ListView) findViewById(R.id.order_list_listview);
		ListAdapter = new OrderListAdapter(this);
		listView.setAdapter(ListAdapter);
		listView.setOnItemClickListener(this);
		getPhotoData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, OrderFormActivity.class);
		intent.putExtra("orderId", orderList.get(position).getItemID());
		intent.putExtra("orderStatus", orderList.get(position).getStatus());
		intent.putExtra("isShowHome", false);
		startActivityForResult(intent, 0);
	}

	private void getPhotoData() {
		WebDataRequest.requestGet(0, handler, "order/list?page_size=50&page_index=0");
	}

	private void initListView() {
		ListAdapter.getDataList().clear();
		ListAdapter.getDataList().addAll(orderList);
		ListAdapter.notifyDataSetChanged();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				try {
					orderList = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
							new TypeReference<List<OrderListInfo>>() {
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (orderList == null || orderList.size() == 0) {
					showNoDataDialog();
					return;
				}
				initListView();
			}
			super.handleMessage(msg);
		}
	};

	private void showNoDataDialog() {
		SuccessDialog dialog = new SuccessDialog(this, "抱歉，您的订单列表为空", false);
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == 1) {
				finish();
			}
			if (resultCode == 2) {
				getPhotoData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
