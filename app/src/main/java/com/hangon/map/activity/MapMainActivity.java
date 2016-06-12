package com.hangon.map.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.example.fd.ourapplication.R;
import com.hangon.bean.map.Datas;
import com.hangon.home.activity.HomeActivity;
import com.hangon.map.util.AnimAsyncTask;
import com.hangon.map.util.GasInfoUtil;
import com.hangon.map.util.IOExceptionHandle;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.activity.MainOrderActivity;
import com.hangon.saying.viewPager.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/23.
 */
public class MapMainActivity extends Activity implements View.OnClickListener, BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener, OnGetGeoCoderResultListener {
    //topbar
    private ImageButton topLeft;
    private ImageButton topRight;
    private TextView topTittle;
    int position = 0;//对应的覆盖物点击标识

    /**
     * 对应类
     */
//    AnimAsyncTask asyncTask;
    GasInfoAdpter adapter;
    JudgeNet judgeNet;


    // 导航参数
    NaviParaOption para;

    // 地理编码
    GeoCoder mGeoCoder = null;
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    // 定位相关
    private static String myLocationPosition = "";
    private MyLocationListener mLocationListener;
    private MyLocationData mMylocationData;
    private LocationClient mLocationClient;
    private MyLocationConfiguration.LocationMode mLocationMode;
    private boolean isFirstIn = true;

    // 导航经纬度
    private double mLatitude;
    private double mLongtitude;
    private double startLatitude;
    private double startLongtutude;
    private double endLatutude;
    private double endLongtitude;

    // 页面组件相关
    private Button road_condition;
    private ImageView location_position;
    private LinearLayout DisplayListButton;
    private TextView show_hideText;
    private ImageView route_search;

    LinearLayout btnOrder;//订单管理
    LinearLayout btnGasStation;//周围加油站

    // 起始地址接收
    private static String start_position = "";
    private static String end_position = "";
    private static String start, end;

    /**
     * 判断参数
     */
    private static int planstatues = 0;
    private int states = 0;

    // 线路搜索
    private RouteLine route = null;
    OverlayManager routeOverlay = null;
    RoutePlanSearch mSearch = null;
    boolean useDefaultIcon = false;

    /**
     * 覆盖我相关
     */
    private BitmapDescriptor mBitmapDescriptor;
    public ListView gasListview;
    List<Datas> list;
    /**
     * 加油站列表
     */
    List<Map<String, ?>> mGasList;
    Dialog dialog;
//底层布局
    FrameLayout mFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        initLocation();

        judgeNet = new JudgeNet();
        states = judgeNet.getStates();
        mLocationListener = new MyLocationListener();
        initView();
        initfindViewById();
        /*
        asyncTask = new AnimAsyncTask(MapMainActivity.this, "正在加载中...");
        asyncTask.execute();*/

        if (states == 1) {
            mFrameLayout.setVisibility(View.GONE);
            route_search.setVisibility(View.VISIBLE);
           show_hideText.setText("  开始        ");
            topTittle.setText("最优路线");
            receive();
            SearchGeocoder();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        SearchRoutePlan();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            judgeNet.setStates(0);

        }
        if (states == 2) {
            topTittle.setText("周围加油站");
            GasReceiver();
            route_search.setVisibility(View.GONE);
        }
    }

    private void GasReceiver() {
        judgeNet.setStates(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GasInfoUtil.VolleyGet(getApplicationContext());
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addInfosOverlay(GasInfoUtil.gasinfo);
                gasShowinfo(GasInfoUtil.gasinfo);
                if (GasInfoUtil.gasinfo == null) {
                    //重新加载
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.netalert, null);
                    TextView donwload = (TextView) view.findViewById(R.id.donwload);
                    dialog = new Dialog(getApplicationContext());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setView(view);
                    dialog = builder.create();
                    dialog.show();
                    donwload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GasReceiver();
                            dialog.dismiss();

                        }
                    });
                }
                initMarkerClickEvent();
            }

        }).start();
    }


    //初始化组件
    private void initfindViewById() {
        mFrameLayout=(FrameLayout)findViewById(R.id.fragmentlayout);
        show_hideText=(TextView)findViewById(R.id.show_hide_listtext);
        road_condition = (Button) findViewById(R.id.road_cond);
        location_position = (ImageView) findViewById(R.id.location_position);
        gasListview = (ListView) findViewById(R.id.gaslist);
        DisplayListButton = (LinearLayout) findViewById(R.id.show_hide);
        route_search = (ImageView) findViewById(R.id.route_search);
        Maplistener maplistener = new Maplistener();
        road_condition.setOnClickListener(maplistener);
        location_position.setOnClickListener(maplistener);
        DisplayListButton.setOnClickListener(maplistener);
        route_search.setOnClickListener(maplistener);
        btnOrder = (LinearLayout) findViewById(R.id.btnOrder);
        btnGasStation = (LinearLayout) findViewById(R.id.btnZwjyz);
        btnOrder.setOnClickListener(this);
        btnGasStation.setOnClickListener(this);
        Listlistener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOrder:
                Intent intent = new Intent(MapMainActivity.this, MainOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.btnZwjyz:
                break;
        }
    }

    //初始化画面
    private void initView() {
        //topbar组件
        topLeft=(ImageButton)findViewById(R.id.topbar_left);
        topRight=(ImageButton)findViewById(R.id.topbar_right);
        topTittle=(TextView)findViewById(R.id.topbar_title);
        topRight.setVisibility(View.GONE);
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MapMainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setTrafficEnabled(false);
        // 地图点击事件处理
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        // 初始化搜索模块，注册事件监听
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);
        /**
         * 覆盖物图标
         */
        mBitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.zwjyz_07);
    }

    //初始化位置
    private void initLocation() {
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();

        option.setNeedDeviceDirect(true);
        option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
        option.setAddrType("all");
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mLocationClient.setLocOption(option);
        /**
         * 初始化搜索模块，注册事件监听 线路规划初始化
         */
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    //定位
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            myLocationPosition = location.getAddrStr();
            mMylocationData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            mBaiduMap.setMyLocationData(mMylocationData);
            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();



            GasInfoUtil.setLocationlatitude(mLatitude);
            GasInfoUtil.setLocationlongtitude(mLongtitude);
            if (isFirstIn) {
                LatLng latLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.drawable.zwjyz_15);
                OverlayOptions options=new MarkerOptions().position(latLng).icon(bitmapDescriptor);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
            }
        }
    }

    // 定位到我的位置
    private void centerToMyLocation() {
        LatLng latLng = new LatLng(mLatitude, mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    // 起始位置终点位置数据接收
    public void receive() {
        if (states == 0 || judgeNet.getAppointRoute() == 1) {
            Toast.makeText(MapMainActivity.this, "sssss", Toast.LENGTH_LONG).show();

            return;
        } else {
            Bundle bundle = this.getIntent().getExtras().getBundle("address");
            start_position = bundle.getString("start_position");
            end_position = bundle.getString("end_position");
            start = start_position;
            end = end_position;
        }
        startLatitude = judgeNet.getLat();
        startLongtutude = judgeNet.getLon();
    }

    /**
     * 发起路线规划搜索示例
     */
    public void SearchRoutePlan() {
        LatLng start = new LatLng(startLatitude, startLongtutude);
        LatLng end = new LatLng(endLatutude, endLongtitude);
        LatLng myposition = new LatLng(mLatitude, mLongtitude);
        PlanNode statrNode = PlanNode.withLocation(start);
        PlanNode endNode = PlanNode.withLocation(end);
        PlanNode mypositionNode = PlanNode.withLocation(myposition);
        route = null;
        mBaiduMap.clear();
        if (judgeNet.getAppointRoute() == 1) {

            judgeNet.setAppointRoute(0);
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(mypositionNode)
                    .to(endNode));
        } else if ("我的位置".equals(end_position)) {
            // end_position = myLocationPosition;
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(endNode)
                    .to(mypositionNode));
        } else if ("我的位置".equals(start_position)) {
            // start_position = myLocationPosition;

            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
                    mypositionNode).to(endNode));
        } else {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(statrNode).to(endNode));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapMainActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        endLatutude = result.getLocation().latitude;
        endLongtitude = result.getLocation().longitude;

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapMainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
//            AnimAsyncTask.progress = 1;
            show_hideText.setText("");

        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.map_icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.map_icon_en);
            }
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBaiduMap.hideInfoWindow();
        // gasListview.setVisibility(View.GONE);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    /**
     * 覆盖物点击事件监听
     */
    private void initMarkerClickEvent() {

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker maker) {
                final Datas info = (Datas) maker.getExtraInfo().get("info");
                // 生成一个TextView用户在地图中显示InfoWindow
                TextView location = new TextView(getApplicationContext());
                location.setBackgroundResource(R.drawable.map_location_tips);
                location.setPadding(30, 20, 30, 50);
                location.setText(info.getName());
                LatLng ll = maker.getPosition();
                double la = ll.latitude;
                double lo = ll.longitude;
                maker.setPosition(ll);
                if (gasListview.getVisibility() != View.GONE) {
                    adapter.setLat(info.getLat());
                    adapter.item(gasListview);
                    adapter.notifyDataSetChanged();
                    adapter.item1(gasListview);

                }
                for (int i = 0; i < GasInfoUtil.infos.size(); i++) {
                    if (info.getLat() == GasInfoUtil.infos.get(i).getLatitude() &&
                            info.getLon() == GasInfoUtil.infos.get(i).getLongitude()) {
                        position = i;
                    }
                }
                InfoWindow mInfoWindow;
                InfoWindow.OnInfoWindowClickListener mOnInfoWindowClickListener;
                mOnInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("gaslat", info.getLat());
                        bundle.putDouble("gaslon", info.getLon());

                        bundle.putInt("position", position);
                        Intent intent = new Intent();
                        intent.putExtra("mGasList", bundle);
                        intent.setClass(MapMainActivity.this, GasSiteDetailsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory
                        .fromView(location), ll, -47,
                        mOnInfoWindowClickListener);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;

            }
        });
    }

    /**
     * 覆盖物的列表展示
     */
    private void gasShowinfo(List<Datas> infos) {
        mGasList = new ArrayList<>();
        for (Datas info : infos) {
            Map gasmap = new HashMap<>();
            gasmap.put("gaslat", info.getLat());
            gasmap.put("gaslon", info.getLon());
            gasmap.put("gasname", info.getName());
            gasmap.put("gasdistance", info.getDistance());
            gasmap.put("gasaddress", info.getAddress());
            mGasList.add(gasmap);
        }
    }

    private void gasShowlist() {
        String from[] = {"gaslat", "gaslon", "gasaddress", "gasdistance",
                "gasname", "gasbrandname", "gasprice"};
        int to[] = {R.id.gaslist_lat, R.id.gaslist_lon, R.id.gaslist_address,
                R.id.gaslist_distance, R.id.gaslist_name,
                R.id.gaslist_brandname, R.id.gaslist_price};
        adapter = new GasInfoAdpter(MapMainActivity.this, mGasList, R.layout.item_gaslist, from,
                to);
        gasListview.setAdapter(adapter);
    }

    private void Listlistener() {
        gasListview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                v=LayoutInflater.from(MapMainActivity.this).inflate(R.layout.item_gaslist,null);
                TextView gasname = (TextView) v.findViewById(R.id.gaslist_name);
                TextView gasdistance = (TextView) v.findViewById(R.id.gaslist_distance);
                TextView gasaddress = (TextView) v.findViewById(R.id.gaslist_address);
                TextView distancemeter=(TextView)v.findViewById(R.id.distance_meter);
                gasname.setTextColor(Color.argb(255, 13, 141, 13));
                gasaddress.setTextColor(Color.argb(255, 13, 141, 13));
                gasdistance.setTextColor(Color.argb(255, 13, 141, 13));
                distancemeter.setTextColor(Color.argb(255,13,141,13));
            }
        });


        gasListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>) listView
                        .getItemAtPosition(position);
                HashMap<String, Double> map1 = (HashMap<String, Double>) listView
                        .getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                Intent intent = new Intent();
                intent.putExtra("mGasList", bundle);
                intent.setClass(MapMainActivity.this, GasSiteDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 初始化图层
     */
    public void addInfosOverlay(List<Datas> infos) {
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (Datas info : infos) {
            // 位置
            latLng = new LatLng(info.getLat(), info.getLon());
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mBitmapDescriptor).zIndex(1);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        LatLng latLng2 = new LatLng(mLatitude, mLongtitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng2);

        mBaiduMap.setMapStatus(u);

    }

    /**
     * 获取加油站信息
     */
    private void ReceiveGas() {

    }

    /**
     * 发起搜索
     */
    public void SearchGeocoder() {
        if (judgeNet.getAppointRoute() == 1) {
            Bundle bundle = this.getIntent().getBundleExtra("endaddress");
            String Address = bundle.get("endaddress").toString();
            Toast.makeText(MapMainActivity.this,Address,Toast.LENGTH_SHORT).show();
            mGeoCoder.geocode(new GeoCodeOption().city("")
                    .address(Address));
        } else if ("我的位置".equals(start_position)) {
            mGeoCoder.geocode(new GeoCodeOption().city("")
                    .address(end_position));
        } else if ("我的位置".equals(end_position)) {
            mGeoCoder.geocode(new GeoCodeOption().city("").address(
                    start_position));
        } else {
            mGeoCoder.geocode(new GeoCodeOption().city("")
                    .address(end_position));
        }
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        LatLng pt1 = new LatLng(mLatitude, mLongtitude);
        LatLng pt2 = new LatLng(startLatitude, startLongtutude);
        LatLng pt3 = new LatLng(endLatutude, endLongtitude);
        // 构建 导航参数
        if ("我的位置".equals(start)) {
            // Toast.makeText(MapMain.this, , duration)
            para = new NaviParaOption().startPoint(pt1).endPoint(pt2);
        } else if ("我的位置".equals(end)) {
            para = new NaviParaOption().startPoint(pt2).endPoint(pt1);
        } else {
            para = new NaviParaOption().startPoint(pt2).endPoint(pt3);
        }
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            IOExceptionHandle.showDialog(MapMainActivity.this);
        }
    }

    /**
     * @author Administrator 自定义覆盖物以及覆盖物监听事件
     */
    // 监听事件
    private class Maplistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.road_cond:
                    // 开启交通图
                    if (mBaiduMap.isTrafficEnabled()) {
                        mBaiduMap.setTrafficEnabled(false);
                        road_condition
                                .setBackgroundResource(R.drawable.zwjyz_10);
                    } else {
                        mBaiduMap.setTrafficEnabled(true);
                        road_condition
                                .setBackgroundResource(R.drawable.zwjyz_03);
                    }
                    break;
                case R.id.location_position:
                    centerToMyLocation();
                    break;
                case R.id.show_hide:
                    try {
                        if ((show_hideText.getText().toString()).equals("  开始")) {
                            startNavi();
                        } else {
                            gasShowlist();
                            if (gasListview.getVisibility() == View.GONE) {
                                gasListview.setVisibility(View.VISIBLE);
                                show_hideText.setText("隐藏列表");
                            } else if (gasListview.getVisibility() == View.VISIBLE) {
                                gasListview.setVisibility(View.GONE);
                                show_hideText.setText("显示列表");
                            }
                        }

                    } catch (Exception e) {
                    }
                    break;
                case R.id.route_search:
                    Intent intent = new Intent();
                    intent.setClass(MapMainActivity.this, BestRouteActivity.class);
                    startActivity(intent);
                    finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        // 开启方向传感器
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 停止方向传感器
    }

    @Override
    protected void onDestroy() {
       /* asyncTask.stopProgressDialog();
        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
        }*/
        mMapView.onDestroy();
        super.onDestroy();
        mBitmapDescriptor.recycle();
        BaiduMapRoutePlan.finish(this);
        BaiduMapPoiSearch.finish(this);
    }

}
