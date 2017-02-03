package com.hackspace.andy.readrss.view;

public interface DetailFeedView {
    void loadViews();
    void getInfoFromActivity();
    void loadDetailFeed();
    void getDetailFeedFromDatabase();
    void getData();
    boolean isOnline();
    void messageBox(String method, String message);
}
