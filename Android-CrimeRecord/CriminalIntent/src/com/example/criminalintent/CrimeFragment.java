package com.example.criminalintent;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {

	private EditText editText;
	private Button DateButton;
	private CheckBox SolvedCheckBox;
	private View v;
	protected Crime c = null;
	protected Button mComplateButton;
	private ImageButton photoTaken;
	private ImageView imageView;
	private Button optionButton;
	private Button sendMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(imageView);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragement_crime, parent, false);
		//增加向上导航按钮 
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}//增加完成
		editText = (EditText)v.findViewById(R.id.crime_title);
		final Intent intent = getActivity().getIntent();
		Bundle bundle = intent.getExtras();
		c = (Crime) bundle.get("crime");
		editText.setText(c.getmTitle());
		
		DateButton = (Button)v.findViewById(R.id.crime_date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		String date = simpleDateFormat.format(c.getmDate());
		DateButton.setText(date);
		DateButton.setEnabled(true);
		DateButton.setOnClickListener(new OnClickListener() {
			/*
			 * 进入日期选择页面
			 */
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = new DatePickerFragment();
				Bundle b = new Bundle();
				b.putSerializable("crime", c);
				dialog.setArguments(b);
				dialog.show(fm, "date");
			}
		});
		
		SolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		SolvedCheckBox.setChecked(c.ismSaved());
		SolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				c.setmSaved(isChecked);
			}
		});
		
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				c.setmTitle(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {}
			
		});
		
		/*
		 * 增加犯罪记录
		 */
		mComplateButton = (Button) v.findViewById(R.id.complate);
		final int resultCode = intent.getIntExtra("resultCode", 0);
		if(resultCode == 0) {
			mComplateButton.setEnabled(false);
		}
		if(intent.getSerializableExtra("crime") == null) {
			mComplateButton.setEnabled(false);
		}
		mComplateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Crime c = (Crime) intent.getSerializableExtra("crime");
				c.setmTitle(editText.getText().toString());
				c.setmSaved(SolvedCheckBox.isChecked());
				intent.putExtra("c", c);
				getActivity().setResult(resultCode, intent);
				getActivity().finish();
			}
		});
		
		//跳转至拍照
		photoTaken = (ImageButton)v.findViewById(R.id.camera_button);
		photoTaken.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent camera = new Intent(getActivity(),CrimeCameraActivity.class);
				startActivityForResult(camera, 1);
			}
		});
		
		//图片显示区域
		imageView = (ImageView) v.findViewById(R.id.crimeImageview_photo);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Photo p = c.getPhoto();
				if(p == null )
					return;
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
				Toast.makeText(getActivity(), path, 1).show();
				ImageFragment.newInstance(path).show(fm, "image");
			}
		});
		
		optionButton = (Button) v.findViewById(R.id.option_suspect);
		optionButton.setBackgroundColor(Color.WHITE);
		optionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_TEXT, editText.getText());
				i.putExtra(Intent.EXTRA_SUBJECT, "Suspect");
				i = Intent.createChooser(i, "Suspect");
				startActivity(i);
			}
		});
		
		sendMessage = (Button) v.findViewById(R.id.send_message);
		sendMessage.setBackgroundColor(Color.WHITE);
		sendMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i, 2);
			}
		});
		
		
		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == 1) { //收到视频界面传回的信息
			String filename = data.getStringExtra("picturereserve");
			if(filename != null) {
				Photo.getContext(getActivity()).setmFilename(filename);
				c.setPhoto(Photo.getContext(getActivity()));
				showPhoto();
			}
		} else if(requestCode == 2) { //收到联系人界面传回的信息
			Uri contactURI = data.getData();
			String contactID = contactURI.getLastPathSegment();
			String name = "";
			
			String[] projection = new String[] {
					People._ID,People.NAME,People.NUMBER
			};
			
			Cursor cursor = getActivity().getContentResolver().
					query(People.CONTENT_URI, projection, People._ID, new String[] {contactID}, People.NAME);
			name = cursor.getString(cursor.getColumnIndex(People.NAME));
			Toast.makeText(getActivity(), name, 1).show();
		}
	}
	
	private void showPhoto() {
		Photo p = c.getPhoto();
		BitmapDrawable b = null;
		
		if(p != null) {
			String path = getActivity().getFileStreamPath(p.getmFilename()).getAbsolutePath();
			b = PictureUtils.getScaleDrawable(getActivity(), path);
		}
		imageView.setImageDrawable(b);
	}
}
