package com.mvs.testapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mvs.testapplication.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
