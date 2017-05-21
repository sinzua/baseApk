package com.ty.entities;

public class Tag {
    private long id;
    private int media_count;
    private String name;

    public int getMedia_count() {
        return this.media_count;
    }

    public void setMedia_count(int media_count) {
        this.media_count = media_count;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
