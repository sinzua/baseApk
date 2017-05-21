package com.hw.entities;

public class LoginUser {
    private String fbuid;
    private String full_name;
    private boolean has_anonymous_profile_picture;
    private boolean is_private;
    private String pk;
    private String profile_pic_url;
    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isHas_anonymous_profile_picture() {
        return this.has_anonymous_profile_picture;
    }

    public void setHas_anonymous_profile_picture(boolean has_anonymous_profile_picture) {
        this.has_anonymous_profile_picture = has_anonymous_profile_picture;
    }

    public String getProfile_pic_url() {
        return this.profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPk() {
        return this.pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getFbuid() {
        return this.fbuid;
    }

    public void setFbuid(String fbuid) {
        this.fbuid = fbuid;
    }

    public boolean is_private() {
        return this.is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }
}
