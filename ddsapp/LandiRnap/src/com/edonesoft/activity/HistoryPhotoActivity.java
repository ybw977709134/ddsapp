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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.adapter.HistoryPhotoListAdapter;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.Affair;
import com.edonesoft.model.OrderCommitModel;
import com.edonesoft.model.Photo;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.dialog.SuccessDialog;

@SuppressLint("HandlerLeak")
public class HistoryPhotoActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private ImageButton gridBtn;
	private ImageButton listBtn;
	private GridView gridView;
	private ListView listView;

	private HistoryPhotoListAdapter gridAdapter;
	private HistoryPhotoListAdapter ListAdapter;
	private List<Photo> photoList;

	private boolean isSelectPhoto;
	private Affair affair;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_photo);
		isSelectPhoto = getIntent().getBooleanExtra("isSelectPhoto", false);
		if (isSelectPhoto) {
			affair = (Affair) getIntent().getSerializableExtra("affair");
		}
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		gridBtn.setEnabled(false);

		gridAdapter = new HistoryPhotoListAdapter(this, false);
		gridView.setAdapter(gridAdapter);
		ListAdapter = new HistoryPhotoListAdapter(this, true);
		listView.setAdapter(ListAdapter);

		if (affair != null && affair.getPhoto_spec() != null) {
			getPhotoData(affair.getPhoto_spec().getItemID());
		} else {
			getPhotoData(0);
		}
	}

	private void findViews() {
		gridBtn = (ImageButton) findViewById(R.id.history_photo_grid);
		listBtn = (ImageButton) findViewById(R.id.history_photo_list);
		gridView = (GridView) findViewById(R.id.history_photo_gridview);
		listView = (ListView) findViewById(R.id.history_photo_listview);
	}

	private void initListeners() {
		gridBtn.setOnClickListener(this);
		listBtn.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.history_photo_grid:
			gridBtn.setEnabled(false);
			listBtn.setEnabled(true);
			initGridView();
			break;
		case R.id.history_photo_list:
			listBtn.setEnabled(false);
			gridBtn.setEnabled(true);
			initListView();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (isSelectPhoto) {
			Intent intent = new Intent(this, CommitOrderActivity.class);
			OrderCommitModel orderCommitModel = new OrderCommitModel();
			orderCommitModel.setValidate_score(photoList.get(position).getValidate_score());
			orderCommitModel.setRequested_services(photoList.get(position).getRequested_services());
			orderCommitModel.setPhoto_common(photoList.get(position).getPhoto_common());
			orderCommitModel.setPhoto_final(photoList.get(position).getPhoto_final());
			orderCommitModel.setName(photoList.get(position).getName());
			if(affair!=null){
				orderCommitModel.setAffair_id(affair.getItemID());
				orderCommitModel.setPhoto_spec_id(affair.getPhoto_spec().getItemID());
				if(affair.getPhoto_spec()!=null){
					orderCommitModel.setPx_width(affair.getPhoto_spec().getPx_width());
					orderCommitModel.setPx_height(affair.getPhoto_spec().getPx_height());
				}
			}
			intent.putExtra("orderCommitModel", orderCommitModel);
			startActivityForResult(intent, 0);
		} else {
			Intent intent = new Intent(this, PhotoFormActivity.class);
			intent.putExtra("orderId", gridAdapter.getDataList().get(position).getOrder_id());
			intent.putExtra("photoId", gridAdapter.getDataList().get(position).getItemID());
			intent.putExtra("photoUrl", gridAdapter.getDataList().get(position).getPhoto_final());
			startActivityForResult(intent, 1);
		}
	}

	private void getPhotoData(int spec_id) {
		WebDataRequest.requestGet(0, handler, "order/photo/list?page_size=50&page_index=0&min_score=0&spec_id="
				+ spec_id);
	}

	private void initGridView() {
		if (photoList != null) {
			gridAdapter.getDataList().clear();
			gridAdapter.getDataList().addAll(photoList);
			gridAdapter.notifyDataSetChanged();
		}
		listView.setVisibility(View.GONE);
		gridView.setVisibility(View.VISIBLE);
	}

	private void initListView() {
		if (photoList != null) {
			ListAdapter.getDataList().clear();
			ListAdapter.getDataList().addAll(photoList);
			ListAdapter.notifyDataSetChanged();
		}
		gridView.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				try {
					photoList = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
							new TypeReference<List<Photo>>() {
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (photoList == null || photoList.size() == 0) {
					showNoDataDialog();
					return;
				}
				initGridView();
			}
			super.handleMessage(msg);
		}
	};

	private void showNoDataDialog() {
		SuccessDialog dialog = new SuccessDialog(this, "抱歉，您的证照库中没有符合要求的照片", false);
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			setResult(1);
			finish();
		}
		if (requestCode == 1 && resultCode == 1) {
			getPhotoData(0);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
