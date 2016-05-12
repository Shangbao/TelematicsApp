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
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.common.Topbar;

/**
 * Created by Administrator on 2016/4/4.
 */
public class ZnwhFragment extends Fragment {

    View znwhView;

    private MyReceiver receiver = null;

    private TextView tvUserId;
    private TextView tvMil;
    private TextView tvGas;
    private TextView tvIsEng;
    private TextView tvIsTran;
    private TextView tvIsLight;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        znwhView=inflater.inflate(R.layout.fragment_znwh,container,false);
//        Bundle bundle = getArguments();
        init();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hangon.fragment.order.ZnwhService");
        getActivity().registerReceiver(receiver, filter);
        Topbar topbar= (Topbar) znwhView.findViewById(R.id.topbar);
        topbar.setBtnIsVisible(false);
        return  znwhView;
    }

    public void init(){
        tvUserId = (TextView) znwhView.findViewById(R.id.znwh_userid);
        tvMil = (TextView) znwhView.findViewById(R.id.znwh_mil);
        tvGas = (TextView) znwhView.findViewById(R.id.znwh_gas);
        tvIsEng = (TextView) znwhView.findViewById(R.id.znwh_iseng);
        tvIsTran = (TextView) znwhView.findViewById(R.id.znwh_istran);
        tvIsLight = (TextView) znwhView.findViewById(R.id.znwh_islight);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            ZnwhInfoVO znwhInfoVO = (ZnwhInfoVO) bundle.getSerializable("ZnwhInfo");
            //tvUserId.setText(znwhInfoVO.getUserId()+"");
            tvMil.setText(znwhInfoVO.getMileage()+"");
            tvGas.setText(znwhInfoVO.getOddGasAmount()+"");
            if (znwhInfoVO.getIsGoodEngine() == 1){
                tvIsEng.setText("异常");
            }else {
                tvIsEng.setText("正常");
            }
            if (znwhInfoVO.getIsGoodTran() == 1){
                tvIsTran.setText("异常");
            }else {
                tvIsTran.setText("正常");
            }
            if (znwhInfoVO.getIsGoodLight() == 1){
                tvIsLight.setText("异常");
            }else {
                tvIsLight.setText("正常");
            }
        }
    }
}
