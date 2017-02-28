package com.hackspace.andy.readrss.injection.component;

import com.hackspace.andy.readrss.injection.ConfigPersistent;
import com.hackspace.andy.readrss.injection.module.ActivityModule;

import dagger.Component;

@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);

}
