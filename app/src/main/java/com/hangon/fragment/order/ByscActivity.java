package com.hangon.fragment.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.carInfo.ByscVO;
import com.hangon.common.Constants;
import com.hangon.common.JsonUtil;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.OrderData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/10.
 */
public class ByscActivity extends Activity {
    ImageView topbarLeft, topbarRight;
    TextView topbarTitle;
    ListView byscList;
    TextView byscKmTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bysc);
        //初始化
        init();
        //获取数据
        getByscData();

    }

    //初始化组件
    private void init(){
        topbarLeft= (ImageView) findViewById(R.id.topbar_left);
        topbarRight= (ImageView) findViewById(R.id.topbar_right);
        topbarTitle= (TextView) findViewById(R.id.topbar_title);
        byscList= (ListView) findViewById(R.id.byscList);
        byscKmTextView= (TextView) findViewById(R.id.byscKmTextView);
        topbarTitle.setText("专属保养手册");
        //返回键
        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("id", 3);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        topbarRight.setVisibility(View.GONE);
    }

    //给用户信息列表设置适配器
    private void setUserAdapter(List byscVOs) {
        String[] x = new String[]{"byscNum", "byxmName", "byxmState"};
        int[] y = new int[]{R.id.item_bysc_xh, R.id.item_bysc_name, R.id.item_bysc_state};
        SimpleAdapter adapter = new SimpleAdapter(ByscActivity.this, byscVOs, R.layout.item_bysc, x, y);
        byscList.setAdapter(adapter);
    }

    //把对象转换成map
    private List<Map<String,Object>> objectToMap(List<ByscVO> ls){
        List<Map<String,Object>> lsMap=new ArrayList<Map<String,Object>>();
        for(int i=0;i<ls.size();i++){
            Map<String,Object> map=new HashMap<String,Object>();
            ByscVO byscVO=ls.get(i);
            map.put("byxmName",byscVO.getByxmName());
            if(byscVO.getByscNum()==1){
                map.put("byscNum",R.drawable.lj_by_002);
            }

            if(byscVO.getByscNum()==2){
                map.put("byscNum",R.drawable.lj_by_003);
            }
            if(byscVO.getByscNum()==3){
                map.put("byscNum",R.drawable.lj_by_004);
            }

            if(byscVO.getByscNum()==4){
                map.put("byscNum",R.drawable.lj_by_005);
            }

            if(byscVO.getByscNum()==5){
                map.put("byscNum",R.drawable.lj_by_006);
            }

            if(byscVO.getByscNum()==6){
                map.put("byscNum",R.drawable.lj_by_007);
            }
            if(byscVO.getByscNum()==7){
                map.put("byscNum",R.drawable.lj_by_008);
            }

            if(byscVO.getByscNum()==8){
                map.put("byscNum",R.drawable.lj_by_006);
            }

            if(byscVO.getByxmState()==2){
                map.put("byxmState",R.drawable.lj_by_009);
            }
            if(byscVO.getByxmState()==1){
                map.put("byxmState",R.drawable.lj_by_010);
            }
            if(byscVO.getByxmState()==0){
                map.put("byxmState",R.drawable.lj_by_011);
            }
            if(byscVO.getByxmState()==3){
                map.put("byxmState",R.drawable.lj_by_012);
            }
            lsMap.add(map);
        }
        return lsMap;
    }

    //获取保养手册数据
    private void getByscData(){
        UserUtil.instance(ByscActivity.this);
        int userId= UserUtil.getInstance().getIntegerConfig("userId");
        String url= Constants.QUERY_BYSC_CONTENT_URL+"?userId="+userId;


        VolleyRequest.RequestGet(ByscActivity.this, url, "getByscData",  new VolleyInterface(ByscActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if(!result.equals("error")){
                      Map map= JsonUtil.jsonToMap(result);
                      String byscKm=  map.get("byscKm").toString();
                      String byscListStr=map.get("byscList").toString();

                      Gson gson = new Gson();
                      List<ByscVO> byscVOList=gson.fromJson(byscListStr, new TypeToken<List<ByscVO>>() {
                      }.getType());
                     byscKmTextView.setText(byscKm + "公里");
                     List<Map<String,Object>> mapArrayList =objectToMap(byscVOList);
                     setUserAdapter(mapArrayList);
                }else{
                    Toast.makeText(ByscActivity.this,"请先为您的车辆设置默认车辆!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
            }
        });
    }
}
