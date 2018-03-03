package com.example.abdel.yourfavredditclient.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abdel on 2/21/2018.
 */

public class Post implements Parcelable {

    String id;
    String title;
    String thumbnail;
    String subreddit;
    String author;
    String description;
    String domain;
    String ups;
    String url;
    String fullname;


    public Post(String id, String title, String thumbnail, String subreddit, String author, String description, String domain, String ups, String url, String fullname) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.subreddit = subreddit;
        this.author = author;
        this.description = description;
        this.domain = domain;
        this.ups = ups;
        this.url = url;
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Post(Parcel p)
    {
        this.id = p.readString();
        this.title = p.readString();
        this.thumbnail = p.readString();
        this.subreddit = p.readString();
        this.author = p.readString();
        this.description = p.readString();
        this.domain = p.readString();
        this.ups = p.readString();
        this.url = p.readString();
        this.fullname = p.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeString(subreddit);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(domain);
        dest.writeString(ups);
        dest.writeString(url);
        dest.writeString(fullname);
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
