package com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ifan on 8/23/2017.
 */

public class DataModelForecast {

    public class City {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("coord")
        @Expose
        private Coord coord;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("population")
        @Expose
        private String population;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Coord getCoord() {
            return coord;
        }

        public void setCoord(Coord coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPopulation() {
            return population;
        }

        public void setPopulation(String population) {
            this.population = population;
        }

    }

    public class Clouds {

        @SerializedName("all")
        @Expose
        private String all;

        public String getAll() {
            return all;
        }

        public void setAll(String all) {
            this.all = all;
        }

    }
    public class Coord {

        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

    }

    public class Example {

        @SerializedName("cod")
        @Expose
        private String cod;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("cnt")
        @Expose
        private String cnt;
        @SerializedName("list")
        @Expose
        private java.util.List<List> list = null;
        @SerializedName("city")
        @Expose
        private City city;

        public String getCod() {
            return cod;
        }

        public void setCod(String cod) {
            this.cod = cod;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCnt() {
            return cnt;
        }

        public void setCnt(String cnt) {
            this.cnt = cnt;
        }

        public java.util.List<List> getList() {
            return list;
        }

        public void setList(java.util.List<List> list) {
            this.list = list;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

    }

    public class List {

        @SerializedName("dt")
        @Expose
        private String dt;
        @SerializedName("main")
        @Expose
        private Main main;
        @SerializedName("weather")
        @Expose
        private java.util.List<Weather> weather = null;
        @SerializedName("clouds")
        @Expose
        private Clouds clouds;
        @SerializedName("wind")
        @Expose
        private Wind wind;
        @SerializedName("sys")
        @Expose
        private Sys sys;
        @SerializedName("dt_txt")
        @Expose
        private String dtTxt;
        @SerializedName("rain")
        @Expose
        private Rain rain;

        public String getDt() {
            return dt;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public java.util.List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(java.util.List<Weather> weather) {
            this.weather = weather;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public Sys getSys() {
            return sys;
        }

        public void setSys(Sys sys) {
            this.sys = sys;
        }

        public String getDtTxt() {
            String[] tmp = dtTxt.split(" ");
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
            Date timeNow = null;
            try {
                timeNow = parser.parse(tmp[1]);
                Calendar cal = Calendar.getInstance();
                cal.setTime(timeNow);
                cal.add(Calendar.HOUR_OF_DAY, 7);
                timeNow = cal.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return tmp[0]+ " " + parser.format(timeNow);
        }

        public void setDtTxt(String dtTxt) {
            this.dtTxt = dtTxt;
        }

        public Rain getRain() {
            return rain;
        }

        public void setRain(Rain rain) {
            this.rain = rain;
        }

    }

    public class Main {

        @SerializedName("temp")
        @Expose
        private String temp;
        @SerializedName("temp_min")
        @Expose
        private String tempMin;
        @SerializedName("temp_max")
        @Expose
        private String tempMax;
        @SerializedName("pressure")
        @Expose
        private String pressure;
        @SerializedName("sea_level")
        @Expose
        private String seaLevel;
        @SerializedName("grnd_level")
        @Expose
        private String grndLevel;
        @SerializedName("humidity")
        @Expose
        private String humidity;
        @SerializedName("temp_kf")
        @Expose
        private String tempKf;

        public String getTemp() {
            Double tempNow = Double.parseDouble(temp);
            int tempPembulatan = (int) Math.round(tempNow);
            return ""+tempPembulatan;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getTempMin() {
            return tempMin;
        }

        public void setTempMin(String tempMin) {
            this.tempMin = tempMin;
        }

        public String getTempMax() {
            return tempMax;
        }

        public void setTempMax(String tempMax) {
            this.tempMax = tempMax;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getSeaLevel() {
            return seaLevel;
        }

        public void setSeaLevel(String seaLevel) {
            this.seaLevel = seaLevel;
        }

        public String getGrndLevel() {
            return grndLevel;
        }

        public void setGrndLevel(String grndLevel) {
            this.grndLevel = grndLevel;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getTempKf() {
            return tempKf;
        }

        public void setTempKf(String tempKf) {
            this.tempKf = tempKf;
        }

    }
    public class Rain {

        @SerializedName("3h")
        @Expose
        private String _3h;

        public String get3h() {
            return _3h;
        }

        public void set3h(String _3h) {
            this._3h = _3h;
        }

    }

    public class Sys {

        @SerializedName("pod")
        @Expose
        private String pod;

        public String getPod() {
            return pod;
        }

        public void setPod(String pod) {
            this.pod = pod;
        }

    }

    public class Weather {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("main")
        @Expose
        private String main;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("icon")
        @Expose
        private String icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

    }
    public class Wind {

        @SerializedName("speed")
        @Expose
        private String speed;
        @SerializedName("deg")
        @Expose
        private String deg;

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getDeg() {
            return deg;
        }

        public void setDeg(String deg) {
            this.deg = deg;
        }

    }
}
