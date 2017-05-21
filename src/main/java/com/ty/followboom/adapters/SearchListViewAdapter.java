package com.ty.followboom.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.forwardwin.base.widgets.CircleImageView;
import com.squareup.picasso.Picasso;
import com.ty.entities.LoginUser;
import com.ty.entities.Tag;
import com.ty.instaview.R;
import java.util.List;

public class SearchListViewAdapter extends BaseAdapter implements OnClickListener {
    public static final int TYPE_TAG = 1;
    public static final int TYPE_USER = 0;
    private Context mContext;
    private List<Tag> mTagInfoDataList;
    private int mType;
    private List<LoginUser> mUserFeedDataList;
    ViewHolder mViewHolder = null;

    private class ViewHolder {
        private CircleImageView mUserAvatar;
        private TextView mUserDescription;
        private TextView mUserName;

        private ViewHolder() {
        }
    }

    public void setType(int type) {
        this.mType = type;
    }

    public SearchListViewAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        if (this.mType == 0 && this.mUserFeedDataList == null) {
            return 0;
        }
        if (1 == this.mType && this.mTagInfoDataList == null) {
            return 0;
        }
        return this.mType == 0 ? this.mUserFeedDataList.size() : this.mTagInfoDataList.size();
    }

    public Object getItem(int postion) {
        if (this.mType == 0 && this.mUserFeedDataList == null) {
            return null;
        }
        if (1 == this.mType && this.mTagInfoDataList == null) {
            return null;
        }
        return this.mType == 0 ? this.mUserFeedDataList.get(postion) : this.mTagInfoDataList.get(postion);
    }

    public long getItemId(int postion) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            this.mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.search_result_item, null);
            this.mViewHolder.mUserAvatar = (CircleImageView) convertView.findViewById(R.id.user_avatar);
            this.mViewHolder.mUserName = (TextView) convertView.findViewById(R.id.user_name);
            this.mViewHolder.mUserDescription = (TextView) convertView.findViewById(R.id.user_description);
            convertView.setTag(this.mViewHolder);
        } else {
            this.mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) instanceof LoginUser) {
            LoginUser userFeedData = (LoginUser) getItem(position);
            this.mViewHolder.mUserAvatar.setVisibility(0);
            String profileUrl = userFeedData.getProfile_pic_url();
            if (!TextUtils.isEmpty(profileUrl)) {
                Picasso.with(this.mContext).load(profileUrl).into(this.mViewHolder.mUserAvatar);
            }
            this.mViewHolder.mUserName.setText(userFeedData.getUsername());
            this.mViewHolder.mUserDescription.setText(userFeedData.getFull_name());
        } else if (getItem(position) instanceof Tag) {
            Tag tagInfoData = (Tag) getItem(position);
            this.mViewHolder.mUserAvatar.setVisibility(8);
            this.mViewHolder.mUserName.setText("#" + tagInfoData.getName());
            this.mViewHolder.mUserDescription.setText("Relative Media Count: " + tagInfoData.getMedia_count());
        }
        return convertView;
    }

    public void onClick(View view) {
    }

    public void setUserFeedDataList(List<LoginUser> userFeedDataList) {
        this.mUserFeedDataList = userFeedDataList;
    }

    public void setTagInfoDataList(List<Tag> tagInfoDataList) {
        this.mTagInfoDataList = tagInfoDataList;
    }
}
