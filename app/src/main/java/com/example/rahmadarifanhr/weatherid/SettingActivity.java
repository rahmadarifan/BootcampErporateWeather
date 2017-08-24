package com.example.rahmadarifanhr.weatherid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;

public class SettingActivity extends AppCompatActivity {
    TextView textViewUnits;
    SettingPreferences settingPreferences;
    LinearLayout linearLayoutSetting;
    int position;
    AlertDialog alertDialog1;
    String units = "";
    CharSequence[] values = {"Standard", "Metric", "Imperial"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingPreferences = new SettingPreferences(getApplicationContext());
        units = settingPreferences.getDataDetails().get(SettingPreferences.UNITS);
        for (int i = 0; i < values.length; i++) {
            if (values[i].toString().equals(units)){
                position = i;
            }
        }
        setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewUnits = (TextView) findViewById(R.id.units);
        textViewUnits.setText(settingPreferences.getDataDetails().get(SettingPreferences.UNITS));

        linearLayoutSetting = (LinearLayout) findViewById(R.id.layout_setting);
        linearLayoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAlertDialogWithRadioButtonGroup();
            }
        });
    }

    public void CreateAlertDialogWithRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("Units");
        builder.setSingleChoiceItems(values, position, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                settingPreferences.saveData(values[item].toString());
                textViewUnits.setText(values[item].toString());
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
