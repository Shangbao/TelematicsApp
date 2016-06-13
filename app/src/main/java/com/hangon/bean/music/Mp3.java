package com.hangon.bean.music;

public class Mp3 {
    private String number;
    private long time;// 时间
    public int id;
    public int playlistId;//列表id
    public String albumName;//专辑名字
    public String singerName;//歌手名字
    public String url;//sd卡路径
    public String name;//歌曲名字
    public int pictureID;
    public int Duration;
    private long allSongIndex;
    public String picUrl;//图片路径

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSqlId() {
        return id;
    }

    public void setSqlId(int sqlId) {
        this.id = sqlId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAllSongIndex() {
        return allSongIndex;
    }

    public void setAllSongIndex(long allSongIndex) {
        this.allSongIndex = allSongIndex;
    }

    @Override
    public String toString() {
        return "Mp3 [id=" + id + ", playlistId=" + playlistId + ", albumName="
                + albumName + ", singerName=" + singerName + ", url=" + url
                + ", name=" + name + ", pictureID=" + pictureID + ", Duration="
                + Duration + ", allSongIndex=" + allSongIndex + "]";
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }


}
