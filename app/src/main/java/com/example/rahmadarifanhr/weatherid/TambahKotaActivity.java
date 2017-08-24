package com.example.rahmadarifanhr.weatherid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rahmadarifanhr.weatherid.adapter.TambahKotaAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TambahKotaActivity extends AppCompatActivity {
    private GridLayoutManager lLayout;
    private RecyclerView mRecyclerView;
    TambahKotaAdapter tambahKotaAdapter;
    private WeatherPreferences weatherPreferences;
    private HashMap<String, String> dataWeather;
    List<String> listKota, listTemp, listIcon, listKotaBaru, listTempBaru, listIconBaru;
    String namaKota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kota);
        weatherPreferences = new WeatherPreferences(getApplicationContext());
        dataWeather = weatherPreferences.getDataDetails();
        listKota = Arrays.asList(dataWeather.get(WeatherPreferences.NAMAKOTA).replace("[", "").replace("]", "").split(", "));
        listTemp = Arrays.asList(dataWeather.get(WeatherPreferences.TEMP).replace("[", "").replace("]", "").split(", "));
        listIcon = Arrays.asList(dataWeather.get(WeatherPreferences.ICON_TEMP).replace("[", "").replace("]", "").split(", "));
        listKotaBaru = new ArrayList<String>(listKota);
        listTempBaru = new ArrayList<String>(listTemp);
        listIconBaru = new ArrayList<String>(listIcon);
        namaKota = getIntent().getStringExtra("kota_now");
        lLayout = new GridLayoutManager(getApplicationContext(), 3);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tambahKotaAdapter = new TambahKotaAdapter(listKotaBaru, listTempBaru, listIconBaru, namaKota);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lLayout);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(tambahKotaAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == listKota.size()) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TambahKotaActivity.this);
                    LayoutInflater inflater = TambahKotaActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    dialogBuilder.setView(dialogView);

                    final EditText edt = (EditText) dialogView.findViewById(R.id.edittext_kota);
                    dialogBuilder.setMessage("Masukkan nama kota");
                    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            TambahKotaActivity.this.startActivity(new Intent(TambahKotaActivity.this, MainActivity.class).putExtra("namaKota", edt.getText().toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", null);
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                } else {
                    TambahKotaActivity.this.startActivity(new Intent(TambahKotaActivity.this, MainActivity.class).putExtra("namaKota", listKota.get(position)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }

            @Override
            public void onLongClick(View view, final int position) {
                if (position != listKota.size()) {
                    new AlertDialog.Builder(TambahKotaActivity.this)
                            .setMessage("Apakah anda ingin menghapus ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    listKotaBaru.remove(position);
                                    listTempBaru.remove(position);
                                    listIconBaru.remove(position);
                                    weatherPreferences.saveData(listKotaBaru.toString(), listTempBaru.toString(), listIconBaru.toString());

                                    TambahKotaActivity.this.finish();
                                    TambahKotaActivity.this.startActivity(getIntent());
                                }
                            })
                            .setNegativeButton("Tidak", null)
                            .show();
                }
            }
        }));
    }
}
