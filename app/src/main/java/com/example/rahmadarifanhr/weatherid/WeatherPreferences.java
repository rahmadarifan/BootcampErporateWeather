package com.example.rahmadarifanhr.weatherid;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import static com.example.rahmadarifanhr.weatherid.SettingPreferences.UNITS;

/**
 * Created by Ifan on 8/25/2017.
 */

public class WeatherPreferences {
    Context context;

    public static final String PREF_FILE = WeatherPreferences.class.getSimpleName();

    SharedPreferences sharedPreferences;

    public static final String NAMAKOTA = "NAMAKOTA";
    public static final String TEMP = "TEMP";
    public static final String ICON_TEMP = "ICON_TEMP";
    public static final String IS_NULL = "IS_NULL";
    SharedPreferences.Editor editor;

    public WeatherPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String namaKota, String temp, String iconTemp) {
        editor.putBoolean(IS_NULL, false);
        editor.putString(NAMAKOTA, namaKota);
        editor.putString(ICON_TEMP, iconTemp);
        editor.putString(TEMP, temp);

        editor.apply();
    }

    public void deleteData() {
        editor.clear();
        editor.commit();
    }

    public boolean isNull() {
        return sharedPreferences.getBoolean(IS_NULL, true);
    }

    public HashMap<String, String> getDataDetails() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put(NAMAKOTA, sharedPreferences.getString(NAMAKOTA, null));
        data.put(TEMP, sharedPreferences.getString(TEMP, null));
        data.put(ICON_TEMP, sharedPreferences.getString(ICON_TEMP, null));
        return data;
    }
}
