package com.hangon.weizhang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cheshouye.api.client.WeizhangClient;
import com.cheshouye.api.client.json.ProvinceInfoJson;
import com.example.fd.ourapplication.R;
import com.hangon.weizhang.adapter.ListAdapter;
import com.hangon.weizhang.model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class ProvinceList extends Activity {
	private ListView lv_list;
	private ListAdapter mAdapter;
	List<ProvinceInfoJson> provinceList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.csy_activity_citys);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.csy_titlebar);

		//标题
		TextView txtTitle = (TextView) findViewById(R.id.topbar_title);
		txtTitle.setText("选择查询地-省份");

		//返回按钮
		ImageView btnBack = (ImageView) findViewById(R.id.topbar_left);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ImageView rightBtn = (ImageView) findViewById(R.id.topbar_right);
		rightBtn.setVisibility(View.INVISIBLE);


		lv_list = (ListView) findViewById(R.id.lv_1ist);

		mAdapter = new ListAdapter(this, getData2());
		lv_list.setAdapter(mAdapter);

		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				TextView txt_name = (TextView) view.findViewById(R.id.txt_name);

				Intent intent = new Intent();
				intent.putExtra("province_name", txt_name.getText());
				intent.putExtra("province_id", txt_name.getTag().toString());

				intent.setClass(ProvinceList.this, CityList.class);
				startActivityForResult(intent, 20);
			}
		});

	}

	/**
	 * title:获取省份信息
	 *
	 * @return
	 */
	private List<ListModel> getData2() {

		List<ListModel> list = new ArrayList<ListModel>();
		provinceList = WeizhangClient.getAllProvince();

		//开通数量提示
		TextView txtListTip = (TextView) findViewById(R.id.list_tip);
		if(provinceList == null){
			txtListTip.setText("车首页系统正在维护中，由此带来的不便，请您谅解！");
		}
		else{
			txtListTip.setText("全国已开通"+ provinceList.size() +"个省份, 其它省将陆续开放");
		}

		for (ProvinceInfoJson provinceInfoJson : provinceList) {
			String provinceName = provinceInfoJson.getProvinceName();
			int provinceId = provinceInfoJson.getProvinceId();

			ListModel model = new ListModel();
			model.setTextName(provinceName);
			model.setNameId(provinceId);
			list.add(model);
		}
		return list;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		Bundle bundle = data.getExtras();
		// 获取城市name
		String cityName = bundle.getString("city_name");
		String cityId = bundle.getString("city_id");

		Intent intent = new Intent();
		intent.putExtra("city_name", cityName);
		intent.putExtra("city_id", cityId);
		setResult(1, intent);
		finish();
	}
}
