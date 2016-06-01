package com.hangon.saying.layout;

import com.baidu.location.*;
import com.hangon.common.MyApplication;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;
import android.os.Process;
import android.os.Vibrator;
import android.widget.Toast;

public class Location extends MyApplication {

	public LocationClient mLocationClient = null;
	private String mData;  
	public MyLocationListenner myListener = new MyLocationListenner();
	public TextView mTv;
	
	@Override
	public void onCreate() {
		mLocationClient = new LocationClient( this );
		mLocationClient.registerLocationListener( myListener );
		super.onCreate(); 
	}
	
	public void logMsg(String str) {
		try {
			mData = str;
			if ( mTv != null )
				mTv.setText(mData);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append(location.getAddrStr());
			}

			logMsg(sb.toString());
		}

	}
}