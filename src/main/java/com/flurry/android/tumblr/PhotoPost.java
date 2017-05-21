package com.flurry.android.tumblr;

import android.os.Bundle;
import android.text.TextUtils;

public class PhotoPost extends Post {
    private String a;
    private String b;

    public PhotoPost(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Url to post cannot be null or empty");
        }
        this.a = str;
    }

    public void setCaption(String str) {
        this.b = str;
    }

    String a() {
        return this.a;
    }

    String b() {
        return this.b;
    }

    Bundle c() {
        Bundle bundle = new Bundle();
        bundle.putString("com.flurry.android.post_caption", b());
        bundle.putString("com.flurry.android.post_url", a());
        bundle.putString("com.flurry.android.post_ios_deeplinks", e());
        bundle.putString("com.flurry.android.post_android_deeplinks", d());
        bundle.putString("com.flurry.android.post_weblink", f());
        bundle.putBoolean("com.flurry.android.is_image_post", true);
        bundle.putInt("com.flurry.android.post_id", g());
        return bundle;
    }
}
