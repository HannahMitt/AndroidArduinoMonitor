package com.hannah.arduinomotiondetector.activity;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hannah.arduinomotiondetector.LocationFinder;
import com.hannah.arduinomotiondetector.NotificationPreferences;
import com.hannah.arduinomotiondetector.R;
import com.hannah.arduinomotiondetector.ValueMsg;
import com.hannah.arduinomotiondetector.tasks.ArduinoReaderRunnable;
import com.hannah.arduinomotiondetector.tasks.SendNotificationTask;

public class ArduinoReceiveDataActivity extends Activity {

	private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
	private static final String TAG = "ArduinoReceiver";

	private TextView mResponseField;

	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;
	private UsbAccessory mAccessory;
	private ParcelFileDescriptor mFileDescriptor;
	private FileInputStream mInputStream;
	private Marker mLocationMarker;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ValueMsg t = (ValueMsg) msg.obj;
			mResponseField.setText("Flag: " + t.getFlag() + "; Reading: " + t.getReading() + "; Date: " + (new Date().toString()));

			if (t.getReading() == 50) {
				new SendNotificationTask(ArduinoReceiveDataActivity.this).execute();
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (!NotificationPreferences.hasPrefences(this)) {
			startActivity(new Intent(this, SettingsActivity.class));
		}

		setUpUIElements();
		setupAccessory();
	}

	private void setUpUIElements() {
		setSensorNameTitle();

		mResponseField = (TextView) findViewById(R.id.arduinoresponse);

		findViewById(R.id.send_email_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("SendMail", "Button pressed");
				new SendNotificationTask(ArduinoReceiveDataActivity.this).execute();
			}
		});

		findViewById(R.id.location_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Location l = LocationFinder.getLocation(ArduinoReceiveDataActivity.this);
				if (l != null) {
					NotificationPreferences.saveLocation(ArduinoReceiveDataActivity.this, new LatLng(l.getLatitude(), l.getLongitude()));
					updateMapMarker();
					// new
					// WebSender().execute(NotificationPreferences.getSensorName(ArduinoReceiveDataActivity.this),
					// l.getLatitude() + ", " + l.getLongitude());
				} else {
					Toast.makeText(ArduinoReceiveDataActivity.this, "Could not get location.", Toast.LENGTH_LONG).show();
				}
			}
		});

		setUpMap();
	}

	private void setUpMap() {
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		UiSettings mapSettings = map.getUiSettings();
		mapSettings.setZoomControlsEnabled(false);
		mapSettings.setScrollGesturesEnabled(false);

		updateMapMarker();
	}

	private void updateMapMarker() {
		final GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		final View mapView = getFragmentManager().findFragmentById(R.id.map).getView();

		if (mLocationMarker != null) {
			mLocationMarker.remove();
		}

		if (NotificationPreferences.hasLocation(this)) {
			final LatLng ll = NotificationPreferences.getLocation(this);
			mLocationMarker = map.addMarker(new MarkerOptions().position(NotificationPreferences.getLocation(this)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 17));

			if (mapView.getViewTreeObserver().isAlive()) {
				mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 17));
					}
				});
			}
		}
	}

	private void setSensorNameTitle() {
		if (NotificationPreferences.hasSensorName(this)) {
			((TextView) findViewById(R.id.sensor_name)).setText(NotificationPreferences.getSensorName(this));
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setSensorNameTitle();

		if (mInputStream != null) {
			Log.e(TAG, "input stream was null on resume");
			return;
		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory, mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.e(TAG, "null accessory");
		}
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}

	private void setupAccessory() {
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);
		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}
	}

	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			ArduinoReaderRunnable runnable = new ArduinoReaderRunnable(mInputStream, mHandler);
			Thread thread = new Thread(null, runnable, "OpenAccessoryTest");
			thread.start();
			// Accessory opened
		} else {
			Log.e(TAG, "failed to open accessory");
		}
	}

	private void closeAccessory() {
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						// USB permission denied
					}
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
				if (accessory != null && accessory.equals(mAccessory)) {
					// accessory detached
					closeAccessory();
				}
			}
		}
	};

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, (android.view.Menu) menu);
		return super.onCreateOptionsMenu(menu);
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(ArduinoReceiveDataActivity.this, SettingsActivity.class));
		return super.onOptionsItemSelected(item);
	}

}