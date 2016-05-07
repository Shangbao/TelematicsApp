package com.hangon.common;

/**
 * Created by Administrator on 2016/4/2.
 */
public class Constants {
    //网络请求URL
    //10.163.0.194   10.58.11.204
    public static final String HOST_IP="10.163.0.5";
    //关于用户信息的网络请求地址
    public static final  String LOGIN_URL="http://"+HOST_IP+":8080/wind/UserLogin?";
    public static final  String REGISTER_URL="http://"+HOST_IP+":8080/wind/UserRegister?";
    public static final  String JUDGE_USER_URL="http://"+HOST_IP+":8080/wind/UserJudge?";
    public static final  String  UPDATE_USER_URL="http://"+HOST_IP+":8080/wind/UpdateUserInfo?";
    public  static final String ADD_USER_ICON_URL="http://"+HOST_IP+":8080/wind/AddUserIcon?";

    //关于车辆信息的网络请求地址
    public static final String GET_BRAND_INFO_URL="http://"+HOST_IP+":8080/wind/Car/BrandInfo";
    public static final String GET_CAR_INFO_URL="http://"+HOST_IP+":8080/wind/Car/CarInfo";
    public static final String ADD_CAR_INFO_URL="http://"+HOST_IP+":8080/wind/Car/AddCarInfo";
    public static final String UPDATE_CAR_INFO_URL="http://"+HOST_IP+":8080/wind/Car/UpdateCarInfo";
    public static final String DELETE_CAR_INFO_URL="http://"+HOST_IP+":8080/wind/Car/DeleteCarInfo";

    //关于订单的网络请求地址
    public static final String ADD_ORDER_INFO_URL="http://"+HOST_IP+":8080/wind/Order/AddOrderInfo";
    public static final String GET_ORDER_INFOS_URL="http://"+HOST_IP+":8080/wind/Order/GetOrderInfos";
    public static final String DELETE_ORDER_INFO_URL="http://"+HOST_IP+":8080/wind/Order/DeleteOrderInfo";
    public static final String CHANGE_ORDER_INFO_URL="http://"+HOST_IP+":8080/wind/Order/ChangeOrderState";
    public static final String GET_WZF_ORDER_INFOS_URL="http://"+HOST_IP+":8080/wind/Order/GetWzfOrderInfos";
    public static final String GET_YZF_ORDER_INFOS_URL="http://"+HOST_IP+":8080/wind/Order/GetYzfOrderInfos";

    //音乐播放状态
    public static final int IDLE = 0;// 空闲状态
    public static final int PLAY = 1;//播放状态
    public static final int PAUSE = 2;//暂停状态
    public static final int STOP = 3;//停止状态

    //音乐播放模式
    public  static  final  int SEQUENCE_MODEL=0;//顺序播放
    public  static  final  int CIRCULATION_MODEL=1;//循环播放
    public static  final  int RANDOM_MODEL=2;//随机播放

    //车牌号
    public static final String PROVINCE_VALUE="京津沪川鄂甘赣桂贵黑吉翼晋辽鲁蒙闽宁青琼陕苏皖湘新渝豫粤云藏浙";

    //动画参数
    public static final int VERSION=Integer.valueOf(android.os.Build.VERSION.SDK);

}
