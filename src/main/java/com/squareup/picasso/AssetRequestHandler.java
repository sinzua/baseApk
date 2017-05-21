package com.squareup.picasso;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.RequestHandler.Result;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.io.IOException;
import java.io.InputStream;

class AssetRequestHandler extends RequestHandler {
    protected static final String ANDROID_ASSET = "android_asset";
    private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
    private final AssetManager assetManager;

    public AssetRequestHandler(Context context) {
        this.assetManager = context.getAssets();
    }

    public boolean canHandleRequest(Request data) {
        Uri uri = data.uri;
        if (ParametersKeys.FILE.equals(uri.getScheme()) && !uri.getPathSegments().isEmpty() && ANDROID_ASSET.equals(uri.getPathSegments().get(0))) {
            return true;
        }
        return false;
    }

    public Result load(Request data, int networkPolicy) throws IOException {
        return new Result(decodeAsset(data, data.uri.toString().substring(ASSET_PREFIX_LENGTH)), LoadedFrom.DISK);
    }

    Bitmap decodeAsset(Request data, String filePath) throws IOException {
        InputStream inputStream;
        Options options = RequestHandler.createBitmapOptions(data);
        if (RequestHandler.requiresInSampleSize(options)) {
            inputStream = null;
            try {
                inputStream = this.assetManager.open(filePath);
                BitmapFactory.decodeStream(inputStream, null, options);
                RequestHandler.calculateInSampleSize(data.targetWidth, data.targetHeight, options, data);
            } finally {
                Utils.closeQuietly(inputStream);
            }
        }
        inputStream = this.assetManager.open(filePath);
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            return decodeStream;
        } finally {
            Utils.closeQuietly(inputStream);
        }
    }
}
