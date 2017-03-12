package com.hackspace.andy.readrss;

import android.app.Application;

import com.hackspace.andy.readrss.injection.component.ActivityComponent;
import com.hackspace.andy.readrss.injection.component.DaggerActivityComponent;
import com.hackspace.andy.readrss.injection.module.ActivityModule;

public class FeedReaderApp extends Application {

    private static ActivityComponent sComponent;

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

    public static ActivityComponent getComponent(){
        return sComponent;
    }

}

