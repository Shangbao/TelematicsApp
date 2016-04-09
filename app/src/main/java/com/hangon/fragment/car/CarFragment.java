package com.hangon.fragment.car;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fd.ourapplication.R;
import com.hangon.carInfoManage.activity.CarManageSaoActivity;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CarFragment extends Fragment implements View.OnClickListener {
    View  carView;
    Button carManageSao;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         carView=inflater.inflate(R.layout.ftagment_car,container,false);
        init();
        return  carView;
    }

    public void init(){
        carManageSao= (Button) carView.findViewById(R.id.carManageSao);
        carManageSao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.carManageSao:
                Intent toSao=new Intent();
                toSao.setClass(getActivity(), CarManageSaoActivity.class);
                startActivity(toSao);
        }
    }
}
