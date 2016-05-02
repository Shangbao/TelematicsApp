package com.hangon.bean.carInfo;

/**
 * Created by Administrator on 2016/4/30.
 */
public class BrandTypeVO {
    private  String name;

    public BrandTypeVO() {
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BrandTypeVO{" +
                "name='" + name + '\'' +
                '}';
    }
}
