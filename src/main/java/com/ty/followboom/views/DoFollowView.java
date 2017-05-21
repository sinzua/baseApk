package com.ty.followboom.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.ty.followboom.entities.Order;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.instaview.R;

public class DoFollowView extends LinearLayout {
    private Context mContext;
    private Order mOrder;
    private LinearLayout mRootView = ((LinearLayout) findViewById(R.id.root_view));

    public DoFollowView(Context context) {
        super(context);
        this.mContext = context;
        inflate(getContext(), R.layout.do_like_view, this);
    }

    public void bindView(Order order) {
        this.mOrder = order;
        if (this.mOrder != null) {
            View convertView;
            if (!this.mOrder.isCollageOrder()) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.single_item_view, this, false);
                PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview), getAvatarUrl(this.mOrder.getAvatarUrl()), R.drawable.placeholder);
            } else if (this.mOrder.getCollage().size() >= 4) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.four_items_view, this, false);
                PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview_1), getAvatarUrl(((Order) this.mOrder.getCollage().get(0)).getAvatarUrl()), R.drawable.placeholder);
                PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview_2), getAvatarUrl(((Order) this.mOrder.getCollage().get(1)).getAvatarUrl()), R.drawable.placeholder);
                PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview_3), getAvatarUrl(((Order) this.mOrder.getCollage().get(2)).getAvatarUrl()), R.drawable.placeholder);
                PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview_4), getAvatarUrl(((Order) this.mOrder.getCollage().get(3)).getAvatarUrl()), R.drawable.placeholder);
            } else if (this.mOrder.getCollage().size() > 0) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.single_item_view, this, false);
                PicassoHelper.setImageView(this.mContext, (ImageView) convertView.findViewById(R.id.get_coins_imageview), getAvatarUrl(((Order) this.mOrder.getCollage().get(0)).getAvatarUrl()), R.drawable.placeholder);
            } else {
                convertView = new View(this.mContext);
            }
            this.mRootView.addView(convertView);
        }
    }

    private String getAvatarUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.indexOf("?versionId") > 0 ? url.substring(0, url.indexOf("?versionId")) : url;
    }
}
