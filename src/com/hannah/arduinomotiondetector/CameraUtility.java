package com.hannah.arduinomotiondetector;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;

public class CameraUtility {

	private static final String TAG = "CameraUtility";

	public static int findCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				Log.d(TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	public static Camera getCamera(Activity activity) {
		try {
			// do we have a camera?
			if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
				Log.d(TAG, "No camera on this device");
			} else {
				int cameraId = CameraUtility.findCamera();
				Camera camera = Camera.open(cameraId);
				return camera;
			}
		} catch (Exception e) {
			// Camera already in use
			e.printStackTrace();
		}

		return null;
	}

	public static void takePicture(Camera camera, Context context) {
		try {
			camera.takePicture(null, null, new PhotoHandler(context));
		} catch (Exception e) {
			Log.d(TAG, "couldnt take photo");
			e.printStackTrace();
		}
	}
}
