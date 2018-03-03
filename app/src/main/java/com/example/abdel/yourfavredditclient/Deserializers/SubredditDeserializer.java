package com.example.abdel.yourfavredditclient.Deserializers;

import com.example.abdel.yourfavredditclient.Models.Subreddit;
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
 * Created by abdel on 2/28/2018.
 */

public class SubredditDeserializer implements JsonDeserializer<List<Subreddit>> {

    final String ARRAY_KEY = "children";

    final String DATA_KEY = "data";
    final String ID_KEY = "id";
    final String NAME_KEY = "display_name";

    @Override
    public List<Subreddit> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject response = json.getAsJsonObject();

        JsonArray postsArray = response.get(DATA_KEY).getAsJsonObject().get(ARRAY_KEY).getAsJsonArray();

        List<Subreddit> result = new ArrayList<>();

        for (int i=0;i < postsArray.size();i++)
        {
            JsonObject currentSubreddit = postsArray.get(i).getAsJsonObject().get(DATA_KEY).getAsJsonObject();

            result.add(new Subreddit(
                    currentSubreddit.get(ID_KEY).getAsString(),
                    currentSubreddit.get(NAME_KEY).getAsString(),
                    false
            ));
        }



        return result;
    }
}
