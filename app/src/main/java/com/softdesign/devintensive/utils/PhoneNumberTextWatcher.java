package com.softdesign.devintensive.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class PhoneNumberTextWatcher implements TextWatcher {
	private static final String DEFAULT_COUNTRY_CODE = "+380";
	private static final int MAX_NUMBER_LENGTH_WITHOUT_COUNTRY_CODE = 9;

	private final String countryCode;

	private boolean mSelfChange = false;
	private boolean mInvalidChange = false;

	private int mStart, mEnd;
	private String mOldText;



	public PhoneNumberTextWatcher(){
		this(DEFAULT_COUNTRY_CODE);
	}

	public PhoneNumberTextWatcher(String countryCode){
		if(isCountryCodeValid(countryCode)){
			this.countryCode = countryCode;
		} else {
			this.countryCode = DEFAULT_COUNTRY_CODE;
		}
	}

	private boolean isCountryCodeValid(String countryCode) {
		if(countryCode == null || countryCode.length() < 2) return false;

		if(countryCode.charAt(0) != '+') return false;

		for(int i = 1; i < countryCode.length(); i++) {
			if(!Character.isDigit(countryCode.charAt(i))) return false;
		}

		return true;
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if(mSelfChange) return;

		if(s.length() == 0) return;

		if(start < countryCode.length() && (start + count) <= countryCode.length()){
			mInvalidChange = true;
			mStart = start;
			mEnd = start + after;
			mOldText = s.subSequence(start, start + count).toString();
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(mSelfChange) return;

		if(mInvalidChange){
			if(s.subSequence(start, start + count).toString().equals(countryCode)) {
				mInvalidChange = false;
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if(mSelfChange) return;

		if(mInvalidChange){
			mInvalidChange = false;
			mSelfChange = true;
			s.replace(mStart, mEnd, mOldText, 0, mOldText.length());
			mSelfChange = false;
			return;
		}

		String formattedText = formatText(s);
		mSelfChange = true;
		s.replace(0, s.length(), formattedText, 0, formattedText.length());

//		if(formattedText.equals(s.toString())){
//			Selection.setSelection(s, s.length());
//		}

		mSelfChange = false;
	}

	private String formatText(CharSequence text) {
		StringBuilder formatted = new StringBuilder();
		int start = 0;

		if(text.length() >= countryCode.length()
				&& text.subSequence(0, countryCode.length()).toString().equals(countryCode)) {
			start = countryCode.length();
		}else{
			for(int i = 0; i < countryCode.length() && i < text.length(); i++){
				if(text.charAt(i) == countryCode.charAt(i)){
					start++;
				}else{
					break;
				}
			}
		}
		formatted.append(countryCode);
		formatted.append(" ");

		int count = 0;
		int countDigits = 0;
		int spaceInterval = 2;
		for(int i = start; i < text.length(); i++){
			if(Character.isDigit(text.charAt(i))){
				formatted.append(text.charAt(i));
				count++;
				if(++countDigits >= MAX_NUMBER_LENGTH_WITHOUT_COUNTRY_CODE) break;

				if(count % spaceInterval == 0 && count > 0 && i < text.length()-1){
					formatted.append(" ");
					count = 0;
					spaceInterval++;
				}
			}
		}

		if(formatted.charAt(formatted.length()-1) == ' '){
			formatted.deleteCharAt(formatted.length()-1);
		}

		return formatted.toString();
	}
}
