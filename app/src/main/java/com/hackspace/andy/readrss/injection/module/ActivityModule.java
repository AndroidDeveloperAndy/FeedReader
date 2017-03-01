package com.hackspace.andy.readrss.injection.module;

import android.content.Context;

import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.Implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Context mContext;

    public ActivityModule(Context context){
        this.mContext = context;
    }

    @Singleton
    @Provides
    PrimaryFeedPresenter providePresenter(PrimaryFeedActivity view) {
        return new PrimaryFeedPresenter(view);
    }

    @Singleton
    @Provides
    MessageService provideService() {
        return new MessageService(mContext);
    }
}
