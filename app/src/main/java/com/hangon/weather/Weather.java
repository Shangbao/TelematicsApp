package com.hangon.weather;

/**
 * Created by Chuan on 2016/5/5.
 */
public class Weather {
    private String date;
    private String weather;
    private String temperature;
    private int weatherIcon;

    public Weather() {
    }

    public Weather(String date, String weather, String temperature) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemperature() {
        return temperature;
    }

}
