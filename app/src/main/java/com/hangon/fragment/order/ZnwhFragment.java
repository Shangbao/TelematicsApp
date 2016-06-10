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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.common.Topbar;
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
    private Button btnSaying;
    private RelativeLayout layout;

    ImageButton topbarLeft, topbarRight;
    TextView topbarTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        znwhView = inflater.inflate(R.layout.fragment_znwh, container, false);
//        Bundle bundle = getArguments();
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
        layout= (RelativeLayout) znwhView.findViewById(R.id.btn_saying);

        topbarLeft= (ImageButton) znwhView.findViewById(R.id.topbar_left);
        topbarRight= (ImageButton) znwhView.findViewById(R.id.topbar_right);
        topbarTitle= (TextView) znwhView.findViewById(R.id.topbar_title);
        topbarLeft.setVisibility(View.GONE);
        topbarRight.setVisibility(View.GONE);
        topbarTitle.setText("智能生活");
        layout.setOnClickListener(this);
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
                startActivity(intent);
                getActivity().finish();
        }
    }

    private void registerReceiver(){
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ZnwhService.ACTION_UPDATE_ZNWH);
        intentFilter.addAction(WeatherService.ACTION_UPDATE_CLEARCAR);
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
            }
            else if (WeatherService.ACTION_UPDATE_CLEARCAR.equals(action)){
                tvClearcar.setText(intent.getStringExtra(WeatherService.ACTION_UPDATE_CLEARCAR));
            }
        }
    }
}
