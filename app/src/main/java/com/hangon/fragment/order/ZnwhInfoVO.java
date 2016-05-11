package com.hangon.fragment.order;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/3.
 */
public class ZnwhInfoVO implements Serializable {
    private int userId;//用户
    private double mileage;//所行驶的公里数
    private int oddGasAmount;//剩余的汽油百分比
    private int isGoodEngine;//引擎状况
    private int isGoodTran;//变速器状况
    private int isGoodLight;//车灯状况

    public ZnwhInfoVO() {

    }

    public ZnwhInfoVO(int userId, double mileage, int oddGasAmount, int isGoodEngine, int isGoodTran, int isGoodLight) {
        this.userId = userId;
        this.mileage = mileage;
        this.oddGasAmount = oddGasAmount;
        this.isGoodEngine = isGoodEngine;
        this.isGoodTran = isGoodTran;
        this.isGoodLight = isGoodLight;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public int getOddGasAmount() {
        return oddGasAmount;
    }

    public void setOddGasAmount(int oddGasAmount) {
        this.oddGasAmount = oddGasAmount;
    }

    public int getIsGoodEngine() {
        return isGoodEngine;
    }

    public void setIsGoodEngine(int isGoodEngine) {
        this.isGoodEngine = isGoodEngine;
    }

    public int getIsGoodTran() {
        return isGoodTran;
    }

    public void setIsGoodTran(int isGoodTran) {
        this.isGoodTran = isGoodTran;
    }

    public int getIsGoodLight() {
        return isGoodLight;
    }

    public void setIsGoodLight(int isGoodLight) {
        this.isGoodLight = isGoodLight;
    }

    @Override
    public String toString() {
        return "ZnwhInfoVO{" +
                "userId=" + userId +
                ", mileage=" + mileage +
                ", oddGasAmount=" + oddGasAmount +
                ", isGoodEngine=" + isGoodEngine +
                ", isGoodTran=" + isGoodTran +
                ", isGoodLight=" + isGoodLight +
                '}';
    }
}
