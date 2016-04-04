package com.example.criminalintent;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment {
	
	private View view;
	private Date dateFragment;
	protected TextView text;
	protected Crime crime;
	
	/*
	 * (non-Javadoc)
	 * 年月日
	 */
	protected int y,m,d;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		view = getActivity().getLayoutInflater().inflate(R.layout.dia_log, null);
		text = (TextView) getActivity().findViewById(R.id.crime_date);
		builder.setTitle("Date of crime");
		
		Bundle bundle = getArguments();
		crime = (Crime) bundle.get("crime");
		
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				setTargetFragment(CrimeActivity.f, 1);
				Fragment targetFragment = getTargetFragment();
				if(targetFragment == null) {
					Toast.makeText(getActivity(), "targetFragment is null!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent i = new Intent();
				Bundle b = new Bundle();
				b.putSerializable("txtdate", crime);
				i.putExtras(b);
				targetFragment.onActivityResult(1, 1, i);
			}
		});
		
		DatePicker date =  (DatePicker) view.findViewById(R.id.datepicker);
		Calendar calendar = Calendar.getInstance();
	//	Toast.makeText(getActivity(), crime.getmDate().getYear()+"", Toast.LENGTH_SHORT).show();
		int year = crime.getmDate().getYear() + 1900;
		int month = crime.getmDate().getMonth();
		int day = crime.getmDate().getDay();
		date.init(year, month, day - 1, new MyDateChangedListener());
		
		builder.setView(view);
		return builder.create();
	}
	
	/*
	 * 处理每次点击日期的事件
	 */
	class MyDateChangedListener implements OnDateChangedListener {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			y = year;
			m = monthOfYear + 1;
			d = dayOfMonth;
			String[] date = text.getText().toString().split("");
			StringBuffer sb = new StringBuffer();
			sb.append(year+"/");
			sb.append(m+"/");
			sb.append(dayOfMonth+" ");
			for(int i = date.length - 8; i < date.length; i++) {
				sb.append(date[i]);
			}
			text.setText(sb.toString());
			sb.delete(0, sb.length());
			dateFragment = crime.getmDate();
			dateFragment.setDate(d);
			dateFragment.setYear(y - 1900);
			dateFragment.setMonth(m - 1);
			crime.setmDate(dateFragment);
		}
		
	}
	
}
