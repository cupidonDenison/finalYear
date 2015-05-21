package com.example.cityguide;

import java.io.File;

import com.example.cityguide.others.DatabaseHandler;
import com.example.cityguide.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends Activity {

	public static final String SERVER_IP = "http://192.168.1.4/";
	//public static final String SERVER_IP = "http://192.168.1.4/";
	//public static final String SERVER_IP = "http://www.denisonspace.herobo.com/";
	private static int SPLASH_TIME_OUT = 3000;
	private static String NUMBER_PREFERENCES_FILE = "/data/data/com.example.cityguide/shared_prefs/number.xml";
	static File f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		getActionBar().hide();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				f = new File(NUMBER_PREFERENCES_FILE);

				if ((f.exists())) {
					DatabaseHandler db = new DatabaseHandler(
							getApplicationContext());

					SharedPreferences pref = getSharedPreferences("number",
							Context.MODE_PRIVATE);

					String number = pref.getString("phone", "default value");
					int valid = db.checkActivation(number);
					
					Log.i("Sql verification", valid+"");
					
					if (valid > 0) {
						Intent i = new Intent(SplashScreen.this,
								HomeActivity.class);
						startActivity(i);

						finish();
					} else {
						Intent i = new Intent(SplashScreen.this,
								VerificationActivity.class);
						startActivity(i);

						finish();
					}//end if-else statement if verification is ok
				} else {
					Intent i = new Intent(SplashScreen.this, RegistrationActivity.class);
					startActivity(i);

					finish();
				}//end if statment number is set
			}//ens run()
		}, SPLASH_TIME_OUT);
	}//end onCreate()
}//end Activity
