package com.hackspace.andy.readrss.presenter.Implementation;

import android.os.AsyncTask;
import android.util.Log;

import com.hackspace.andy.readrss.loader.implementation.BaseFeedParser;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import java.util.List;

import javax.inject.Inject;

public class PrimaryFeedPresenter implements PrimaryFeedPresenterImpl {

    private static final String TAG = PrimaryFeedPresenter.class.getName();
    private BaseFeedParser<List<Message>> mListLoader;
    private List<Message> mMessagesList;
    private PrimaryFeedActivity mPrimaryFeedView;

    public PrimaryFeedPresenter(PrimaryFeedActivity view){
       this.mPrimaryFeedView = view;
    }

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
                mPrimaryFeedView.showError();
                e.getMessage();
                Log.e(TAG, "Error load feed in the home page!", e);
            }
        return mMessagesList;
    }
}
