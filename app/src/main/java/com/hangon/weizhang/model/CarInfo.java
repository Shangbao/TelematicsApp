package com.hangon.weizhang.model;

/**
 * Created by Chuan on 2016/4/19.
 */

import com.cheshouye.a.a.c;
import com.cheshouye.a.a.e;
import com.cheshouye.api.client.json.a;

import java.io.Serializable;

public class CarInfo extends a implements Serializable {
    private String chepai_no;
    private String chejia_no;
    private String engine_no;
    private String register_no;
    private int city_id;

    public CarInfo() {
    }

    public CarInfo(String chepai_no, String chejia_no, String engine_no, String register_no, int city_id) {
        this.chepai_no = chepai_no;
        this.chejia_no = chejia_no;
        this.engine_no = engine_no;
        this.register_no = register_no;
        this.city_id = city_id;
    }

    public String getChepai_no() {
        return this.chepai_no;
    }

    public void setChepai_no(String chepai_no) {
        this.chepai_no = chepai_no;
    }

    public String getChejia_no() {
        return this.chejia_no;
    }

    public void setChejia_no(String chejia_no) {
        this.chejia_no = chejia_no;
    }

    public String getEngine_no() {
        return this.engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public int getCity_id() {
        return this.city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getRegister_no() {
        return this.register_no;
    }

    public void setRegister_no(String register_no) {
        this.register_no = register_no;
    }

    public c toJSONObject() {
        c var1 = new c();

        try {
            if (this.chepai_no != null) {
                var1.a("chepai_no", this.chepai_no);
            }

            if (this.chejia_no != null) {
                var1.a("chejia_no", this.chejia_no);
            }

            if (this.engine_no != null) {
                var1.a("engine_no", this.engine_no);
            }

            if (this.register_no != null) {
                var1.a("register_no", this.register_no);
            }

            if (this.city_id > 0) {
                var1.a("city_id", this.city_id);
            }
        } catch (Exception var3) {
            com.cheshouye.api.client.b.a.a("CarInfo toJson失败", var3);
        }

        return var1;
    }

    public static CarInfo fromJson(String jsonStr) {
        CarInfo var1 = new CarInfo();

        try {
            c var2;
            if ((var2 = (c) (new e(jsonStr)).d()).f("chepai_no")) {
                var1.setChepai_no(var2.e("chepai_no"));
            }

            if (var2.f("chejia_no")) {
                var1.setChejia_no(var2.e("chejia_no"));
            }

            if (var2.f("engine_no")) {
                var1.setEngine_no(var2.e("engine_no"));
            }

            if (var2.f("register_no")) {
                var1.setRegister_no(var2.e("register_no"));
            }

            if (var2.f("city_id")) {
                var1.setCity_id(var2.b("city_id"));
            }
        } catch (Exception var3) {
            com.cheshouye.api.client.b.a.a("CarInfo fromJson失败:" + jsonStr, var3);
        }

        return var1;
    }

    public String toString() {
        return "WeizhangRequestJson [chepai_no=" + this.chepai_no + ", chejia_no=" + this.chejia_no + ", engine_no=" + this.engine_no + ", register_no=" + this.register_no + ", city_id=" + this.city_id + "]";
    }
}

