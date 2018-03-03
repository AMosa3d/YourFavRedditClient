package com.example.abdel.yourfavredditclient.Activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
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

public class StaredSubredditsActivity extends AppCompatActivity implements PassSubredditsInterface {

    List<Subreddit> subreddits;
    StaredSubredditsAdapter adapter;
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
        else
            NetworkUtils.getSubreddits(this, this, PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.access_token_pref_key),""));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter.setStaredSubreddits(subreddits);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SUBREDDITS_SAVE_INSTANCE_BUNDLE_KEY, (ArrayList<? extends Parcelable>) subreddits);
        outState.putInt(POSITION_SAVE_INSTANCE_BUNDLE_KEY,recyclerView.getVerticalScrollbarPosition());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void passSubreddits(List<Subreddit> subreddits) {
        subreddits = LogicUtils.reorderStaredList(subreddits, DatabaseUtils.retrieveStaredSubreddit(this));
        this.subreddits = subreddits;
        adapter.setStaredSubreddits(subreddits);
    }
}
