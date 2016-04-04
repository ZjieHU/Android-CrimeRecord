package com.example.criminalintent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime implements Serializable {
	
	private UUID mID;
	private String mTitle;
	private Date mDate;
	private boolean mSaved;
	private Photo photo;
	
	public Crime() {
		mID = UUID.randomUUID();
		mDate = new Date();
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("JSON_ID", mID);
		json.put("JSON_TITLE", mTitle);
		json.put("JSON_SOLVED", mSaved);
		json.put("JSON_DATE", mDate);
		if(photo != null) {
			json.put("JSON_PHOTO", photo.getmFilename());
		}
		return json;
	}
	
	public Date getmDate() {
		return mDate;
	}

	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}

	public boolean ismSaved() {
		return mSaved;
	}

	public void setmSaved(boolean mSaved) {
		this.mSaved = mSaved;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public UUID getmID() {
		return mID;
	}
	
	@Override
	public String toString() {
		return this.mTitle;
	}
}
