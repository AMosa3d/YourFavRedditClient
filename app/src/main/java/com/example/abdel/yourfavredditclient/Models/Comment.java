package com.example.abdel.yourfavredditclient.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abdel on 2/26/2018.
 */

public class Comment implements Parcelable {

    String id;
    String author;
    String body;
    String fullname;

    public Comment(String id, String author, String body, String fullname) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getFullname() {
        return fullname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Comment(Parcel p)
    {
        id = p.readString();
        author = p.readString();
        body = p.readString();
        fullname = p.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(body);
        dest.writeString(fullname);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
