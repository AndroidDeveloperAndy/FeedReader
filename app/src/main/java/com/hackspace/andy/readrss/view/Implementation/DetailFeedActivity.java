package com.hackspace.andy.readrss.view.Implementation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.view.DetailFeedView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

@EActivity(R.layout.activity_detail_feed)
public class DetailFeedActivity extends Activity implements ILoaderData<List<Message>>, SwipeRefreshLayout.OnRefreshListener, DetailFeedView {

    private static final String TAG = DetailFeedActivity.class.getName();

    private static final String ARG_TITLE = "TITLE_ARGUMENT";
    private static final String ARG_DATE = "DATE_ARGUMENT";
    private static final String ARG_DESCRIPTION = "DESCRIPTION_ARGUMENT";
    private static final String ARG_LINK = "LINK_ARGUMENT";

    private static final String P = "p";

    private static String sTitle;
    private static String sDate;
    private static String sDescription;
    private static String sUrl;
    private Document mStructureDetailFeed;

    private List<Message> mList;
    private Intent mIntent;
    private String mDetailFeed;

    @ViewById(R.id.head)
    TextView mTxHead;
    @ViewById(R.id.textFeed)
    TextView mTxFeed;
    @ViewById(R.id.link)
    TextView mTxLink;
    @ViewById(R.id.detailFeedDate)
    TextView mTxDate;
    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.cv)
    CardView mCardViewDetailFeed;

    private MessageService mRealm;
    private RealmConfiguration mConfigRealmWithDetailFeed;

    private static ConnectivityManager sConnectManager;
    private static NetworkInfo sNetworkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);
    }

    @Override
    public void getData() {
        if (isOnline()) {
            loadDetailFeed();
            Toast.makeText(this, R.string.load_from_network, Toast.LENGTH_LONG).show();
        } else {
            getDetailFeedFromDatabase();
            Toast.makeText(this, R.string.load_from_database, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void loadDetailFeed() {
        try {
            mTxHead.setText(sTitle);
            mTxDate.setText(sDate);
            mTxLink.setText(sUrl);
            new ThreadDetailFeed(mTxFeed).execute(sUrl);
        } catch (Exception e) {
            messageBox("loadDetailFeed",e.getMessage());
            Log.e(TAG, "Error load detail page!", e);
        }
    }

    @AfterViews
    @Override
    public void loadViews() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);

        mConfigRealmWithDetailFeed = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(mConfigRealmWithDetailFeed);

        mRealm = new MessageService(this);
        getInfoFromActivity();
        getData();
    }

    @Override
    public void getInfoFromActivity() {
        mIntent = getIntent();

        sTitle = mIntent.getStringExtra(ARG_TITLE);
        sDate = mIntent.getStringExtra(ARG_DATE);
        sUrl = mIntent.getStringExtra(ARG_LINK);
        sDescription = mIntent.getStringExtra(ARG_DESCRIPTION);
    }

    @Override
    public void endLoad(@NonNull List<Message> data) {
        if (data.size() > 0) {
            mTxHead.setText(getIntent().getStringExtra(ARG_TITLE));
            mTxDate.setText(getIntent().getStringExtra(ARG_DATE));
            mTxLink.setText(getIntent().getStringExtra(ARG_LINK));
            mTxFeed.setText(getIntent().getStringExtra(ARG_DESCRIPTION));
        }
    }

    public static Intent newInstance(Context context, String title, String date, String link, String description) {
        Intent startIntent = new Intent(context, DetailFeedActivity.class);

        startIntent.putExtra(ARG_TITLE, title);
        startIntent.putExtra(ARG_DATE, date);
        startIntent.putExtra(ARG_LINK, link);
        startIntent.putExtra(ARG_DESCRIPTION, description);

        return startIntent;
    }

    @Override
    public void getDetailFeedFromDatabase() {
        try {
            mList = mRealm.query();
            for (Message msg : mList) {
                mTxHead.setText(msg.getTitle());
                mTxDate.setText(msg.getDate());
                new ThreadDetailFeed(mTxFeed).execute(sUrl);
                mTxLink.setText(sUrl);
            }
        }catch (Exception e){
            messageBox("getDetailFeedFromDatabase",e.getMessage());
        }
    }

    @Override
    public boolean isOnline() {
        if(getApplicationContext() == null) {
            return false;
        }
        sConnectManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        sNetworkInfo = sConnectManager.getActiveNetworkInfo();
        return sNetworkInfo != null && sNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (isOnline()) {
            DetailFeedActivity.this.runOnUiThread(() -> loadDetailFeed());
            Toast.makeText(getApplicationContext(), R.string.update_data, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),String.format("%s\n%s",getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void messageBox(String method, String message)
    {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    private class ThreadDetailFeed extends AsyncTask<String, Void, String> {

        private TextView mTextViewFeed;

        public ThreadDetailFeed(TextView textViewFeed) {
            this.mTextViewFeed = textViewFeed;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                mStructureDetailFeed = Jsoup.parse(sDescription);
                mStructureDetailFeed.select(P);
            }catch (Exception e){
                messageBox("ThreadDetailFeed",e.getMessage());
            }
            return mDetailFeed = mStructureDetailFeed.text();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTextViewFeed.setText(mDetailFeed);
        }
    }
}


