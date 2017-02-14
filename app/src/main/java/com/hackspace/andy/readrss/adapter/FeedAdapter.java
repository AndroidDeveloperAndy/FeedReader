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

import butterknife.ButterKnife;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PersonViewHolder>{

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mNameFeed;
        private TextView mDateFeed;
        private ImageView mImgHabra;

        PersonViewHolder(View itemView) {
            super(itemView);
            mImgHabra = (ImageView) itemView.findViewById(R.id.imgHab);
            mNameFeed = (TextView)  itemView.findViewById(R.id.feed);
            mDateFeed = (TextView)  itemView.findViewById(R.id.dateFeed);
            mCardView = (CardView)  itemView.findViewById(R.id.cv);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Message> mMessageList;
    private View mViewItem;
    private PersonViewHolder mPersonViewHolder;
    final static String PICTURE_URL = "https://habrastorage.org/getpro/habr/app/566/b4a/0ef/566b4a0efc6eb3f762c632a89f4a03a3.jpg";

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
