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
import com.hangon.bean.carInfo.CarInfoVO;
import com.hangon.bean.carInfo.CarMessageVO;
import com.hangon.common.DialogTool;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
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

    List<CarMessageVO> carMessageList;//车辆信息列表数据
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_set_carinfo);
        init();
        initAdapter();
    }

    //初始化组件
    private void init(){
        btnSao= (Button) findViewById(R.id.btnSao);
        btnShou= (Button) findViewById(R.id.btnShou);
        btnSao.setOnClickListener(this);
        btnShou.setOnClickListener(this);
        listView= (ListView) findViewById(R.id.carInfoList);
    }

    //初始化适配器
    private void initAdapter(){
        carMessageList=getCarMessageList();
        adapter=new SetCarInfoAdapter(carMessageList,SetCarInfoActivity.this);
        adapter.setBtnClickListener(new SetCarInfoAdapter.btnClickListener() {
            @Override
            public void btnEditeClick(int position) {
                Intent intent=new Intent();
                intent.setClass(SetCarInfoActivity.this, AlterCarMessageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("carMessage", carMessageList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                Log.e("aa",""+position);
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
                Intent intent=new Intent();
                intent.setClass(SetCarInfoActivity.this, ScanCarMessageActivity.class);
                Bundle bundle=new Bundle();
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
                break;
            case R.id.btnSao:
                Intent intent1=new Intent();
                intent1.setClass(SetCarInfoActivity.this, CaptureActivity.class);
                startActivity(intent1);
                break;
        }

    }


   //获取车辆信息列表
    private List<CarMessageVO> getCarMessageList(){
        List<CarMessageVO> list=new ArrayList<CarMessageVO>();

        CarMessageVO carMessageVO=new CarMessageVO();
        carMessageVO.setCarInfoId(1);
        carMessageVO.setBrandIndex(1);
        carMessageVO.setBrandTypeIndex(1);
        carMessageVO.setCarFlag("呵呵");
        carMessageVO.setDoorCount(3);
        carMessageVO.setSeatCount(5);

        carMessageVO.setChassisNum("abc123456");//车架号
        carMessageVO.setEngineNum("a1111");
       carMessageVO.setProvinceIndex(1);
        carMessageVO.setCarLicenceTail("A66666");

        carMessageVO.setName("黄大宝");
        carMessageVO.setPhoneNum("13166837709");
        carMessageVO.setMileage(150000);
        carMessageVO.setOddGasAmount(56);
        carMessageVO.setIsGoodEngine(1);
        carMessageVO.setIsGoodTran(0);
        carMessageVO.setIsGoodLight(1);
        carMessageVO.setState(1);

        list.add(carMessageVO);

        return list;
    }

    int p;//要删除的数据的位置
    private void delete(int position){
         p=carMessageList.get(position).getCarInfoId();
        String url=""+"position="+p;

        VolleyRequest.RequestGet(SetCarInfoActivity.this, url, "deleteCarMessage", new VolleyInterface(SetCarInfoActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if(result.equals("success")){
                    carMessageList.remove(p);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(SetCarInfoActivity.this,"网络异常，删除失败",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
