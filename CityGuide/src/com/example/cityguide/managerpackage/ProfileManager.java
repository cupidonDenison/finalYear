package com.example.cityguide.managerpackage;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cityguide.HomeActivity;
import com.example.cityguide.RegistrationActivity;
import com.example.cityguide.SplashScreen;
import com.example.cityguide.VerificationActivity;
import com.example.cityguide.callbackinterface.ProfileImpl;
import com.example.cityguide.callbackinterface.ProfileInterface;
import com.example.cityguide.sqlitehandler.DatabaseHandler;

public class ProfileManager {

	// private final static String
	private final static String PROFILE_MGR = "profile_mgr_log";

	public static void checkProfileRegistration(Context context,
			ProfileInterface profile) {
		final String NUMBER_PREFERENCES_FILE = "/data/data/com.example.cityguide/shared_prefs/profile.xml";
		File registrationFile;

		int userStatus = -1;

		registrationFile = new File(NUMBER_PREFERENCES_FILE);
		// registrationFile = context.getDatabasePath("profile.xml");

		SharedPreferences pref = context.getSharedPreferences("profile",
				Context.MODE_PRIVATE);

		//boolean isREgistered = pref.getBoolean("isRegistered", false);
		
		boolean isRegistered = SplashScreen.isUserRegistered;
		
		Log.d(PROFILE_MGR, "isRegistered: "+isRegistered);

		if (isRegistered) {
			if ((registrationFile.exists())) {

				Log.d(PROFILE_MGR, "Preference file exist");
				//DatabaseHandler db = new DatabaseHandler(context);

				String number = pref.getString("phone", "default value");
				// boolean valid = pref.getBoolean("verify", false);
				boolean verify = pref.getBoolean("verify", false);

				//int valid = db.checkActivation(number);
				Log.i(PROFILE_MGR, verify + " : registered user");
				if (verify) {
					userStatus = 1;
				}// end if user has verified number
				else {
					userStatus = 0;
				}// End else user is registered but has not verify number

			}// end if preference file is found
			else {
				Log.d(PROFILE_MGR, "Preference file does not  exist");
				userStatus = -1;

			}// end else first time user use app
		}else{
			Log.d(PROFILE_MGR, "User not registered");
		}

		profile.checkRegistration(userStatus);

	}// -----end function

}// End class
