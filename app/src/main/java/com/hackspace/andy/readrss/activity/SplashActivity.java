package com.hackspace.andy.readrss.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackspace.andy.readrss.R;

public class SplashActivity extends AppCompatActivity {

    private Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        homeIntent = new Intent(this, PrimaryFeedActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
