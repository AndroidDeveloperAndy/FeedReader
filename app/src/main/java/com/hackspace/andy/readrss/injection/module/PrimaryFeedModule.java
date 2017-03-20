package com.hackspace.andy.readrss.injection.module;

import android.content.Context;

import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PrimaryFeedModule{

    @Singleton
    @Provides
    PrimaryFeedPresenterImpl providesPrimaryPresenter() {
        return new PrimaryFeedPresenter();
    }

    @Singleton
    @Provides
    MessageService provideService(Context context) {
        return new MessageService(context);
    }
}
