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
public class WeatherAdapter extends ArrayAdapter<Weather> {
    private int resourceId;

    public WeatherAdapter(Context context, int textViewResourceId, List<Weather> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather iweather = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView date = (TextView) view.findViewById(R.id.weather_forecast_date);
        TextView weather = (TextView) view.findViewById(R.id.weather_forecast_weather);
        TextView temperature = (TextView) view.findViewById(R.id.weather_forecast_temperature);
        TextView wind = (TextView) view.findViewById(R.id.weather_forecast_wind);
        date.setText(iweather.getDate());
        weather.setText(iweather.getWeather());
        temperature.setText(iweather.getTemperature());
        wind.setText(iweather.getWind());
        return view;
    }
}
