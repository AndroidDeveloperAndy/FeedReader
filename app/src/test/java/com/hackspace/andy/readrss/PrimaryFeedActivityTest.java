package com.hackspace.andy.readrss;

import android.content.Context;
import android.widget.TextView;

import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.util.DefaultConfig;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATE_SDK)
public class PrimaryFeedActivityTest {

    private PrimaryFeedActivity mActivity;
    private Context mContext;
    private TextView mNameFeed;
    private TextView mDateFeed;
    private Message message;

    @Before
    public void setUp() throws Exception{
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mActivity = Robolectric.setupActivity(PrimaryFeedActivity.class);
        mNameFeed = (TextView) mActivity.findViewById(R.id.feed);
        mDateFeed = (TextView) mActivity.findViewById(R.id.dateFeed);
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

    @Test
    public void checkTextNotNull() throws Exception{
        assertNotNull(mNameFeed);
        assertNotNull(mDateFeed);
    }
}
