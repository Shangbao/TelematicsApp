package com.hangon.order.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fd.ourapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.map.Gastprice;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.map.util.GasInfoUtil;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.util.Date1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AppointmentOrder extends Activity {
	//加油站地址
	private TextView mGasAddress;
	//加油站名字
	private TextView mGasname;
	//个人姓名
	private TextView mCusname;
	//电话
	private TextView mCusphone;
	//车牌
	private TextView mCusPlatecar;
	//单价
	private TextView mGasPrice;
	//选择油的类型
	private Spinner mSelectGasType;
	//升数
	private EditText mGasLitre;
	//确定预约
	private TextView mConfirmappoint;
	//个人信息
	private LinearLayout mPerSonaInformal;

	/**
	 * 接收加油站油的类型与单价
	 */
	private List mGasTypeList;
	private List mGasTyprPriceList;
	int position;
	/**
	 * 定义Spiner所需要的适配器，加载数据
	 */
	private ArrayAdapter<String> adapter;
	/**
	 * 定义一个String，用来获取当前Spinner的值
	 */
	String selected;
	/**
	 * 接收个人信息数据
	 */
	private int mPersonalPosition;
	private int Gasposition;
	List<PersonalInformationData> mPersonalList;
	JudgeNet judge;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointgas);
		//mPersonalList=new ArrayList<>();
		getData();
		//组件获取
		initfindViewById();

		//接收数据
		ReceiveData();
		//下拉列表适配器
		SpinerAdapter();
	}

	private void setInitData(List<PersonalInformationData> mPersonalList) {
		if (judge.getPersonalInformation() == 0) {
			mCusname.setText(mPersonalList.get(0).getName());
			mCusphone.setText(mPersonalList.get(0).getPhoneNum());
			mCusPlatecar.setText(Constants.PROVINCE_VALUE.charAt(mPersonalList.get(0).getProvinceIndex())+mPersonalList.get(0).getCarLicenceTail());
		} else if (judge.getPersonalInformation() == 1) {
			Bundle bundle = this.getIntent().getExtras().getBundle("personal_information");
			mPersonalPosition = bundle.getInt("position");
			Gasposition = bundle.getInt("gasposition");
			mCusname.setText(mPersonalList.get(mPersonalPosition).getName());
			mCusphone.setText(mPersonalList.get(mPersonalPosition).getPhoneNum());
			mCusPlatecar.setText(Constants.PROVINCE_VALUE.charAt(mPersonalList.get(mPersonalPosition).getProvinceIndex())+mPersonalList.get(mPersonalPosition).getCarLicenceTail());
		}
	}
	private void initfindViewById() {
		mGasAddress = (TextView) findViewById(R.id.appoint_gasaddress);
		mGasname = (TextView) findViewById(R.id.appoint_gasname);
		mCusname = (TextView) findViewById(R.id.appointgas_cusname);
		mCusphone = (TextView) findViewById(R.id.appointgas_phone);
		mGasPrice = (TextView) findViewById(R.id.appoint_gasprice);
		mCusPlatecar = (TextView) findViewById(R.id.appointgas_platecar);
		mSelectGasType = (Spinner) findViewById(R.id.appoint_gasselecttype);
		mGasLitre = (EditText) findViewById(R.id.appoint_gaslitre);
		mConfirmappoint = (TextView) findViewById(R.id.confirm_appointment);
		mPerSonaInformal = (LinearLayout) findViewById(R.id.appoint_change_personal);

		mConfirmappoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				double a=Double.parseDouble(mGasLitre.getText().toString());
				double b=Double.parseDouble(mGasPrice.getText().toString());
				DialogTool.createNormalDialog(AppointmentOrder.this, "预约加油", "共" +a*b+ "元,是否预约", "取消", "确定", null, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PostVolley();
						Toast.makeText(AppointmentOrder.this,selected.toString(),Toast.LENGTH_LONG).show();

					}
				}).show();

			}
		});
		mPerSonaInformal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), position + " ", Toast.LENGTH_LONG).show();
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				Intent intent = new Intent();
				intent.setClass(AppointmentOrder.this, PersonalInformation.class);
				intent.putExtra("gasposition", bundle);
				startActivity(intent);
			}
		});
		judge = new JudgeNet();
		//mPersonalList = new ArrayList<PersonalInformationData>();
		//获取个人信息，存放于List
		// mPersonalList=PersonalInformationData.list;
		//用于获取加油站所有的加油类型
		mGasTypeList = new ArrayList();
		//用于获取对应有类型的价格
		mGasTyprPriceList = new ArrayList();
	}

	//接收数据
	private void ReceiveData() {
		if (judge.getPersonalInformation() != 1) {
			Bundle bundle = this.getIntent().getExtras().getBundle("GasSite");
			position = bundle.getInt("GasSiteposition");
			List<Gastprice> list = GasInfoUtil.gasinfo.get(position).getGastprice();
			for (int i = 0; i < list.size(); i++) {
				mGasTyprPriceList.add(list.get(i).getPrice());
				mGasTypeList.add(list.get(i).getName());
			}
			mGasAddress.setText(GasInfoUtil.gasinfo.get(position).getAddress());
			mGasname.setText(GasInfoUtil.gasinfo.get(position).getName());
		} else if (judge.getPersonalInformation() == 1) {
			List<Gastprice> list = GasInfoUtil.gasinfo.get(Gasposition).getGastprice();
			for (int i = 0; i < list.size(); i++) {
				mGasTyprPriceList.add(list.get(i).getPrice());
				mGasTypeList.add(list.get(i).getName());
			}
			mGasAddress.setText(GasInfoUtil.gasinfo.get(Gasposition).getAddress());
			mGasname.setText(GasInfoUtil.gasinfo.get(Gasposition).getName());
			judge.setPersonalInformation(0);
		}
	}

	private void SpinerAdapter() {
		//将可选内容与ArrayAdapter连接起来
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mGasTypeList);
		//设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		mSelectGasType.setAdapter(adapter);
		/**
		 * 默认选中第一个
		 */
		mSelectGasType.setSelection(0, true);
		//设置加油类型初始值
		selected = mGasTypeList.get(0).toString();
		mGasPrice.setText( mGasTyprPriceList.get(0).toString());
		// 省级下拉框监听
		mSelectGasType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			// 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selected = mGasTypeList.get(position).toString();
				mGasPrice.setText( mGasTyprPriceList.get(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}


	private void PostVolley() {
		double a=Double.valueOf(mGasLitre.getText().toString());
		double b=Double.valueOf(mGasPrice.getText().toString());
		String url=Constants.ADD_ORDER_INFO_URL;
		Toast.makeText(AppointmentOrder.this,"bb",Toast.LENGTH_LONG).show();
		final Map map = new HashMap<>();
		map.put("gasStationAddress", mGasAddress.getText().toString());
		map.put("gasStationName", mGasname.getText().toString());
		map.put("cusName", mCusname.getText().toString());
		map.put("cusPhoneNum", mCusphone.getText().toString());
		map.put("gasSinglePrice", mGasPrice.getText().toString());
		map.put("cusPlateNum", mCusPlatecar.getText().toString());
		map.put("gasType", selected.toString());
		map.put("gasSumPrice",a*b+"");
		map.put("gasLitre", mGasLitre.getText().toString());
		VolleyRequest.RequestPost(AppointmentOrder.this, url, "PostVolley", map, new VolleyInterface(AppointmentOrder.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
			@Override
			public void onMySuccess(String result) {
				Date1 date1=new Date1();
				double a=Double.parseDouble(mGasLitre.getText().toString());
				double b=Double.parseDouble(mGasPrice.getText().toString());
				Bundle bundle=new Bundle();
				bundle.putString("gasStationAddress", mGasAddress.getText().toString());
				bundle.putString("gasStationName", mGasname.getText().toString());
				bundle.putString("cusPlateNum", mCusPlatecar.getText().toString());
				bundle.putString("cusPhoneNum", mCusphone.getText().toString());
				bundle.putString("gasSinglePrice", mGasPrice.getText().toString());
				bundle.putString("gasType", selected.toString());
				bundle.putString("gasSumPrice", a * b + "");
				bundle.putString("gasLitre", mGasLitre.getText().toString());
				bundle.putString("cusphone", mCusphone.getText().toString());
				bundle.putString("cusName",mCusname.getText().toString());
				bundle.putString("orderTime",date1.GetTime());
                JudgeNet judgeNet=new JudgeNet();
				judgeNet.setAppointOrderData(1);
				Intent confirm = new Intent();
				confirm.setClass(AppointmentOrder.this, AppointMentOrderDetails.class);
				confirm.putExtra("orderdata",bundle);
				startActivity(confirm);
			}
			@Override
			public void onMyError(VolleyError error) {
				Toast.makeText(AppointmentOrder.this,"网络错误",Toast.LENGTH_LONG).show();
			}
		});

	}
	private void getData() {
		String url = Constants.GET_CAR_INFO_URL;
		VolleyRequest.RequestGet(AppointmentOrder.this, url, "getData", new VolleyInterface(AppointmentOrder.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
			@Override
			public void onMySuccess(String result) {
				Gson gson = new Gson();
				Log.e("aaaa", result);
				mPersonalList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
				}.getType());
				Log.e("aaa", mPersonalList.get(0).getName() + mPersonalList.get(0).getProvinceIndex()+mPersonalList.get(0).getPhoneNum());
			     setInitData(mPersonalList);
			}

			@Override
			public void onMyError(VolleyError error) {

			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}
