package com.github.oryanmat.trellowidget.model;

public class Board {
    public String id;
    public String name;
    public BoardList[] lists;

    @Override
    public String toString() {
        return name;
    }
}
