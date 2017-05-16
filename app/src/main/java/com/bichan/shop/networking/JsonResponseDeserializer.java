package com.bichan.shop.networking;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by cuong on 5/16/2017.
 */

public class JsonResponseDeserializer <T> implements JsonDeserializer<T>{

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement content = json.getAsJsonObject().get("result");
        return new Gson().fromJson(content, typeOfT);
    }
}
