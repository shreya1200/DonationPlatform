package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;//transition from one activity to another after a specific interval of time
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_SCREEN=5000; //capital letters as variable is static set to 5 sec

    //variables
    Animation topAnim, bottomAnim;
    ImageView logoimg;
    ImageView name;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this, Onboarding.class);
                startActivity(intent);
                finish();

                //  Pair[] pairs =new Pair[2];
                // pairs[0] = new Pair<View,String>(logoimg,"logo_image");
                //pairs[1] = new Pair<View,String>(name,"logo_text");

                //if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                //  ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(IntroPage.this, pairs);
                // startActivity(intent,options.toBundle());  //options .bundle will carrry our animation
            }

        }, SPLASH_SCREEN);
    }}
//    Timer timer;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this,homepage.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 1000);
//    }
