package com.hackspace.andy.readrss.view;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hackspace.andy.readrss.adapter.FeedAdapter;
import com.hackspace.andy.readrss.loader.FeedParserFactory;
import com.hackspace.andy.readrss.model.Message;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.BaseFeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.MessageService;
import com.hackspace.andy.readrss.model.MessagesServiceImpl;
import com.hackspace.andy.readrss.refresh.PullToRefreshComponent;
import com.hackspace.andy.readrss.refresh.RefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PrimaryFeedActivity extends ListActivity implements ILoaderData<List<Message>>{

    private static final String TAG = PrimaryFeedActivity.class.getName();
    private List<Message> messagesList;
    private BaseFeedParser<List<Message>> loader;
    private FeedAdapter adapter;
    private PullToRefreshComponent pullToRefresh;

    private ListView listFeed;
    private ViewGroup upperButton,lowerButton;

    private MessageService realm;

    protected static String FEED_URL = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_feed);

        upperButton = (ViewGroup) this
                .findViewById(R.id.refresh_upper_button);
        lowerButton = (ViewGroup) this
                .findViewById(R.id.refresh_lower_button);

        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                .build();
        Realm.setDefaultConfiguration(config);
        realm = new MessageService(this);

        this.adapter = new FeedAdapter(this, messagesList);
        this.getListView().setAdapter(this.adapter);
        listFeed = (ListView) findViewById(android.R.id.list);

        if(realm.hasMessages()){
            messagesList = realm.query();
            adapter = new FeedAdapter(this, messagesList);
            listFeed.setAdapter(adapter);
        }
        else {
            loadFeed();
        }

        this.pullToRefresh = new PullToRefreshComponent(upperButton,
                lowerButton, this.getListView(), new Handler());
        this.pullToRefresh.setOnPullDownRefreshAction(new RefreshListener() {

            @Override
            public void refreshFinished() {
                PrimaryFeedActivity.this.runOnUiThread(() ->loadFeed());
                Log.i(TAG,"pull down");
            }

            @Override
            public void doRefresh() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        this.pullToRefresh.setOnPullUpRefreshAction(new RefreshListener() {

            @Override
            public void refreshFinished() {
                PrimaryFeedActivity.this.runOnUiThread(() -> loadFeed());
                Log.i(TAG,"pull up");
            }

            @Override
            public void doRefresh() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
            for (Message msg : messagesList){
                msg.getTitle();
                msg.getDate();
                msg.getDescription();
                msg.getLink();
            }

            //realm.insert(messagesList);

            adapter = new FeedAdapter(this, messagesList);

            listFeed.setAdapter(adapter);
        } catch (Throwable t){
            Log.e(TAG,"Error load list feed!",t);
        }
    }
}

