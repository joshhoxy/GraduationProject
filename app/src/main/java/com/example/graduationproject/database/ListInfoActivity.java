package com.example.graduationproject.database;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListInfoActivity extends AppCompatActivity {
    public static final String TAG = "FB";
    public FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference logRef;
    private DatabaseReference userRef;
    private ChildEventListener mChild;
    //List01Adapter adapter;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();
    TextView tv_userName;
    TextView tv_userEmail;
    Uri userProfileURL;
    public String UID;
    ImageView img_userProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageButton likeBtn = findViewById(R.id.btn_like);

        // user profile
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UID = user.getUid();
        tv_userName = findViewById(R.id.userName);
        tv_userEmail = findViewById(R.id.userEmail);
        img_userProfile = findViewById(R.id.userProfile);
        mAuth = FirebaseAuth.getInstance();
        userProfileURL = user.getPhotoUrl();
        mDatabase = FirebaseDatabase.getInstance();
        userRef = mDatabase.getReference().child("User").child(UID);

        // load from_user profile
        userRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue().toString();
                Log.d(TAG,"user name is loaded as: "+ name);
                tv_userName.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        tv_userEmail.setText(user.getEmail());
        if(userProfileURL !=null){
            Glide.with(this).load(userProfileURL).into(img_userProfile);
            Log.d("facebook",img_userProfile.toString());
        }

        initDatabase();

        // search list
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String >(this,android.R.layout.simple_dropdown_item_1line,new ArrayList<String>());
        listView.setAdapter(adapter);

        logRef = userRef.child("log");
        logRef = logRef.push();
        logRef.child("name").setValue("child check");
        logRef.child("rating").setValue(4);
        //temp
        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"data snapshot is loaded as: "+ dataSnapshot.toString());
                Log.d(TAG, "data snapshot_name: " + dataSnapshot.child("name").getValue());
                String log = dataSnapshot.getValue().toString();
                Log.d(TAG,"user log is loaded as: "+ log );
                //Array.add(log);
                adapter.add(log);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListInfoActivity.this, ListLikeActivity.class));
            }
        });
    }


    private void initDatabase() {

        logRef = mDatabase.getReference("User").child(UID);

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        logRef.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logRef.removeEventListener(mChild);
    }

}