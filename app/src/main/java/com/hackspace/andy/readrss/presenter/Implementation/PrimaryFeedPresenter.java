package com.hackspace.andy.readrss.presenter.implementation;

import android.os.AsyncTask;
import android.util.Log;

import com.hackspace.andy.readrss.loader.implementation.BaseFeedParser;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity;

import java.util.List;

public class PrimaryFeedPresenter implements PrimaryFeedPresenterImpl {

    private static final String TAG = PrimaryFeedPresenter.class.getName();

    private BaseFeedParser<List<Message>> mListLoader;
    private List<Message> mMessagesList;

    @Override
    public List<Message> getNews() {
            try {
                if ((mListLoader != null) && mListLoader.getStatus() != AsyncTask.Status.RUNNING) {
                    if (mListLoader.isCancelled()) {
                        mListLoader = BaseFeedParser.getParser(PrimaryFeedActivity.class.newInstance());

                        mListLoader.execute((Void[]) null);
                    } else {
                        mListLoader.cancel(true);
                        mListLoader = BaseFeedParser.getParser(PrimaryFeedActivity.class.newInstance());

                        mListLoader.execute((Void[]) null);
                    }

                } else if (mListLoader != null && mListLoader.getStatus() == AsyncTask.Status.PENDING) {

                    mListLoader = BaseFeedParser.getParser(PrimaryFeedActivity.class.newInstance());

                    mListLoader.execute((Void[]) null);
                } else if ((mListLoader != null) && mListLoader.getStatus() == AsyncTask.Status.FINISHED) {

                    mListLoader = BaseFeedParser.getParser(PrimaryFeedActivity.class.newInstance());

                    mListLoader.execute((Void[]) null);
                } else if (mListLoader == null) {

                    mListLoader = BaseFeedParser.getParser(PrimaryFeedActivity.class.newInstance());

                    mListLoader.execute((Void[]) null);
                    mMessagesList = mListLoader.get();
                }
            } catch (Exception e) {
                e.getMessage();
                Log.e(TAG, "Error load feed in the home page!", e);
            }
        return mMessagesList;
    }
}
