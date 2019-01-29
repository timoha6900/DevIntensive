package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserListRes {

	@SerializedName("success")
	@Expose
	private boolean success;
	@SerializedName("user_data")
	@Expose
	private List<UserData> userData = null;

	public List<UserData> getUserData() {
		return userData;
	}


	public class UserData {

		@SerializedName("first_name")
		@Expose
		private String firstName;
		@SerializedName("second_name")
		@Expose
		private String secondName;
		@SerializedName("rait")
		@Expose
		private int rait;
		@SerializedName("linesCode")
		@Expose
		private int linesCode;
		@SerializedName("projects")
		@Expose
		private int projects;
		@SerializedName("bio")
		@Expose
		private String bio;


		public String getFullName() {
			return firstName + " " + secondName;
		}

		public int getRait() {
			return rait;
		}

		public int getLinesCode() {
			return linesCode;
		}

		public int getProjects() {
			return projects;
		}

		public String getBio() {
			return bio;
		}


	}

}
