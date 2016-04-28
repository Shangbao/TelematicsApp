package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.CarMessageVO;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ScanCarMessageActivity extends Activity {
    private TextView car_name_spinner;// 车品牌
    private TextView car_type_spinner;//车品牌类型

    private TextView car_plate_num;//车牌号
    private TextView car_enginenum_edit;//车的引擎号
    private TextView car_degree;//车身级别
    private TextView car_chassis_number;//车架号

    private TextView car_mileage;//车行驶的公里数
    private TextView car_gas;//车汽油剩余量
    private TextView engine_is_good;//车引擎的状况
    private TextView trans_is_good;//车变速器的状况
    private TextView light_is_good;//车灯的状况

    private TextView state;

    CarMessageVO carMessageVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_car_message);
        Intent intent=getIntent();
        carMessageVO= (CarMessageVO) intent.getSerializableExtra("carMessage");
        init();
        setValue();
    }

    private void init(){
         car_name_spinner= (TextView) findViewById(R.id.car_name_spinner);// 车品牌
         car_type_spinner= (TextView) findViewById(R.id.car_type_spinner);//车品牌类型

         car_plate_num= (TextView) findViewById(R.id.car_plate_num);//车牌号
          car_enginenum_edit= (TextView) findViewById(R.id.car_engine_number);//车的引擎号
         car_degree= (TextView) findViewById(R.id.car_degree);//车身级别
        car_chassis_number= (TextView) findViewById(R.id.car_chassis_number);

        car_mileage= (TextView) findViewById(R.id.car_mileage);//车行驶的公里数
        car_gas= (TextView) findViewById(R.id.car_gas);//车汽油剩余量
        engine_is_good= (TextView) findViewById(R.id.engine_is_good);//车引擎的状况
        trans_is_good= (TextView) findViewById(R.id.trans_is_good);//车变速器的状况
        light_is_good= (TextView) findViewById(R.id.light_is_good);//车灯的状况

        state= (TextView) findViewById(R.id.state);
    }
    private void setValue(){

        car_plate_num.setText(carMessageVO.getProvinceIndex()+carMessageVO.getCarLicenceTail());
        car_enginenum_edit.setText(carMessageVO.getEngineNum());
        car_chassis_number.setText(carMessageVO.getChassisNum());

        car_degree.setText(carMessageVO.getDoorCount()+"门"+carMessageVO.getSeatCount()+"座");

        car_mileage.setText(carMessageVO.getMileage()+"");
        car_gas.setText(carMessageVO.getOddGasAmount()+"");

        if(carMessageVO.getIsGoodEngine()==1){
            engine_is_good.setText("正常");
        }else {
            engine_is_good.setText("异常");
        }

        if(carMessageVO.getIsGoodTran()==1){
            trans_is_good.setText("正常");
        }else {
            trans_is_good.setText("异常");
        }

        if(carMessageVO.getIsGoodLight()==1){
            light_is_good.setText("正常");
        }else {
            light_is_good.setText("异常");
        }

        if(carMessageVO.getState()==1){
            state.setText("默认使用车辆");
        }else {
            state.setText("非常用车辆");
        }
    }
}