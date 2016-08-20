package com.github.oryanmat.trellowidget.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BoardList {
    public static final Type BOARD_LIST_TYPE = new TypeToken<List<Board>>() {}.getType();
    public static final String NULL_JSON = "{\"id\":\"-1\",\"name\":\"oops\",\"cards\":[]}";

    public String id;
    public String name;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardList boardList = (BoardList) o;
        return id.equals(boardList.id);
    }
}

