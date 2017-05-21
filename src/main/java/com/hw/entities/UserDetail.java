package com.hw.entities;

public class UserDetail {
    private boolean auto_expand_chaining;
    private String biography;
    private boolean can_boost_post;
    private boolean can_convert_to_business;
    private boolean can_see_organic_insights;
    private String external_lynx_url;
    private String external_url;
    private int follower_count;
    private int following_count;
    private String full_name;
    private boolean has_anonymous_profile_picture;
    private boolean has_chaining;
    private ImageVersion hd_profile_pic_url_info;
    private boolean include_direct_blacklist_status;
    private boolean is_business;
    private boolean is_needy;
    private boolean is_private;
    private boolean is_profile_action_needed;
    private boolean is_verified;
    private int media_count;
    private long pk;
    private String profile_pic_url;
    private boolean show_insights_terms;
    private String username;
    private boolean usertag_review_enabled;
    private int usertags_count;

    public int getUsertags_count() {
        return this.usertags_count;
    }

    public void setUsertags_count(int usertags_count) {
        this.usertags_count = usertags_count;
    }

    public boolean isHas_anonymous_profile_picture() {
        return this.has_anonymous_profile_picture;
    }

    public void setHas_anonymous_profile_picture(boolean has_anonymous_profile_picture) {
        this.has_anonymous_profile_picture = has_anonymous_profile_picture;
    }

    public int getFollowing_count() {
        return this.following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public boolean isUsertag_review_enabled() {
        return this.usertag_review_enabled;
    }

    public void setUsertag_review_enabled(boolean usertag_review_enabled) {
        this.usertag_review_enabled = usertag_review_enabled;
    }

    public String getExternal_lynx_url() {
        return this.external_lynx_url;
    }

    public void setExternal_lynx_url(String external_lynx_url) {
        this.external_lynx_url = external_lynx_url;
    }

    public boolean isCan_boost_post() {
        return this.can_boost_post;
    }

    public void setCan_boost_post(boolean can_boost_post) {
        this.can_boost_post = can_boost_post;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean is_profile_action_needed() {
        return this.is_profile_action_needed;
    }

    public void setIs_profile_action_needed(boolean is_profile_action_needed) {
        this.is_profile_action_needed = is_profile_action_needed;
    }

    public boolean isHas_chaining() {
        return this.has_chaining;
    }

    public void setHas_chaining(boolean has_chaining) {
        this.has_chaining = has_chaining;
    }

    public int getMedia_count() {
        return this.media_count;
    }

    public void setMedia_count(int media_count) {
        this.media_count = media_count;
    }

    public boolean isAuto_expand_chaining() {
        return this.auto_expand_chaining;
    }

    public void setAuto_expand_chaining(boolean auto_expand_chaining) {
        this.auto_expand_chaining = auto_expand_chaining;
    }

    public boolean isInclude_direct_blacklist_status() {
        return this.include_direct_blacklist_status;
    }

    public void setInclude_direct_blacklist_status(boolean include_direct_blacklist_status) {
        this.include_direct_blacklist_status = include_direct_blacklist_status;
    }

    public int getFollower_count() {
        return this.follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public long getPk() {
        return this.pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public boolean is_verified() {
        return this.is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic_url() {
        return this.profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public boolean isCan_see_organic_insights() {
        return this.can_see_organic_insights;
    }

    public void setCan_see_organic_insights(boolean can_see_organic_insights) {
        this.can_see_organic_insights = can_see_organic_insights;
    }

    public boolean isCan_convert_to_business() {
        return this.can_convert_to_business;
    }

    public void setCan_convert_to_business(boolean can_convert_to_business) {
        this.can_convert_to_business = can_convert_to_business;
    }

    public boolean is_private() {
        return this.is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public boolean is_business() {
        return this.is_business;
    }

    public void setIs_business(boolean is_business) {
        this.is_business = is_business;
    }

    public boolean isShow_insights_terms() {
        return this.show_insights_terms;
    }

    public void setShow_insights_terms(boolean show_insights_terms) {
        this.show_insights_terms = show_insights_terms;
    }

    public ImageVersion getHd_profile_pic_url_info() {
        return this.hd_profile_pic_url_info;
    }

    public void setHd_profile_pic_url_info(ImageVersion hd_profile_pic_url_info) {
        this.hd_profile_pic_url_info = hd_profile_pic_url_info;
    }

    public boolean is_needy() {
        return this.is_needy;
    }

    public void setIs_needy(boolean is_needy) {
        this.is_needy = is_needy;
    }

    public String getExternal_url() {
        return this.external_url;
    }

    public void setExternal_url(String external_url) {
        this.external_url = external_url;
    }
}
