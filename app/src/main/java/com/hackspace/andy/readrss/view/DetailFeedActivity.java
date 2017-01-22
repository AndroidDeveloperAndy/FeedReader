package com.hackspace.andy.readrss.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.DownloadImageTask;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Message;
import com.hackspace.andy.readrss.refresh.PullToRefreshComponent;
import com.hackspace.andy.readrss.refresh.RefreshListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;


public class DetailFeedActivity extends AppCompatActivity implements ILoaderData <List<Message>>{

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

    private Intent intent;

    private ImageView imgHabra;
    private TextView txHead,txFeed,txLink,txDate;

    private String detailFeed;
    private PullToRefreshComponent pullToRefresh;

    private ViewGroup upperButton,lowerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        upperButton = (ViewGroup) this
                .findViewById(R.id.refresh_upper_button);
        lowerButton = (ViewGroup) this
                .findViewById(R.id.refresh_lower_button);

        loadViews();
        getInfoFromActivity();
        loadDetailFeed();

        this.pullToRefresh = new PullToRefreshComponent(upperButton,
                lowerButton,null, new Handler()); //TODO getList from PrimaryActivity
        this.pullToRefresh.setOnPullDownRefreshAction(new RefreshListener() {

            @Override
            public void refreshFinished() {
                DetailFeedActivity.this.runOnUiThread(() ->loadDetailFeed());
                Log.i(TAG,"pull down");
            }

            @Override
            public void doRefresh() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        this.pullToRefresh.setOnPullUpRefreshAction(new RefreshListener() {

            @Override
            public void refreshFinished() {
                DetailFeedActivity.this.runOnUiThread(() -> loadDetailFeed());
                Log.i(TAG,"pull up");
            }

            @Override
            public void doRefresh() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadDetailFeed(){
        try{
            new DownloadImageTask(imgHabra).execute();
            txHead.setText(title);
            txDate.setText(date);
            txLink.setText(url);
            new ThreadDetailFeed(txFeed).execute(url);
        }catch (Exception e){
            Log.e(TAG, "Error load detail page!", e);
        }
    }

    private void loadViews(){
        txHead = (TextView) findViewById(R.id.head);
        txFeed = (TextView) findViewById(R.id.textFeed);
        txLink = (TextView) findViewById(R.id.link);
        txDate = (TextView) findViewById(R.id.detailFeedDate);
        imgHabra = (ImageView) findViewById(R.id.imgHab);
    }

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

    public class ThreadDetailFeed extends AsyncTask<String, Void, String> {

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
            doc.select("img[src$=.png]");
            doc.select("div");
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


