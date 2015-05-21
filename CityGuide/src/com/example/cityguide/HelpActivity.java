package com.example.cityguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.cityguide.R;
import com.example.cityguide.others.HelpAdapter;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class HelpActivity extends DrawerActivity {
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		expListView = (ExpandableListView) findViewById(R.id.helpEL);

        // preparing list data
        prepareListData();
 
        listAdapter = new HelpAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
        	
        	
        	 @Override
             public boolean onGroupClick(ExpandableListView parent, View v,
                     int groupPosition, long id) {
                 // Toast.makeText(getApplicationContext(),
                 // "Group Clicked " + listDataHeader.get(groupPosition),
                 // Toast.LENGTH_SHORT).show();
                 return false;
             }
        });
        
     // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
                
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                
            }
        });
        
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {

               
                return false;
            }
        });
	}
	
	
	 private void prepareListData() {
	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<String>>();
	 
	        // Adding child data
	        listDataHeader.add("About");
	        listDataHeader.add("How to use City Guide");
	        listDataHeader.add("Contact Us");

	        // Adding child data
	        List<String> about = new ArrayList<String>();
	        about.add("City Guide Version 1.0");

	 
	        List<String> FAQ = new ArrayList<String>();
	        FAQ.add("City Guide is a location based application created to help you to visit unknown places without " +
	        		" any troubles. It provides you all the places, friends and events available around you based on your location " + "\n\n" +
	        		"Important Note : You need to enable your GPS and internet connection to make best use of City Guide");
	      
	 
	        List<String> Contact = new ArrayList<String>();
	        Contact.add("Need Help?? \n\nYou may contact us on 59120186 or 59270885 for any queries");

	 
	        listDataChild.put(listDataHeader.get(0), about); // Header, Child data
	        listDataChild.put(listDataHeader.get(1), FAQ);
	        listDataChild.put(listDataHeader.get(2), Contact);
	    }


}
