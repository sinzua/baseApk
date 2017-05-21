package com.ty.followboom.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.ty.entities.ImageVersion;
import com.ty.entities.PostItem;
import com.ty.followboom.helpers.VLTools;
import com.ty.instaview.R;
import java.util.List;

public class PostGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private ImageView mImageView;
    private TextView mLikeCount;
    private ImageView mLogo;
    public List<PostItem> mPosts;
    private int mType;

    public PostGridViewAdapter(Context context, int type) {
        this.mContext = context;
        this.mType = type;
    }

    public int getCount() {
        if (this.mPosts == null) {
            return 0;
        }
        return this.mPosts.size();
    }

    public PostItem getItem(int postion) {
        if (this.mPosts == null) {
            return null;
        }
        return (PostItem) this.mPosts.get(postion);
    }

    public long getItemId(int postion) {
        return 0;
    }

    @SuppressLint({"ViewHolder"})
    public View getView(int postion, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.getlikes_post_item_view, parent, false);
        this.mImageView = (ImageView) convertView.findViewById(R.id.image_view);
        this.mLogo = (ImageView) convertView.findViewById(R.id.logo);
        this.mLikeCount = (TextView) convertView.findViewById(R.id.like_count);
        PostItem videoPost = getItem(postion);
        if (videoPost != null) {
            if (TextUtils.isEmpty(((ImageVersion) videoPost.getImage_versions().get(0)).getUrl())) {
                Picasso.with(this.mContext).load((int) R.drawable.placeholder).into(this.mImageView);
            } else {
                Picasso.with(this.mContext).load(((ImageVersion) videoPost.getImage_versions().get(0)).getUrl()).placeholder((int) R.drawable.placeholder).into(this.mImageView);
            }
            if (2 == this.mType) {
                this.mLogo.setImageResource(R.drawable.ic_heart);
                this.mLikeCount.setText("" + videoPost.getLike_count());
            } else if (5 == this.mType) {
                if (videoPost.getMedia_type() == 2) {
                    this.mLogo.setVisibility(0);
                } else {
                    this.mLogo.setVisibility(8);
                }
                this.mLogo.setImageResource(R.drawable.ic_views);
                this.mLikeCount.setText("" + VLTools.removePointIfHave(Double.toString(videoPost.getView_count())));
            }
        }
        return convertView;
    }

    public void setPosts(List<PostItem> posts) {
        this.mPosts = posts;
    }

    public void addToPosts(List<PostItem> posts) {
        this.mPosts.addAll(posts);
    }

    public List<PostItem> getPosts() {
        return this.mPosts;
    }
}
