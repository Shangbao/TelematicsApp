package com.hangon.bean.carInfo;

/**
 * Created by Administrator on 2016/4/27.
 */
public class CarInfoVO {
    private String name;
    private String phoneNum;
    private String plateNum;
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

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    @Override
    public String toString() {
        return "CarInfoVO{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", plateNum='" + plateNum + '\'' +
                ", state=" + state +
                '}';
    }
}
