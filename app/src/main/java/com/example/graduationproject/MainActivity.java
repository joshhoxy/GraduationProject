package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.Login;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    boolean onAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginBtn = (Button) findViewById(R.id.btn_login);
        Button logoutBtn = (Button) findViewById(R.id.btn_logout);
        LinearLayout myListBtn = findViewById(R.id.btn_myList);
        LinearLayout OCR_btn = findViewById(R.id.btn_OCR);

        if (FirebaseAuth.getInstance().getCurrentUser() !=null) {
            onAuth =true;
            Log.d(TAG,"logged in");
            loginBtn.setVisibility(View.INVISIBLE);
            logoutBtn.setVisibility(View.VISIBLE);
        }
        else{
            onAuth = false;
            Log.d(TAG,"logged out");
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.graduationproject.login.LoginActivity.class);
                startActivity(intent);
            }
        });


        myListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAuth == true){
                    Intent intent = new Intent(getApplicationContext(), com.example.graduationproject.database.ListInfoActivity.class);
                    startActivity(intent);
                }
                else{
                    startToast("Please login first.");
                }


            }
        });


        OCR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.graduationproject.ocr.MainActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG,"")
            }
        });

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
