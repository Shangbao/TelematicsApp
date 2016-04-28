package com.hangon.bean.carInfo;

/**
 * Created by Administrator on 2016/4/28.
 */
public class YcglVO {
    private double mileage;
    private int oddGasAmount;
    private double yjNum;
    private double bsNum;
    private double cdNum;

    public YcglVO() {
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

    public double getYjNum() {
        return yjNum;
    }

    public void setYjNum(double yjNum) {
        this.yjNum = yjNum;
    }

    public double getBsNum() {
        return bsNum;
    }

    public void setBsNum(double bsNum) {
        this.bsNum = bsNum;
    }

    public double getCdNum() {
        return cdNum;
    }

    public void setCdNum(double cdNum) {
        this.cdNum = cdNum;
    }

    @Override
    public String toString() {
        return "YcglVO{" +
                "mileage=" + mileage +
                ", oddGasAmount=" + oddGasAmount +
                ", yjNum=" + yjNum +
                ", bsNum=" + bsNum +
                ", cdNum=" + cdNum +
                '}';
    }
}
