package com.example.abdel.yourfavredditclient.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdel.yourfavredditclient.Models.Subreddit;
import com.example.abdel.yourfavredditclient.R;
import com.example.abdel.yourfavredditclient.Utils.DatabaseUtils;

import java.util.List;

/**
 * Created by abdel on 2/28/2018.
 */

public class StaredSubredditsAdapter extends RecyclerView.Adapter<StaredSubredditsAdapter.StaredSubredditsViewHolder> {

    List<Subreddit> staredSubreddits;
    Context context;

    public StaredSubredditsAdapter(Context context) {
        this.context = context;
    }

    public void setStaredSubreddits(List<Subreddit> staredSubreddits) {
        this.staredSubreddits = staredSubreddits;
        notifyDataSetChanged();
    }

    @Override
    public StaredSubredditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StaredSubredditsViewHolder(
                LayoutInflater.from(context)
                .inflate(R.layout.stared_subreddit_single_item,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(StaredSubredditsViewHolder holder, int position) {
        Subreddit currentSubreddit = staredSubreddits.get(position);
        holder.bind(currentSubreddit,position);
    }

    public List<Subreddit> getStaredSubreddits() {
        return staredSubreddits;
    }

    @Override
    public int getItemCount() {
        if (staredSubreddits == null || staredSubreddits.size() == 0)
            return 0;
        return staredSubreddits.size();
    }

    class StaredSubredditsViewHolder extends RecyclerView.ViewHolder
    {
        TextView staredSubredditNameTextView;
        ImageView staredImageView;
        String name;
        boolean isStared;

        public StaredSubredditsViewHolder(View itemView) {
            super(itemView);
            staredSubredditNameTextView = (TextView) itemView.findViewById(R.id.stared_subreddit_textView);
            staredImageView = (ImageView) itemView.findViewById(R.id.star_imageView);
        }

        void bind(Subreddit subreddit, final int position)
        {
            name = subreddit.getName();
            isStared = subreddit.getStared();
            staredSubredditNameTextView.setText(name);
            staredImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isStared)
                    {
                        DatabaseUtils.unFavSubreddit(context,name);
                        staredImageView.setImageResource(R.drawable.black_star);
                        isStared = false;
                    }
                    else
                    {
                        DatabaseUtils.favSubreddit(context,name);
                        staredImageView.setImageResource(R.drawable.yellow_star);
                        isStared = true;
                    }
                    staredSubreddits.get(position).setStared(isStared);
                }
            });

            if (isStared)
                staredImageView.setImageResource(R.drawable.yellow_star);
            else
                staredImageView.setImageResource(R.drawable.black_star);
        }
    }
}
