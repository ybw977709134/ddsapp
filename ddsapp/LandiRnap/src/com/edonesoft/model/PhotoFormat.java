package com.edonesoft.model;

import java.io.Serializable;

public class PhotoFormat implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Name;
	private String Remark;
	private String Icon_url;
	private String Channel_accepted;
	private int ItemID;
	private int Px_height;
	private int Px_width;
	private int Parent_id;
	private int Min_dpi;
	private int Status;
	private int Spec_type;
	private int Chin_bottom;
	private int Head_top;

	public int getChin_bottom() {
		return Chin_bottom;
	}

	public void setChin_bottom(int chin_bottom) {
		Chin_bottom = chin_bottom;
	}

	public int getHead_top() {
		return Head_top;
	}

	public void setHead_top(int head_top) {
		Head_top = head_top;
	}

	public String getChannel_accepted() {
		return Channel_accepted;
	}

	public void setChannel_accepted(String channel_accepted) {
		Channel_accepted = channel_accepted;
	}

	public int getSpec_type() {
		return Spec_type;
	}

	public void setSpec_type(int spec_type) {
		Spec_type = spec_type;
	}

	public int getMin_dpi() {
		return Min_dpi;
	}

	public void setMin_dpi(int min_dpi) {
		Min_dpi = min_dpi;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public int getParent_id() {
		return Parent_id;
	}

	public void setParent_id(int parent_id) {
		Parent_id = parent_id;
	}

	public String getIcon_url() {
		return Icon_url;
	}

	public void setIcon_url(String icon_url) {
		Icon_url = icon_url;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public int getPx_height() {
		return Px_height;
	}

	public void setPx_height(int px_height) {
		Px_height = px_height;
	}

	public int getPx_width() {
		return Px_width;
	}

	public void setPx_width(int px_width) {
		Px_width = px_width;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	// @Override
	// public int describeContents() {
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeString(Name);
	// dest.writeString(Remark);
	// dest.writeString(Icon_url);
	// dest.writeString(Channel_accepted);
	// dest.writeInt(ItemID);
	// dest.writeInt(Px_height);
	// dest.writeInt(Px_width);
	// dest.writeInt(Parent_id);
	// dest.writeInt(Min_dpi);
	// dest.writeInt(Status);
	// dest.writeInt(Spec_type);
	// dest.writeInt(Chin_bottom);
	// dest.writeInt(Head_top);
	// }
	//
	// public static final Parcelable.Creator<PhotoFormat> CREATOR = new
	// Creator<PhotoFormat>() {
	// @Override
	// public PhotoFormat[] newArray(int size) {
	// return new PhotoFormat[size];
	// }
	//
	// @Override
	// public PhotoFormat createFromParcel(Parcel in) {
	// return new PhotoFormat(in);
	// }
	// };

	public PhotoFormat(String name, String remark, String icon_url, String channel_accepted, int itemID, int px_height,
			int px_width, int parent_id, int min_dpi, int status, int spec_type, int chin_bottom, int head_top) {
		super();
		Name = name;
		Remark = remark;
		Icon_url = icon_url;
		Channel_accepted = channel_accepted;
		ItemID = itemID;
		Px_height = px_height;
		Px_width = px_width;
		Parent_id = parent_id;
		Min_dpi = min_dpi;
		Status = status;
		Spec_type = spec_type;
		Chin_bottom = chin_bottom;
		Head_top = head_top;
	}

	public PhotoFormat() {
		super();
	}

	// public PhotoFormat(Parcel in) {
	// Name = in.readString();
	// Remark = in.readString();
	// Icon_url = in.readString();
	// Channel_accepted = in.readString();
	// ItemID = in.readInt();
	// Px_height = in.readInt();
	// Px_width = in.readInt();
	// Parent_id = in.readInt();
	// Min_dpi = in.readInt();
	// Status = in.readInt();
	// Spec_type = in.readInt();
	// Chin_bottom = in.readInt();
	// Head_top = in.readInt();
	// }
}
