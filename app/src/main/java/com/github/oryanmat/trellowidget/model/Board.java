package com.github.oryanmat.trellowidget.model;

import java.util.List;

public class Board {
    public static final String NULL_JSON = "{\"id\":\"-1\",\"name\":\"oops\",\"url\":\"\",\"lists\":[]}";
    public String id;
    public String name;
    public String url;
    public List<BoardList> lists;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id.equals(board.id);
    }
}
