package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.YcglVO;
import com.hangon.weizhang.activity.MainActivity;
import com.hangon.weizhang.activity.WeizhangResult;
import com.hangon.weizhang.adapter.CarAdapter;
import com.hangon.weizhang.model.CarInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.fd.ourapplication.R.id.weizhang_cha;

/**
 * Created by Administrator on 2016/4/28.
 */
public class WeizhangActivity extends Activity{

    private Button weizhang_btn;
    private ListView carList;
    private List<CarInfo> carInfoList = new ArrayList<CarInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_weizhang);
        init();
        initCarInfo();
        CarAdapter adapter = new CarAdapter(WeizhangActivity.this,R.layout.car_item,carInfoList);
        carList = (ListView) findViewById(R.id.weizhang_info_list);
        carList.setAdapter(adapter);
        weizhang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeizhangActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WeizhangActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("carInfo", carInfoList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void init(){
        weizhang_btn = (Button) findViewById(R.id.weizhang_cha);
        carList = (ListView) findViewById(R.id.weizhang_info_list);
    }

    private void initCarInfo(){
        CarInfo carInfo = new CarInfo("川B12345","000459","1635",null,658);
        carInfoList.add(carInfo);
    }

}
