package com.ty.followboom.helpers;

import android.content.Context;
import com.forwardwin.base.widgets.JsonSerializer;
import com.ty.entities.PostItem;
import com.ty.helloworld.helpers.PreferenceHelper;
import java.util.List;

public class HwHelper {
    private static final String HW = "hw";
    private static final String POSTS_URL = "posts_url";
    private static final String PROFILE_URL = "profile_url";

    public static String getProfileUrl(Context context) {
        return PreferenceHelper.getContent(context, HW, PROFILE_URL);
    }

    public static void saveProfileUrl(Context context, String profileUrl) {
        com.forwardwin.base.widgets.PreferenceHelper.saveContent(context, HW, PROFILE_URL, profileUrl);
    }

    public static void savePosts(Context context, List<PostItem> posts) {
        com.forwardwin.base.widgets.PreferenceHelper.saveContent(context, HW, POSTS_URL, JsonSerializer.getInstance().serialize(posts));
    }

    public static List<PostItem> getPosts(Context context) {
        return (List) JsonSerializer.getInstance().deserialize(PreferenceHelper.getContent(context, HW, POSTS_URL), List.class, PostItem.class);
    }
}
