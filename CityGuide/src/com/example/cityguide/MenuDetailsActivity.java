package com.example.cityguide;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cityguide.R;
import com.example.cityguide.communication.JSONParser;
import com.example.cityguide.entity.Place;
import com.google.gson.Gson;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MenuDetailsActivity extends FragmentActivity {

	ViewPager pager;
	TextView textViewPlaceName, textViewAddress, textViewDistance;
	RatingBar ratingBar;
	ImageView imageViewPlace;
	Button buttonFindPlace;
	Place selectedPlace;
	static String placeId,placeAddress;
	static double placeLat, placeLng;
	JSONParser parser;
	JSONObject jsonObj;
	static int placeRating;
	String placeDetailURL = SplashScreen.SERVER_IP
			+ "project/placeDetail.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_details);
		
	}// End onCreate()

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}// end onOptionItemSelected()
	
}//End Activity