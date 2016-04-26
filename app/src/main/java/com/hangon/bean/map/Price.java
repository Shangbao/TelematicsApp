package com.hangon.bean.map;

public class Price {
	 private String type;
	 private String price;
	 public Price(String type,String price){
		 this.type=type;
		 this.price=price;
	 }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Price [type=" + type + ", price=" + price + "]";
	}
	
}
