package com.hackspace.andy.readrss.activity;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.FeedReaderForTest;
import com.hackspace.andy.readrss.view.implementation.DetailFeedActivity_;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,application = FeedReaderForTest.class,sdk = 22)
public class DetailFeedActivityTest {

    private DetailFeedActivity_ mActivity;

    @Before
    public void setUp() {
        mActivity = Robolectric.setupActivity(DetailFeedActivity_.class);
    }

    @Test
    public void checkActivityNotNull() {
        assertNotNull(mActivity);
    }

    @Test
    public void checkNameApp(){
        assertTrue(mActivity.getTitle().equals("eReader Habra"));
    }
}