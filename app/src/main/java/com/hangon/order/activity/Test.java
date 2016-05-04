package com.hangon.order.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fd on 2016/5/2.
 */
public class Test extends Activity {
    List<PersonalInformationData> list;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaaaaaa);
        list=new ArrayList<>();
        getData();
        t1=(TextView)findViewById(R.id.te1);
       // t1.setText(list.get(0).getName());
        Button b1=(Button)findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t1.setText(list.get(0).getName()+list.get(0).getPhoneNum()+list.get(0).getCarLicenceTail()+list.get(0).getProvinceIndex()+list.get(0).getState());
            }
        });
    }

    private void getData() {
        String url = Constants.GET_CAR_INFO_URL;
        VolleyRequest.RequestGet( Test .this, url, "getData", new VolleyInterface( Test .this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                Log.e("aaaa", result);
                list = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
                Log.e("aaa", list.get(0).getName() + list.get(0).getName());
            }
            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }
}
