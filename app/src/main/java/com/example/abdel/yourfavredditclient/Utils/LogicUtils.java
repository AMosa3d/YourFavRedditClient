package com.example.abdel.yourfavredditclient.Utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.Models.Subreddit;
import com.example.abdel.yourfavredditclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdel on 2/28/2018.
 */

public final class LogicUtils {

    public static List<Subreddit> reorderStaredList(List<Subreddit> mySubscription, List<String> staredSubredditNames)
    {
        List<Subreddit> result = new ArrayList<>();

        for (int i=0;i<staredSubredditNames.size();i++)
        {
            for (Subreddit currentSubreddit: mySubscription)
            {
                if (currentSubreddit.getName().equals(staredSubredditNames.get(i)))
                {
                    currentSubreddit.setStared(true);
                    result.add(currentSubreddit);
                    mySubscription.remove(currentSubreddit);
                    break;
                }
            }
        }
        for (Subreddit currentSubreddit: mySubscription)
        {
            currentSubreddit.setStared(false);
            result.add(currentSubreddit);
        }
        return result;
    }

    public static List<Post> filterByFavSubreddits(List<Post> posts, List<String> staredSubredditNames) {

        if (staredSubredditNames.size() == 0)
            return posts;


        List<Post> result = new ArrayList<>();


        for (Post currentPost : posts) {
            for (int i = 0; i < staredSubredditNames.size(); i++) {
                if(staredSubredditNames.get(i).equals(currentPost.getSubreddit())){
                    result.add(currentPost);
                    break;
                }
            }
        }

        return result;
    }

    public static void makeCommentDialog(final Context context,View customDialogView,String title, final String token, final String parent)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText commentBodyEditText;
        TextView dialogTitleTextView;
        Button addCommentButton;
        View addCommentView = customDialogView;

        commentBodyEditText = (EditText) addCommentView.findViewById(R.id.post_comment_editText);
        dialogTitleTextView = (TextView) addCommentView.findViewById(R.id.post_comment_title_textView);
        addCommentButton = (Button) addCommentView.findViewById(R.id.post_comment_button);

        builder.setView(addCommentView);
        dialogTitleTextView.setText(title);

        final Dialog dialog = builder.create();

        dialog.show();

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkUtils.postComment(context,token,commentBodyEditText.getText().toString(),parent);

                dialog.dismiss();
            }
        });

    }

}
