package com.hackspace.andy.readrss.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.enums.ParserType;
import com.hackspace.andy.readrss.loader.BaseFeedParser;
import com.hackspace.andy.readrss.loader.FeedParserFactory;
import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Message;

import java.util.List;

public class DetailFeedActivity extends AppCompatActivity implements ILoaderData <String>{

    private static final String TAG = DetailFeedActivity.class.getName();
    private BaseFeedParser<List<Message>> loader;
    private static String url;
    private static String title;
    TextView txHead,txFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        txHead = (TextView) findViewById(R.id.head);
        txFeed = (TextView) findViewById(R.id.textFeed);

        try{
            loadFeed(ParserType.SAX);
            txHead.setText(title);
        }catch (Exception e){
            Log.e(TAG, "Error load detail page!", e);
            throw new RuntimeException(e);
        }
    }

    private void loadFeed(ParserType type){
        try {
            Intent intent = getIntent();
            url = intent.getStringExtra("LINK");
            title = intent.getStringExtra("TITLE");
            if ((loader != null) && loader.getStatus() != AsyncTask.Status.RUNNING) {
                if (loader.isCancelled()) {
                    loader = FeedParserFactory.getParser(type, this, url);

                    loader.execute((Void[]) null);
                } else {
                    loader.cancel(true);
                    loader = FeedParserFactory.getParser(type, this, url);

                    loader.execute((Void[]) null);
                }

            } else if (loader != null && loader.getStatus() == AsyncTask.Status.PENDING) {

                loader = FeedParserFactory.getParser(type, this, url);

                loader.execute((Void[]) null);
            } else if ((loader != null) && loader.getStatus() == AsyncTask.Status.FINISHED) {

                loader = FeedParserFactory.getParser(type, this, url);

                loader.execute((Void[]) null);
            } else if (loader == null) {

                loader = FeedParserFactory.getParser(type, this, url);

                loader.execute((Void[]) null);
            }
        }catch (Exception e){
            Log.e(TAG, "Error load feed!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endLoad(@NonNull String data) {

    }
}
