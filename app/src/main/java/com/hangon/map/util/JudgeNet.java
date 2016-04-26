package com.hangon.map.util;

/**
 * Created by Administrator on 2016/4/23.
 */
public class JudgeNet {
    /**
     * 状态判断
     */
    private static int states=0;
    /**
     * 传输经纬度---路线查询向地图主页
     */
    private static double lat=0.0;
    private static double lon=0.0;

    public static int getStates() {
        return states;
    }

    public static void setStates(int states) {
        JudgeNet.states = states;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        JudgeNet.lat = lat;
    }

    public static double getLon() {
        return lon;
    }

    public static void setLon(double lon) {
        JudgeNet.lon = lon;
    }
}
