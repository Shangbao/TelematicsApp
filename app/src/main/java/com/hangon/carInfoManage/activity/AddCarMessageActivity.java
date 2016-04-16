package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chuan on 2016/4/15.
 */
public class AddCarMessageActivity extends Activity {
    private Spinner car_name_spinner;
    private Spinner car_type_spinner;
    private Spinner province;
    private EditText car_num_pre;
    private EditText car_num_edit;
    private EditText car_enginenum_edit;
    private EditText door_count_edit;
    private EditText seat_count_edit;
    private EditText car_mileage_edit;
    private EditText car_gas_edit;
    private Spinner engine_spinner;
    private Spinner trans_spinner;
    private Spinner light_spinner;
    private Button finish_btn;

    private String carNum;
    private int provinceId;
    private String engineNum;
    private int doorCount;
    private int seatCount;
    private int mileage;
    private int gas;
    private boolean isGoodEngine;
    private boolean isGoodTran;
    private boolean isGoodLight;

    private String[] carName = {"汽车品牌"};
    private String[] carType = {"汽车型号"};

    private ArrayAdapter<String> carNameAdapter;
    private ArrayAdapter<String> carTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_message);
        init();
        carNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,carName);
        car_name_spinner.setAdapter(carNameAdapter);
        carTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,carType);
        car_type_spinner.setAdapter(carTypeAdapter);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(false==judge()){
                    Toast.makeText(AddCarMessageActivity.this, "亲，请完整填写信息！", Toast.LENGTH_SHORT).show();
                }
                else{
                    getData();
                    //addCarInfo();
                }
            }
        });
    }

    private void init(){
        car_name_spinner = (Spinner) findViewById(R.id.car_name_spinner);
        car_type_spinner = (Spinner) findViewById(R.id.car_type_spinner);
        province = (Spinner) findViewById(R.id.car_province);
        car_num_pre = (EditText) findViewById(R.id.pre);
        car_num_edit = (EditText) findViewById(R.id.car_number);
        car_enginenum_edit = (EditText) findViewById(R.id.car_engine_number);
        door_count_edit = (EditText) findViewById(R.id.door_count);
        seat_count_edit = (EditText) findViewById(R.id.seat_count);
        car_mileage_edit = (EditText) findViewById(R.id.car_mileage);
        car_gas_edit = (EditText) findViewById(R.id.car_gas);
        engine_spinner = (Spinner) findViewById(R.id.engine_is_good);
        trans_spinner = (Spinner) findViewById(R.id.trans_is_good);
        light_spinner = (Spinner) findViewById(R.id.light_is_good);
        finish_btn = (Button) findViewById(R.id.finish_btn);
        setRegion(car_gas_edit);
    }

    private void getData(){
        provinceId = province.getSelectedItemPosition();
        carNum = car_num_edit.getText().toString().trim() + car_num_pre.getText().toString();
        engineNum = car_enginenum_edit.getText().toString().trim();
        doorCount = Integer.parseInt(door_count_edit.getText().toString());
        seatCount = Integer.parseInt(seat_count_edit.getText().toString());
        mileage = Integer.parseInt(car_mileage_edit.getText().toString());
        gas = Integer.parseInt(car_gas_edit.getText().toString());
        isGoodEngine = engine_spinner.getSelectedItem().toString()=="正常"?true:false;
        isGoodTran = trans_spinner.getSelectedItem().toString()=="正常"?true:false;
        isGoodLight = light_spinner.getSelectedItem().toString()=="正常"?true:false;
    }

    private boolean judge(){
        if(car_num_edit.getText().toString().trim().isEmpty()
                ||car_enginenum_edit.getText().toString().trim().isEmpty()
                ||door_count_edit.getText().toString().trim().isEmpty()
                ||seat_count_edit.getText().toString().trim().isEmpty()
                ||car_mileage_edit.getText().toString().trim().isEmpty()
                ||car_gas_edit.getText().toString().trim().isEmpty())
            return false;
        else
            return true;
    }

    private void addCarInfo(){
        String url = "";
        Map<String,Object> map = new HashMap<>();
        map.put("carNum",carNum);
        map.put("provinceId",provinceId);
        map.put("engineNum",engineNum);
        map.put("doorCount",doorCount);
        map.put("seatCount",seatCount);
        map.put("mileage",mileage);
        map.put("gas",gas);
        map.put("isGoodEngine",isGoodEngine);
        map.put("isGoodTran",isGoodTran);
        map.put("isGoodLight", isGoodLight);
        VolleyRequest.RequestPost(this, url, "postUserCarInfo", map, new VolleyInterface(this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Toast.makeText(AddCarMessageActivity.this,"亲，添加完成了哦！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }
    private int MIN_MARK = 0;
    private int MAX_MARK = 100;
    private void setRegion( final EditText et)
    {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1)
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK)
                        {
                            s = String.valueOf(MAX_MARK);
                            et.setText(s);
                        }
                        else if(num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s)
            {
                if (s != null && !s.equals(""))
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int markVal = 0;
                        try
                        {
                            markVal = Integer.parseInt(s.toString());
                        }
                        catch (NumberFormatException e)
                        {
                            markVal = 0;
                        }
                        if (markVal > MAX_MARK)
                        {
                            Toast.makeText(getBaseContext(), "亲，汽油剩余量不能超过100%!", Toast.LENGTH_SHORT).show();
                            et.setText(String.valueOf(MAX_MARK));
                        }
                        return;
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
