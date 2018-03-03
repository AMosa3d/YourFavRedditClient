package com.example.abdel.yourfavredditclient.Utils;

import com.example.abdel.yourfavredditclient.Models.Account;
import com.example.abdel.yourfavredditclient.Models.Comment;
import com.example.abdel.yourfavredditclient.Models.PostCommentResponse;
import com.example.abdel.yourfavredditclient.Models.OAuthAccessToken;
import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.Models.Subreddit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by abdel on 2/21/2018.
 */

public interface RedditAPIManager {

    @POST("/api/v1/access_token")
    @FormUrlEncoded
    Call<OAuthAccessToken> getAccessToken(
            @Header("client_id") String clientId,
            @Header("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri
    );

    @POST("/api/v1/access_token")
    @FormUrlEncoded
    Call<OAuthAccessToken> refreshAccessToken(
            @Header("client_id") String clientId,
            @Header("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("refresh_token") String token
    );

    @GET("/best")
    Call<List<Post>> getBest(
            @Header("Authorization") String token
    );

    @GET("/r/{subreddit}/comments/{id}")
    Call<List<Comment>> getComments(
            @Header("Authorization") String token,
            @Path("subreddit") String subreddit,
            @Path("id") String id
    );

    @GET("/subreddits/mine/subscriber")
    Call<List<Subreddit>> getMySubreddits(
            @Header("Authorization") String token
    );

    @GET("/api/v1/me")
    Call<Account> getAccount(
            @Header("Authorization") String token
    );


    @POST("/api/comment")
    @FormUrlEncoded
    Call<PostCommentResponse> postComment(
            @Header("Authorization") String token,
            @Field("parent") String fullname,
            @Field("text") String body
    );
}
