package com.hackspace.andy.readrss.presenter;

import com.hackspace.andy.readrss.model.Entity.Message;

import java.util.List;

public interface PrimaryFeedPresenterImpl {
    List<Message> getNews();
}
