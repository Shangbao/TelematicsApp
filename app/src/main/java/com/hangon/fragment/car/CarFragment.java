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
import com.hangon.common.Topbar;
import com.hangon.map.activity.BestRouteActivity;
import com.hangon.map.activity.GasStationActivity;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.map.util.JudgeNet;
import com.hangon.map.util.NetReceiver;
import com.hangon.outer.zxing.CaptureActivity;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CarFragment extends Fragment implements View.OnClickListener {
    View  carView;

    Topbar carTopbar;//标题栏
    Button carManageSao;//扫一扫添加车辆
    Button btnShouAdd;//手动添加
    Button btnSetCarInfo;//车辆信息管理
    Button btnBestWay;//最优路线
    Button btnGasStation;//周围加油站
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
        carManageSao= (Button) carView.findViewById(R.id.carManageSao);
        btnShouAdd= (Button) carView.findViewById(R.id.btnShouAdd);
        btnSetCarInfo= (Button) carView.findViewById(R.id.btnSetCarInfo);
        btnBestWay= (Button) carView.findViewById(R.id.btnBestWay);
        btnGasStation= (Button) carView.findViewById(R.id.btnGasStation);
        btnGasStation.setOnClickListener(this);
        btnBestWay.setOnClickListener(this);
        btnSetCarInfo.setOnClickListener(this);
        carManageSao.setOnClickListener(this);
        btnShouAdd.setOnClickListener(this);
        mReceiver=new NetReceiver();//网络接受
        mFilter=new IntentFilter();
        judgeNet=new JudgeNet();
        carTopbar= (Topbar) carView.findViewById(R.id.carTopbar);
        carTopbar.setBtnIsVisible(false);
    }

    @Override
    //点击事件
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.carManageSao:
                intent=new Intent();
                intent.setClass(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;

            case R.id.btnShouAdd:
                intent=new Intent();
                intent.setClass(getActivity(), AddCarMessageActivity.class);
                startActivity(intent);
                break;

            case R.id.btnSetCarInfo:
                intent=new Intent();
                intent.setClass(getActivity(),AddCarMessageActivity.class);
                startActivity(intent);
                break;

            case R.id.btnBestWay:
                judgeNetState=mReceiver.getNetType();
                intent=new Intent();
                intent.setClass(getActivity(), BestRouteActivity.class);
                startActivity(intent);
                break;

            case R.id.btnGasStation:
                judgeNetState=mReceiver.getNetType();
                if(judgeNetState.equals("mobilenet")||judgeNetState.equals("wifinet")){
                    judgeNet.setStates(2);
                    Log.e("bbb",judgeNet.getStates()+"");
                    intent=new Intent();
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
    }
}
