package com.hangon.bean.map;



import java.io.Serializable;
import java.util.List;

/**
 * 加油站的与位置相关信息
 */
public class Datas implements Comparable<Datas>,Serializable{
	private String id;//编号
	private String name;//加油站名称
	private String area;//地区
	private String areaname;//地区名称
	private String address;//地址
	private String brandname;//品牌名称
	private String type;//加油站类型
	private String discount;//折扣
	private String exhaust;
	private String position;
	private double lon;
	private double lat;
	private List<Price> price;
	private List<Gastprice> gastprice;
	private String fwlsmc;
	private String distance;
    public Datas(String name,String addresss,String brandname,List<Gastprice> gasprice,double lon,double lat
    		    ,String distance){
     this.name=name;
     this.brandname=brandname;
     this.type=type;
     this.lat=lat;
     this.lon=lon;
     this.address=address;
     this.distance=distance;
     this.gastprice=gasprice;
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getExhaust() {
		return exhaust;
	}

	public void setExhaust(String exhaust) {
		this.exhaust = exhaust;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public List<Price> getPrice() {
		return price;
	}

	public void setPrice(List<Price> price) {
		this.price = price;
	}

	public List<Gastprice> getGastprice() {
		return gastprice;
	}

	public void setGastprice(List<Gastprice> gastprice) {
		this.gastprice = gastprice;
	}

	public String getFwlsmc() {
		return fwlsmc;
	}

	public void setFwlsmc(String fwlsmc) {
		this.fwlsmc = fwlsmc;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Datas [id=" + id + ", name=" + name + ", area=" + area
				+ ", address=" + address + ", brandname=" + brandname
				+ ", type=" + type + ", discount=" + discount + ", exhaust="
				+ exhaust + ", position=" + position + ", lon=" + lon
				+ ", lat=" + lat + ", price=" + price + ", gastprice="
				+ gastprice + ", fwlsmc=" + fwlsmc + ", distance=" + distance
				+ "]";
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public Datas(){
		
	}	
	@Override
	public int compareTo(Datas another) {
		return	Integer.valueOf(this.distance)-Integer.valueOf(another.distance);		
	}
	
}
