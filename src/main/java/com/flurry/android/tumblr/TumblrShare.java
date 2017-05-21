package com.flurry.android.tumblr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.flurry.sdk.em;
import com.flurry.sdk.fi;
import com.flurry.sdk.hc;
import com.flurry.sdk.hl;
import com.flurry.sdk.js;
import com.flurry.sdk.kg;
import com.flurry.sdk.me;
import java.util.HashMap;

public class TumblrShare {
    private static final String a = TumblrShare.class.getName();

    public static Bitmap getTumblrImage() {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
            return null;
        } else if (js.a() == null) {
            throw new IllegalStateException("Flurry SDK must be initialized.");
        } else {
            hc hcVar = new hc();
            hcVar.x();
            return hcVar.o();
        }
    }

    public static void setOAuthConfig(String str, String str2) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Consumer api key cannot be null or empty.");
        } else if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("Consumer secret cannot be null or empty.");
        } else {
            em.a(str);
            em.b(str2);
        }
    }

    public static void post(Context context, Post post) {
        if (VERSION.SDK_INT < 10) {
            kg.b(a, "Device SDK Version older than 10");
        } else if (js.a() == null) {
            throw new IllegalStateException("Flurry SDK must be initialized before posting on Tumblr");
        } else if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        } else if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("Context cannot be null.");
        } else if (post == null) {
            throw new IllegalArgumentException("Post object cannot be null.");
        } else if (TextUtils.isEmpty(em.a())) {
            throw new IllegalArgumentException("Consumer api key cannot be null or empty. Please set consumer key using setOAuthConfig().");
        } else if (TextUtils.isEmpty(em.b())) {
            throw new IllegalArgumentException("Consumer secret cannot be null or empty. Please set consumer secret using setOAuthConfig().");
        } else {
            hl.a().b("ShareClick", post.d(), new HashMap());
            me.a().a(post.g(), post);
            fi.a(context, post.c());
        }
    }
}
