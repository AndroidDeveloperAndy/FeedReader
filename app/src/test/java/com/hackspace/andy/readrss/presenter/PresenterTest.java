package com.hackspace.andy.readrss.presenter;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.FeedReaderForTest;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,application = FeedReaderForTest.class,sdk = 22)
public class PresenterTest {

    private PrimaryFeedPresenter mPresenter;
    private PrimaryFeedPresenter spyPresenter;

    @Mock PrimaryFeedView mPrimaryFeedView;
    @Mock MessageService mMessageService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mPresenter = new PrimaryFeedPresenter();
        mPresenter.setView(mPrimaryFeedView);
        spyPresenter = spy(mPresenter);
    }

    @Test
    public void getDataWithInternet(){
        spyPresenter.getNews();
        mPrimaryFeedView.getFeedFromNetwork();
        when(mMessageService.hasRanks()).thenReturn(false);
        verify(spyPresenter).getNews();
        verify(mPrimaryFeedView).getFeedFromNetwork();
    }

    @Test
    public void getDataWithDatabase(){
        mPrimaryFeedView.getFeedFromDatabase();
        when(mMessageService.hasRanks()).thenReturn(true);
        verify(mPrimaryFeedView).getFeedFromDatabase();
    }
}
