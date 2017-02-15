package com.hackspace.andy.readrss.view.Implementation;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.hackspace.andy.readrss.model.MessagesServiceImpl;
import com.hackspace.andy.readrss.presenter.Implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.presenter.PrimaryFeedPresenterImpl;
import com.hackspace.andy.readrss.view.PrimaryFeedView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_primary_feed)
public class PrimaryFeedActivity extends Activity implements PrimaryFeedView ,ILoaderData<List<Message>>{

    private static final String TAG = PrimaryFeedActivity.class.getName();

    PrimaryFeedPresenterImpl mPrimaryFeedPresenter = new PrimaryFeedPresenter(this);

    private List<Message> mMessagesList;
    @Bean protected FeedAdapter mFeedAdapter;

    @ViewById(R.id.swipeRefreshLayout) protected SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(android.R.id.list) protected RecyclerView mRvList;

    private MessagesServiceImpl mRealmService = new MessageService(this);

    private static ConnectivityManager sManagerConnect;
    private static NetworkInfo sNetworkInfo;

    @AfterViews
    @Override
    public void getFeedFromNetwork(){
        try {
            mMessagesList = mPrimaryFeedPresenter.getNews();
            setListFeed();
            mFeedAdapter.setFeedAdapter(mMessagesList);
            mRvList.setAdapter(mFeedAdapter);
            Toast.makeText(this,R.string.load_from_network,Toast.LENGTH_LONG).show();
            mRealmService.config(this);
        }catch (Exception e){
            messageBox("getFeedFromNetwork",e.getMessage());
        }
    }

    @Override
    public List<Message> getFeedFromDatabase(){
        try {
            mMessagesList = mRealmService.query();
            setListFeed();
            mFeedAdapter.setFeedAdapter(mMessagesList);
            mRvList.setAdapter(mFeedAdapter);
            Toast.makeText(this, R.string.load_from_database, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            messageBox("getFeedFromDatabase",e.getMessage());
        }
        return mMessagesList;
    }


    public void setListFeed(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRvList.setLayoutManager(llm);
        mRvList.setHasFixedSize(true);
        //TODO Have bug with two Detail Activity
        mRvList.addOnItemTouchListener(new RecyclerClickListener((view, position) ->
                startActivity(DetailFeedActivity_.newInstance(this,
                        mMessagesList.get(position).getTitle(),
                        mMessagesList.get(position).getDate().toString(),
                        mMessagesList.get(position).getLink(),
                        mMessagesList.get(position).getDescription())))
        );
    }

    @AfterViews
    @Override //TODO why this method is a part of View interface ? What if some one call this before *.xml inflation ?
    public void setSwipeRefreshLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if(isOnline()) {
                PrimaryFeedActivity.this.runOnUiThread(() -> getFeedFromNetwork());
                Toast.makeText(getApplicationContext(),R.string.update_data,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),String.format("%s\n%s",getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });
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
            mRealmService.insert(mMessagesList);

        }catch (Throwable t){
            Log.e(TAG,"Error load list feed!",t);
        }
    }

    @Override
    public void getAlertDialogForConnectionError() {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(this);
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
    public void messageBox(String method, String message)
    {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setMessage(String.format("%s\n%s",getString(R.string.error_method)+method,getString(R.string.error)+message))
                .setNeutralButton("OK", null)
                .show();
    }
}

