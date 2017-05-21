package com.ty.helloworld.entities;

public class Comment {
    private int bit_flags;
    private String content_type;
    private long created_at;
    private long created_at_utc;
    private long media_id;
    private long pk;
    private String status;
    private String text;
    private int type;
    private LoginUser user;
    private long user_id;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getCreated_at_utc() {
        return this.created_at_utc;
    }

    public void setCreated_at_utc(long created_at_utc) {
        this.created_at_utc = created_at_utc;
    }

    public long getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getBit_flags() {
        return this.bit_flags;
    }

    public void setBit_flags(int bit_flags) {
        this.bit_flags = bit_flags;
    }

    public LoginUser getUser() {
        return this.user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    public String getContent_type() {
        return this.content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getMedia_id() {
        return this.media_id;
    }

    public void setMedia_id(long media_id) {
        this.media_id = media_id;
    }

    public long getPk() {
        return this.pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
