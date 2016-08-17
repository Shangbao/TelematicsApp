package com.hangon.bean.music;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/4/10.
 */
public class Music {
    private int id;
    private String number;
    private String title;// 歌名
    private String singer;// 艺术家
    private String url;// 路径
    private long time;// 时间
    private String name;// 歌曲文件名
    public Bitmap picture;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Music() {
        super();
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
