package com.ty.entities;

import java.util.ArrayList;

public class PostItem {
    private Comment caption;
    private boolean caption_is_edited;
    private String client_cache_key;
    private String code;
    private int comment_count;
    private ArrayList<Comment> comments;
    private long device_timestamp;
    private int filter_type;
    private boolean has_liked;
    private boolean has_more_comments;
    private String id;
    private ArrayList<ImageVersion> image_versions;
    private int like_count;
    private int max_num_visible_preview_comments;
    private int media_type;
    private String organic_tracking_token;
    private boolean photo_of_you;
    private long pk;
    private long taken_at;
    private LoginUser user;
    private ArrayList<ImageVersion> video_versions;
    private double view_count;

    public long getTaken_at() {
        return this.taken_at;
    }

    public void setTaken_at(long taken_at) {
        this.taken_at = taken_at;
    }

    public long getPk() {
        return this.pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDevice_timestamp() {
        return this.device_timestamp;
    }

    public void setDevice_timestamp(long device_timestamp) {
        this.device_timestamp = device_timestamp;
    }

    public int getMedia_type() {
        return this.media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient_cache_key() {
        return this.client_cache_key;
    }

    public void setClient_cache_key(String client_cache_key) {
        this.client_cache_key = client_cache_key;
    }

    public int getFilter_type() {
        return this.filter_type;
    }

    public void setFilter_type(int filter_type) {
        this.filter_type = filter_type;
    }

    public ArrayList<ImageVersion> getImage_versions() {
        return this.image_versions;
    }

    public void setImage_versions(ArrayList<ImageVersion> image_versions) {
        this.image_versions = image_versions;
    }

    public ArrayList<ImageVersion> getVideo_versions() {
        return this.video_versions;
    }

    public void setVideo_versions(ArrayList<ImageVersion> video_versions) {
        this.video_versions = video_versions;
    }

    public LoginUser getUser() {
        return this.user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    public String getOrganic_tracking_token() {
        return this.organic_tracking_token;
    }

    public void setOrganic_tracking_token(String organic_tracking_token) {
        this.organic_tracking_token = organic_tracking_token;
    }

    public int getLike_count() {
        return this.like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public boolean isHas_liked() {
        return this.has_liked;
    }

    public void setHas_liked(boolean has_liked) {
        this.has_liked = has_liked;
    }

    public boolean isHas_more_comments() {
        return this.has_more_comments;
    }

    public void setHas_more_comments(boolean has_more_comments) {
        this.has_more_comments = has_more_comments;
    }

    public int getMax_num_visible_preview_comments() {
        return this.max_num_visible_preview_comments;
    }

    public void setMax_num_visible_preview_comments(int max_num_visible_preview_comments) {
        this.max_num_visible_preview_comments = max_num_visible_preview_comments;
    }

    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getComment_count() {
        return this.comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public Comment getCaption() {
        return this.caption;
    }

    public void setCaption(Comment caption) {
        this.caption = caption;
    }

    public boolean isCaption_is_edited() {
        return this.caption_is_edited;
    }

    public void setCaption_is_edited(boolean caption_is_edited) {
        this.caption_is_edited = caption_is_edited;
    }

    public boolean isPhoto_of_you() {
        return this.photo_of_you;
    }

    public void setPhoto_of_you(boolean photo_of_you) {
        this.photo_of_you = photo_of_you;
    }

    public double getView_count() {
        return this.view_count;
    }

    public void setView_count(double view_count) {
        this.view_count = view_count;
    }
}
