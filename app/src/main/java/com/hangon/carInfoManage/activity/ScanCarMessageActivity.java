package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.carInfo.BrandVO;
import com.hangon.bean.carInfo.CarMessageVO;
import com.hangon.common.Constants;
import com.hangon.common.MyApplication;
import com.hangon.common.Topbar;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ScanCarMessageActivity extends Activity {
    Gson gson;//数据解析器
    private TextView car_name_spinner;// 车品牌
    private TextView car_type_spinner;//车品牌类型
    private ImageView carFlag;//车标志

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

    ImageView topbarLeft,topbarRight;
    TextView topbarTitle;

    CarMessageVO carMessageVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_car_message);
        Intent intent = getIntent();
        carMessageVO = (CarMessageVO) intent.getSerializableExtra("carMessage");
        init();
        setValue();
        getBrandInfoList();
    }

    private void init() {
        car_name_spinner = (TextView) findViewById(R.id.car_name_spinner);// 车品牌
        car_type_spinner = (TextView) findViewById(R.id.car_type_spinner);//车品牌类型
        carFlag = (ImageView) findViewById(R.id.carFlag);

        car_plate_num = (TextView) findViewById(R.id.car_plate_num);//车牌号
        car_enginenum_edit = (TextView) findViewById(R.id.car_engine_number);//车的引擎号
        car_degree = (TextView) findViewById(R.id.car_degree);//车身级别
        car_chassis_number = (TextView) findViewById(R.id.car_chassis_number);

        car_mileage = (TextView) findViewById(R.id.car_mileage);//车行驶的公里数
        car_gas = (TextView) findViewById(R.id.car_gas);//车汽油剩余量
        engine_is_good = (TextView) findViewById(R.id.engine_is_good);//车引擎的状况
        trans_is_good = (TextView) findViewById(R.id.trans_is_good);//车变速器的状况
        light_is_good = (TextView) findViewById(R.id.light_is_good);//车灯的状况

        state = (TextView) findViewById(R.id.state);

        topbarLeft= (ImageView) findViewById(R.id.topbar_left);
        topbarRight= (ImageView) findViewById(R.id.topbar_right);
        topbarTitle= (TextView) findViewById(R.id.topbar_title);
        topbarTitle.setText("查看车辆信息");

        topbarRight.setVisibility(View.GONE);
        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanCarMessageActivity.this, SetCarInfoActivity.class);
                startActivity(intent);
                ScanCarMessageActivity.this.finish();
            }
        });
    }

    private void setValue() {
        car_plate_num.setText(Constants.PROVINCE_VALUE.charAt(carMessageVO.getProvinceIndex()) + carMessageVO.getCarLicenceTail());
        car_enginenum_edit.setText(carMessageVO.getEngineNum());
        car_chassis_number.setText(carMessageVO.getChassisNum());
        getCarFlag(carMessageVO.getCarFlag().trim());
        car_degree.setText(carMessageVO.getDoorCount() + "门" + carMessageVO.getSeatCount() + "座");

        car_mileage.setText(carMessageVO.getMileage() + "");
        car_gas.setText(carMessageVO.getOddGasAmount() + "");

        if (carMessageVO.getIsGoodEngine() == 1) {
            engine_is_good.setText("正常");
        } else {
            engine_is_good.setText("异常");
        }

        if (carMessageVO.getIsGoodTran() == 1) {
            trans_is_good.setText("正常");
        } else {
            trans_is_good.setText("异常");
        }

        if (carMessageVO.getIsGoodLight() == 1) {
            light_is_good.setText("正常");
        } else {
            light_is_good.setText("异常");
        }

        if (carMessageVO.getState() == 1) {
            state.setText("默认使用车辆");
        } else {
            state.setText("非常用车辆");
        }
    }

    private void getCarFlag(String iconUrl) {
        String url = Constants.CAR_FLAG_URL + iconUrl;
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                carFlag.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ScanCarMessageActivity.this, "网络异常，车标志加载失败", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getHttpQueues().add(request);
    }

    //获得车品牌数据
    public void getBrandInfoList() {
        String url = Constants.GET_BRAND_INFO_URL;
        VolleyRequest.RequestGet(ScanCarMessageActivity.this, url, "getBrandInfoList", new VolleyInterface(ScanCarMessageActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                gson = new Gson();
                List<BrandVO> list = new ArrayList<BrandVO>();
                list = gson.fromJson(result, new TypeToken<List<BrandVO>>() {
                }.getType());

                car_name_spinner.setText(list.get(carMessageVO.getBrandIndex()).getBrand());
                car_type_spinner.setText(list.get(carMessageVO.getBrandIndex()).getBrandTypeList().get(carMessageVO.getBrandTypeIndex()).getName());
                Log.e("getBrandInfoList", result);
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(ScanCarMessageActivity.this, "网络异常，请重试加载", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
