package com.hackspace.andy.readrss.injection;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hackspace.andy.readrss.injection.component.ActivityComponent;
import com.hackspace.andy.readrss.injection.module.ActivityModule;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import io.realm.Realm;

public class FeedReaderApp extends Application {

    private static FeedReaderApp sApplication;
    private static ActivityComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Realm.init(this);
        sApplication = this;
        sAppComponent = buildComponent();
    }

    private ActivityComponent buildComponent() {
        return DaggerAppComponent.builder()
                .topActivityModule(new ActivityModule(sApplication.getApplicationContext()))
                .build();
    }

    public ActivityComponent getComponent(){
        return sAppComponent;
    }
}

