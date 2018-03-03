package com.example.abdel.yourfavredditclient.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdel on 2/21/2018.
 */

public class OAuthAccessToken {

    //Serialize name attr. is used to match this element in the json to this attr. in the class
    //can be neglected if the string in the class has the same name , but it's better to stay with naming conventions

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("refresh_token")
    String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
