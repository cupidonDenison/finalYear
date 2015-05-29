package com.example.cityguide.managerpackage;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cityguide.SplashScreen;
import com.example.cityguide.callbackinterface.PlaceResponseListener;
import com.example.cityguide.communication.JSONParser;
import com.example.cityguide.entity.Place;
import com.example.cityguide.sqlitehandler.PlaceDatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class PlaceResponseManager {

	private static String PLACE_TYPE;
	private final String PLACE_REPONSE_MANAGER_TAG = "place_response_manager";
	private static PlaceResponseListener responseListener;
	
	private static final String DATABASE_TAG = "sqlite_log";
	
	

	public static void getPlaces(Context context, String type,
			PlaceResponseListener responseListener) {
		PlaceResponseManager.responseListener = responseListener;
		PLACE_TYPE = type;

		new PlaceResponseManager().new PlacesAsync().execute();
	}// End getAllPlaces()

	// ---end function

	class PlacesAsync extends AsyncTask<Void, Void, Void> {
		JSONParser parser = new JSONParser();
		JSONObject responseObj;
		int responseCount;
		ArrayList<Place> placeList = new ArrayList<Place>();

		String placeUrl = SplashScreen.SERVER_IP + "project/getPlacesType.php";

		public boolean isOnline() {
			ConnectivityManager cm = (ConnectivityManager) SplashScreen.appcontext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			return netInfo != null && netInfo.isConnectedOrConnecting();
		}

		@Override
		protected Void doInBackground(Void... pr) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type", PLACE_TYPE));
			params.add(new BasicNameValuePair("startLat",
					SplashScreen.myLocation.getLatitude() + ""));
			params.add(new BasicNameValuePair("startLong",
					SplashScreen.myLocation.getLongitude() + ""));

			responseObj = parser.makeHttpRequest(placeUrl, "POST", params);

			Log.i(PLACE_REPONSE_MANAGER_TAG,
					"lat: " + SplashScreen.myLocation.getLatitude());

			if (isOnline()) {

				Log.i("Response Type", PLACE_TYPE);
				Log.i("Response object", responseObj.toString());

				try {
					responseCount = responseObj.getInt("count");

					String responseStatus = responseObj.getString("status");

					Log.i("Response status : ", responseStatus);
					Log.i("response count", String.valueOf(responseCount));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					if (responseCount > 0) {
						JSONArray placesArray = responseObj
								.getJSONArray("places");
						for (int i = 0; i < responseCount; i++) {
							JSONObject obj = placesArray.getJSONObject(i);

							// getting all information about a place
							String id = obj.getString("place_id");
							String icon = obj.getString("icon");
							String name = obj.getString("name");
							double latitude = obj.getDouble("latitude");
							double longitude = obj.getDouble("longitude");

							JSONArray typeArray = obj.getJSONArray("types")
									.getJSONArray(0);

							ArrayList<String> typesArrayList = new ArrayList<String>();

							for (int j = 0; j < typeArray.length(); j++) {
								String placeType = typeArray.getString(j);
								if (!(placeType.equals("establishment"))) {
									typesArrayList.add(placeType);
								}
							}// end for loop

							// creating new object of the class Place
							Place newPlace = new Place(id, name, latitude,
									longitude, typesArrayList);

							List<NameValuePair> p = new ArrayList<NameValuePair>();

							Log.w("response location",
									SplashScreen.myLocation.getLatitude()
											+ " "
											+ ""
											+ SplashScreen.myLocation
													.getLongitude());
							
							
							Log.w("response Place location", "endLat: "+latitude+" endLon:"+longitude);
							
							p.add(new BasicNameValuePair("startLat",
									SplashScreen.myLocation.getLatitude() + ""));
							p.add(new BasicNameValuePair("startLong",
									SplashScreen.myLocation.getLongitude() + ""));
							p.add(new BasicNameValuePair("endLat", latitude
									+ ""));
							p.add(new BasicNameValuePair("endLong", longitude
									+ ""));

							JSONObject object = parser.makeHttpRequest(
									SplashScreen.SERVER_IP
											+ "/project/getLocationDetails.php",
									"POST", p);

							String dist = object.getString("distance");
							// Calculating distance between user and the place
							newPlace.setDistance(Double.parseDouble(String
									.format("%.2f",
											Double.parseDouble(dist) / 1000)));

							placeList.add(newPlace);

						}// end loop in placeArray
						Log.i("response placeList", "size: " + placeList.size());
						responseListener.getPlace(placeList);
					}// End if responseCount > 0
				}// End try

				catch (Exception e) {

					Log.e("Response error", "errror in jsonArray catch", e);
				}// End catch

			}// End if isOnline

			return null;
		}// ENd doInBackground()
		
		//----------function to store places in sqlite database
		
		

	}// End AsyncTask
	
	
	public int storePlace(Context context,ArrayList<Place> allPlaces){
		PlaceDatabaseHelper dbHelper = OpenHelperManager.getHelper(context,PlaceDatabaseHelper.class);
		RuntimeExceptionDao<Place,String> placeDao = dbHelper.getPlaceRuntimeExceptionDao();
		placeDao.delete(getAllPlacesFomSqliteDB(context));
		int insert = 0;
		
		Log.i(DATABASE_TAG, "Dao Created");
		for(Place place : allPlaces){
			try{
			insert += placeDao.create(place);
			}catch(Exception e){
				Log.e(DATABASE_TAG, "Object insertion error", e);
			}
		}//End for loop
		OpenHelperManager.releaseHelper();
		Log.i(DATABASE_TAG, "database helper release");
		return insert;
	}//end function
	
	public List<Place> getAllPlacesFomSqliteDB(Context context){
		PlaceDatabaseHelper dbHelper = OpenHelperManager.getHelper(context,PlaceDatabaseHelper.class);
		RuntimeExceptionDao<Place,String> placeDao = dbHelper.getPlaceRuntimeExceptionDao();
		
		
		OpenHelperManager.releaseHelper();
		return placeDao.queryForAll();
	}//end function
	
	


}// End class
