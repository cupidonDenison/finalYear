package com.example.cityguide.sqlitehandler;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.cityguide.entity.Place;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class PlaceDatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME="citi_guide.db";
	private static final  int DATABASE_VERSION = 1;
	private RuntimeExceptionDao<Place, String> placeRuntimeExceptionDao;
	
	public PlaceDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION,com.example.cityguide.R.raw.ormlite_config);
	}
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {

		try {
			TableUtils.createTable(arg1, Place.class);
			//TableUtils.clearTable(arg1, Place.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//End onCreate

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub;
		
		try {
			TableUtils.dropTable(arg1, Place.class, true);
			onCreate(arg0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//end onUpgrade
	
	
	public RuntimeExceptionDao<Place, String> getPlaceRuntimeExceptionDao(){
		
		if(placeRuntimeExceptionDao == null){
			placeRuntimeExceptionDao = getRuntimeExceptionDao(Place.class);
		}
		return placeRuntimeExceptionDao;
	}//End function

}//End class