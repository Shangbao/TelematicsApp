package com.hangon.fragment.userinfo;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.user.UserInfo;
import com.hangon.common.Topbar;
import com.hangon.common.UserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/4/4.
 */
public class UserFragment extends Fragment  {
    View userView;
    ListView userInfoList;
    UserInfo userInfo;//用户信息
    TextView homeNickName;//昵称
    TextView homePhoneNum;//手机号
    ImageView homeHeadIcon;//头像显示
    ImageView toSetHeadIcon;//头像设置
    Topbar userTopbar;//标题栏
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         userView=inflater.inflate(R.layout.fragment_user,container,false);
        init();
        UserUtil.instance(getActivity());
        userInfo=UserUtil.getInstance().getUserInfo4Login();
        Log.e("ee", userInfo.toString());
        homeNickName.setText(userInfo.getNickname());
        homePhoneNum.setText(userInfo.getUserName());
        setUserAdapter();
        initOnclick();
        return userView;
    }

    private void initOnclick(){
        toSetHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetHeadIcon = new Intent();
                toSetHeadIcon.setClass(getActivity(), UserIconActivity.class);
                startActivity(toSetHeadIcon);
            }
        });

        userTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
                Intent toUpdateUserInfo = new Intent();
                toUpdateUserInfo.setClass(getActivity(), UpdateUserActivity.class);
                startActivity(toUpdateUserInfo);
            }
        });

    }

    //初始化组件
    private void init(){
        userInfoList= (ListView) userView.findViewById(R.id.userInfoList);
        userTopbar= (Topbar) userView.findViewById(R.id.userTopbar);
        userTopbar.setLeftIsVisible(false);
        homeNickName= (TextView) userView.findViewById(R.id.homeNickName);
        homePhoneNum= (TextView) userView.findViewById(R.id.homePhoneNum);
        homeHeadIcon= (ImageView) userView.findViewById(R.id.homeHeadIcon);
        toSetHeadIcon= (ImageView) userView.findViewById(R.id.toSetHeadIcon);
    }

    //给用户信息列表设置适配器
    private void setUserAdapter(){
        String [] x=new String[]{"img","userInfoKey","userInfoValue"};
        int [] y=new int[]{R.id.userImageView,R.id.userInfoKey,R.id.userInfoValue};
        SimpleAdapter adapter=new SimpleAdapter(getActivity(),getData(),R.layout.item_user_info,x,y);
        userInfoList.setAdapter(adapter);
    }

    //获取用户列表的数据
    private List<Map<String,Object>> getData(){
      List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        Map<String,Object> map=new HashMap<String, Object>();

        map.put("imag", R.drawable.login_002);
        map.put("userInfoKey", "性别");
        map.put("userInfoValue",userInfo.getSex());
        list.add(map);

        map=new HashMap<String, Object>();
        map.put("imag", R.drawable.login_002);
        map.put("userInfoKey", "年龄");

        map.put("userInfoValue",userInfo.getAge());
        list.add(map);

        map=new HashMap<String, Object>();
        map.put("imag", R.drawable.login_003);
        map.put("userInfoKey","驾驶证号");
        map.put("userInfoValue",userInfo.getDriverNum());
        list.add(map);

        return  list;
    }
}
