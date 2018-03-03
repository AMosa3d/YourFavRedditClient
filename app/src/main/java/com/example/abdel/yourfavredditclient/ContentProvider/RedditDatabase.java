package com.example.abdel.yourfavredditclient.ContentProvider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by abdel on 2/28/2018.
 */

@Database(version = RedditDatabase.VERSION)

public final class RedditDatabase {
    public static final int VERSION = 1;

    @Table(SubredditsColumns.class) public static final String SUBREDDITS_TABLE = "subreddits";

    @Table(PostsColumns.class) public static final String POSTS_TABLE = "posts";
}
