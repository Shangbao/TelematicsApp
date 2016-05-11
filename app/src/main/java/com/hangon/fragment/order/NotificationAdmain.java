package com.hangon.fragment.order;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Chuan on 2016/5/9.
 */
public class NotificationAdmain {
    private static int NOTIFICATION_ID;
    private NotificationManager nm;
    private Notification notification;
    private NotificationCompat.Builder cBuilder;
    private Notification.Builder nBuilder;
    private Context mContext;
    int requestCode = (int) SystemClock.uptimeMillis();
    private static final int FLAG = Notification.FLAG_INSISTENT;

    public NotificationAdmain(Context context, int ID) {
        this.NOTIFICATION_ID = ID;
        mContext = context;
        // 获取系统服务来初始化对象
        nm = (NotificationManager) mContext
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        cBuilder = new NotificationCompat.Builder(mContext);
    }

    /**
     * 设置在顶部通知栏中的各种信息
     */
    private void setCompatBuilder(Intent intent, int smallIcon, String ticker,
                                  String title, String msg) {
        // 如果当前Activity启动在前台，则不开启新的Activity。
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 当设置下面PendingIntent.FLAG_UPDATE_CURRENT这个参数的时候，常常使得点击通知栏没效果，你需要给notification设置一个独一无二的requestCode
        // 将Intent封装进PendingIntent中，点击通知的消息后，就会启动对应的程序
        PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        cBuilder.setContentIntent(pIntent);// 该通知要启动的Intent

        cBuilder.setSmallIcon(smallIcon);// 设置顶部状态栏的小图标
        cBuilder.setTicker(ticker);// 在顶部状态栏中的提示信息

        cBuilder.setContentTitle(title);// 设置通知中心的标题
        cBuilder.setContentText(msg);// 设置通知中心中的内容
        cBuilder.setWhen(System.currentTimeMillis());

        /*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
         * 不设置的话点击消息后也不清除，但可以滑动删除
         */
        cBuilder.setAutoCancel(true);
        // 将Ongoing设为true 那么notification将不能滑动删除
        // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
         * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
         */
        cBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        /*
         * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
         * Notification.DEFAULT_SOUND：系统默认铃声。
         * Notification.DEFAULT_VIBRATE：系统默认震动。
         * Notification.DEFAULT_LIGHTS：系统默认闪光。
         * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
         */
        cBuilder.setDefaults(Notification.DEFAULT_ALL);
    }

    /**
     * 普通的通知
     */
    public void normal_notification(Intent intent, int smallIcon,
                                    String ticker, String title, String msg) {

        setCompatBuilder(intent, smallIcon, ticker, title, msg);
        sent();
    }

    /**
     * 发送通知
     */
    private void sent() {
        notification = cBuilder.build();
        // 发送该通知
        nm.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 根据id清除通知
     */
    public void clear() {
        // 取消通知
        nm.cancelAll();

    }
}
