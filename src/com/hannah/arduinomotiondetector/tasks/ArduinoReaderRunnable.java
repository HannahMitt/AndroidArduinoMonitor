package com.hannah.arduinomotiondetector.tasks;

import java.io.FileInputStream;
import java.io.IOException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hannah.arduinomotiondetector.ValueMsg;

public class ArduinoReaderRunnable implements Runnable{
	
	private static final String TAG = "ArduinoReceiver";
	private FileInputStream mInputStream;
	private Handler mHandler;
	
	public ArduinoReaderRunnable(FileInputStream inputStream, Handler handler){
		mInputStream = inputStream;
		mHandler = handler;
	}
	
	@Override
	public void run() {
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		while (true) { // read data
			try {
				ret = mInputStream.read(buffer);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
				break;
			}

			i = 0;
			while (i < ret) {
				int len = ret - i;
				if (len >= 1) {
					Message m = Message.obtain(mHandler);
					int value = (int) buffer[i];
					// 'f' is the flag, use for your own logic
					// value is the value from the arduino
					m.obj = new ValueMsg('f', value);
					mHandler.sendMessage(m);
				}
				i += 1; // number of bytes sent from arduino
			}

		}
	}

}
