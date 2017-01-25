package com.hackspace.andy.readrss.loader.Implementation;

import android.os.AsyncTask;
import android.util.Log;

import com.hackspace.andy.readrss.loader.FeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;

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

	private static final String TAG = BaseFeedParser.class.getName();

	private final URL feedUrl;
	private ILoaderData<T> endDataPoint;
	static private BaseFeedParser parser;

	protected BaseFeedParser(String feedUrl, ILoaderData<T> endDataPoint){
		this.endDataPoint = endDataPoint;
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static BaseFeedParser getParser(ILoaderData loaderData, String feedUrl) {
		parser = new SaxFeedParser(feedUrl, loaderData);
		return parser;
	}

	@Override
	protected T doInBackground(Void... params) {
		T tmp = null;
		try {
			tmp = parse();
		} catch (Exception e) {
			//TODO do not ignore exceptions, think about business logic to notify user by TOAST ore some dialogue interaction (cancel, retry)
			Log.e(TAG, "Error loaded feed!", e);
		}
		return tmp;
	}

	@Override
	protected void onPostExecute(T t) {
		if(t != null)
			endDataPoint.endLoad(t);
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
}