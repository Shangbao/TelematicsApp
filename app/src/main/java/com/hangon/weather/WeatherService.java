package com.hangon.weather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.fragment.order.NotificationAdmain;
import com.hangon.home.activity.HomeActivity;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.*;
import com.mob.mobapi.apis.Weather;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chuan on 2016/5/19.
 */
public class WeatherService extends Service implements APICallback {

    private String ip;
    private ArrayList<HashMap<String, Object>> results;
    private String city;
    private String temperature;
    private String clearCar;
    private String airCondition;
    private String exerciseIndex;
    private boolean flag = true;
    private boolean isClear = false;

    int smallIcon = R.mipmap.ic_launcher;

    private static final int updateWeather = 1;
    private static final int updateCity = 2;
    private static final int updateClearCar = 3;
    private static final int updateAirCondition = 4;
    private static final int updateExerciseIndex = 5;

    static int NOTIFICATION_ID = 13565400;

    public static final String ACTION_UPDATE_WEATHER = "com.hangon.weather.UPDATE_WEATHER";
    public static final String ACTION_UPDATE_CITY = "com.hangon.weather.UPDATE_CITY";
    public static final String ACTION_UPDATE_CLEARCAR = "com.hangon.weather.UPDATE_CLEARCAR";
    public static final String ACTION_UPDATE_AIR = "com.hangon.weather.UPDATE_AIR";
    public static final String ACTION_UPDATE_EXE = "com.hangon.weather.UPDATE_EXE";

    Intent clearIntent;
    NotificationAdmain admain;
    WeatherBinder binder;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case updateWeather:
                    updateWeahther();
                    break;
                case updateCity:
                    updateCity();
                    break;
                case updateClearCar:
                    updateClearCar();
                    break;
                case updateAirCondition:
                    updateAirCondition();
                    break;
                case updateExerciseIndex:
                    updateExerciseIndex();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new WeatherBinder();
        return binder;
    }

    @Override
    public void onCreate() {
        MobAPI.initSDK(this, "120b650027878");
        com.mob.mobapi.apis.Weather api = (com.mob.mobapi.apis.Weather) MobAPI.getAPI(com.mob.mobapi.apis.Weather.NAME);
        api.getSupportedCities(this);
        clearIntent = new Intent(this, HomeActivity.class);
        admain = new NotificationAdmain(this,NOTIFICATION_ID);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ip = null;
                try {
                    NetworkHelper network = new NetworkHelper();
                    ArrayList<KVPair<String>> values = new ArrayList<KVPair<String>>();
                    values.add(new KVPair<String>("ie", "utf-8"));
                    String resp = network.httpGet("http://pv.sohu.com/cityjson", values, null, null);
                    resp = resp.replace("var returnCitySN = {", "{").replace("};", "}");
                    ip = (String) (new Hashon().fromJson(resp).get("cip"));
                    Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
                    api.queryByIPAddress(ip, WeatherService.this);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        },0,1000 * 60 * 5);
    }

    public void onWeatherDetailsGot(Map<String, Object> result) {
        results = (ArrayList<HashMap<String, Object>>) result.get("result");
        HashMap<String, Object> weather = results.get(0);
        city = com.mob.tools.utils.R.toString(weather.get("city"));
        temperature = com.mob.tools.utils.R.toString(weather.get("temperature"));
        clearCar = com.mob.tools.utils.R.toString(weather.get("washIndex"));
        airCondition = com.mob.tools.utils.R.toString(weather.get("airCondition"));
        exerciseIndex = com.mob.tools.utils.R.toString(weather.get("exerciseIndex"));

        handler.sendEmptyMessage(updateWeather);
        handler.sendEmptyMessage(updateCity);
        handler.sendEmptyMessage(updateClearCar);
        handler.sendEmptyMessage(updateAirCondition);
        handler.sendEmptyMessage(updateExerciseIndex);
        if (!isClear && clearCar.equals("适宜")){
            admain.normal_notification(clearIntent,smallIcon,"汽车智能维护","亲，今天的天气适合洗车哦！","洗车提示！");
        }
    }

    public void updateWeahther() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_WEATHER);
        intent.putExtra(ACTION_UPDATE_WEATHER, temperature);
        sendBroadcast(intent);
        handler.sendEmptyMessageDelayed(updateWeather, 1000);
    }

    public void updateCity() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_CITY);
        intent.putExtra(ACTION_UPDATE_CITY, city);
        sendBroadcast(intent);
        handler.sendEmptyMessageDelayed(updateCity, 1000);
    }

    public void updateClearCar(){
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_CLEARCAR);
        intent.putExtra(ACTION_UPDATE_CLEARCAR, clearCar);
        sendBroadcast(intent);
        handler.sendEmptyMessageDelayed(updateClearCar, 1000);
    }

    public void updateAirCondition(){
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_AIR);
        intent.putExtra(ACTION_UPDATE_AIR, airCondition);
        sendBroadcast(intent);
        handler.sendEmptyMessageDelayed(updateAirCondition, 1000);
    }

    public void updateExerciseIndex(){
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_EXE);
        intent.putExtra(ACTION_UPDATE_EXE, exerciseIndex);
        sendBroadcast(intent);
        handler.sendEmptyMessageDelayed(updateAirCondition, 1000);
    }



    @Override
    public void onSuccess(API api, int action, Map<String, Object> result) {
        switch (action) {
            case com.mob.mobapi.apis.Weather.ACTION_IP:
                onWeatherDetailsGot(result);
                break;
        }
    }

    @Override
    public void onError(API api, int i, Throwable details) {
        details.printStackTrace();
        Toast.makeText(this, "亲，查询不到你所要的城市天气！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        isClear = false;
        Log.e("结果:", "stopWeahther");
    }

    public class WeatherBinder extends Binder{
        public void stopWeather(){
            flag = false;
        }

        public void startWeather(){
            flag = true;
        }
    }
}
