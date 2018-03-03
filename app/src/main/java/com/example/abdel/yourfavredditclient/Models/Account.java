package com.example.abdel.yourfavredditclient.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdel on 2/28/2018.
 */

public class Account {
    //Note that there is no need to create custom deserializer here
    //since it can be handled normally and the data are not nested inside the result object

    String id;
    String name;
    @SerializedName("icon_img")
    String avatar;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
