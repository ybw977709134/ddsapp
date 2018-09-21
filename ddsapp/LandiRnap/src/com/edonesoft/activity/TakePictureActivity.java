package com.edonesoft.activity;

import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderCommitModel;
import com.edonesoft.model.PhotoFormat;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.utils.ImageUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.edonesoft.views.CameraView;
import com.edonesoft.views.dialog.LoadingDialog;
import com.edonesoft.views.dialog.TakePhotoGuideDialog;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class TakePictureActivity extends BaseActivity implements OnClickListener, CameraView.OnCameraSelectListener {
	private ImageButton flashLightBtn;
	private ImageButton reverseBtn;
	private ImageView okBtn;
	private SurfaceView surfaceView;

	private CameraView cameraView;
	private LoadingDialog loadingDialog;

	private String photographCode;
	private OrderCommitModel orderCommitModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);
		showGuideDialog();
		photographCode = getIntent().getStringExtra("photographCode");
		orderCommitModel = new OrderCommitModel();
		orderCommitModel.setPhotograph_code(photographCode);
		int affairId = getIntent().getIntExtra("affairId", 0);
		orderCommitModel.setAffair_id(affairId);
		if (affairId != 0) {
			PhotoFormat photoFormat = (PhotoFormat) getIntent().getSerializableExtra("photoFormat");
			if (photoFormat != null) {
				orderCommitModel.setPhoto_spec_id(photoFormat.getItemID());
				orderCommitModel.setPx_width(photoFormat.getPx_width());
				orderCommitModel.setPx_height(photoFormat.getPx_height());
			}
			String affairName = getIntent().getStringExtra("affairName");
			if (affairName != null) {
				orderCommitModel.setName(affairName);
			}
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initViews();
	}

	private void initViews() {
		findViews();
		initListeners();
		try {
			cameraView = new CameraView(this);
			cameraView.setOnCameraSelectListener(this);
			cameraView.setCameraView(surfaceView, 43);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findViews() {
		flashLightBtn = (ImageButton) findViewById(R.id.take_picture_flashlight);
		reverseBtn = (ImageButton) findViewById(R.id.take_picture_reverse);
		okBtn = (ImageView) findViewById(R.id.take_picture_okbtn);
		surfaceView = (SurfaceView) findViewById(R.id.take_picture_surfaceview);
	}

	private void initListeners() {
		flashLightBtn.setOnClickListener(this);
		reverseBtn.setOnClickListener(this);
		okBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_picture_flashlight:
			if (cameraView.flash_type == CameraView.FLASH_OFF) {
				cameraView.changeFlash(CameraView.FLASH_ON);
			} else {
				cameraView.changeFlash(CameraView.FLASH_OFF);
			}
			break;
		case R.id.take_picture_reverse:
			cameraView.changeCamera();
			break;
		case R.id.take_picture_okbtn:
			showLoadingDialog();
			cameraView.takePicture();
			break;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			cameraView.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (cameraView != null)
			cameraView.onPause();
	}

	@Override
	public void onShake(int orientation) {

	}

	@Override
	public void onTakePicture(final boolean success, final String filePath) {
		if (success) {
			orderCommitModel.setPhoto_common(filePath);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Bitmap bitmap = ImageUtil.getRotateBitmap(filePath);
					Bitmap tmpBmp = bitmap.copy(Bitmap.Config.RGB_565, true);// 必须转换成rgb565
					// 人脸识别，寻找人脸
					FaceDetector faceDet = new FaceDetector(tmpBmp.getWidth(), tmpBmp.getHeight(), 1);
					FaceDetector.Face[] faceList = new FaceDetector.Face[1];
					faceDet.findFaces(tmpBmp, faceList);

					FaceDetector.Face face = faceList[0];
					if (face != null) {
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				}
			}).start();
		} else {
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			cameraView.onResume();
			showToast("相机拍照失败，请重新拍摄!");
		}
	}

	@Override
	public void onChangeFlashMode(int flashMode) {
		switch (flashMode) {
		case CameraView.FLASH_OFF:
			flashLightBtn.setImageResource(R.drawable.take_pic_camera_off);
			break;
		case CameraView.FLASH_ON:
			flashLightBtn.setImageResource(R.drawable.take_pic_camera_on);
			break;
		}
	}

	@Override
	public void onChangeCameraPosition(int camera_position) {
		if (camera_position == 1) {
			showToast("建议采用后置摄像头拍摄!");
		}
	}

	/** * 设置亮度 */
	public static void setBrightness(Activity activity, int brightness) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
		activity.getWindow().setAttributes(lp);
	}

	/** * 停止自动亮度调节 */
	public static void stopAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	private void showLoadingDialog() {
		loadingDialog = new LoadingDialog(this);
		loadingDialog.show();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			if (msg.what == 0) {
				Intent intent = new Intent(TakePictureActivity.this, ModifyPictureActivity.class);
				intent.putExtra("orderCommitModel", orderCommitModel);
				startActivityForResult(intent, 0);
			} else {
				cameraView.onResume();
				showToast("未找到符合规格的人脸头像，请重新拍摄!");
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			setResult(1);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showGuideDialog() {
		TakePhotoGuideDialog dialog = new TakePhotoGuideDialog(this);
		dialog.show();
	}
}
