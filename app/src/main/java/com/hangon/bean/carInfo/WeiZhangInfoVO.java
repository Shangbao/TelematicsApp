package com.hangon.bean.carInfo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/2.
 */
public class WeiZhangInfoVO implements Serializable{
    private String name;//车主姓名
    private int provinceIndex;//车牌索引
    private String carLicenceTail;//车牌尾号
    private String engineNum;//引擎号
    private String chassisNum;//车架号

    public WeiZhangInfoVO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getChassisNum() {
        return chassisNum;
    }

    public void setChassisNum(String chassisNum) {
        this.chassisNum = chassisNum;
    }

    @Override
    public String toString() {
        return "WeiZhangInfoVO{" +
                "name='" + name + '\'' +
                ", provinceIndex=" + provinceIndex +
                ", carLicenceTail='" + carLicenceTail + '\'' +
                ", engineNum='" + engineNum + '\'' +
                ", chassisNum='" + chassisNum + '\'' +
                '}';
    }
}
