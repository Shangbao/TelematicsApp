package com.hangon.push;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hangon.common.Constants;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chuan on 2016/8/4.
 */
public class QzLocationService extends Service {

    private LocationManager locationManager;
    private String locationProvider;
    private Location location;

    private Double lat;
    private Double lon;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            //更新第一次地址
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
        } catch (SecurityException e){
            e.printStackTrace();
        }
        List<String> providers = locationManager.getProviders(true);
        Toast.makeText(QzLocationService.this, "位置后台打开！", Toast.LENGTH_SHORT).show();
        Toast.makeText(QzLocationService.this, providers.toString(), Toast.LENGTH_SHORT).show();
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            locationProvider = LocationManager.PASSIVE_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Location
        try {
            location = locationManager.getLastKnownLocation(locationProvider);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (null != location){
            sendLocation(location);
        }
        //监视地理位置变化
        try {
            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void sendLocation(Location location){
        UserUtil.instance(QzLocationService.this);
        String userId= UserUtil.getInstance().getIntegerConfig("userId")+"";
        Log.e("userId",userId);
        String url= Constants.SEND_JWD;
        lat = location.getLatitude();
        lon = location.getLongitude();
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("userLat", lat+"");
        map.put("userLng", lon + "");
        Toast.makeText(QzLocationService.this, "发送位置", Toast.LENGTH_SHORT).show();
        Toast.makeText(QzLocationService.this, "lat："+lat+"lon："+lon, Toast.LENGTH_SHORT).show();
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
    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            sendLocation(location);
        }
    };

    @Override
    public void onDestroy() {
        if(locationManager!=null){
            //移除监听器
            try {
                locationManager.removeUpdates(locationListener);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }
}
