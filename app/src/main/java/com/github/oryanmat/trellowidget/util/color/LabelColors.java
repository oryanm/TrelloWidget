package com.github.oryanmat.trellowidget.util.color;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class LabelColors {
    public static final String PINK = "#FF80CE";
    public static final String ORANGE = "#FFAB4A";
    public static final String LIME = "#51E898";
    public static final String SKY = "#00C2E0";
    public static final String BLUE = "#0079BF";
    public static final String GREEN = "#61BD4F";
    public static final String YELLOW = "#F2D600";
    public static final String RED = "#EB5A46";
    public static final String PURPLE = "#C377E0";
    public static final String LIGHT_GREY = "#C4C9CC";

    public static Map<String, Integer> colors = new Builder<String, Integer>()
            .put("black", Color.BLACK)
            .put("purple", Color.parseColor(PURPLE))
            .put("pink", Color.parseColor(PINK))
            .put("red", Color.parseColor(RED))
            .put("orange", Color.parseColor(ORANGE))
            .put("yellow", Color.parseColor(YELLOW))
            .put("green", Color.parseColor(GREEN))
            .put("lime", Color.parseColor(LIME))
            .put("sky", Color.parseColor(SKY))
            .put("blue", Color.parseColor(BLUE))
            .put(null, Color.parseColor(LIGHT_GREY))
            .build();

    public static class Builder<K, V> {
        Map<K, V> map = new HashMap<>();

        public Builder() {
        }

        public Builder<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return map;
        }
    }
}
