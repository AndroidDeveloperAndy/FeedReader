package com.hackspace.andy.readrss.util;

import org.androidannotations.annotations.Extra;

public class StringsUtils {

    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
    public static final String PICTURE_URL = "https://habrahabr.ru/images/logo.png";
    public static final String FEED_URL = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";
    public static final String TAB = "%s\n%s";

    public static final String CHANNEL = "channel";
    public static final String PUB_DATE = "pubDate";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String TITLE = "title";
    public static final String ITEM = "item";

    @Extra("TITLE_ARGUMENT")       public static String ARG_TITLE;
    @Extra("DATE_ARGUMENT")        public static String ARG_DATE;
    @Extra("DESCRIPTION_ARGUMENT") public static String ARG_DESCRIPTION;
    @Extra("LINK_ARGUMENT")        public static String ARG_LINK;

}
