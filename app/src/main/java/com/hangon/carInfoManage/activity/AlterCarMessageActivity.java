package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.carInfo.BrandTypeVO;
import com.hangon.bean.carInfo.BrandVO;
import com.hangon.bean.carInfo.CarMessageVO;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.Topbar;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/27.
 */
public class AlterCarMessageActivity extends Activity {
    Gson gson;//数据解析器
    List<BrandVO> brandList;//车型号
    List<BrandTypeVO> brandTypeList;//车类型

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

    private EditText cus_name;
    private EditText phone_num;
    private  Spinner state;

    Topbar topbar;

    CarMessageVO carMessageVO;//获取的指定车辆的信息

    private List<String> carName = null;
    private List<String> carType = null;

    private ArrayAdapter<String> carNameAdapter;
    private ArrayAdapter<String> carTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gai_car_message);
        Intent intent=getIntent();
        carMessageVO= (CarMessageVO) intent.getSerializableExtra("carMessage");
        Log.e("aaa", carMessageVO.getCarInfoId() + "");
        init();
        setEditeValue();//为Editext设置值
        getBrandInfoList();//获取品牌列表
        setSpinnerListener();
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

        cus_name= (EditText) findViewById(R.id.cus_name);
        phone_num= (EditText) findViewById(R.id.phone_num);
        state= (Spinner) findViewById(R.id.state);
        topbar= (Topbar) findViewById(R.id.alterCarMessageTopbar);

    }

    //为spinner设置监听事件
    private void setSpinnerListener(){
        car_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carType=null;
                carType=new ArrayList<String>();
                for (int i=0;i<brandList.get(position).getBrandTypeList().size();i++){
                    carType.add(brandList.get(position).getBrandTypeList().get(i).getName().toString().trim());
                }
                carTypeAdapter = new ArrayAdapter<String>(AlterCarMessageActivity.this,android.R.layout.simple_list_item_multiple_choice,carType);
                car_type_spinner.setAdapter(carTypeAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        topbar.setLeftIsVisible(false);

        topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
                DialogTool.createNormalDialog(AlterCarMessageActivity.this, "修改车辆信息", "你确定要修改吗?", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      getData();
                        updateCarInfo();
                    }
                }, null).show();
            }
        });

    }

    //设置组件里面的值
    private void setEditeValue(){
        province.setSelection(carMessageVO.getProvinceIndex());
        car_num_pre.setText(carMessageVO.getCarLicenceTail().substring(0, 1));
        car_num_edit.setText(carMessageVO.getCarLicenceTail().substring(1));

        cus_name.setText(carMessageVO.getName());
        phone_num.setText(carMessageVO.getPhoneNum());

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
        state.setSelection(carMessageVO.getState());
    }

    //获得车品牌数据
    public void getBrandInfoList(){
        String url= Constants.GET_BRAND_INFO_URL;
        VolleyRequest.RequestGet(AlterCarMessageActivity.this, url, "getBrandInfoList", new VolleyInterface(AlterCarMessageActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                gson = new Gson();
                brandList=new ArrayList<BrandVO>();
                brandList= gson.fromJson(result,new TypeToken<List<BrandVO>>(){}.getType());
                setAdapter(brandList,carMessageVO.getBrandIndex());
                car_name_spinner.setSelection(carMessageVO.getBrandIndex());
                car_type_spinner.setSelection(carMessageVO.getBrandTypeIndex());
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(AlterCarMessageActivity.this, "网络异常，请重试加载", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //给车类型设置适配器
    private void setAdapter(List<BrandVO> brandList,int index){
        carName=new ArrayList<String>();
        carType=new ArrayList<String>();
        for(int i=0;i<brandList.size();i++){
            carName.add(brandList.get(i).getBrand());
        }

        for (int i=0;i<brandList.get(index).getBrandTypeList().size();i++){
            carType.add(brandList.get(index).getBrandTypeList().get(i).getName().toString().trim());
        }

        //为车品牌和车类型添加适配器
        carNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,carName);
        car_name_spinner.setAdapter(carNameAdapter);
        carTypeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,carType);
        car_type_spinner.setAdapter(carTypeAdapter);
    }

    //获取数据
    private void getData(){
        carMessageVO.setBrandIndex(car_name_spinner.getSelectedItemPosition());
        carMessageVO.setBrandTypeIndex(car_type_spinner.getSelectedItemPosition());

        carMessageVO.setPhoneNum(phone_num.getText().toString().trim());
        carMessageVO.setName(cus_name.getText().toString().trim());

        carMessageVO.setProvinceIndex(province.getSelectedItemPosition());
        carMessageVO.setCarLicenceTail(car_num_pre.getText().toString()+car_num_edit.getText().toString().trim() );
        carMessageVO.setChassisNum(car_chassis_number.getText().toString().trim());
        carMessageVO.setEngineNum(car_enginenum_edit.getText().toString().trim());

        carMessageVO.setDoorCount(Integer.parseInt(door_count_edit.getText().toString()));
        carMessageVO.setSeatCount(Integer.parseInt(seat_count_edit.getText().toString()));

        carMessageVO.setMileage(Double.parseDouble(car_mileage_edit.getText().toString()));
        carMessageVO.setOddGasAmount(Integer.parseInt(car_gas_edit.getText().toString()));
        carMessageVO.setIsGoodEngine(engine_spinner.getSelectedItem().toString().equals("正常") ?1:0);
        carMessageVO.setIsGoodTran(trans_spinner.getSelectedItem().toString().equals("正常") ? 1 : 0);
        carMessageVO.setIsGoodLight(light_spinner.getSelectedItem().toString().equals("正常")?1:0);
        carMessageVO.setState(state.getSelectedItemPosition() == 0 ? 1 : 0);
    }

    private void updateCarInfo(){
        String url = Constants.UPDATE_CAR_INFO_URL;

        Map<String,Object> map = new HashMap<>();
        map.put("carInfoId",carMessageVO.getCarInfoId()+"");
        map.put("brandIndex", carMessageVO.getBrandIndex() + "");
        map.put("brandTypeIndex", carMessageVO.getBrandTypeIndex() + "");

        // map.put("carFlag",carMessageVO.getCarFlag());
        map.put("provinceIndex", carMessageVO.getProvinceIndex() + "");
        map.put("carLicenceTail", carMessageVO.getCarLicenceTail());

        map.put("name", carMessageVO.getName());
        map.put("phoneNum", carMessageVO.getPhoneNum());
        map.put("mileage", carMessageVO.getMileage() + "");
        map.put("chassisNum", carMessageVO.getChassisNum());
        map.put("engineNum", carMessageVO.getEngineNum());

        map.put("oddGasAmount", carMessageVO.getOddGasAmount() + "");
        map.put("isGoodEngine", carMessageVO.getIsGoodEngine() + "");
        map.put("isGoodTran", carMessageVO.getIsGoodTran() + "");
        map.put("isGoodLight", carMessageVO.getIsGoodLight() + "");
        map.put("state", carMessageVO.getState() + "");

        VolleyRequest.RequestPost(this, url, "postUserCarInfo", map, new VolleyInterface(this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Toast.makeText(AlterCarMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(AlterCarMessageActivity.this, SetCarInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(AlterCarMessageActivity.this, "网络异常,请重新加载", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
