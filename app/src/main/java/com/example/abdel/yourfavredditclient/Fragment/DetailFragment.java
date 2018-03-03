package com.example.abdel.yourfavredditclient.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abdel.yourfavredditclient.Adapters.CommentsAdapter;
import com.example.abdel.yourfavredditclient.Interfaces.PassCommentInterface;
import com.example.abdel.yourfavredditclient.Models.Comment;
import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.R;
import com.example.abdel.yourfavredditclient.Utils.LogicUtils;
import com.example.abdel.yourfavredditclient.Utils.NetworkUtils;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;


/*
 * Copyright (C) 2013 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by abdel on 2/22/2018.
 */

public class DetailFragment extends Fragment implements PassCommentInterface {

    final String SHARE_TYPE = "text/plain";
    final String SHARE_TITLE = "Share With";
    final String ADD_COMMENT_TITLE = "Add Comment";
    Post post;
    CommentsAdapter adapter;
    NestedScrollView nestedScrollView;
    List<Comment> comments;
    private static final String POSITION_SAVE_INSTANCE_BUNDLE_KEY = "current_item_position";
    private static final String COMMENTS_SAVE_INSTANCE_BUNDLE_KEY = "comments_list";
    private static final String POST_SAVE_INSTANCE_BUNDLE_KEY = "post";

    public DetailFragment() {
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment,container,false);

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.post_nestedScrollView);
        adapter = new CommentsAdapter(getContext(),getActivity());

        if (savedInstanceState != null)
        {
            comments = savedInstanceState.getParcelableArrayList(COMMENTS_SAVE_INSTANCE_BUNDLE_KEY);
            post = savedInstanceState.getParcelable(POST_SAVE_INSTANCE_BUNDLE_KEY);
            nestedScrollView.setVerticalScrollbarPosition(savedInstanceState.getInt(POSITION_SAVE_INSTANCE_BUNDLE_KEY));
        }
        else
            try {
                NetworkUtils.getComments(getContext(),
                        this,
                        PreferenceManager.getDefaultSharedPreferences(getContext())
                                .getString(getString(R.string.access_token_pref_key), ""),
                        post.getSubreddit(),
                        post.getId()
                );
            }
            catch (Exception e)
            {
                FirebaseCrash.report(e);
            }

            try {
                setDataToUI(view);
            }
            catch (Exception e)
            {
                FirebaseCrash.report(e);
            }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(COMMENTS_SAVE_INSTANCE_BUNDLE_KEY, (ArrayList<? extends Parcelable>) comments);
        outState.putParcelable(POST_SAVE_INSTANCE_BUNDLE_KEY,post);
        outState.putInt(POSITION_SAVE_INSTANCE_BUNDLE_KEY,nestedScrollView.getVerticalScrollbarPosition());

        super.onSaveInstanceState(outState);
    }

    void setDataToUI(View view) {
        ImageView thumbnailIV;
        TextView titleTV,authorTV,descriptionTV;
        Button commentBtn;
        RecyclerView commentsRV;
        final FloatingActionButton shareFab;

        thumbnailIV = (ImageView) view.findViewById(R.id.thumbnail_detail_imageView);
        titleTV = (TextView) view.findViewById(R.id.title_detail_textView);
        authorTV = (TextView) view.findViewById(R.id.author_detail_textView);
        descriptionTV = (TextView) view.findViewById(R.id.description_detail_textView);
        commentBtn = (Button) view.findViewById(R.id.comment_button);
        commentsRV = (RecyclerView) view.findViewById(R.id.comments_recyclerView);
        shareFab = (FloatingActionButton) view.findViewById(R.id.fab_detail);

        Glide.with(getContext()).load(post.getThumbnail()).into(thumbnailIV);

        thumbnailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post.getDomain().startsWith("self"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getUrl()));
                    startActivity(intent);
                }
            }
        });

        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType(SHARE_TYPE);
                shareIntent.putExtra(Intent.EXTRA_TITLE,post.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT,post.getUrl());

                startActivity(Intent.createChooser(shareIntent,SHARE_TITLE));
            }
        });

        titleTV.setText(post.getTitle());
        authorTV.setText(post.getAuthor());
        descriptionTV.setText(post.getDescription());
        descriptionTV.setMovementMethod(LinkMovementMethod.getInstance());

        commentsRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapter.setComments(comments);
        commentsRV.setAdapter(adapter);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.access_token_pref_key), "");
                    View customDialogView = getActivity().getLayoutInflater().inflate(R.layout.post_comment_dialog, null);
                    LogicUtils.makeCommentDialog(getContext(), customDialogView, ADD_COMMENT_TITLE, token, post.getFullname());
                }
                catch (Exception e)
                {
                    FirebaseCrash.report(e);
                }
            }
        });
    }


    @Override
    public void passCommentsToAdapter(List<Comment> comments) {
        this.comments = comments;
        adapter.setComments(comments);
    }
}
