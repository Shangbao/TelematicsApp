package com.hangon.fragment.order;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.common.Topbar;
import com.hangon.fragment.userinfo.UserIconActivity;
import com.hangon.home.activity.HomeActivity;
import com.hangon.maintenace.CarMiantenance;
import com.hangon.saying.viewPager.MainActivity;
import com.hangon.weather.WeatherService;

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
    private ImageView carRepair;
    private ImageView carWash;
    ImageView topbarLeft, topbarRight;
    TextView topbarTitle;
    private ImageView btnByxq;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        znwhView = inflater.inflate(R.layout.fragment_znwh, container, false);
        init();
        registerReceiver();
        return znwhView;
    }

    public void init() {
        carRepair=(ImageView)znwhView.findViewById(R.id.img2);
        carWash=(ImageView)znwhView.findViewById(R.id.img3);
        carWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CarMiantenance.class);
                intent.putExtra("ids","a");
                startActivityForResult(intent, HomeActivity.INTENT_SAYING);
            }
        });
        carRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CarMiantenance.class);
                intent.putExtra("ids","b");
                startActivityForResult(intent, HomeActivity.INTENT_SAYING);
            }
        });
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
        btnByxq= (ImageView) znwhView.findViewById(R.id.btn_byxq);
        topbarLeft= (ImageView) znwhView.findViewById(R.id.topbar_left);
        topbarRight= (ImageView) znwhView.findViewById(R.id.topbar_right);
        topbarTitle= (TextView) znwhView.findViewById(R.id.topbar_title);

        topbarLeft.setVisibility(View.GONE);
        topbarRight.setVisibility(View.GONE);
        topbarTitle.setText("智能生活");
        layout.setOnClickListener(this);
        btnByxq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_saying:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                startActivityForResult(intent, HomeActivity.INTENT_SAYING);
                break;
            case R.id.btn_byxq:
                intent=new Intent(getActivity(), ByscActivity.class);
                startActivityForResult(intent, HomeActivity.INTENT_SAYING);
                break;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
