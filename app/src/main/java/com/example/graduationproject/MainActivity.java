package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.parse.MapMain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        Button loginBtn = findViewById(R.id.btnLogin);
        Button ocrBtn = findViewById(R.id.btnOcr);
        Button mapBtn = findViewById(R.id.btnMap);
        Button dataBtn = findViewById(R.id.btnData);*/


        /*loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.graduationproject.login.SignUpActivity.class);
                startActivity(intent);
            }
        });

        ocrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.graduationproject.ocr.MainActivity.class);
                startActivity(intent);
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapMain.class);
                startActivity(intent);
            }
        });

        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.graduationproject.database.MainActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
