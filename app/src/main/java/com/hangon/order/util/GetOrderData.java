package com.hangon.order.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrderData {
	/**
	 * 全部订单
	 *
	 * @return
	 */
	OrderData mOrderData = new OrderData();

	private Context context;
   public GetOrderData(Context context){
		this.context=context;
	}
	public List getAllData() {
		List<OrderData> mOrderList = mOrderData.mOrderDataList;
		List mOrderDataList = new ArrayList<>();
		for (int i = 0; i < mOrderList.size(); i++) {
			Map map = new HashMap();
			map.put("OrderGasStationName", mOrderList.get(i).getGasStationName());
			map.put("OrderGasStationAddress", mOrderList.get(i).getGasStationAddress());
			//map.put("OrderGasStationType", mOrderList.get(i).getGasStationType());
			map.put("OrderGasLitre", mOrderList.get(i).getGasLitre());
			map.put("OrderGasSinglePrice", mOrderList.get(i).getGasSinglePrice());
			map.put("OrderGasSumPrice", mOrderList.get(i).getGasSumPrice());
			map.put("OrderGasType", mOrderList.get(i).getGasType());
			map.put("OrderGasTime", mOrderList.get(i).getStrTime());
			if (mOrderList.get(i).getOrderState() == 0) {
				map.put("OrderGasState", "待支付");
			} else if (mOrderList.get(i).getOrderState() == 1) {
				map.put("OrderGasState", "已支付");
			}
			mOrderDataList.add(map);
		}
		return mOrderDataList;
	}

	/**
	 * 已支付订单
	 *
	 * @return
	 */
	public List getPayData() {
		List<OrderData> mOrderList = mOrderData.mOrderDataList;
		List mOrderDataList = new ArrayList<>();
		for (int i = 0; i < mOrderList.size(); i++) {

			if (mOrderList.get(i).getOrderState() == 1) {
				Map map = new HashMap();
				map.put("OrderGasState", "已支付");
				map.put("OrderGasStationName", mOrderList.get(i).getGasStationName());
				map.put("OrderGasStationAddress", mOrderList.get(i).getGasStationAddress());
			//	map.put("OrderGasStationType", mOrderList.get(i).getGasStationType());
				map.put("OrderGasLitre", mOrderList.get(i).getGasLitre());
				map.put("OrderGasSinglePrice", mOrderList.get(i).getGasSinglePrice());
				map.put("OrderGasSumPrice", mOrderList.get(i).getGasSumPrice());
				map.put("OrderGasType", mOrderList.get(i).getGasType());
				map.put("OrderGasTime", mOrderList.get(i).getStrTime());
				mOrderDataList.add(map);
			}

		}
		return mOrderDataList;
	}

	/**
	 * 未支付订单
	 */
	public List getNotPayData() {
		List<OrderData> mOrderList = mOrderData.mOrderDataList;
		List mOrderDataList = new ArrayList<>();
		for (int i = 0; i < mOrderList.size(); i++) {
			if (mOrderList.get(i).getOrderState() == 0) {
				Map map = new HashMap();
				map.put("OrderGasState", "待支付");
				map.put("OrderGasStationName", mOrderList.get(i).getGasStationName());
				map.put("OrderGasStationAddress", mOrderList.get(i).getGasStationAddress());
			//	map.put("OrderGasStationType", mOrderList.get(i).getGasStationType());
				map.put("OrderGasLitre", mOrderList.get(i).getGasLitre());
				map.put("OrderGasSinglePrice", mOrderList.get(i).getGasSinglePrice());
				map.put("OrderGasSumPrice", mOrderList.get(i).getGasSumPrice());
				map.put("OrderGasType", mOrderList.get(i).getGasType());
				map.put("OrderGasTime", mOrderList.get(i).getStrTime());
				mOrderDataList.add(map);
			}
		}
		return mOrderDataList;
	}


 public List OrderData(List list){
	 return list;

 }

	public void getData() {

		String url = Constants.GET_ORDER_INFOS_URL;
		VolleyRequest.RequestGet(context, url, "getData", new VolleyInterface(context, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
			@Override
			public void onMySuccess(String result) {
				Gson gson = new Gson();
				Log.e("aaaa", result);
				List<OrderData> getOrderData= gson.fromJson(result, new TypeToken<List<OrderData>>() {}.getType());
				Log.d("abcd", getOrderData.get(0).getCusName() + getOrderData.get(0).getCusPhoneNum());

				Toast.makeText(context, getOrderData.get(0).getCusName() + getOrderData.get(0).getCusPhoneNum(), Toast.LENGTH_LONG).show();
				for (int i = 0; i <getOrderData.size(); i++) {
					Map map = new HashMap();
					map.put("OrderGasStationName", getOrderData.get(i).getGasStationName());
					map.put("OrderGasStationAddress", getOrderData.get(i).getGasStationAddress());
					//	map.put("OrderGasStationType",getOrderData.get(i).getGasStationType());
					map.put("OrderGasLitre", getOrderData.get(i).getGasLitre());
					map.put("OrderGasSinglePrice", getOrderData.get(i).getGasSinglePrice());
					map.put("OrderGasSumPrice", getOrderData.get(i).getGasSumPrice());
					map.put("OrderGasType", getOrderData.get(i).getGasType());
					map.put("OrderGasTime", getOrderData.get(i).getStrTime());
					if (getOrderData.get(i).getOrderState() == 0) {
						map.put("OrderGasState", "待支付");
					} else if (getOrderData.get(i).getOrderState() == 1) {
						map.put("OrderGasState", "已支付");
					}
					//mOrderList.add(map);
				}

			}

			@Override
			public void onMyError(VolleyError error) {

			}
		});
	}
}