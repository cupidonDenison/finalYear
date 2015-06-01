package com.example.cityguide.managerpackage;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cityguide.callbackinterface.GCMInterface;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMManager {
	private static final String SENDER_ID = "609320741786";
	private final static String GCM_TAG = "gcm_log";

	public static void registerDevice(final Context context, final GCMInterface gcmInterface) {
		
		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				String registrationId = "";

				GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
				try {
					registrationId = gcm.register(SENDER_ID);
				} catch (IOException e) {
					Log.e(GCM_TAG, "registration error", e);
					e.printStackTrace();
				}
				
				Log.d(GCM_TAG, "registration id: "+registrationId);
				gcmInterface.getRegistrationId(registrationId);
				return null;
			}
			
		}.execute();
		

	}
	// -------end registerDevice()

}// End class
