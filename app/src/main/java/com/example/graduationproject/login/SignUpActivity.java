package com.example.graduationproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.R;
import com.example.graduationproject.database.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "signuplog";
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userRef = FirebaseDatabase.getInstance().getReference().child("User");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_register).setOnClickListener(onClickListener);
        findViewById(R.id.btn_goToLoginActivity).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btn_register:
                    signUp();
                    break;

                case R.id.btn_goToLoginActivity:
                    startLoginActivity();
                    break;
            }
        }
    };

    private void signUp(){

        EditText e_text = (EditText)findViewById(R.id.emailEditText);
        EditText n_text = (EditText)findViewById(R.id.nameEditText);
        EditText p_text = (EditText)findViewById(R.id.passwordEditText);
        EditText p_ch_text = (EditText)findViewById(R.id.passwordCheckEditText);

        String email = e_text.getText().toString();
        String name = n_text.getText().toString();
        String password = p_text.getText().toString();
        String passwordCheck = p_ch_text.getText().toString();

        if (email.length()>0 && password.length()>0 && passwordCheck.length()>0) {

            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String UID = user.getUid();
                                    //User newUser = new User(name,email);
                                    Log.d(TAG,user.getEmail() + "name: "+ user.getDisplayName());
                                    userRef.child(UID).child("name").setValue(name);
                                    userRef.child(UID).child("email").setValue(email);
                                    startToast("회원가입에 성공하였습니다.");
                                    startLoginActivity();
                                    //UI
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                        Log.d(TAG, "파이어베이스 연동 실패");
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }else {
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void startLoginActivity(){
        Intent intent = new Intent (this,com.example.graduationproject.login.LoginActivity.class);
        startActivity(intent);
    }

}

