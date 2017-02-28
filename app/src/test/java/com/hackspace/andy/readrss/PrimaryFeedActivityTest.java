package com.hackspace.andy.readrss;

import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.util.DefaultConfig;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATE_SDK)
public class PrimaryFeedActivityTest {

    private PrimaryFeedActivity mActivity;
    private Message message;

    @Before
    public void setUp() throws Exception{
        mActivity = new PrimaryFeedActivity();
        message = new Message();
    }

    @Test
    public void checkActivityNotNull() throws Exception{
        assertNotNull(mActivity);
    }
    @Test
    public void checkMessageNotNull() throws Exception{
        assertNotNull(message);
    }
}
