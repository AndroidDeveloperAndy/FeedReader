package com.hackspace.andy.readrss.view;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.hackspace.andy.readrss.loader.FeedParserFactory;
import com.hackspace.andy.readrss.model.Message;
import com.hackspace.andy.readrss.enums.ParserType;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.BaseFeedParser;
import com.hackspace.andy.readrss.loader.DownloadImageTask;
import com.hackspace.andy.readrss.loader.ILoaderData;

import java.util.ArrayList;
import java.util.List;

public class PrimaryFeedActivity extends ListActivity implements ILoaderData<List<Message>>{

    private static final String TAG = PrimaryFeedActivity.class.getName();
    private List<Message> messagesList;
    private BaseFeedParser<List<Message>> loader;
    private ParserType type;
    private ArrayAdapter<String> adapterListParsers,adapterListFeed;
    private long start,duration;
    private List<String> titles;
    //private List<String> date;

    private ImageView imgHabra;

    private Intent detailIntent;

    private static String feedUrl = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546";

    private String urlImageHabr = "https://pp.vk.me/c625620/v625620167/2ac69/m412UXyPZPE.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_feed);

        imgHabra = (ImageView) findViewById(R.id.imgHab);

        try {
            new DownloadImageTask(imgHabra).execute(urlImageHabr);
            loadFeed(ParserType.SAX);
        }catch (Exception e){
            Log.e(TAG, "Error load home page!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, ParserType.ANDROID_SAX.ordinal(), ParserType.ANDROID_SAX.ordinal(), "Android Sax");
        menu.add(Menu.NONE, ParserType.SAX.ordinal(), ParserType.SAX.ordinal(), "Classic SAX");
        menu.add(Menu.NONE, ParserType.DOM.ordinal(), ParserType.DOM.ordinal(), "DOM");
        menu.add(Menu.NONE, ParserType.XML_PULL.ordinal(), ParserType.XML_PULL.ordinal(), "XML Pull");

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);

        type = ParserType.values()[item.getItemId()];
        adapterListParsers = (ArrayAdapter<String>) this.getListAdapter();
        if (adapterListParsers.getCount() > 0){
            adapterListParsers.clear();
        }
        this.loadFeed(type);

        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        detailIntent = new Intent(this, DetailFeedActivity.class);
        detailIntent.putExtra("LINK", messagesList.get(position).toString());
        detailIntent.putExtra("TITLE", messagesList.get(position).getTitle());

        startActivity(detailIntent);
    }

    private void loadFeed(ParserType type){
        try {
            if ((loader != null) && loader.getStatus() != AsyncTask.Status.RUNNING) {
                if (loader.isCancelled()) {
                    loader = FeedParserFactory.getParser(type, this, feedUrl);

                    loader.execute((Void[]) null);
                } else {
                    loader.cancel(true);
                    loader = FeedParserFactory.getParser(type, this, feedUrl);

                    loader.execute((Void[]) null);
                }

            } else if (loader != null && loader.getStatus() == AsyncTask.Status.PENDING) {

                loader = FeedParserFactory.getParser(type, this, feedUrl);

                loader.execute((Void[]) null);
            } else if ((loader != null) && loader.getStatus() == AsyncTask.Status.FINISHED) {

                loader = FeedParserFactory.getParser(type, this, feedUrl);

                loader.execute((Void[]) null);
            } else if (loader == null) {

                loader = FeedParserFactory.getParser(type, this, feedUrl);

                loader.execute((Void[]) null);
            }
        }catch (Exception e){
            Log.e(TAG, "Error load feed in the home page!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endLoad(@NonNull List<Message> data) {
        try{
            start = System.currentTimeMillis();
            messagesList = data;
            duration = System.currentTimeMillis() - start;
            Log.i("AndroidNews", "Parser duration=" + duration);
            titles = new ArrayList<>(messagesList.size());
            //date = new ArrayList<>(messagesList.size());
            for (Message msg : messagesList){
                titles.add(msg.getTitle());
                //date.add(msg.getDate());
            }
            adapterListFeed = new ArrayAdapter<>(this, R.layout.item, titles);
            this.setListAdapter(adapterListFeed);
        } catch (Throwable t){
            Log.e(TAG,t.getMessage(),t);
        }
    }
}

