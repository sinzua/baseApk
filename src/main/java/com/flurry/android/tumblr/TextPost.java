package com.flurry.android.tumblr;

import android.os.Bundle;
import android.text.TextUtils;

public class TextPost extends Post {
    private String a;
    private String b;

    public TextPost(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Body to post cannot be null or empty");
        }
        this.b = str;
    }

    public void setTitle(String str) {
        this.a = str;
    }

    String a() {
        return this.a;
    }

    String b() {
        return this.b;
    }

    Bundle c() {
        Bundle bundle = new Bundle();
        bundle.putString("com.flurry.android.post_title", a());
        bundle.putString("com.flurry.android.post_body", b());
        bundle.putString("com.flurry.android.post_ios_deeplinks", e());
        bundle.putString("com.flurry.android.post_android_deeplinks", d());
        bundle.putString("com.flurry.android.post_weblink", f());
        bundle.putBoolean("com.flurry.android.is_text_post", true);
        bundle.putInt("com.flurry.android.post_id", g());
        return bundle;
    }
}
