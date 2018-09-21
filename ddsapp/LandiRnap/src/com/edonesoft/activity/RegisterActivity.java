package com.edonesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.edonesoft.base.BaseDialog;
import com.edonesoft.base.BaseDialog.OnDialogClickListener;
import com.edonesoft.fragment.RegisterFragment1;
import com.edonesoft.fragment.RegisterFragment2;
import com.edonesoft.fragment.RegisterFragment3;
import com.edonesoft.landi.rnap.activity.R;
import com.edonesoft.model.AddressItem;
import com.edonesoft.views.dialog.RegisterSuccessDialog;

public class RegisterActivity extends FragmentActivity {
	private FragmentManager fragmentManager;
	private RegisterFragment1 fragment1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getVerCode();
	}

	public void showRegisterSuccessDialog() {
		RegisterSuccessDialog dialog = new RegisterSuccessDialog(this);
		dialog.setCancelClickListener(new OnDialogClickListener() {

			@Override
			public void onClick(BaseDialog dialog) {
				setResult(1);
				finish();
			}
		});
		dialog.setConfirmClickListener(new OnDialogClickListener() {

			@Override
			public void onClick(BaseDialog dialog) {
				Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
				startActivity(intent);
				setResult(1);
				finish();
			}
		});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 1) {
			if (data != null) {
				AddressItem address = (AddressItem) data.getSerializableExtra("address");
				fragment1.setCountry(address.getNumber() + " " + address.getText());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getVerCode() {
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragment1 = new RegisterFragment1(this);
		fragmentTransaction.add(R.id.frameLayout, fragment1).commit();
	}

	public void writeVerCode(String phoneNumber) {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		RegisterFragment2 fragment2 = new RegisterFragment2(this, phoneNumber);
		fragmentTransaction.add(R.id.frameLayout, fragment2).commit();
	}

	public void writePwd(String phoneNumber, String vercode) {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		RegisterFragment3 fragment3 = new RegisterFragment3(this, phoneNumber, vercode);
		fragmentTransaction.add(R.id.frameLayout, fragment3).commit();
	}

	public void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}
