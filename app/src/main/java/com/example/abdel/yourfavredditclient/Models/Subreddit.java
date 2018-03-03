package com.example.abdel.yourfavredditclient.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abdel on 2/28/2018.
 */

public class Subreddit implements Parcelable {

    String id;
    String name;
    Boolean isStared;

    public Subreddit(String id, String name, boolean isStared) {
        this.id = id;
        this.name = name;
        this.isStared = isStared;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStared() {
        return isStared;
    }

    public void setStared(Boolean stared) {
        isStared = stared;
    }

    public static final Creator<Subreddit> CREATOR = new Creator<Subreddit>() {
        @Override
        public Subreddit createFromParcel(Parcel source) {
            return new Subreddit(source);
        }

        @Override
        public Subreddit[] newArray(int size) {
            return new Subreddit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Subreddit(Parcel p)
    {
        id = p.readString();
        name = p.readString();
        int bool = p.readInt();
        if (bool == 1)
            isStared = true;
        else
            isStared = false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        if (isStared)
            dest.writeInt(1);
        else
            dest.writeInt(0);
    }
}
