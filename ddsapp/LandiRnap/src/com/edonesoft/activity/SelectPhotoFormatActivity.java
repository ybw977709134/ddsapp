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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.adapter.PhotoFormatGridViewAdapter;
import com.edonesoft.adapter.PhotoFormatTypeAdapter;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.base.BaseDialog;
import com.edonesoft.base.BaseDialog.OnDialogClickListener;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.PhotoFormat;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.HorizontalListView;
import com.edonesoft.views.dialog.CommenDialog;

@SuppressLint("HandlerLeak")
public class SelectPhotoFormatActivity extends BaseActivity implements OnItemClickListener {
	private GridView gridView;
	private HorizontalListView typeListView;

	private PhotoFormatGridViewAdapter gridViewAdapter;
	private PhotoFormatTypeAdapter typeAdapter;

	private List<PhotoFormat> typeList;
	private List<PhotoFormat> photoFormatList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_photoformat);
		initViews();
		getTypeData();
	}

	private void initViews() {
		gridView = (GridView) findViewById(R.id.select_photoformat_gridview);
		typeListView = (HorizontalListView) findViewById(R.id.select_photoformat_type_list);
		gridViewAdapter = new PhotoFormatGridViewAdapter(this);
		typeAdapter = new PhotoFormatTypeAdapter(this);
	}

	private void getPhotoFormatDataByParent(int parentId) {
		WebDataRequest.requestGet(1, handler, "system/photo/spec/list?id=" + parentId);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == R.id.select_photoformat_gridview) {
			if (!photoFormatList.get(position).getChannel_accepted().contains("1")) {
				showTipDialog(photoFormatList.get(position).getItemID());
				return;
			}
			Intent data = new Intent();
			data.putExtra("photoFormat", photoFormatList.get(position));
			setResult(0, data);
			finish();
		} else if (parent.getId() == R.id.select_photoformat_type_list) {
			getPhotoFormatDataByParent(typeList.get(position).getItemID());
			typeAdapter.setSelectIndex(position);
			typeAdapter.notifyDataSetChanged();
		}
	}

	private void getTypeData() {
		WebDataRequest.requestGet(0, handler, "system/photo/spec/list?id=0");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				switch (msg.arg1) {
				case 0:
					try {
						typeList = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
								new TypeReference<List<PhotoFormat>>() {
								});
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (typeList != null && typeList.size() > 0) {
						initTypeListData();
						getPhotoFormatDataByParent(typeList.get(0).getItemID());
					}
					break;
				case 1:
					try {
						photoFormatList = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
								new TypeReference<List<PhotoFormat>>() {
								});
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (photoFormatList != null && photoFormatList.size() > 0) {
						updatePhotoFormatListData();
					}
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	private void updatePhotoFormatListData() {
		gridViewAdapter.getDataList().clear();
		gridViewAdapter.getDataList().addAll(photoFormatList);
		gridViewAdapter.notifyDataSetChanged();
	}

	private void initTypeListData() {
		typeAdapter.getDataList().addAll(typeList);
		typeListView.setAdapter(typeAdapter);
		gridView.setAdapter(gridViewAdapter);
		typeListView.setOnItemClickListener(this);
		gridView.setOnItemClickListener(this);
	}

	private void showTipDialog(final int itemId) {
		CommenDialog dialog = new CommenDialog(this);
		dialog.setTitleText("提示");
		dialog.setContentText("该照片规格不支持手机拍摄，是否进入网点搜索界面");
		dialog.setCancelClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				dialog.dismiss();
			}
		});
		dialog.setConfirmClickListener(new OnDialogClickListener() {
			@Override
			public void onClick(BaseDialog dialog) {
				Intent intent = new Intent(SelectPhotoFormatActivity.this, SearchStationActivity.class);
				intent.putExtra("spec_id", itemId);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
