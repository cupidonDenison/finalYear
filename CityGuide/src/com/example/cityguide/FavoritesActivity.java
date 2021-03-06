package com.example.cityguide;


import com.example.cityguide.others.Clist;
import com.example.cityguide.others.CustomList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FavoritesActivity extends DrawerActivity {
	
	String[] web = {
		    "Caudan Waterfront",
		      "Bagatelle Mall",
		    
		  } ;
	
	String[] description = {
		    "Shopping Mall",
		      "Mall of Mauritius",
		    
		  } ;
	
	String[] location = {
		    "-20.160063 57.497957",
		      "-20.223496 57.497352",
		    
		  } ;
	
	Integer[] imageId = {
		      R.drawable.download,
		      R.drawable.photo,
		      

		  };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		
		ListView l = (ListView)findViewById(R.id.listFav);
		
		Clist adapter = new Clist(FavoritesActivity.this, web, imageId,description,location);

		        l.setAdapter(adapter);
		        
		        Log.d("Favorite Places :", location[0] +"\n" + location[1]);

	}
}
