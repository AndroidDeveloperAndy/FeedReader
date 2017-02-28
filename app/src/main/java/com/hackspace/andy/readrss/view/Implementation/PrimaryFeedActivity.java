package com.hackspace.andy.readrss.view.Implementation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.hackspace.andy.readrss.adapter.FeedAdapter;
import com.hackspace.andy.readrss.adapter.RecyclerClickListener;
import com.hackspace.andy.readrss.injection.component.ActivityComponent;
import com.hackspace.andy.readrss.injection.component.ConfigPersistentComponent;
import com.hackspace.andy.readrss.injection.module.ActivityModule;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.model.MessagesServiceImpl;
import com.hackspace.andy.readrss.presenter.Implementation.PrimaryFeedPresenter;
import com.hackspace.andy.readrss.util.DialogFactory;
import com.hackspace.andy.readrss.util.NetworkUtil;
import com.hackspace.andy.readrss.view.PrimaryFeedView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@EActivity(R.layout.activity_primary_feed)
public class PrimaryFeedActivity extends Activity implements PrimaryFeedView,
                                                                ILoaderData<List<Message>>,
                                                                SwipeRefreshLayout.OnRefreshListener{

    @Inject PrimaryFeedPresenter mPrimaryFeedPresenter;
    @Inject FeedAdapter mFeedAdapter;

    @ViewById(R.id.swipeRefreshLayout) protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(android.R.id.list) protected RecyclerView mRvList;

    private List<Message> mMessagesList;
    private MessagesServiceImpl mRealmService;
    private ActivityComponent mActivityComponent;

    @Override
    public void getFeedFromNetwork(){
        try {
            mPrimaryFeedPresenter = new PrimaryFeedPresenter(this);
            mMessagesList = mPrimaryFeedPresenter.getNews();
            showFeed(mMessagesList);
        }catch (Exception e){
            showError();
            NetworkUtil.messageBox("getFeedFromNetwork",e.getMessage(),this);
        }
    }

    @Override
    public List<Message> getFeedFromDatabase(){
        try {
            mRealmService = new MessageService(this);
            mMessagesList = mRealmService.query();
            showFeed(mMessagesList);
        }catch (Exception e){
            showError();
            NetworkUtil.messageBox("getFeedFromDatabase",e.getMessage(),this);
        }
        return mMessagesList;
    }

    @Override
    public void showFeed(List<Message> listFeed){
        mRvList.setAdapter(mFeedAdapter);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setHasFixedSize(true);
        mFeedAdapter.setFeed(listFeed);
        mFeedAdapter.notifyDataSetChanged();
    }

    @Override
    public void getData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            getFeedFromNetwork();
            Toast.makeText(this, R.string.load_from_network, Toast.LENGTH_LONG).show();
        } else {
            getFeedFromDatabase();
            Toast.makeText(this, R.string.load_from_database, Toast.LENGTH_LONG).show();
        }
    }

    @AfterViews
    public void setListFeed(){

        mActivityComponent.inject(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRvList.addOnItemTouchListener(new RecyclerClickListener((view, position) ->
                startActivity(DetailFeedActivity_.newInstance(this,
                        mMessagesList.get(position).getTitle(),
                        mMessagesList.get(position).getDate().toString(),
                        mMessagesList.get(position).getLink(),
                        mMessagesList.get(position).getDescription())))
        );
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        getData();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if(NetworkUtil.isNetworkAvailable(this)) {
            PrimaryFeedActivity.this.runOnUiThread(() -> getFeedFromNetwork());
            Toast.makeText(getApplicationContext(),R.string.update_data,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),String.format("%s\n%s",getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
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
            NetworkUtil.messageBox("getFeedFromDatabase",t.getMessage(),this);
        }
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_feed))
                .show();
    }
}

