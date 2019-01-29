package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintesiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

	private SharedPreferences mSharedPreferences;

	private static final String[] USER_FIELDS = {
			ConstantManager.USER_PHONE_KEY,
			ConstantManager.USER_MAIL_KEY,
			ConstantManager.USER_VK_KEY,
			ConstantManager.USER_GIT_KEY,
			ConstantManager.USER_BIO_KEY
	};

	private static final String[] USER_VALUES = {
			ConstantManager.USER_RAITING_VALUE,
			ConstantManager.USER_CODE_LINES_VALUE,
			ConstantManager.USER_PROJECT_VALUES
	};

	public PreferencesManager() {
		this.mSharedPreferences = DevintesiveApplication.getSharedPreferences();
	}

	public void saveUserProfileData(List<String> userFields) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();

		for(int i = 0; i < USER_FIELDS.length; i++) {
			editor.putString(USER_FIELDS[i], userFields.get(i));
		}
		editor.apply();
	}

	public void saveUserProfileDataWithoutBio(List<String> userFields) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();

		for(int i = 0; i < USER_FIELDS.length-1; i++) {
			editor.putString(USER_FIELDS[i], userFields.get(i));
		}
		editor.apply();
	}

	public List<String> loadUserProfileData() {
		List<String> userFields = new ArrayList<>();
		for(String field : USER_FIELDS) {
			userFields.add(mSharedPreferences.getString(field, null));
		}
		return userFields;
	}

	public void saveUserProfileValues(int[] userValues){
		SharedPreferences.Editor editor = mSharedPreferences.edit();

		for(int i = 0; i < USER_VALUES.length; i++) {
			editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
		}
		editor.apply();
	}

	public List<String> loadUserProfileValues() {
		List<String> userFields = new ArrayList<>();
		for(String field : USER_VALUES) {
			userFields.add(mSharedPreferences.getString(field, "0"));
		}
		return userFields;
	}

	public void saveUserPhoto(Uri uri) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
		editor.apply();
	}

	public Uri loadUserPhoto() {
		return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
				"android.resource://com.softdesign.devintensive/drawable/user_photo"));
	}

	public void saveAuthToken(String authToken) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
		editor.apply();
	}

	public String getAuthToken() {
		return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
	}

	public void saveUserId(String userId) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(ConstantManager.USER_ID_KEY, userId);
		editor.apply();
	}

	public String getUserId() {
		return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
	}

}
