package com.hangon.bean.carInfo;

/**
 * Created by Administrator on 2016/4/27.
 */
public class CarInfoVO {
    private String name;
    private String phoneNum;
    private int provinceIndex;//车牌省份索引
    private String carLicenceTail;//车牌尾号
    private int state;

    public CarInfoVO() {
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    @Override
    public String toString() {
        return "CarInfoVO{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", provinceIndex=" + provinceIndex +
                ", carLicenceTail='" + carLicenceTail + '\'' +
                ", state=" + state +
                '}';
    }
}
