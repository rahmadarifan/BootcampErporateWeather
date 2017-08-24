package com.example.rahmadarifanhr.weatherid;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Ifan on 8/24/2017.
 */

public class SettingPreferences {
    Context context;

    public static final String PREF_FILE = "SettingPreferences";

    SharedPreferences sharedPreferences;

    public static final String UNITS = "UNIT";
    public static final String IS_NULL = "IS_NULL";
    SharedPreferences.Editor editor;

    public SettingPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String units) {
        editor.putBoolean(IS_NULL, false);
        editor.putString(UNITS, units);
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
        data.put(UNITS, sharedPreferences.getString(UNITS, null));
        return data;
    }
}
