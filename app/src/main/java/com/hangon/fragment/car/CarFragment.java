package com.hangon.fragment.car;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.carInfoManage.activity.AddCarMessageActivity;
import com.hangon.carInfoManage.activity.SetCarInfoActivity;
import com.hangon.carInfoManage.activity.WeizhangActivity;
import com.hangon.carInfoManage.activity.YichangActivity;
import com.hangon.common.Topbar;
import com.hangon.map.activity.BestRouteActivity;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.map.util.JudgeNet;
import com.hangon.map.util.NetReceiver;
import com.hangon.order.activity.OrderMain;
import com.hangon.weather.WeatherActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CarFragment extends Fragment implements View.OnClickListener {
        View  carView;

        Topbar carTopbar;//标题栏
        Button btnSstq;//扫一扫实时天气
        Button btnSetCarInfo;//车辆信息管理
        Button btnBestWay;//最优路线
        Button btnGasStation;//周围加油站
        Button btnWeizhang;//违章查询
        Button btnYichang;//异常管理
        Button btnOrder;//订单管理

        Intent intent;//用于跳转

        JudgeNet judgeNet;//判断网络状态
        String judgeNetState;//判断网络是否可用参数
        /**
         * 判断网络是否可用
         */
        NetReceiver mReceiver ;
        IntentFilter mFilter;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            carView=inflater.inflate(R.layout.ftagment_car,container,false);
            init();
            mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            getActivity().registerReceiver(mReceiver, mFilter);
            return  carView;
        }

        //初始化组件与实例化
        public void init(){
            //实例化
            btnSstq= (Button) carView.findViewById(R.id.btnSstq);
            btnSetCarInfo= (Button) carView.findViewById(R.id.btnSetCarInfo);
            btnBestWay= (Button) carView.findViewById(R.id.btnBestWay);
            btnGasStation= (Button) carView.findViewById(R.id.btnGasStation);
            btnWeizhang= (Button) carView.findViewById(R.id.btnWeiZhang);
            //btnYichang= (Button) carView.findViewById(R.id.btnYichang);
            btnOrder= (Button) carView.findViewById(R.id.btnOrder);
            //设置监听事件
            btnGasStation.setOnClickListener(this);
            btnBestWay.setOnClickListener(this);
            btnSetCarInfo.setOnClickListener(this);
            btnSstq.setOnClickListener(this);
            btnWeizhang.setOnClickListener(this);
//            btnYichang.setOnClickListener(this);
            btnOrder.setOnClickListener(this);

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
            switch (v.getId()){
                case R.id.btnSstq:
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WeatherActivity.class);
                    startActivity(intent);
                    break;


                case R.id.btnSetCarInfo:
                    intent=new Intent();
                    intent.setClass(getActivity(),SetCarInfoActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnBestWay:
                    judgeNetState=mReceiver.getNetType();
                    intent=new Intent();
                    intent.setClass(getActivity(), BestRouteActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnWeiZhang:
                    intent=new Intent();
                    intent.setClass(getActivity(), WeizhangActivity.class);
                    startActivity(intent);
                    break;
                /*
                case R.id.btnYichang:
                    intent=new Intent();
                    intent.setClass(getActivity(), YichangActivity.class);
                    startActivity(intent);
                    break;*/

                case R.id.btnGasStation:
                    judgeNetState=mReceiver.getNetType();
                    if(judgeNetState.equals("mobilenet")||judgeNetState.equals("wifinet")){
                        judgeNet.setStates(2);
                        Log.e("bbb", judgeNet.getStates() + "");
                        intent=new Intent();
                        intent.setClass(getActivity(), MapMainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnOrder:
                    intent=new Intent();
                    intent.setClass(getActivity(), OrderMain.class);
                    startActivity(intent);
                break;
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            getActivity().unregisterReceiver(mReceiver);
        }


}
