package com.hackspace.andy.readrss.injection.component;

import android.app.Application;
import android.content.Context;

import com.hackspace.andy.readrss.injection.ApplicationContext;
import com.hackspace.andy.readrss.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ApplicationContext Context context();
    Application application();
}
