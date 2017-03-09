package com.hackspace.andy.readrss.injection;

import android.app.Application;

import com.hackspace.andy.readrss.injection.component.ActivityComponent;
import com.hackspace.andy.readrss.injection.component.DaggerActivityComponent;
import com.hackspace.andy.readrss.injection.module.ActivityModule;

public class FeedReaderApp extends Application {

    private static ActivityComponent sComponent;
    public static ActivityComponent getComponent(){
        return sComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sComponent = buildComponent();
    }

    private ActivityComponent buildComponent() {
        return DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .build();
    }

}

