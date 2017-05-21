package com.flurry.sdk;

import android.util.SparseArray;
import com.flurry.android.tumblr.Post;

public class me {
    private static me b = null;
    private SparseArray<Post> a = new SparseArray();

    private me() {
    }

    public static me a() {
        if (b == null) {
            b = new me();
        }
        return b;
    }

    public void a(int i, Post post) {
        this.a.append(i, post);
    }

    public void a(int i) {
        this.a.remove(i);
    }
}
