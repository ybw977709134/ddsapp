package com.edonesoft.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.listeners.OnDegreesChangeListener;
import com.edonesoft.model.OrderCommitModel;
import com.edonesoft.model.PhotoFormat;
import com.edonesoft.adapter.TakePictureSizeListAdapter;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.utils.FileUtil;
import com.edonesoft.utils.ImageUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.edonesoft.views.CropImageRectView;
import com.edonesoft.views.CropImageView;
import com.edonesoft.views.RotateImageView;
import com.edonesoft.views.CropImageRectView.CropParam;
import com.edonesoft.views.dialog.LoadingDialog;
import com.edonesoft.views.HorizontalListView;

@SuppressLint("HandlerLeak")
public class ModifyPictureActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private TextView commitBtn;
	private CropImageView cropImageView;
	private CropImageRectView cropImageRectView;
	private TextView sizeTv;
	private HorizontalListView sizeListview;
	private TextView selectPhotoFormatBtn;
	private TextView restorePhotoBtn;
	private LoadingDialog loadingDialog;
	private RotateImageView rotateImageView;
	private TextView rotateLeftView;
	private TextView rotateRightView;

	private TakePictureSizeListAdapter listAdapter;
	private List<PhotoFormat> photoFormatCommenList;
	private OrderCommitModel orderCommitModel;

	private Bitmap imageBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_pic);
		orderCommitModel = getIntent().getParcelableExtra("orderCommitModel");
		imageBitmap = ImageUtil.getRotateBitmap(orderCommitModel.getPhoto_common());
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		getPhotoFommatData();
		cropImageView.setImageBitmap(imageBitmap);
	}

	private void findViews() {
		cropImageRectView = (CropImageRectView) findViewById(R.id.modify_pic_image_rect);
		rotateImageView = (RotateImageView) findViewById(R.id.modify_pic_rotate_view);
		commitBtn = (TextView) findViewById(R.id.commit_btn);
		cropImageView = (CropImageView) findViewById(R.id.modify_pic_image_view);
		sizeTv = (TextView) findViewById(R.id.modify_pic_photoformat_text);
		sizeListview = (HorizontalListView) findViewById(R.id.modify_pic_photoformat_listview);
		rotateLeftView = (TextView) findViewById(R.id.modify_pic_rotate_left_view);
		rotateRightView = (TextView) findViewById(R.id.modify_pic_rotate_right_view);
		selectPhotoFormatBtn = (TextView) findViewById(R.id.modify_pic_select_photoformat);
		restorePhotoBtn = (TextView) findViewById(R.id.modify_pic_restore);
	}

	private void initListeners() {
		commitBtn.setOnClickListener(this);
		selectPhotoFormatBtn.setOnClickListener(this);
		restorePhotoBtn.setOnClickListener(this);
		rotateLeftView.setOnClickListener(this);
		rotateRightView.setOnClickListener(this);
		rotateImageView.setDegreesChangeListener(new OnDegreesChangeListener() {
			@Override
			public void onDegreesChanged(int degrees) {
				cropImageView.rotate(degrees + 15);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_btn: {
			createFinalPhoto(false);
		}
			break;
		case R.id.modify_pic_select_photoformat: {
			Intent intent = new Intent(this, SelectPhotoFormatActivity.class);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.modify_pic_restore:
			rotateImageView.setDegrees(-15);
			cropImageView.initBmpPosition();
			break;
		case R.id.modify_pic_rotate_left_view:
			rotateImageView.setDegrees(rotateImageView.degrees - 1);
			break;
		case R.id.modify_pic_rotate_right_view:
			rotateImageView.setDegrees(rotateImageView.degrees + 1);
			break;
		}
	}

	private void getPhotoFommatData() {
		WebDataRequest.requestGet(0, handler, "system/photo/spec/list?id=-1");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 0) {
				String responseText = msg.getData().getString("ResponseText");
				if (WebDataRequestHelper.validateJsonResponse(msg)) {
					try {
						photoFormatCommenList = JSON.parseObject(new JSONObject(responseText).optString("Detail"),
								new TypeReference<ArrayList<PhotoFormat>>() {
								});
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (photoFormatCommenList != null && photoFormatCommenList.size() > 0) {
						initCommenPhotoFormatList();
					}
				}
			} else if (msg.arg1 == 1) {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				Intent intent = new Intent(ModifyPictureActivity.this, PreviewCommitPicActivity.class);
				intent.putExtra("orderCommitModel", orderCommitModel);
				startActivityForResult(intent, 1);
			}
			super.handleMessage(msg);
		}
	};

	private void initCommenPhotoFormatList() {
		listAdapter = new TakePictureSizeListAdapter(this);
		listAdapter.getDataList().addAll(photoFormatCommenList);
		sizeListview.setAdapter(listAdapter);
		setListViewWidthBasedOnChildren(sizeListview);
		sizeListview.setOnItemClickListener(this);

		if (orderCommitModel.getPhoto_spec_id() != 0) {
			for (int i = 0; i < photoFormatCommenList.size(); i++) {
				if (photoFormatCommenList.get(i).getItemID() == orderCommitModel.getPhoto_spec_id()) {
					updateSelectPhotoFormat(i);
				}
			}
		} else {
			updateSelectPhotoFormat(0);
		}
	}

	public int setListViewWidthBasedOnChildren(HorizontalListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return 0;
		}
		int totalWidth = 0;
		int count = listAdapter.getCount();
		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalWidth += listItem.getMeasuredWidth();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.width = totalWidth;
		listView.setLayoutParams(params);
		return totalWidth;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (orderCommitModel.getPhoto_spec_id() == 0) {
			updateSelectPhotoFormat(position);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 0) {
			if (data != null) {
				if (listAdapter.getDataList().size() != 0) {
					PhotoFormat photoFormat = (PhotoFormat) data.getSerializableExtra("photoFormat");
					listAdapter.getDataList().add(0, photoFormat);
					updateSelectPhotoFormat(0);
				}
			}
		}
		if (requestCode == 1) {
			if (resultCode == 1) {
				setResult(1);
				finish();
			} else if (resultCode == 2) {
				setResult(2);
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateSelectPhotoFormat(int position) {
		PhotoFormat photoFormat = listAdapter.getDataList().get(position);
		sizeTv.setText(photoFormat.getPx_width() + " X " + photoFormat.getPx_height() + " Pix");
		listAdapter.setSelectIndex(position);
		listAdapter.notifyDataSetChanged();

		if (imageBitmap != null) {
			CropParam param = new CropParam();
			param.mAspectX = photoFormat.getPx_width();
			param.mAspectY = photoFormat.getPx_height();
			param.head_top = photoFormat.getHead_top();
			param.chin_bottom = photoFormat.getChin_bottom();
			cropImageRectView.initialize(param);
			cropImageView.updateClipRect(cropImageRectView.mCropWindow.left(), cropImageRectView.mCropWindow.top());
		}
	}

	private void createFinalPhoto(final boolean isAuto) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap finalBitmap = null;
				if (isAuto) {
					finalBitmap = ImageUtil.automaticCropBitmap(imageBitmap, false);
				} else {
					finalBitmap = ImageUtil.zoomBitmap(cropImageView.clipImage(),
							cropImageRectView.mCropParam.mAspectX, cropImageRectView.mCropParam.mAspectY);
				}
				if (finalBitmap == null) {
					showToast("自动剪裁错误，请重新剪裁!");
					return;
				}
				saveFinalPhoto(finalBitmap);
				Message message = new Message();
				message.arg1 = 1;
				handler.sendMessage(message);
			}
		}).start();
	}

	private void saveFinalPhoto(Bitmap bitmap) {
		String finalPath = FileUtil.getFileName(orderCommitModel.getPhoto_common()) + "final.jpg";
		FileUtil.saveBitmap(finalPath, bitmap);
		orderCommitModel.setPhoto_final(finalPath);
		if (listAdapter != null && listAdapter.getDataList().size() > 0) {
			PhotoFormat photoFormat = listAdapter.getDataList().get(listAdapter.getSelectIndex());
			if (orderCommitModel.getAffair_id() == 0) {
				orderCommitModel.setPhoto_spec_id(photoFormat.getItemID());
				orderCommitModel.setPx_width(photoFormat.getPx_width());
				orderCommitModel.setPx_height(photoFormat.getPx_height());
				orderCommitModel.setName(photoFormat.getName());
			}
		}
	}
}
