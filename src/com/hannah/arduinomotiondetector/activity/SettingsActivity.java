package com.hannah.arduinomotiondetector.activity;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.hannah.arduinomotiondetector.NotificationPreferences;
import com.hannah.arduinomotiondetector.R;
import com.hannah.arduinomotiondetector.WebSender;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		final EditText nameText = (EditText) findViewById(R.id.sensor_name_edit_text);
		final EditText emailText = (EditText) findViewById(R.id.email_edit_text);
		final EditText phoneText = (EditText) findViewById(R.id.phone_number_edit_text);
		final CheckBox emailOn = (CheckBox) findViewById(R.id.email_on);
		final CheckBox photoOn = (CheckBox) findViewById(R.id.photo_on);
		final CheckBox smsOn = (CheckBox) findViewById(R.id.sms_on);
		final CheckBox alarmOn = (CheckBox) findViewById(R.id.alarm_on);

		if (NotificationPreferences.hasPrefences(this)) {
			nameText.setText(NotificationPreferences.getSensorName(this));
			emailText.setText(NotificationPreferences.getEmail(this));
			phoneText.setText(NotificationPreferences.getPhone(this));
			emailOn.setChecked(NotificationPreferences.getEmailOn(this));
			photoOn.setChecked(NotificationPreferences.getPhotoOn(this));
			smsOn.setChecked(NotificationPreferences.getSMSOn(this));
			alarmOn.setChecked(NotificationPreferences.getAlarmOn(this));
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
				NotificationPreferences.saveAlertsOn(SettingsActivity.this, emailOn.isChecked(), photoOn.isChecked(), smsOn.isChecked(), alarmOn.isChecked());

				new WebSender().execute(getXMLMessage(name, email, phone));

				SettingsActivity.this.finish();
			}
		});
	}

	private String getXMLMessage(String name, String email, String phone) {
		LatLng ll = NotificationPreferences.getLocation(this);

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			org.w3c.dom.Element rootElement = document.createElement("sensor");
			rootElement.setAttribute("name", name);
			rootElement.setAttribute("email", email);
			rootElement.setAttribute("phone", phone);
			rootElement.setAttribute("latitude", ll.latitude + "");
			rootElement.setAttribute("longitude", ll.longitude + "");
			document.appendChild(rootElement);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			Properties outFormat = new Properties();
			outFormat.setProperty(OutputKeys.INDENT, "yes");
			outFormat.setProperty(OutputKeys.METHOD, "xml");
			outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			outFormat.setProperty(OutputKeys.VERSION, "1.0");
			outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperties(outFormat);
			DOMSource domSource = new DOMSource(document.getDocumentElement());
			OutputStream output = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(output);
			transformer.transform(domSource, result);
			return output.toString();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return null;
	}
}
