package com.hangon.user.dao;

import android.content.Context;

import com.hangon.common.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/2.
 */
public class LoginDao {
    /**
     *
     * @return 返回map格式
     */
    public Map getLoginInfo (String s){
        Map loginInfo=new HashMap();
       loginInfo=JsonUtil.jsonToMap(s);
      return loginInfo;
    }
}
