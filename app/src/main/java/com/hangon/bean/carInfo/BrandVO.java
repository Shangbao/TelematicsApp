package com.hangon.bean.carInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/4/30.
 */
public class BrandVO {
    private int id;
    private String brand;
    private String carFlag;
    private List<BrandTypeVO> brandTypeList;

    public BrandVO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarFlag() {
        return carFlag;
    }

    public void setCarFlag(String carFlag) {
        this.carFlag = carFlag;
    }

    public List<BrandTypeVO> getBrandTypeList() {
        return brandTypeList;
    }

    public void setBrandTypeList(List<BrandTypeVO> brandTypeList) {
        this.brandTypeList = brandTypeList;
    }

    @Override
    public String toString() {
        return "BrandVO{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", carFlag='" + carFlag + '\'' +
                ", brandTypeList=" + brandTypeList +
                '}';
    }
}
