package com.hackspace.andy.readrss.view.Implementation;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.hackspace.andy.readrss.adapter.FeedAdapter;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.view.PrimaryFeedView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PrimaryFeedActivity extends ListActivity implements ILoaderData<List<Message>>,PrimaryFeedView {

    private static final String TAG = PrimaryFeedActivity.class.getName();

    private List<Message> messagesList;
    private BaseFeedParser<List<Message>> loader;
    private FeedAdapter adapter;

    private ListView listFeed;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MessageService realm;
    private RealmConfiguration configRealm;

    private static ConnectivityManager managerConnect;
    private static NetworkInfo netInfo;

    protected static final String FEED_URL = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_feed);

        createViews();

        if(isOnline(this)){
            getFeedFromNetwork();
            Toast.makeText(this,R.string.load_from_network,Toast.LENGTH_LONG).show();
        }
        else {
            getFeedFromDatabase();
            Toast.makeText(this,R.string.load_from_database,Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void getFeedFromNetwork(){
        try {
            if ((loader != null) && loader.getStatus() != AsyncTask.Status.RUNNING) {
                if (loader.isCancelled()) {
                    loader = BaseFeedParser.getParser(this, FEED_URL);

                    loader.execute((Void[]) null);
                } else {
                    loader.cancel(true);
                    loader = BaseFeedParser.getParser(this, FEED_URL);

                    loader.execute((Void[]) null);
                }

            } else if (loader != null && loader.getStatus() == AsyncTask.Status.PENDING) {

                loader = BaseFeedParser.getParser(this, FEED_URL);

                loader.execute((Void[]) null);
            } else if ((loader != null) && loader.getStatus() == AsyncTask.Status.FINISHED) {

                loader = BaseFeedParser.getParser(this, FEED_URL);

                loader.execute((Void[]) null);
            } else if (loader == null) {

                loader = BaseFeedParser.getParser(this, FEED_URL);

                loader.execute((Void[]) null);
            }
        }catch (Exception e){
            Log.e(TAG, "Error load feed in the home page!", e);
        }
    }

    @Override
    public void getFeedFromDatabase(){
        messagesList = realm.query();
        adapter = new FeedAdapter(this, messagesList);
        listFeed.setAdapter(adapter);
    }

    @Override
    public void createViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if(isOnline(getApplicationContext())) {
                PrimaryFeedActivity.this.runOnUiThread(() -> getFeedFromNetwork());
                Toast.makeText(getApplicationContext(),R.string.update_data,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),R.string.dont_update+"\n"+R.string.check_network,Toast.LENGTH_LONG).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });

        listFeed = (ListView) findViewById(android.R.id.list);

        this.adapter = new FeedAdapter(this, messagesList);
        this.getListView().setAdapter(this.adapter);

        configRealm = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(configRealm);

        realm = new MessageService(this);
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

    private static boolean isOnline(Context context)
    {
        managerConnect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = managerConnect.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}

