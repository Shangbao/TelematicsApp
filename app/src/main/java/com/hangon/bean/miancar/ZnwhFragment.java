package com.hangon.bean.miancar;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.hangon.bean.map.Status;
import com.hangon.common.MyApplication;
import com.hangon.common.MyStringRequest;
import com.hangon.common.Topbar;
import com.hangon.fragment.order.ZnwhInfoVO;
import com.hangon.fragment.order.ZnwhService;
import com.hangon.fragment.userinfo.UserIconActivity;
import com.hangon.home.activity.HomeActivity;
import com.hangon.map.util.JudgeNet;
import com.hangon.saying.viewPager.MainActivity;
import com.hangon.weather.WeatherService;

import java.util.Collections;

/**
 * Created by Administrator on 2016/4/4.
 */
public class ZnwhFragment extends Fragment implements View.OnClickListener {

    View znwhView;

    private MyReceiver receiver = null;

    private TextView tvUserId;
    private TextView tvMil;
    private TextView tvGas;
    private TextView tvIsEng;
    private TextView tvIsTran;
    private TextView tvIsLight;
    private TextView tvClearcar;
    private TextView tvAir;
    private TextView tvExe;
    private Button btnSaying;
    private RelativeLayout layout;

    ImageButton topbarLeft, topbarRight;
    TextView topbarTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        znwhView = inflater.inflate(R.layout.fragment_znwh, container, false);
        getJsonDataToXc();

        init();
        registerReceiver();
        return znwhView;
    }

    public void init() {
        tvUserId = (TextView) znwhView.findViewById(R.id.znwh_userid);
        tvMil = (TextView) znwhView.findViewById(R.id.znwh_mil);
        tvGas = (TextView) znwhView.findViewById(R.id.znwh_gas);
        tvIsEng = (TextView) znwhView.findViewById(R.id.znwh_iseng);
        tvIsTran = (TextView) znwhView.findViewById(R.id.znwh_istran);
        tvIsLight = (TextView) znwhView.findViewById(R.id.znwh_islight);
        tvClearcar = (TextView) znwhView.findViewById(R.id.znwh_clear);
        tvAir = (TextView) znwhView.findViewById(R.id.znwh_kqzl);
        tvExe = (TextView) znwhView.findViewById(R.id.znwh_ydzs);
        layout= (RelativeLayout) znwhView.findViewById(R.id.btn_saying);

        topbarLeft= (ImageButton) znwhView.findViewById(R.id.topbar_left);
        topbarRight= (ImageButton) znwhView.findViewById(R.id.topbar_right);
        topbarTitle= (TextView) znwhView.findViewById(R.id.topbar_title);
        topbarLeft.setVisibility(View.GONE);
        topbarRight.setVisibility(View.GONE);
        topbarTitle.setText("智能生活");
        layout.setOnClickListener(this);
    }

    // 测试获得洗车店维修点的Json数据

    public  void getJsonDataToXc() {
        try {
            String url="http://api.map.baidu.com/place/v2/search?query=%E6%B4%97%E8%BD%A6%E5%BA%97%E5%BA%97&page_size=10&page_num=0&scope=1&location=39.915,116.404&radius=2000&output=json&ak=8ZcbE4SeBsjWyfulkiqswRaHOm1mFZV8&mcode=84:62:7A:13:86:10:06:F6:77:86:66:B5:46:E6:58:B1:A7:F4:85:BB;baidumapsdk.demo";
            MyStringRequest request = new MyStringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.e("aaa", response);
                            if (response == null || response.isEmpty()) {
                                return;
                            } else {
                                Gson gson = new Gson();
                                data data = gson.fromJson(response, data.class);
                                Log.e("xxx",data.getStatus()+"");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity(),"网络错误", Toast.LENGTH_SHORT).show();
                }
            });
            request.setTag("StringReqGet");
            MyApplication.getHttpQueues().add(request);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_saying:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                startActivityForResult(intent, HomeActivity.INTENT_SAYING);
        }
    }

    private void registerReceiver(){
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ZnwhService.ACTION_UPDATE_ZNWH);
        intentFilter.addAction(WeatherService.ACTION_UPDATE_CLEARCAR);
        intentFilter.addAction(WeatherService.ACTION_UPDATE_AIR);
        intentFilter.addAction(WeatherService.ACTION_UPDATE_EXE);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ZnwhService.ACTION_UPDATE_ZNWH.equals(action)){
                Bundle bundle = intent.getExtras();
                ZnwhInfoVO znwhInfoVO = (ZnwhInfoVO) bundle.getSerializable("ZnwhInfo");
                //tvUserId.setText(znwhInfoVO.getUserId()+"");
                tvMil.setText(znwhInfoVO.getMileage() + "");
                tvGas.setText(znwhInfoVO.getOddGasAmount() + "");
                if (znwhInfoVO.getIsGoodEngine() == 1) {
                    tvIsEng.setText("异常");
                } else {
                    tvIsEng.setText("正常");
                }
                if (znwhInfoVO.getIsGoodTran() == 1) {
                    tvIsTran.setText("异常");
                } else {
                    tvIsTran.setText("正常");
                }
                if (znwhInfoVO.getIsGoodLight() == 1) {
                    tvIsLight.setText("异常");
                } else {
                    tvIsLight.setText("正常");
                }
            } else if (WeatherService.ACTION_UPDATE_CLEARCAR.equals(action)){
                tvClearcar.setText(intent.getStringExtra(WeatherService.ACTION_UPDATE_CLEARCAR));
            } else if (WeatherService.ACTION_UPDATE_AIR.equals(action)){
                tvAir.setText(intent.getStringExtra(WeatherService.ACTION_UPDATE_AIR));
            } else if (WeatherService.ACTION_UPDATE_EXE.equals(action)){
                tvExe.setText(intent.getStringExtra(WeatherService.ACTION_UPDATE_EXE));
            }
        }
    }
}
