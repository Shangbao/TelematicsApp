package com.hangon.map.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.fd.ourapplication.R;
import com.hangon.saying.util.Location;

import java.util.List;

/**
 * Created by mykonons on 2016/8/2.
 */
public class TestPoi extends Activity  implements OnGetPoiSearchResultListener,OnGetSuggestionResultListener {
    private TextView cityAddress;
    /**
     * POI检索
     */
    List<PoiInfo> listpoiinfo;
    List<PoiDetailResult> listPoidetailinfo;
    private PoiSearch mPoiSearch = null;
    private ViewHolder viewHolder;

   private ListView poiList;

    private SuggestionSearch mSuggestionSearch = null;//检索建议
    List<SuggestionResult.SuggestionInfo> suggestionsInfoList=null;
    private String gasaddress;//接受加油站地址
    private LocationClient mLocClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testpoi);

        poiList=(ListView)findViewById(R.id.testpoi);
        mLocClient = ((Location) getApplication()).mLocationClient;
        cityAddress=new TextView(getApplicationContext());
        ((Location) getApplication()).mTv =cityAddress;
        setLocationOption();
        mLocClient.start();
        Toast.makeText(getApplicationContext(), cityAddress.getText().toString(), Toast.LENGTH_SHORT).show();



        /**
         * 初始化搜索模块，注册事件监听
         */

        mPoiSearch = PoiSearch.newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        /**
         * 监听结果回调
         */

        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  Thread.sleep(2000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              init();
          }
      }).start();
    }

    private void init() {

        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword("长春").city("加油站"));
        mPoiSearch.searchInCity((new PoiCitySearchOption()).city("长春")
                .keyword("加油站")
                .pageNum(20));

//          PoiDetailSearchOption poiDetailSearchOption=new PoiDetailSearchOption();
//
//          poiDetailSearchOption.poiUid("095a14d5c4927d21cae33c86");
//           mPoiSearch.searchPoiDetail(poiDetailSearchOption);
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
        mLocClient.setLocOption(option);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
          Toast.makeText(getApplicationContext(),"aaaaa",Toast.LENGTH_SHORT).show();

        if (poiResult == null
                || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(getApplicationContext(), "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {

            poiResult.getAllPoi();
            listpoiinfo = poiResult.getAllPoi();
            Toast.makeText(getApplicationContext(),poiResult.getTotalPoiNum()+"aaaaa",Toast.LENGTH_SHORT).show();
            TestAdapter adapter=new TestAdapter(listpoiinfo);
            poiList.setAdapter(adapter);
            if (listpoiinfo != null && listpoiinfo.size() != 0) {
                for (PoiInfo poiInfo : listpoiinfo) {


                }

    }}}

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
       //listPoidetailinfo= (List<PoiDetailResult>) poiDetailResult;
        Toast.makeText(getApplicationContext(),poiDetailResult.getName()+"==="+poiDetailResult.getTelephone(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }
    class TestAdapter extends BaseAdapter{
        private List<PoiInfo> list;

        TestAdapter(List<PoiInfo> list){
                  this.list=list;
              }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.maintancelist,null);
                viewHolder.distancepoi=(TextView)convertView.findViewById(R.id.maintenanceshop_distance);
                viewHolder.namepoi=(TextView)convertView.findViewById(R.id.main_tainshop_name);
                viewHolder.address=(TextView)convertView.findViewById(R.id.main_shop_address);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.namepoi.setText(list.get(position).name.toString());
            LatLng latLng=list.get(position).location;
            LatLng latLng1=list.get(1).location;
            viewHolder.distancepoi.setText(list.get(position).uid);
            viewHolder.address.setText(list.get(position).address);
            return convertView;
        }
    }
    class ViewHolder{

        private TextView distancepoi;
        private  TextView namepoi;
        private TextView address;
    }
    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();


        super.onDestroy();
    }
}
