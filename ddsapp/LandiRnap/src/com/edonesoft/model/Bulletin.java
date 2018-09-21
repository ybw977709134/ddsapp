package com.edonesoft.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Bulletin implements Parcelable {
	private int Bulletin_Group;
	private String Bulletin_code;
	private String Gallery_content;
	private String Icon_url;
	private String Intro;
	private int ItemID;
	private int Status;
	private String bulletin_title;
	private double create_date;
	private double recommend_rate;
	private String richtext_content;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Bulletin_Group);
		dest.writeString(Bulletin_code);
		dest.writeString(Gallery_content);
		dest.writeString(Icon_url);
		dest.writeString(Intro);
		dest.writeInt(ItemID);
		dest.writeInt(Status);
		dest.writeString(bulletin_title);
		dest.writeDouble(create_date);
		dest.writeDouble(recommend_rate);
		dest.writeString(richtext_content);
	}

	public static final Parcelable.Creator<Bulletin> CREATOR = new Creator<Bulletin>() {
		@Override
		public Bulletin[] newArray(int size) {
			return new Bulletin[size];
		}

		@Override
		public Bulletin createFromParcel(Parcel in) {
			return new Bulletin(in);
		}
	};

	public Bulletin(Parcel in) {
		Bulletin_Group = in.readInt();
		Bulletin_code = in.readString();
		Gallery_content = in.readString();
		Icon_url = in.readString();
		Intro = in.readString();
		ItemID = in.readInt();
		Status = in.readInt();
		bulletin_title = in.readString();
		create_date = in.readDouble();
		recommend_rate = in.readDouble();
		richtext_content = in.readString();
	}

	public int getBulletin_Group() {
		return Bulletin_Group;
	}

	public void setBulletin_Group(int bulletin_Group) {
		Bulletin_Group = bulletin_Group;
	}

	public String getBulletin_code() {
		return Bulletin_code;
	}

	public void setBulletin_code(String bulletin_code) {
		Bulletin_code = bulletin_code;
	}

	public String getGallery_content() {
		return Gallery_content;
	}

	public void setGallery_content(String gallery_content) {
		Gallery_content = gallery_content;
	}

	public String getIcon_url() {
		return Icon_url;
	}

	public void setIcon_url(String icon_url) {
		Icon_url = icon_url;
	}

	public String getIntro() {
		return Intro;
	}

	public void setIntro(String intro) {
		Intro = intro;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getBulletin_title() {
		return bulletin_title;
	}

	public void setBulletin_title(String bulletin_title) {
		this.bulletin_title = bulletin_title;
	}

	public double getCreate_date() {
		return create_date;
	}

	public void setCreate_date(double create_date) {
		this.create_date = create_date;
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

	public Bulletin(int bulletin_Group, String bulletin_code, String gallery_content, String icon_url, String intro,
			int itemID, int status, String bulletin_title, double create_date, double recommend_rate,
			String richtext_content) {
		super();
		Bulletin_Group = bulletin_Group;
		Bulletin_code = bulletin_code;
		Gallery_content = gallery_content;
		Icon_url = icon_url;
		Intro = intro;
		ItemID = itemID;
		Status = status;
		this.bulletin_title = bulletin_title;
		this.create_date = create_date;
		this.recommend_rate = recommend_rate;
		this.richtext_content = richtext_content;
	}

	public Bulletin() {
		super();
	}
}
