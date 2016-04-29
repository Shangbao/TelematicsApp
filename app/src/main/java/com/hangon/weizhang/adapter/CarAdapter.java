package com.hangon.weizhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.weizhang.model.CarInfo;

import java.util.List;

/**
 * Created by Chuan on 2016/4/28.
 */
public class CarAdapter extends ArrayAdapter<CarInfo> {

    private int resourceId;

    public CarAdapter(Context context,int textViewResourceId,List<CarInfo> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarInfo carInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView car = (TextView) view.findViewById(R.id.car_info);
        car.setText(carInfo.getChepai_no());
        return view;
    }
}
