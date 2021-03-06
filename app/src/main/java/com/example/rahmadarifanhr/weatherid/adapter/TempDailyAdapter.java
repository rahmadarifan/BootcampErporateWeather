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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ifan on 8/22/2017.
 */

public class TempDailyAdapter extends RecyclerView.Adapter<TempDailyAdapter.TempDailyHolders> {

    private List<DataModelForecast.List> listTempDailys;
    private List<DataModelForecast.List> tmpListTempDailys;
    private Context context;
    private String satuanCuaca;
    public class TempDailyHolders extends RecyclerView.ViewHolder {

        public TextView time, temp_weather;
        public ImageView icon_weather;

        public TempDailyHolders(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            icon_weather = view.findViewById(R.id.icon_weather);
            temp_weather = view.findViewById(R.id.temp_weather);
            context = view.getContext();
        }
    }

    public TempDailyAdapter(List<DataModelForecast.List> listTempDailys, String satuanCuaca) {
        this.listTempDailys = listTempDailys;
        tmpListTempDailys = new ArrayList<>();
        this.satuanCuaca = satuanCuaca;
        generateDay();

    }

    @Override
    public TempDailyHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weather, parent, false);
        return new TempDailyHolders(itemView);
    }

    @Override
    public void onBindViewHolder(TempDailyHolders holder, int position) {
        holder.time.setText(getDayName(tmpListTempDailys.get(position).getDtTxt().substring(0, 10)));
        holder.temp_weather.setText(tmpListTempDailys.get(position).getMain().getTemp() + satuanCuaca);
        int id = context.getResources().getIdentifier("weather_"+tmpListTempDailys.get(position).getWeather().get(0).getIcon(), "drawable", context.getPackageName());
        holder.icon_weather.setImageResource(id);

    }

    @Override
    public int getItemCount() {
        return this.tmpListTempDailys.size();
    }

    private String getDayName(String tanggal) {
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(tanggal);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String hari = outFormat.format(date);
            String result = "";
            if (hari.equals("Sunday")){
                result = "Min";
            } else if (hari.equals("Monday")){
                result = "Sen";
            } else if (hari.equals("Tuesday")){
                result = "Sel";
            } else if (hari.equals("Wednesday")){
                result = "Rab";
            } else if (hari.equals("Thursday")){
                result = "Kam";
            } else if (hari.equals("Friday")){
                result = "Jum";
            } else if (hari.equals("Saturday")){
                result = "Sab";
            } else {
                result = "null";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void generateDay() {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Date timeNow = null;
        String tmp = "";
        try {
            timeNow = parser.parse(parser.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < listTempDailys.size(); i++) {
            String tmpTime = listTempDailys.get(i).getDtTxt().substring(11, 16);
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


        for (int j = 0; j < listTempDailys.size(); j++) {
            String tmpTime = listTempDailys.get(j).getDtTxt().substring(11, 16);
            if (tmpTime.equals(tmp)) {
                int x = j-1;
                if (j-1 < 0) x = j;
                tmpListTempDailys.add(listTempDailys.get(x));
                this.notifyDataSetChanged();
            }
        }

        this.notifyDataSetChanged();
    }
}

