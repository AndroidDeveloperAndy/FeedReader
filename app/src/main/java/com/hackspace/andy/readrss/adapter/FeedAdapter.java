package com.hackspace.andy.readrss.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackspace.andy.readrss.R;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PersonViewHolder>{

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mNameFeed;
        private TextView mDateFeed;
        private ImageView mImgHabra;

        PersonViewHolder(View itemView) {
            super(itemView);
            //TODO How about to create custom View init all fields in this View add public method setContent ant pass Entity in this method,instead of boilerplate with findViewById.
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            mNameFeed = (TextView) itemView.findViewById(R.id.feed);
            mDateFeed = (TextView) itemView.findViewById(R.id.dateFeed);
            mImgHabra = (ImageView) itemView.findViewById(R.id.imgHab);
        }
    }

    private List<Message> mMessageList;
    private View mViewItem;
    private PersonViewHolder mPersonViewHolder;
    final static String PICTURE_URL = "https://pp.vk.me/c625620/v625620167/2ac69/m412UXyPZPE.jpg";


    public FeedAdapter(List<Message> messages){
        this.mMessageList = messages;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FeedAdapter.PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mViewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        mPersonViewHolder = new PersonViewHolder(mViewItem);
        Picasso.with(mViewItem.getContext()).load(PICTURE_URL).into(mPersonViewHolder.mImgHabra);
        return mPersonViewHolder;
    }

    @Override
    public void onBindViewHolder(FeedAdapter.PersonViewHolder personViewHolder, int position) {
        personViewHolder.mNameFeed.setText(mMessageList.get(position).getTitle());
        personViewHolder.mDateFeed.setText(mMessageList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
