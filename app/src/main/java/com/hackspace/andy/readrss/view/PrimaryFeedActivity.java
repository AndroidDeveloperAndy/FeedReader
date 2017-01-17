package com.hackspace.andy.readrss.view;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.hackspace.andy.readrss.adapter.FeedAdapter;
import com.hackspace.andy.readrss.loader.FeedParserFactory;
import com.hackspace.andy.readrss.model.Message;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.BaseFeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;

import java.util.ArrayList;
import java.util.List;

public class PrimaryFeedActivity extends ListActivity implements ILoaderData<List<Message>>{

    private static final String TAG = PrimaryFeedActivity.class.getName();
    private List<Message> messagesList;
    private BaseFeedParser<List<Message>> loader;
    private List<String> news;
    private FeedAdapter adapter;
    private ListView listFeed;

    protected static String FEED_URL = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_feed);

        listFeed = (ListView) findViewById(android.R.id.list);

        loadFeed();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        startActivity(DetailFeedActivity.newInstance(this,
                        messagesList.get(position).getTitle(),
                        messagesList.get(position).getDate().toString(),
                        messagesList.get(position).getLink(),
                        messagesList.get(position).getDescription()));
    }

    private void loadFeed(){
        try {
            if ((loader != null) && loader.getStatus() != AsyncTask.Status.RUNNING) {
                if (loader.isCancelled()) {
                    loader = FeedParserFactory.getParser(this, FEED_URL);

                    loader.execute((Void[]) null);
                } else {
                    loader.cancel(true);
                    loader = FeedParserFactory.getParser(this, FEED_URL);

                    loader.execute((Void[]) null);
                }

            } else if (loader != null && loader.getStatus() == AsyncTask.Status.PENDING) {

                loader = FeedParserFactory.getParser(this, FEED_URL);

                loader.execute((Void[]) null);
            } else if ((loader != null) && loader.getStatus() == AsyncTask.Status.FINISHED) {

                loader = FeedParserFactory.getParser(this, FEED_URL);

                loader.execute((Void[]) null);
            } else if (loader == null) {

                loader = FeedParserFactory.getParser(this, FEED_URL);

                loader.execute((Void[]) null);
            }

        }catch (Exception e){
            Log.e(TAG, "Error load feed in the home page!", e);
        }
    }

    @Override
    public void endLoad(@NonNull List<Message> data) {
        try{
            messagesList = data;
            news = new ArrayList<>(messagesList.size());
            for (Message msg : messagesList){
                news.add(msg.getTitle());
                news.add(msg.getDate());
            }
            adapter = new FeedAdapter(this, messagesList);

            listFeed.setAdapter(adapter);
        } catch (Throwable t){
            Log.e(TAG,"Error load list feed!",t);
        }
    }
}

