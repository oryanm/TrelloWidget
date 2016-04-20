package com.github.oryanmat.trellowidget.model;

import android.net.Uri;

public class Board {
    public static final String NULL_JSON = "{\"id\":\"-1\",\"name\":\"oops\",\"url\":\"\",\"lists\":[]}";
    public String id;
    public String name;
    public String url;
    public BoardList[] lists;

    @Override
    public String toString() {
        return name;
    }

    public Uri uri() { return Uri.parse(url); }
}
