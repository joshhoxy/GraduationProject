package com.example.graduationproject.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView tv_name;
    private TextView tv_email;
    private EditText et_user;
    private EditText et_email;
    private Button sendButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef =database.getReference("message");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_main);

        tv_name = findViewById(R.id.textView_1);
        tv_email = findViewById(R.id.textView_2);
        et_user = findViewById(R.id.editTextView_1);
        et_email = findViewById(R.id.editTextView_2);
        sendButton = findViewById(R.id.btnSend);

    }
}



