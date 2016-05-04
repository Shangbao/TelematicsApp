package com.hangon.map.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.map.Gastprice;
import com.hangon.map.util.GasInfoUtil;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.activity.AppointmentOrder;

import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 */
public class GasSiteDetailsActivity extends Activity {
    //加油站名称
    private TextView mGasNameDetails;
    //加油站类型
    private TextView mGasAreanameDetails;
    //加油卡
    private TextView mGasFwlmscDetails;
    //加油站地址
    private TextView mGasAddressDetails;
    //距离
    private TextView mGasDistanceDetails;
    //进行线路查询
    private ImageView mGasStartRoute;
    //预约加油
    private Button mAppointAddGas;
    //获取加油站在列表中的位置
    private int position;
 //自定义不知，代码生成
    private LinearLayout gas_price_city;
    private LinearLayout gas_price_site;
    private LinearLayout gas_indent_maker;
    //用于进行状态判断
    JudgeNet judge;
    //存取加油站数据
    GasInfoUtil datainfo;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_gas_details);
        context=GasSiteDetailsActivity.this;
        init();
        receive();
        datainfo=new GasInfoUtil();
    }
//由于接受加油站的数据
    public void receive(){
        Bundle bundle=this.getIntent().getExtras().getBundle("mGasList");
        position=bundle.getInt("position");
        mGasAddressDetails.setText(GasInfoUtil.gasinfo.get(position).getAddress());
        mGasDistanceDetails.setText(GasInfoUtil.gasinfo.get(position).getDistance());
        mGasAreanameDetails.setText(GasInfoUtil.gasinfo.get(position).getBrandname());
        mGasNameDetails.setText(GasInfoUtil.gasinfo.get(position).getName());
        mGasFwlmscDetails.setText(GasInfoUtil.gasinfo.get(position).getFwlsmc());
        for(int i=0;i<GasInfoUtil.gasinfo.get(position).getGastprice().size();i++){
            LinearLayout layout1=new LinearLayout(context);
            layout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,80));
            TextView textView1=new TextView(context);
            textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50,1));
            textView1.setGravity(Gravity.CENTER);
            textView1.setText(GasInfoUtil.gasinfo.get(position).getGastprice().get(i).getName());
            textView1.setTextSize(15);
            textView1.setTextColor(Color.rgb(0, 0, 0));
            TextView textView2=new TextView(context);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50,1));
            textView2.setGravity(Gravity.CENTER);
            textView2.setText(GasInfoUtil.gasinfo.get(position).getGastprice().get(i).getPrice());
            textView2.setTextSize(15);
            textView2.setTextColor(Color.rgb(0, 0, 0));
            layout1.addView(textView1);
            layout1.addView(textView2);
            gas_price_site.addView(layout1);

        }
        for(int i=0;i<GasInfoUtil.gasinfo.get(position).getPrice().size();i++){
            LinearLayout layout1=new LinearLayout(context);
            layout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,80));
            TextView textView1=new TextView(context);
            textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50,1));
            textView1.setGravity(Gravity.CENTER);
            textView1.setText(GasInfoUtil.gasinfo.get(position).getPrice().get(i).getType());
            textView1.setTextSize(15);
            textView1.setTextColor(Color.rgb(0, 0, 0));
            TextView textView2=new TextView(context);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50,1));
            textView2.setGravity(Gravity.CENTER);
            textView2.setText(GasInfoUtil.gasinfo.get(position).getPrice().get(i).getPrice());
            textView2.setTextSize(15);
            textView2.setTextColor(Color.rgb(0, 0, 0));
            layout1.addView(textView1);
            layout1.addView(textView2);
            gas_price_city.addView(layout1);
            ImageView imageView=new ImageView(context);
            imageView.setImageResource(R.drawable.map_add_gas);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,75));
            LinearLayout layout3=new LinearLayout(context);
            layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,80));
            layout3.addView(imageView);
            gas_indent_maker.addView(layout3);

        }

    }
    //获取页面各个控件以及监听事件的添加
    private void init() {
        judge=new JudgeNet();
        mGasStartRoute =(ImageView)findViewById(R.id.GasStartRoute);
        mGasAddressDetails =(TextView)findViewById(R.id.gasAddressDetails);
        mGasNameDetails =(TextView)findViewById(R.id.gasNameDetails);
        mGasDistanceDetails =(TextView)findViewById(R.id.gasDistanceDetails);
        mGasFwlmscDetails =(TextView)findViewById(R.id.gasFwlmscDetails);
        mGasAreanameDetails =(TextView) findViewById(R.id.gasAreanameDetails);
        gas_price_city=(LinearLayout)findViewById(R.id.gas_price_city);
        gas_price_site=(LinearLayout)findViewById(R.id.gas_price_site);
        gas_indent_maker=(LinearLayout)findViewById(R.id.gas_indent_maker);
        mAppointAddGas=(Button)findViewById(R.id.appoint_add_gas);
        Onclick mOnclick=new Onclick() ;
        mGasStartRoute.setOnClickListener(mOnclick);
        mAppointAddGas.setOnClickListener(mOnclick);

    }
    //设置监听事件
    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.GasStartRoute:
                    //发送终点位置到最优路线,用来进行状态判断
                    judge.setStates(3);
                    Bundle bundle = new Bundle();
                    bundle.putString("endaddress", mGasAddressDetails.getText().toString());
                    Intent intent = new Intent();
                    intent.setClass(GasSiteDetailsActivity.this, BestRouteActivity.class);
                    intent.putExtra("endaddress", bundle);
                    startActivity(intent);
                    break;
                case R.id.appoint_add_gas:
                    Bundle bundle1=new Bundle();
                    bundle1.putInt("GasSiteposition", position);
                    Intent intent2=new Intent();
                    intent2.setClass(GasSiteDetailsActivity.this, AppointmentOrder.class);
                    intent2.putExtra("GasSite", bundle1);
                    startActivity(intent2);
                    break;

            }
        }
    }
}
