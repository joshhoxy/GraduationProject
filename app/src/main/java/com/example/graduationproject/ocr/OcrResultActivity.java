package com.example.graduationproject.ocr;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.graduationproject.R;
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

        int i = 0;
        double[] similar = new double[store_data.size()];
        //Log.d("check", "check " + result);
        for(StoreData data : store_data)
        {
            String store_name = data.getStore_name();

            similar[i] = similarity(store_name, result);
           i++;
        }

        double max = 0;
        int max_index = 0;
        for(int j = 0; j < i; j++)
        {
            if(max < similar[j])
            {
                max = similar[j];
                max_index = j;
            }
        }
        place_result_txtView.setText(store_data.get(max_index).getBusiness_status());
        Log.d("check", store_data.get(max_index).getStore_name());

        while(true)
        {
            try {
                Thread.sleep(50);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private double similarity(String s1, String s2){
        String longer = s1, shorter = s2;

        if(s1.length() < s2.length()){
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if(longerLength == 0) return 1.0;

        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private int editDistance(String s1, String s2){
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];

        for(int i = 0; i <= s1.length(); i++)
        {
            int lastValue = i;
            for(int j = 0; j <= s2.length(); j++)
            {
                if(i ==0){
                    costs[j] = j;
                } else {
                    if( j > 0){
                        int newValue = costs[j-1];

                        if(s1.charAt(i - 1) != s2.charAt(j - 1)){
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j])+1;
                        }

                        costs[j-1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }

            if (i > 0) costs[s2.length()] = lastValue;
        }

        return costs[s2.length()];
    }
}