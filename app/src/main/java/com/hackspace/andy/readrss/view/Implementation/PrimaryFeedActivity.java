package com.hackspace.andy.readrss.view.Implementation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.hackspace.andy.readrss.adapter.FeedAdapter;
import com.hackspace.andy.readrss.adapter.RecyclerClickListener;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.presenter.Implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.presenter.PrimaryFeedPresenterImpl;
import com.hackspace.andy.readrss.view.PrimaryFeedView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PrimaryFeedActivity extends Activity implements PrimaryFeedView ,ILoaderData<List<Message>>{

    private static final String TAG = PrimaryFeedActivity.class.getName();
    private PrimaryFeedPresenterImpl mPrimaryFeedPresenter = new PrimaryFeedPresenter(this);

    private List<Message> mMessagesList;
    private FeedAdapter mFeedAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRvList;

    private MessageService mRealmService;
    private RealmConfiguration mConfigRealm;

    private static ConnectivityManager sManagerConnect;
    private static NetworkInfo sNetworkInfo;
    private AlertDialog.Builder mAlertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_feed);

        createViews();
        getFeedFromNetwork();
    }

    @Override
    public void getAlertDialogForConnectionError() {
        mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setTitle("Dialog");
        mAlertDialog.setMessage("Error load feed. Reconnect for internet.");
        mAlertDialog.setPositiveButton("Retry", (dialog, which) -> {
            getFeedFromNetwork();
        });
        mAlertDialog.setNegativeButton("Cancel", (dialog, which) -> {
        });
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    @Override
    public void getFeedFromNetwork(){
        try {
        mMessagesList = mPrimaryFeedPresenter.getNews();
        FeedAdapter adapter = new FeedAdapter(mMessagesList);
        mRvList.setAdapter(adapter);
        Toast.makeText(this,R.string.load_from_network,Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            messageBox("getFeedFromNetwork",e.getMessage());
            getAlertDialogForConnectionError();
        }
    }

    @Override
    public List<Message> getFeedFromDatabase(){
        try {
            mRealmService = new MessageService(this);
            mMessagesList = mRealmService.query();
            mFeedAdapter = new FeedAdapter(mMessagesList);
            mRvList.setAdapter(mFeedAdapter);
            Toast.makeText(this, R.string.load_from_database, Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            messageBox("getFeedFromDatabase",e.getMessage());
        }
        return mMessagesList;
    }

    @Override
    public void createViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if(isOnline()) {
                PrimaryFeedActivity.this.runOnUiThread(() -> getFeedFromNetwork());
                Toast.makeText(getApplicationContext(),R.string.update_data,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),String.format("%s\n%s",getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });

        mRvList = (RecyclerView) findViewById(android.R.id.list);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRvList.setLayoutManager(llm);
        mRvList.setHasFixedSize(true);

        mRvList.addOnItemTouchListener(new RecyclerClickListener(this, (view, position) -> startActivity(DetailFeedActivity.newInstance(PrimaryFeedActivity.this,
                mMessagesList.get(position).getTitle(),
                mMessagesList.get(position).getDate().toString(),
                mMessagesList.get(position).getLink(),
                mMessagesList.get(position).getDescription())))
        );
        mConfigRealm = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(mConfigRealm);
    }

    @Override
    public boolean isOnline()
    {
        if(getApplicationContext() == null) {
            return false;
        }
        sManagerConnect = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        sNetworkInfo = sManagerConnect.getActiveNetworkInfo();
        return sNetworkInfo != null && sNetworkInfo.isConnectedOrConnecting();
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
            mRealmService = new MessageService(this);
            mRealmService.insert(mMessagesList);

        } catch (Throwable t){
            messageBox("endLoad",t.getMessage());
            Log.e(TAG,"Error load list feed!",t);
        }
    }

    @Override
    public void messageBox(String method, String message)
    {
        Log.d("EXCEPTION: " + method,  message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}

