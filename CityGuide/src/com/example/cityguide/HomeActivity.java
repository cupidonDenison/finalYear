package com.example.cityguide;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.example.cityguide.communication.JSONParser;
import com.example.cityguide.entity.GridImage;
import com.example.cityguide.entity.Place;
import com.example.cityguide.location.MyLocation;
import com.example.cityguide.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class HomeActivity extends DrawerActivity {

	GridView Grid;
	ArrayList<GridImage> gridArray = new ArrayList<GridImage>();
	GridAdapter GridViewAdapter;
	ProgressDialog pDialog;
	public static List<Place> placesList = new ArrayList<Place>();
	static int responseCount;
	static String type;
	JSONObject responseObj;
	boolean connectionOK ;
	Bitmap place2Image;
	JSONParser parser = new JSONParser();
	private static final String placesURL = SplashScreen.SERVER_IP+ "project/getPlacesType.php";
	
	

	Document mapDoc;
	String provider;
	Criteria criteria = new Criteria();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
/*		getActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));*/ 
		ImageView view = (ImageView) findViewById(android.R.id.home);
		view.setPadding(5, 0, 2, 0); // left, top, right, bottom
		Log.i("Home_activity", "Home activity started");

		Grid = (GridView) findViewById(R.id.gridView);

		gridArray.add(new GridImage(R.drawable.ic_car, "Car Rental"));
		gridArray.add(new GridImage(R.drawable.ic_doctor, "Doctor"));
		gridArray.add(new GridImage(R.drawable.ic_hotel, "Hotel"));
		gridArray.add(new GridImage(R.drawable.ic_people, "People"));
		gridArray.add(new GridImage(R.drawable.ic_restaurant, "Restaurant"));
		gridArray.add(new GridImage(R.drawable.ic_tourist, "Tourist Place"));

		
		GridViewAdapter = new GridAdapter();
		Grid.setAdapter(GridViewAdapter);
		Grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				placesList = new ArrayList<Place>();
				onGridViewClick(pos);
			}
		});
	} // end onCreate

	@Override
	protected int getLayout() {
		return R.layout.activity_main;
	}

	class GridAdapter extends ArrayAdapter<GridImage> {

		public GridAdapter() {
			super(getApplicationContext(), R.layout.gridview_layout, gridArray);
		}// end constructor

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.gridview_layout, parent, false);
			}
			GridImage currentIcon = gridArray.get(position);
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.item_image);
			TextView textView = (TextView) itemView
					.findViewById(R.id.item_text);
			imageView.setImageResource(currentIcon.getImageId());
			textView.setText(currentIcon.imageName);
			return itemView;
		}

	}// end class MyGridAdapter

	public void onGridViewClick(int position) {
		/*
		 * Intent categoryIntent = new Intent(this, MenuActivity.class); type =
		 * gridArray.get(position).getImageName();
		 * categoryIntent.putExtra("Type", type); startActivity(categoryIntent);
		 */
		if (isOnline()) {
			type = gridArray.get(position).getImageName();
			if(type.equals("People")){
				Intent intent = new Intent(getApplicationContext(), PeopleActivity.class);
				startActivity(intent);
			}else{
				Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				startActivity(intent);
			}
			

		} else {
			Toast.makeText(getApplicationContext(),
					"You need to switch on wifi or data connection ",
					Toast.LENGTH_LONG).show();
		}// end else check for connection

	}// end onGridViewClick()

	

	// check for internet connection
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static String getType() {
		return type;
	}

	
}// End Activity
