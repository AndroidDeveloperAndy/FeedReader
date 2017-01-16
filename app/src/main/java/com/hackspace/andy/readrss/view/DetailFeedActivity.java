package com.hackspace.andy.readrss.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.DownloadImageTask;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
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

    //private Document doc;

    private Intent intent;

    private ImageView imgHabra;
    private TextView txHead,txFeed,txLink,txDate;

    //private String detailFeed;

    final static String PICTURE_URL = "https://pp.vk.me/c625620/v625620167/2ac69/m412UXyPZPE.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        loadViews();
        getInfoFromActivity();

        try{
            new DownloadImageTask(imgHabra).execute(PICTURE_URL);
            txHead.setText(title);
            txDate.setText(date);
            txLink.setText(url);
            //new JsoupThreadDetailFeed(txFeed).execute();
            txFeed.setText(description);
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

    /*public class JsoupThreadDetailFeed extends AsyncTask<String, Void, String> {

        private TextView textFeed;

        public JsoupThreadDetailFeed(TextView textViewFeed) {
           this.textFeed  = textViewFeed;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                doc = Jsoup.connect("https://habrahabr.ru/rss/post/319678/").get();
                doc.select("p");
                detailFeed = doc.text();
            } catch (IOException e) {
            e.printStackTrace();
        }
            return detailFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textFeed.setText(detailFeed);
        }
    }*/
}


