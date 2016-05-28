package com.hangon.fragment.car;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.carInfoManage.activity.AddCarMessageActivity;
import com.hangon.carInfoManage.activity.SetCarInfoActivity;
import com.hangon.common.Topbar;
import com.hangon.fragment.music.MusicService;
import com.hangon.map.activity.BestRouteActivity;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.map.util.JudgeNet;
import com.hangon.map.util.NetReceiver;
import com.hangon.weather.WeatherActivity;
import com.hangon.weather.WeatherService;
import com.hangon.weizhang.activity.MainActivity;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.Weather;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CarFragment extends Fragment implements View.OnClickListener {
        View  carView;

        Topbar carTopbar;//标题栏
        LinearLayout btnSstq;//扫一扫实时天气
        Button btnSetCarInfo;//车辆信息管理
        Button btnBestWay;//最优路线
        Button btnWeizhang;//违章查询
        Button btnYyjy;//预约加油按钮

        TextView tvWeather;
        TextView tvCity;

        private String ip;


        Intent intent;//用于跳转

        JudgeNet judgeNet;//判断网络状态
        String judgeNetState;//判断网络是否可用参数
        /**
         * 判断网络是否可用
         */
        NetReceiver mReceiver ;
        IntentFilter mFilter;

        private ProgressReceiver progressReceiver;
        private String city,weather;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            carView=inflater.inflate(R.layout.ftagment_car,container,false);
            init();
            mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            getActivity().registerReceiver(mReceiver, mFilter);
            registerReceiver();
            return  carView;
        }

        //初始化组件与实例化
        public void init(){
            //实例化
            btnSstq= (LinearLayout) carView.findViewById(R.id.btnSstq);
            btnSetCarInfo= (Button) carView.findViewById(R.id.btnSetCarInfo);
            btnBestWay= (Button) carView.findViewById(R.id.btnBestWay);
            btnWeizhang= (Button) carView.findViewById(R.id.btnWeiZhang);
            btnYyjy= (Button) carView.findViewById(R.id.btnYyjy);
            tvWeather = (TextView) carView.findViewById(R.id.weather_text);
            tvCity = (TextView) carView.findViewById(R.id.city_text);

            //设置监听事件
            btnBestWay.setOnClickListener(this);
            btnSetCarInfo.setOnClickListener(this);
            btnSstq.setOnClickListener(this);
            btnWeizhang.setOnClickListener(this);
           btnYyjy.setOnClickListener(this);

            mReceiver=new NetReceiver();//网络接受
            mFilter=new IntentFilter();
            judgeNet=new JudgeNet();

            //标题栏
            carTopbar= (Topbar) carView.findViewById(R.id.carTopbar);
            carTopbar.setBtnIsVisible(false);
        }

        @Override
        //点击事件
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSstq:
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WeatherActivity.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                    break;

                case R.id.btnSetCarInfo:
                    intent = new Intent();
                    intent.setClass(getActivity(), SetCarInfoActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnBestWay:
                    judgeNetState = mReceiver.getNetType();
                    intent = new Intent();
                    intent.setClass(getActivity(), BestRouteActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnYyjy:
                    judgeNetState = mReceiver.getNetType();
                    if (judgeNetState.equals("mobilenet") || judgeNetState.equals("wifinet")) {
                        judgeNet.setStates(2);
                        Log.e("bbb", judgeNet.getStates() + "");
                        intent = new Intent();
                        intent.setClass(getActivity(), MapMainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            getActivity().unregisterReceiver(mReceiver);
            getActivity().unregisterReceiver(progressReceiver);
        }

    private void registerReceiver(){
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherService.ACTION_UPDATE_WEATHER);
        intentFilter.addAction(WeatherService.ACTION_UPDATE_CITY);
        getActivity().registerReceiver(progressReceiver, intentFilter);
    }

    class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(WeatherService.ACTION_UPDATE_WEATHER.equals(action)){
                weather = intent.getStringExtra(WeatherService.ACTION_UPDATE_WEATHER);
                tvWeather.setText(weather);
            }else if(WeatherService.ACTION_UPDATE_CITY.equals(action)){
                //Retrive the current music and get the title to show on top of the screen.
                city = intent.getStringExtra(WeatherService.ACTION_UPDATE_CITY);
                tvCity.setText(city);
            }
        }
    }
}
