package com.edonesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.edonesoft.base.BaseActivity;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.OrderCommitModel;
import com.edonesoft.utils.ImageUtil;

public class PreviewCommitPicActivity extends BaseActivity {
	private ImageView commitPic;
	private TextView commitBtn;

	private OrderCommitModel orderCommitModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_commit_pic);
		orderCommitModel = getIntent().getParcelableExtra("orderCommitModel");
		initViews();
	}

	private void initViews() {
		commitPic = (ImageView) findViewById(R.id.preview_commit_pic_image);
		commitBtn = (TextView) findViewById(R.id.commit_btn);

		commitPic.setImageBitmap(ImageUtil.getRotateBitmap(orderCommitModel.getPhoto_final()));
		commitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PreviewCommitPicActivity.this, CalculatePriceActivity.class);
				intent.putExtra("orderCommitModel", orderCommitModel);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
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
}
