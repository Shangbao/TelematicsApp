package com.hangon.weizhang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cheshouye.api.client.WeizhangIntentService;
import com.cheshouye.api.client.json.CityInfoJson;
import com.cheshouye.api.client.json.InputConfigJson;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.hangon.bean.carInfo.WeiZhangInfoVO;
import com.hangon.common.Constants;
import com.hangon.common.JsonUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.home.activity.HomeActivity;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.weizhang.model.CarInfo;

public class MainActivity extends Activity {


    private String defaultChepai = "粤"; // 粤=广东

    private TextView short_name;
    private TextView query_city;
    private View btn_cpsz;
    private Button btn_query;

    private EditText chepai_number;
    private EditText chejia_number;
    private EditText engine_number;


    // 行驶证图示
    private View popXSZ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.csy_activity_main);
        init();
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.csy_titlebar);
        // 标题
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("车辆违章查询");

        // 返回按钮
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomeActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        short_name.setText(defaultChepai);

        getWeizhangData(Constants.USER_ID+"");
        // ********************************************************
        Log.d("初始化服务代码", "");
        Intent weizhangIntent = new Intent(this, WeizhangIntentService.class);
        weizhangIntent.putExtra("appId", 1642);// 您的appId
        weizhangIntent.putExtra("appKey", "5a9963b57060b40910877ff440273d5b");// 您的appKey
        startService(weizhangIntent);
        // ********************************************************

        // 选择省份缩写
        /*
        query_city = (TextView) findViewById(R.id.cx_city);
		chepai_number = (EditText) findViewById(R.id.chepai_number);
		chejia_number = (EditText) findViewById(R.id.chejia_number);
		engine_number = (EditText) findViewById(R.id.engine_number);
		short_name = (TextView) findViewById(R.id.chepai_sz);

		// ----------------------------------------------

		btn_cpsz = (View) findViewById(R.id.btn_cpsz);
		btn_query = (Button) findViewById(R.id.btn_query);
		*/

        btn_cpsz.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShortNameList.class);
                intent.putExtra("select_short_name", short_name.getText());
                startActivityForResult(intent, 0);
            }
        });

        query_city.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProvinceList.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_query.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 获取违章信息
                CarInfo car = new CarInfo();
                String quertCityStr = null;
                String quertCityIdStr = null;

                final String shortNameStr = short_name.getText().toString()
                        .trim();
                final String chepaiNumberStr = chepai_number.getText()
                        .toString().trim();
                if (query_city.getText() != null
                        && !query_city.getText().equals("")) {
                    quertCityStr = query_city.getText().toString().trim();

                }

                if (query_city.getTag() != null
                        && !query_city.getTag().equals("")) {
                    quertCityIdStr = query_city.getTag().toString().trim();
                    car.setCity_id(Integer.parseInt(quertCityIdStr));
                }
                final String chejiaNumberStr = chejia_number.getText()
                        .toString().trim();
                final String engineNumberStr = engine_number.getText()
                        .toString().trim();

                Intent intent = new Intent();

                car.setChejia_no(chejiaNumberStr);
                car.setChepai_no(shortNameStr + chepaiNumberStr);

                car.setEngine_no(engineNumberStr);
                Bundle bundle = new Bundle();
                bundle.putSerializable("carInfo", car);
                intent.putExtras(bundle);
                boolean result = checkQueryItem(car);

                if (result) {
                    intent.setClass(MainActivity.this, WeizhangResult.class);
                    startActivity(intent);

                }
            }
        });


        // 显示隐藏行驶证图示
        popXSZ = (View) findViewById(R.id.popXSZ);
        popXSZ.setOnTouchListener(new popOnTouchListener());
        hideShowXSZ();
    }

    //获取违章信息数据
    private void getWeizhangData(String userId) {
        String url = Constants.GET_WEIZHANG_INFO_URL + "userId=" + userId;
        VolleyRequest.RequestGet(MainActivity.this, url, "getWeizhangData", new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("getWeizhangData--result", result);
                if (!result.equals("error")) {
                    Gson gson = new Gson();
                    WeiZhangInfoVO weiZhangInfoVO = (WeiZhangInfoVO) JsonUtil.jsonToBean(result, WeiZhangInfoVO.class);
                    defaultChepai = (Constants.PROVINCE_VALUE.charAt(weiZhangInfoVO.getProvinceIndex()) + "").trim();
                    Log.e("defaultChepai", defaultChepai);
                    short_name.setText(defaultChepai);
                    chepai_number.setText(weiZhangInfoVO.getCarLicenceTail());
                    chejia_number.setText(weiZhangInfoVO.getChassisNum());
                    engine_number.setText(weiZhangInfoVO.getEngineNum());
                    CarInfo car = new CarInfo();
                } else {
                    Toast.makeText(MainActivity.this, "车主没有设置默认车辆，请手动填写。", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        // 选择省份缩写
        query_city = (TextView) findViewById(R.id.cx_city);
        chepai_number = (EditText) findViewById(R.id.chepai_number);
        chejia_number = (EditText) findViewById(R.id.chejia_number);
        engine_number = (EditText) findViewById(R.id.engine_number);
        short_name = (TextView) findViewById(R.id.chepai_sz);

        // ----------------------------------------------

        btn_cpsz = (View) findViewById(R.id.btn_cpsz);
        btn_query = (Button) findViewById(R.id.btn_query);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case 0:
                Bundle bundle = data.getExtras();
                String ShortName = bundle.getString("short_name");
                short_name.setText(ShortName);
                break;
            case 1:
                Bundle bundle1 = data.getExtras();
                // String cityName = bundle1.getString("city_name");
                String cityId = bundle1.getString("city_id");
                // query_city.setText(cityName);
                // query_city.setTag(cityId);
                // InputConfigJson inputConfig =
                // WeizhangClient.getInputConfig(Integer.parseInt(cityId));
                // System.out.println(inputConfig.toJson());
                setQueryItem(Integer.parseInt(cityId));

                break;
        }
    }

    // 根据城市的配置设置查询项目
    private void setQueryItem(int cityId) {

        InputConfigJson cityConfig = WeizhangClient.getInputConfig(cityId);

        // 没有初始化完成的时候;
        if (cityConfig != null) {
            CityInfoJson city = WeizhangClient.getCity(cityId);

            query_city.setText(city.getCity_name());
            query_city.setTag(cityId);

            int len_chejia = cityConfig.getClassno();
            int len_engine = cityConfig.getEngineno();

            View row_chejia = (View) findViewById(R.id.row_chejia);
            View row_engine = (View) findViewById(R.id.row_engine);

            // 车架号
            if (len_chejia == 0) {
                row_chejia.setVisibility(View.GONE);
            } else {
                row_chejia.setVisibility(View.VISIBLE);
                setMaxlength(chejia_number, len_chejia);
                if (len_chejia == -1) {
                    chejia_number.setHint("请输入完整车架号");
                } else if (len_chejia > 0) {
                    chejia_number.setHint("请输入车架号后" + len_chejia + "位");
                }
            }

            // 发动机号
            if (len_engine == 0) {
                row_engine.setVisibility(View.GONE);
            } else {
                row_engine.setVisibility(View.VISIBLE);
                setMaxlength(engine_number, len_engine);
                if (len_engine == -1) {
                    engine_number.setHint("请输入完整车发动机号");
                } else if (len_engine > 0) {
                    engine_number.setHint("请输入发动机后" + len_engine + "位");
                }
            }
        }
    }

    // 提交表单检测
    private boolean checkQueryItem(CarInfo car) {
        if (car.getCity_id() == 0) {
            Toast.makeText(MainActivity.this, "请选择查询地", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (car.getChepai_no().length() != 7) {
            Toast.makeText(MainActivity.this, "您输入的车牌号有误", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (car.getCity_id() > 0) {
            InputConfigJson inputConfig = WeizhangClient.getInputConfig(car
                    .getCity_id());
            int engineno = inputConfig.getEngineno();
            int registno = inputConfig.getRegistno();
            int classno = inputConfig.getClassno();

            // 车架号
            if (classno > 0) {
                if (car.getChejia_no().equals("")) {
                    Toast.makeText(MainActivity.this, "输入车架号不为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (car.getChejia_no().length() != classno) {
                    Toast.makeText(MainActivity.this, "输入车架号后" + classno + "位",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (classno < 0) {
                if (car.getChejia_no().length() == 0) {
                    Toast.makeText(MainActivity.this, "输入全部车架号", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            //发动机
            if (engineno > 0) {
                if (car.getEngine_no().equals("")) {
                    Toast.makeText(MainActivity.this, "输入发动机号不为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (car.getEngine_no().length() != engineno) {
                    Toast.makeText(MainActivity.this,
                            "输入发动机号后" + engineno + "位", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (engineno < 0) {
                if (car.getEngine_no().length() == 0) {
                    Toast.makeText(MainActivity.this, "输入全部发动机号", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            // 注册证书编号
            if (registno > 0) {
                if (car.getRegister_no().equals("")) {
                    Toast.makeText(MainActivity.this, "输入证书编号不为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (car.getRegister_no().length() != registno) {
                    Toast.makeText(MainActivity.this,
                            "输入证书编号后" + registno + "位", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (registno < 0) {
                if (car.getRegister_no().length() == 0) {
                    Toast.makeText(MainActivity.this, "输入全部证书编号", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    // 设置/取消最大长度限制
    private void setMaxlength(EditText et, int maxLength) {
        if (maxLength > 0) {
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    maxLength)});
        } else { // 不限制
            et.setFilters(new InputFilter[]{});
        }
    }

    // 显示隐藏行驶证图示
    private void hideShowXSZ() {
        View btn_help1 = (View) findViewById(R.id.ico_chejia);
        View btn_help2 = (View) findViewById(R.id.ico_engine);
        Button btn_closeXSZ = (Button) findViewById(R.id.btn_closeXSZ);

        btn_help1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.VISIBLE);
            }
        });
        btn_help2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.VISIBLE);
            }
        });
        btn_closeXSZ.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.GONE);
            }
        });
    }

    // 避免穿透导致表单元素取得焦点
    private class popOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            popXSZ.setVisibility(View.GONE);
            return true;
        }
    }

}
