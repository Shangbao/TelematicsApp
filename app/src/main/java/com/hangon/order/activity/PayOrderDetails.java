package com.hangon.order.activity;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.OrderData;

public class PayOrderDetails extends Activity {
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

	OrderData orderData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_order_details);
		initFindViewById();
		receiveData();
	}

	/**
	 * 获取页面组件ID
	 */
	private void initFindViewById() {
		cusName = (TextView) findViewById(R.id.pay_cus_name);
		cusPhoneNum = (TextView) findViewById(R.id.pay_cus_phonenum);
		cusPlateNum = (TextView) findViewById(R.id.pay_cusplatenum);
		gasStationName = (TextView) findViewById(R.id.pay_order_gasname);
		gasStationAddress = (TextView) findViewById(R.id.pay_order_gasaddress);
		gasType = (TextView) findViewById(R.id.pay_order_gastype);
		gasLitre = (TextView) findViewById(R.id.pay_order_gaslitre);
		gasSinglePrice = (TextView) findViewById(R.id.pay_order_gasprice);
		gasSumPrice = (TextView) findViewById(R.id.pay_order_gassumpprice);
		gasStationType = (TextView) findViewById(R.id.pay_order_gasstationtype);
		mDeleteOrder = (TextView) findViewById(R.id.pay_order_delete);
		orderTime=(TextView)findViewById(R.id.pay_order_gastime);
		/**
		 * 实列化监听事件
		 */
		DeleteListener deleteListener=new DeleteListener();
		mDeleteOrder.setOnClickListener(deleteListener);
	}

	private void receiveData() {
		Bundle bundle = this.getIntent().getBundleExtra("gasdata");
		orderData = (OrderData) bundle.getSerializable("orderdata");
		cusName.setText(orderData.getCusName());
		cusPlateNum.setText(orderData.getCuspPlateNum());
		cusPhoneNum.setText(orderData.getCusPhoneNum());
		gasStationName.setText(orderData.getGasStationName());
		gasStationAddress.setText(orderData.getGasStationAddress());
		gasType.setText(orderData.getGasType());
		gasLitre.setText(orderData.getGasLitre());
		gasSinglePrice.setText(orderData.getGasSinglePrice());
		gasSumPrice.setText(orderData.getGasSumPrice());
		orderTime.setText(orderData.getStrTime());
		//gasStationType.setText(mReceiveDataList.get(position).getGasStationType());
	}
	public class DeleteListener implements OnClickListener{
		@Override
		public void onClick(View v) {
				DialogTool.createNormalDialog(PayOrderDetails.this, "删除订单", "确定删除此订单吗？", "取消", "确定", null, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogTool.createNormalDialog(PayOrderDetails.this, "取消订单", "确定取消吗？", "确定", "取消", null, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int OrderId=orderData.getOrderId();
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
					}
				}).show();

		}

	}
}
