package com.hannah.arduinomotiondetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

public class NotificationPreferences {

	private static final String NAME_PREF = "name_pref";
	private static final String EMAIL_PREF = "email_pref";
	private static final String PHONE_PREF = "phone_pref";
	private static final String LAT_PREF = "lat_pref";
	private static final String LONG_PREF = "long_pref";
	private static final String EMAIL_ON_PREF = "email_on_pref";
	private static final String PHOTO_ON_PREF = "photo_on_pref";
	private static final String SMS_ON_PREF = "sms_on_pref";
	private static final String ALARM_ON_PREF = "alarm_on_pref";
	private static final String IP_PREF = "ip_pref";

	public static boolean hasPrefences(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(EMAIL_PREF) && preferences.contains(PHONE_PREF) && preferences.contains(NAME_PREF);
	}

	public static boolean hasSensorName(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(NAME_PREF);
	}

	public static boolean hasLocation(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(LAT_PREF) && preferences.contains(LONG_PREF);
	}

	public static void saveSensorName(Context context, String name) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(NAME_PREF, name);
		editor.commit();
	}

	public static void saveEmail(Context context, String email) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(EMAIL_PREF, email);
		editor.commit();
	}

	public static void savePhone(Context context, String phone) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(PHONE_PREF, phone);
		editor.commit();
	}

	public static void saveAlertsOn(Context context, boolean email, boolean photo, boolean sms, boolean alarm) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean(EMAIL_ON_PREF, email);
		editor.putBoolean(PHOTO_ON_PREF, photo);
		editor.putBoolean(SMS_ON_PREF, sms);
		editor.putBoolean(ALARM_ON_PREF, alarm);
		editor.commit();
	}

	public static void saveLocation(Context context, LatLng location) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putFloat(LAT_PREF, (float) location.latitude);
		editor.putFloat(LONG_PREF, (float) location.longitude);
		editor.commit();
	}
	
	public static void saveIP(Context context, String IP) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(IP_PREF, IP);
		editor.commit();
	}

	public static String getSensorName(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(NAME_PREF, "");
	}

	public static String getEmail(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(EMAIL_PREF, "");
	}

	public static String getPhone(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(PHONE_PREF, "");
	}

	public static boolean getEmailOn(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(EMAIL_ON_PREF, false);
	}

	public static boolean getPhotoOn(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(PHOTO_ON_PREF, false);
	}

	public static boolean getSMSOn(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(SMS_ON_PREF, false);
	}

	public static boolean getAlarmOn(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(ALARM_ON_PREF, false);
	}

	public static LatLng getLocation(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		LatLng location = new LatLng(preferences.getFloat(LAT_PREF, 0), preferences.getFloat(LONG_PREF, 0));

		return location;
	}
	
	public static String getIP(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String IP = preferences.getString(IP_PREF, null);

		return IP;
	}
}
