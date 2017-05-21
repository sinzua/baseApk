package com.ty.followboom.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;

public class InstagramHelper {
    public static final String DEFAULT_ADMOB = "1";
    public static final String FAQ_URL = "http://v.xiumi.us/board/v5/2xhkc/8469009";
    private static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
    private static final String TYPE_IMAGE = "image/*";
    private static final String TYPE_VIDEO = "video/*";

    public static void repostImage(Context context, String mediaPath) {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType(TYPE_IMAGE);
        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(mediaPath)));
        context.startActivity(Intent.createChooser(share, "InstaSaver"));
    }

    public static void shareImage(Context context, String mediaUrl) {
        String mediaPath = ImageDownloader.getInstance(context).getAlbumPath() + ImageDownloader.getInstance(context).generateImageName(mediaUrl);
        Intent share = new Intent("android.intent.action.SEND");
        share.setType(TYPE_IMAGE);
        share.putExtra("android.intent.extra.SUBJECT", "Share");
        share.putExtra("android.intent.extra.TEXT", mediaUrl);
        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(mediaPath)));
        context.startActivity(Intent.createChooser(share, "InstaSaver"));
    }

    public static void shareVideo(Context context, String videoUrl) {
        String mediaPath = ImageDownloader.getInstance(context).getAlbumPath() + ImageDownloader.getInstance(context).generateVideoName(videoUrl);
        Intent share = new Intent("android.intent.action.SEND");
        share.setType(TYPE_VIDEO);
        share.putExtra("android.intent.extra.SUBJECT", "Share");
        share.putExtra("android.intent.extra.TEXT", videoUrl);
        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(mediaPath)));
        context.startActivity(Intent.createChooser(share, "InstaSaver"));
    }
}
