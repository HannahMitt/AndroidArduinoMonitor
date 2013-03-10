package com.hannah.arduinomotiondetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class NotificationPreferences {

	private static final String EMAIL_PREF = "email_pref";
	private static final String PHONE_PREF = "phone_pref";
	
	public static boolean hasPrefences(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(EMAIL_PREF) && preferences.contains(PHONE_PREF);
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
	
	public static String getEmail(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(EMAIL_PREF, "");
	}
	
	public static String getPhone(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(PHONE_PREF, "");
	}
}
