package com.hackspace.andy.readrss.model.Implementation;

import android.app.Activity;
import android.content.Context;

import com.hackspace.andy.readrss.model.MessagesServiceImpl;
import com.hackspace.andy.readrss.model.Entity.Message;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MessageService implements MessagesServiceImpl {

    private Realm mRealm;
    private static MessageService instance;
    private Context mContext;

    public MessageService(Context context) {
        mRealm = Realm.getDefaultInstance();
        this.mContext = context;
    }

    public static MessageService with(Activity activity) {

        if (instance == null) {
            instance = new MessageService(activity.getApplication());
        }
        return instance;
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
