package com.example.abdel.yourfavredditclient.Deserializers;

import com.example.abdel.yourfavredditclient.Models.Comment;
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
 * Created by abdel on 2/26/2018.
 */

public class CommentDeserializer implements JsonDeserializer<List<Comment>> {

    final String ARRAY_KEY = "children";

    final String DATA_KEY = "data";
    final String ID_KEY = "id";
    final String AUTHOR_KEY = "author";
    final String BODY_KEY = "body";
    final String FULLNAME_KEY = "name";

    @Override
    public List<Comment> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonArray response = json.getAsJsonArray();

        //Note get(1) here to skip the first object which is the actual post
        JsonArray commentsArray = response.get(1).getAsJsonObject().get(DATA_KEY).getAsJsonObject().get(ARRAY_KEY).getAsJsonArray();

        List<Comment> result = new ArrayList<>();

        for (int i=0;i < commentsArray.size();i++)
        {
            JsonObject currentComment = commentsArray.get(i).getAsJsonObject().get(DATA_KEY).getAsJsonObject();

            if (currentComment == null || currentComment.get(ID_KEY) == null
                    || currentComment.get(AUTHOR_KEY) == null
                    || currentComment.get(BODY_KEY) == null
                    || currentComment.get(FULLNAME_KEY) == null)
                continue;



            result.add(new Comment(
                    currentComment.get(ID_KEY).getAsString(),
                    currentComment.get(AUTHOR_KEY).getAsString(),
                    currentComment.get(BODY_KEY).getAsString(),
                    currentComment.get(FULLNAME_KEY).getAsString()
            ));
        }



        return result;
    }
}
