package com.edonesoft.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.edonesoft.activity.RegisterActivity;
import com.edonesoft.app.AppConfig;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.utils.StringUtil;
import com.edonesoft.utils.WebDataRequest;
import com.edonesoft.utils.WebDataRequestHelper;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class RegisterFragment3 extends Fragment {
	private RegisterActivity mActivity;

	private String phoneNumber;
	private String vercode;

	private EditText newPwdEt;
	private EditText rePwdEt;
	private TextView commitBtn;
	private ImageButton backBtn;

	public RegisterFragment3() {
		super();
	}

	public RegisterFragment3(RegisterActivity mActivity, String phoneNumber, String vercode) {
		super();
		this.mActivity = mActivity;
		this.vercode = vercode;
		this.phoneNumber = phoneNumber;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register3, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		newPwdEt = (EditText) view.findViewById(R.id.register_newpwd);
		rePwdEt = (EditText) view.findViewById(R.id.register_repwd);
		commitBtn = (TextView) view.findViewById(R.id.register_commit);
		backBtn = (ImageButton) view.findViewById(R.id.back_btn);
		commitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commitNewPwd();
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
	}

	private void requestRegister(String password) {
		String requestBody = "{\"Birthday\":0,\"ClientName\":\"android\",\"Gender\":1,\"MobilePhone\":\"" + phoneNumber
				+ "\",\"Name\":\"\",\"Password\":\"" + password + "\",\"Vcode\":\"" + vercode + "\"}";
		WebDataRequest.requestPost(0, handler, "sso/register", requestBody);
	}

	private void commitNewPwd() {
		String newPwd = newPwdEt.getText().toString();
		String rePwd = rePwdEt.getText().toString();

		if (StringUtil.isNullOrEmpty(newPwd)) {
			mActivity.showToast("请输入新密码！");
			return;
		}
		if (StringUtil.isNullOrEmpty(rePwd)) {
			mActivity.showToast("请输入确认密码！");
			return;
		}
		if (!newPwd.equals(rePwd)) {
			mActivity.showToast("两次输入的密码不一致！");
			return;
		}
		if (!newPwd.matches("[0-9A-Za-z]{6,20}$")) {
			mActivity.showToast("请输入正确的密码格式！");
			return;
		}
		requestRegister(newPwd);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String responseText = msg.getData().getString("ResponseText");
			if (WebDataRequestHelper.validateJsonResponse(msg)) {
				JSONObject responseJson = WebDataRequestHelper.getJsonObject(responseText);
				JSONObject json;
				try {
					json = responseJson.getJSONObject("Detail");
					AppConfig config = AppConfig.sharedInstance();
					config.CurrentUID = json.getInt("UserID");
					config.Token = json.getString("Token");
					config.saveToPreference();
				} catch (JSONException e) {
					mActivity.showToast("数据解析错误!");
				}
				commitSuccess();
			}
			super.handleMessage(msg);
		}
	};

	private void commitSuccess() {
		mActivity.showRegisterSuccessDialog();
	}
}
