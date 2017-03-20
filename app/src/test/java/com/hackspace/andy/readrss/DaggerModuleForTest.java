package com.hackspace.andy.readrss;

import com.hackspace.andy.readrss.activity.PrimaryFeedActivityTest;
import com.hackspace.andy.readrss.injection.module.PrimaryFeedModule;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerModuleForTest extends PrimaryFeedModule{

    @Provides
    @Singleton
    public PrimaryFeedPresenterImpl providesPrimaryPresenter() {
        return PrimaryFeedActivityTest.mPrimaryPresenter;
    }
}
