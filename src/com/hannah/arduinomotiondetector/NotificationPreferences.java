package com.hannah.arduinomotiondetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.preference.PreferenceManager;

public class NotificationPreferences {

	private static final String NAME_PREF = "name_pref";
	private static final String EMAIL_PREF = "email_pref";
	private static final String PHONE_PREF = "phone_pref";
	private static final String LAT_PREF = "lat_pref";
	private static final String LONG_PREF = "long_pref";
	
	public static boolean hasPrefences(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(EMAIL_PREF) && preferences.contains(PHONE_PREF) && preferences.contains(NAME_PREF);
	}
	
	public static boolean hasSensorName(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(NAME_PREF);
	}
	
	public static void saveSensorName(Context context, String name){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(NAME_PREF, name);
		editor.commit();
	}
	
	public static void saveEmail(Context context, String email){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(EMAIL_PREF, email);
		editor.commit();
	}
	
	public static void savePhone(Context context, String phone){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(PHONE_PREF, phone);
		editor.commit();
	}
	
	public static void saveLocation(Context context, Location location){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putFloat(LAT_PREF, (float) location.getLatitude());
		editor.putFloat(LONG_PREF, (float) location.getLongitude());
		editor.commit();
	}
	
	public static String getSensorName(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(NAME_PREF, "");
	}
	
	public static String getEmail(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(EMAIL_PREF, "");
	}
	
	public static String getPhone(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(PHONE_PREF, "");
	}
	
	public static Location getLocation(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Location location = new Location(NotificationPreferences.class.getSimpleName());
		location.setLatitude(preferences.getFloat(LAT_PREF, 0));
		location.setLongitude(preferences.getFloat(LONG_PREF, 0));
		
		return location;
	}
}
