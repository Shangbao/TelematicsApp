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
    //判断选择个人信息返回参数值
    private static  int personalInformation=0;
    //判断是否是从预约加油页面传过去的数据
    private static  int appointOrderData=0;
    //判断从预约加油到线路规划
    private static int appointRoute=0;
    public int getAppointRoute() {
        return appointRoute;
    }

    public void setAppointRoute(int appointOrderData) {
        JudgeNet.appointRoute= appointOrderData;
    }
    public int getAppointOrderData() {
        return appointOrderData;
    }

    public void setAppointOrderData(int appointOrderData) {
        JudgeNet.appointOrderData = appointOrderData;
    }

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

    public  int getPersonalInformation() {
        return personalInformation;
    }
    public void setPersonalInformation(int personalInformation) {
        JudgeNet.personalInformation = personalInformation;
    }

    public static double getLon() {
        return lon;
    }

    public static void setLon(double lon) {
        JudgeNet.lon = lon;
    }
}
