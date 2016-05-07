package com.hangon.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;

import java.util.List;

/**
 * Created by Chuan on 2016/5/5.
 */
public class CityAdapter extends ArrayAdapter<City> {

    private int resourceId;

    public CityAdapter(Context context, int resource, List<City> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView cityName = (TextView) view.findViewById(R.id.city);
        cityName.setText(city.getName());
        return view;
    }
}
