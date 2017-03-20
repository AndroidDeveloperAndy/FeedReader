package com.hackspace.andy.readrss.activity;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.FeedReaderForTest;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity_;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,application = FeedReaderForTest.class,sdk = 22)
public class PrimaryFeedActivityTest {

    private PrimaryFeedActivity_ mActivity;
    @Mock public static PrimaryFeedPresenter mPrimaryPresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mActivity = Robolectric.setupActivity(PrimaryFeedActivity_.class);
    }

    @Test
    public void checkActivityNotNull() {
        assertNotNull(mActivity);
    }

    @Test
    public void checkGetDataFromInternet() {
        assertNotNull(mPrimaryPresenter.getNews());
        verify(mPrimaryPresenter).getNews();
    }
}
