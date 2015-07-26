package com.oryanmat.trellowidget.model;

public class ListArray {
    public String id;
    public String name;
    public BoardList[] lists;

    public static ListArray oneItemList(String name) {
        ListArray array = new ListArray();
        BoardList list = new BoardList();
        list.name = name;
        array.lists = new BoardList[] {list};
        return array;
    }
}
