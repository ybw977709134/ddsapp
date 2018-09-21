package com.edonesoft.model;

import java.io.Serializable;

public class Advertisement implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ImageURL;
	private int ItemID;
	private int Position;
	private int Status;
	private String TargetUrl;
	private String Title;
	private double recommend_rate;
	private String richtext_content;

	public String getImageURL() {
		return ImageURL;
	}

	public void setImageURL(String imageURL) {
		ImageURL = imageURL;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public int getPosition() {
		return Position;
	}

	public void setPosition(int position) {
		Position = position;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getTargetUrl() {
		return TargetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		TargetUrl = targetUrl;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public double getRecommend_rate() {
		return recommend_rate;
	}

	public void setRecommend_rate(double recommend_rate) {
		this.recommend_rate = recommend_rate;
	}

	public String getRichtext_content() {
		return richtext_content;
	}

	public void setRichtext_content(String richtext_content) {
		this.richtext_content = richtext_content;
	}
}
