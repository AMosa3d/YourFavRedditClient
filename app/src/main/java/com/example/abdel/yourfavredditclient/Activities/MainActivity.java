package com.example.abdel.yourfavredditclient.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abdel.yourfavredditclient.Fragment.DetailFragment;
import com.example.abdel.yourfavredditclient.Fragment.HomeFragment;
import com.example.abdel.yourfavredditclient.Interfaces.PassPostsInterface;
import com.example.abdel.yourfavredditclient.Interfaces.TabletModeCommunicator;
import com.example.abdel.yourfavredditclient.Models.Account;
import com.example.abdel.yourfavredditclient.Models.OAuthAccessToken;
import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.R;
import com.example.abdel.yourfavredditclient.Utils.DatabaseUtils;
import com.example.abdel.yourfavredditclient.Utils.LogicUtils;
import com.example.abdel.yourfavredditclient.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PassPostsInterface, TabletModeCommunicator {

    private static final String POSITION_SAVE_INSTANCE_BUNDLE_KEY = "current_item_position";
    private static final String POSTS_SAVE_INSTANCE_BUNDLE_KEY = "posts_list";
    final String CODE_KEY = "code";
    HomeFragment homeFragment = new HomeFragment();
    final String REDIRECT_URI = "redirecturi://relaunchapp";
    List<Post> posts = new ArrayList<>();
    ActionBarDrawerToggle toggle;
    boolean tabletMode;
    DetailFragment detailFragment;

    SharedPreferences preferences;
    Account account;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.open_nav_drawer,R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_stared_subreddits_menu_item)
                {
                    startActivity(new Intent(getBaseContext(), StaredSubredditsActivity.class));
                    return true;
                }
                return false;
            }
        });

        if (findViewById(R.id.detail_container) != null)
            tabletMode = true;

        homeFragment.setTabletMode(tabletMode);

        if (savedInstanceState == null)
        {
            if (tabletMode)
            {
                detailFragment = new DetailFragment();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_container, detailFragment)
                        .commit();
            }


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_container, homeFragment)
                    .commit();
        }
        pullDataFromRestAPI();
    }

    void pullDataFromRestAPI()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String code = preferences.getString(getString(R.string.code_pref_key),"");

        if (code.equals(""))
        {
            Uri authenticationUri = NetworkUtils.buildAuthURI();

            Intent intent = new Intent(Intent.ACTION_VIEW,authenticationUri);
            startActivity(intent);
        }
        else {
            String accessToken = preferences.getString(getString(R.string.access_token_pref_key), "");
            NetworkUtils.getPosts(this,this,accessToken);
            NetworkUtils.getAccount(this,this,accessToken);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().substring(0, REDIRECT_URI.length()).equals(REDIRECT_URI)) {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();

            String code = uri.getQueryParameter(CODE_KEY);

            preferences.edit().putString(getString(R.string.code_pref_key), code).apply();

            NetworkUtils.getAuthToken(getApplicationContext(),this,code);

            getIntent().setData(null);
        }

        passPostsData(posts);

        super.onResume();
    }

    void passPostsData(List<Post> posts)
    {
        posts = LogicUtils.filterByFavSubreddits(posts, DatabaseUtils.retrieveStaredSubreddit(this));
        this.posts = posts;
        DatabaseUtils.saveTopFivePosts(this,posts);
        homeFragment.setPostsList(posts,this);
    }

    @Override
    public void PassPostsThrough(List<Post> posts) {
        passPostsData(posts);
    }

    @Override
    public void PassAccountThrough(Account account) {

        this.account = account;
        View navHeader = navView.getHeaderView(0);

        TextView navName = (TextView)(navHeader.findViewById(R.id.nav_name_textview));
        navName.setText(account.getName());

        ImageView navAvatar = (ImageView) navHeader.findViewById(R.id.nav_avatar_imageview);
        Glide.with(this).load(account.getAvatar()).into(navAvatar);
    }

    @Override
    public void SaveToken(OAuthAccessToken token) {
        preferences.edit()
                .putString(getString(R.string.access_token_pref_key),token.getAccessToken())
                .putString(getString(R.string.refresh_token_pref_key),token.getRefreshToken())
                .apply();
    }

    @Override
    public void passPost(Post currentPost) {
        detailFragment = new DetailFragment();
        detailFragment.setPost(currentPost);

        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,detailFragment).commit();
    }
}
