package com.github.oryanmat.trellowidget.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Json {
    static Gson gson = new Gson();

    private Json() {
    }

    public static Gson get() {
        if (gson == null) {
            gson = new Gson();
        }

        return gson;
    }

    public static <T> T tryParseJson(String json, Class<T> c, T defaultValue) {
        try {
            return gson.fromJson(json, c);
        } catch (JsonSyntaxException e) {
            return defaultValue;
        }
    }
}
