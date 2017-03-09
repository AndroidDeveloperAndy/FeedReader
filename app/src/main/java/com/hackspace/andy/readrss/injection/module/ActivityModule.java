package com.hackspace.andy.readrss.injection.module;

import android.content.Context;

import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    @Singleton
    PrimaryFeedPresenterImpl providesTopPresenter(PrimaryFeedView view) {
        return new PrimaryFeedPresenter(view);
    }

    @Provides
    @Singleton
    MessageService provideService(Context context) {
        return new MessageService(context);
    }
}
