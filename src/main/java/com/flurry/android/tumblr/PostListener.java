package com.flurry.android.tumblr;

public interface PostListener {
    void onPostFailure(String str);

    void onPostSuccess(Long l);
}
