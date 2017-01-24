package com.hackspace.andy.readrss.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.model.Entity.Message;

import java.util.List;

public class FeedAdapter extends BaseAdapter {

    private List<Message> listEntity;
    private LayoutInflater lInflater;
    private Activity activity;

    private View view;
    private Message data;

    private TextView feed,date;

    public FeedAdapter(Activity activity, List<Message> entity) {
        this.activity = activity;
        this.listEntity = entity;
        lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ((null != listEntity) ? listEntity.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Message getItem(int position) {
        return ((null != listEntity) ? listEntity.get(position) : null);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        view = convertView;

        if (null == view) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        data = listEntity.get(position);

        if (null != data) {

            feed = (TextView) view.findViewById(R.id.feed);
            feed.setText(data.getTitle());

            date = (TextView) view.findViewById(R.id.dateFeed);
            date.setText(data.getDate());
        }
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

