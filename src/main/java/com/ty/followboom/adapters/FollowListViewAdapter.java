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
import com.ty.instaview.R;
import java.util.List;

public class FollowListViewAdapter extends BaseAdapter implements OnClickListener {
    private Context mContext;
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

    public FollowListViewAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        if (this.mUserFeedDataList == null) {
            return 0;
        }
        return this.mUserFeedDataList.size();
    }

    public LoginUser getItem(int postion) {
        if (this.mUserFeedDataList == null) {
            return null;
        }
        return (LoginUser) this.mUserFeedDataList.get(postion);
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
        if (getItem(position) != null) {
            LoginUser userFeedData = getItem(position);
            this.mViewHolder.mUserAvatar.setVisibility(0);
            String profileUrl = userFeedData.getProfile_pic_url();
            if (!TextUtils.isEmpty(profileUrl)) {
                Picasso.with(this.mContext).load(profileUrl).into(this.mViewHolder.mUserAvatar);
            }
            this.mViewHolder.mUserName.setText(userFeedData.getUsername());
        }
        return convertView;
    }

    public void onClick(View view) {
    }

    public void setUserFeedDataList(List<LoginUser> userFeedDataList) {
        this.mUserFeedDataList = userFeedDataList;
    }

    public void addToUserFeedDataList(List<LoginUser> userFeedDataList) {
        this.mUserFeedDataList.addAll(userFeedDataList);
    }
}
