package com.hackspace.andy.readrss.loader.Implementation;

import android.os.AsyncTask;

import com.hackspace.andy.readrss.loader.FeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.PrimaryFeedView;

import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@EBean(scope = EBean.Scope.Singleton)
public abstract class BaseFeedParser<T> extends AsyncTask <Void, Void, T> implements FeedParser<T> {

	public static final String CHANNEL = "channel";
	public static final String PUB_DATE = "pubDate";
	public static final String DESCRIPTION = "description";
	public static final String LINK = "link";
	public static final String TITLE = "title";
	public static final String ITEM = "item";

	private static final String FEED_URL = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";

	private final URL mFeedUrl;
	private ILoaderData<T> mEndDataPoint;
	static private BaseFeedParser sParser;
	private PrimaryFeedView mFeedView = new PrimaryFeedActivity();

	protected BaseFeedParser(ILoaderData<T> endDataPoint){
		this.mEndDataPoint = endDataPoint;
		try {
			this.mFeedUrl = new URL(FEED_URL);
		} catch (MalformedURLException e) {
			mFeedView.messageBox("BaseFeedParser",e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static BaseFeedParser getParser(ILoaderData loaderData) {
		sParser = new SaxFeedParser(loaderData);
		return sParser;
	}

	@Override
	protected T doInBackground(Void... params) {
		return parse();
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
			mFeedView.messageBox("getInputStream",e.getMessage());
			throw new RuntimeException(e);
		}
	}
}