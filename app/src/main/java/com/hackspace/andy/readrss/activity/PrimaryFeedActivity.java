package com.hackspace.andy.readrss.activity;

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

import com.hackspace.andy.readrss.FeedParserFactory;
import com.hackspace.andy.readrss.Message;
import com.hackspace.andy.readrss.ParserType;
import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.BaseFeedParser;
import com.hackspace.andy.readrss.loader.DownloadImageTask;
import com.hackspace.andy.readrss.loader.ILoaderData;

import java.util.ArrayList;
import java.util.List;

public class PrimaryFeedActivity extends ListActivity implements ILoaderData<List<Message>>{

    private List<Message> messages;
    private BaseFeedParser<List<Message>> loader;
    private ParserType type;
    private ArrayAdapter<String> adapterListParsers,adapterListFeed;
    private long start,duration;
    private List<String> titles;

    private ImageView imgHabra;

    private Intent detailIntent;

    private String urlImageHabr = "https://pp.vk.me/c625620/v625620167/2ac69/m412UXyPZPE.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_feed);

        imgHabra = (ImageView) findViewById(R.id.imgHab);

        new DownloadImageTask(imgHabra).execute(urlImageHabr);
        loadFeed(ParserType.SAX);
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
        startActivity(detailIntent);

        /*Intent viewMessage = new Intent(Intent.ACTION_VIEW,
                Uri.parse(messages.get(position).getLink().toExternalForm()));
        this.startActivity(viewMessage);*/
    }

    private void loadFeed(ParserType type){

        if ((loader != null) && loader.getStatus() != AsyncTask.Status.RUNNING) {
            if (loader.isCancelled()) {
                loader = FeedParserFactory.getParser(type, this);

                loader.execute((Void[]) null);
            } else {
                loader.cancel(true);
                loader = FeedParserFactory.getParser(type, this);

                loader.execute((Void[]) null);
            }

        } else if(loader != null && loader.getStatus() == AsyncTask.Status.PENDING) {

            loader = FeedParserFactory.getParser(type, this);

            loader.execute((Void[]) null);
        } else if ((loader != null) && loader.getStatus() == AsyncTask.Status.FINISHED) {

            loader = FeedParserFactory.getParser(type, this);

            loader.execute((Void[]) null);
        } else if (loader == null) {

            loader = FeedParserFactory.getParser(type, this);

            loader.execute((Void[]) null);
        }

    }

    @Override
    public void endLoad(@NonNull List<Message> data) {
        try{
            start = System.currentTimeMillis();
            messages = data;
            duration = System.currentTimeMillis() - start;
            Log.i("AndroidNews", "Parser duration=" + duration);
            titles = new ArrayList<>(messages.size());
            for (Message msg : messages){
                titles.add(msg.getTitle());
            }
            adapterListFeed = new ArrayAdapter<>(this, R.layout.item, titles);
            this.setListAdapter(adapterListFeed);
        } catch (Throwable t){
            Log.e("AndroidNews",t.getMessage(),t);
        }
    }
}

