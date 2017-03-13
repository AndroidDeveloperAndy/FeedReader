package com.hackspace.andy.readrss;

import com.hackspace.andy.readrss.injection.component.AppComponent;
import com.hackspace.andy.readrss.injection.component.DaggerAppComponent;

public class FeedReaderForTest extends FeedReaderApp{

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
    }

    AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .primaryFeedModule(new DaggerModuleForTest())
                .build();
    }
}
