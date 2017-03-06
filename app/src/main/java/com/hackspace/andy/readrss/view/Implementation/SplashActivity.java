package com.hackspace.andy.readrss.view.implementation;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import static com.hackspace.andy.readrss.util.ResourceUtils.LENGTH;

@EActivity
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(()->{
        PrimaryFeedActivity_.intent(this).start();
        finish();
        }, LENGTH);
    }
}
