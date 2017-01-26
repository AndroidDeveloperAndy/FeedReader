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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

    private TextView mTxHead, mTxFeed, mTxLink, mTxDate;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CardView mCardViewDetailFeed;

    private MessageService mRealm;
    private RealmConfiguration mConfigRealmWithDetailFeed;

    private static ConnectivityManager mConnectManager;
    private static NetworkInfo mNetworkInfo;

    private AlertDialog.Builder mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        loadViews();
        getInfoFromActivity();
        getData();
    }

    @Override
    public void getData() {
        if (isOnline(this)) {
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
            getAlertDialog();
            e.getMessage();
            Log.e(TAG, "Error load detail page!", e);
        }
    }

    @Override
    public void loadViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);

        mCardViewDetailFeed = (CardView) findViewById(R.id.cv);

        mTxHead = (TextView) findViewById(R.id.head);
        mTxDate = (TextView) findViewById(R.id.detailFeedDate);
        mTxFeed = (TextView) findViewById(R.id.textFeed);
        mTxLink = (TextView) findViewById(R.id.link);

        mConfigRealmWithDetailFeed = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(mConfigRealmWithDetailFeed);

        mRealm = new MessageService(this);
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
        mList = mRealm.query();
        for (Message msg : mList) {
            mTxHead.setText(msg.getTitle());
            mTxDate.setText(msg.getDate());
            new ThreadDetailFeed(mTxFeed).execute(sUrl);
            mTxLink.setText(msg.getLink());
        }
    }

    @Override
    public void getAlertDialog(){
        mAlertDialog = new AlertDialog.Builder(getApplicationContext());
        mAlertDialog.setTitle("Dialog");
        mAlertDialog.setMessage("Error load feed.");
        mAlertDialog.setPositiveButton("Retry", (dialog, which) -> {
            loadDetailFeed();
        });
        mAlertDialog.setNegativeButton("Cancel", (dialog, which) -> {
            DetailFeedActivity.this.finish();
        });
        mAlertDialog.setCancelable(true);
        mAlertDialog.setOnCancelListener(dialog -> {
            DetailFeedActivity.this.finish();
        });
    }

    private static boolean isOnline(Context context) {
        mConnectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (isOnline(getApplicationContext())) {
            DetailFeedActivity.this.runOnUiThread(() -> loadDetailFeed());
            Toast.makeText(getApplicationContext(), R.string.update_data, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),String.format("%s\n%s",getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
        }

    }

    private class ThreadDetailFeed extends AsyncTask<String, Void, String> {

        private TextView mTextViewFeed;

        public ThreadDetailFeed(TextView textViewFeed) {
            this.mTextViewFeed = textViewFeed;
        }

        @Override
        protected String doInBackground(String... params) {
            mStructureDetailFeed = Jsoup.parse(sDescription);
            mStructureDetailFeed.select(P);
            return mDetailFeed = mStructureDetailFeed.text();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTextViewFeed.setText(mDetailFeed);
        }
    }
}


