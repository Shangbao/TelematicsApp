package com.hangon.common;

/**
 * Created by Administrator on 2016/4/2.
 */
public class Constants {
    //网络请求URL
    //10.163.0.194   10.58.11.204
    public static final String HOST_IP="10.163.0.5";
    public static final  String LOGIN_URL="http://"+HOST_IP+":8080/wind/UserLogin?";
    public static final  String REGISTER_URL="http://"+HOST_IP+":8080/wind/UserRegister?";
   public static final  String JUDGE_USER_URL="http://"+HOST_IP+":8080/wind/UserJudge?";

    //音乐播放状态
    public static final int IDLE = 0;// 空闲状态
    public static final int PLAY = 1;//播放状态
    public static final int PAUSE = 2;//暂停状态
    public static final int STOP = 3;//停止状态

    //音乐播放模式
    public  static  final  int SEQUENCE_MODEL=0;//顺序播放
    public  static  final  int CIRCULATION_MODEL=1;//循环播放
    public static  final  int RANDOM_MODEL=2;//随机播放
}
