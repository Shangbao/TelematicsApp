package com.hangon.map.util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hangon.bean.map.Datas;
import com.hangon.bean.map.Gastprice;
import com.hangon.bean.map.Price;
import com.hangon.bean.map.Status;
import com.hangon.common.MyApplication;
import com.hangon.common.MyStringRequest;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/4/23.
 */
public class GasInfoUtil implements Serializable {
    private static final long serialVersionUID = -758459502806858414L;
    /**
     * 当前位置
     */
    private static double locationlatitude;
    private static double locationlongtitude;
    /**
     * 精度
     */
    private double latitude;
    /**
     * 纬度
     */
    private double longitude;
    /**
     * 图片ID，真实项目中可能是图片路径
     */
    private int imgId;
    /**
     * 商家名称
     */
    private String gasname;
    /**
     * 距离
     */
    private String distance;
    /**
     * 运营商类型
     */
    private String brandname;
    /**
     * 加油站价格
     */
    private List<Gastprice> gasprice;
    /**
     * 省控油价
     */
    private List<Price> proventprice;
    /**
     * 加油卡
     */
    private String fwlsmc;

    /**
     * 加油站地址
     */
    private String gasaddress;

    /**
     * 家油站区域
     */
    private String areaname;

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getFwlsmc() {
        return fwlsmc;
    }

    public void setFwlsmc(String fwlsmc) {
        this.fwlsmc = fwlsmc;
    }

    public String getGasname() {
        return gasname;
    }

    public void setGasname(String gasname) {
        this.gasname = gasname;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public List<Gastprice> getGasprice() {
        return gasprice;
    }

    public void setGasprice(List<Gastprice> gasprice) {
        this.gasprice = gasprice;
    }

    public List<Price> getProventprice() {
        return proventprice;
    }

    public void setProventprice(List<Price> proventprice) {
        this.proventprice = proventprice;
    }

    public String getGasaddress() {
        return gasaddress;
    }

    public void setGasaddress(String gasaddress) {
        this.gasaddress = gasaddress;
    }

    public GasInfoUtil() {
    }

    public GasInfoUtil(double latitude, double longitude, int imgId, String name,
                String distance, String brandname, String gasaddress, String fwlmsc,
                List<Gastprice> gaslistprice,List<Price> listprice) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.gasname = name;
        this.distance = distance;
        this.gasaddress = gasaddress;
        this.brandname = brandname;
        this.fwlsmc = fwlmsc;
        this.gasprice=gaslistprice;
        this.proventprice=listprice;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getgasName() {
        return gasname;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setName(String name) {
        this.gasname = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static double getLocationlatitude() {
        return locationlatitude;
    }

    public static void setLocationlatitude(double locationlatitude) {
        GasInfoUtil.locationlatitude = locationlatitude;
    }

    public static double getLocationlongtitude() {
        return locationlongtitude;
    }

    public static void setLocationlongtitude(double locationlongtitude) {
        GasInfoUtil.locationlongtitude = locationlongtitude;
    }

    public static List<Datas> gasinfo = new ArrayList<Datas>();
    public static void VolleyGet(final Context context) {
        String url = "http://apis.juhe.cn/oil/local?key=f76a41676986a78971d9ab2265dbe714&lon="
                + locationlongtitude
                + "&lat="
                + locationlatitude
                + "&format=2&r=3000";

        MyStringRequest request = new MyStringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        JudgeNet judgeNet = new JudgeNet();
                        Gson gson = new Gson();
                        Status status = gson.fromJson(response, Status.class);
                        if (status.getResult()!= null) {
                            Toast.makeText(context, "111111",
                                    Toast.LENGTH_LONG).show();
                            gasinfo = status.getResult().getData();
                            Collections.sort(gasinfo);
                        } else if (status.getResult() == null) {
                            Toast.makeText(context, "抱歉，查找失败",
                                    Toast.LENGTH_LONG).show();
                            AnimAsyncTask.progress = 1;

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("StringReqGet");
        MyApplication.getHttpQueues().add(request);
        return ;
    }

    public static List<GasInfoUtil> infos = new ArrayList<GasInfoUtil>();
    public static List<Datas> getGasinfo() {
        return gasinfo;
    }
    public static void setGasinfo(List<Datas> gasinfo) {
        GasInfoUtil.gasinfo = gasinfo;
    }

}
