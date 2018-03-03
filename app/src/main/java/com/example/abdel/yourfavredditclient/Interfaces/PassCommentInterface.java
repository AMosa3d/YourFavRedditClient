package com.example.abdel.yourfavredditclient.Interfaces;

import com.example.abdel.yourfavredditclient.Models.Comment;

import java.util.List;

/**
 * Created by abdel on 3/2/2018.
 */

public interface PassCommentInterface {
    void passCommentsToAdapter(List<Comment> comments);
}
