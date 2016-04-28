package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.CarMessageVO;

/**
 * Created by Administrator on 2016/4/27.
 */
public class AlterCarMessageActivity extends Activity {
    private Spinner car_name_spinner;// 车品牌
    private Spinner car_type_spinner;//车品牌类型
    private Spinner province;//车牌省份

    private EditText car_num_pre;//区域号
    private EditText car_num_edit;//车尾号
    private EditText car_enginenum_edit;//车的引擎号
    private EditText door_count_edit;//车门数量
    private EditText seat_count_edit;//车座位数量
    private EditText car_chassis_number;//车架号

    private EditText car_mileage_edit;//车行驶的公里数
    private EditText car_gas_edit;//车汽油剩余量
    private Spinner engine_spinner;//车引擎的状况
    private Spinner trans_spinner;//车变速器的状况
    private Spinner light_spinner;//车灯的状况

    private Button finish_btn;//编辑成功的按钮

    CarMessageVO carMessageVO;//获取的指定车辆的信息

    private String[] carName = {"汽车品牌"};
    private String[] carType = {"汽车型号"};

    private ArrayAdapter<String> carNameAdapter;
    private ArrayAdapter<String> carTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gai_car_message);
        init();
        Intent intent=getIntent();
        carMessageVO= (CarMessageVO) intent.getSerializableExtra("carMessage");
        init();
        setEditeValue();

    }

    //初始化组件
    private void init(){
        car_name_spinner = (Spinner) findViewById(R.id.car_name_spinner);
        car_type_spinner = (Spinner) findViewById(R.id.car_type_spinner);

        province = (Spinner) findViewById(R.id.car_province);
        car_num_pre = (EditText) findViewById(R.id.pre);
        car_num_edit = (EditText) findViewById(R.id.car_number);
        car_enginenum_edit = (EditText) findViewById(R.id.car_engine_number);
        car_chassis_number= (EditText) findViewById(R.id.car_chassis_number);
        door_count_edit = (EditText) findViewById(R.id.door_count);
        seat_count_edit = (EditText) findViewById(R.id.seat_count);

        car_mileage_edit = (EditText) findViewById(R.id.car_mileage);
        car_gas_edit = (EditText) findViewById(R.id.car_gas);
        engine_spinner = (Spinner) findViewById(R.id.engine_is_good);
        trans_spinner = (Spinner) findViewById(R.id.trans_is_good);
        light_spinner = (Spinner) findViewById(R.id.light_is_good);

        finish_btn = (Button) findViewById(R.id.finish_btn);

        carNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,carName);
        car_name_spinner.setAdapter(carNameAdapter);
        carTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,carType);
        car_type_spinner.setAdapter(carTypeAdapter);
    }

    //设置组件里面的值
    private void setEditeValue(){
        //car_name_spinner.setSelection(carMessageVO.getBrandIndex());
        //car_type_spinner.setSelection(carMessageVO.getBrandTypeIndex());
        province.setSelection(carMessageVO.getProvinceIndex());
        car_num_pre.setText(carMessageVO.getCarLicenceTail().substring(0, 1));
        car_num_edit.setText(carMessageVO.getCarLicenceTail().substring(1));

        car_enginenum_edit.setText(carMessageVO.getEngineNum());
        car_chassis_number.setText(carMessageVO.getChassisNum());

        door_count_edit.setText(carMessageVO.getDoorCount()+"");
        seat_count_edit.setText(carMessageVO.getSeatCount()+"");

        //车异常监听
        car_mileage_edit.setText(carMessageVO.getMileage()+"");
        car_gas_edit.setText(carMessageVO.getOddGasAmount()+"");
        engine_spinner.setSelection(carMessageVO.getIsGoodEngine());
        trans_spinner.setSelection(carMessageVO.getIsGoodTran());
        light_spinner.setSelection(carMessageVO.getIsGoodLight());

    }
}
