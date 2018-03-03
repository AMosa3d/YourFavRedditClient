package com.example.abdel.yourfavredditclient.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abdel.yourfavredditclient.Activities.DetailActivity;
import com.example.abdel.yourfavredditclient.Interfaces.TabletModeCommunicator;
import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.R;

import java.util.List;

/**
 * Created by abdel on 2/26/2018.
 */

public class PostsPagerAdapter extends PagerAdapter {

    List<Post> posts;
    Context context;
    TabletModeCommunicator communicator;
    boolean tabletMode;

    public PostsPagerAdapter(List<Post> posts, Context context, boolean tabletMode) {
        this.posts = posts;
        this.context = context;
        communicator = (TabletModeCommunicator) context;
        this.tabletMode = tabletMode;
    }

    public void setPosts(List<Post> posts, Context context) {
        this.context = context;
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void setTabletMode(boolean tabletMode) {
        this.tabletMode = tabletMode;
    }

    public void setCommunicator(TabletModeCommunicator communicator) {
        this.communicator = communicator;
    }

    @Override
    public int getCount() {
        if (posts == null || posts.size() == 0)
            return 0;
        return posts.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.posts_item,null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (communicator == null)
                    communicator = (TabletModeCommunicator) context;

                if (tabletMode)
                {
                    communicator.passPost(posts.get(position));
                }
                else
                {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(context.getString(R.string.post_object_key),posts.get(position));
                    context.startActivity(intent);
                }
            }
        });

        fillDataToUI(view,position);
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (view == object)
            return true;
        return false;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    void fillDataToUI(View view, int index)
    {
        TextView titleTV,authorTV,subredditTV;
        ImageView thumbnailIM;
        Post currentPost = posts.get(index);

        titleTV = (TextView) view.findViewById(R.id.title_textView);
        authorTV = (TextView) view.findViewById(R.id.author_textView);
        subredditTV = (TextView) view.findViewById(R.id.subreddit_textView);
        thumbnailIM = (ImageView) view.findViewById(R.id.thumbnail_imageView);

        titleTV.setText(currentPost.getTitle());
        authorTV.setText("By : " + currentPost.getAuthor());
        subredditTV.setText("To : " + currentPost.getSubreddit());
        Glide.with(context).load(currentPost.getThumbnail()).into(thumbnailIM);
    }
}
