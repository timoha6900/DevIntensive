package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModelRes {
	@SerializedName("success")
	@Expose
	private boolean success;
	@SerializedName("data")
	@Expose
	private Data data;
	@SerializedName("profileValues")
	@Expose
	private ProfileValues profileValues;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public ProfileValues getProfileValues() {
		return profileValues;
	}

	public void setProfileValues(ProfileValues profileValues) {
		this.profileValues = profileValues;
	}


	public class Data {

		@SerializedName("first_name")
		@Expose
		private String firstName;
		@SerializedName("second_name")
		@Expose
		private String secondName;
		@SerializedName("phone")
		@Expose
		private String phone;
		@SerializedName("email")
		@Expose
		private String email;
		@SerializedName("vk")
		@Expose
		private String vk;
		@SerializedName("git")
		@Expose
		private String git;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getSecondName() {
			return secondName;
		}

		public void setSecondName(String secondName) {
			this.secondName = secondName;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getVk() {
			return vk;
		}

		public void setVk(String vk) {
			this.vk = vk;
		}

		public String getGit() {
			return git;
		}

		public void setGit(String git) {
			this.git = git;
		}

	}

	public class ProfileValues {

		@SerializedName("rait")
		@Expose
		private int rait;
		@SerializedName("linesCode")
		@Expose
		private int linesCode;
		@SerializedName("projects")
		@Expose
		private int projects;

		public int getRait() {
			return rait;
		}

		public void setRait(int rait) {
			this.rait = rait;
		}

		public int getLinesCode() {
			return linesCode;
		}

		public void setLinesCode(int linesCode) {
			this.linesCode = linesCode;
		}

		public int getProjects() {
			return projects;
		}

		public void setProjects(int projects) {
			this.projects = projects;
		}

	}

}
