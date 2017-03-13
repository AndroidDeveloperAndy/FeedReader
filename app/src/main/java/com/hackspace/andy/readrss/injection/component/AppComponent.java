package com.hackspace.andy.readrss.injection.component;

import com.hackspace.andy.readrss.injection.module.PrimaryFeedModule;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PrimaryFeedModule.class})
public interface AppComponent{
    void inject(PrimaryFeedActivity primaryFeedActivity);
}
