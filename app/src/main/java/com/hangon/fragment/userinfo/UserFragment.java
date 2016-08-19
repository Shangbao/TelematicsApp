package com.hangon.fragment.userinfo;

import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.bean.user.UserInfo;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.ImageUtil;
import com.hangon.common.JsonUtil;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.fragment.music.MusicImage;
import com.hangon.fragment.order.ZnwhService;
import com.hangon.home.activity.HomeActivity;
import com.hangon.user.activity.LoginActivity;
import com.hangon.video.CkRecode;
import com.hangon.video.VideoService;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
   SwitchButton aSwitch;//智能维护启动按钮
   SwitchButton bSwitch;//行车记录启动按钮

    TextView btnShare;//分享APP
    TextView btnReturnLogin;//退出登录
    Button ckBtn;
    ZnwhService.MyBinder znwhBinder;

    ImageView topbarLeft, topbarRight;
    TextView topbarTitle;

    Intent znwhIntent;
    Intent videoIntent;

    public static final int UPDATE_TEXT = 1;
    public static final int UPDATE_ICON = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    UserUtil.instance(getActivity());
                    userInfo = UserUtil.getInstance().getUserInfo4Login();
                    homeNickName.setText(userInfo.getNickname());
                    homePhoneNum.setText(userInfo.getUserName());
                    setUserAdapter();
                    break;
                case UPDATE_ICON:
                    UserUtil.instance(getActivity());
                    userInfo = UserUtil.getInstance().getUserInfo4Login();
                    getUserIconFromCookies();
                    break;
            }
        }
    };

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userView = inflater.inflate(R.layout.fragment_user, container, false);
        init();
        UserUtil.instance(getActivity());
        userInfo = UserUtil.getInstance().getUserInfo4Login();
        homeNickName.setText(userInfo.getNickname());
        homePhoneNum.setText(userInfo.getUserName());
        getUserIconFromCookies();
        setUserAdapter();
        banListViewSlide();
        getActivity().bindService(znwhIntent, znwhConn, Service.BIND_AUTO_CREATE);

        return userView;

    }
   //判断文件夹是否存在
    private void judgeFolder(){

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(znwhConn);
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
        btnReturnLogin = (TextView) userView.findViewById(R.id.btnReturnLogin);
        btnShare = (TextView) userView.findViewById(R.id.btn_share);
        ckBtn = (Button) userView.findViewById(R.id.ck_video);
        aSwitch = (SwitchButton) userView.findViewById(R.id.switch_znwh);
        bSwitch = (SwitchButton) userView.findViewById(R.id.switch_xcjly);
        ckBtn.setOnClickListener(this);
        btnReturnLogin.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        toSetHeadIcon.setOnClickListener(this);
        topbarLeft = (ImageView) userView.findViewById(R.id.topbar_left);
        topbarRight = (ImageView) userView.findViewById(R.id.topbar_right);
        topbarTitle = (TextView) userView.findViewById(R.id.topbar_title);
        topbarRight.setImageResource(R.drawable.lj_gr_001);
        topbarLeft.setVisibility(View.GONE);
        topbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUpdateUserInfo = new Intent();
                toUpdateUserInfo.setClass(getActivity(), UpdateUserActivity.class);
                startActivityForResult(toUpdateUserInfo, HomeActivity.INTENT_UPDATEUSER);
            }
        });

        aSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    judgeCarExist(true);
                }else {
                    judgeCarExist(false);
                }
            }
        });
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(getActivity(), VideoService.class);
                    getActivity().startService(intent);
                } else {
                    Intent intent = new Intent(getActivity(), VideoService.class);
                    getActivity().stopService(intent);
                    Toast.makeText(getActivity(), "行车记录成功，请进入内存设备查看！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        znwhIntent = new Intent(getActivity(), ZnwhService.class);
        videoIntent = new Intent(getActivity(), VideoService.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case HomeActivity.INTENT_UPDATEUSER:
                if (resultCode == UpdateUserActivity.RESULT_UPDATE){
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = UPDATE_TEXT;
                            handler.sendMessage(message);
                        }
                    }, 200);
                }
                break;
            case HomeActivity.INTENT_USERICON:
                if (resultCode == getActivity().RESULT_OK){
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = UPDATE_ICON;
                            handler.sendMessage(message);
                        }
                    }, 500);
                }
                break;
        }
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

        map.put("img", R.drawable.grzx_23);
        map.put("userInfoKey", "性别");
        map.put("userInfoValue", userInfo.getSex());
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("img", R.drawable.grzx_27);
        map.put("userInfoKey", "年龄");
        map.put("userInfoValue", userInfo.getAge());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.grzx_31);
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
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // title标题：微信、QQ（新浪微博不需要标题）
                oks.setTitle("推荐车联网移动应用");  //最多30个字符

                // text是分享文本：所有平台都需要这个字段
                oks.setText("我正在使用车联网，大家一起来用吧！"+"http://10.163.200.124:8080/wind/img/background/ccut_znlsj.apk");  //最多40个字符

                //网络图片的url：所有平台
                oks.setImageUrl("http://10.163.200.124:8080/wind/img/background/shareapp.png");//网络图片rul

                // url：仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://10.163.200.124:8080/wind/img/background/ccut_znlsj.apk");   //网友点进链接后，可以看到分享的详情

                // Url：仅在QQ空间使用
                oks.setTitleUrl("http://10.163.200.124:8080/wind/img/background/ccut_znlsj.apk");  //网友点进链接后，可以看到分享的详情

                // 启动分享GUI
                oks.show(getActivity());

                break;

            case R.id.ck_video:

                File file=new File( Environment.getExternalStorageDirectory()+"/xinchejilu");
                if(file.exists()){
                    Intent intent = new Intent(getActivity(), CkRecode.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"没有行车记录",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //清除存在内存卡里面的登录信息
    private void clearCookies() {
        UserUtil.instance(getActivity());
        changeLoginFlag(0, UserUtil.getInstance().getIntegerConfig("userId"));
        UserUtil.getInstance().saveBooleanConfig("isSave", false);
        UserUtil.getInstance().saveStringConfig("userPass", "");
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
    }




    /**
     * 把登陆状态变为登陆状态
     */
    private void changeLoginFlag(int loginFlag,int userId){
        String url=Constants.CHANGE_LOGINFLAG_URL;
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("loginFlag",loginFlag+"");
        map.put("userId",userId+"");
        VolleyRequest.RequestPost(getActivity(), url, "changeLoginFlag1", map, new VolleyInterface(getActivity(),VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("result",result);
                Map map=new HashMap();
                map=JsonUtil.jsonToMap(result);
                boolean success= (boolean) map.get("success");
                String msg= (String) map.get("msg");
                if(success){
                    Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
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


    private void judgeCarExist(final boolean flag) {
        String url = Constants.QUERY_CAR_URL;
        Map map = new HashMap();
        UserUtil.instance(getActivity());
        String userId=UserUtil.getInstance().getIntegerConfig("userId")+"";
        map.put("userId",userId);
        VolleyRequest.RequestPost(getActivity(), url, "judgeCarExist", map, new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (result.equals("exist")) {
                   if(flag==true){
                       aSwitch.setChecked(true);
                       znwhBinder.on();
                   }else if(flag ==false){
                       aSwitch.setChecked(false);
                       znwhBinder.off();
                   }
                } else if (result.equals("noexist")) {
                    aSwitch.setChecked(false);
                    Toast.makeText(getActivity(), "无默认车辆,请设置默认车辆", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                aSwitch.setChecked(false);
                Toast.makeText(getActivity(), "网络或者服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
