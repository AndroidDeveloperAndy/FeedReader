package com.hackspace.andy.readrss.loader;

public abstract class FeedParserFactory {

	static private BaseFeedParser parser = null;

	public static BaseFeedParser getParser( ILoaderData loaderData, String feedUrl) {
		parser = new SaxFeedParser(feedUrl, loaderData);
		return parser;
	}
}

