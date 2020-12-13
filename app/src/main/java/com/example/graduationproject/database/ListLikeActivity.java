package com.example.graduationproject.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.graduationproject.R;
import com.example.graduationproject.parse.MapMain;
import com.example.graduationproject.parse.MapMainActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

public class ListLikeActivity extends AppCompatActivity {
    ListLikeAdapter adapter;
    ImageButton mapBtn;
    Location location;
    LatLng coordination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ListView listView = findViewById(R.id.listview);
        adapter = new ListLikeAdapter(this);
        listView.setAdapter(adapter);
    }
}