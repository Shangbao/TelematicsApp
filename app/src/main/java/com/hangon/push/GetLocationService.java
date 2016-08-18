package com.hangon.push;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hangon.common.Constants;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chuan on 2016/8/18.
 */
public class GetLocationService extends Service {

    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 1000 * 60 * 5;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationClient = ((com.hangon.saying.util.Location) getApplication()).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double lat = bdLocation.getLatitude();
                double lon = bdLocation.getLongitude();
                sendLocation(lat, lon);
            }
        });
        locationClient.start();
        locationClient.requestLocation();
    }

    private void sendLocation(double lat, double lon){
        UserUtil.instance(GetLocationService.this);
        String userId= UserUtil.getInstance().getIntegerConfig("userId")+"";
        Log.e("userId", userId);
        String url= Constants.SEND_JWD;
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userLat", lat + "");
        map.put("userLng", lon + "");
        VolleyRequest.RequestPost(this, url, "sendLocation", map, new VolleyInterface(this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.d("位置", "onMySuccess: ");
            }

            @Override
            public void onMyError(VolleyError error) {
                Log.d("位置", "onMyError: ");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()){
            locationClient.stop();
            locationClient = null;
        }
    }
}
