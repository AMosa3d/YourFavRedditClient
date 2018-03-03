package com.example.abdel.yourfavredditclient.Adapters;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.abdel.yourfavredditclient.Models.Comment;
import com.example.abdel.yourfavredditclient.R;
import com.example.abdel.yourfavredditclient.Utils.LogicUtils;

import java.util.List;

/**
 * Created by abdel on 2/28/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    List<Comment> comments;
    Context context;
    Activity activity;

    public CommentsAdapter(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        return new CommentsViewHolder(
                inflater.inflate(R.layout.comment_single_item,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment currentComment = comments.get(position);
        holder.bind(currentComment.getAuthor(),currentComment.getBody(),currentComment.getFullname());
    }

    @Override
    public int getItemCount() {
        if (comments == null || comments.size() == 0)
            return 0;
        return comments.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        final String REPLY_TITLE = "Reply";
        TextView commentAuthor,commentBody;
        Button replyBtn;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            commentAuthor = (TextView) itemView.findViewById(R.id.author_comment_textView);
            commentBody = (TextView) itemView.findViewById(R.id.body_comment_textView);
            replyBtn = (Button) itemView.findViewById(R.id.reply_button);


        }

        void bind(String authorName, String body, final String fullname)
        {
            commentAuthor.setText(authorName);
            commentBody.setText(body);

            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String token = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.access_token_pref_key),"");
                    View customDialogView = activity.getLayoutInflater().inflate(R.layout.post_comment_dialog,null);
                    LogicUtils.makeCommentDialog(context,customDialogView,REPLY_TITLE,token,fullname);
                }
            });
        }
    }
}
