package com.hackspace.andy.readrss.activity;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.util.DefaultConfig;
import com.hackspace.andy.readrss.view.implementation.DetailFeedActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATE_SDK)
public class DetailFeedActivityTest {

    private DetailFeedActivity mActivity;

    @Before
    public void setUp() throws Exception {
        mActivity = new DetailFeedActivity();
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(mActivity);
    }
}