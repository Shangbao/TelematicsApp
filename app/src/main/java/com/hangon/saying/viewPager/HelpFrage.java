package com.hangon.saying.viewPager;

import com.example.fd.ourapplication.R;
import com.hangon.order.util.BaseFragmentPagerAdapter;
import com.hangon.saying.view.XListView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpFrage extends Fragment implements BaseFragmentPagerAdapter.UpdateAble, XListView.IXListViewListener {
    private SensorManager sensorManager;
    private Vibrator vibrator;
    AlertDialog.Builder builder;
    private static final String TAG = "TestSensorActivity";
    private static final int SENSOR_SHAKE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carlife_help_fra, null);

        return view;
    }

    @Override
    public void update() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
