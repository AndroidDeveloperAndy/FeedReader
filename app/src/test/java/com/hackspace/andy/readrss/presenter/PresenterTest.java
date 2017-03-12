package com.hackspace.andy.readrss.presenter;

import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.util.DefaultConfig;
import com.hackspace.andy.readrss.util.NetworkUtil;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity_;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = DefaultConfig.class)
public class PresenterTest {

    private PrimaryFeedPresenter mPresenter;
    private List<Message> mMessageList;

    @Mock PrimaryFeedView mPrimaryFeedView;
    @Mock MessageService mMessageService;
    @Mock PrimaryFeedActivity_ mPrimaryFeedActivity;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mPresenter = new PrimaryFeedPresenter();
        mPresenter.setView(mPrimaryFeedView);
        Message message = new Message();
        message.setTitle("News");
        message.setDate("12.03.2017");
        message.setDescription("Word of the News");
        message.setLink("habrhabra.ru");
        mMessageList = new ArrayList<>();
        mMessageList.add(message);
    }

    @Test
    public void getDataWithInternet(){
        when(NetworkUtil.isNetworkAvailable(mPrimaryFeedActivity)).thenReturn(true);
        when(mPresenter.getNews()).thenReturn(Collections.EMPTY_LIST);
        verify(mPrimaryFeedView).getFeedFromNetwork();
        verify(mMessageService).insert(mMessageList);
    }

    @Test
    public void getDataWithDatabase(){
        when(NetworkUtil.isNetworkAvailable(mPrimaryFeedActivity)).thenReturn(false);
        verify(mPrimaryFeedView).getFeedFromDatabase();
    }

    @Test
    public void getError() {
        mPresenter.getNews();
        verify(mPrimaryFeedView).showError();
    }
}
