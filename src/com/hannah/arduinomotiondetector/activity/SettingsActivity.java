package com.hannah.arduinomotiondetector.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hannah.arduinomotiondetector.NotificationPreferences;
import com.hannah.arduinomotiondetector.R;
import com.hannah.arduinomotiondetector.WebSender;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		final EditText nameText = (EditText)findViewById(R.id.sensor_name_edit_text);
		final EditText emailText = (EditText)findViewById(R.id.email_edit_text);
		final EditText phoneText = (EditText)findViewById(R.id.phone_number_edit_text);
		
		if(NotificationPreferences.hasPrefences(this)){
			nameText.setText(NotificationPreferences.getSensorName(this));
			emailText.setText(NotificationPreferences.getEmail(this));
			phoneText.setText(NotificationPreferences.getPhone(this));
		}
		
		Button saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String name = nameText.getText().toString();
				String email = emailText.getText().toString();
				String phone = phoneText.getText().toString();
				NotificationPreferences.saveSensorName(SettingsActivity.this, name);
				NotificationPreferences.saveEmail(SettingsActivity.this, email);
				NotificationPreferences.savePhone(SettingsActivity.this, phone);
				
				new WebSender().execute(NotificationPreferences.getSensorName(SettingsActivity.this),"hi");
				
				SettingsActivity.this.finish();
			}
		});
	}
}
