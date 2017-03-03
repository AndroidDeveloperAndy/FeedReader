package com.hackspace.andy.readrss.model;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.model.Implementation.MessageService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@Config(constants = BuildConfig.class)
public class ModelTest {

    private MessageService mMessageService;

    @Before
    public void newMessage() {
        mMessageService = new MessageService();
    }

    @Test
    public void checkDataNotNull() {
        assertNotNull(mMessageService.query());
    }
}
