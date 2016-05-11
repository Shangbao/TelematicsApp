package com.hangon.fragment.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;

import java.util.List;

/**
 * Created by fd on 2016/5/9.
 */
public class ZnwhAdapter extends ArrayAdapter<ZnwhInfoVO> {
    private int resourceId;

    public ZnwhAdapter(Context context, int textViewResourceId, List<ZnwhInfoVO> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ZnwhInfoVO znwhInfoVO = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tvUserId = (TextView) view.findViewById(R.id.znwh_userid);
        TextView tvMil = (TextView) view.findViewById(R.id.znwh_mil);
        TextView tvGas = (TextView) view.findViewById(R.id.znwh_gas);
        TextView tvIsEng = (TextView) view.findViewById(R.id.znwh_iseng);
        TextView tvIsTran = (TextView) view.findViewById(R.id.znwh_istran);
        TextView tvIsLight = (TextView) view.findViewById(R.id.znwh_islight);
        tvUserId.setText(znwhInfoVO.getUserId());
        tvMil.setText(znwhInfoVO.getMileage()+"");
        tvGas.setText(znwhInfoVO.getOddGasAmount());
        tvIsEng.setText(znwhInfoVO.getIsGoodEngine());
        tvIsTran.setText(znwhInfoVO.getIsGoodTran());
        tvIsLight.setText(znwhInfoVO.getIsGoodLight());
        return view;
    }
}
