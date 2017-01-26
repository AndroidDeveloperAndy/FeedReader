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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hackspace.andy.readrss.adapter.FeedAdapter;
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

    private List<Message> mMessagesList;
    private BaseFeedParser<List<Message>> mListLoader;
    private FeedAdapter mFeedAdapter;

    private RecyclerView mRvList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MessageService mRealmService;
    private RealmConfiguration mConfigRealm;

    private static ConnectivityManager mManagerConnect;
    private static NetworkInfo mNetworkInfo;

    private AlertDialog.Builder mAlertDialog;

    //TODO this constant shouldn't be in View, more looks like Model layer.
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
            if ((mListLoader != null) && mListLoader.getStatus() != AsyncTask.Status.RUNNING) {
                if (mListLoader.isCancelled()) {
                    mListLoader = BaseFeedParser.getParser(this, FEED_URL);

                    mListLoader.execute((Void[]) null);
                } else {
                    mListLoader.cancel(true);
                    mListLoader = BaseFeedParser.getParser(this, FEED_URL);

                    mListLoader.execute((Void[]) null);
                }

            } else if (mListLoader != null && mListLoader.getStatus() == AsyncTask.Status.PENDING) {

                mListLoader = BaseFeedParser.getParser(this, FEED_URL);

                mListLoader.execute((Void[]) null);
            } else if ((mListLoader != null) && mListLoader.getStatus() == AsyncTask.Status.FINISHED) {

                mListLoader = BaseFeedParser.getParser(this, FEED_URL);

                mListLoader.execute((Void[]) null);
            } else if (mListLoader == null) {

                mListLoader = BaseFeedParser.getParser(this, FEED_URL);

                mListLoader.execute((Void[]) null);
            }
        }catch (Exception e){
            Log.e(TAG, "Error load feed in the home page!", e);
        }
    }

    @Override
    public void getFeedFromDatabase(){
        mMessagesList = mRealmService.query();
        mFeedAdapter = new FeedAdapter(mMessagesList);
        mRvList.setAdapter(mFeedAdapter);
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
                Toast.makeText(getApplicationContext(),String.format("%s\n%s",getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });


        mRvList =(RecyclerView)findViewById(android.R.id.list);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRvList.setLayoutManager(llm);
        mRvList.setHasFixedSize(true);

        mRvList.addOnItemTouchListener(new RecyclerClickListener(this) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

                startActivity(DetailFeedActivity.newInstance(PrimaryFeedActivity.this,
                        mMessagesList.get(position).getTitle(),
                        mMessagesList.get(position).getDate().toString(),
                        mMessagesList.get(position).getLink(),
                        mMessagesList.get(position).getDescription()));
            }
        });

        mConfigRealm = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(mConfigRealm);

        mRealmService = new MessageService(this);
    }

    @Override
    public void endLoad(@NonNull List<Message> data) {
        try{
            mMessagesList = data;
            for (Message msg : mMessagesList){
                msg.getTitle();
                msg.getDate();
                msg.getDescription();
                msg.getLink();
            }

            mRealmService.insert(mMessagesList);
            FeedAdapter adapter = new FeedAdapter(mMessagesList);
            mRvList.setAdapter(adapter);

        } catch (Throwable t){
            mAlertDialog = new AlertDialog.Builder(getApplicationContext());
            mAlertDialog.setTitle("Dialog");
            mAlertDialog.setMessage("Error load feed.");
            mAlertDialog.setPositiveButton("Retry", (dialog, which) -> {
                getFeedFromNetwork();
            });
            mAlertDialog.setNegativeButton("Cancel", (dialog, which) -> {
                t.getMessage();
                Log.e(TAG,"Error load list feed!",t);
            });
            mAlertDialog.setCancelable(true);
            mAlertDialog.setOnCancelListener(dialog -> {
                t.getMessage();
                Log.e(TAG,"Error load list feed!",t);
            });
        }
    }

    private static boolean isOnline(Context context)
    {
        mManagerConnect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mManagerConnect.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting();
    }
}

