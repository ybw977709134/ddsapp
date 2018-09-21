package com.edonesoft.model;

import java.io.Serializable;

public class OrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Affair Affair;
	private String Delivery_address;
	private String Delivery_name;
	private String Delivery_telephone;
	private String Name;
	private int OrderId;
	private String OrderNo;
	private String Photo_common;
	private String Photo_original;
	private PhotoFormat Photo_spec;
	private String Photo_valid_url;
	private String Photograph_code;
	private String Requested_services;
	private double Take_date;
	private int Validate_score;
	private String Photo_info;
	private boolean IsRecentPhoto;
	private String AffairName;
	private String AffairCode;
	private String Status;

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getAffairName() {
		return AffairName;
	}

	public void setAffairName(String affairName) {
		AffairName = affairName;
	}

	public String getAffairCode() {
		return AffairCode;
	}

	public void setAffairCode(String affairCode) {
		AffairCode = affairCode;
	}

	public String getPhoto_info() {
		return Photo_info;
	}

	public void setPhoto_info(String photo_info) {
		Photo_info = photo_info;
	}

	public boolean isIsRecentPhoto() {
		return IsRecentPhoto;
	}

	public void setIsRecentPhoto(boolean isRecentPhoto) {
		IsRecentPhoto = isRecentPhoto;
	}

	public Affair getAffair() {
		return Affair;
	}

	public void setAffair(Affair affair) {
		Affair = affair;
	}

	public PhotoFormat getPhoto_spec() {
		return Photo_spec;
	}

	public void setPhoto_spec(PhotoFormat photo_spec) {
		Photo_spec = photo_spec;
	}

	public String getDelivery_address() {
		return Delivery_address;
	}

	public void setDelivery_address(String delivery_address) {
		Delivery_address = delivery_address;
	}

	public String getDelivery_name() {
		return Delivery_name;
	}

	public void setDelivery_name(String delivery_name) {
		Delivery_name = delivery_name;
	}

	public String getDelivery_telephone() {
		return Delivery_telephone;
	}

	public void setDelivery_telephone(String delivery_telephone) {
		Delivery_telephone = delivery_telephone;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getOrderId() {
		return OrderId;
	}

	public void setOrderId(int orderId) {
		OrderId = orderId;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getPhoto_common() {
		return Photo_common;
	}

	public void setPhoto_common(String photo_common) {
		Photo_common = photo_common;
	}

	public String getPhoto_original() {
		return Photo_original;
	}

	public void setPhoto_original(String photo_original) {
		Photo_original = photo_original;
	}

	public String getPhoto_valid_url() {
		return Photo_valid_url;
	}

	public void setPhoto_valid_url(String photo_valid_url) {
		Photo_valid_url = photo_valid_url;
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

	public double getTake_date() {
		return Take_date;
	}

	public void setTake_date(double take_date) {
		Take_date = take_date;
	}

	public int getValidate_score() {
		return Validate_score;
	}

	public void setValidate_score(int validate_score) {
		Validate_score = validate_score;
	}

}
