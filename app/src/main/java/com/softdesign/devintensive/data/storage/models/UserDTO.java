package com.softdesign.devintensive.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.softdesign.devintensive.data.network.res.UserListRes;

public class UserDTO implements Parcelable {

	private String mFullName;
	private String mRating;
	private String mCodeLines;
	private String mProjects;
	private String mBio;

	public UserDTO(UserListRes.UserData userData) {
		mFullName = userData.getFullName();
		mRating = String.valueOf(userData.getRait());
		mCodeLines = String.valueOf(userData.getLinesCode());
		mProjects = String.valueOf(userData.getProjects());
		mBio = userData.getBio();
	}

	protected UserDTO(Parcel in) {
		mFullName = in.readString();
		mRating = in.readString();
		mCodeLines = in.readString();
		mProjects = in.readString();
		mBio = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mFullName);
		dest.writeString(mRating);
		dest.writeString(mCodeLines);
		dest.writeString(mProjects);
		dest.writeString(mBio);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<UserDTO> CREATOR = new Parcelable.Creator<UserDTO>() {
		@Override
		public UserDTO createFromParcel(Parcel in) {
			return new UserDTO(in);
		}

		@Override
		public UserDTO[] newArray(int size) {
			return new UserDTO[size];
		}
	};

	public String getFullName() {
		return mFullName;
	}

	public String getRating() {
		return mRating;
	}

	public String getCodeLines() {
		return mCodeLines;
	}

	public String getProjects() {
		return mProjects;
	}

	public String getBio() {
		return mBio;
	}
}
