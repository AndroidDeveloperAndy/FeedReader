package com.hackspace.andy.readrss.injection.module;

import android.app.Application;
import android.content.Context;

import com.hackspace.andy.readrss.injection.ApplicationContext;
import com.hackspace.andy.readrss.model.Implementation.MessageService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    MessageService provideFeedService() {
        return new MessageService(mApplication.getApplicationContext());
    }
}
