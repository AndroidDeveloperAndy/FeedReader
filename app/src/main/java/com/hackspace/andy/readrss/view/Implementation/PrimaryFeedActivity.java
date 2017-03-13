package com.hackspace.andy.readrss.view.implementation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.hackspace.andy.readrss.FeedReaderApp;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.adapter.FeedAdapter;
import com.hackspace.andy.readrss.adapter.RecyclerClickListener;
import com.hackspace.andy.readrss.loader.interfaces.ILoaderData;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.model.interfaces.MessagesServiceImpl;
import com.hackspace.andy.readrss.presenter.implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.presenter.interfaces.PrimaryFeedPresenterImpl;
import com.hackspace.andy.readrss.util.DialogFactory;
import com.hackspace.andy.readrss.util.NetworkUtil;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import static com.hackspace.andy.readrss.util.ResourceUtils.TAB;

@EActivity(R.layout.activity_primary_feed)
public class PrimaryFeedActivity extends Activity implements PrimaryFeedView,
                                                                ILoaderData<List<Message>>,
                                                                SwipeRefreshLayout.OnRefreshListener{
    @Inject PrimaryFeedPresenterImpl mPrimaryFeedPresenter;
    @Bean   FeedAdapter mFeedAdapter;

    @ViewById(R.id.swipeRefreshLayout)  SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(android.R.id.list)        RecyclerView mRvList;

    private MessagesServiceImpl mRealmService;
    private List<Message> mMessagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeedReaderApp.getComponent().inject(this);
        mPrimaryFeedPresenter.setView(this);
    }

    @Override
    public void getFeedFromNetwork(){
        try {
            mPrimaryFeedPresenter = new PrimaryFeedPresenter();
            mMessagesList = mPrimaryFeedPresenter.getNews();
            showFeed(mMessagesList);
        }catch (Exception e){
            showError();
            DialogFactory.messageBox("getFeedFromNetwork",e.getMessage(),this);
        }
    }

    @Override
    public void getFeedFromDatabase(){
        try {
            mRealmService = new MessageService(this);
            mMessagesList = mRealmService.query();
            showFeed(mMessagesList);
        }catch (Exception e){
            showError();
            DialogFactory.messageBox("getFeedFromDatabase",e.getMessage(),this);
        }
    }

    @Override
    public void showFeed(List<Message> listFeed){
        mRvList.setAdapter(mFeedAdapter);
        mFeedAdapter.setFeed(listFeed);
        mFeedAdapter.notifyDataSetChanged();
    }

    @Override
    public void getData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            getFeedFromNetwork();
            DialogFactory.createProgressDialog(this,R.string.load_from_network);
        } else {
            getFeedFromDatabase();
            DialogFactory.createProgressDialog(this,R.string.load_from_database);
        }
    }

    @AfterViews
    public void setListFeed(){
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRvList.addOnItemTouchListener(new RecyclerClickListener((view, position) ->
                startActivity(DetailFeedActivity_.newInstance(this,
                        mMessagesList.get(position).getTitle(),
                        mMessagesList.get(position).getDate(),
                        mMessagesList.get(position).getLink(),
                        mMessagesList.get(position).getDescription())))
        );
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setHasFixedSize(true);
        getData();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if(NetworkUtil.isNetworkAvailable(this)) {
            PrimaryFeedActivity.this.runOnUiThread(this::getFeedFromNetwork);
            DialogFactory.createProgressDialog(this,R.string.update_data);
        }else {
            Toast.makeText(getApplicationContext(),String.format(TAB,getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void endLoad(@NonNull List<Message> data) {
        try{
            mMessagesList = data;
            for (Message msg : mMessagesList){
                msg.getTitle();
                msg.getDate();
            }
            mRealmService = new MessageService(this);
            mRealmService.insert(mMessagesList);
        }catch (Throwable t){
            showError();
            DialogFactory.messageBox("endLoad",t.getMessage(),this);
        }
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_feed)).show();
    }
}

