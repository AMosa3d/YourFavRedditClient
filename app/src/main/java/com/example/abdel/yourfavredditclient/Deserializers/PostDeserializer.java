package com.example.abdel.yourfavredditclient.Deserializers;

import com.example.abdel.yourfavredditclient.Models.Post;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdel on 2/24/2018.
 */

public class PostDeserializer implements JsonDeserializer<List<Post>> {

    final String ARRAY_KEY = "children";

    final String DATA_KEY = "data";
    final String ID_KEY = "id";
    final String TITLE_KEY = "title";
    final String DESCRIPTION_KEY = "selftext";
    final String THUMBNAIL_KEY = "thumbnail";
    final String DOMAIN_KEY = "domain";
    final String SUBREDDIT_KEY = "subreddit";
    final String AUTHOR_KEY = "author";
    final String UPS_KEY = "ups";
    final String URL_KEY = "url";
    final String FULLNAME_KEY = "name";

    @Override
    public List<Post> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject response = json.getAsJsonObject();

        JsonArray postsArray = response.get(DATA_KEY).getAsJsonObject().get(ARRAY_KEY).getAsJsonArray();

        List<Post> result = new ArrayList<>();

        for (int i=0;i < postsArray.size();i++)
        {
            JsonObject currentPost = postsArray.get(i).getAsJsonObject().get(DATA_KEY).getAsJsonObject();

            result.add(new Post(
                    currentPost.get(ID_KEY).getAsString(),
                    currentPost.get(TITLE_KEY).getAsString(),
                    currentPost.get(THUMBNAIL_KEY).getAsString(),
                    currentPost.get(SUBREDDIT_KEY).getAsString(),
                    currentPost.get(AUTHOR_KEY).getAsString(),
                    currentPost.get(DESCRIPTION_KEY).getAsString(),
                    currentPost.get(DOMAIN_KEY).getAsString(),
                    currentPost.get(UPS_KEY).getAsString(),
                    currentPost.get(URL_KEY).getAsString(),
                    currentPost.get(FULLNAME_KEY).getAsString()
            ));
        }



        return result;
    }
}
