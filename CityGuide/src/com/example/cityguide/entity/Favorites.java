package com.example.cityguide.entity;

public class Favorites {
	
	    String fname;
	    Double lat,lng;
	 
	    // constructors
	    public Favorites() {
	    }
	 
	    public Favorites(String fname, Double lat, Double lng) {
	        this.fname = fname;
	        this.lat = lat;
	        this.lng = lng;
	    }

		public String getFname() {
			return fname;
		}

		public void setFname(String fname) {
			this.fname = fname;
		}

		public Double getLat() {
			return lat;
		}

		public void setLat(Double lat) {
			this.lat = lat;
		}

		public Double getLng() {
			return lng;
		}

		public void setLng(Double lng) {
			this.lng = lng;
		}

	
		
	

}
