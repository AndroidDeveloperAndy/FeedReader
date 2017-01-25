package com.hackspace.andy.readrss.view.Implementation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hackspace.andy.readrss.adapter.RVAdapter;
import com.hackspace.andy.readrss.adapter.RecyclerClickListener;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.view.PrimaryFeedView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PrimaryFeedActivity extends Activity implements ILoaderData<List<Message>>,PrimaryFeedView {

    private static final String TAG = PrimaryFeedActivity.class.getName();

    private List<Message> messagesList;
    private BaseFeedParser<List<Message>> loader;
    private RVAdapter adapter;

    private RecyclerView rvList;
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
        getData();
    }

    @Override
    public void getData(){
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
        adapter = new RVAdapter(messagesList);
        rvList.setAdapter(adapter);
    }

    @Override
    public void createViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if(isOnline(getApplicationContext())) {
                PrimaryFeedActivity.this.runOnUiThread(() -> getFeedFromNetwork());
                Toast.makeText(getApplicationContext(),R.string.update_data,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),R.string.dont_update+"\n"+R.string.check_network,Toast.LENGTH_LONG).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });


        rvList=(RecyclerView)findViewById(android.R.id.list);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvList.setLayoutManager(llm);
        rvList.setHasFixedSize(true);

        rvList.addOnItemTouchListener(new RecyclerClickListener(this) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

                startActivity(DetailFeedActivity.newInstance(PrimaryFeedActivity.this,
                        messagesList.get(position).getTitle(),
                        messagesList.get(position).getDate().toString(),
                        messagesList.get(position).getLink(),
                        messagesList.get(position).getDescription()));
            }
        });

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

            realm.insert(messagesList);
            RVAdapter adapter = new RVAdapter(messagesList);
            rvList.setAdapter(adapter);

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

