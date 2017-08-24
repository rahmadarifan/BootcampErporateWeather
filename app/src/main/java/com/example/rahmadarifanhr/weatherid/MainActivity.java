package com.example.rahmadarifanhr.weatherid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.rahmadarifanhr.weatherid.API.GoogleMapAPI.GoogleMapsAPIClient;
import com.example.rahmadarifanhr.weatherid.API.GoogleMapAPI.Model.DataModelGeoCode;
import com.example.rahmadarifanhr.weatherid.API.GoogleMapAPI.Model.DataModelPlace;
import com.example.rahmadarifanhr.weatherid.API.GoogleMapAPI.Model.GoogleMapService;
import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.APIClient;
import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelForecast;
import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.Model.DataModelWeatherToday;
import com.example.rahmadarifanhr.weatherid.API.OpenWeatherAPI.OpenWeatherService;
import com.example.rahmadarifanhr.weatherid.adapter.TambahKotaAdapter;
import com.example.rahmadarifanhr.weatherid.adapter.TempDailyAdapter;
import com.example.rahmadarifanhr.weatherid.adapter.TempTodayAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.list;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    public final static String TAG = MainActivity.class.getSimpleName();

    OpenWeatherService openWeatherService;
    GoogleMapService googleMapService;

    TextView textViewCityNow, textViewTempNow, textViewDescNow, textViewKelembapan, textViewTekanan, textViewKecepatanUdara;
    ImageView imageViewKecepatanUdara, imageViewCity, btnSetting, imageViewCuacaSekarang, imageViewAddCity;
    public RecyclerView recyclerViewHourly, recyclerViewDaily;
    public TempTodayAdapter tempTodayAdapter;
    public TempDailyAdapter tempDailyAdapter;
    List<DataModelForecast.List> listForecast;
    List<String> listKota, listTemp, listIcon;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_FINE_LOCATION_ACCESS = 1;
    private Location myLocation;

    ProgressDialog dialog;
    private SettingPreferences settingPreferences;
    private WeatherPreferences weatherPreferences;
    private HashMap<String, String> dataWeather;
    String latitude, longitude, kota, temp, icon_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = ProgressDialog.show(this, "", "Mengambil Data", true);
        dialog.show();

        listForecast = new ArrayList<>();
        listKota = new ArrayList<>();
        listTemp = new ArrayList<>();
        listIcon = new ArrayList<>();

        settingPreferences = new SettingPreferences(getApplicationContext());
        weatherPreferences = new WeatherPreferences(getApplicationContext());

        textViewCityNow = (TextView) findViewById(R.id.city_name);
        textViewTempNow = (TextView) findViewById(R.id.temp_now);
        textViewDescNow = (TextView) findViewById(R.id.desc_now);
        textViewKelembapan = (TextView) findViewById(R.id.kelembapan);
        textViewTekanan = (TextView) findViewById(R.id.tekanan);
        textViewKecepatanUdara = (TextView) findViewById(R.id.kecepatan_udara);
        imageViewKecepatanUdara = (ImageView) findViewById(R.id.icon_kecepatan_udara);
        imageViewCity = (ImageView) findViewById(R.id.city_image);
        imageViewCuacaSekarang = (ImageView) findViewById(R.id.icon_weather_now);
        imageViewAddCity = (ImageView) findViewById(R.id.add_city);

        btnSetting = (ImageView) findViewById(R.id.setting);
        if (imageViewKecepatanUdara != null)
            Glide.with(this).load(R.drawable.windmill).into(imageViewKecepatanUdara);

        recyclerViewHourly = (RecyclerView) findViewById(R.id.recycler_view_today);
        recyclerViewHourly.setHasFixedSize(true);
        recyclerViewHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewHourly.setItemAnimator(new DefaultItemAnimator());

        recyclerViewDaily = (RecyclerView) findViewById(R.id.recycler_view_day);
        recyclerViewDaily.setHasFixedSize(true);
        recyclerViewDaily.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewDaily.setItemAnimator(new DefaultItemAnimator());

        openWeatherService = APIClient.getClient().create(OpenWeatherService.class);
        googleMapService = GoogleMapsAPIClient.getClient().create(GoogleMapService.class);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        imageViewAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TambahKotaActivity.class).putExtra("kota_now", kota));
            }
        });
    }

    @Override
    protected void onStart() {
        buildApiClient();
        checkPermissions();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void buildApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void checkPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_ACCESS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, new LocationRequest().setInterval(10000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY), this);
            mGoogleApiClient.disconnect();
//            String latitude = "0.451195";
//            String longitude = "101.420973";

            latitude = String.valueOf(myLocation.getLatitude());
            longitude = String.valueOf(myLocation.getLongitude());

            HashMap<String, String> dataSetting = settingPreferences.getDataDetails();
            final String unit;
            if (!settingPreferences.isNull()) {
                unit = dataSetting.get(SettingPreferences.UNITS);
            } else {
                unit = "Metric";
                settingPreferences.saveData(unit);
            }
            Log.v(TAG, unit);

            int getRes = getResources().getIdentifier(unit, "array", MainActivity.this.getPackageName());
            final String satuan[] = getResources().getStringArray(getRes);
            final String appid = BuildConfig.OPENWEATHER_API_KEY;
            String namaKota = getIntent().getStringExtra("namaKota");
            Call<DataModelWeatherToday.Example> call;
            if (namaKota == null) {
                call = openWeatherService.getWeatherToday(latitude, longitude, unit, appid);

            } else {
                call = openWeatherService.getWeatherTodayByCity(namaKota, unit, appid);
            }
            call.enqueue(new Callback<DataModelWeatherToday.Example>() {
                @Override
                public void onResponse(Call<DataModelWeatherToday.Example> call, Response<DataModelWeatherToday.Example> response) {
                    icon_temp = response.body().getWeather().get(0).getIcon();
                    int id = getResources().getIdentifier("weather_" + icon_temp, "drawable", getPackageName());
                    imageViewCuacaSekarang.setImageResource(id);
                    temp = response.body().getMain().getTemp() + satuan[0];
                    textViewTempNow.setText(temp);
                    textViewDescNow.setText(response.body().getWeather().get(0).getDescription());
                    textViewKelembapan.setText(response.body().getMain().getHumidity() + satuan[1]);
                    textViewTekanan.setText(response.body().getMain().getPressure() + satuan[2]);
                    textViewKecepatanUdara.setText(response.body().getWind().getSpeed() + satuan[3]);

                    kota = response.body().getName();
                    if (weatherPreferences.isNull()) {
                        listKota.add(kota);
                        listTemp.add(temp);
                        listIcon.add(icon_temp);
                        weatherPreferences.saveData(listKota.toString(), listTemp.toString(), listIcon.toString());
                    } else {
                        dataWeather = weatherPreferences.getDataDetails();
                        listKota = Arrays.asList(dataWeather.get(WeatherPreferences.NAMAKOTA).replace("[", "").replace("]", "").split(", "));
                        listTemp = Arrays.asList(dataWeather.get(WeatherPreferences.TEMP).replace("[", "").replace("]", "").split(", "));
                        listIcon = Arrays.asList(dataWeather.get(WeatherPreferences.ICON_TEMP).replace("[", "").replace("]", "").split(", "));
                        boolean available = false;
                        for (int i = 0; i < listKota.size(); i++) {
                            if (listKota.get(i).equals(kota)) {
                                available = true;
                                break;
                            }
                        }
                        if (available == false) {
                            Log.v(TAG, kota);
                            List<String> listKotaBaru = new ArrayList<String>(listKota);
                            listKotaBaru.add(kota);
                            List<String> listTempBaru = new ArrayList<String>(listTemp);
                            listTempBaru.add(temp);
                            List<String> listIconBaru = new ArrayList<String>(listIcon);
                            listIconBaru.add(icon_temp);

                            weatherPreferences.saveData(listKotaBaru.toString(), listTempBaru.toString(), listIconBaru.toString());
                        }
                    }
                    textViewCityNow.setText(kota);
                    textViewCityNow.setSelected(true);

                    latitude = response.body().getCoord().getLat();
                    longitude = response.body().getCoord().getLon();
                    getForecast(latitude, longitude, unit, appid, satuan[0]);
                }

                @Override
                public void onFailure(Call<DataModelWeatherToday.Example> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getForecast(String latitude, String longitude, String unit, String appid, final String satuan) {
        Call<DataModelForecast.Example> forecast = openWeatherService.getForecast(latitude, longitude, unit, appid);
        forecast.enqueue(new Callback<DataModelForecast.Example>() {
            @Override
            public void onResponse(Call<DataModelForecast.Example> call, Response<DataModelForecast.Example> response) {
                listForecast = response.body().getList();

                tempTodayAdapter = new TempTodayAdapter(listForecast, satuan);
                recyclerViewHourly.setAdapter(tempTodayAdapter);

                tempDailyAdapter = new TempDailyAdapter(listForecast, satuan);
                recyclerViewDaily.setAdapter(tempDailyAdapter);
            }

            @Override
            public void onFailure(Call<DataModelForecast.Example> call, Throwable t) {
                t.printStackTrace();
            }
        });


        Call<DataModelGeoCode.Example> dataGeoCode = googleMapService.getGeocodeData(latitude + ", " + longitude, BuildConfig.GEOCODE_API_KEY);
        dataGeoCode.enqueue(new Callback<DataModelGeoCode.Example>() {
            @Override
            public void onResponse(Call<DataModelGeoCode.Example> call, Response<DataModelGeoCode.Example> response) {
                int n = response.body().getResults().size() - 2;
                if (n < 0) n = 0;

                Call<DataModelPlace.Example> dataPlace = googleMapService.getPlaceData(response.body().getResults().get(n).getPlaceId(), BuildConfig.PLACE_API_KEY);
                dataPlace.enqueue(new Callback<DataModelPlace.Example>() {
                    @Override
                    public void onResponse(Call<DataModelPlace.Example> call, Response<DataModelPlace.Example> response) {
                        dialog.dismiss();
                        if (response.body().getResult().getPhotos() != null) {
                            Log.v(TAG, "Ada");
                            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&photoreference=";
                            String photo_reference = response.body().getResult().getPhotos().get(0).getPhotoReference();
                            url += photo_reference + "&key=" + BuildConfig.PLACE_API_KEY;

                            if (imageViewCity != null)
                                Glide.with(MainActivity.this).load(url).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        imageViewCity.setImageDrawable(getResources().getDrawable(R.drawable.sleman_image));
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Log.v(TAG, resource.toString());
                                        return false;
                                    }
                                }).into(imageViewCity);
                        }
                    }

                    @Override
                    public void onFailure(Call<DataModelPlace.Example> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<DataModelGeoCode.Example> call, Throwable t) {

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
    }

}
