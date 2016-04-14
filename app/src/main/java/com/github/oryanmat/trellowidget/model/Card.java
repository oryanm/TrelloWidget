package com.github.oryanmat.trellowidget.model;

import android.net.Uri;

public class Card {
    public String id;
    public String name;
    public String desc;
    public String due;
    public Badges badges;
    public String url;
    public Label[] labels;
    public Uri uri() { return Uri.parse(url); }
}
