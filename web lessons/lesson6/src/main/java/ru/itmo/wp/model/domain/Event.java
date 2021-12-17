package ru.itmo.wp.model.domain;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    private long id;
    private long userId;
    private Date creationTime;
    private Enum type;
    public static enum EventType {
        ENTER,
        LOGOUT
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Enum getEventType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setEventType(Enum type) {
        this.type = type;
    }
}
