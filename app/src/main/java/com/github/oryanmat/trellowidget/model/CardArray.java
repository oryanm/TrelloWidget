package com.github.oryanmat.trellowidget.model;

public class CardArray {
    public static final String ERROR = "ERROR";

    public String id;
    public String name;
    public Card[] cards;

    public static CardArray oneItemList(String name) {
        CardArray list = new CardArray();
        Card card = new Card();
        card.name = name;
        card.badges = new Badges();
        list.cards = new Card[] {card};
        list.id = ERROR;
        return list;
    }
}
