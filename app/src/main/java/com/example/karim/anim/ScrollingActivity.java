package com.example.karim.anim;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollingActivity extends AppCompatActivity {

    TextView mWeather, uf;

    public String q;
    public String units;
    public String appid;
    Get get;
    public String content;
    public float latV;
    public float lonV;
    public float lat;
    public float lon;
    public float tempmin;
    public float tempmax;
    public float sea;


    public void parse(){
        q = "якутск";
        units = "metric";
        appid = "14694abc40a51e1b28c053fdea1faa06";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        JsonPlace jsonPlace = retrofit.create(JsonPlace.class);
        Call<Get> call = jsonPlace.getGets(q, units, appid);
        call.enqueue(new Callback<Get>() {
            @Override
            public void onResponse(Call<Get> call, Response<Get> response) {
                get = response.body();

                content = String.valueOf(get.getMain().getTemp());

                lat = ((float)get.getCoord().getLat());
                lon = ((float)get.getCoord().getLon());

                mWeather.setText(content);

                SharedPreferences mS = getSharedPreferences("coord", MODE_PRIVATE);
                SharedPreferences.Editor eD = mS.edit();
                eD.putFloat("lat", lat);
                eD.putFloat("lon", lon);
                eD.apply();
            }

            @Override
            public void onFailure(Call<Get> call, Throwable t) {

            }
        });

        SharedPreferences mS = getSharedPreferences("coord", MODE_PRIVATE);
        lat = mS.getFloat("lat", 0);
        lon = mS.getFloat("lon", 0);
        Call<Get> call2 = jsonPlace.getGets(lat, lon, appid);
        call2.enqueue(new Callback<Get>() {
            @Override
            public void onResponse(Call<Get> call, Response<Get> response) {
                get = response.body();

                sea = ((float)get.getMain().getSea_level());


                uf.setText(sea + "");


            }

            @Override
            public void onFailure(Call<Get> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        uf = findViewById(R.id.uf);


        mWeather = findViewById(R.id.mWeather);

        parse();


    }
}
