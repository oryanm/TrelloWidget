package com.oryanmat.trellowidget.model;

public class BoardList {
    public static final String NULL_JSON = "{\"id\":\"-1\",\"name\":\"oops\",\"cards\":[]}";

    public String id;
    public String name;

    @Override
    public String toString() {
        return name;
    }
}

