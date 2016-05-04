package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.carInfo.WeiZhangInfoVO;
import com.hangon.common.Constants;
import com.hangon.common.Topbar;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.home.activity.HomeActivity;
import com.hangon.weizhang.activity.MainActivity;
import com.hangon.weizhang.model.CarInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class WeizhangActivity extends Activity{

    private Button weizhang_btn;
    private ListView weizhangListView;
    private List<WeiZhangInfoVO> weizhangInfoList;
    private WeiZhangAdapter adapter;
    Topbar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_weizhang);
        init();
        getWeizhangData();//获取违章列表中的数据
       setEvent();//设置点击、选中等等事件
    }

    //初始化组件
    private void init(){
        weizhang_btn = (Button) findViewById(R.id.weizhang_cha);
        weizhangListView = (ListView) findViewById(R.id.weizhang_info_list);
        topbar= (Topbar) findViewById(R.id.weizhangTopbar);
    }
   //初始化适配器
    private void initAdapter(List<WeiZhangInfoVO> list){
        adapter=new WeiZhangAdapter(list,WeizhangActivity.this);
        weizhangListView.setAdapter(adapter);
    }

    //设置响应事件
    private void setEvent(){
        weizhang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeizhangActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        weizhangListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WeizhangActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                CarInfo carinfo = new CarInfo();
                carinfo.setChepai_no(Constants.PROVINCE_VALUE.charAt(weizhangInfoList.get(position).getProvinceIndex()) + weizhangInfoList.get(position).getCarLicenceTail());
                carinfo.setChejia_no(weizhangInfoList.get(position).getChassisNum());
                carinfo.setEngine_no(weizhangInfoList.get(position).getEngineNum());
                bundle.putSerializable("carInfo", carinfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        topbar.setRightIsVisible(false);
        topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                Intent intent=new Intent();
                intent.setClass(WeizhangActivity.this, HomeActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }

            @Override
            public void rightClick() {

            }
        });
    }

    //获取违章信息数据
    private void getWeizhangData(){
        String url= Constants.GET_CAR_INFO_URL;
        VolleyRequest.RequestGet(WeizhangActivity.this, url, "getWeizhangData", new VolleyInterface(WeizhangActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson=new Gson();
                weizhangInfoList=new ArrayList<WeiZhangInfoVO>();
                weizhangInfoList=gson.fromJson(result,new TypeToken<List<WeiZhangInfoVO>>(){}.getType());
               initAdapter(weizhangInfoList);
                Log.e("xxxxxx", result);
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(WeizhangActivity.this,"网络异常，请重试加载列表",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
