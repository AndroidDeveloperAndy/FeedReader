package com.hackspace.andy.readrss.presenter.interfaces;

import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import java.util.List;

public interface PrimaryFeedPresenterImpl {
    List<Message> getNews();
    void setView(PrimaryFeedView view);
}
