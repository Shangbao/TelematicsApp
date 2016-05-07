package com.hangon.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.Weather;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chuan on 2016/5/5.
 */
public class WeatherActivity extends Activity implements APICallback,View.OnClickListener {

    private String ip;

    private TextView tvCity;
    private TextView tvUpdateTime;
    private TextView tvTemperature;
    private TextView tvWind;
    private TextView tvDate;
    private TextView tvCurrentTemperature;
    private TextView tvCurrentWeather;

    public static Weather context;

    private LinearLayout weatherBg;
    private LinearLayout titleBarLayout;
    private LinearLayout currentWeatherLayout;

    private ListView weatherForcastList;

    private ArrayList<HashMap<String, Object>> weeks;
    private ArrayList<HashMap<String, Object>> results;
    private ArrayList<com.hangon.weather.Weather> weathers;

    private Intent intent;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weather);
        init();
        MobAPI.initSDK(this, "120b650027878");
        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
        api.getSupportedCities(this);

        intent = getIntent();
        if(city == null){
            new Thread(){
                public void run() {
                    ip = null;
                    try {
                        NetworkHelper network = new NetworkHelper();
                        ArrayList<KVPair<String>> values = new ArrayList<KVPair<String>>();
                        values.add(new KVPair<String>("ie", "utf-8"));
                        String resp = network.httpGet("http://pv.sohu.com/cityjson", values, null, null);
                        resp = resp.replace("var returnCitySN = {", "{").replace("};", "}");
                        ip = (String) (new Hashon().fromJson(resp).get("cip"));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }finally {
                        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
                        api.queryByIPAddress(ip, WeatherActivity.this);
                    }
                }

            }.start();
        }
    }

    private void initWeather(){
        for(HashMap<String,Object> week : weeks){
            com.hangon.weather.Weather weather = new com.hangon.weather.Weather(com.mob.tools.utils.R.toString(week.get("week")),
                    com.mob.tools.utils.R.toString(week.get("dayTime")),
                    com.mob.tools.utils.R.toString(week.get("temperature")),
                    com.mob.tools.utils.R.toString(week.get("wind")));
            weathers.add(weather);
        }
    }

    private void onWeatherDetailsGot(Map<String, Object> result){
        results = (ArrayList<HashMap<String, Object>>) result.get("result");
        HashMap<String, Object> weather = results.get(0);

        weathers = new ArrayList<com.hangon.weather.Weather>();

        weeks = (ArrayList<HashMap<String,Object>>) weather.get("future");
        HashMap<String, Object> week = weeks.get(0);
        tvCity.setText(com.mob.tools.utils.R.toString(weather.get("city")));
        tvCurrentTemperature.setText(com.mob.tools.utils.R.toString(weather.get("temperature")));
        tvCurrentWeather.setText(com.mob.tools.utils.R.toString(weather.get("weather")));
        String time = com.mob.tools.utils.R.toString(weather.get("updateTime"));
        String date = "今日 ";
        String upTime = time.substring(8, 10) + ":" + time.substring(10, 12);
        tvUpdateTime.setText(date + " " + upTime);
        tvTemperature.setText(com.mob.tools.utils.R.toString(week.get("temperature")));
        tvWind.setText(com.mob.tools.utils.R.toString(weather.get("wind")));
        tvDate.setText(time.substring(4, 6) + "/" + time.substring(6, 8) + " " + com.mob.tools.utils.R.toString(weather.get("week")));
        initWeather();
        WeatherAdapter adapter = new WeatherAdapter(WeatherActivity.this,R.layout.weather_forecast_item,weathers);
        weatherForcastList.setAdapter(adapter);
    }

    private void init(){
        tvCity = (TextView) findViewById(R.id.city);
        tvUpdateTime = (TextView) findViewById(R.id.update_time);
        tvTemperature = (TextView) findViewById(R.id.temperature);
        tvCurrentTemperature = (TextView) findViewById(R.id.current_temperature);
        tvCurrentWeather = (TextView) findViewById(R.id.current_weather);

        tvWind = (TextView) findViewById(R.id.wind);
        tvDate = (TextView) findViewById(R.id.date);
        weatherBg = (LinearLayout) findViewById(R.id.weather_bg);
        weatherForcastList = (ListView) findViewById(R.id.weather_forcast_list);
        tvCity.setOnClickListener(this);
    }

    @Override
    public void onSuccess(API api, int action, Map<String, Object> result) {
        switch (action) {
            case Weather.ACTION_IP: onWeatherDetailsGot(result); break;
            case Weather.ACTION_QUERY: onWeatherDetailsGot(result); break;
        }
    }

    @Override
    public void onError(API api, int action, Throwable details) {
        details.printStackTrace();
        Toast.makeText(this, "亲，查询不到你所要的城市天气！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city:
                intent = new Intent();
                intent.setClass(WeatherActivity.this, SelectCity.class);
                WeatherActivity.this.startActivityForResult(intent, 1);
                break;
            case R.id.share:
                break;
            case R.id.about:
                break;
            case R.id.refresh:
                break;
        }
    }

    /**
     * 设置布局的高度（铺满屏幕）
     */
    private void setCurrentWeatherLayoutHight() {
        // 通知栏高度
        int statusBarHeight = 0;
        try {
            statusBarHeight = getResources().getDimensionPixelSize(
                    Integer.parseInt(Class
                            .forName("com.android.internal.R$dimen")
                            .getField("status_bar_height")
                            .get(Class.forName("com.android.internal.R$dimen")
                                    .newInstance()).toString()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // 屏幕高度
        int displayHeight = ((WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getHeight();
        // title bar LinearLayout高度
        titleBarLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int titleBarHeight = titleBarLayout.getMeasuredHeight();

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) currentWeatherLayout
                .getLayoutParams();
        linearParams.height = displayHeight - statusBarHeight - titleBarHeight;
        currentWeatherLayout.setLayoutParams(linearParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    city = data.getStringExtra("city");
                    Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
                    api.queryByCityName(city, this);
                }
                break;
            default:
        }
    }
}
