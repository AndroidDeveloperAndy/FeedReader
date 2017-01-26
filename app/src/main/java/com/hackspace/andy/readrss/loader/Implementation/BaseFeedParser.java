package com.hackspace.andy.readrss.loader.Implementation;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

	private final URL mFeedUrl;
	private ILoaderData<T> mEndDataPoint;
	static private BaseFeedParser sParser;

	protected BaseFeedParser(String feedUrl, ILoaderData<T> endDataPoint){
		this.mEndDataPoint = endDataPoint;
		try {
			this.mFeedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static BaseFeedParser getParser(ILoaderData loaderData, String feedUrl) {
		sParser = new SaxFeedParser(feedUrl, loaderData);
		return sParser;
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
			mEndDataPoint.endLoad(t);
	}

	protected InputStream getInputStream() {
		try {
			return mFeedUrl.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
}