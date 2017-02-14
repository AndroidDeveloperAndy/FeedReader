package com.hackspace.andy.readrss.model.Implementation;

import android.app.Activity;
import android.content.Context;

import com.hackspace.andy.readrss.model.MessagesServiceImpl;
import com.hackspace.andy.readrss.model.Entity.Message;

import org.androidannotations.annotations.EBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

@EBean
public class MessageService implements MessagesServiceImpl {

    private Realm mRealm;
    private static MessageService sInstance;
    private Context mContext;
    private RealmConfiguration mConfigRealm;


    public MessageService(Context context) {
        this.mContext = context;
        mConfigRealm = new RealmConfiguration.Builder(context.getApplicationContext()).build();
        Realm.setDefaultConfiguration(mConfigRealm);
        mRealm = Realm.getDefaultInstance();
    }

    public static MessageService getInstance() {
        return sInstance;
    }

    public static MessageService with(Activity activity) {

        if (sInstance == null) {
            sInstance = new MessageService(activity.getApplication());
        }
        return sInstance;
    }

    @Override
    public void insert(List<Message> messages) {
        mRealm = Realm.getDefaultInstance();
        for (Message message : messages) {
            mRealm.executeTransaction(transaction -> mRealm.copyToRealm(message));
        }
    }

    @Override
    public RealmResults<Message> query(){
        return mRealm.where(Message.class).findAll();
    }

    @Override
    public boolean hasMessages() {
        return !mRealm.allObjects(Message.class).isEmpty();
    }
}
