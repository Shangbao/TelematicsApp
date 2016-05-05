package com.hangon.fragment.order;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fd.ourapplication.R;
import com.hangon.common.Topbar;

/**
 * Created by Administrator on 2016/4/4.
 */
public class ZnwhFragment extends Fragment {
    View znwhView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        znwhView=inflater.inflate(R.layout.fragment_znwh,container,false);
        Topbar topbar= (Topbar) znwhView.findViewById(R.id.topbar);
        topbar.setBtnIsVisible(false);
        return  znwhView;
    }
}
