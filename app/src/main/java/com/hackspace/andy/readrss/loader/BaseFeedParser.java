package com.hackspace.andy.readrss.loader;
import android.os.AsyncTask;

import com.hackspace.andy.readrss.FeedParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseFeedParser<T> extends AsyncTask <Void, Void, T> implements FeedParser<T> {

	public static final String CHANNEL = "channel";
	public static final String PUB_DATE = "pubDate";
	public static final String DESCRIPTION = "description";
	public static final String LINK = "link";
	public static final String TITLE = "title";
	public static final String ITEM = "item";
	
	private final URL feedUrl;
	private ILoaderData<T> endDataPoint;

	protected BaseFeedParser(String feedUrl, ILoaderData<T> endDataPoint){
		this.endDataPoint = endDataPoint;
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected T doInBackground(Void... params) {
		return parse();
	}

	@Override
	protected void onPostExecute(T t) {
		if(t != null)
			endDataPoint.endLoad(t);
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}