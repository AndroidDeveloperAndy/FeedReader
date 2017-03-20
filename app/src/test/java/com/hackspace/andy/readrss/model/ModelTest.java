package com.hackspace.andy.readrss.model;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.FeedReaderForTest;
import com.hackspace.andy.readrss.model.Entity.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@Config(constants = BuildConfig.class,application = FeedReaderForTest.class,sdk = 22)
public class ModelTest {

    private Message mMessage;

    @Before
    public void setUp() {
        mMessage = new Message();
        setData();
    }

    @Test
    public void checkDataNotNull() {
        assertNotNull(mMessage.getTitle());
        assertNotNull(mMessage.getDate());
        assertNotNull(mMessage.getDescription());
        assertNotNull(mMessage.getLink());
    }

    public void setData(){
        mMessage.setTitle("title");
        mMessage.setDate("date");
        mMessage.setDescription("description");
        mMessage.setLink("link");
    }
}
