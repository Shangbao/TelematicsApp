package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.carInfo.BrandVO;
import com.hangon.bean.carInfo.CarInfoVO;
import com.hangon.bean.carInfo.CarMessageVO;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.JsonUtil;
import com.hangon.common.Topbar;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.home.activity.HomeActivity;
import com.hangon.map.util.JudgeNet;
import com.xys.libzxing.zxing.activity.CaptureActivity;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/4/26.
 */
public class SetCarInfoActivity extends Activity implements View.OnClickListener{


    ListView listView;//车辆信息列表
    SetCarInfoAdapter adapter;//车辆信息适配器

    Button btnShou;
    Button btnSao;
    Topbar topbar;

    List<CarMessageVO> carMessageList;//车辆信息列表数据
    Gson gson;//解析json数据

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_set_carinfo);
        init();
        getCarMessageList();
    }

    //初始化组件
    private void init(){
        btnSao= (Button) findViewById(R.id.btnSao);
        btnShou= (Button) findViewById(R.id.btnShou);
        topbar= (Topbar) findViewById(R.id.addCarMessageTopbar);
        btnSao.setOnClickListener(this);
        btnShou.setOnClickListener(this);
        listView= (ListView) findViewById(R.id.carInfoList);
        topbar.setRightIsVisible(false);
        topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                Intent intent=new Intent();
                intent.putExtra("id",1);
                intent.setClass(SetCarInfoActivity.this, HomeActivity.class);
                startActivity(intent);
                SetCarInfoActivity.this.finish();
            }

            @Override
            public void rightClick() {

            }
        });
    }

    //初始化适配器
    private void initAdapter(final List<CarMessageVO> carMessageList){
        adapter=new SetCarInfoAdapter(carMessageList,SetCarInfoActivity.this);
        adapter.setBtnClickListener(new SetCarInfoAdapter.btnClickListener() {
            @Override
            public void btnEditeClick(int position) {
                Intent intent = new Intent();
                intent.setClass(SetCarInfoActivity.this, AlterCarMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("carMessage", carMessageList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                Log.e("aa", "" + position);
            }

            @Override
            public void btnDeleteClick(final int position) {

                DialogTool.createNormalDialog(SetCarInfoActivity.this, "删除车辆信息", "真的要删除吗?", "确认", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(position);

                    }
                }, null).show();
            }

            @Override
            public void btnScanClick(int position) {
                Intent intent = new Intent();
                intent.setClass(SetCarInfoActivity.this, ScanCarMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("carMessage", carMessageList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
    }



    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnShou:
                Intent intent=new Intent();
                intent.setClass(SetCarInfoActivity.this,AddCarMessageActivity.class);
                startActivity(intent);
                JudgeNet judgeNet=new JudgeNet();
                if(judgeNet.getPersonalInformation()==2){
                    finish();
                }
                break;
            case R.id.btnSao:
                Intent intent1=new Intent();
                intent1.setClass(SetCarInfoActivity.this, CaptureActivity.class);
                startActivity(intent1);
                break;
        }

    }


   //获取车辆信息列表
    private void getCarMessageList(){
        String url= Constants.GET_CAR_INFO_URL;
        VolleyRequest.RequestGet(SetCarInfoActivity.this, url, "getCarMessageList", new VolleyInterface(SetCarInfoActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {

            @Override
            public void onMySuccess(String result) {
                gson=new Gson();
                carMessageList=new ArrayList<CarMessageVO>();
                carMessageList= gson.fromJson(result,new TypeToken<List<CarMessageVO>>(){}.getType());
                initAdapter(carMessageList);
            }

            @Override
            public void onMyError(VolleyError error) {
             Toast.makeText(SetCarInfoActivity.this,"网络异常,车辆加载失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    int p;//要删除的数据的位置
    private void delete(int position){
         p=position;
        String url=Constants.DELETE_CAR_INFO_URL+"?carInfoId="+carMessageList.get(position).getCarInfoId();

        VolleyRequest.RequestGet(SetCarInfoActivity.this, url, "deleteCarMessage", new VolleyInterface(SetCarInfoActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                carMessageList.remove(p);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(SetCarInfoActivity.this,"网络异常，删除失败",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
