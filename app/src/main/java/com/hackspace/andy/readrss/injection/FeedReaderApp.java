package com.hackspace.andy.readrss.injection;

import android.app.Application;

import com.hackspace.andy.readrss.injection.component.ActivityComponent;
import com.hackspace.andy.readrss.injection.module.ActivityModule;

import io.realm.Realm;

public class FeedReaderApp extends Application {

    private static ActivityComponent sComponent;
    public static ActivityComponent getComponent(){
        return sComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        sComponent = buildComponent();
    }

    private ActivityComponent buildComponent() {
        return DaggerAppComponent.builder()
                .topActivityModule(new ActivityModule())
                .build();
    }

}

