package com.ty.http;

public class RequstURL {
    public static final String FEED = "https://i.instagram.com/api/v1/feed/user/%s/";
    public static final String FOLLOW = "https://i.instagram.com/api/v1/friendships/create/%s/";
    public static final String FOLLOWERS = "https://i.instagram.com/api/v1/friendships/%s/followers/";
    public static final String FOLLOWING = "https://i.instagram.com/api/v1/friendships/%s/following/";
    public static final String LIKE = "https://i.instagram.com/api/v1/media/%s/like/?d=0&src=%s";
    public static final String LOGIN = "https://i.instagram.com/api/v1/accounts/login/";
    public static final String POPULAR = "https://i.instagram.com/api/v1/feed/popular/";
    public static final String SERVER_URL = "https://i.instagram.com/api/v1/";
    public static final String SHOW_MANY = "https://i.instagram.com/api/v1/friendships/show_many/";
    public static final String SINGUP = "https://i.instagram.com/api/v1/accounts/create/";
    public static final String TAGLINE = "https://i.instagram.com/api/v1/feed/tag/%s/";
    public static final String TAG_SEARCH = "https://i.instagram.com/api/v1/tags/search/?q=%s";
    public static final String TIMELINE = "https://i.instagram.com/api/v1/feed/timeline/";
    public static final String USERLIKED = "https://i.instagram.com/api/v1/feed/liked/";
    public static final String USER_INFO = "https://i.instagram.com/api/v1/users/%s/info/";
    public static final String USER_SEARCH = "https://i.instagram.com/api/v1/users/search/?query=%s";
}
