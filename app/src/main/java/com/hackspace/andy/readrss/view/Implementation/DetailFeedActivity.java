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

public class DetailFeedActivity extends Activity implements ILoaderData <List<Message>>,SwipeRefreshLayout.OnRefreshListener,DetailFeedView {

    private static final String TAG = DetailFeedActivity.class.getName();

    private static final String ARG_TITLE = "_TITLE_ARGUMENT";
    private static final String ARG_DATE = "_DATE_ARGUMENT";
    private static final String ARG_DESCRIPTION = "_DESCRIPTION_ARGUMENT";
    private static final String ARG_LINK = "_LINK_ARGUMENT";

    private static String title;
    private static String date;
    private static String description;
    private static String url;
    private Document doc;

    private List<Message> list;
    private Intent intent;
    private String detailFeed;

    private TextView txHead,txFeed,txLink,txDate;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CardView cardViewDF;

    private MessageService realm;
    private RealmConfiguration configRealmWithDetailFeed;

    private static ConnectivityManager cm;
    private static NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        loadViews();
        getInfoFromActivity();
        getData();
    }

    @Override
    public void getData(){
        if(isOnline(this)) {
            loadDetailFeed();
            Toast.makeText(this, R.string.load_from_network,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, R.string.error_load_picture+"\n"+R.string.check_network,Toast.LENGTH_SHORT).show();
            getDetailFeedFromDatabase();
            Toast.makeText(this,R.string.load_from_database,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void loadDetailFeed(){
        try{
            txHead.setText(title);
            txDate.setText(date);
            txLink.setText(url);
            new ThreadDetailFeed(txFeed).execute(url);
        }catch (Exception e){
            Log.e(TAG, "Error load detail page!", e);
        }
    }

    @Override
    public void loadViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);

        cardViewDF = (CardView) findViewById(R.id.cv);

        txHead = (TextView) findViewById(R.id.head);
        txDate = (TextView) findViewById(R.id.detailFeedDate);
        txFeed = (TextView) findViewById(R.id.textFeed);
        txLink = (TextView) findViewById(R.id.link);

        configRealmWithDetailFeed = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(configRealmWithDetailFeed);

        realm = new MessageService(this);
    }

    @Override
    public void getInfoFromActivity(){
        intent = getIntent();

        title = intent.getStringExtra(ARG_TITLE);
        date = intent.getStringExtra(ARG_DATE);
        url = intent.getStringExtra(ARG_LINK);
        description = intent.getStringExtra(ARG_DESCRIPTION);
    }

    @Override
    public void endLoad(@NonNull List<Message> data) {
        if(data.size() > 0){
            txHead.setText(getIntent().getStringExtra(ARG_TITLE));
            txDate.setText(getIntent().getStringExtra(ARG_DATE));
            txLink.setText(getIntent().getStringExtra(ARG_LINK));
            txFeed.setText(getIntent().getStringExtra(ARG_DESCRIPTION));
        }
    }

    public static Intent newInstance(Context context, String title, String date, String link, String description){
        Intent startIntent = new Intent(context, DetailFeedActivity.class);

        startIntent.putExtra(ARG_TITLE, title);
        startIntent.putExtra(ARG_DATE, date);
        startIntent.putExtra(ARG_LINK, link);
        startIntent.putExtra(ARG_DESCRIPTION, description);

        return startIntent;
    }

    @Override
    public void getDetailFeedFromDatabase(){
        list = realm.query();
        for (Message msg : list){
            txHead.setText(msg.getTitle());
            txDate.setText(msg.getDate());
            new ThreadDetailFeed(txFeed).execute(url);
            txLink.setText(msg.getLink());
        }
    }

    private static boolean isOnline(Context context)
    {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if(isOnline(getApplicationContext())) {
            DetailFeedActivity.this.runOnUiThread(() -> loadDetailFeed());
            Toast.makeText(getApplicationContext(),R.string.update_data,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),R.string.dont_update+"\n"+R.string.check_network,Toast.LENGTH_LONG).show();
        }

    }

    private class ThreadDetailFeed extends AsyncTask<String, Void, String> {

        private TextView textFeed;

        public ThreadDetailFeed(TextView textViewFeed) {
           this.textFeed  = textViewFeed;
        }

        @Override
        protected String doInBackground(String... params) {
            doc = Jsoup.parse(description);
            doc.select("p");
            doc.select("a[href]");
            doc.select("br");
            detailFeed = doc.text();
            return detailFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textFeed.setText(detailFeed);
        }
    }
}

