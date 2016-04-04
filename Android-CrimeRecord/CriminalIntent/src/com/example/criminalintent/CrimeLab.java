package com.example.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class CrimeLab {
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	
	private ArrayList<Crime> list ;
	private CriminalIntentJSONSerializer serializer ;
	
	private static CrimeLab sCrimeLab;
	private Context context;
	
	private CrimeLab(Context context) {
		this.context = context;
		serializer = new CriminalIntentJSONSerializer(context, FILENAME);
		/*
		 * 此处生成100个Crime
		 */
	/*	for(int i = 0; i < 50; i++) {
			Crime c = new Crime();
			c.setmTitle("Crime #" + (i+1));
			c.setmSaved(i % 2 == 0);
			list.add(c);
		} */
	}
	
	
	public void addCrime(Crime c) {
		list.add(c);
		serializer.addCrimeToSDCard(list);
	}
	
	/*
	 * 删除完之后进行的保存操作
	 */
	public void saveCrime() {
		serializer.addCrimeToSDCard(list);
	}
	
	public static CrimeLab get(Context c) {
		if(sCrimeLab == null) {
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	public ArrayList<Crime> getArrayList() {
		list = serializer.loadCrimes();
		return list;
	}
	
	public Crime getCrime(UUID id) {
		for(Crime c : list) {
			if(c.getmID().equals(id)) 
				return c;
		}
		return null;
	}
	
	public void deleteCrime(Crime c) {
		list.remove(c);
	}
}
