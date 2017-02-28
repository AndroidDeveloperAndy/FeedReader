package com.hackspace.andy.readrss.view;

public interface DetailFeedView {
    void loadViews();
    void getInfoFromActivity();
    void loadDetailFeed();
    void getDetailFeedFromDatabase();
    void getData();
    void showError();
}
