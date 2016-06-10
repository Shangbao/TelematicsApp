package com.hangon.bean.user;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private int userId;//用户id
    private String userName;//用户名
    private String userPass;//密码
    private String nickname;//昵称
    private String sex;//性别
    private int age;//年龄
    private String driverNum;//驾驶证号
    private String userIconContent;//图片的二进制码
    private boolean isSave;//是否保存
    private String userIconUrl;//用户头像对应的地址

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getUserIconContent() {
        return userIconContent;
    }

    public void setUserIconContent(String userIconContent) {
        this.userIconContent = userIconContent;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDriverNum() {
        return driverNum;
    }

    public void setDriverNum(String driverNum) {
        this.driverNum = driverNum;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }
}
