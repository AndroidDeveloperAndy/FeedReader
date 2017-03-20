package com.hackspace.andy.readrss.model.Implementation;

import android.content.Context;

import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.model.interfaces.MessagesServiceImpl;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MessageService implements MessagesServiceImpl {

    private Context mContext;

    public MessageService(Context context) {
        this.mContext = context;
    }

    @Override
    public void insert(List<Message> messages) {
        for (Message message : messages) {
            Realm.getDefaultInstance().executeTransaction(transaction -> Realm.getDefaultInstance().copyToRealm(message));
        }
    }

    @Override
    public RealmResults<Message> query() {
        return Realm.getDefaultInstance().where(Message.class).findAll();
    }

    @Override
    public boolean hasRanks() {
        return Realm.getDefaultInstance().where(Message.class).count()!= 0;
    }
}
