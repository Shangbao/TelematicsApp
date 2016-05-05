package com.hangon.order.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.activity.AppointMentOrderDetails;

public class GasOrderAdapter extends BaseAdapter {
	ViewHolder vh;
	Context context;
	int resource;
	List<OrderData> gaslist;
	int position;
	public GasOrderAdapter(Context context,List<OrderData> list,int resource){
		this.context = context;
		this.resource = resource;
		this.gaslist = list;
	}
	@Override
	public int getCount() {
		return gaslist.size();
	}

	@Override
	public Object getItem(int position) {
		return gaslist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(resource, null);
			vh.list_gasname = (TextView) convertView.findViewById(R.id.list_order_gasname);
			vh.list_gassumprice = (TextView) convertView.findViewById(R.id.list_gassumprice);
			vh.list_gaslitre = (TextView) convertView.findViewById(R.id.list_gaslitre);
			vh.list_gastype = (TextView) convertView.findViewById(R.id.list_gastype);
			vh.list_ordertime = (TextView) convertView.findViewById(R.id.list_ordertime);
			vh.list_gasorder_status = (TextView) convertView.findViewById(R.id.list_gasorder_status);
			vh.list_gassumprice = (TextView) convertView.findViewById(R.id.list_gassumprice);
			vh.list_gassumprice = (TextView) convertView.findViewById(R.id.list_gassumprice);
			vh.gaslist_cancel_order = (TextView) convertView.findViewById(R.id.gaslist_cancel_order);
			vh.gaslist_payment_order = (TextView) convertView.findViewById(R.id.gaslist_payment_order);
           convertView.setTag(vh);
		}else{
			vh= (ViewHolder) convertView.getTag();
		}
		PayOnclickListener listener=new PayOnclickListener(position);
		vh.gaslist_cancel_order.setOnClickListener(listener);
		vh.gaslist_payment_order.setOnClickListener(listener);
		vh.list_gasname.setText(gaslist.get(position).getGasStationName());
		vh.list_gassumprice.setText(gaslist.get(position).getGasSumPrice());
		vh.list_gaslitre.setText(gaslist.get(position).getGasLitre());
		//vh.list_gastype.setText(map.get("OrderGasType").toString());
		vh.list_ordertime.setText(gaslist.get(position).getStrTime());
	//	vh.list_gasorder_status.setText(gaslist.get(position).getOrderState());
		if((gaslist.get(position).getOrderState()==1)){
			vh.list_gasorder_status.setText("已支付");
			vh.gaslist_cancel_order.setVisibility(View.GONE);
			vh.gaslist_payment_order.setText("删除订单");
		}
		notifyDataSetChanged();
		return convertView;
	}
	class ViewHolder {
		//关于订单列表字段
		/**
		 * 加油站名称
		 */
		TextView list_gasname;
		/**
		 * 支付状态
		 */
		TextView list_gasorder_status;
		/**
		 * 订单时间
		 */
		TextView list_ordertime;
		/**
		 * 加油类型
		 */
		TextView list_gastype;
		/**
		 * 加油升数
		 */
		TextView list_gaslitre;
		/**
		 * 总金额
		 */
		TextView list_gassumprice;
		/**
		 * 取消订单
		 */
		TextView gaslist_cancel_order;
		/**
		 * 付款项（当已经完结时，会将其改为删除按钮）
		 */
		TextView gaslist_payment_order;
	}
	public class PayOnclickListener implements OnClickListener{
		int position;
		public PayOnclickListener(int position) {
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.gaslist_cancel_order:
					DialogTool.createNormalDialog(context, "取消订单", "确定取消吗？", "确定", "取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							int orderId=gaslist.get(position).getOrderId();
							String url= Constants.DELETE_ORDER_INFO_URL+"?orderId="+orderId+"";
							VolleyRequest.RequestGet(context,url,"aaa", new VolleyInterface(context, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
								@Override
								public void onMySuccess(String result) {
									gaslist.remove(position);
									notifyDataSetChanged();
								}
								@Override
								public void onMyError(VolleyError error) {
									Toast.makeText(context, "网络错误", Toast.LENGTH_LONG).show();
								}
							});

						}
					}, null).show();
					break;
				case R.id.gaslist_payment_order:
					if(gaslist.get(position).getOrderState()==0){
					DialogTool.createNormalDialog(context, "取消订单", "确定取消吗？", "取消", "确定", null, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							int orderId = gaslist.get(position).getOrderId();
							String url = Constants.CHANGE_ORDER_INFO_URL + "?orderId=" + orderId + "";
							VolleyRequest.RequestGet(context, url, "aaa", new VolleyInterface(context, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
								@Override
								public void onMySuccess(String result) {
									notifyDataSetChanged();
								}

								;

								@Override
								public void onMyError(VolleyError error) {
									Toast.makeText(context, "网络错误", Toast.LENGTH_LONG).show();
								}
							});
					}
					}).show();}
					else if (gaslist.get(position).getOrderState()==1){
						DialogTool.createNormalDialog(context, "删除订单", "确定删除吗？", "取消", "确定", null, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int orderId = gaslist.get(position).getOrderId();
								String url = Constants.DELETE_ORDER_INFO_URL + "?orderId=" + orderId + "";
								VolleyRequest.RequestGet(context, url, "aaa", new VolleyInterface(context, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
									@Override
									public void onMySuccess(String result) {
										gaslist.remove(position);
										notifyDataSetChanged();
									}
									@Override
									public void onMyError(VolleyError error) {
										Toast.makeText(context, "网络错误", Toast.LENGTH_LONG).show();
									}
								});


							}
						}).show();}
					break;
			}
		}}

}

