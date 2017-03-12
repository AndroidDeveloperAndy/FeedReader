package com.hackspace.andy.readrss.injection.module;

import android.content.Context;

import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Singleton
    @Provides
    PrimaryFeedPresenter providesTopPresenter() {
        return new PrimaryFeedPresenter();
    }

    @Singleton
    @Provides
    MessageService provideService(Context context) {
        return new MessageService(context);
    }
}
