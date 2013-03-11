package com.hannah.arduinomotiondetector;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationFinder {

	/**
	 * This is a fast code to get the last known location of the phone. If there
	 * is no exact gps-information it falls back to the network-based location
	 * info. This code is using LocationManager. Code from:
	 * http://www.androidsnippets.org/snippets/21/
	 * 
	 * @param ctx
	 * @return
	 */
	public static Location getLocation(Context ctx) {
		LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		/*
		 * Loop over the array backwards, and if you get an accurate location,
		 * then break out the loop
		 */
		Location l = null;

		for (int i = providers.size() - 1; i >= 0; i--) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		
		if(l != null)
			Log.d("Location", l.getLatitude() + ", " + l.getLongitude());
		else
			Log.d("Location", "location is null");
		return l;
	}
}
