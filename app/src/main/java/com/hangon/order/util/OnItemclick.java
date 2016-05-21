package com.hangon.order.util;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.hangon.order.activity.AppointMentOrderDetails;
import com.hangon.order.activity.PayOrderDetails;

public class OnItemclick implements OnItemClickListener {
	Context startcontext;
	List<OrderData> orderDatas;
	public OnItemclick(Context startcontext,List<OrderData> orderDatas) {
		this.startcontext=startcontext;
		this.orderDatas=orderDatas;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		if(orderDatas.get(position).getOrderState()==0){
			Bundle bundle = new Bundle();
			bundle.putSerializable("orderdata",orderDatas.get(position));
			Intent intent = new Intent();
			intent.putExtra("gasdata", bundle);
			intent.setClass(startcontext,AppointMentOrderDetails.class);
			startcontext.startActivity(intent);
		}
		if(orderDatas.get(position).getOrderState()==1){
			Bundle bundle = new Bundle();
			bundle.putSerializable("orderdata",orderDatas.get(position));
			Intent intent = new Intent();
			intent.putExtra("gasdata", bundle);
			intent.setClass(startcontext,PayOrderDetails.class);
			startcontext.startActivity(intent);
		}
	}
}