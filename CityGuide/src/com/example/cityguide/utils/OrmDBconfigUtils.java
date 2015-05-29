package com.example.cityguide.utils;

import java.io.IOException;
import java.sql.SQLException;

import com.example.cityguide.entity.Place;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class OrmDBconfigUtils extends OrmLiteConfigUtil {
	
	
	static Class<?> classes[] = new Class[]{Place.class};
	
	public static void main(String args[]){
		try {
			writeConfigFile("ormlite_config.txt",classes);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}//end class