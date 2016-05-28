package com.hangon.weather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

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

/**
 * Created by Chuan on 2016/5/19.
 */
public class WeatherService extends Service implements APICallback {

    private String ip;
    private ArrayList<HashMap<String, Object>> results;
    private String city;
    private String temperature;
    private boolean flag = true;

    private static final int updateWeather = 1;
    private static final int updateCity = 2;

    public static final String ACTION_UPDATE_WEATHER = "com.hangon.weather.UPDATE_WEATHER";
    public static final String ACTION_UPDATE_CITY = "com.hangon.weather.UPDATE_CITY";

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case updateWeather:
                    updateWeahther();
                    break;
                case updateCity:
                    updateCity();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        MobAPI.initSDK(this, "120b650027878");
        com.mob.mobapi.apis.Weather api = (com.mob.mobapi.apis.Weather) MobAPI.getAPI(com.mob.mobapi.apis.Weather.NAME);
        api.getSupportedCities(this);
        new Thread() {
            public void run() {
                while (flag) {
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
                        Thread.sleep(1000 * 60 * 5);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void onWeatherDetailsGot(Map<String, Object> result) {
        results = (ArrayList<HashMap<String, Object>>) result.get("result");
        HashMap<String, Object> weather = results.get(0);
        city = com.mob.tools.utils.R.toString(weather.get("city"));
        temperature = com.mob.tools.utils.R.toString(weather.get("temperature"));
        handler.sendEmptyMessage(updateWeather);
        handler.sendEmptyMessage(updateCity);
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
}
