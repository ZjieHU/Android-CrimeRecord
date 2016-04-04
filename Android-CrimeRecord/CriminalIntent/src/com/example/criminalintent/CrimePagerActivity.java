package com.example.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CrimePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList list;
	
	@Override
	public void onCreate(Bundle savedInstanceStated) {
		super.onCreate(savedInstanceStated);
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		setContentView(R.layout.fragement_crime);
		
		list = CrimeLab.get(this).getArrayList();
		Toast.makeText(getApplication(), "111", 1).show();
		FragmentManager fm = getSupportFragmentManager();
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1; //官方提示这样写
			}
			
			@Override
			 public Object instantiateItem(ViewGroup container, int position) {
				UUID id = ((Crime)list.get(position)).getmID();
				LayoutInflater inflater = (LayoutInflater) container.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				Toast.makeText(getApplication(), position+"", 1).show();
				return inflater.inflate(R.layout.fragement_crime, null) ;  
			}
			
			
		};
			
	}

}
