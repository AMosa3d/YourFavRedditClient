package com.example.abdel.yourfavredditclient.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.abdel.yourfavredditclient.BuildConfig;
import com.example.abdel.yourfavredditclient.Deserializers.CommentDeserializer;
import com.example.abdel.yourfavredditclient.Deserializers.PostDeserializer;
import com.example.abdel.yourfavredditclient.Deserializers.SubredditDeserializer;
import com.example.abdel.yourfavredditclient.Interfaces.PassCommentInterface;
import com.example.abdel.yourfavredditclient.Interfaces.PassPostsInterface;
import com.example.abdel.yourfavredditclient.Interfaces.PassSubredditsInterface;
import com.example.abdel.yourfavredditclient.Models.Account;
import com.example.abdel.yourfavredditclient.Models.Comment;
import com.example.abdel.yourfavredditclient.Models.OAuthAccessToken;
import com.example.abdel.yourfavredditclient.Models.Post;
import com.example.abdel.yourfavredditclient.Models.PostCommentResponse;
import com.example.abdel.yourfavredditclient.Models.Subreddit;
import com.example.abdel.yourfavredditclient.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by abdel on 2/21/2018.
 */

public final class NetworkUtils {

    final static String CLIENT_ID = BuildConfig.CLIENT_ID;

    final static String AUTH_BASE_URL = "https://www.reddit.com/api/v1/authorize?";

    final static String REDIRECT_URI_KEY = "redirect_uri";
    final static String CLIENT_ID_KEY = "client_id";
    final static String RESPONSE_TYPE_KEY = "response_type";
    final static String STATE_KEY = "state";
    final static String DURATION_KEY = "duration";
    final static String SCOPE_KEY = "scope";
    final static String AUTH_CODE_KEY = "authorization_code";
    final static String REFRESH_CODE_KEY = "refresh_token";

    final static String REDIRECT_URI = "redirecturi://relaunchapp";
    final static String RESPONSE_TYPE = "code";
    final static String STATE = "View";
    final static String DURATION = "permanent";
    final static String SCOPE = "identity read submit mysubreddits";

    final static String CLIENT_SECRET = "";

    final static String REDDIT_BASE_URL = "https://www.reddit.com";
    final static String REFRESH_BASE_URL = "https://www.reddit.com";
    final static String OAUTH_BASE_URL = "https://oauth.reddit.com";
    final static String BEARER = "bearer ";

    public static Uri buildAuthURI()
    {
        return Uri.parse(AUTH_BASE_URL).buildUpon()
                .appendQueryParameter(CLIENT_ID_KEY,CLIENT_ID)
                .appendQueryParameter(RESPONSE_TYPE_KEY,RESPONSE_TYPE)
                .appendQueryParameter(STATE_KEY,STATE)
                .appendQueryParameter(REDIRECT_URI_KEY,REDIRECT_URI)
                .appendQueryParameter(DURATION_KEY,DURATION)
                .appendQueryParameter(SCOPE_KEY,SCOPE)
                .build();
    }

    static Retrofit buildRetrofitObject(String baseUrl, OkHttpClient client, GsonConverterFactory gsonConverterFactory)
    {
        Retrofit.Builder retrofitBuilder =new Retrofit.Builder().addConverterFactory(gsonConverterFactory)
                .baseUrl(baseUrl);
        if (client != null)
            retrofitBuilder.client(client);

        return retrofitBuilder.build();
    }

    static Request buildAuthHeader(Response response)
    {
        String authString = Credentials.basic(CLIENT_ID,CLIENT_SECRET);

        return response.request()
                .newBuilder()
                .addHeader("Authorization",authString)
                .build();
    }



    public static void getComments(final Context context, final PassCommentInterface commentInterface, final String token, final String subreddit, final String postId)
    {
        Gson customGSON = new GsonBuilder()
                .registerTypeAdapter(List.class,new CommentDeserializer())
                .create();

        Retrofit retrofit = buildRetrofitObject(OAUTH_BASE_URL,null, GsonConverterFactory.create(customGSON));

        RedditAPIManager api = retrofit.create(RedditAPIManager.class);

        Call<List<Comment>> commentCall = api.getComments(BEARER + token, subreddit, postId);

        commentCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, retrofit2.Response<List<Comment>> response) {
                //TODO: Retrive data and build the list
                if (response.body() != null)
                {
                    commentInterface.passCommentsToAdapter(response.body());
                }
                else
                    Toast.makeText(context,"Comments Data Retrieval Failed!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(context,"Comments Data Retrieval Failed!",Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void getSubreddits(final Context context, final PassSubredditsInterface subredditsInterface, final String token)
    {
        Gson customGSON = new GsonBuilder()
                .registerTypeAdapter(List.class,new SubredditDeserializer())
                .create();

        Retrofit retrofit = buildRetrofitObject(OAUTH_BASE_URL,null,GsonConverterFactory.create(customGSON));

        RedditAPIManager api = retrofit.create(RedditAPIManager.class);

        Call<List<Subreddit>> subredditCall = api.getMySubreddits(BEARER + token);

        subredditCall.enqueue(new Callback<List<Subreddit>>() {
            @Override
            public void onResponse(Call<List<Subreddit>> call, retrofit2.Response<List<Subreddit>> response) {
                //TODO: Retrive data and build the list
                if (response.body() != null)
                {
                    subredditsInterface.passSubreddits(response.body());
                }
                else
                    Toast.makeText(context,"Subreddits Data Retrieval Failed!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Subreddit>> call, Throwable t) {
                Toast.makeText(context,"Subreddits Data Retrieval Failed!",Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void getPosts(final Context context, final PassPostsInterface postsInterface, String token)
    {
        Gson customGSON = new GsonBuilder()
                .registerTypeAdapter(List.class,new PostDeserializer())
                .setLenient()
                .create();

        Retrofit retrofit = buildRetrofitObject(OAUTH_BASE_URL,null, GsonConverterFactory.create(customGSON));

        RedditAPIManager api = retrofit.create(RedditAPIManager.class);

        Call<List<Post>> postCall = api.getBest(NetworkUtils.BEARER + token);

        postCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {
                //TODO: Retrive data and build the list
                if (response.body() != null)
                {
                    postsInterface.PassPostsThrough(response.body());
                }
                else
                    refreshAccessToken(context,postsInterface);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(context,"Data Retrieval Failed!",Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void getAuthToken(final Context context, final PassPostsInterface postsInterface, String code)
    {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                return buildAuthHeader(response);
            }
        });

        OkHttpClient client = httpClientBuilder.build();

        Retrofit retrofit = buildRetrofitObject(REDDIT_BASE_URL,client,GsonConverterFactory.create());

        RedditAPIManager api = retrofit.create(RedditAPIManager.class);

        Call<OAuthAccessToken> accessTokenCall = api.getAccessToken(
                CLIENT_ID,
                CLIENT_SECRET,
                NetworkUtils.AUTH_CODE_KEY,
                code,
                REDIRECT_URI
        );

        accessTokenCall.enqueue(new Callback<OAuthAccessToken>() {
            @Override
            public void onResponse(Call<OAuthAccessToken> call, retrofit2.Response<OAuthAccessToken> response) {
                //TODO:Fill the token
                if (response.body() == null)
                {
                    Toast.makeText(context,"Authorization Failed!",Toast.LENGTH_LONG).show();
                    return;
                }
                postsInterface.SaveToken(response.body());
                getPosts(context,postsInterface,response.body().getAccessToken());
                getAccount(context,postsInterface,response.body().getAccessToken());
            }

            @Override
            public void onFailure(Call<OAuthAccessToken> call, Throwable t) {
                Toast.makeText(context,"Authorization Failed!",Toast.LENGTH_LONG).show();
            }

        });
    }

    static void refreshAccessToken(final Context context, final PassPostsInterface postsInterface)
    {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                return NetworkUtils.buildAuthHeader(response);
            }
        });

        OkHttpClient client = httpClientBuilder.build();

        Retrofit retrofit = NetworkUtils.buildRetrofitObject(REDDIT_BASE_URL,client,GsonConverterFactory.create());

        RedditAPIManager api = retrofit.create(RedditAPIManager.class);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        Call<OAuthAccessToken> accessTokenCall = api.refreshAccessToken(
                CLIENT_ID,
                CLIENT_SECRET,
                REFRESH_CODE_KEY,
                preferences.getString(context.getString(R.string.refresh_token_pref_key),"")
        );

        accessTokenCall.enqueue(new Callback<OAuthAccessToken>() {
            @Override
            public void onResponse(Call<OAuthAccessToken> call, retrofit2.Response<OAuthAccessToken> response) {
                //TODO:Fill the token
                if (response.body() == null)
                {
                    Toast.makeText(context,"Authorization Failed!",Toast.LENGTH_LONG).show();
                    return;
                }
                postsInterface.SaveToken(response.body());
                getPosts(context,postsInterface,response.body().getAccessToken());
                getAccount(context,postsInterface,response.body().getAccessToken());
            }

            @Override
            public void onFailure(Call<OAuthAccessToken> call, Throwable t) {
                Toast.makeText(context,"Authorization Failed!",Toast.LENGTH_LONG).show();
            }

        });
    }

    public static void getAccount(final Context context, final PassPostsInterface postsInterface, final String token)
    {
        Retrofit retrofit = buildRetrofitObject(OAUTH_BASE_URL,null,GsonConverterFactory.create());

        RedditAPIManager api = retrofit.create(RedditAPIManager.class);

        Call<Account> accountCall = api.getAccount(BEARER + token);

        accountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, retrofit2.Response<Account> response) {
                //TODO: Retrive data and build the list
                if (response.body() != null)
                {
                    postsInterface.PassAccountThrough(response.body());
                }
                else
                    Toast.makeText(context,"Account Data Retrieval Failed!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(context,"Account Data Retrieval Failed!",Toast.LENGTH_LONG).show();
            }
        });


    }

    public static void postComment(final Context context, String token, String body, String parent)
    {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                return buildAuthHeader(response);
            }
        });

        OkHttpClient client = httpClientBuilder.build();

        Retrofit retrofit = buildRetrofitObject(OAUTH_BASE_URL,client,GsonConverterFactory.create());

        RedditAPIManager api = retrofit.create(RedditAPIManager.class);

        Call<PostCommentResponse> postCommentCall = api.postComment(NetworkUtils.BEARER + token,parent,body);

        postCommentCall.enqueue(new Callback<PostCommentResponse>() {
            @Override
            public void onResponse(Call<PostCommentResponse> call, retrofit2.Response<PostCommentResponse> response) {
                //TODO:Fill the token
                if (response.body() != null && response.isSuccessful())
                {
                    Toast.makeText(context,"Your Comment Has Been Posted!",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context,"Sorry Cannot post your comment please try again later",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<PostCommentResponse> call, Throwable t) {
                Toast.makeText(context,"Sorry Cannot post your comment please try again later",Toast.LENGTH_LONG).show();
            }

        });
    }
}
