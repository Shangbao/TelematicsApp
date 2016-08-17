package com.hangon.map.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.SuggestionsInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.fd.ourapplication.R;
import com.hangon.common.Topbar;
import com.hangon.home.activity.HomeActivity;
import com.hangon.map.util.JudgeNet;
import com.hangon.map.util.NetReceiver;
import com.hangon.saying.util.Location;

import java.util.List;

/**
 * Created by Administrator on 2016/4/23.
 */
public class BestRouteActivity extends Activity implements
        OnGetGeoCoderResultListener, OnGetPoiSearchResultListener,
        OnGetSuggestionResultListener {
    //地址
    private TextView cityAddress;
    //topbar
    private ImageView topLeft;
    private ImageView topRight;
    private TextView topTittle;
    AutoCompleteTextView startPosition;//起点
    AutoCompleteTextView endPosition;//终点
    ImageView siteSwap;//地址互换
    TextView queryStart;//发起路线查询按钮
    private String text;//???
    GeoCoder mGeoCoder = null;//地理编码
    JudgeNet judgeNet;//判断网络函数
    double lat,lon;
    /**
     * POI检索
     */
    List<PoiInfo> listpoiinfo;
    List<CityInfo> listcityinfo;
    private PoiSearch mPoiSearch = null;



    /**
     * 搜索列表
     */
    private PoiSearchAdapter adapter;
    ListView searchPositionList;
    private int load_Index = 0;
    private static StringBuilder sb;

    private SuggestionSearch mSuggestionSearch = null;//检索建议
    List<SuggestionResult.SuggestionInfo> suggestionsInfoList=null;

    private String gasaddress;//接受加油站地址

    /**
     * 判断网络是否可用
     */
    NetReceiver mReceiver;
    IntentFilter mFilter;

    /**
     * 判断网络状态参数
     */
    String judgeNetState;
    private LocationClient mLocClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_best_way);
        mLocClient = ((Location) getApplication()).mLocationClient;
        ((Location) getApplication()).mTv =cityAddress;
        setLocationOption();
        mLocClient.start();
        mReceiver = new NetReceiver();
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
        init();
        judgeNet = new JudgeNet();
        /**
         * 初始化搜索模块，注册事件监听
         */
        mGeoCoder = GeoCoder.newInstance();
        mPoiSearch = PoiSearch.newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        /**
         * 监听结果回调
         */
        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        receiver();

    }

    /**
     * 接受数据
     */
    private void receiver() {
        if (judgeNet.getStates() == 3) {
            Bundle bundle = this.getIntent().getExtras()
                    .getBundle("endaddress");
            gasaddress = bundle.getString("endaddress");
            endPosition.setText(gasaddress);
        }
    }
    //初始化组件
    private void init() {
        cityAddress=(TextView)findViewById(R.id.cityaddress);
        topLeft=(ImageView)findViewById(R.id.topbar_left);
        topRight=(ImageView)findViewById(R.id.topbar_right);
        topTittle=(TextView)findViewById(R.id.topbar_title);
       topRight.setImageResource(R.drawable.lj_lx_004);
        startPosition = (AutoCompleteTextView) findViewById(R.id.start_position);
        endPosition = (AutoCompleteTextView) findViewById(R.id.end_position);
        siteSwap = (ImageView) findViewById(R.id.site_swap);
        searchPositionList = (ListView) findViewById(R.id.searchPositionList);
        RouteListener routeListener = new RouteListener();
        PoiSearchListenerEnd end = new PoiSearchListenerEnd();
        PoiSearchListenerStart start = new PoiSearchListenerStart();
        PoiSearchListenerList liststart = new PoiSearchListenerList();
        siteSwap.setOnClickListener(routeListener);
        startPosition.addTextChangedListener(start);
        endPosition.addTextChangedListener(end);
        siteSwap.setOnClickListener(routeListener);
        searchPositionList.setOnItemClickListener(liststart);
        queryStart();
    }
    private void queryStart() {
        topTittle.setText("最优路线");
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeNetState = mReceiver.getNetType();
                if (judgeNetState.equals("mobilenet") || judgeNetState.equals("wifinet")) {
                    judgeNet.setStates(1);
                    SearchGeocoder();
                    if ("".equals(startPosition.getText().toString().trim())
                            || "".equals(endPosition.getText().toString().trim())) {
                        Toast.makeText(BestRouteActivity.this, "请输入起始点",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
//                                try {
//                                    Thread.sleep(1300);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                Bundle addressdata = new Bundle();
                                addressdata.putString("start_position",
                                        startPosition.getText().toString().trim());
                                addressdata.putString("end_position", endPosition
                                        .getText().toString().trim());
                                Intent intent = new Intent();
                                intent.putExtra("address", addressdata);
                                intent.setClass(BestRouteActivity.this, MapMainActivity.class);
                                startActivity(intent);
                            }
                        }).start();
                    }
                } else {
                    Toast.makeText(BestRouteActivity.this, "抱歉，当前无网络可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class RouteListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String address = startPosition.getText().toString();
            startPosition.setText(endPosition.getText().toString());
            endPosition.setText(address);
        }
    }

    public class PoiSearchListenerStart implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(s.length()<=0) return;
            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(cityAddress.getText().toString()));
//            mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityAddress.getText().toString())
//                    .keyword(startPosition.getText().toString())
//                    .pageNum(load_Index));
//            sb = new StringBuilder();
            searchPositionList.setVisibility(View.VISIBLE);
        }
    }

    public class PoiSearchListenerEnd implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(s.length()<=0) return;
            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(cityAddress.getText().toString()));


//            mPoiSearch.searchInCity((new PoiCitySearchOption())
//                    .city(cityAddress.getText().toString())
//                    .keyword(endPosition.getText().toString())
//                    .pageNum(load_Index));
//
//            sb = new StringBuilder();
            searchPositionList.setVisibility(View.VISIBLE);
        }
    }

    public class PoiSearchListenerList implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            try {
                if (startPosition.hasFocus()) {
                   startPosition.setText(suggestionsInfoList.get(position).key+" "+suggestionsInfoList.get(position).city+suggestionsInfoList.get(position).district);

                } else if (endPosition.hasFocus()) {
                    endPosition.setText(suggestionsInfoList.get(position).key+" "+suggestionsInfoList.get(position).city+suggestionsInfoList.get(position).district);

                }
            } catch (StringIndexOutOfBoundsException e) {

            }
            searchPositionList.setVisibility(View.GONE);
        }
    }

    /**
     * 发起搜索
     */
    public void SearchGeocoder() {
        if ("我的位置".equals(startPosition.getText().toString())) {

            mGeoCoder.geocode(new GeoCodeOption().city("").address(
                    endPosition.getText().toString()));
        } else if ("我的位置".equals(endPosition.getText().toString())) {
            mGeoCoder.geocode(new GeoCodeOption().city("").address(
                    startPosition.getText().toString()));
        } else {
            mGeoCoder.geocode(new GeoCodeOption().city("").address(
                    startPosition.getText().toString()));
        }
    }

    //地理编码
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        // Toast.makeText(RouteQuery.this, strInfo, Toast.LENGTH_LONG).show();
        judgeNet.setLat(result.getLocation().latitude);
        judgeNet.setLon(result.getLocation().longitude);
    }

    //反地理编码
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        suggestionsInfoList=suggestionResult.getAllSuggestions();
        adapter = new PoiSearchAdapter(this, suggestionsInfoList);
        searchPositionList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
            }
        }
    }

    //poi详情结果
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BestRouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null
                || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            //Toast.makeText(RouteQuery.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            poiResult.getAllPoi();
            listpoiinfo = poiResult.getAllPoi();
            if (listpoiinfo != null && listpoiinfo.size() != 0) {
                for (PoiInfo poiInfo : listpoiinfo) {

                }

            }
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            listcityinfo = poiResult.getSuggestCityList();
            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mGeoCoder.destroy();
        super.onDestroy();
    }
    //获取当前位置
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        //option.setPoiExtraInfo(true);
        if (true) {
            option.setAddrType("all");
        }
        // option.setScanSpan(3000);
        //  option.setPoiNumber(10);
        //option.disableCache(true);
        mLocClient.setLocOption(option);
    }
    /**
     * 所在位置适配器
     */
    public class PoiSearchAdapter extends BaseAdapter {
        private Context context;
        private List<SuggestionResult.SuggestionInfo> list;
        private ViewHolder holder;


        public PoiSearchAdapter(Context context, List<SuggestionResult.SuggestionInfo> appGroup) {
            this.context = context;
            this.list = appGroup;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int location) {
            return list.get(location);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        public void addObject(List<SuggestionResult.SuggestionInfo> mAppGroup) {
            this.list = mAppGroup;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_activity_poi_search, null);
                holder.mpoi_name = (TextView) convertView
                        .findViewById(R.id.mpoiNameT);
                holder.mpoi_address = (TextView) convertView
                        .findViewById(R.id.mpoiAddressT);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mpoi_name.setText(list.get(position).key);
            holder.mpoi_address.setText(list.get(position).city+list.get(position).district);

            return convertView;
        }

        public class ViewHolder {
            public TextView mpoi_name;// 名称
            public TextView mpoi_address;// 地址
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
