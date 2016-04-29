package com.hangon.weizhang.activity;

/**
 * Created by Chuan on 2016/4/19.
 */

import com.cheshouye.api.client.c.a;
import com.cheshouye.api.client.c.b;
import com.cheshouye.api.client.json.AllConfigJson;
import com.cheshouye.api.client.json.CityInfoJson;
import com.cheshouye.api.client.json.InputConfigJson;
import com.cheshouye.api.client.json.ProvinceInfoJson;
import com.cheshouye.api.client.json.WeizhangResponseJson;
import com.hangon.weizhang.model.CarInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class WeizhangClient {
    public WeizhangClient() {
    }

    public static AllConfigJson getAll() {
        if(a.a().b() == null) {
            com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
            return null;
        } else {
            return a.a().b();
        }
    }

    public static List<ProvinceInfoJson> getAllProvince() {
        ArrayList var0 = new ArrayList();
        if(a.a().c() == null) {
            com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
            return null;
        } else {
            Set var1;
            if((var1 = a.a().c().entrySet()) == null) {
                com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
                return null;
            } else {
                Iterator var2 = var1.iterator();

                while(var2.hasNext()) {
                    ProvinceInfoJson var3 = (ProvinceInfoJson)((Entry)var2.next()).getValue();
                    var0.add(var3);
                }

                return var0;
            }
        }
    }

    public static ProvinceInfoJson getProvince(int provinceId) {
        if(a.a().c() == null) {
            com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
            return null;
        } else {
            return (ProvinceInfoJson)a.a().c().get(Integer.valueOf(provinceId));
        }
    }

    public static List<CityInfoJson> getCitys(int provinceId) {
        ArrayList var1 = new ArrayList();
        if(a.a().d() == null) {
            com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
            return null;
        } else {
            Set var2 = a.a().d().entrySet();
            if(a.a().d() == null) {
                com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
                return null;
            } else {
                Iterator var3 = var2.iterator();

                while(var3.hasNext()) {
                    Entry var5 = (Entry)var3.next();
                    if(provinceId == ((CityInfoJson)var5.getValue()).getProvince_id()) {
                        CityInfoJson var4;
                        (var4 = new CityInfoJson()).setCity_id(((CityInfoJson)var5.getValue()).getCity_id());
                        var4.setProvince_id(((CityInfoJson)var5.getValue()).getProvince_id());
                        var4.setCity_name(((CityInfoJson)var5.getValue()).getCity_name());
                        var4.setCar_head(((CityInfoJson)var5.getValue()).getCar_head());
                        var1.add(var4);
                    }
                }

                return var1;
            }
        }
    }

    public static CityInfoJson getCity(int cityId) {
        if(a.a().d() == null) {
            com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
            return null;
        } else {
            return (CityInfoJson)a.a().d().get(Integer.valueOf(cityId));
        }
    }

    public static InputConfigJson getInputConfig(int cityId) {
        if(a.a().e() == null) {
            com.cheshouye.api.client.b.a.c("WeizhangClient没有检测到可用的WeizhangIntentService服务，无法获取配置信息");
            return null;
        } else {
            return (InputConfigJson)a.a().e().get(Integer.valueOf(cityId));
        }
    }

    public static WeizhangResponseJson getWeizhang(CarInfo car) {
        String var1 = car.getChepai_no();
        String var2 = car.getChejia_no();
        String var3 = car.getEngine_no();
        String var4 = car.getRegister_no();
        int car1 = car.getCity_id();
        WeizhangResponseJson car2;
        if((car2 = WeizhangResponseJson.fromJson(b.a("{hphm=" + var1 + "&classno=" + var2 + "&engineno=" + var3 + "&registno=" + var4 + "&city_id=" + car1 + "&car_type=02}"))).getStatus() == 9999) {
            a.a().b((long)car2.getCount());
            return com.cheshouye.api.client.a.b.a();
        } else {
            return car2;
        }
    }
}

