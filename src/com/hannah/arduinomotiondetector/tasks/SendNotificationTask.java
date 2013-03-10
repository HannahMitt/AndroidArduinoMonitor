package com.hannah.arduinomotiondetector.tasks;

import java.util.Calendar;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.hannah.arduinomotiondetector.GMailSender;
import com.hannah.arduinomotiondetector.NotificationPreferences;

public class SendNotificationTask extends AsyncTask<Void, Void, Void> {

	private static final String SENDER_EMAIL = "uoftarduinonotifier@gmail.com";
	private static final String SENDER_PASSWORD = "996883975";

	private Activity mActivity;
	
	public SendNotificationTask(Activity activity){
		mActivity = activity;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			GMailSender sender = new GMailSender(SENDER_EMAIL, SENDER_PASSWORD);
			sender.sendMail("Arduino Alert!", "New alert at " + Calendar.getInstance().getTime(), SENDER_EMAIL, NotificationPreferences.getEmail(mActivity));
		} catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
		}

		sendSMS(mActivity, NotificationPreferences.getPhone(mActivity), "Alert for message 50");
		
		return null;
	}
	
	private static void sendSMS(Activity activity, String phoneNumber, String message) {
		TelephonyManager telMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = telMgr.getSimState();

		if (simState == TelephonyManager.SIM_STATE_READY) {
			PendingIntent pi = PendingIntent.getActivity(activity, 0, new Intent(activity, SendNotificationTask.class), 0);
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, pi, null);
		} else{
			Toast.makeText(activity, "Phone cannot send text message", Toast.LENGTH_LONG).show();
		}
	}

}
