package com.edonesoft.model;

import java.io.Serializable;

public class Affair implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Affair_code;
	private String Affair_intro;
	private double Create_date;
	private double Effective_date;
	private double Expire_date;
	private String Icon_url;
	private boolean Is_public;
	private int ItemID;
	private int Max_customer_count;
	private int Min_dpi;
	private int Min_validate_score;
	private String Name;
	private String richtext_content;
	private PhotoFormat Photo_spec;
	private Company Company;
	private int CurrentCustomerCount;

	public int getCurrentCustomerCount() {
		return CurrentCustomerCount;
	}

	public void setCurrentCustomerCount(int currentCustomerCount) {
		CurrentCustomerCount = currentCustomerCount;
	}

	public PhotoFormat getPhoto_spec() {
		return Photo_spec;
	}

	public void setPhoto_spec(PhotoFormat photo_spec) {
		Photo_spec = photo_spec;
	}

	public int getMin_dpi() {
		return Min_dpi;
	}

	public void setMin_dpi(int min_dpi) {
		Min_dpi = min_dpi;
	}

	public int getMin_validate_score() {
		return Min_validate_score;
	}

	public void setMin_validate_score(int min_validate_score) {
		Min_validate_score = min_validate_score;
	}

	public String getAffair_code() {
		return Affair_code;
	}

	public void setAffair_code(String affair_code) {
		Affair_code = affair_code;
	}

	public String getAffair_intro() {
		return Affair_intro;
	}

	public void setAffair_intro(String affair_intro) {
		Affair_intro = affair_intro;
	}

	public double getCreate_date() {
		return Create_date;
	}

	public void setCreate_date(double create_date) {
		Create_date = create_date;
	}

	public double getEffective_date() {
		return Effective_date;
	}

	public void setEffective_date(double effective_date) {
		Effective_date = effective_date;
	}

	public double getExpire_date() {
		return Expire_date;
	}

	public void setExpire_date(double expire_date) {
		Expire_date = expire_date;
	}

	public String getIcon_url() {
		return Icon_url;
	}

	public void setIcon_url(String icon_url) {
		Icon_url = icon_url;
	}

	public boolean isIs_public() {
		return Is_public;
	}

	public void setIs_public(boolean is_public) {
		Is_public = is_public;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public int getMax_customer_count() {
		return Max_customer_count;
	}

	public void setMax_customer_count(int max_customer_count) {
		Max_customer_count = max_customer_count;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getRichtext_content() {
		return richtext_content;
	}

	public void setRichtext_content(String richtext_content) {
		this.richtext_content = richtext_content;
	}

	public Company getCompany() {
		return Company;
	}

	public void setCompany(Company company) {
		Company = company;
	}
}
