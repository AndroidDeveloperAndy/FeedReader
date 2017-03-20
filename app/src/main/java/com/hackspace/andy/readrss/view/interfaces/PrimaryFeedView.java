package com.hackspace.andy.readrss.view.interfaces;

import com.hackspace.andy.readrss.model.Entity.Message;

import java.util.List;

public interface PrimaryFeedView {
    void getFeedFromNetwork();
    void getFeedFromDatabase();
    void showFeed(List<Message> listFeed);
    void showError();
    void getData();
}
