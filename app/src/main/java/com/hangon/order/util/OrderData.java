package com.hangon.order.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.activity.PersonalInformationData;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OrderData implements  Serializable{
	/**
	 * 判断支付状态
	 */
	private int orderState;
	/**
	 * 姓名
	 */
	private String cusName;
	/**
	 * 手机号
	 */
	private String cusPhoneNum;
	/**
	 *车牌号
	 */
	private String cusPlateNum;
	/**
	 * 加油站名称
	 */
	private String gasStationName;
//	/**
//	 * 加油站类型
//	 */
//	private String gasStationType;
	/**
	 * 加油站地址
	 */
	private String gasStationAddress;
	/**
	 * 加油类型
	 */
	private String gasType;
	/**
	 * 加油升数
	 */
	private String gasLitre;
	/**
	 * 单价
	 */
	private String gasSinglePrice;
	/**
	 * 总钱数
	 */
	private String gasSumPrice;
	/**
	 *预定时间
	 */
	private String strTime;
	private int orderId;


	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCusPlateNum() {
		return cusPlateNum;
	}

	public void setCusPlateNum(String cusPlateNum) {
		this.cusPlateNum = cusPlateNum;
	}

	public String getCusPhoneNum() {
		return cusPhoneNum;
	}

	public void setCusPhoneNum(String cusPhoneNum) {
		this.cusPhoneNum = cusPhoneNum;
	}

	public String getGasStationName() {
		return gasStationName;
	}

	public void setGasStationName(String gasStationName) {
		this.gasStationName = gasStationName;
	}

	public String getGasStationAddress() {
		return gasStationAddress;
	}

	public void setGasStationAddress(String gasStationAddress) {
		this.gasStationAddress = gasStationAddress;
	}

	public String getGasType() {
		return gasType;
	}

	public void setGasType(String gasType) {
		this.gasType = gasType;
	}

	public String getGasLitre() {
		return gasLitre;
	}

	public void setGasLitre(String gasLitre) {
		this.gasLitre = gasLitre;
	}

	public String getGasSinglePrice() {
		return gasSinglePrice;
	}

	public void setGasSinglePrice(String gasSinglePrice) {
		this.gasSinglePrice = gasSinglePrice;
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public String getGasSumPrice() {
		return gasSumPrice;
	}

	public void setGasSumPrice(String gasSumPrice) {
		this.gasSumPrice = gasSumPrice;
	}

	@Override
	public String toString() {
		return "OrderData{" +
				"orderState=" + orderState +
				", cusName='" + cusName + '\'' +
				", cusPhoneNum='" + cusPhoneNum + '\'' +
				", cusPlateNum='" + cusPlateNum + '\'' +
				", gasStationName='" + gasStationName + '\'' +
				", gasStationAddress='" + gasStationAddress + '\'' +
				", gasType='" + gasType + '\'' +
				", gasLitre='" + gasLitre + '\'' +
				", gasSinglePrice='" + gasSinglePrice + '\'' +
				", gasSumPrice='" + gasSumPrice + '\'' +
				", strTime='" + strTime + '\'' +
				", orderId=" + orderId +
				'}';
	}


	public OrderData(){

	}
}
