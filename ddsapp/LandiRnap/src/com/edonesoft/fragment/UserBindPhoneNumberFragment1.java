package com.edonesoft.fragment;

import com.edonesoft.activity.UserBindPhoneNumberActivity;
import com.edonesoft.landi.rnap.activity.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class UserBindPhoneNumberFragment1 extends Fragment {
	private UserBindPhoneNumberActivity mActivity;
	private RelativeLayout bindNewPhoneNumberRellay;

	public UserBindPhoneNumberFragment1() {
		super();
	}

	public UserBindPhoneNumberFragment1(UserBindPhoneNumberActivity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_bind_phone_number1, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		bindNewPhoneNumberRellay = (RelativeLayout) view.findViewById(R.id.bind_newphone_number_rellay);
		bindNewPhoneNumberRellay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.getVerCode();
			}
		});
	}
}
