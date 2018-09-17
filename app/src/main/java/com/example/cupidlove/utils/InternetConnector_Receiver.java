package com.example.cupidlove.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetConnector_Receiver extends BroadcastReceiver {



	public InternetConnector_Receiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {

			boolean isVisible = MyApplication.isActivityVisible();// Check if
																	// activity
																	// is
																	// visible
																	// or not
			Log.e("Activity is Visible ", "Is activity visible : " + isVisible);

			// If it is visible then trigger the task else do nothing
			if (isVisible == true) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager
						.getActiveNetworkInfo();

				// Check internet connection and accrding to state change the
				// text of activity by calling method
				if (networkInfo != null && networkInfo.isConnected()) {

					((BaseActivity)MyApplication.activity).changeTextStatus(true);
				} else {
					((BaseActivity)MyApplication.activity).changeTextStatus(false);
				}
			}
		} catch (Exception e) {

			Log.e("Exception is ",e.getMessage() + ":- Internate Exception ");
			e.printStackTrace();
		}

	}
}
