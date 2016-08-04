package com.hangon.bean.miancar;

/**
 * Created by Administrator on 2016/8/3.
 */
public class result implements  Comparable<result> {
    private String name;
    private MLocation location;
    private String address;
    private String street_id;
    private String telephone;
    private int detail;
    private String uid;
    private DetaileInfo detail_info;

    public result(String name, MLocation location, String address, String street_id, String telephone, int detail, String uid, DetaileInfo detail_info) {
        this.name = name;
        this.location = location;
        this.address = address;
        this.street_id = street_id;
        this.telephone = telephone;
        this.detail = detail;
        this.uid = uid;
        this.detail_info = detail_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MLocation getLocation() {
        return location;
    }

    public void setLocation(MLocation location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet_id() {
        return street_id;
    }

    public void setStreet_id(String street_id) {
        this.street_id = street_id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public DetaileInfo getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(DetaileInfo detail_info) {
        this.detail_info = detail_info;
    }

    @Override
    public int compareTo(result another) {
        return this.detail_info.getDistance()-another.detail_info.getDistance();
    }
}
