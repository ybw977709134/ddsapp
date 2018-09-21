package com.edonesoft.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private double Birthday;
	private String Client_name;
	private double CreateDate;
	private String Email;
	private String EncryptPassword;
	private int Gender;
	private String Id_card;
	private int ItemID;
	private String Mobilephone;
	private String Name;
	private String Portrait;
	private int Status;

	public double getBirthday() {
		return Birthday;
	}

	public void setBirthday(double birthday) {
		Birthday = birthday;
	}

	public String getClient_name() {
		return Client_name;
	}

	public void setClient_name(String client_name) {
		Client_name = client_name;
	}

	public double getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(double createDate) {
		CreateDate = createDate;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getEncryptPassword() {
		return EncryptPassword;
	}

	public void setEncryptPassword(String encryptPassword) {
		EncryptPassword = encryptPassword;
	}

	public int getGender() {
		return Gender;
	}

	public void setGender(int gender) {
		Gender = gender;
	}

	public String getId_card() {
		return Id_card;
	}

	public void setId_card(String id_card) {
		Id_card = id_card;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public String getMobilephone() {
		return Mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		Mobilephone = mobilephone;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPortrait() {
		return Portrait;
	}

	public void setPortrait(String portrait) {
		Portrait = portrait;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
