package com.hangon.fragment.userinfo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.fd.ourapplication.R;
import com.hangon.common.Topbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/4/4.
 */
public class UserFragment extends Fragment {
    View userView;
    ListView userInfoList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         userView=inflater.inflate(R.layout.fragment_user,container,false);
        init();
        setUserAdapter();

        return userView;
    }

    //初始化组件
    private void init(){
        userInfoList= (ListView) userView.findViewById(R.id.userInfoList);
        Topbar topbar= (Topbar) userView.findViewById(R.id.topbar);
        topbar.setBtnIsVisible(false);
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

        map.put("imag",R.drawable.login_001);
        map.put("userInfoKey","手机号");
        map.put("userInfoValue","13166837709");
        list.add(map);

        map=new HashMap<String, Object>();
        map.put("imag", R.drawable.login_002);
        map.put("userInfoKey", "性别");
        map.put("userInfoValue", "男");
        list.add(map);

        map=new HashMap<String, Object>();
        map.put("imag", R.drawable.login_003);
        map.put("userInfoKey","驾驶证号");
        map.put("userInfoValue","11111");
        list.add(map);

        return  list;
    }
}
