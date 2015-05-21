package com.example.cityguide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguide.entity.Contact;
import com.example.cityguide.others.ContactManager;
import com.example.cityguide.others.Friends;
import com.example.cityguide.others.JSONParser;
import com.example.cityguide.R;

public class PeopleActivity extends DrawerActivity {
	
	ListView peopleList;
	private TreeMap<String,Contact> allContacts;
	private List<Friends> friendList = new ArrayList<Friends>();
	ProgressDialog pDialog;
	ImageView imageView;
	
	ListImageParser adapter;
	
	Bitmap image;
	
	String name;
	String num;
	String imageLoc;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
	    imageView = (ImageView)findViewById(R.id.thumbnail);
	    
	    peopleList = (ListView) findViewById(R.id.peopleList);
	    new PhoneContact().execute();	    
	}
	
	@Override
	protected int getLayout() {
		return R.layout.activity_people;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.people, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == R.id.refresh) {
			//new PhoneContact().execute();
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);		
	}
	
	
	class ListImageParser extends ArrayAdapter<Friends> {
		
		public ListImageParser() {
			super(PeopleActivity.this, R.layout.list_friends_row, friendList);			
		}// End constructor
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			Friends currentIcon = friendList.get(position);
			Log.d("Friend List count", friendList.size() + " Contacts found");
			if (itemView == null) {			
				Log.d("Friends: ", "Phone Number: " + currentIcon.getNum()+ "\nName: " +currentIcon.getName());
				itemView = getLayoutInflater().inflate(
						R.layout.list_friends_row, parent, false);
			}
			
			ImageView imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
			imageView.setImageBitmap(currentIcon.getImage());

			TextView nameTextView = (TextView) itemView.findViewById(R.id.name);
			nameTextView.setText(currentIcon.getName());

			TextView numTextView = (TextView) itemView.findViewById(R.id.num);
			numTextView.setText(currentIcon.getNum());

			return itemView;		
		}
	}// end inner class
	
		
	class PhoneContact extends AsyncTask<Void, Void, TreeMap<String, Contact>> {
			
		@Override
		protected void onPreExecute() {
			pDialog = ProgressDialog.show(PeopleActivity.this, "Friends","Loading Friends");
			super.onPreExecute();
		}

		@Override
		protected TreeMap<String, Contact> doInBackground(Void... params) {
			ContactManager cm = new ContactManager(getApplicationContext());
			return cm.readPhoneContact();
		}

		@Override
		protected void onPostExecute(TreeMap<String, Contact> result) {
			
			allContacts = new TreeMap<String,Contact>();
			allContacts = result;
			int i=1;
			for (Entry<String, Contact> entry : result.entrySet()) {
				Contact newContact = entry.getValue();
				i++;
			}
			
			new FindFriend().execute();
			super.onPostExecute(result);
			pDialog.dismiss();			
		}
	}// end PhoneContact Task
	
	
	class FindFriend extends AsyncTask<Void, Void, Void> {
		
		JSONObject resultObj = new JSONObject();
		String url =SplashScreen.SERVER_IP+"project/test/testJsonString.php";
		String imageUrl = SplashScreen.SERVER_IP +"project/";
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {			
			pDialog = ProgressDialog.show(PeopleActivity.this, "Friends","Loading Friends");		
			super.onPreExecute();			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			JSONObject obj = new JSONObject();
			JSONArray jArray = new JSONArray();
			
			try {
				// Comparing and adding same contacts
				for (Entry<String, Contact> entry : allContacts.entrySet()) {
					JSONObject tempObj = new JSONObject();
					tempObj.put("number", entry.getKey());
					jArray.put(tempObj);
				}// end for loop
				
				obj.put("contacts", jArray);
				JSONParser parser = new JSONParser();
				resultObj =  parser.postJSON(url, obj);				
			} catch (JSONException e) {			
			}			
			return null;
		}//End doInBackground

		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			
			try {
				
				JSONArray responseArray = resultObj.getJSONArray("friends");
				Log.i("Contact friends from server", responseArray.length()+"");
				for(int i = 0; i < responseArray.length() ; i++){
					
					JSONObject friendObj  = responseArray.getJSONObject(i);			
					
					name = friendObj.getString("name");
					num = friendObj.getString("number");
					imageLoc = friendObj.getString("imageLoc");
					
					
					URL locImg = new URL(imageUrl + imageLoc);
					
					HttpURLConnection connection = (HttpURLConnection) locImg
							.openConnection();
					connection.setDoInput(true);
					Log.d("Connection",
							"Opening new connection for image");
					connection.connect();
					InputStream in = connection.getInputStream();

					Log.d("Friend creating inputStream",
							"Creating input stream for image");
					image = BitmapFactory.decodeStream(in);
					image = Bitmap.createScaledBitmap(image, 80, 80, true);
										
					friendList.add(new Friends(name, imageLoc,image,num));
					adapter = new ListImageParser();
					peopleList.setAdapter(adapter);		
				}
				
				peopleList.setOnItemClickListener(new OnItemClickListener() {
			          public void onItemClick(AdapterView<?> parent, View view,
			                  int position, long id) {
			        	  Friends temp = (Friends) peopleList.getItemAtPosition(position);	
			        
			        	  Intent i = new Intent(getApplicationContext(), FriendDetailActivity.class);
			        	  

			              i.putExtra("name", temp.getName());
			              i.putExtra("num", temp.getNum());
			              i.putExtra("image", temp.getImage());
			              startActivity(i);
			        	  
			          }
			          
				});
			} catch (JSONException e) {				
				e.printStackTrace();				
			}catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {		
				e.printStackTrace();				
			}
			super.onPostExecute(result);
		}
	}
}// End activity
