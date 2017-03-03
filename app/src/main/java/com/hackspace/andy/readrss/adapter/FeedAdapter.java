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

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hackspace.andy.readrss.util.StringsUtils.PICTURE_URL;

@EBean
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{

    private List<Message> mMessageList;

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv)       CardView mCardView;
        @BindView(R.id.feed)     TextView mNameFeed;
        @BindView(R.id.dateFeed) TextView mDateFeed;
        @BindView(R.id.imgHab)   ImageView mImage;

        FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Inject
    public FeedAdapter(){
        mMessageList = new ArrayList<>();
    }

    public void setFeed(List<Message> messages) {
        this.mMessageList = messages;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View mViewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        FeedViewHolder mPersonViewHolder = new FeedViewHolder(mViewItem);
        Picasso.with(mViewItem.getContext()).load(PICTURE_URL).into(mPersonViewHolder.mImage);
        return mPersonViewHolder;
    }

    @Override
    public void onBindViewHolder(FeedAdapter.FeedViewHolder holder, int position) {
        holder.mNameFeed.setText(mMessageList.get(position).getTitle());
        holder.mDateFeed.setText(mMessageList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
