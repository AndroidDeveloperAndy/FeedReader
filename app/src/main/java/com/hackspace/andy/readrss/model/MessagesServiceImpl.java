package com.hackspace.andy.readrss.model;

import java.util.List;

import io.realm.RealmResults;

public interface MessagesServiceImpl {

    public void insert(List<Message> messages);
    RealmResults<Message> query();
    public boolean hasMessages();
}
