package com.ty.followboom.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.squareup.picasso.Picasso;
import com.ty.followboom.views.PhotoImageView;
import com.ty.instaview.R;
import java.io.File;
import java.util.List;

public class DownloadsGridViewAdapter extends BaseAdapter implements OnClickListener {
    private Context mContext;
    private List<String> mFileList;
    ViewHolder mViewHolder = null;

    private class ViewHolder {
        private PhotoImageView mImageView;

        private ViewHolder() {
        }
    }

    public DownloadsGridViewAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        if (this.mFileList == null) {
            return 0;
        }
        return this.mFileList.size();
    }

    public String getItem(int postion) {
        if (this.mFileList == null) {
            return null;
        }
        return (String) this.mFileList.get(postion);
    }

    public long getItemId(int postion) {
        return 0;
    }

    public void setFileList(List<String> fileList) {
        this.mFileList = fileList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            this.mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.download_item_view, null);
            this.mViewHolder.mImageView = (PhotoImageView) convertView.findViewById(R.id.image_view);
            this.mViewHolder.mImageView.setOnClickListener(this);
            convertView.setTag(this.mViewHolder);
        } else {
            this.mViewHolder = (ViewHolder) convertView.getTag();
        }
        String filePath = getItem(position);
        this.mViewHolder.mImageView.setTag(filePath);
        if (!TextUtils.isEmpty(filePath)) {
            if (filePath.endsWith("mp4")) {
                File file = new File(filePath);
                if (file.exists()) {
                    this.mViewHolder.mImageView.setImageBitmap(ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), 3), 200, 200, 2));
                }
            } else {
                Picasso.with(this.mContext).load(filePath).into(this.mViewHolder.mImageView);
            }
        }
        return convertView;
    }

    public void onClick(View v) {
        if (R.id.image_view == v.getId() && (v.getTag() instanceof String)) {
            String url = (String) v.getTag();
            Intent intent;
            if (url.endsWith("mp4")) {
                intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse(url), "video/*");
                this.mContext.startActivity(intent);
                return;
            }
            intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(url), "image/*");
            this.mContext.startActivity(intent);
        }
    }
}
