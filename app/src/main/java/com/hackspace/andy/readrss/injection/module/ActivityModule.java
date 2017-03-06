package com.hackspace.andy.readrss.injection.module;

import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    @Singleton
    PrimaryFeedPresenterImpl providesTopPresenter() {
        return new PrimaryFeedPresenter();
    }

    @Provides
    @Singleton
    MessageService provideService() {
        return new MessageService();
    }
}
