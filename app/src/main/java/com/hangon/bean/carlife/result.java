package com.hangon.bean.carlife;

/**
 * Created by Administrator on 2016/8/3.
 */
public class result {
    private String name;
    private MLocation location;
    private String address;
    private String street_id;
    private String telephone;
    private int detail;
    private String uid;



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStreet_id() {
        return street_id;
    }

    public void setStreet_id(String street_id) {
        this.street_id = street_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MLocation getLocation() {
        return location;
    }

    public void setLocation(MLocation location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
