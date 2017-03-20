package com.hackspace.andy.readrss.model.interfaces;

import com.hackspace.andy.readrss.model.Entity.Message;

import java.util.List;

import io.realm.RealmResults;

public interface MessagesServiceImpl {

    void insert(List<Message> messages);
    RealmResults<Message> query();
    boolean hasRanks();
}
