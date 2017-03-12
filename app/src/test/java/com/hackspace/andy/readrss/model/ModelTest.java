package com.hackspace.andy.readrss.model;

import com.hackspace.andy.readrss.BuildConfig;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity_;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@Config(constants = BuildConfig.class)
public class ModelTest {

    private MessageService mMessageService;
    @Mock PrimaryFeedActivity_ mActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mMessageService = new MessageService(mActivity.getApplicationContext());
    }

    @Test
    public void checkDataNotNull() {
        assertNotNull(mMessageService.query());
    }
}
