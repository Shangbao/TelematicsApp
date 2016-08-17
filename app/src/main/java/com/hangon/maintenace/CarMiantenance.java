package com.hangon.maintenace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.hangon.bean.miancar.DetaileInfo;
import com.hangon.bean.miancar.data;
import com.hangon.bean.miancar.result;
import com.hangon.common.MyApplication;
import com.hangon.common.MyStringRequest;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.map.util.GasInfoUtil;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.activity.MainOrderActivity;
import com.hangon.saying.util.Location;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mykonons on 2016/8/3.
 */
public class CarMiantenance extends Activity{
    //定位信息
    private LocationClient mLocClient;
    private TextView myLocation;
    private TextView myLocation1;
    private TextView myLocation2;
    private LatLng myPosition;
    private List<result> mianList;
    private List<DetaileInfo> detaileInfos;
    private  ViewHorder viewHorder;
    private ListView mianTananceList;
    //topbar
    private ImageView topleft;
    private ImageView topright;
    private TextView toptittle;
    //进入map状态判断
    JudgeNet judge;
    private String search=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintanance);

          initFindViewById();
             receiveData();
          setLocationOption();
         Timer timer=new Timer();
        timer.schedule(new wait(),1000);

    }

    private void receiveData() {
        String judge=this.getIntent().getExtras().getString("ids");
        if(judge.equals("b")) {
            toptittle.setText("周围维修店");
            search="汽修店";
        }
        if(judge.equals("a")){
            toptittle.setText("周围洗车店");
            search="洗车店";

        }

    }

    public void initFindViewById(){
         myLocation=(TextView)findViewById(R.id.mylocation);
         myLocation1=(TextView)findViewById(R.id.mylocation1);
         myLocation2=(TextView)findViewById(R.id.mylocation2);
         mianTananceList=(ListView)findViewById(R.id.maintanance_list);
         topleft=(ImageView)findViewById(R.id.topbar_left);
         topright=(ImageView)findViewById(R.id.topbar_right);
         toptittle=(TextView)findViewById(R.id.topbar_title);
         topleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     finish();
             }
         });

         topright.setVisibility(View.GONE);
     }
    //获取当前位置
    private void setLocationOption() {
        myPosition=new LatLng(0,0);
        mLocClient = ((Location) getApplication()).mLocationClient;
        ((Location) getApplication()).mTv = myLocation;
        ((Location) getApplication()).lat = myLocation1;
        ((Location) getApplication()).lon = myLocation2;
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        if (true) {
            option.setAddrType("all");
        }
        mLocClient.setLocOption(option);
        mLocClient.start();

       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               myPosition=new LatLng(Double.parseDouble(myLocation1.getText().toString().trim()),Double.parseDouble(myLocation2.getText().toString().trim()));
           }
       }).start();
    }


        class wait extends TimerTask {
            @Override
            public void run() {
             getJsonDataToXc();
            }
        }

    // 测试获得洗车店维修点的Json数据

    public  void getJsonDataToXc() {
        try {
            String url="http://api.map.baidu.com/place/v2/search?query="+search+"&page_size=100&page_num=0&scope=2&location="+myLocation1.getText().toString()+","+myLocation2.getText().toString()+"&radius=2000&output=json&ak=8ZcbE4SeBsjWyfulkiqswRaHOm1mFZV8&mcode=84:62:7A:13:86:10:06:F6:77:86:66:B5:46:E6:58:B1:A7:F4:85:BB;baidumapsdk.demo";
            MyStringRequest request = new MyStringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.e("aaa", response);
                            if (response == null || response.isEmpty()) {
                                return;
                            } else {
                                Gson gson = new Gson();
                                data data = gson.fromJson(response, data.class);
                                if(data==null){
                                    Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else{
                                    mianList=data.getResults();
                                    if(mianList==null||mianList.isEmpty()){return;}
                                    else{
                                Collections.sort(mianList);
                                Log.e("xxx",data.getStatus()+"");
                                Log.e("aaa",mianList.get(0).getAddress());
                                MiantananceCar miantananceCar=new MiantananceCar(mianList);
                                mianTananceList.setAdapter(miantananceCar);
                            }}}
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(),"网络错误", Toast.LENGTH_SHORT).show();
                }
            });
            request.setTag("StringReqGet");
            MyApplication.getHttpQueues().add(request);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
        }
    }
    class MiantananceCar extends BaseAdapter{
         List<result> miantananceList;

         MiantananceCar(List<result> results){
             this.miantananceList=results;
         }
        @Override
        public int getCount() {
            return miantananceList.size();
        }

        @Override
        public Object getItem(int position) {
            return miantananceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(null==convertView){
                viewHorder=new ViewHorder();
                convertView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.maintancelist,null);
                viewHorder.main_shop_address=(TextView)convertView.findViewById(R.id.main_shop_address);
                viewHorder.main_tainshop_name=(TextView)convertView.findViewById(R.id.main_tainshop_name);
                viewHorder.maintenanceshop_distance=(TextView)convertView.findViewById(R.id.maintenanceshop_distance);
                viewHorder.shopscoer=(TextView )convertView.findViewById(R.id.shopscore);
                viewHorder.main_overall_rating=(ImageView)convertView.findViewById(R.id.main_overall_rating);
                viewHorder.line_detail=(LinearLayout)convertView.findViewById(R.id.line_detail);
                viewHorder.line_phone=(LinearLayout)convertView.findViewById(R.id.line_phone);
                viewHorder.line_start_there=(LinearLayout)convertView.findViewById(R.id.line_start_there);
                convertView.setTag(viewHorder);
            }else{
                viewHorder= (ViewHorder) convertView.getTag();
            }
            viewHorder.main_shop_address.setText(miantananceList.get(position).getAddress());
            LatLng latLng=new LatLng(miantananceList.get(position).getLocation().getLat(),miantananceList.get(position).getLocation().getLng());
            viewHorder.maintenanceshop_distance.setText((int)DistanceUtil.getDistance(latLng,myPosition)+"");
            viewHorder.main_tainshop_name.setText(miantananceList.get(position).getName());

            double score=0;
            if(miantananceList.get(position).getDetail_info().getOverall_rating()!=null){
                score= Double.parseDouble(miantananceList.get(position).getDetail_info().getOverall_rating()+0);
                viewHorder.shopscoer.setText(miantananceList.get(position).getDetail_info().getOverall_rating()+"分");
            }else{
                viewHorder.shopscoer.setText("暂无评分");
            }
            if((int)score==0){
             viewHorder.main_overall_rating.setBackgroundResource(R.drawable.xcd_002);
            }
             if((int)score==1) {
                 viewHorder.main_overall_rating.setBackgroundResource(R.drawable.xcd_002);
            }
            if ((int)score==2){
                viewHorder.main_overall_rating.setBackgroundResource(R.drawable.xcd_003);
            }
            if((int)score==3){
                viewHorder.main_overall_rating.setBackgroundResource(R.drawable.xcd_004);
            }
            if((int)score==4){
                viewHorder.main_overall_rating.setBackgroundResource(R.drawable.xcd_005);
            }
            if((int)score==5){
                viewHorder.main_overall_rating.setBackgroundResource(R.drawable.xcd_006);
            }
            viewHorder.line_start_there.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    judge=new JudgeNet();
                    judge.setStates(1);
                    judge.setAppointRoute(1);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("lon", mianList.get(position).getLocation().getLng());
                    bundle.putDouble("lat", mianList.get(position).getLocation().getLat());
                    bundle.putString("endaddress", mianList.get(position).getAddress());
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MapMainActivity.class);
                    intent.putExtra("endaddress", bundle);

                    startActivity(intent);
                }
            });
            viewHorder.line_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mianList.get(position).getTelephone()==null){
                        Toast.makeText(getApplicationContext(),"暂无本店电话信息",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        new PopupWindows(getApplicationContext(), mianTananceList, position);
                    }
                }
            });
            viewHorder.line_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),CarMaindetails.class);
                    intent.putExtra("uid",mianList.get(position).getUid());
                    intent.putExtra("distance",viewHorder.maintenanceshop_distance.getText().toString().trim());
                    startActivity(intent);
                }
            });
           //     Toast.makeText(getApplicationContext(),miantananceList.get(position).getDetail_info().getOverall_rating()+"分",Toast.LENGTH_SHORT).show();
           Log.e("aass", miantananceList.get(position).getDetail_info().getOverall_rating() + "");
            return convertView;
        }
    }
    class ViewHorder{
        private TextView main_tainshop_name;
        private TextView maintenanceshop_distance;
        private TextView main_shop_address;
        private TextView shopscoer;
        private ImageView main_overall_rating;

        private LinearLayout line_start_there;
        private LinearLayout line_phone;
        private LinearLayout line_detail;
    }
    public class PopupWindows extends PopupWindow {
        public PopupWindows(Context mContext, View parent, final int position) {
            View view = View
                    .inflate(mContext, R.layout.carmain_detailse_pop, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.car_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_hujio);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);

            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(mianList==null||mianList.isEmpty())return;
                    Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mianList.get(position).getTelephone()));
                    startActivity(intent);
                    dismiss();
                    finish();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }
}
