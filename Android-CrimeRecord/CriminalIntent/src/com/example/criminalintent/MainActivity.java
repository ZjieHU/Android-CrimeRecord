package com.example.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		
		Fragment fragment = fm.findFragmentById(R.id.continer);
		CrimeListFragment crimeListFragment = new CrimeListFragment();
		fm.beginTransaction().add(R.id.continer, crimeListFragment).commit();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 * 增加一个普通的fragment
	 */
	public void addFragment() {
		FragmentManager fm = getSupportFragmentManager();
		
		Fragment fragment = fm.findFragmentById(R.id.continer);
		if(fragment == null ) {
			Toast.makeText(this, "fragment为空", 1).show();
			fragment = new CrimeFragment();
			fm.beginTransaction().add(R.id.continer, fragment).commit();
		}  
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
