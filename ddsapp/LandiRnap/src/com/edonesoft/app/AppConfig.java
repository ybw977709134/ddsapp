package com.edonesoft.app;

import com.edonesoft.utils.StringUtil;

import android.content.Context;
import android.content.SharedPreferences;

public class AppConfig {
	private static AppConfig _config;

	public static AppConfig sharedInstance() {
		if (_config == null) {
			_config = new AppConfig();

			SharedPreferences preferences = App.appContext.getSharedPreferences("RnapUserInfo", Context.MODE_PRIVATE);
			_config.CurrentUID = preferences.getInt("CurrentMember_ID", 0);
			_config.CurrentPhone = preferences.getString("CurrentMember_Phone", "anonymous");
			_config.CurrentEmail = preferences.getString("CurrentMember_Email", "");
			_config.CurrentDisplayName = preferences.getString("CurrentMember_DisplayName", "");
			_config.CurrentMemeberGrade = preferences.getInt("CurrentMember_MemberGrade", 0);
			_config.CurrentIconUrl = preferences.getString("CurrentMember_IconUrl", "");

			_config.Token = preferences.getString("Access_Token", "");
			_config.Latitude = preferences.getFloat("Location_Latitude", 0);
			_config.Longitude = preferences.getFloat("Location_Longitude", 0);
			_config.Address = preferences.getString("Location_Address", "address");
			_config.idCard = preferences.getString("ID_Card", "address");
			_config.IsFirstLoad = preferences.getBoolean("IsFirstLoad", true);

			_config.realName = preferences.getString("Real_Name", "address");
		}
		return _config;
	}

	public int CurrentUID;
	public String CurrentPhone;
	public String CurrentEmail;
	public String CurrentDisplayName;
	public int CurrentMemeberGrade;
	public String CurrentIconUrl;
	public String realName;
	public String idCard;
	public String Token;
	public double Latitude;
	public double Longitude;
	public String Address;

	public boolean IsFirstLoad;

	public void saveToPreference() {
		SharedPreferences preferences = App.appContext.getSharedPreferences("RnapUserInfo", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = preferences.edit();
		edit.putString("CurrentMember_Phone", this.CurrentPhone);
		edit.putString("CurrentMember_Email", this.CurrentEmail);
		edit.putString("CurrentMember_DisplayName", this.CurrentDisplayName);
		edit.putString("CurrentMember_Email", this.CurrentEmail);
		edit.putInt("CurrentMember_MemberGrade", this.CurrentMemeberGrade);
		edit.putString("CurrentMember_IconUrl", this.CurrentIconUrl);

		edit.putString("Access_Token", this.Token);
		edit.putFloat("Location_Latitude", (float) this.Latitude);
		edit.putFloat("Location_Longitude", (float) this.Longitude);
		edit.putString("Location_Address", this.Address);

		edit.putBoolean("IsFirstLoad", this.IsFirstLoad);
		edit.putString("ID_Card", idCard);
		edit.putString("Real_Name", realName);
		edit.commit();
	}

	public void userLogout() {
		this.Address="";
		this.CurrentPhone="";
		this.CurrentUID = 0;
		this.CurrentEmail = "";
		this.CurrentDisplayName = "";
		this.realName = "";
		this.idCard = "";
		this.Token = "";
		this.CurrentMemeberGrade = 0;
		this.CurrentIconUrl = "";
		saveToPreference();
	}

	public boolean isUserLogin() {
		return !StringUtil.isNullOrEmpty(this.Token);
	}

	public boolean isFirstLoad() {
		return this.IsFirstLoad;
	}
}