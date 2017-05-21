package com.ty.followboom.helpers;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.ty.instaview.R;

public class PicassoHelper {
    public static void setImageView(Context context, ImageView imageView, String ImageUrl) {
        if (context != null) {
            if (TextUtils.isEmpty(ImageUrl)) {
                Picasso.with(context).load((int) R.drawable.placeholder).into(imageView);
            } else {
                Picasso.with(context).load(ImageUrl).placeholder((int) R.drawable.placeholder).into(imageView);
            }
        }
    }

    public static void setImageView(Context context, ImageView imageView, String ImageUrl, int defaultImageResource) {
        if (TextUtils.isEmpty(ImageUrl)) {
            Picasso.with(context).load(defaultImageResource).into(imageView);
        } else {
            Picasso.with(context).load(ImageUrl).placeholder(defaultImageResource).into(imageView);
        }
    }
}
