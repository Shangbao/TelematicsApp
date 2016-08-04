package com.hangon.saying.util;

import com.baidu.location.*;
import com.baidu.mapapi.model.LatLng;
import com.hangon.common.MyApplication;

import android.widget.TextView;

public class Location extends MyApplication {

    public LocationClient mLocationClient = null;
    private String mData;
    public MyLocationListenner myListener = new MyLocationListenner();
    public TextView mTv;
    public TextView lat;
    public  TextView lon;

    @Override
    public void onCreate() {
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        super.onCreate();
    }
    public void logLocation(double lats,double lons){
       try{
           if(lat!=null){
           lat.setText(lats+"");
           }
           if(lon!=null){
               lon.setText(lons+"");
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    public void logMsg(String str) {
        try {
            mData = str;
            if (mTv != null)
                mTv.setText(mData);
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            StringBuffer sb = new StringBuffer(256);
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append(location.getAddrStr());
            }

            logMsg(sb.toString());
            logLocation(location.getLatitude(),location.getLongitude());

        }

    }
}