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

import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.CarInfoVO;
import com.hangon.common.DialogTool;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class SetCarInfoActivity extends Activity {
    List<CarInfoVO> list;
    ListView listView;
    SetCarInfoAdapter adapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_set_carinfo);

        list=getCarInfos();
        listView= (ListView) findViewById(R.id.carInfoList);
        adapter=new SetCarInfoAdapter(list,SetCarInfoActivity.this);
        adapter.setBtnClickListener(new SetCarInfoAdapter.btnClickListener() {
            @Override
            public void btnEditeClick(int position) {
                Intent intent=new Intent();
                intent.setClass(SetCarInfoActivity.this,AlterCarMessageActivity.class);
                startActivity(intent);
                Log.e("aa",""+position);
            }

            @Override
            public void btnDeleteClick(final int position) {
                DialogTool.createNormalDialog(SetCarInfoActivity.this, "删除车辆信息", "真的要删除吗?", "确认", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }, null).show();
            }
        });
        listView.setAdapter(adapter);
    }

    private List<CarInfoVO> getCarInfos(){
        List<CarInfoVO> list=new ArrayList<CarInfoVO>();

        for(int i=0;i<5;i++){
            CarInfoVO carInfoVO=new CarInfoVO();
            carInfoVO.setName("邱勇" + i);
            carInfoVO.setPhoneNum("13166837709" + "Q" + i);
            carInfoVO.setPlateNum("京。A" + "666666" + i);
            if (i==1){
                carInfoVO.setState(1);
            }else {
                carInfoVO.setState(0);
            }
            list.add(carInfoVO);
        }
        return list;
    }

}
