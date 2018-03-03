package com.example.abdel.yourfavredditclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.R;

import com.example.abdel.yourfavredditclient.Fragment.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    Post currentPost;
    DetailFragment detailFragment = new DetailFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent != null)
        {
            currentPost = intent.getParcelableExtra(getString(R.string.post_object_key));
        }

        if (savedInstanceState == null)
        {
            detailFragment.setPost(currentPost);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, detailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
