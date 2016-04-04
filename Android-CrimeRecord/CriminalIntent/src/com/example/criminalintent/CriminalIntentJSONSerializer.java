package com.example.criminalintent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;












import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CriminalIntentJSONSerializer {
	
	private Context mContext;
	private String mFilename;
	
	public CriminalIntentJSONSerializer(Context c, String f) {
		this.mContext = c;
		this.mFilename = f;
	}
	
	public void saveCrimes(ArrayList<Crime> list) {
		try {
			JSONArray array = new JSONArray();
			for(Crime c : list) {
				array.put(c.toJSON());
			}
			//Write the file to disk
			Writer writer = null;
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
			if(writer != null) 
				writer.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Crime> loadCrimes() {
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader;
		SimpleDateFormat sdf = new SimpleDateFormat();
		try {
			//open read file
			Crime c = null;
			InputStream is = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = reader.readLine()) != null) {
				JSONArray jsonArray = new JSONArray(line);
				//Toast.makeText(mContext, jsonArray.get(0)+"", 5).show();
				for(int i = 0; i < jsonArray.length(); i++) {
					c = new Crime();
					JSONObject json = jsonArray.getJSONObject(i);
					c.setmTitle(json.getString("JSON_TITLE"));
					c.setmSaved(json.getBoolean("JSON_SOLVED"));
					crimes.add(c);
				}
				//c.setmTitle(json.getString("JSON_TITLE"));
				//c.setmDate(sdf.parse(json.getString("JSON_DATE")));
				//Toast.makeText(mContext, jsonArray.get(0)+"", 5).show();
				//c.setmSaved(json.getBoolean("JSON_SOLVED"));
				//crimes.add(c);
			}
			
		}catch(Exception e) {
			
		}
		return crimes;
	}
	
	public void addCrimeToSDCard(ArrayList<Crime> list) {
		
		Writer writer = null;
		JSONArray jsonArray = new JSONArray();
		try {
			for(Crime c : list) {
				JSONObject json = c.toJSON();
				jsonArray.put(json);
			}
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(jsonArray.toString());
			if(writer != null) 
				writer.close();
		}catch(Exception e) {
			Log.v("addCrimeToSDCard", e.toString());
		}
	}
}
