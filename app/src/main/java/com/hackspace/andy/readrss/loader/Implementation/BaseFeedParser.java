package com.hackspace.andy.readrss.loader.implementation;

import android.os.AsyncTask;
import android.util.Log;

import com.hackspace.andy.readrss.loader.interfaces.FeedParser;
import com.hackspace.andy.readrss.loader.interfaces.ILoaderData;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.hackspace.andy.readrss.util.ResourceUtils.FEED_URL;
import static com.hackspace.andy.readrss.util.ResourceUtils.TAG_BASE_PARSER;

public abstract class BaseFeedParser<T> extends AsyncTask <Void, Void, T> implements FeedParser<T> {

	private final URL mFeedUrl;
	private ILoaderData<T> mEndDataPoint;
	private PrimaryFeedView mFeedView = new PrimaryFeedActivity();

	BaseFeedParser(ILoaderData<T> endDataPoint){
		this.mEndDataPoint = endDataPoint;
		try {
			this.mFeedUrl = new URL(FEED_URL);
		} catch (MalformedURLException e) {
			mFeedView.showError();
			Log.d(TAG_BASE_PARSER,"BaseFeedParser: ",e);
			throw new RuntimeException(e);
		}
	}

	public static BaseFeedParser getParser(ILoaderData loaderData) {
		return new SaxFeedParser(loaderData);
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

	InputStream getInputStream() {
		try {
			return mFeedUrl.openStream();
		} catch (IOException e) {
			mFeedView.showError();
			Log.d(TAG_BASE_PARSER,"getInputStream: ",e);
			throw new RuntimeException(e);
		}
	}
}