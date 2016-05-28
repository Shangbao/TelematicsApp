package com.hangon.order.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.util.PersonalAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PersonalInformation extends Activity {
    private ListView mPersonalInformation;
    //	private List<PersonalInformationData> list;
    JudgeNet judge;
    /**
     * 装载数据
     */

    PersonalAdapter mPersonalAdapter;
    /**
     * 接收加油站在列表中所处位置
     */
    private int gasposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persona_linformation);
        //获取数据
        getData();
        //初始化控件
        // initFindViewById();
    }

    private void initReceiverData() {
        Bundle bundle = this.getIntent().getBundleExtra("gasposition");
        gasposition = bundle.getInt("position");
    }

    private void getData() {
        initReceiverData();
        String url = Constants.GET_CAR_INFO_URL;
        VolleyRequest.RequestGet(PersonalInformation.this, url, "getData", new VolleyInterface(PersonalInformation.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                Log.e("aaaa", result);
                List<PersonalInformationData> list = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
                Log.e("aaa", list.get(0).getName() + list.get(0).getPhoneNum() + list.get(0).getProvinceIndex());
                List mDatalist = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Map map = new HashMap<>();
                    map.put("personal_name", list.get(i).getName());
                    map.put("personal_phone", list.get(i).getPhoneNum());
                    map.put("personal_plate", Constants.PROVINCE_VALUE.charAt(list.get(i).getProvinceIndex()) + list.get(i).getCarLicenceTail());
                    mDatalist.add(map);
                }
                mPersonalInformation = (ListView) findViewById(R.id.personal_information);
                mPersonalAdapter = new PersonalAdapter(mDatalist, PersonalInformation.this, R.layout.personal_information_list);
                mPersonalInformation.setAdapter(mPersonalAdapter);
                judge = new JudgeNet();
                mPersonalInformation.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long arg3) {
                        judge.setPersonalInformation(1);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        bundle.putInt("gasposition", gasposition);
                        Intent intent = new Intent();
                        intent.setClass(PersonalInformation.this, AppointmentOrder.class);
                        intent.putExtra("personal_information", bundle);
                        startActivity(intent);
                        Toast.makeText(PersonalInformation.this, position + " ", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
