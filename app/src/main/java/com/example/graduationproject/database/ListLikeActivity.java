package com.example.graduationproject.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.graduationproject.R;

public class ListLikeActivity extends AppCompatActivity {
    ListLikeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ListView listView = findViewById(R.id.listview);
        adapter = new ListLikeAdapter(this);
        listView.setAdapter(adapter);
    }
}