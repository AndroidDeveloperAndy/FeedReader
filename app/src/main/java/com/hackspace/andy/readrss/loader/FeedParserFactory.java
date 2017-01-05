package com.hackspace.andy.readrss.loader;

import com.hackspace.andy.readrss.enums.ParserType;
import com.hackspace.andy.readrss.loader.AndroidSaxFeedParser;
import com.hackspace.andy.readrss.loader.BaseFeedParser;
import com.hackspace.andy.readrss.loader.DomFeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.loader.SaxFeedParser;
import com.hackspace.andy.readrss.loader.XmlPullFeedParser;

import java.net.URL;

public abstract class FeedParserFactory {

	static BaseFeedParser parser = null;

	public static BaseFeedParser getParser(ParserType type, ILoaderData loaderData, String feedUrl){

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
