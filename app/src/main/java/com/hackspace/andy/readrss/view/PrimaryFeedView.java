package com.hackspace.andy.readrss.view;

public interface PrimaryFeedView {
    void createViews();
    void getFeedFromNetwork();
    void getFeedFromDatabase();
    void getData();
    void getAlertDialog();
}
