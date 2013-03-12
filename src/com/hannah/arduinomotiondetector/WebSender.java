package com.hannah.arduinomotiondetector;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class WebSender extends AsyncTask<String, Void, Void> {

	private static final String TAG = "WebSender";

	private static final int PORT = 4400;
	private static final String SERVER_IP = "142.1.129.187";

	@Override
	protected Void doInBackground(String... arg0) {
		Log.d(TAG, "Message: " + arg0[0]);
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
			Log.d(TAG, "C: Connecting to " + SERVER_IP + "...");
			Socket socket = new Socket(serverAddr, PORT);
			try {
				Log.d(TAG, "C: Sending: " + arg0[0]);
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				// where you issue the commands
				out.println("Hey Server, from Android!");
				out.println(arg0[0]);
				out.flush();
				out.close();
				Log.d(TAG, "C: Sent.");
			} catch (Exception e) {
				Log.e(TAG, "S: Error", e);
			}
			socket.close();
			Log.d(TAG, "C: Closed.");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "C: Error", e);
		}

		return null;
	}
}
