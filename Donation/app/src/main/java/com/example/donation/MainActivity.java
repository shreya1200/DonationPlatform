package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.security.AuthProvider;
import java.util.Timer;//transition from one activity to another after a specific interval of time
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_SCREEN=5000; //capital letters as variable is static set to 5 sec

    //variables
    Animation topAnim, bottomAnim;
    ImageView logoimg;
    ImageView name;
    TextView slogan;
    FirebaseAuth fauth;

    void newUser(){
        Intent intent = new Intent(MainActivity.this, Onboarding.class);
        startActivity(intent);
    }

    void alreadyLoggedIn(){
        Intent intent = new Intent(MainActivity.this, profile.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        fauth = FirebaseAuth.getInstance();
        //animations

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks
        logoimg = findViewById(R.id.loginlogo);
        name = findViewById(R.id.ekiya);
        slogan = findViewById(R.id.tagline);

        //assigning animations

        logoimg.setAnimation(bottomAnim);
        name.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = fauth.getCurrentUser();
                if(user!=null){
                    System.out.println("xxxxxxxxxxxxxxxxxx : "+user.getUid());
                    alreadyLoggedIn();
                }else{
                    newUser();
                }
                finish();
            }

        }, SPLASH_SCREEN);
    }}
