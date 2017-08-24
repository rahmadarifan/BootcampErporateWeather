package com.example.rahmadarifanhr.weatherid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelForecast;
import com.example.rahmadarifanhr.weatherid.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by Ifan on 8/25/2017.
 */

public class TambahKotaAdapter extends RecyclerView.Adapter<TambahKotaAdapter.TambahKotaHolders> {

    private Context context;
    private List<String> listKota, listTemp, listIcon;
    private String kotaNow;

    public class TambahKotaHolders extends RecyclerView.ViewHolder {

        public TextView nama_kota, temp_kota;
        public ImageView icon_weather, check;

        public TambahKotaHolders(View view) {
            super(view);
            nama_kota = (TextView) view.findViewById(R.id.nama_kota);
            temp_kota = (TextView) view.findViewById(R.id.temp_kota);
            icon_weather = (ImageView) view.findViewById(R.id.icon_weather_kota);
            check = (ImageView) view.findViewById(R.id.check);
            context = view.getContext();
        }
    }

    public TambahKotaAdapter(List<String> listKota, List<String> listTemp, List<String> listIcon, String kotaNow) {
        this.listKota = listKota;
        this.listTemp = listTemp;
        this.listIcon = listIcon;
        this.kotaNow = kotaNow;
    }

    @Override
    public TambahKotaHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kota, parent, false);
        return new TambahKotaHolders(itemView);
    }

    @Override
    public void onBindViewHolder(TambahKotaHolders holder, int position) {
        if (position == listKota.size()) {
            holder.nama_kota.setVisibility(View.INVISIBLE);
            holder.temp_kota.setText("+");
            holder.icon_weather.setVisibility(View.INVISIBLE);
        } else {
            holder.nama_kota.setText(listKota.get(position));
            holder.nama_kota.setSelected(true);
            holder.temp_kota.setText(listTemp.get(position));
            int id = context.getResources().getIdentifier("weather_" + listIcon.get(position), "drawable", context.getPackageName());
            holder.icon_weather.setImageResource(id);
            if (listKota.get(position).equals(kotaNow)) {
                holder.check.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.listKota.size() + 1;
    }


}