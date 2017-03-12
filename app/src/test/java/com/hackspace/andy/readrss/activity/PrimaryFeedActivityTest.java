package com.hackspace.andy.readrss.activity;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.util.DefaultConfig;
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
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATE_SDK)
public class PrimaryFeedActivityTest {

    @Mock
    public static PrimaryFeedPresenter mPrimaryPresenter;
    private PrimaryFeedActivity_ mActivity;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mActivity = Robolectric.setupActivity(PrimaryFeedActivity_.class);
    }

    @Test
    public void checkActivityNotNull() throws Exception{
        assertNotNull(mActivity);
    }

    @Test
    public void checkGetData() {
        verify(mPrimaryPresenter).getNews();
    }
}
