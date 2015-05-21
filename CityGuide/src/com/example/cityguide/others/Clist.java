package com.example.cityguide.others;


import com.example.cityguide.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Clist extends ArrayAdapter<String>{
	
	
	private final Activity context;
	private final String[] web;
	private final String[] description;
	private final String[] location;
	private final Integer[] imageId;
	
	public Clist(Activity context,
	String[] web, Integer[] imageId, String[]description, String[] location) {
	super(context, R.layout.menu_row, web);
	this.context = context;
	this.web = web;
	this.description = description;
	this.location = location;
	this.imageId = imageId;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.menu_row, null, true);
	
	TextView pName = (TextView) rowView.findViewById(R.id.txt_Name);
	TextView pDesc = (TextView) rowView.findViewById(R.id.txt_description);
	TextView plocation = (TextView) rowView.findViewById(R.id.txt_Location);
	ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
	pName.setText(web[position]);
	pDesc.setText(description[position]);
	plocation.setText(location[position]);
	imageView.setImageResource(imageId[position]);
	return rowView;
	}

}
