package com.hackspace.andy.readrss.loader.implementation;

import android.os.AsyncTask;
import android.util.Log;

import com.hackspace.andy.readrss.loader.interfaces.FeedParser;
import com.hackspace.andy.readrss.loader.interfaces.ILoaderData;
import com.hackspace.andy.readrss.util.DialogFactory;
import com.hackspace.andy.readrss.util.NetworkUtil;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

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
	private static final String TAG = BaseFeedParser.class.getName();

	private final URL mFeedUrl;
	private ILoaderData<T> mEndDataPoint;
	static private BaseFeedParser sParser;
	private PrimaryFeedView mFeedView = new PrimaryFeedActivity();

	protected BaseFeedParser(ILoaderData<T> endDataPoint){
		this.mEndDataPoint = endDataPoint;
		try {
			this.mFeedUrl = new URL(FEED_URL);
		} catch (MalformedURLException e) {
			mFeedView.showError();
			Log.d(TAG,"BaseFeedParser: ",e);
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
			mFeedView.showError();
			Log.d(TAG,"getInputStream: ",e);
			throw new RuntimeException(e);
		}
	}
}