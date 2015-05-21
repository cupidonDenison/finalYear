package com.example.cityguide.others;

import android.graphics.Bitmap;

public class Friends {
	
	private String name, thumbnailUrl,num;
	private Bitmap image;
 
    public Friends(String name, String thumbnailUrl,Bitmap image, String num) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.image = image;
        this.num = num;
    }

    public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
    
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
 
  
}
