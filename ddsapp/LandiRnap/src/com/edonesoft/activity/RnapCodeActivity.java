package com.edonesoft.activity;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.DensityUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class RnapCodeActivity extends BaseActivity {
	private ImageView userCodeImage;
	private TextView userCodeTv;

	private String itemId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rnap_code);
		itemId = getIntent().getStringExtra("itemId");
		initViews();
	}

	private void initViews() {
		userCodeImage = (ImageView) findViewById(R.id.user_code_image);
		userCodeTv = (TextView) findViewById(R.id.user_code_text);
		if (itemId.length() != 0) {
			userCodeTv.setText("蓝证码：" + itemId);
			userCodeImage.setImageBitmap(createQRImage(itemId));
		}
	}

	/**
	 * 根据字符串生成二维码
	 */
	public Bitmap createQRImage(String url) {
		int QR_WIDTH = DensityUtil.dip2px(this, 150);
		try {
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_WIDTH);
			int[] pixels = new int[QR_WIDTH * QR_WIDTH];
			for (int y = 0; y < QR_WIDTH; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_WIDTH, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_WIDTH);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
