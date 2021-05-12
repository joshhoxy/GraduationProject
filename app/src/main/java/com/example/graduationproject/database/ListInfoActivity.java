package com.example.graduationproject.database;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ListInfoActivity extends AppCompatActivity {
    List01Adapter adapter;
    TextView tv_userName;
    TextView tv_userEmail;
    Uri userProfileURL;
    String UID;
    FirebaseAuth mAuth;
    ImageView img_userProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageButton likeBtn = findViewById(R.id.btn_like);
        tv_userName = findViewById(R.id.userName);
        tv_userEmail = findViewById(R.id.userEmail);
        img_userProfile = findViewById(R.id.userProfile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UID = user.getUid();
        userProfileURL = user.getPhotoUrl();
        tv_userName.setText(user.getDisplayName());
        tv_userEmail.setText(user.getEmail());
        Glide.with(this).load(userProfileURL).into(img_userProfile);
        Log.d("facebook",img_userProfile.toString());



        ListView listView = findViewById(R.id.listView);
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