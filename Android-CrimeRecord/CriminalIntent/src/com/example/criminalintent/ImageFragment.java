package com.example.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
	
	public static ImageFragment newInstance(String imagePath) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("filename",imagePath);
		ImageFragment imageFragment = new ImageFragment();
		imageFragment.setArguments(bundle);
		imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return imageFragment;
	}
	
	private ImageView mImageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mImageView = new ImageView(getActivity());
		String path = (String)getArguments().getSerializable("filename");
		BitmapDrawable image = PictureUtils.getScaleDrawable(getActivity(), path);
		mImageView.setImageDrawable(image);
		
		return mImageView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		PictureUtils.cleanImageView(mImageView);
	}
	
	
	
}
