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
import com.hangon.common.UserUtil;
import com.hangon.map.util.GasInfoUtil;
import com.hangon.map.util.JudgeNet;

import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 */
public class GasSiteDetailsActivity extends Activity {
    private TextView gas_indent_name;
    private TextView gas_indent_address;
    private TextView GasNameDetails;
    private TextView GasAreanameDetails;
    private TextView GasFwlmscDetails;
    private TextView GasAddressDetails;
    private TextView GasDistanceDetails;
    private ImageView GasStartRoute;
    private Button start;

    private int position;
    private String gastype;
    private String gasprice;

    private LinearLayout gas_price_city;
    private LinearLayout gas_price_site;
    private LinearLayout gas_indent_maker;

    List<Gastprice> gascitylist ;
    JudgeNet judge;
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

    public void receive(){
        Bundle bundle=this.getIntent().getExtras().getBundle("mGasList");
        position=bundle.getInt("position");
        GasAddressDetails.setText(GasInfoUtil.gasinfo.get(position).getAddress());
        GasDistanceDetails.setText(GasInfoUtil.gasinfo.get(position).getDistance());
        GasAreanameDetails.setText(GasInfoUtil.gasinfo.get(position).getBrandname());
        GasNameDetails.setText(GasInfoUtil.gasinfo.get(position).getName());
        GasFwlmscDetails.setText(GasInfoUtil.gasinfo.get(position).getFwlsmc());
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
    private void init() {
        judge=new JudgeNet();
        GasStartRoute=(ImageView)findViewById(R.id.GasStartRoute);
        GasAddressDetails=(TextView)findViewById(R.id.gasAddressDetails);
        GasNameDetails=(TextView)findViewById(R.id.gasNameDetails);
        GasDistanceDetails=(TextView)findViewById(R.id.gasDistanceDetails);
        GasFwlmscDetails=(TextView)findViewById(R.id.gasFwlmscDetails);
        GasAreanameDetails=(TextView) findViewById(R.id.gasAreanameDetails);
        gas_price_city=(LinearLayout)findViewById(R.id.gas_price_city);
        gas_price_site=(LinearLayout)findViewById(R.id.gas_price_site);
        gas_indent_maker=(LinearLayout)findViewById(R.id.gas_indent_maker);
        GasStartRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge.setStates(3);
                Bundle bundle=new Bundle();
                bundle.putString("endaddress",GasAddressDetails.getText().toString());
                Intent intent=new Intent();
                intent.setClass(GasSiteDetailsActivity.this, BestRouteActivity.class);
                intent.putExtra("endaddress", bundle);
                startActivity(intent);
            }
        });
    }
}
