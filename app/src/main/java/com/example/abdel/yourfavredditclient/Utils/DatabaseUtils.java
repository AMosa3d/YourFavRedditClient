package com.example.abdel.yourfavredditclient.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.abdel.yourfavredditclient.ContentProvider.PostsColumns;
import com.example.abdel.yourfavredditclient.ContentProvider.RedditProvider;
import com.example.abdel.yourfavredditclient.ContentProvider.SubredditsColumns;
import com.example.abdel.yourfavredditclient.Models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdel on 2/28/2018.
 */

public final class DatabaseUtils {

    public static void favSubreddit(Context context, String name)
    {
        ContentValues values = new ContentValues();
        values.put(SubredditsColumns.SUBREDDIT_NAME,name);

        context.getContentResolver().insert(
                RedditProvider.Subreddit.SUBREDDITS,
                values
        );
    }

    public static void unFavSubreddit(Context context, String name)
    {
        context.getContentResolver().delete(
                RedditProvider.Subreddit.SUBREDDITS,
                SubredditsColumns.SUBREDDIT_NAME + "=?",
                new String[]{name}
        );
    }

    public static List<String> retrieveStaredSubreddit(Context context)
    {
        Cursor cursor = context.getContentResolver().query(
                RedditProvider.Subreddit.SUBREDDITS,
                null,
                null,
                null,
                null
        );

        List<String> result = new ArrayList<>();
        if (cursor == null)
            return result;

        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount();i++)
        {
            result.add(cursor.getString(1));
            cursor.moveToNext();
        }

        return result;
    }

    public static void saveTopFivePosts(Context context, List<Post> posts)
    {
        if(posts.size() == 0)
            return;

        final int SIZE = 5;

        context.getContentResolver().delete(
                RedditProvider.Post.POSTS,
                null,
                null
        );


        ContentValues values = new ContentValues();
        for (int i=0;i<SIZE;i++)
        {
            Post currentPost = posts.get(i);

            values.put(PostsColumns.POST_TITLE,currentPost.getTitle());
            values.put(PostsColumns.POST_SUBREDDIT,currentPost.getSubreddit());
            values.put(PostsColumns.POST_AUTHOR,currentPost.getAuthor());

            context.getContentResolver().insert(
                    RedditProvider.Post.POSTS,
                    values
            );
        }

    }

    public static List<Post> getWidgetData(Context context) {
        Cursor cursor = context.getContentResolver().query(
                RedditProvider.Post.POSTS,
                null,
                null,
                null,
                null
        );

        List<Post> result = new ArrayList<>();
        if (cursor == null)
            return result;

        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount();i++)
        {
            result.add(new Post(
                    "",
                    cursor.getString(cursor.getColumnIndex(PostsColumns.POST_TITLE)),
                    "",
                    cursor.getString(cursor.getColumnIndex(PostsColumns.POST_SUBREDDIT)),
                    cursor.getString(cursor.getColumnIndex(PostsColumns.POST_AUTHOR)),
                    "","","","",""
                    )
            );
            cursor.moveToNext();
        }

        return result;
    }
}
