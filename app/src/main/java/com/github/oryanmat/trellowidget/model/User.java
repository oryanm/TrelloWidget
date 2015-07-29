package com.github.oryanmat.trellowidget.model;

public class User {
    String id;
    String fullName;
    String username;

    @Override
    public String toString() {
        return fullName + "@" + username;
    }
}
