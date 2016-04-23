package com.hangon.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.hangon.bean.user.UserInfo;


public class UserUtil {
	private static UserUtil instance;
	private SharedPreferences sharedPrefercne;
	public static UserUtil getInstance() {
		return instance;
	}

	public static void instance(Context context) {
		instance = new UserUtil(context);
	}

	private UserUtil(Context context) {
		sharedPrefercne = context.getSharedPreferences("user_info", Context.MODE_MULTI_PROCESS);
//		sharedPrefercne = context.getSharedPreferences("user_info", 0);
	}
	
	public UserInfo getUserInfo4Login() {
		UserInfo returnParam = getUserInfo();
		returnParam.setIsSave(getBooleanConfig("isSave"));
		returnParam.setUserName(getStringConfig("userName"));
		returnParam.setUserPass(getStringConfig("userPass"));
		return returnParam;
	}

	public UserInfo getUserInfo() {
		UserInfo returnParam = new UserInfo();
		returnParam.setNickname(getStringConfig("nickname"));
		returnParam.setAge(getIntegerConfig("age"));
		returnParam.setSex(getStringConfig("sex"));
		returnParam.setDriverNum(getStringConfig("driverNum"));
		returnParam.setUserIconContent(getStringConfig("userIconContent"));
		return returnParam;
	}
	
	public void saveLoginUserInfo(UserInfo loginUserInfo){
		saveStringConfig("userName", loginUserInfo.getUserName());
		saveStringConfig("userPass", loginUserInfo.getUserPass());
		saveStringConfig("nickname", loginUserInfo.getNickname());
		saveStringConfig("sex", loginUserInfo.getSex());
		saveIntegerConfig("age", loginUserInfo.getAge());
		saveStringConfig("driverNum", loginUserInfo.getDriverNum());
		saveStringConfig("userIconContent",loginUserInfo.getUserIconContent());
		saveBooleanConfig("isSave", loginUserInfo.isSave());
	}

	public void saveUpdateUserInfo(UserInfo userInfo){
		saveStringConfig("nickname", userInfo.getNickname());
		saveStringConfig("sex", userInfo.getSex());
		saveIntegerConfig("age", userInfo.getAge());
		saveStringConfig("driverNum", userInfo.getDriverNum());
	}


	public String getStringConfig(String key) {
		return sharedPrefercne.getString(key, "");
	}
	public int getIntegerConfig(String key) {
		return sharedPrefercne.getInt(key, -1);
	}

	public boolean getBooleanConfig(String key) {
		return sharedPrefercne.getBoolean(key, false);
	}

	public void saveStringConfig(String key, String value){
		sharedPrefercne.edit().putString(key, value).commit();
	}
	public void saveBooleanConfig(String key, boolean value){
		sharedPrefercne.edit().putBoolean(key, value).commit();
	}
	public void saveIntegerConfig(String key, int value){
		sharedPrefercne.edit().putInt(key, value).commit();
	}

}
