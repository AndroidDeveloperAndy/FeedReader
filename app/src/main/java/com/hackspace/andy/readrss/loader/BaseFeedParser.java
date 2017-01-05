package com.hackspace.andy.readrss.loader;

import android.os.AsyncTask;
import android.util.Log;

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

	protected BaseFeedParser(String feedUrl, ILoaderData<T> endDataPoint){
		this.endDataPoint = endDataPoint;
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Error url!", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected T doInBackground(Void... params) {
		T tmp = null;
		try {
			tmp = parse();
		} catch (Exception e) {
			Log.e(TAG, "Error loaded feed!", e);
			throw new RuntimeException(e);
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
			Log.e(TAG, "Error open Stream!", e);
			throw new RuntimeException(e);
		}
	}
}