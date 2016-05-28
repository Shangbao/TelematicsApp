package com.hangon.order.activity;

import java.util.ArrayList;
import java.util.List;

public class PersonalInformationData {
    private String name;
    private String phoneNum;
    private int provinceIndex;//车牌省份索引
    private String carLicenceTail;//车牌尾号
    private int state;

    public PersonalInformationData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getProvinceIndex() {
        return provinceIndex;
    }

    public void setProvinceIndex(int provinceIndex) {
        this.provinceIndex = provinceIndex;
    }

    public String getCarLicenceTail() {
        return carLicenceTail;
    }

    public void setCarLicenceTail(String carLicenceTail) {
        this.carLicenceTail = carLicenceTail;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PersonalInformationData{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", provinceIndex=" + provinceIndex +
                ", carLicenceTail='" + carLicenceTail + '\'' +
                ", state=" + state +
                '}';
    }
}

