package com.hangon.map.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/23.
 */
public class GasInfoAdpter extends SimpleAdapter {
    ViewHolder vh;
    Context context;
    int resource;
    static double lat = 0.0;
    int fals = -1;
    List gaslist;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public GasInfoAdpter(Context context, List<? extends Map<String, ?>> data,
                         int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.resource = resource;
        this.gaslist = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        vh = new ViewHolder();
        View v = LayoutInflater.from(context).inflate(resource, null);
        vh.rl = (RelativeLayout) v.findViewById(R.id.item_rl);
        vh.gasname = (TextView) v.findViewById(R.id.gaslist_name);
        vh.gasdistance = (TextView) v.findViewById(R.id.gaslist_distance);
        vh.gasaddress = (TextView) v.findViewById(R.id.gaslist_address);
        vh.gaslat = (TextView) v.findViewById(R.id.gaslist_lat);
        vh.gaslon = (TextView) v.findViewById(R.id.gaslist_lon);
        vh.gasbrandname = (TextView) v.findViewById(R.id.gaslist_brandname);
        HashMap map = (HashMap) gaslist.get(position);
        vh.gasname.setText(map.get("gasname").toString());
        vh.gasaddress.setText(map.get("gasaddress").toString());
        vh.gasdistance.setText(map.get("gasdistance").toString());
        //vh.gaslat.setText(map.get("gaslat").toString());
        //vh.gaslon.setText(map.get("gaslon").toString());
        //vh.gasbrandname.setText(map.get("gasbrandname").toString());
        if (position == fals) {
            vh.rl.setBackgroundColor(Color.argb(1000, 234, 230, 231));
        }
        return v;
    }

    public void item(ListView list) {
        for (int i = 0; i < gaslist.size(); i++) {
            HashMap map1 = (HashMap) gaslist.get(i);
            if (((lat + "")).trim()
                    .equals(map1.get("gaslat").toString().trim())) {
                fals = i;
                return;
            }
        }
    }

    public int getFals() {
        return fals;
    }

    public void setFals(int fals) {
        this.fals = fals;
    }

    public void item1(ListView list) {
        for (int i = 0; i < gaslist.size(); i++) {
            HashMap map1 = (HashMap) gaslist.get(i);
            if (((lat + "")).trim()
                    .equals(map1.get("gaslat").toString().trim())) {
                list.setSelection(i);
            }
        }
    }

    class ViewHolder {
        RelativeLayout rl;
        TextView gasname;
        TextView gasdistance;
        TextView gasaddress;
        TextView gaslat;
        TextView gaslon;
        TextView gasprice;
        TextView gasbrandname;
        TextView gascode;
    }
}
