package com.ty.followboom.helpers;

import com.ty.entities.PostItem;

public class VideoHelper {
    public static boolean isVideo(PostItem mediaFeedData) {
        if (mediaFeedData != null && mediaFeedData.getMedia_type() == 2) {
            return true;
        }
        return false;
    }
}
