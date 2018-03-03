package com.example.abdel.yourfavredditclient.Interfaces;

import com.example.abdel.yourfavredditclient.Models.Account;
import com.example.abdel.yourfavredditclient.Models.OAuthAccessToken;
import com.example.abdel.yourfavredditclient.Models.Post;

import java.util.List;

/**
 * Created by abdel on 2/25/2018.
 */

public interface PassPostsInterface {
    void PassPostsThrough(List<Post> posts);
    void PassAccountThrough(Account account);
    void SaveToken(OAuthAccessToken token);
}
