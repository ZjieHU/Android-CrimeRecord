package com.example.criminalintent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CrimeListFragment extends ListFragment{
	private static final String TAG = "CrimeListFragment";
	
	private ArrayList<Crime> list;
	private ArrayAdapter adapter;
	private boolean isMultiple = false;
	private ArrayList<Integer> multipleDelete = new ArrayList<Integer>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle("Crimes");
		list = CrimeLab.get(getActivity()).getArrayList();
		//adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list);
		CrimeAdapter crimeAdapter = new CrimeAdapter(list);
		setListAdapter(crimeAdapter);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ListView listView = (ListView)getActivity().findViewById(android.R.id.list); //取得上下文的ListView对象
		registerForContextMenu(listView); //注册上下文的长按选项Menu
		
/*		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				return false;
			}
		}); */
	}



	@Override
	public boolean onContextItemSelected(MenuItem item) { //上下文操作
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		//Toast.makeText(getActivity(), adapterContextMenuInfo.position+"", 1).show();
		if(item.getTitle().equals("delete_item")) {
			Crime c = CrimeLab.get(getActivity()).getArrayList().get(adapterContextMenuInfo.position);
			CrimeLab.get(getActivity()).deleteCrime(c);
			CrimeLab.get(getActivity()).saveCrime();
			onCreate(null);
			isMultiple = false;
		}else if(item.getTitle().equals("multiple_delete")){
			//adapterContextMenuInfo.targetView.setBackgroundColor(Color.GRAY);
			if(!isMultiple) {
				isMultiple = true;
			}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.crime_fragment_empty_list, null);
		return v;
	}



	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(isMultiple) {
			for(Integer i : multipleDelete) {
				if(i == position) {
					v.setBackgroundColor(Color.WHITE);
					multipleDelete.remove((Object)position);
					return;
				}
			}
			multipleDelete.add(position);
			v.setBackgroundColor(Color.GRAY);
			return;
		}
		Crime c = (Crime)getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity(), CrimeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("crime", c);
		intent.putExtras(bundle);
		startActivity(intent);
		/*FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.ListFragment, new CrimeFragment());
		ft.addToBackStack(null);
		ft.commit(); */
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { //操作栏
		if(item.getTitle().equals("add_crime")) {
			isMultiple = false;
			Crime crime = new Crime();
			Intent intent = new Intent(getActivity(), CrimeActivity.class);
			intent.putExtra("crime", crime);
			intent.putExtra("resultCode", 1); //CrimeActivity将会返回的结果代码
			startActivityForResult(intent, 1);
		}else if(item.getTitle().equals("delete_crimes") && isMultiple) {
			ArrayList<Crime> list2 = new ArrayList<Crime>();
			for(Crime c : list)
				list2.add(c);
			for(Integer i : multipleDelete) {
				//Toast.makeText(getActivity(), i.getmDate()+"", 1).show();
				CrimeLab.get(getActivity()).deleteCrime(list2.get(i));
			}
			CrimeLab.get(getActivity()).saveCrime();
			onCreate(null);
		}else if(item.getTitle().equals("Cancle") && isMultiple) {
			onCreate(null);
		}
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == 1) { //证明是从操作栏增加Crime对象,从CrimeFragment返回,resultCode,resultCode
			Crime crime = (Crime) data.getSerializableExtra("c");//都是1
			CrimeLab.get(getActivity()).addCrime(crime);
			onCreate(null);
		}
	}

	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_list_add, menu);
	}



	/*
	 * 添加定制的Adapter内置类
	 */
	private class CrimeAdapter extends ArrayAdapter {

		public CrimeAdapter(ArrayList list) {
			super(getActivity(),0,list);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.listview, null);
			}
			convertView.setBackgroundColor(Color.WHITE);
			Crime c = (Crime)getItem(position);
			TextView title = (TextView)convertView.findViewById(R.id.crime_list_item_title);
			title.setText(c.getmTitle());
			TextView date = (TextView)convertView.findViewById(R.id.crime_list_item_date);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			date.setText(simpleDateFormat.format(c.getmDate()));
			CheckBox isSolved = (CheckBox)convertView.findViewById(R.id.crime_list_item_checkBox);
			isSolved.setChecked(c.ismSaved());
			
			return convertView;
		}
	}
}
