package com.example.rahmadarifanhr.weatherid.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelForecast;
import com.example.rahmadarifanhr.weatherid.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ifan on 8/21/2017.
 */

public class TempTodayAdapter extends RecyclerView.Adapter<TempTodayAdapter.TempTodayHolders> {

    private List<DataModelForecast.List> listTempTodays;
    private List<DataModelForecast.List> tmpListTempToday;
    private Context context;

    public class TempTodayHolders extends RecyclerView.ViewHolder {

        public TextView timeToday, tempTimeToday;
        public ImageView iconTimeToday;

        public TempTodayHolders(View view) {
            super(view);
            timeToday = view.findViewById(R.id.time_today);
            iconTimeToday = view.findViewById(R.id.icon_weather_time_today);
            tempTimeToday = view.findViewById(R.id.temp_weather_time_today);
            context = view.getContext();
        }
    }

    public TempTodayAdapter(List<DataModelForecast.List> listTempTodays) {
        this.listTempTodays = listTempTodays;
        tmpListTempToday = new ArrayList<>();
        generateTime();
    }

    @Override
    public TempTodayHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weather_today, parent, false);
        return new TempTodayHolders(itemView);
    }

    @Override
    public void onBindViewHolder(TempTodayHolders holder, int position) {
        holder.timeToday.setText(tmpListTempToday.get(position).getDtTxt().substring(11, 16));
//        holder.gambarTempToday.setImageResource(listTempToday.getGambarTempToday());
        holder.tempTimeToday.setText(tmpListTempToday.get(position).getMain().getTemp() + context.getResources().getString(R.string.celcius));
    }

    @Override
    public int getItemCount() {
        return tmpListTempToday.size();
    }

    void generateTime() {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Date timeNow = null;
        String tmp = "";
        try {
            timeNow = parser.parse(parser.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < listTempTodays.size(); i++) {
            String tmpTime = listTempTodays.get(i).getDtTxt().substring(11, 16);
            try {
                Date timeListNow = parser.parse(tmpTime);
                if (timeNow.after(timeListNow)) {
                    tmp = parser.format(timeListNow);
                    break;
                } else if (timeListNow.after(timeNow)){
                    tmp = parser.format(timeListNow);
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < listTempTodays.size(); j++) {
                String tmpTime = listTempTodays.get(j).getDtTxt().substring(11, 16);
                if (tmpTime.equals(tmp)) {
                    tmpListTempToday.add(listTempTodays.get(j));
                    if (j + 1 >= listTempTodays.size()) {
                        j = -1;
                    }
                    tmp = listTempTodays.get(j + 1).getDtTxt().substring(11, 16);
                    break;
                }
            }
        }
        this.notifyDataSetChanged();
    }
}
