package com.edonesoft.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderCommitModel implements Parcelable {
	private int Affair_id;
	private int Photo_spec_id;
	private int Validate_score;
	private int px_width;
	private int px_height;
	private String Name;
	private String Photo_common;
	private String Photo_final;
	private String Photograph_code;
	private String Requested_services;
	private int file_size;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Affair_id);
		dest.writeInt(Photo_spec_id);
		dest.writeInt(Validate_score);
		dest.writeInt(px_width);
		dest.writeInt(px_height);
		dest.writeString(Name);
		dest.writeString(Photo_common);
		dest.writeString(Photo_final);
		dest.writeString(Photograph_code);
		dest.writeString(Requested_services);
		dest.writeInt(file_size);
	}

	public static final Parcelable.Creator<OrderCommitModel> CREATOR = new Creator<OrderCommitModel>() {
		@Override
		public OrderCommitModel[] newArray(int size) {
			return new OrderCommitModel[size];
		}

		@Override
		public OrderCommitModel createFromParcel(Parcel in) {
			return new OrderCommitModel(in);
		}
	};

	public OrderCommitModel(Parcel in) {
		Affair_id = in.readInt();
		Photo_spec_id = in.readInt();
		Validate_score = in.readInt();
		px_width = in.readInt();
		px_height = in.readInt();
		Name = in.readString();
		Photo_common = in.readString();
		Photo_final = in.readString();
		Photograph_code = in.readString();
		Requested_services = in.readString();
		file_size = in.readInt();
	}

	public int getAffair_id() {
		return Affair_id;
	}

	public void setAffair_id(int affair_id) {
		Affair_id = affair_id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

	public int getPhoto_spec_id() {
		return Photo_spec_id;
	}

	public void setPhoto_spec_id(int photo_spec_id) {
		Photo_spec_id = photo_spec_id;
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

	public int getValidate_score() {
		return Validate_score;
	}

	public void setValidate_score(int validate_score) {
		Validate_score = validate_score;
	}

	public int getPx_width() {
		return px_width;
	}

	public void setPx_width(int px_width) {
		this.px_width = px_width;
	}

	public int getPx_height() {
		return px_height;
	}

	public void setPx_height(int px_height) {
		this.px_height = px_height;
	}

	public int getFile_size() {
		return file_size;
	}

	public void setFile_size(int file_size) {
		this.file_size = file_size;
	}

	public OrderCommitModel(int affair_id, int photo_spec_id, int validate_score, int px_width, int px_height,
			String name, String photo_common, String photo_final, String photograph_code, String requested_services,
			int file_size) {
		super();
		Affair_id = affair_id;
		Photo_spec_id = photo_spec_id;
		Validate_score = validate_score;
		this.px_width = px_width;
		this.px_height = px_height;
		Name = name;
		Photo_common = photo_common;
		Photo_final = photo_final;
		Photograph_code = photograph_code;
		Requested_services = requested_services;
		this.file_size = file_size;
	}

	public OrderCommitModel() {
		super();
	}
}
