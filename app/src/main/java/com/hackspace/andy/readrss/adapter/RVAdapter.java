package com.hackspace.andy.readrss.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.loader.Implementation.DownloadImageTask;
import com.hackspace.andy.readrss.model.Entity.Message;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private TextView nameFeed;
        private TextView dateFeed;
        private ImageView imgHabra;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nameFeed = (TextView)itemView.findViewById(R.id.feed);
            dateFeed = (TextView)itemView.findViewById(R.id.dateFeed);
            imgHabra = (ImageView)itemView.findViewById(R.id.imgHab);
            new DownloadImageTask(imgHabra).execute();
        }
    }

    private List<Message> messageList;
    private View viewItem;
    private PersonViewHolder personViewHolder;

    public RVAdapter(List<Message> messages){
        this.messageList = messages;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RVAdapter.PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        personViewHolder = new PersonViewHolder(viewItem);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(RVAdapter.PersonViewHolder personViewHolder, int position) {
        personViewHolder.nameFeed.setText(messageList.get(position).getTitle());
        personViewHolder.dateFeed.setText(messageList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
