package com.hackspace.andy.readrss;

import android.app.Application;

import com.hackspace.andy.readrss.injection.component.AppComponent;
import com.hackspace.andy.readrss.injection.component.DaggerAppComponent;
import com.hackspace.andy.readrss.injection.module.PrimaryFeedModule;

import io.realm.Realm;

public class FeedReaderApp extends Application {

    private static AppComponent sComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initialization();
        sComponent = buildComponent();
    }

    AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .primaryFeedModule(new PrimaryFeedModule())
                .build();
    }

    public static AppComponent getComponent(){
        return sComponent;
    }

    void initialization(){
        Realm.init(this);
    }
}

