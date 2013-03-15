package com.hannah.arduinomotiondetector.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hannah.arduinomotiondetector.PhotoHandler;
import com.hannah.arduinomotiondetector.R;

public class CameraActivity extends Activity {
	private final static String DEBUG_TAG = "MakePhotoActivity";
	private Camera camera;
	private int cameraId = 0;
	SurfaceTexture surfaceTexture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);

		surfaceTexture = new SurfaceTexture(0);

		try {
			// do we have a camera?
			if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
				Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG).show();
			} else {
				cameraId = findFrontFacingCamera();
				if (cameraId < 0) {
					Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
				} else {
					camera = Camera.open(cameraId);
					camera.setPreviewTexture(surfaceTexture);
					camera.startPreview();
				}
			}
		} catch (Exception e) {
			// Camera already in use
			e.printStackTrace();
		}
	}

	public void onClick(View view) {
		try {
			camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
		} catch (Exception e) {
			Log.d(DEBUG_TAG, "couldnt take photo");
			e.printStackTrace();
		}
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				Log.d(DEBUG_TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	@Override
	protected void onPause() {
		if (camera != null) {
			camera.release();
			camera = null;
		}
		super.onPause();
	}
}