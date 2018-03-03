package com.example.abdel.yourfavredditclient.ContentProvider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by abdel on 2/28/2018.
 */

@ContentProvider(authority = RedditProvider.AUTHORITY,database = RedditDatabase.class)
public final class RedditProvider {
    public static final String AUTHORITY = "com.example.abdel.yourfavredditclient";

    @TableEndpoint(table = RedditDatabase.SUBREDDITS_TABLE)
    public static class Subreddit
    {
        @ContentUri(path = "subreddits", type = "vnd.android.cursor.dir/subreddit",defaultSort = SubredditsColumns._ID)
        public static final Uri SUBREDDITS = Uri.parse("content://" + AUTHORITY + "/subreddits");
    }

    @TableEndpoint(table = RedditDatabase.POSTS_TABLE)
    public static class Post
    {
        @ContentUri(path = "posts", type = "vnd.android.cursor.dir/post",defaultSort = PostsColumns._ID)
        public static final Uri POSTS = Uri.parse("content://" + AUTHORITY + "/posts");
    }
}
