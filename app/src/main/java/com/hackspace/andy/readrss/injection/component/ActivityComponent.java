package com.hackspace.andy.readrss.injection.component;

import com.hackspace.andy.readrss.injection.PerActivity;
import com.hackspace.andy.readrss.injection.module.ActivityModule;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(PrimaryFeedActivity primaryFeedActivity);
}
