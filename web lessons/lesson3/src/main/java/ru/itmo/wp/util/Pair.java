package ru.itmo.wp.util;

class Pair {
    private String user;
    private String text;
    Pair(String user, String text) {
        this.user = user;
        this.text = text;
    }
    Object getUser() {
        return user;
    }
    Object getText() {
        return text;
    }
}
