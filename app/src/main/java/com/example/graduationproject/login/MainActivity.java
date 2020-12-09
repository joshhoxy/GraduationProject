package com.example.graduationproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_logout_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignUpActivity();
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
    }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.logoutButton:
                        FirebaseAuth.getInstance().signOut();
                        startSignUpActivity();
                        break;
                }
            }
        };

    private void startSignUpActivity(){
        Intent intent = new Intent (this,com.example.graduationproject.login.SignUpActivity.class);
        startActivity(intent);
    }
}