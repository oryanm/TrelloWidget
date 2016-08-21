package com.github.oryanmat.trellowidget.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class BoardList {
    public static final Type BOARD_LIST_TYPE = new TypeToken<List<Board>>() {}.getType();
    public static final String NULL_JSON = "{\"id\":\"-1\",\"name\":\"oops\",\"cards\":[]}";
    public static final String ERROR = "ERROR";

    public String id;
    public String name;
    public List<Card> cards;

    public static BoardList oneItemList(String name) {
        Card card = new Card();
        card.name = name;
        card.badges = new Badges();
        BoardList list = new BoardList();
        list.id = ERROR;
        list.cards = Collections.singletonList(card);
        return list;
    }

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

