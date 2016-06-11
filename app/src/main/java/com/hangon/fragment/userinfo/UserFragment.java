package com.hangon.fragment.userinfo;

import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.fd.ourapplication.R;
import com.hangon.bean.user.UserInfo;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.ImageUtil;
import com.hangon.common.Topbar;
import com.hangon.common.UserUtil;
import com.hangon.fragment.music.MusicImage;
import com.hangon.fragment.order.ZnwhService;
import com.hangon.home.activity.HomeActivity;
import com.hangon.user.activity.LoginActivity;
import com.hangon.video.VideoActivity;
import com.hangon.video.VideoService;
import com.hangon.weather.WeatherService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/4/4.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    View userView;
    ListView userInfoList;
    UserInfo userInfo;//用户信息

    TextView homeNickName;//昵称
    TextView homePhoneNum;//手机号
    MusicImage homeHeadIcon;//头像显示
    ImageView toSetHeadIcon;//头像设置
    Switch aSwitch;//智能维护启动按钮
    Switch bSwitch;//行车记录启动按钮

    Button btnShare;//分享APP
    Button btnReturnLogin;//退出登录
    Button ckBtn;
    ZnwhService.MyBinder znwhBinder;
    VideoService.VideoBinder videoBinder;

    ImageButton topbarLeft, topbarRight;
    TextView topbarTitle;

    Intent znwhIntent;
    Intent videoIntent;

    private ProgressReceiver progressReceiver;

    private ServiceConnection znwhConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            znwhBinder = (ZnwhService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getActivity(), "智能维护开启失败！", Toast.LENGTH_SHORT).show();
        }
    };

    private ServiceConnection videoConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            videoBinder = (VideoService.VideoBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getActivity(), "亲，行车记录暂时无法使用哦！", Toast.LENGTH_SHORT).show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userView = inflater.inflate(R.layout.fragment_user, container, false);
        init();
        registerReceiver();
        UserUtil.instance(getActivity());
        userInfo = UserUtil.getInstance().getUserInfo4Login();
        homeNickName.setText(userInfo.getNickname());
        homePhoneNum.setText(userInfo.getUserName());
        getUserIconFromCookies();
        setUserAdapter();
        banListViewSlide();
        getActivity().bindService(videoIntent, videoConn, Service.BIND_AUTO_CREATE);
        getActivity().bindService(znwhIntent, znwhConn, Service.BIND_AUTO_CREATE);
        return userView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(znwhConn);
        getActivity().unbindService(videoConn);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case HomeActivity.INTENT_UPDATEUSER:
                if (resultCode == UpdateUserActivity.RESULT_UPDATE){
                }
                break;
        }
    }



    //获取内存里面的图片信息
    private void getUserIconFromCookies() {
        UserUtil.instance(getActivity());
        String s = UserUtil.getInstance().getStringConfig("userIconContent");
        if (s == null || s.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            Bitmap bitmap1 = ImageUtil.getRoundedCornerBitmap(bitmap, 100);
            homeHeadIcon.setImageBitmap(bitmap1);
        } else {
            byte[] bytes = ImageUtil.getStringByte(s);
            Bitmap bitmap = ImageUtil.getBitmapFromByte(bytes);
            Bitmap bitmap1 = ImageUtil.getRoundedCornerBitmap(bitmap, 100);
            homeHeadIcon.setImageBitmap(bitmap);
        }
    }

    //初始化组件
    private void init() {
        userInfoList = (ListView) userView.findViewById(R.id.userInfoList);
        homeNickName = (TextView) userView.findViewById(R.id.homeNickName);
        homePhoneNum = (TextView) userView.findViewById(R.id.homePhoneNum);
        homeHeadIcon = (MusicImage) userView.findViewById(R.id.homeHeadIcon);
        toSetHeadIcon = (ImageView) userView.findViewById(R.id.toSetHeadIcon);
        btnReturnLogin = (Button) userView.findViewById(R.id.btnReturnLogin);
        btnShare = (Button) userView.findViewById(R.id.btn_share);
        ckBtn = (Button) userView.findViewById(R.id.ck_video);
        aSwitch = (Switch) userView.findViewById(R.id.switch_znwh);
        bSwitch = (Switch) userView.findViewById(R.id.switch_xcjly);
        ckBtn.setOnClickListener(this);
        btnReturnLogin.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        toSetHeadIcon.setOnClickListener(this);
        topbarLeft = (ImageButton) userView.findViewById(R.id.topbar_left);
        topbarRight = (ImageButton) userView.findViewById(R.id.topbar_right);
        topbarTitle = (TextView) userView.findViewById(R.id.topbar_title);
        topbarRight.setBackgroundResource(R.drawable.grzx_03);
        topbarLeft.setVisibility(View.GONE);
        topbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUpdateUserInfo = new Intent();
                toUpdateUserInfo.setClass(getActivity(), UpdateUserActivity.class);
                startActivityForResult(toUpdateUserInfo, HomeActivity.INTENT_UPDATEUSER);
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().bindService(znwhIntent, znwhConn, Service.BIND_AUTO_CREATE);
                    znwhBinder.on();
                } else {
                    znwhBinder.off();
                    getActivity().unbindService(znwhConn);
                }
            }
        });
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    videoBinder.record();
                } else {
                    videoBinder.stop();
                    Toast.makeText(getActivity(), "行车记录成功，请进入内存设备查看！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        znwhIntent = new Intent(getActivity(), ZnwhService.class);
        videoIntent = new Intent(getActivity(), VideoService.class);
    }

    //给用户信息列表设置适配器
    private void setUserAdapter() {
        String[] x = new String[]{"img", "userInfoKey", "userInfoValue"};
        int[] y = new int[]{R.id.userImageView, R.id.userInfoKey, R.id.userInfoValue};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_user_info, x, y);
        userInfoList.setAdapter(adapter);
    }

    //获取用户列表的数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("img", R.drawable.user_icon1);
        map.put("userInfoKey", "性别");
        map.put("userInfoValue", userInfo.getSex());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.user_icon2);
        map.put("userInfoKey", "年龄");
        map.put("userInfoValue", userInfo.getAge());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.user_icon3);
        map.put("userInfoKey", "驾驶证号");
        map.put("userInfoValue", userInfo.getDriverNum());
        list.add(map);
        return list;
    }

    @Override
    //所有按钮的点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toSetHeadIcon:
                Intent toSetHeadIcon = new Intent();
                toSetHeadIcon.setClass(getActivity(), UserIconActivity.class);
                startActivityForResult(toSetHeadIcon, HomeActivity.INTENT_USERICON);
                break;
            case R.id.btnReturnLogin:
                DialogTool.createNormalDialog(getActivity(), "退出登录", "你确定要退出登录吗?", "取消", "确认", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearCookies();
                        getActivity().finish();
                        System.exit(0);
                    }
                }).show();

                break;
            case R.id.btn_share:
                break;

            case R.id.ck_video:
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                startActivity(intent);
                break;
        }
    }

    //清除存在内存卡里面的登录信息
    private void clearCookies() {
        UserUtil.instance(getActivity());
        UserUtil.getInstance().saveBooleanConfig("isSave", false);
        UserUtil.getInstance().saveStringConfig("userPass", "");
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    //禁止listview滑动
    private void banListViewSlide() {
        userInfoList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void registerReceiver() {
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VideoActivity.ACTION_STOP);
        getActivity().registerReceiver(progressReceiver, intentFilter);
    }

    class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (VideoActivity.ACTION_STOP.equals(action)) {
                bSwitch.setChecked(false);
                videoBinder.stop();
                Toast.makeText(getActivity(), "行车记录成功，请进入内存设备查看！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
