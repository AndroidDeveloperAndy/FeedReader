package com.hackspace.andy.readrss.view.Implementation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private Intent homeIntent;
    private int LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(()->{
        homeIntent = new Intent(SplashActivity.this, PrimaryFeedActivity.class);
        startActivity(homeIntent);
        finish();
        },LENGTH);
    }
}
