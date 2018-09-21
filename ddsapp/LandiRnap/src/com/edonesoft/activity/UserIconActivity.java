package com.edonesoft.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edonesoft.app.AppConfig;
import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.DensityUtil;
import com.edonesoft.utils.FileUtil;
import com.edonesoft.utils.ImageUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

@SuppressLint("HandlerLeak")
public class UserIconActivity extends BaseActivity implements OnClickListener {
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private TextView userNameTv;
	private ImageView userIcon;
	private RelativeLayout albumRellay;
	private RelativeLayout cameraRellay;

	private String cropImagePath;
	private String cameraPhotoPath;
	private File cropImageFile;
	private File cameraPhotoFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_icon);
		initImageLoader();
		initViews();
	}

	private void initViews() {
		userNameTv = (TextView) findViewById(R.id.profile_nickname);
		userIcon = (ImageView) findViewById(R.id.profile_usericon);
		albumRellay = (RelativeLayout) findViewById(R.id.profile_album_icon_rellay);
		cameraRellay = (RelativeLayout) findViewById(R.id.profile_camera_icon_rellay);

		userNameTv.setText(AppConfig.sharedInstance().CurrentDisplayName);
		imageLoader.displayImage(AppConfig.sharedInstance().CurrentIconUrl, userIcon, options);
		albumRellay.setOnClickListener(this);
		cameraRellay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profile_album_icon_rellay:
			getPhotoFromGallery();
			break;
		case R.id.profile_camera_icon_rellay:
			getPhotoFromCamera();
			break;
		}
	}

	public void getPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 0);
	}

	public void getPhotoFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraPhotoFile));
		startActivityForResult(intent, 1);
	}

	@SuppressWarnings("deprecation")
	public void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.user_default_icon)
				.displayer(new RoundedBitmapDisplayer(DensityUtil.dip2px(this, 80)))
				.showImageForEmptyUri(R.drawable.user_default_icon).showImageOnFail(R.drawable.user_default_icon)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).build();
		String protraitFilePath = FileUtil.getDataPath() + "protrait/";
		File protraitFile = new File(protraitFilePath);
		if (!protraitFile.exists()) {
			protraitFile.mkdirs();
		}
		cameraPhotoPath = protraitFilePath + "cameraImage.jpg";
		cropImagePath = protraitFilePath + "cropImage.jpg";
		cameraPhotoFile = new File(cameraPhotoPath);
		cropImageFile = new File(cropImagePath);
	}

	public void cropImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropImageFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		startActivityForResult(intent, 3);
	}

	public void updateProfilePortrait(String iconPath) {
		WebDataRequest.requestPost(1, handler, "profile/edit/portrait", "\"" + iconPath + "\"");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 0) {
				Uri selectedImage = data.getData();
				cropImage(selectedImage);
			}
			if (requestCode == 1) {
				Uri selectedImage = Uri.fromFile(cameraPhotoFile);
				cropImage(selectedImage);
			}
			if (requestCode == 3) {
				uploadOwnerPortrait();
			}
		}
	}

	/**
	 * 上传游客肖像
	 */
	public void uploadOwnerPortrait() {
		String suffix = cropImagePath.substring(cropImagePath.lastIndexOf(".") + 1, cropImagePath.length());
		byte[] imageBytes = ImageUtil.BitmapToBytes(ImageUtil.getBitmap(cropImagePath), suffix,100);
		WebDataRequest.requestUploadFile(0, handler, imageBytes);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String responseText = msg.getData().getString("ResponseText");
			switch (msg.arg1) {
			case 0:// 上传头像文件
				if (WebDataRequestHelper.validateJsonResponse(msg)) {
					String iconUrl = WebDataRequestHelper.getJsonObject(responseText).optString("Detail");
					if (userIcon != null) {
						updateProfilePortrait(iconUrl);
						imageLoader.displayImage(iconUrl, userIcon, options);
					}
				}
				break;
			case 1:
				showToast("头像修改成功!");
				break;
			}
		}
	};

}
