package ru.itmo.web.lesson4.model;

import ru.itmo.web.lesson4.util.DataUtil;

public class User {
    private final long id;
    private final String handle;
    private final String name;
    private final DataUtil.Color color;
    public User(long id, String handle, String name, DataUtil.Color color) {
        this.id = id;
        this.handle = handle;
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }

    public DataUtil.Color getColor() { return color; }
}
