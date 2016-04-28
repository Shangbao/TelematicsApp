package com.hangon.bean.carInfo;

/**
 * Created by Administrator on 2016/4/26.
 */
public class CarMessageVO {
    private  int carInfoId;//车信息id

    private String brand;//车品牌
    private String brandType;//车品牌类型

    private String carFlag;//车标志图片
    private int doorCount;//门的数量
    private int seatCount;//座位的数量

    private String  chassisNum;//车架号
    private String engineNum;//发动机号

    private int provinceIndex;//车牌省份索引
    private String carLicenceTail;//车牌尾号

    private String name;//车主人姓名
    private String phoneNum;//车主人电话号码

    private double mileage;//已经行驶的公里数
    private int oddGasAmount;//汽油剩余量(百分比)
    private int isGoodEngine;//发动机性能情况
    private int isGoodTran;//变速器性能情况
    private int isGoodLight;//车灯状况

    public int getCarInfoId() {
        return carInfoId;
    }

    public void setCarInfoId(int carInfoId) {
        this.carInfoId = carInfoId;
    }

    public int getIsGoodLight() {
        return isGoodLight;
    }

    public void setIsGoodLight(int isGoodLight) {
        this.isGoodLight = isGoodLight;
    }

    public int getIsGoodTran() {
        return isGoodTran;
    }

    public void setIsGoodTran(int isGoodTran) {
        this.isGoodTran = isGoodTran;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarLicenceTail() {
        return carLicenceTail;
    }

    public void setCarLicenceTail(String carLicenceTail) {
        this.carLicenceTail = carLicenceTail;
    }

    public int getProvinceIndex() {
        return provinceIndex;
    }

    public void setProvinceIndex(int provinceIndex) {
        this.provinceIndex = provinceIndex;
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

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public int getDoorCount() {
        return doorCount;
    }

    public void setDoorCount(int doorCount) {
        this.doorCount = doorCount;
    }

    public String getCarFlag() {
        return carFlag;
    }

    public void setCarFlag(String carFlag) {
        this.carFlag = carFlag;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "CarMessageVO{" +
                "carInfoId=" + carInfoId +
                ", brand='" + brand + '\'' +
                ", brandType='" + brandType + '\'' +
                ", carFlag='" + carFlag + '\'' +
                ", doorCount=" + doorCount +
                ", seatCount=" + seatCount +
                ", chassisNum='" + chassisNum + '\'' +
                ", engineNum='" + engineNum + '\'' +
                ", provinceIndex=" + provinceIndex +
                ", carLicenceTail='" + carLicenceTail + '\'' +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", mileage=" + mileage +
                ", oddGasAmount=" + oddGasAmount +
                ", isGoodEngine=" + isGoodEngine +
                ", isGoodTran=" + isGoodTran +
                ", isGoodLight=" + isGoodLight +
                '}';
    }
}
