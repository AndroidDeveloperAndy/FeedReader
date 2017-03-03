package com.hackspace.andy.readrss.injection.module;

import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    @Singleton
    PrimaryFeedPresenterImpl providesTopPresenter(PrimaryFeedActivity view) {
        return new PrimaryFeedPresenter(view);
    }

    @Provides
    @Singleton
    MessageService provideService() {
        return new MessageService();
    }
}
