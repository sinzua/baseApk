package com.ty.followboom.entities;

public class UserInfo {
    private AccountInfo accountInfo;
    private String avatarUrl;
    private String edition;
    private long followersCount;
    private long followingsCount;
    private String key;
    private String password;
    private long postsCount;
    private String userId;
    private String username;
    private String uuid;

    public UserInfo(String username, String password, String avatarUrl, String userId, String uuid, String key, AccountInfo accountInfo) {
        this.username = username;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
        this.uuid = uuid;
        this.key = key;
        this.edition = "";
        this.accountInfo = accountInfo;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEdition() {
        return this.edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AccountInfo getAccountInfo() {
        return this.accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public long getPostsCount() {
        return this.postsCount;
    }

    public void setPostsCount(long postsCount) {
        this.postsCount = postsCount;
    }

    public long getFollowersCount() {
        return this.followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getFollowingsCount() {
        return this.followingsCount;
    }

    public void setFollowingsCount(long followingsCount) {
        this.followingsCount = followingsCount;
    }
}
