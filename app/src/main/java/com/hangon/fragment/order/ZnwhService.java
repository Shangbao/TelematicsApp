package com.hangon.fragment.order;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.fd.ourapplication.R;
import com.hangon.home.activity.HomeActivity;

import java.util.Random;

/**
 * Created by Chuan on 2016/5/9.
 */
public class ZnwhService extends Service {

    NotificationAdmain admain;
    Intent intent;
    static int NOTIFICATION_ID = 13565400;

    private Thread thread;

    int smallIcon = R.mipmap.ic_launcher;

    private boolean flag = false;

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
                    reflush();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ZnwhInfo", znwhInfo);
                    intent.putExtras(bundle);
                    intent.setAction("com.hangon.fragment.order.ZnwhService");
                    sendBroadcast(intent);
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

    public void reflush(){
        Random random = new Random();
        isGoodEngine = random.nextInt(2);
        isGoodTran = random.nextInt(2);
        isGoodLight = random.nextInt(2);
        znwhInfo.setUserId(1);
        znwhInfo.setMileage(Double.valueOf(Math.floor(random.nextDouble() * 10000)));
        znwhInfo.setOddGasAmount(random.nextInt(101));
        znwhInfo.setIsGoodEngine(isGoodEngine);
        znwhInfo.setIsGoodTran(isGoodTran);
        znwhInfo.setIsGoodLight(isGoodLight);
        if (isGoodEngine != oldEngine){
            if (isGoodEngine == 0){
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

//    public void getYcMessage(){
//        String url = ""+ "?userId=" +1;
//        VolleyRequest.RequestGet(Znwh.this, url, "getYcMessage", new VolleyInterface(Znwh.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
//            @Override
//            public void onMySuccess(String result) {
//                Log.d("res",result);
//            }
//
//            @Override
//            public void onMyError(VolleyError error) {
//                Log.d("tag", "onMyError: ");
//            }
//        });
//    }

}
