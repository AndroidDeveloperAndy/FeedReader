package com.hackspace.andy.readrss;

import com.hackspace.andy.readrss.loader.AndroidSaxFeedParser;
import com.hackspace.andy.readrss.loader.BaseFeedParser;
import com.hackspace.andy.readrss.loader.DomFeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.loader.SaxFeedParser;
import com.hackspace.andy.readrss.loader.XmlPullFeedParser;

public abstract class FeedParserFactory {
	static String feedUrl = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";
	
	public static BaseFeedParser getParser(ParserType type, ILoaderData loaderData){
		BaseFeedParser parser = null;

		switch (type){
			case SAX:
				parser = new SaxFeedParser(feedUrl, loaderData);
				break;
			case DOM:
				parser = new DomFeedParser(feedUrl, loaderData);
				break;
			case ANDROID_SAX:
				parser = new AndroidSaxFeedParser(feedUrl, loaderData);
				break;
			case XML_PULL:
				parser = new XmlPullFeedParser(feedUrl, loaderData);
				break;
		}

		return parser;
	}
}
