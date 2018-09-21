package com.edonesoft.model;

import java.io.Serializable;

public class Studio implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Email;
	private double Create_date;
	private int ItemID;
	private double LocLatitude;
	private double LocLongitude;
	private String Loc_address;
	private String Loc_zipcode;
	private int Manager_id;
	private String Name;
	private String Phone_number;
	private String Service_provided;
	private int Status;
	private String Studio_code;
	private String Studio_intro;
	private int Average_price;
	private String Icon_url;
	private int Star_count;
	private String Studio_type;

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public double getCreate_date() {
		return Create_date;
	}

	public void setCreate_date(double create_date) {
		Create_date = create_date;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public double getLocLatitude() {
		return LocLatitude;
	}

	public void setLocLatitude(double locLatitude) {
		LocLatitude = locLatitude;
	}

	public double getLocLongitude() {
		return LocLongitude;
	}

	public void setLocLongitude(double locLongitude) {
		LocLongitude = locLongitude;
	}

	public String getLoc_address() {
		return Loc_address;
	}

	public void setLoc_address(String loc_address) {
		Loc_address = loc_address;
	}

	public String getLoc_zipcode() {
		return Loc_zipcode;
	}

	public void setLoc_zipcode(String loc_zipcode) {
		Loc_zipcode = loc_zipcode;
	}

	public int getManager_id() {
		return Manager_id;
	}

	public void setManager_id(int manager_id) {
		Manager_id = manager_id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhone_number() {
		return Phone_number;
	}

	public void setPhone_number(String phone_number) {
		Phone_number = phone_number;
	}

	public String getService_provided() {
		return Service_provided;
	}

	public void setService_provided(String service_provided) {
		Service_provided = service_provided;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getStudio_code() {
		return Studio_code;
	}

	public void setStudio_code(String studio_code) {
		Studio_code = studio_code;
	}

	public String getStudio_intro() {
		return Studio_intro;
	}

	public void setStudio_intro(String studio_intro) {
		Studio_intro = studio_intro;
	}

	public int getAverage_price() {
		return Average_price;
	}

	public void setAverage_price(int average_price) {
		Average_price = average_price;
	}

	public String getIcon_url() {
		return Icon_url;
	}

	public void setIcon_url(String icon_url) {
		Icon_url = icon_url;
	}

	public int getStar_count() {
		return Star_count;
	}

	public void setStar_count(int star_count) {
		Star_count = star_count;
	}

	public String getStudio_type() {
		return Studio_type;
	}

	public void setStudio_type(String studio_type) {
		Studio_type = studio_type;
	}
}
