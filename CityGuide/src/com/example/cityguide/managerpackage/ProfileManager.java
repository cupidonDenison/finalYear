package com.example.cityguide.managerpackage;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cityguide.HomeActivity;
import com.example.cityguide.RegistrationActivity;
import com.example.cityguide.VerificationActivity;
import com.example.cityguide.callbackinterface.ProfileImpl;
import com.example.cityguide.callbackinterface.ProfileInterface;
import com.example.cityguide.sqlitehandler.DatabaseHandler;

public class ProfileManager {
	
	//private final static String 
	private final static String PROFILE_MGR = "profile_mgr_log";
	
	public static void checkProfileRegistration(Context context,ProfileInterface profile){
		final String NUMBER_PREFERENCES_FILE = "/data/data/com.example.cityguide/shared_prefs/profile.xml";
		 File registrationFile;
		 
		 int userStatus =-1;

		registrationFile = new File(NUMBER_PREFERENCES_FILE);

		if ((registrationFile.exists())) {
			DatabaseHandler db = new DatabaseHandler(context);

			SharedPreferences pref = context.getSharedPreferences("profile",
					Context.MODE_PRIVATE);
			String number = pref.getString("phone", "default value");
			int valid = db.checkActivation(number);
			Log.i(PROFILE_MGR, valid + " : registered user");
			if (valid > 0) {
				userStatus = 1;
			}// end if user has verified number
			else {
				userStatus = 0;
			}// End else user is registered but has not verify number

			
		}// end if preference file is found
		else{
			
			userStatus = -1;
			
		}//end else first time user use app
		
		
			profile.checkRegistration(userStatus);
		
	}//-----end function

}//End class