package com.example.criminalintent;

import android.content.Context;

public class Photo {
	
	private String mFilename;
	private Context context;
	private static Photo sPhoto;
	
	public Photo (Context c) {
		this.context = c;
	}
	public String getmFilename() {
		return mFilename;
	}

	public void setmFilename(String mFilename) {
		this.mFilename = mFilename;
	}
	
	public static Photo getContext(Context c) {
		if(sPhoto == null) {
			sPhoto = new Photo(c.getApplicationContext());
		}
		return sPhoto;
	}
}
