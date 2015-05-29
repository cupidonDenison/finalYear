package com.example.cityguide;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cityguide.communication.JSONParser;
import com.example.cityguide.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	private static final String numberURL = SplashScreen.SERVER_IP+"project/addNumber.php";
	private static String NUMBER_PREFERENCES_FILE = "/data/data/com.example.cityguide/shared_prefs/number.xml";
	
	private static String result;
	ImageButton mainBtn;
	String num, name;
	EditText nameEditText, numberEditText;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		numberEditText = ((EditText) findViewById(R.id.edtPhone));
		nameEditText = ((EditText) findViewById(R.id.edtName));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_up, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == R.id.ok) {
			openAlert();
			return true;
		}
		return super.onOptionsItemSelected(item);		
	}

	private void openAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				RegistrationActivity.this);
		num = numberEditText.getText().toString();
		name = nameEditText.getText().toString();
		num = "+230"+num;
		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(parms);

		layout.setGravity(Gravity.CLIP_VERTICAL);
		layout.setPadding(2, 2, 2, 2);

		TextView phone_num = new TextView(this);
		phone_num.setText(num);

		LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tv1Params.bottomMargin = 5;
		layout.addView(phone_num, tv1Params);

		alertDialogBuilder.setView(layout);
		alertDialogBuilder.setTitle("Phone Number Verification");
		alertDialogBuilder.setMessage("Verify Your Phone Number : ");

		// set positive button: Yes message
		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// send number to server

						new SendNumber().execute();
						// create sharedPreferences to store phone number

						// go to a new activity of the app

					}
				});
		// set negative button: No message
		alertDialogBuilder.setNegativeButton("Edit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// cancel the alert box and put a Toast to the user
						dialog.cancel();
					}
				});
		// set neutral button: Exit the app message
		alertDialogBuilder.setNeutralButton("Exit the app",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// exit the app and go to the HOME
						RegistrationActivity.this.finish();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		// show alert
		alertDialog.show();
	}

	private class SendNumber extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(RegistrationActivity.this);
			pDialog.setMessage("Registering Users.....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			JSONParser parser = new JSONParser();
			
			// Building Parameters
			List<NameValuePair> details = new ArrayList<NameValuePair>();
			details.add(new BasicNameValuePair("number", num));
			details.add(new BasicNameValuePair("name", name));

			// getting JSON Object
            // Note that url accepts POST method
			JSONObject responseObject = parser.makeHttpRequest(numberURL,
					"POST", details);
			
			// check log cat for response
			Log.i("Number", responseObject.toString());

			try {
				result = responseObject.getString("result");				
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "An error occured..Please Try again", Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void resul) {
			pDialog.dismiss();
			
			if (result.equals("true")) {
				//Log.i("Number result t ", result);
				SharedPreferences numberPref = getSharedPreferences(
						"profile",
						Context.MODE_PRIVATE);
				
				SharedPreferences.Editor preferencesEditor = numberPref.edit();
				preferencesEditor.putString("phone", num);
				preferencesEditor.commit();
				
				
				Intent positveActivity = new Intent(
						getApplicationContext(), VerificationActivity.class);
				
				RegistrationActivity.this.finish();
				
				startActivity(positveActivity);

			} else if(result.equals("false")){
				Log.i("Number result f ", result);
				Toast.makeText(getApplicationContext(), "Invalid number",
						Toast.LENGTH_LONG).show();
			}else if(result.equals("no data")){
				Log.i("Number result n ", result);
				Toast.makeText(getApplicationContext(), "All field are required",
						Toast.LENGTH_LONG).show();
			}
		}

	}// End class SendNumber

}// End Activity
