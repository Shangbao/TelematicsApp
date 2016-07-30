package com.hangon.video;

import android.graphics.Bitmap;

public class CarRecode {
	
	private Bitmap bitmap;
	private String path;
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public CarRecode(Bitmap bitmap, String path) {
		super();
		this.bitmap = bitmap;
		this.path = path;
	}
	public CarRecode() {
		super();
	}
	
	
	

}
