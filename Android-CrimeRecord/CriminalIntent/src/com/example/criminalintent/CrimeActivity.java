package com.example.criminalintent;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CrimeActivity extends ActionBarActivity {
	
	private Crime crime;
	private EditText editText;
	private Button DateButton;
	private CheckBox SolvedCheckBox;
	protected static Fragment f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.continer);
		if(fragment == null) {
			fragment = new CrimeFragment();
			f = fragment;
			fm.beginTransaction().add(R.id.continer, fragment).commit();
		}
		/*editText = (EditText)findViewById(R.id.crime_title);
		
		crime = new Crime();
		Intent intent = getIntent();
		crime.setmSaved(intent.getBooleanExtra("isSolved", false));
		crime.setmTitle(intent.getStringExtra("title"));
		
		editText.setText(crime.getmTitle());
		
		DateButton = (Button)findViewById(R.id.crime_date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		String date = simpleDateFormat.format(crime.getmDate());
		DateButton.setText(date);
		//DateButton.setEnabled(false);
		DateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
			
		});
		
		SolvedCheckBox = (CheckBox)findViewById(R.id.crime_solved);
		SolvedCheckBox.setChecked(crime.ismSaved());
		SolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//crime.setmSaved(isChecked);
			}
		});
		
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				crime.setmTitle(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {}
			
		}); */
	}
	
}
