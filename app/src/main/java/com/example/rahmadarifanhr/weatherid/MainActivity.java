package com.example.rahmadarifanhr.weatherid;

import android.app.ProgressDialog;
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
import com.example.rahmadarifanhr.weatherid.adapter.TempDailyAdapter;
import com.example.rahmadarifanhr.weatherid.adapter.TempTodayAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    public final static String TAG = MainActivity.class.getSimpleName();

    OpenWeatherService openWeatherService;
    GoogleMapService googleMapService;

    TextView textViewCityNow, textViewTempNow, textViewDescNow, textViewKelembapan, textViewTekanan, textViewKecepatanUdara;
    ImageView imageViewKecepatanUdara, imageViewCity;
    public RecyclerView recyclerViewHourly, recyclerViewDaily;
    public TempTodayAdapter tempTodayAdapter;
    public TempDailyAdapter tempDailyAdapter;
    List<DataModelForecast.List> listForecast;

    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_FINE_LOCATION_ACCESS = 1;
    private Location myLocation;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = ProgressDialog.show(this, "", "Mengambil Data", true);
        dialog.show();

        listForecast = new ArrayList<>();

        textViewCityNow = (TextView) findViewById(R.id.city_name);
        textViewTempNow = (TextView) findViewById(R.id.temp_now);
        textViewDescNow = (TextView) findViewById(R.id.desc_now);
        textViewKelembapan = (TextView) findViewById(R.id.kelembapan);
        textViewTekanan = (TextView) findViewById(R.id.tekanan);
        textViewKecepatanUdara = (TextView) findViewById(R.id.kecepatan_udara);
        imageViewKecepatanUdara = (ImageView) findViewById(R.id.icon_kecepatan_udara);
        imageViewCity = (ImageView) findViewById(R.id.city_image);
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

            String latitude = String.valueOf(myLocation.getLatitude());
            String longitude = String.valueOf(myLocation.getLongitude());
            final String unit = "metric";
            String appid = BuildConfig.OPENWEATHER_API_KEY;

            Call<DataModelWeatherToday.Example> call = openWeatherService.getWeatherToday(latitude, longitude, unit, appid);
            call.enqueue(new Callback<DataModelWeatherToday.Example>() {
                @Override
                public void onResponse(Call<DataModelWeatherToday.Example> call, Response<DataModelWeatherToday.Example> response) {
                    textViewCityNow.setText(response.body().getName());
                    textViewCityNow.setSelected(true);
                    textViewTempNow.setText(response.body().getMain().getTemp() + getResources().getString(R.string.celcius));
                    textViewDescNow.setText(response.body().getWeather().get(0).getDescription());

                    textViewKelembapan.setText(response.body().getMain().getHumidity() + " %");
                    textViewTekanan.setText(response.body().getMain().getPressure() + " hPa");
                    textViewKecepatanUdara.setText(response.body().getWind().getSpeed() + " m/s");
                }

                @Override
                public void onFailure(Call<DataModelWeatherToday.Example> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            Call<DataModelForecast.Example> call1 = openWeatherService.getForecast(latitude, longitude, unit, appid);
            call1.enqueue(new Callback<DataModelForecast.Example>() {
                @Override
                public void onResponse(Call<DataModelForecast.Example> call, Response<DataModelForecast.Example> response) {
                    listForecast = response.body().getList();

                    tempTodayAdapter = new TempTodayAdapter(listForecast);
                    recyclerViewHourly.setAdapter(tempTodayAdapter);

                    tempDailyAdapter = new TempDailyAdapter(listForecast);
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
                                            imageViewCity.setImageDrawable(getResources().getDrawable(R.drawable.pekanbaru_image));
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
    }

}
