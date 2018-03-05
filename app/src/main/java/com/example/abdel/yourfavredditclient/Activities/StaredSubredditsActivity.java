package com.example.abdel.yourfavredditclient.Activities;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.abdel.yourfavredditclient.Adapters.StaredSubredditsAdapter;
import com.example.abdel.yourfavredditclient.Interfaces.PassSubredditsInterface;
import com.example.abdel.yourfavredditclient.Models.Subreddit;
import com.example.abdel.yourfavredditclient.R;
import com.example.abdel.yourfavredditclient.Utils.DatabaseUtils;
import com.example.abdel.yourfavredditclient.Utils.LogicUtils;
import com.example.abdel.yourfavredditclient.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class StaredSubredditsActivity extends AppCompatActivity implements PassSubredditsInterface,LoaderManager.LoaderCallbacks<Cursor> {

    List<Subreddit> subreddits;
    List<String> staredSubreddit = new ArrayList<>();
    StaredSubredditsAdapter adapter;
    final int LOADER_ID = 34;
    RecyclerView recyclerView;
    private static final String POSITION_SAVE_INSTANCE_BUNDLE_KEY = "current_item_position";
    private static final String SUBREDDITS_SAVE_INSTANCE_BUNDLE_KEY = "subreddit_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stared_subreddits);

        recyclerView = (RecyclerView) findViewById(R.id.stared_subreddit_recyclerView);
        adapter = new StaredSubredditsAdapter(this);

        if (savedInstanceState != null)
        {
            subreddits = savedInstanceState.getParcelableArrayList(SUBREDDITS_SAVE_INSTANCE_BUNDLE_KEY);
            recyclerView.setVerticalScrollbarPosition(savedInstanceState.getInt(POSITION_SAVE_INSTANCE_BUNDLE_KEY));
        }
        else {
            getSupportLoaderManager().initLoader(LOADER_ID,null,this).forceLoad();
            NetworkUtils.getSubreddits(this, this, PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.access_token_pref_key), ""));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter.setStaredSubreddits(subreddits);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SUBREDDITS_SAVE_INSTANCE_BUNDLE_KEY, (ArrayList<? extends Parcelable>) adapter.getStaredSubreddits());
        outState.putInt(POSITION_SAVE_INSTANCE_BUNDLE_KEY,recyclerView.getVerticalScrollbarPosition());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void passSubreddits(List<Subreddit> subreddits) {
        subreddits = LogicUtils.reorderStaredList(subreddits, staredSubreddit);
        this.subreddits = subreddits;
        adapter.setStaredSubreddits(subreddits);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {


            Cursor subredditCursor = null;

            @Override
            protected void onStartLoading() {
                if (subredditCursor == null)
                    forceLoad();
                else
                    deliverResult(subredditCursor);
            }

            @Override
            public void deliverResult(Cursor data) {
                subredditCursor = data;
                super.deliverResult(data);
            }

            @Override
            public Cursor loadInBackground() {
                return DatabaseUtils.retrieveStaredSubreddit(StaredSubredditsActivity.this);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        staredSubreddit = DatabaseUtils.getStaredSubredditNames(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {

    }
}
