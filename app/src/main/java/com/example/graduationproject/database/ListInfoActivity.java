package com.example.graduationproject.database;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.R;

public class ListInfoActivity extends AppCompatActivity {
    List01Adapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageButton likeBtn = findViewById(R.id.btn_like);
        ListView listView = findViewById(R.id.listview);
        adapter = new List01Adapter(this);
        listView.setAdapter(adapter);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListInfoActivity.this, ListLikeActivity.class));
            }
        });
    }
}