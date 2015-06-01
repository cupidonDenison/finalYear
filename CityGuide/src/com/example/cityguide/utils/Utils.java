package com.example.cityguide.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Utils {

	
	private final static String UTILS_TAG = "utils_log";
	 private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	public static void showSettingsAlert(final Context mContext) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS  settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	// -----------end function
	
	//Check if google play services is available
	
	public static  boolean checkPlayServices(Context context) {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(UTILS_TAG, "This device is not supported.");
	           
	        }
	        return false;
	    }else{
	    	Log.i(UTILS_TAG,"Google play services available");
	    }
	    return true;
	}//End checkPlayServices

	// ----------end function
	
	public static void storeDeviceId(Context context,String deviceId){
		SharedPreferences numberPref = context.getSharedPreferences(
				"profile",
				Context.MODE_PRIVATE);
		
		SharedPreferences.Editor preferencesEditor = numberPref.edit();
		preferencesEditor.putString("device_id", deviceId);
		preferencesEditor.putBoolean("isregistered", false);
		preferencesEditor.commit();
		
	}
	//End function
	
	
	public static String getStoreDDeviceId(Context context){
		String deviceId ="";
		SharedPreferences pref = context.getSharedPreferences("profile",
				Context.MODE_PRIVATE);

		deviceId = pref.getString("device_id", "default value");
		return deviceId;
	}
}// End class Utils
