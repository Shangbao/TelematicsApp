package com.hangon.fragment.order;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fd.ourapplication.R;
import com.hangon.common.Topbar;
import com.hangon.order.activity.OrderMain;

/**
 * Created by Administrator on 2016/4/4.
 */
public class OrderFragment extends Fragment {
    View orderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orderView=inflater.inflate(R.layout.fragment_order,container,false);
        Topbar topbar= (Topbar) orderView.findViewById(R.id.topbar);
        topbar.setBtnIsVisible(false);
        return  orderView;
    }
}
