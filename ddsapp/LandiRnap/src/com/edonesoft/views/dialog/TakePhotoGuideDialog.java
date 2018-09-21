package com.edonesoft.views.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.edonesoft.base.BaseDialog;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.DensityUtil;
import com.edonesoft.views.FramesSequenceAnimation;

@SuppressLint("UseSparseArrays")
public class TakePhotoGuideDialog extends BaseDialog implements OnCompletionListener {
	private RelativeLayout parentRellay;
	private ImageView animationIv;
	private ImageView animationMark1;
	private ImageView animationMark2;
	private ImageView animationMark3;
	private ImageView animationMark4;
	private TextView nextBtn;
	private int selectIndex;

	private FramesSequenceAnimation guideAnimation1 = null;
	private FramesSequenceAnimation guideAnimation2 = null;
	private FramesSequenceAnimation guideAnimation3 = null;
	private FramesSequenceAnimation guideAnimation4 = null;
	private MediaPlayer guideVoicePlayer1;
	private MediaPlayer guideVoicePlayer2;
	private MediaPlayer guideVoicePlayer3;
	private MediaPlayer guideVoicePlayer4;

	public TakePhotoGuideDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_take_photo_guide);
		setCancelable(true);

		findViews();
		resizeViews();
		initListeners();
		initAnimations();
		initVoices();
		changeMarkSelected(1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_animation_mark1:
			changeMarkSelected(1);
			break;
		case R.id.guide_animation_mark2:
			changeMarkSelected(2);
			break;
		case R.id.guide_animation_mark3:
			changeMarkSelected(3);
			break;
		case R.id.guide_animation_mark4:
			changeMarkSelected(4);
			break;
		case R.id.guide_animation_nextbtn:
			clickNextBtn();
			break;
		}
	}

	private void findViews() {
		parentRellay = (RelativeLayout) findViewById(R.id.dialog_parent);
		animationIv = (ImageView) findViewById(R.id.guide_animation_imageview);
		animationMark1 = (ImageView) findViewById(R.id.guide_animation_mark1);
		animationMark2 = (ImageView) findViewById(R.id.guide_animation_mark2);
		animationMark3 = (ImageView) findViewById(R.id.guide_animation_mark3);
		animationMark4 = (ImageView) findViewById(R.id.guide_animation_mark4);
		nextBtn = (TextView) findViewById(R.id.guide_animation_nextbtn);
	}

	private void initListeners() {
		animationMark1.setOnClickListener(this);
		animationMark2.setOnClickListener(this);
		animationMark3.setOnClickListener(this);
		animationMark4.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	private void initAnimations() {
		guideAnimation1 = new FramesSequenceAnimation(getContext(), animationIv, R.array.guide_animation1, 10);
		guideAnimation1.setOneShot(true);
		guideAnimation2 = new FramesSequenceAnimation(getContext(), animationIv, R.array.guide_animation2, 13);
		guideAnimation2.setOneShot(true);
		guideAnimation3 = new FramesSequenceAnimation(getContext(), animationIv, R.array.guide_animation3, 11);
		guideAnimation3.setOneShot(true);
		guideAnimation4 = new FramesSequenceAnimation(getContext(), animationIv, R.array.guide_animation4, 10);
		guideAnimation4.setOneShot(true);
	}

	/**
	 * 初始化声音播放器，和声音变量
	 */
	private void initVoices() {
		guideVoicePlayer1 = MediaPlayer.create(getContext(), R.raw.guide_voice1);
		guideVoicePlayer2 = MediaPlayer.create(getContext(), R.raw.guide_voice2);
		guideVoicePlayer3 = MediaPlayer.create(getContext(), R.raw.guide_voice3);
		guideVoicePlayer4 = MediaPlayer.create(getContext(), R.raw.guide_voice4);
		guideVoicePlayer1.setOnCompletionListener(this);
		guideVoicePlayer2.setOnCompletionListener(this);
		guideVoicePlayer3.setOnCompletionListener(this);
		guideVoicePlayer4.setOnCompletionListener(this);
	}

	@SuppressWarnings("deprecation")
	/**
	 * 重新计算dialog中的组件大小
	 */
	private void resizeViews() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int windowWidth = wm.getDefaultDisplay().getWidth();
		int windowHeight = wm.getDefaultDisplay().getHeight();

		FrameLayout.LayoutParams paramsParent = (LayoutParams) parentRellay.getLayoutParams();
		paramsParent.width = windowWidth - DensityUtil.dip2px(getContext(), 40);
		paramsParent.height = windowHeight - DensityUtil.dip2px(getContext(), 100);
		parentRellay.setLayoutParams(paramsParent);

		RelativeLayout.LayoutParams paramsImage = (android.widget.RelativeLayout.LayoutParams) animationIv
				.getLayoutParams();
		paramsImage.width = windowWidth - DensityUtil.dip2px(getContext(), 70);
		paramsImage.height = paramsImage.width / 16 * 19;
		animationIv.setLayoutParams(paramsImage);
	}

	/**
	 * 切换选中标签,并切换播放的动画和声音
	 * 
	 * @param index
	 */
	private void changeMarkSelected(int index) {
		stopAnimation();
		if (index == 1) {
			selectIndex = 1;
			animationMark1.setEnabled(false);
			animationMark2.setEnabled(true);
			animationMark3.setEnabled(true);
			animationMark4.setEnabled(true);

			nextBtn.setText("下一步");
			playAnimationAndVoice1();
		} else if (index == 2) {
			selectIndex = 2;
			animationMark1.setEnabled(true);
			animationMark2.setEnabled(false);
			animationMark3.setEnabled(true);
			animationMark4.setEnabled(true);

			nextBtn.setText("下一步");
			playAnimationAndVoice2();
		} else if (index == 3) {
			selectIndex = 3;
			animationMark1.setEnabled(true);
			animationMark2.setEnabled(true);
			animationMark3.setEnabled(false);
			animationMark4.setEnabled(true);
			nextBtn.setText("下一步");
			playAnimationAndVoice3();
		} else if (index == 4) {
			selectIndex = 4;
			animationMark1.setEnabled(true);
			animationMark2.setEnabled(true);
			animationMark3.setEnabled(true);
			animationMark4.setEnabled(false);
			nextBtn.setText("去拍照");
			playAnimationAndVoice4();
		}
	}

	/**
	 * 下一波按钮的点击处理
	 */
	private void clickNextBtn() {
		if (selectIndex == 1) {
			changeMarkSelected(2);
		} else if (selectIndex == 2) {
			changeMarkSelected(3);
		} else if (selectIndex == 3) {
			changeMarkSelected(4);
		} else if (selectIndex == 4) {
			cancel();
		}
	}

	/**
	 * 播放标签1的动画和声音
	 */
	private void playAnimationAndVoice1() {
		pauseVoice();
		guideVoicePlayer1.start();
		guideAnimation1.initAnimation();
		guideAnimation1.start();
	}

	/**
	 * 播放标签2的动画和声音
	 */
	private void playAnimationAndVoice2() {
		pauseVoice();
		guideVoicePlayer2.start();
		guideAnimation2.initAnimation();
		guideAnimation2.start();
	}

	/**
	 * 播放标签3的动画和声音
	 */
	private void playAnimationAndVoice3() {
		pauseVoice();
		guideVoicePlayer3.start();
		guideAnimation3.initAnimation();
		guideAnimation3.start();
	}

	/**
	 * 播放标签4的动画和声音
	 */
	private void playAnimationAndVoice4() {
		pauseVoice();
		guideVoicePlayer4.start();
		guideAnimation4.initAnimation();
		guideAnimation4.start();
	}

	/**
	 * 暂停正在播放的声音
	 * 
	 * @param index
	 */
	private void pauseVoice() {
		if (guideVoicePlayer1.isPlaying()) {
			guideVoicePlayer1.pause();
		} else if (guideVoicePlayer2.isPlaying()) {
			guideVoicePlayer2.pause();
		} else if (guideVoicePlayer3.isPlaying()) {
			guideVoicePlayer3.pause();
		} else if (guideVoicePlayer4.isPlaying()) {
			guideVoicePlayer4.pause();
		}
	}

	private void stopAnimation() {
		guideAnimation1.stop();
		guideAnimation2.stop();
		guideAnimation3.stop();
		guideAnimation4.stop();
	}

	@Override
	public void cancel() {
		stopAnimation();
		pauseVoice();
		super.cancel();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mp == guideVoicePlayer1) {
			changeMarkSelected(2);
		} else if (mp == guideVoicePlayer2) {
			changeMarkSelected(3);
		} else if (mp == guideVoicePlayer3) {
			changeMarkSelected(4);
		} else if (mp == guideVoicePlayer4) {
			cancel();
		}
	}
}
