package com.ty.followboom.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ty.followboom.entities.Order;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.instaview.R;
import java.util.Iterator;

public class DoMixView extends LinearLayout {
    private Context mContext;
    private int[] mImageViewIds = new int[]{R.id.get_coins_imageview_1, R.id.get_coins_imageview_2, R.id.get_coins_imageview_3, R.id.get_coins_imageview_4};
    private Order mOrder;
    private LinearLayout mRootView;

    public DoMixView(Context context) {
        super(context);
        this.mContext = context;
        inflate(getContext(), R.layout.do_like_view, this);
        this.mRootView = (LinearLayout) findViewById(R.id.root_view);
    }

    public void bindView(Order order) {
        this.mOrder = order;
        if (this.mOrder != null) {
            View convertView;
            if (!this.mOrder.isCollageOrder()) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.single_item_view, this, false);
                if (this.mOrder.isLikeOrder()) {
                    PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview), getMediaUrl(this.mOrder.getVideoThumbnailUrl()), R.drawable.placeholder);
                } else if (this.mOrder.isFollowOrder()) {
                    PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview), getMediaUrl(this.mOrder.getAvatarUrl()), R.drawable.placeholder);
                }
            } else if (this.mOrder.getCollage().size() >= 4) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.four_items_view, this, false);
                Iterator it = this.mOrder.getCollage().iterator();
                while (it.hasNext()) {
                    orderItem = (Order) it.next();
                    if (orderItem.isLikeOrder()) {
                        PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(this.mImageViewIds[this.mOrder.getCollage().indexOf(orderItem)]), getMediaUrl(orderItem.getVideoThumbnailUrl()), R.drawable.placeholder);
                    } else if (orderItem.isFollowOrder()) {
                        PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(this.mImageViewIds[this.mOrder.getCollage().indexOf(orderItem)]), getMediaUrl(orderItem.getAvatarUrl()), R.drawable.placeholder);
                    }
                }
            } else if (this.mOrder.getCollage().size() > 0) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.single_item_view, this, false);
                orderItem = (Order) this.mOrder.getCollage().get(0);
                if (orderItem.isLikeOrder()) {
                    PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview), getMediaUrl(((Order) this.mOrder.getCollage().get(0)).getVideoThumbnailUrl()), R.drawable.placeholder);
                } else if (orderItem.isFollowOrder()) {
                    PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview), getMediaUrl(((Order) this.mOrder.getCollage().get(0)).getAvatarUrl()), R.drawable.placeholder);
                }
            } else {
                convertView = new View(this.mContext);
            }
            this.mRootView.addView(convertView);
        }
    }

    private String getMediaUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.indexOf("?versionId") > 0 ? url.substring(0, url.indexOf("?versionId")) : url;
    }
}
