package com.example.cityguide.sqlitehandler;

import java.util.ArrayList;
import java.util.List;

import com.example.cityguide.entity.Favorites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "trackMeDB";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "activation_table";
	public static final String TABLE_FAVORITES = "favorites";
	
	private static final String COLUMN_PHONE_NUM = "phone_number";
	private static final String COLUMN_STATUS = "status";
	
	private static final String COLUMN_FNAME = "place_name";
	private static final String COLUMN_LATITUDE = "lat";
	private static final String COLUMN_LONGITUDE = "lng";
	
	
			
	
	public DatabaseHandler(Context context){
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}//end constructor

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String createTable = "CREATE TABLE "+TABLE_NAME+"("+COLUMN_PHONE_NUM+" TEXT PRIMARY KEY,"+COLUMN_STATUS+" INTEGER)";
		db.execSQL(createTable);
		
		String CREATE_TABLE_FAVORITES = "CREATE TABLE "+TABLE_FAVORITES +"(" 
				+ COLUMN_FNAME+" TEXT,"		
				+COLUMN_LATITUDE+" REAL ,"
				+COLUMN_LONGITUDE+ " REAL)";
		db.execSQL(CREATE_TABLE_FAVORITES);
	}//end onCreate

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_FAVORITES);

	}//End onUpgrade
	
	public void createFavorites(Favorites f){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_FNAME, f.getFname());
		values.put(COLUMN_LATITUDE, f.getLat());
		values.put(COLUMN_LONGITUDE, f.getLng());
		
		db.insert(TABLE_FAVORITES, null, values);
		db.close();
		
	}
	
	//getting all favorite places users added in the database
	public List<Favorites> getAllFavorites(){
		List<Favorites> favList = new ArrayList<Favorites>();
		
		String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
            do {          	
            	Favorites favorites = new Favorites();
            	favorites.setFname(cursor.getString(0)); // getting name
            	favorites.setLat(Double.parseDouble(cursor.getString(1))); //getting location
            	favorites.setLng(Double.parseDouble(cursor.getString(2))); //getting location  
            	
            	// save all the data in a list
            	favList.add(favorites);           
            }while(cursor.moveToNext());}
		return favList;
	}
	
	public int checkActivation(String number){
		int result = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		
		String select_query = "SELECT * FROM "+TABLE_NAME+ " WHERE "+COLUMN_PHONE_NUM+" =  '"+number+"' AND "+COLUMN_STATUS+" = 1";
		//String select_query = "SELECT * FROM "+TABLE_NAME;
		//String select_query = "SELECT * FROM "+TABLE_NAME+ " WHERE "+COLUMN_PHONE_NUM+" =  '"+number+"'";
		Cursor cursor = db.rawQuery(select_query, null);
		
		
		result = cursor.getCount();
		String res ="";
		if(result >0){
			cursor.moveToNext();
			res = cursor.getString(1);
		}
		
		db.close();
		return result;
	}//End checkActivation()
	
	public void setActivation(String number,int status){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		String updateStr = "UPDATE "+TABLE_NAME+ " SET "+COLUMN_STATUS+ " = "+ status +" WHERE "+COLUMN_PHONE_NUM+ " ='"+number+"'";
		
		db.execSQL(updateStr);
		
		db.close();
		
	}//End setActivation
	
	public void createRecord(String number){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_PHONE_NUM, number);
		values.put(COLUMN_STATUS, 1);
		
		db.insert(TABLE_NAME, null, values);
		
		db.close();
	}//End createRecord()

}//End class DatabaseHandler