package com.hackspace.andy.readrss.activity;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.view.implementation.DetailFeedActivity;
import com.hackspace.andy.readrss.view.implementation.DetailFeedActivity_;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DetailFeedActivityTest {

    private DetailFeedActivity mActivity;

    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.setupActivity(DetailFeedActivity_.class);
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(mActivity);
    }
}