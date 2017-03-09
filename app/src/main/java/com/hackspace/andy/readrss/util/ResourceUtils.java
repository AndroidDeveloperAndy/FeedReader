package com.hackspace.andy.readrss.util;

import com.hackspace.andy.readrss.loader.implementation.BaseFeedParser;
import com.hackspace.andy.readrss.loader.implementation.SaxFeedParser;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;

public class ResourceUtils {

    public static final String PICTURE_URL = "https://habrahabr.ru/images/logo.png";
    public static final String FEED_URL = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";
    public static final String TAB = "%s\n%s";

    public static final String CHANNEL = "channel";
    public static final String PUB_DATE = "pubDate";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String TITLE = "title";
    public static final String ITEM = "item";

    public static final String TITLE_ARG = "TITLE_ARGUMENT";
    public static final String DATE_ARG = "DATE_ARGUMENT";
    public static final String DESCRIPTION_ARG = "DESCRIPTION_ARGUMENT";
    public static final String LINK_ARG = "LINK_ARGUMENT";

    public static int LENGTH = 1500;

    public static final String TAG_PRESENTER = PrimaryFeedPresenter.class.getName();
    public static final String TAG_BASE_PARSER = BaseFeedParser.class.getName();
    public static final String TAG_SAX_PARSER = SaxFeedParser.class.getName();



}
