package com.example.graduationproject.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.graduationproject.login.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {
    public static final String TAG = "Database";
    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public String userName;
    public String email;

    public User() {
        //Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String email){
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email= email;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    @Override
    public String toString(){
        return "User{" + "userName='" + userName + '\'' +
        ", email='" + email + '\'' + '}';
    }

    public void writeNewUser(String UID, String name, String email){
        User user = new User(name,email);
        mDatabase.child("User").child(UID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                Log.d(TAG,"write success");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Log.d(TAG,"write failed");
                    }
                });
    }
}
