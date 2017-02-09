package com.hackspace.andy.readrss.view.Implementation;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

@EActivity
public class SplashActivity extends AppCompatActivity {

    private int LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(()->{
        PrimaryFeedActivity_.intent(this).start();
        finish();
        },LENGTH);
    }
}
