package com.hackspace.andy.readrss.activity;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.util.DefaultConfig;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATE_SDK)
public class PrimaryFeedActivityTest {

    @Mock
    public static PrimaryFeedPresenter mPrimaryPresenter;
    private PrimaryFeedActivity mActivity;
    private Message mMessage;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mActivity = new PrimaryFeedActivity();
        mMessage = new Message();
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
