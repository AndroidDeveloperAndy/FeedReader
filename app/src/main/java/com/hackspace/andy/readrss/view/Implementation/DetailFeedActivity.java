package com.hackspace.andy.readrss.view.implementation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.interfaces.ILoaderData;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.model.Implementation.MessageService;
import com.hackspace.andy.readrss.model.interfaces.MessagesServiceImpl;
import com.hackspace.andy.readrss.util.DialogFactory;
import com.hackspace.andy.readrss.util.NetworkUtil;
import com.hackspace.andy.readrss.view.interfaces.DetailFeedView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import static com.hackspace.andy.readrss.util.ResourceUtils.DATE_ARG;
import static com.hackspace.andy.readrss.util.ResourceUtils.DESCRIPTION_ARG;
import static com.hackspace.andy.readrss.util.ResourceUtils.LINK_ARG;
import static com.hackspace.andy.readrss.util.ResourceUtils.TAB;
import static com.hackspace.andy.readrss.util.ResourceUtils.TITLE_ARG;

@EActivity(R.layout.activity_detail_feed)
public class DetailFeedActivity extends Activity implements ILoaderData<List<Message>>,
                                                            SwipeRefreshLayout.OnRefreshListener,
                                                            DetailFeedView{

    @ViewById(R.id.cv)              CardView mCardViewDetailFeed;
    @ViewById(R.id.head)            TextView mTxHead;
    @ViewById(R.id.detailFeedDate)  TextView mTxDate;
    @ViewById(R.id.link)            TextView mTxLink;
    @ViewById(R.id.textFeed)        TextView mTxFeed;
    @ViewById(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @Extra(TITLE_ARG)               public static String ARG_TITLE;
    @Extra(DATE_ARG)                public static String ARG_DATE;
    @Extra(DESCRIPTION_ARG)         public static String ARG_DESCRIPTION;
    @Extra(LINK_ARG)                public static String ARG_LINK;

    private static String sTitle,sDate,sDescription,sUrl;
    private MessagesServiceImpl mRealm;
    private List<Message> mList;
    private Intent mIntent;

    @AfterViews
    @Override
    public void loadViews() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
    public void getData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            DialogFactory.createProgressDialog(this, R.string.load_from_network);
            loadDetailFeedFromNetwork();
        } else {
            DialogFactory.createProgressDialog(this, R.string.load_from_database);
            loadDetailFeedFromDatabase();
        }
    }

    @Override
    public void loadDetailFeedFromNetwork() {
        try {
            mTxHead.setText(sTitle);
            mTxDate.setText(sDate);
            mTxLink.setText(sUrl);
            new ThreadDetailFeed(mTxFeed).execute(sUrl);
        } catch (Exception e) {
            showError();
            DialogFactory.messageBox("loadDetailFeed",e.getMessage(),this);
        }
    }

    @Override
    public void loadDetailFeedFromDatabase() {
        mRealm = new MessageService();
        mList = mRealm.query();
        try {
            for (Message msg : mList) {
                mTxHead.setText(sTitle);
                mTxDate.setText(sDate);
                mTxLink.setText(sUrl);
                new ThreadDetailFeed(mTxFeed).execute(msg.getLink());
            }
        }catch (Exception e){
            showError();
            DialogFactory.messageBox("getDetailFeedFromDatabase",e.getMessage(),this);
        }
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
        Intent startIntent = DetailFeedActivity_.intent(context).get();
        startIntent.putExtra(ARG_TITLE, title);
        startIntent.putExtra(ARG_DATE, date);
        startIntent.putExtra(ARG_LINK, link);
        startIntent.putExtra(ARG_DESCRIPTION, description);
        return startIntent;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (NetworkUtil.isNetworkAvailable(this)) {
            DialogFactory.createProgressDialog(this, R.string.update_data);
            DetailFeedActivity.this.runOnUiThread(() -> loadDetailFeedFromNetwork());
        } else {
            Toast.makeText(getApplicationContext(),String.format(TAB,getString(R.string.dont_update),getString(R.string.check_network)),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_feed)).show();
    }

    private class ThreadDetailFeed extends AsyncTask<String, Void, String> {

        private TextView mTextViewFeed;
        private Document mStructureDetailFeed;
        private String mDetailFeed;

        public ThreadDetailFeed(TextView textViewFeed) {
            this.mTextViewFeed = textViewFeed;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                mStructureDetailFeed = Jsoup.parse(sDescription);
                mStructureDetailFeed.select("p");
            }catch (Exception e){
                showError();
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


