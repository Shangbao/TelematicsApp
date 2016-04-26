package com.hangon.bean.map;


public class Gastprice {
	private String name;
	private String price;
   public Gastprice(String name,String price){
	   
   }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Gastprice [name=" + name + ", price=" + price + "]";
	}

}
