package com.edonesoft.model;

import java.io.Serializable;

public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int Affair_id;
	private String Channel_accepted;
	private int Customer_id;
	private int ItemID;
	private String Name;
	private int Order_id;
	private String Photo_combined;
	private String Photo_common;
	private String Photo_final;
	private String Photo_original;
	private int Photo_spec_id;
	private int Photograph_channel;
	private String Photograph_code;
	private String Requested_services;
	private int Status;
	private double Taken_date;
	private String Taken_studio_code;
	private int Validate_score;
	private boolean IsRecentPhoto;

	public boolean isIsRecentPhoto() {
		return IsRecentPhoto;
	}

	public void setIsRecentPhoto(boolean isRecentPhoto) {
		IsRecentPhoto = isRecentPhoto;
	}

	public int getAffair_id() {
		return Affair_id;
	}

	public void setAffair_id(int affair_id) {
		Affair_id = affair_id;
	}

	public String getChannel_accepted() {
		return Channel_accepted;
	}

	public void setChannel_accepted(String channel_accepted) {
		Channel_accepted = channel_accepted;
	}

	public int getCustomer_id() {
		return Customer_id;
	}

	public void setCustomer_id(int customer_id) {
		Customer_id = customer_id;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getOrder_id() {
		return Order_id;
	}

	public void setOrder_id(int order_id) {
		Order_id = order_id;
	}

	public String getPhoto_combined() {
		return Photo_combined;
	}

	public void setPhoto_combined(String photo_combined) {
		Photo_combined = photo_combined;
	}

	public String getPhoto_common() {
		return Photo_common;
	}

	public void setPhoto_common(String photo_common) {
		Photo_common = photo_common;
	}

	public String getPhoto_final() {
		return Photo_final;
	}

	public void setPhoto_final(String photo_final) {
		Photo_final = photo_final;
	}

	public String getPhoto_original() {
		return Photo_original;
	}

	public void setPhoto_original(String photo_original) {
		Photo_original = photo_original;
	}

	public int getPhoto_spec_id() {
		return Photo_spec_id;
	}

	public void setPhoto_spec_id(int photo_spec_id) {
		Photo_spec_id = photo_spec_id;
	}

	public int getPhotograph_channel() {
		return Photograph_channel;
	}

	public void setPhotograph_channel(int photograph_channel) {
		Photograph_channel = photograph_channel;
	}

	public String getPhotograph_code() {
		return Photograph_code;
	}

	public void setPhotograph_code(String photograph_code) {
		Photograph_code = photograph_code;
	}

	public String getRequested_services() {
		return Requested_services;
	}

	public void setRequested_services(String requested_services) {
		Requested_services = requested_services;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public double getTaken_date() {
		return Taken_date;
	}

	public void setTaken_date(double taken_date) {
		Taken_date = taken_date;
	}

	public String getTaken_studio_code() {
		return Taken_studio_code;
	}

	public void setTaken_studio_code(String taken_studio_code) {
		Taken_studio_code = taken_studio_code;
	}

	public int getValidate_score() {
		return Validate_score;
	}

	public void setValidate_score(int validate_score) {
		Validate_score = validate_score;
	}
}
