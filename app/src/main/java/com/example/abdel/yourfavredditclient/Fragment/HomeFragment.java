package com.example.abdel.yourfavredditclient.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdel.yourfavredditclient.Adapters.PostsPagerAdapter;
import com.example.abdel.yourfavredditclient.Interfaces.TabletModeCommunicator;
import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdel on 2/22/2018.
 */

public class HomeFragment extends Fragment {

    private static final String POSITION_SAVE_INSTANCE_BUNDLE_KEY = "current_item_position";
    private static final String POSTS_SAVE_INSTANCE_BUNDLE_KEY = "posts_list";
    private static final String TABLET_SAVE_INSTANCE_BUNDLE_KEY = "tablet_mode";
    List<Post> postsList;
    boolean tabletMode;
    ViewPager postsPager;
    PostsPagerAdapter adapter;
    int position = 0;
    AdView adView;

    public void setTabletMode(boolean tabletMode) {
        this.tabletMode = tabletMode;
        adapter.setTabletMode(tabletMode);
        TabletModeCommunicator communicator = (TabletModeCommunicator) getContext();
        adapter.setCommunicator(communicator);
    }

    public HomeFragment() {
        adapter = new PostsPagerAdapter(
                postsList,
                getActivity(),
                tabletMode
        );
    }

    public void setPostsList(List<Post> postsList, Context context) {
        this.postsList = postsList;
        adapter.setPosts(postsList,context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);

        postsPager = (ViewPager) view.findViewById(R.id.home_pager);
        postsPager.setAdapter(adapter);

        MobileAds.initialize(getContext());

        adView = (AdView) view.findViewById(R.id.adview);


        adView.loadAd(
                new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //to test it on emulator
                    .build()
        );

        if (savedInstanceState != null)
        {
            postsList = savedInstanceState.getParcelableArrayList(POSTS_SAVE_INSTANCE_BUNDLE_KEY);
            tabletMode = savedInstanceState.getBoolean(TABLET_SAVE_INSTANCE_BUNDLE_KEY);
            adapter.setPosts(postsList,getContext());
            adapter.setTabletMode(tabletMode);
            setPosition(savedInstanceState.getInt(POSITION_SAVE_INSTANCE_BUNDLE_KEY));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(POSTS_SAVE_INSTANCE_BUNDLE_KEY, (ArrayList<? extends Parcelable>) postsList);
        outState.putInt(POSITION_SAVE_INSTANCE_BUNDLE_KEY,getPosition());
        outState.putBoolean(TABLET_SAVE_INSTANCE_BUNDLE_KEY,tabletMode);

        super.onSaveInstanceState(outState);
    }


    int getPosition()
    {
        return postsPager.getCurrentItem();
    }

    void setPosition(int position)
    {
        this.position = position;
        if (postsPager != null)
            postsPager.setCurrentItem(position);
    }

}
