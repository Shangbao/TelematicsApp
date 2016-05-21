package com.hangon.order.activity;

import java.util.List;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.util.OrderData;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import org.json.JSONArray;

public class AppointMentOrderDetails extends Activity {
	/**
	 * 客户姓名
	 */
	TextView cusName;
	/**
	 * 客户电话
	 */
	TextView cusPhoneNum;
	/**
	 * 客户车牌
	 */
	TextView cusPlateNum;
	/**
	 * 加油站地址
	 */
	TextView gasStationAddress;
	/**
	 * 加油站名称
	 */
	TextView gasStationName;
	/**
	 * 加油类型
	 */
	TextView gasType;
	/**
	 * 加油站类型
	 */
	TextView gasStationType;
	/**
	 * 加油升数
	 */
	TextView gasLitre;
	/**
	 * 单价
	 */
	TextView gasSinglePrice;
	/**
	 * 总钱数
	 */
	TextView gasSumPrice;
	/**
	 * 预定时间
	 */
	TextView orderTime;
	/**
	 * 删除订单
	 */
	TextView mDeleteOrder;
	/**
	 * 扫码加油
	 */
	TextView appointment_sweep_qrcode;
	/**
	 * 取消订单
	 */
	TextView appointment_cancel_order;
	/**
	 * 付款
	 */
	TextView appointment_paymoney;
	/**
	 * 接受数据List
	 */
	private OrderData orderData;
	//ID
	int OrderId;
	List<OrderData> mReceiveDataList;
	OrderData mOrderData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment_order_details);
		initFindViewById();
		receiveData();
	}

	private void initFindViewById() {
		cusName = (TextView) findViewById(R.id.appoint_cus_name);
		cusPhoneNum = (TextView) findViewById(R.id.appoint_cus_phonenum);
		cusPlateNum = (TextView) findViewById(R.id.appoint_cus_platenum);
		gasStationAddress = (TextView) findViewById(R.id.appoint_order_gasaddress);
		gasStationName = (TextView) findViewById(R.id.appoint_order_gasname);
		gasStationType = (TextView) findViewById(R.id.appoint_order_gasStationType);
		gasLitre = (TextView) findViewById(R.id.appoint_order_gassumlitre);
		gasSinglePrice = (TextView) findViewById(R.id.appoint_order_gasPrice);
		gasSumPrice = (TextView) findViewById(R.id.appoint_order_gasSumPrice);
		gasType = (TextView) findViewById(R.id.appoint_order_gasType);
		orderTime = (TextView) findViewById(R.id.appoint_order_gasTime);
		appointment_cancel_order = (TextView) findViewById(R.id.appointment_cancel_order);
		appointment_paymoney = (TextView) findViewById(R.id.appointment_paymony);
		appointment_sweep_qrcode = (TextView) findViewById(R.id.appointment_sweep_qrcode);
		AppointMentListeer appointMentListeer=new AppointMentListeer();
		appointment_cancel_order.setOnClickListener(appointMentListeer);
		appointment_paymoney.setOnClickListener(appointMentListeer);
		appointment_sweep_qrcode.setOnClickListener(appointMentListeer);
	}
	private void receiveData() {
		JudgeNet judgeNet=new JudgeNet();

		if(judgeNet.getAppointOrderData()==0){
		Bundle bundle = this.getIntent().getBundleExtra("gasdata");
		orderData = (OrderData) bundle.getSerializable("orderdata");
		cusName.setText(orderData.getCusName());
		cusPlateNum.setText(orderData.getCusPlateNum());
		cusPhoneNum.setText(orderData.getCusPhoneNum());
		gasStationName.setText(orderData.getGasStationName());
		gasStationAddress.setText(orderData.getGasStationAddress());
		gasType.setText(orderData.getGasType());
		gasLitre.setText(orderData.getGasLitre());
		gasSinglePrice.setText(orderData.getGasSinglePrice());
		gasSumPrice.setText(orderData.getGasSumPrice());
		orderTime.setText(orderData.getStrTime());
		OrderId=orderData.getOrderId();
		//gasStationType.setText(mReceiveDataList.get(position).getGasStationType());
	}
	if(judgeNet.getAppointOrderData()==1){
		Bundle bundle = this.getIntent().getBundleExtra("orderdata");
		cusName.setText(bundle.get("cusName").toString());
		cusPlateNum.setText(bundle.get("cusPlateNum").toString());
		cusPhoneNum.setText(bundle.get("cusphone").toString());
		gasStationName.setText(bundle.get("gasStationName").toString());
		gasStationAddress.setText(bundle.get("gasStationAddress").toString());
		gasType.setText(bundle.get("gasType").toString());
		gasLitre.setText(bundle.get("gasLitre").toString());
		gasSinglePrice.setText(bundle.get("gasSinglePrice").toString());
		gasSumPrice.setText(bundle.get("gasSumPrice").toString());
		orderTime.setText(bundle.get("orderTime").toString());
	}
	}
	public class AppointMentListeer implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.appointment_cancel_order:
					DialogTool.createNormalDialog(AppointMentOrderDetails.this, "取消订单", "确定取消吗？", "确定", "取消", null, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String url= Constants.DELETE_ORDER_INFO_URL+"?orderId="+OrderId+"";
							VolleyRequest.RequestGet(getApplicationContext(), url, "aaa", new VolleyInterface(getApplicationContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
								@Override
								public void onMySuccess(String result) {
									finish();
								}
								@Override
								public void onMyError(VolleyError error) {
									Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
								}
							});
						}
					}).show();
					break;
				case R.id.appointment_paymony:
					DialogTool.createNormalDialog(AppointMentOrderDetails.this, "取消订单", "确定取消吗？", "确定", "取消", null, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String url= Constants.CHANGE_ORDER_INFO_URL+"?orderId="+OrderId+"";
							VolleyRequest.RequestGet(getApplicationContext(), url, "aaa", new VolleyInterface(getApplicationContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
								@Override
								public void onMySuccess(String result) {
									finish();
								}
								@Override
								public void onMyError(VolleyError error) {
									Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
								}
							});
						}
					}).show();
					break;
				case R.id.appointment_sweep_qrcode:
					View root= LayoutInflater.from(getApplicationContext()).inflate(R.layout.qrcode,null);
					ImageView imageView=(ImageView)root.findViewById(R.id.qrcode_img);
					Bitmap bitmapDrawable=EncodingUtils.createQRCode("123", 100, 100, null);
						imageView.setImageBitmap(EncodingUtils.createQRCode("123", 100, 100, null));
					PopupWindow popupWindow=new PopupWindow(root,500,500);
					popupWindow.showAsDropDown(v);
					popupWindow.showAtLocation(findViewById(R.id.appoint_order_gasname), Gravity.CENTER, 20, 20);


					break;
			}
		}

	}
   private void ReceiveData(){

   }
}
