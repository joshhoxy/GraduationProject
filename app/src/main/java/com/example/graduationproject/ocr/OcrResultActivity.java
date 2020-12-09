package com.example.graduationproject.ocr;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.parse.MapMainActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class OcrResultActivity extends AppCompatActivity implements Runnable{

    TextView ocr_result_txtView;
    TextView place_now_txtView;
    TextView place_result_txtView;

    String result;
    GpsTracker gpsTracker;
    PlaceAPI places;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_result);

        ocr_result_txtView = (TextView) findViewById(R.id.ocr_result_view);
        place_now_txtView = (TextView) findViewById(R.id.place_now_view);
        place_result_txtView = (TextView) findViewById(R.id.place_result_view);

        //인텐트로 받아온 ocr 결과값을 꺼낸다
        Intent intent = getIntent();
        result = intent.getStringExtra("ocr_result");
        //Log.d("check", result);
        ocr_result_txtView.setText(result);

        //gpsTracker 클래스 사용해서 현재 위치를 알아냄
        gpsTracker = new GpsTracker(OcrResultActivity.this);
        //LatLng currentLatLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        String now_location = latitude + ", " + longitude;
        place_now_txtView.setText(now_location);

        Thread t = new Thread(this);
        t.start();

    }


    @Override
    public void run() {

        //현재 위치 근방에 있는 가게들의 정보를 알아온다.
        places = new PlaceAPI(this, latitude, longitude, 500.00, "restaurant");
        places.parsing();

        ArrayList<StoreData> store_data = new ArrayList<>();
        store_data = places.getList();

        //Log.d("check", "check " + result);
        for(StoreData data : store_data)
        {
            Log.d("check", data.getStore_name());
            Log.d("check", "check" + result);
            if(data.getStore_name().equalsIgnoreCase(result))
                place_result_txtView.setText(data.getRating().toString());
        }

        while(true)
        {
            try {
                Thread.sleep(50);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}