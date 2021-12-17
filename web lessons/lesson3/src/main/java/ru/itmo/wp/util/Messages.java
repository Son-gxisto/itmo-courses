package ru.itmo.wp.util;


import java.util.ArrayList;
import java.util.List;

public class Messages
{
    private List<Pair> msgList;
    public Messages() {
        msgList = new ArrayList<>();
    }
    public void add(String user, String message) {
        msgList.add(new Pair(user, message));
    }
    public List<Pair> getMsgList() {
        return msgList;
    }
}
