package com.hangon.weather;

/**
 * Created by Chuan on 2016/5/5.
 */
public class Weather {
    private String date;
    private String weather;
    private String temperature;
    private String wind;

    public Weather() {
    }

    public Weather(String date, String weather, String temperature, String wind) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
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

    public String getWind() {
        return wind;
    }
}
