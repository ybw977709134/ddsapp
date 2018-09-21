package com.edonesoft.model;

import java.io.Serializable;

public class OrderListInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Delivery_address;
	private String Delivery_name;
	private String Delivery_telephone;
	private String DeliveryZipcode;
	private int ItemID;
	private int MemberId;
	private String OrderNo;
	private double TotalAmount;
	private int PaymentMethod;
	private int Status;
	private double CreateDate;
	private String PhotoName;
	private String AffairCode;
	private String AffairName;
	private String PhotoUrl;

	public String getPhotoUrl() {
		return PhotoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		PhotoUrl = photoUrl;
	}

	public String getPhotoName() {
		return PhotoName;
	}

	public void setPhotoName(String photoName) {
		PhotoName = photoName;
	}

	public double getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(double createDate) {
		CreateDate = createDate;
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

	public String getDeliveryZipcode() {
		return DeliveryZipcode;
	}

	public void setDeliveryZipcode(String deliveryZipcode) {
		DeliveryZipcode = deliveryZipcode;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public int getMemberId() {
		return MemberId;
	}

	public void setMemberId(int memberId) {
		MemberId = memberId;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public double getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}

	public int getPaymentMethod() {
		return PaymentMethod;
	}

	public void setPaymentMethod(int paymentMethod) {
		PaymentMethod = paymentMethod;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getAffairCode() {
		return AffairCode;
	}

	public void setAffairCode(String affairCode) {
		AffairCode = affairCode;
	}

	public String getAffairName() {
		return AffairName;
	}

	public void setAffairName(String affairName) {
		AffairName = affairName;
	}
}
