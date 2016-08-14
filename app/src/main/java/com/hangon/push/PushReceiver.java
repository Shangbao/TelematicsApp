package com.hangon.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hangon.common.SystemUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Chuan on 2016/8/10.
 */
public class PushReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("收到", "onReceive - " + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
        Log.d("收到", "onReceive - " + bundle.getString(JPushInterface.EXTRA_ALERT));
        Log.d("收到", "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, PushActivity.class);  //自定义打开的界面
            Log.d("收到2", "onReceive - " + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
            Log.d("收到2", "onReceive - " + bundle.getString(JPushInterface.EXTRA_ALERT));
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Log.d("失败", "Unhandled intent - " + intent.getAction());
        }
    }

}
