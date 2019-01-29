package com.softdesign.devintensive.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class SiteLinkTextWatcher implements TextWatcher {

	private static final String DEFAULT_SITE_LINK = "vk.com/";

	private final String mSiteLink;

	private boolean mSelfChange = false;
	private boolean mInvalidChange = false;

	private int mStart, mEnd;
	private String mOldText;



	public SiteLinkTextWatcher(){
		this(DEFAULT_SITE_LINK);
	}

	public SiteLinkTextWatcher(String siteLink){
		this.mSiteLink = validateSiteLink(siteLink);
	}

	private String validateSiteLink(String siteLink){
		StringBuilder validLink = new StringBuilder();
		for(int i = 0; i < siteLink.length(); i++){
			if(siteLink.charAt(i) != ' '){
				validLink.append(siteLink.charAt(i));
			}
		}
		return validLink.toString();
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if(mSelfChange) return;

		if(s.length() == 0) return;

		if(start < mSiteLink.length() && (start + count) <= mSiteLink.length()){
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
			if(s.subSequence(start, start + count).toString().equals(mSiteLink)) {
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

		if(text.length() >= mSiteLink.length()
				&& text.subSequence(0, mSiteLink.length()).toString().equals(mSiteLink)) {
			start = mSiteLink.length();
		}else{
			for(int i = 0; i < mSiteLink.length() && i < text.length(); i++){
				if(text.charAt(i) == mSiteLink.charAt(i)){
					start++;
				}else{
					break;
				}
			}
		}
		formatted.append(mSiteLink);

		for(int i = start; i < text.length(); i++){
			if(text.charAt(i) != ' ') {
				formatted.append(text.charAt(i));
			}
		}

		return formatted.toString();
	}

}
