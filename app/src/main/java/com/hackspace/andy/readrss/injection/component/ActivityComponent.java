package com.hackspace.andy.readrss.injection.component;

import com.hackspace.andy.readrss.injection.module.ActivityModule;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(PrimaryFeedActivity primaryFeedActivity);
}
