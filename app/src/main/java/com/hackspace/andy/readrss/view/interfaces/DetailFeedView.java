package com.hackspace.andy.readrss.view.interfaces;

public interface DetailFeedView {
    void loadViews();
    void getInfoFromActivity();
    void loadDetailFeedFromNetwork();
    void loadDetailFeedFromDatabase();
    void getData();
    void showError();
}
