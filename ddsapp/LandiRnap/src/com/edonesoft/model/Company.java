package com.edonesoft.model;

import java.io.Serializable;

public class Company implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Company_intro;
	private String Company_size;
	private double Create_date;
	private String Email;
	private double Foundation_date;
	private int ItemID;
	private String Loc_address;
	private String Loc_zipcode;
	private String Logo_url;
	private int Manager_id;
	private String Name;
	private String Phone_number;
	private int Status;

	public String getCompany_intro() {
		return Company_intro;
	}

	public void setCompany_intro(String company_intro) {
		Company_intro = company_intro;
	}

	public String getCompany_size() {
		return Company_size;
	}

	public void setCompany_size(String company_size) {
		Company_size = company_size;
	}

	public double getCreate_date() {
		return Create_date;
	}

	public void setCreate_date(double create_date) {
		Create_date = create_date;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public double getFoundation_date() {
		return Foundation_date;
	}

	public void setFoundation_date(double foundation_date) {
		Foundation_date = foundation_date;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
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

	public String getLogo_url() {
		return Logo_url;
	}

	public void setLogo_url(String logo_url) {
		Logo_url = logo_url;
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

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
