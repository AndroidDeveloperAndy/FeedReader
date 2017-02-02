package com.hackspace.andy.readrss.view;

import com.hackspace.andy.readrss.model.Entity.Message;

import java.util.List;

public interface PrimaryFeedView {
    void createViews();
    void getFeedFromNetwork();
    List<Message> getFeedFromDatabase();
    void getAlertDialog();
    boolean isOnline();
}
