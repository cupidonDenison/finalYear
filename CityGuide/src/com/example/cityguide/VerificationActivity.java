package com.example.cityguide;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.cityguide.communication.JSONParser;
import com.example.cityguide.sqlitehandler.DatabaseHandler;
import com.example.cityguide.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class VerificationActivity extends Activity {

	ProgressDialog pDialog;
	String result;

	private static final String numberURL = SplashScreen.SERVER_IP+"project/verifyNumber.php";
	//private static String NUMBER_PREFERENCES_FILE = "/data/data/com.example.cityguide/shared_prefs/profile";
	String number, codeText;

	EditText codeEditText;
	static DatabaseHandler db;

	JSONParser parser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);

		SharedPreferences pref = getSharedPreferences("profile",
				Context.MODE_PRIVATE);

		number = pref.getString("phone", "default value");
		codeEditText = (EditText) findViewById(R.id.edt_Code);
		
		 Log.i("number from preferences", number);
		// If your minSdkVersion is 11 or higher use:
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/*public void menus(View view) {
		new CheckCode().execute();
	}
*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verification, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
			return true;
		}
		
		if (id == R.id.ok) {
			new CheckCode().execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class CheckCode extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(VerificationActivity.this);
			pDialog.setMessage("verifying Code.....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			List<NameValuePair> details = new ArrayList<NameValuePair>();

			codeText = codeEditText.getText().toString();
			details.add(new BasicNameValuePair("code", codeText));
			details.add(new BasicNameValuePair("number", number));

			JSONObject responseObj = parser.makeHttpRequest(numberURL, "POST",
					details);

			Log.i("result object", responseObj.toString());
			try {

				result = responseObj.getString("result");

				 Log.e("Result from verificationActivity ", result);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void res) {
			pDialog.dismiss();
			if (result.equals("true")) {
				
				db = new DatabaseHandler(getApplicationContext());
				
				db.createRecord(number);
				
				SharedPreferences numberPref = getSharedPreferences(
						"profile",
						Context.MODE_PRIVATE);
				
				SharedPreferences.Editor preferencesEditor = numberPref.edit();
				preferencesEditor.putBoolean("verify", true);
				preferencesEditor.commit();
				
				Intent m = new Intent(getApplicationContext(),
						HomeActivity.class);
				VerificationActivity.this.finish();
				startActivity(m);
			}// End if statement
			else {
				InputMethodManager inputManager = (InputMethodManager) VerificationActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				codeEditText.setText("");
				Toast.makeText(getApplicationContext(), "Wrong Code",
						Toast.LENGTH_LONG).show();
			}// End else statement
		}// end onPostExecute()

	}// end class GetCode

}// End Activity
