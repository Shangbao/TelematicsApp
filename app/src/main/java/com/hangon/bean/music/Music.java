package com.hangon.bean.music;

/**
 * Created by Administrator on 2016/4/10.
 */
public class Music {
    private String  number;
    private String title;// 歌名
    private String singer;// 艺术家
    private String url;// 路径
    private long time;// 时间
    private String name;// 歌曲文件名

    public Music() {
        super();
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
