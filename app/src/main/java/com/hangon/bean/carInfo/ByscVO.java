package com.hangon.bean.carInfo;


/**
 * Created by Administrator on 2016/8/10.
 */
public class ByscVO {
    private int byscNum;
    private String byxmName;//保养项目名称
    private int byxmState;//保养项目状态

    public ByscVO() {
    }

    public ByscVO(int byscNum, String byxmName, int byxmState) {
        this.byscNum = byscNum;
        this.byxmName = byxmName;
        this.byxmState = byxmState;
    }

    public String getByxmName() {
        return byxmName;
    }

    public void setByxmName(String byxmName) {
        this.byxmName = byxmName;
    }

    public int getByxmState() {
        return byxmState;
    }

    public void setByxmState(int byxmState) {
        this.byxmState = byxmState;
    }

    public int getByscNum() {
        return byscNum;
    }

    public void setByscNum(int byscNum) {
        this.byscNum = byscNum;
    }

    @Override
    public String toString() {
        return "ByscVO{" +
                "byscNum=" + byscNum +
                ", byxmName='" + byxmName + '\'' +
                ", byxmState=" + byxmState +
                '}';
    }
}

