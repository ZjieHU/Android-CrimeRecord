package com.example.criminalintent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class CrimeCameraFragment extends Fragment {
	
	private Camera camera;
	private SurfaceView surfaceView;
	private View mProgressContainer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, parent, parent == null);
		
		mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
		
		Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(camera != null) {
					camera.takePicture(mShutterCallback, null, mJpegCallback); //调用底下实现的三个接口
				}
			}
		});	
		
		surfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(camera != null)
					camera.stopPreview();
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if(camera != null) {
						camera.setPreviewDisplay(holder);
					}
				}catch(Exception e) {
					Log.v("cameraCreate",e.toString());
				}
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) { //surface 的尺寸，长宽
				if(camera == null)
					return;
				Camera.Parameters parameters = camera.getParameters();
				Size s = getBestSupportSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				s = getBestSupportSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);
				try {
					camera.startPreview();
				}catch(Exception e) {
					camera.release();
					camera = null;
				}
			}
		});
		
		return v;
	}
	
	//找出适合相机的最佳尺寸
	private Size getBestSupportSize(List<Size> sizes, int width, int height) {
		Size bestSize = sizes.get(0);
		int largeArea = bestSize.width * bestSize.height;
		for(Size s : sizes) {
			int area = s.width * s.height;
			if(area > largeArea) {
				bestSize = s;
				largeArea = area;
			}
		}
		return bestSize;
	}
	
	private Camera.ShutterCallback mShutterCallback = new ShutterCallback() {
		
		@Override
		public void onShutter() {
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	
	private Camera.PictureCallback mJpegCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String filename = UUID.randomUUID().toString() + ".JPG";
			FileOutputStream os = null;
			boolean success = true;
			try {
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			}catch(Exception e) {
				Log.v("CameraPicture", e.toString());
				success = false;
			}finally{
				if(os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.v("os resume", e.toString());
						success = false;
					}
				}
			}
			if(success) {
				//Toast.makeText(getActivity(), "OK", 1).show();
				Intent i = new Intent();
				i.putExtra("picturereserve", filename);
				getActivity().setResult(1, i);
			}
			getActivity().finish();
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		try {
			camera = Camera.open(0);
		}catch(Exception e) {
			camera = Camera.open();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(camera != null) {
			camera.release();
			camera = null;
		}
	}
}
