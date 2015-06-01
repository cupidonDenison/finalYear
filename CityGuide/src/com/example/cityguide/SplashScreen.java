package com.example.cityguide;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.cityguide.callbackinterface.GCMInterface;
import com.example.cityguide.callbackinterface.PlaceResponseListener;
import com.example.cityguide.callbackinterface.ProfileInterface;
import com.example.cityguide.entity.Place;
import com.example.cityguide.location.MyLocation;
import com.example.cityguide.managerpackage.GCMManager;
import com.example.cityguide.managerpackage.PlaceResponseManager;
import com.example.cityguide.managerpackage.ProfileManager;
import com.example.cityguide.utils.Utils;

public class SplashScreen extends Activity {

	public static final String SERVER_IP = "http://192.168.1.4/";
	// public static final String SERVER_IP = "http://192.168.1.4/";
	// public static final String SERVER_IP =
	// "http://www.denisonspace.herobo.com/";

	public static boolean isGooglePlayServicesAvailable = false;

	private static String DEVICE_ID = "";

	public static Context appcontext;
	// private static int SPLASH_TIME_OUT = 3000;

	public static Location myLocation;
	LocationManager locationManager;
	private final static String SPLASH_TAG = "splash_log";
	static RelativeLayout progressLayout;
	private static ArrayList<Place> allPlaces;

	private static int userStatus = -1;

	private static boolean loadPlace = false;
	
	public static boolean isUserRegistered ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		getActionBar().hide();

		appcontext = getApplicationContext();

		Log.i(SPLASH_TAG, "Splash screen launch");

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		myLocation = new MyLocation(this, locationManager).getLocation();

		if (myLocation != null) {
			Log.i(SPLASH_TAG, "latitude: " + myLocation.getLatitude());
		}

		isGooglePlayServicesAvailable = Utils.checkPlayServices(this);

		SharedPreferences pref = getSharedPreferences("profile",
				Context.MODE_PRIVATE);

		boolean isRegistered = pref.getBoolean("isregistered", false);
		
		Log.d(SPLASH_TAG, "isRegistered: " + isRegistered);
		Log.d(SPLASH_TAG, "Number: "+pref.getString("phone", "my number"));
		
		isUserRegistered = isRegistered;

		if (!isRegistered) {
			if (isGooglePlayServicesAvailable) {
				GCMManager.registerDevice(this, new GCMInterface() {

					@Override
					public void getRegistrationId(String registration_id) {
						DEVICE_ID = registration_id;
						Log.d(SPLASH_TAG, "\n\n\n\n Device Id:" + DEVICE_ID);
						
						Utils.storeDeviceId(appcontext, DEVICE_ID);
					}// get RegistrationId()
				});
			}// End if device support google play services
		}

		
		PlaceResponseManager.getPlaces(this, "all",
				new PlaceResponseListener() {

					@Override
					public void getPlace(ArrayList<Place> response) {

						allPlaces = response;

						Log.i(SPLASH_TAG, "Place size: " + allPlaces.size());
						loadPlace = true;

						// To run the code on the ui thread
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								int createSqliteRecord = new PlaceResponseManager()
										.storePlace(appcontext, allPlaces);
								Log.i(SPLASH_TAG, "Record added to sqlite: "
										+ createSqliteRecord);
								checkProfile();

							}
						});

						// End runOnUiThread()

						try {
							findViewById(R.id.loadingPanel).setVisibility(
									RelativeLayout.GONE);
						} catch (Exception e) {
							Log.e("Dialog loading error", "loading error", e);
						}
					}
				});// -------end call to get places

	}// end onCreate()

	public void checkProfile() {

		ProfileManager.checkProfileRegistration(appcontext,
				new ProfileInterface() {

					@Override
					public void checkRegistration(int registrationStatus) {

						Log.i(SPLASH_TAG, "registration status: "
								+ registrationStatus);
						userStatus = registrationStatus;

						switch (userStatus) {
						case -1:
							Intent i = new Intent(appcontext,
									RegistrationActivity.class);
							startActivity(i);
							finish();
							break;
						case 0:
							Intent i1 = new Intent(appcontext,
									VerificationActivity.class);
							startActivity(i1);
							finish();

							break;
						case 1:
							Intent i2 = new Intent(appcontext,
									HomeActivity.class);
							startActivity(i2);
							finish();

							break;

						default:
							break;
						}// End switch

					}// end checkRegistration()
				});// End checkProfileRegistration

		// -------end call to verify contact

	}

	// ----end function

	public static ArrayList<Place> getAllPlaces() {

		return allPlaces;

	}

}// end Activity
