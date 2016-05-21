package com.hangon.fragment.order;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chuan on 2016/5/9.
 */
public class Znwh extends Activity implements Runnable {
    @Override
    public void run() {
        getYcMessage();
    }

    public void getYcMessage(){
        String url = ""+ "?userId=" +1;
        VolleyRequest.RequestGet(Znwh.this, url, "getYcMessage", new VolleyInterface(Znwh.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.d("res",result);
            }

            @Override
            public void onMyError(VolleyError error) {
                Log.d("tag", "onMyError: ");
            }
        });
    }
}
