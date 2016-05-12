package com.hangon.fragment.order;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.hangon.common.Constants;
import com.hangon.common.JsonUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.home.activity.HomeActivity;

import java.util.Random;

/**
 * Created by Chuan on 2016/5/9.
 */
public class ZnwhService extends Service {

    NotificationAdmain admain;
    Intent intent;
    ZnwhInfoVO znwhInfoVO;//智能维护信息;
    static int NOTIFICATION_ID = 13565400;

    private Thread thread;

    int smallIcon = R.mipmap.ic_launcher;

    private boolean flag = true;

    private int isGoodEngine, isGoodTran, isGoodLight;
    private int oldEngine = 1, oldTran = 1, oldLight = 1;

    private ZnwhInfoVO znwhInfo = new ZnwhInfoVO();

    @Override
    public void onCreate() {
        super.onCreate();
        admain = new NotificationAdmain(this,NOTIFICATION_ID);
        intent = new Intent(this, HomeActivity.class);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(flag){
                    getZnwhInfo();

                    updateZnwhInfo();
                    try{
                        Thread.sleep(20000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void reflush(ZnwhInfoVO znwhInfoVO){
        isGoodEngine=znwhInfoVO.getIsGoodEngine();
        isGoodLight=znwhInfoVO.getIsGoodLight();
        isGoodTran=znwhInfoVO.getIsGoodTran();
        if (znwhInfoVO.getOddGasAmount()<15){
            admain.normal_notification(intent,smallIcon,"汽车智能维护","你的汽车油量不足15%","请尽快加油");
        }
        if (znwhInfoVO.getMileage()>=1500&&znwhInfoVO.getMileage()%1500>=0&&znwhInfoVO.getMileage()%1500<=100){
            admain.normal_notification(intent,smallIcon,"汽车智能维护","你的里程数为:"+znwhInfoVO.getMileage(),"请及时保养你的汽车");
        }

        if (isGoodEngine != oldEngine){
            if (znwhInfoVO.getIsGoodEngine() == 0){
                admain.normal_notification(intent, smallIcon, "汽车智能维护", "你的汽车需要维修！",
                        "发动机出现异常！");
            }
            else{
                admain.normal_notification(intent, smallIcon, "汽车智能维护", "你的汽车完成维修！",
                        "发动机可以正常运行！");
            }
        }
        if (isGoodTran != oldTran){
            if (isGoodTran == 0){
                admain.normal_notification(intent, smallIcon, "汽车智能维护", "你的汽车需要维修！",
                        "变速器出现异常！");
            }
            else{
                admain.normal_notification(intent, smallIcon, "汽车智能维护", "你的汽车完成维修！",
                        "变速器可以正常运行！");
            }
        }
        if (isGoodLight != oldLight){
            if (isGoodEngine == 0){
                admain.normal_notification(intent, smallIcon, "汽车智能维护", "你的汽车需要维修！",
                        "车灯出现异常！");
            }
            else{
                admain.normal_notification(intent, smallIcon, "汽车智能维护", "你的汽车完成维修！",
                        "车灯可以正常运行！");
            }
        }
        oldEngine = isGoodEngine;
        oldTran = isGoodTran;
        oldLight = isGoodLight;
    }

    public void getZnwhInfo(){
        String url = Constants.GET_ZNWH_INFO_URL.trim()+ "?userId=" +1;
        VolleyRequest.RequestGet(ZnwhService.this, url, "getZnwhInfo", new VolleyInterface(ZnwhService.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.d("res", result);
                Gson gson=new Gson();
                znwhInfoVO= (ZnwhInfoVO) JsonUtil.jsonToBean(result,ZnwhInfoVO.class);
                Log.e("aaa",znwhInfoVO.getMileage()+"");
                reflush(znwhInfoVO);//发送通知
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ZnwhInfo", znwhInfoVO);
                intent.putExtras(bundle);
                intent.setAction("com.hangon.fragment.order.ZnwhService");
                sendBroadcast(intent);

            }

            @Override
            public void onMyError(VolleyError error) {
                Log.d("error",error.toString());
            }
        });

    }

    private void updateZnwhInfo(){
        String url=Constants.UPDATE_ZNWH_INFO_URL+"?userId="+1;
        VolleyRequest.RequestGet(ZnwhService.this, url, "updateZnwhInfo", new VolleyInterface(ZnwhService.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {

            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }

}
