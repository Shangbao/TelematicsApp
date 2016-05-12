package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.carInfo.BrandTypeVO;
import com.hangon.bean.carInfo.BrandVO;
import com.hangon.bean.carInfo.CarMessageVO;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.JsonUtil;
import com.hangon.common.MyApplication;
import com.hangon.common.Topbar;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chuan on 2016/4/15.
 */
public class AddCarMessageActivity extends Activity {
    Gson gson;//json数据解析
    List<BrandVO> brandList;//车型号
    List<BrandTypeVO> brandTypeList;//车类型
    CarMessageVO carMessageVO;// 一个车详细信息

    private ImageView carFlag;
    private Spinner car_name_spinner;
    private Spinner car_type_spinner;
    private Spinner province;

    private EditText phone_num;
    private EditText cus_name;

    private EditText car_num_pre;
    private EditText car_num_edit;
    private EditText car_enginenum_edit;
    private EditText car_chassis_number;
    private EditText door_count_edit;
    private EditText seat_count_edit;

    private EditText car_mileage_edit;
    private EditText car_gas_edit;
    private Spinner engine_spinner;
    private Spinner trans_spinner;
    private Spinner light_spinner;

    Topbar topbar;


    private List<String> carName = null;
    private List<String> carType = null;

    private ArrayAdapter<String> carNameAdapter;
    private ArrayAdapter<String> carTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_message);
        init();
        //获得车品牌数据
        getBrandInfoList();//获取车型号数据
        setSpinnerListener();//为车型号设置监听事件

       //标题栏
        topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
             Intent intent=new Intent(AddCarMessageActivity.this,SetCarInfoActivity.class);
                startActivity(intent);
                AddCarMessageActivity.this.finish();
            }

            @Override
            public void rightClick() {
                DialogTool.createNormalDialog(AddCarMessageActivity.this, "保存车辆信息", "你确定要保存吗?", "取消", "确认",null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (false == judge()) {
                            Toast.makeText(AddCarMessageActivity.this, "亲，请完整填写信息！", Toast.LENGTH_SHORT).show();
                        } else {
                            getData();
                            addCarInfo();
                        }
                    }
                }).show();
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

    //初始化组件
    private void init(){
        car_name_spinner = (Spinner) findViewById(R.id.car_name_spinner);
        car_type_spinner = (Spinner) findViewById(R.id.car_type_spinner);
        carFlag= (ImageView) findViewById(R.id.carFlag);
        province = (Spinner) findViewById(R.id.car_province);
        car_num_pre = (EditText) findViewById(R.id.pre);
        car_num_edit = (EditText) findViewById(R.id.car_number);

        phone_num= (EditText) findViewById(R.id.phone_num);
        cus_name= (EditText) findViewById(R.id.cus_name);

        car_enginenum_edit = (EditText) findViewById(R.id.car_engine_number);
        car_chassis_number= (EditText) findViewById(R.id.car_chassis_number);
        door_count_edit = (EditText) findViewById(R.id.door_count);
        seat_count_edit = (EditText) findViewById(R.id.seat_count);

        car_mileage_edit = (EditText) findViewById(R.id.car_mileage);
        car_gas_edit = (EditText) findViewById(R.id.car_gas);
        engine_spinner = (Spinner) findViewById(R.id.engine_is_good);
        trans_spinner = (Spinner) findViewById(R.id.trans_is_good);
        light_spinner = (Spinner) findViewById(R.id.light_is_good);
         topbar= (Topbar) findViewById(R.id.addCarMessageTopbar);

        setRegion(car_gas_edit);
    }

    //为spinner设置监听事件
    private void setSpinnerListener(){
        car_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carType=null;
                carType=new ArrayList<String>();
                getCarFlag(brandList,position);
                for (int i=0;i<brandList.get(position).getBrandTypeList().size();i++){
                    carType.add(brandList.get(position).getBrandTypeList().get(i).getName().toString().trim());
                }
                carTypeAdapter = new ArrayAdapter<String>(AddCarMessageActivity.this,android.R.layout.simple_list_item_multiple_choice,carType);
                car_type_spinner.setAdapter(carTypeAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //获取车图标图片资源
    private void getCarFlag(List<BrandVO> list,int position){
        String url=Constants.CAR_FLAG_URL.trim()+list.get(position).getCarFlag().trim();
        ImageRequest request=new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
           carFlag.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(AddCarMessageActivity.this,"车标志图片加载失败",Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getHttpQueues().add(request);
    }

    //获取数据
    private void getData(){
        carMessageVO=new CarMessageVO();

        carMessageVO.setBrandIndex(car_name_spinner.getSelectedItemPosition());
        carMessageVO.setBrandTypeIndex(car_type_spinner.getSelectedItemPosition());
       carMessageVO.setCarFlag(brandList.get(car_name_spinner.getSelectedItemPosition()).getCarFlag());

        carMessageVO.setPhoneNum(phone_num.getText().toString().trim());
        carMessageVO.setName(cus_name.getText().toString().trim());

        carMessageVO.setProvinceIndex(province.getSelectedItemPosition());
        carMessageVO.setCarLicenceTail(car_num_pre.getText().toString()+car_num_edit.getText().toString().trim());
        carMessageVO.setChassisNum(car_chassis_number.getText().toString().trim());
        carMessageVO.setEngineNum(car_enginenum_edit.getText().toString().trim());

        carMessageVO.setDoorCount(Integer.parseInt(door_count_edit.getText().toString()));
        carMessageVO.setSeatCount(Integer.parseInt(seat_count_edit.getText().toString()));

        carMessageVO.setMileage(Double.parseDouble(car_mileage_edit.getText().toString()));
        carMessageVO.setOddGasAmount(Integer.parseInt(car_gas_edit.getText().toString()));
        carMessageVO.setIsGoodEngine(engine_spinner.getSelectedItem().toString().equals("正常") ?1:0);
        carMessageVO.setIsGoodTran(trans_spinner.getSelectedItem().toString().equals("正常") ?1:0);
        carMessageVO.setIsGoodLight(light_spinner.getSelectedItem().toString().equals("正常")?1:0);

    }

    //对输入框的值判断
    private boolean judge(){
        if(car_num_edit.getText().toString().trim().isEmpty()
                ||car_enginenum_edit.getText().toString().trim().isEmpty()
                ||door_count_edit.getText().toString().trim().isEmpty()
                ||seat_count_edit.getText().toString().trim().isEmpty()
                ||car_mileage_edit.getText().toString().trim().isEmpty()
                ||car_gas_edit.getText().toString().trim().isEmpty()||cus_name.getText().toString().trim().isEmpty()||phone_num.getText().toString().trim().isEmpty())
            return false;
        else
            return true;
    }

    private void addCarInfo(){
        String url = Constants.ADD_CAR_INFO_URL;

        Map<String,Object> map = new HashMap<>();
        map.put("brandIndex",carMessageVO.getBrandIndex()+"");
        map.put("brandTypeIndex",carMessageVO.getBrandTypeIndex()+"");

        map.put("carFlag",carMessageVO.getCarFlag());
        map.put("provinceIndex",carMessageVO.getProvinceIndex()+"");
        map.put("carLicenceTail",carMessageVO.getCarLicenceTail());

        map.put("name",carMessageVO.getName());
        map.put("phoneNum",carMessageVO.getPhoneNum());
        map.put("mileage",carMessageVO.getMileage()+"");
        map.put("chassisNum",carMessageVO.getChassisNum());
        map.put("engineNum",carMessageVO.getEngineNum());

        map.put("oddGasAmount",carMessageVO.getOddGasAmount()+"");
        map.put("isGoodEngine",carMessageVO.getIsGoodEngine()+"");
        map.put("isGoodTran",carMessageVO.getIsGoodTran()+"");
        map.put("isGoodLight",carMessageVO.getIsGoodLight()+"");

        VolleyRequest.RequestPost(this, url, "postUserCarInfo", map, new VolleyInterface(this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Toast.makeText(AddCarMessageActivity.this, "亲，添加完成了哦！", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(AddCarMessageActivity.this,SetCarInfoActivity.class);
                startActivity(intent);
                AddCarMessageActivity.this.finish();
            }

            @Override
            public void onMyError(VolleyError error) {
          Toast.makeText(AddCarMessageActivity.this,"网络异常,请重新加载",Toast.LENGTH_SHORT).show();
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
                if (start > 1) {
                    if (MIN_MARK != -1 && MAX_MARK != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK) {
                            s = String.valueOf(MAX_MARK);
                            et.setText(s);
                        } else if (num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_MARK != -1 && MAX_MARK != -1) {
                        int markVal = 0;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = 0;
                        }
                        if (markVal > MAX_MARK) {
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

        //获得车品牌数据
        public List<BrandVO> getBrandInfoList(){
            String url=Constants.GET_BRAND_INFO_URL;
            VolleyRequest.RequestGet(AddCarMessageActivity.this, url, "getBrandInfoList", new VolleyInterface(AddCarMessageActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
                @Override
                public void onMySuccess(String result) {
                    gson=new Gson();
                   brandList=new ArrayList<BrandVO>();
                    brandList= gson.fromJson(result,new TypeToken<List<BrandVO>>(){}.getType());
                    setAdapter(brandList,0);
                    getCarFlag(brandList,0);
                    Log.e("getBrandInfoList",result);
                }

                @Override
                public void onMyError(VolleyError error) {
                 Toast.makeText(AddCarMessageActivity.this,"网络异常，请重试加载",Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }


}
