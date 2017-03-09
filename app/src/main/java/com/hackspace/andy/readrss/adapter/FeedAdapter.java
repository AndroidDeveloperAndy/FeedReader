package com.hackspace.andy.readrss.adapter;

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

import java.util.List;

import butterknife.ButterKnife;

import static com.hackspace.andy.readrss.util.ResourceUtils.PICTURE_URL;

@EBean
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{

    private List<Message> mMessageList;

    static class FeedViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameFeed;
        private TextView mDateFeed;
        private ImageView mImage;

        FeedViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.imgHab);
            mNameFeed = (TextView)  itemView.findViewById(R.id.feed);
            mDateFeed = (TextView)  itemView.findViewById(R.id.dateFeed);
            ButterKnife.bind(this, itemView);
        }
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
