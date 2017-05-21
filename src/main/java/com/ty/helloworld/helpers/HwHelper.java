package com.ty.helloworld.helpers;

import android.content.Context;
import com.ty.entities.PostItem;
import java.util.List;

public class HwHelper {
    private static final String HW = "hw";
    private static final String POSTS_URL = "posts_url";
    private static final String PROFILE_URL = "profile_url";

    public static String getProfileUrl(Context context, String profileUrl) {
        return PreferenceHelper.getContent(context, HW, PROFILE_URL);
    }

    public static void savePosts(Context context, List<PostItem> posts) {
        PreferenceHelper.saveContent(context, HW, POSTS_URL, JsonSerializer.getInstance().serialize(posts));
    }

    public static List<PostItem> getPosts(Context context) {
        return (List) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, HW, POSTS_URL), List.class, PostItem.class);
    }
}
