package com.example.abdel.yourfavredditclient.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.R;
import com.example.abdel.yourfavredditclient.Utils.DatabaseUtils;

import java.util.List;

/**
 * Created by abdel on 2/28/2018.
 */

public class PostsWidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    List<Post> posts;
    SharedPreferences preferences;

    public PostsWidgetRemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String accessToken = preferences.getString(context.getString(R.string.access_token_pref_key),"");

        posts = DatabaseUtils.getWidgetData(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (posts == null)
            return 0;
        return posts.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.posts_widget_single_item);

        //remoteView.setTextViewText(R.id.title_widget_textView,"TITLE");
        //remoteView.setTextViewText(R.id.author_widget_textView,"By ");
        //remoteView.setTextViewText(R.id.subreddit_widget_textView,"To ");

        remoteView.setTextViewText(R.id.title_widget_textView,posts.get(position).getTitle());
        remoteView.setTextViewText(R.id.author_widget_textView,"By " + posts.get(position).getAuthor());
        remoteView.setTextViewText(R.id.subreddit_widget_textView,"To " + posts.get(position).getSubreddit());

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
