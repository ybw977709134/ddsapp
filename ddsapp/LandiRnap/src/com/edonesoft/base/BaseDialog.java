package com.edonesoft.base;

import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.OptAnimationLoader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

public abstract class BaseDialog extends Dialog implements android.view.View.OnClickListener {
	private View mDialogView;
	private AnimationSet mModalInAnim;
	private AnimationSet mModalOutAnim;

	public OnDialogClickListener mCancelClickListener;
	public OnDialogClickListener mConfirmClickListener;

	public static interface OnDialogClickListener {
		public void onClick(BaseDialog dialog);
	}

	public BaseDialog(Context context) {
		this(context, 0);
	}

	public BaseDialog(Context context, int theme) {
		super(context, R.style.alert_dialog);
		setCancelable(false);
		setCanceledOnTouchOutside(false);

		mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
		mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
		mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mDialogView.post(new Runnable() {
					@Override
					public void run() {
						dismiss();
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mDialogView.startAnimation(mModalInAnim);
	}

	protected void dismissWithAnimation() {
		mDialogView.startAnimation(mModalOutAnim);
	}

	public BaseDialog setCancelClickListener(OnDialogClickListener listener) {
		mCancelClickListener = listener;
		return this;
	}

	public BaseDialog setConfirmClickListener(OnDialogClickListener listener) {
		mConfirmClickListener = listener;
		return this;
	}

	@Override
	public void cancel() {
		dismissWithAnimation();
	}
}
