package com.github.oryanmat.trellowidget.model;

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

    @Override
    public boolean equals(Object that) {
        if (that instanceof Board) {
            return this.id.equals(((Board) that).id);
        }
        return super.equals(that);
    }
}
