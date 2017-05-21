package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.RequestHandler.Result;
import java.io.IOException;
import java.io.InputStream;

class ContentStreamRequestHandler extends RequestHandler {
    final Context context;

    ContentStreamRequestHandler(Context context) {
        this.context = context;
    }

    public boolean canHandleRequest(Request data) {
        return "content".equals(data.uri.getScheme());
    }

    public Result load(Request data, int networkPolicy) throws IOException {
        return new Result(decodeContentStream(data), LoadedFrom.DISK);
    }

    protected Bitmap decodeContentStream(Request data) throws IOException {
        InputStream inputStream;
        ContentResolver contentResolver = this.context.getContentResolver();
        Options options = RequestHandler.createBitmapOptions(data);
        if (RequestHandler.requiresInSampleSize(options)) {
            inputStream = null;
            try {
                inputStream = contentResolver.openInputStream(data.uri);
                BitmapFactory.decodeStream(inputStream, null, options);
                RequestHandler.calculateInSampleSize(data.targetWidth, data.targetHeight, options, data);
            } finally {
                Utils.closeQuietly(inputStream);
            }
        }
        inputStream = contentResolver.openInputStream(data.uri);
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            return decodeStream;
        } finally {
            Utils.closeQuietly(inputStream);
        }
    }
}
