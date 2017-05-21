package com.ty.followboom.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.forwardwin.base.widgets.Tools;
import com.ty.entities.ImageVersion;
import com.ty.entities.PostItem;
import com.ty.followboom.helpers.ImageDownloader;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.helpers.VideoHelper;
import com.ty.followboom.models.RateUsManager;
import com.ty.followboom.views.CustomDialog;
import com.ty.followboom.views.CustomImageVideoView;
import com.ty.followboom.views.OptionDialog;
import com.ty.instaview.R;
import java.util.Arrays;
import java.util.List;

public class TimeLineGridViewAdapter extends BaseAdapter implements OnClickListener {
    private static final String TAG = "TimeLineGridViewAdapter";
    private Context mContext;
    private CustomDialog mCustomDialog;
    private OnClickListener mCustomDialogClickListener = new OnClickListener() {
        public void onClick(View view) {
            int id = view.getId();
            if (R.id.positive_button == id) {
                Log.d(TimeLineGridViewAdapter.TAG, "go to rate us");
                TrackHelper.track(TrackHelper.CATEGORY_RATE, TrackHelper.ACTION_RATE, "click");
                VLTools.rateUs(TimeLineGridViewAdapter.this.mContext);
                TimeLineGridViewAdapter.this.mCustomDialog.dismiss();
            } else if (R.id.negative_button == id) {
                Log.d(TimeLineGridViewAdapter.TAG, "rate us download");
                TrackHelper.track(TrackHelper.CATEGORY_RATE, TrackHelper.ACTION_DOWNLOAD, "click");
                if (view.getTag() instanceof PostItem) {
                    if (VideoHelper.isVideo((PostItem) view.getTag())) {
                        TimeLineGridViewAdapter.this.downloadVideo(((ImageVersion) ((PostItem) view.getTag()).getVideo_versions().get(0)).getUrl());
                    } else {
                        TimeLineGridViewAdapter.this.downloadImage(((ImageVersion) ((PostItem) view.getTag()).getImage_versions().get(0)).getUrl());
                    }
                }
                TimeLineGridViewAdapter.this.mCustomDialog.dismiss();
            }
        }
    };
    private List<PostItem> mMediaFeedData;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (TimeLineGridViewAdapter.this.mOptionDialog != null) {
                TimeLineGridViewAdapter.this.mOptionDialog.dismiss();
            }
        }
    };
    private OptionDialog mOptionDialog;
    private boolean mUserInfoClickFlag;
    ViewHolder viewHolder = null;

    private class ViewHolder {
        private CustomImageVideoView mCustomImageVideoView;
        private ImageView mDownloadButton;
        private ImageView mPlayButton;
        private TextView mPostTime;
        private ImageView mProfileImage;
        private ImageView mReportButton;
        private LinearLayout mUserInfo;
        private TextView mUserName;

        private ViewHolder() {
        }
    }

    public TimeLineGridViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setUserInfoClickFlag(boolean userInfoClickFlag) {
        this.mUserInfoClickFlag = userInfoClickFlag;
    }

    public int getCount() {
        if (this.mMediaFeedData == null) {
            return 0;
        }
        return this.mMediaFeedData.size();
    }

    public PostItem getItem(int postion) {
        if (this.mMediaFeedData == null) {
            return null;
        }
        return (PostItem) this.mMediaFeedData.get(postion);
    }

    public long getItemId(int postion) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            this.viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.post_item_view, null);
            this.viewHolder.mUserInfo = (LinearLayout) convertView.findViewById(R.id.user_info);
            this.viewHolder.mProfileImage = (ImageView) convertView.findViewById(R.id.profile_image);
            this.viewHolder.mUserName = (TextView) convertView.findViewById(R.id.user_name);
            this.viewHolder.mPostTime = (TextView) convertView.findViewById(R.id.post_time);
            this.viewHolder.mCustomImageVideoView = (CustomImageVideoView) convertView.findViewById(R.id.image_video_view);
            this.viewHolder.mPlayButton = (ImageView) convertView.findViewById(R.id.play_button);
            this.viewHolder.mDownloadButton = (ImageView) convertView.findViewById(R.id.download_button);
            this.viewHolder.mReportButton = (ImageView) convertView.findViewById(R.id.report_button);
            this.viewHolder.mUserInfo.setOnClickListener(this);
            this.viewHolder.mPlayButton.setOnClickListener(this);
            this.viewHolder.mDownloadButton.setOnClickListener(this);
            this.viewHolder.mReportButton.setOnClickListener(this);
            convertView.setTag(this.viewHolder);
        } else {
            this.viewHolder = (ViewHolder) convertView.getTag();
        }
        PostItem mediaFeedData = getItem(position);
        if (mediaFeedData != null) {
            int i;
            PicassoHelper.setImageView(this.mContext, this.viewHolder.mProfileImage, mediaFeedData.getUser().getProfile_pic_url(), R.drawable.ic_profile);
            this.viewHolder.mUserName.setText(mediaFeedData.getUser().getUsername());
            this.viewHolder.mPostTime.setText(Tools.timeDiffStringFromTimeInterval(mediaFeedData.getTaken_at()));
            this.viewHolder.mUserInfo.setTag(mediaFeedData);
            this.viewHolder.mDownloadButton.setTag(mediaFeedData);
            if (VLTools.inReview(this.mContext)) {
                this.viewHolder.mDownloadButton.setVisibility(8);
            }
            ImageView access$1000 = this.viewHolder.mPlayButton;
            if (VideoHelper.isVideo(mediaFeedData)) {
                i = 0;
            } else {
                i = 8;
            }
            access$1000.setVisibility(i);
            this.viewHolder.mPlayButton.setTag(mediaFeedData);
            this.viewHolder.mCustomImageVideoView.bindView(mediaFeedData);
        }
        return convertView;
    }

    public List<PostItem> getMediaFeedData() {
        return this.mMediaFeedData;
    }

    public void setMediaFeedData(List<PostItem> mediaFeedData) {
        this.mMediaFeedData = mediaFeedData;
    }

    public void addToMediaFeedData(List<PostItem> mediaFeedData) {
        this.mMediaFeedData.addAll(mediaFeedData);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (R.id.download_button == id) {
            TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_DOWNLOAD, "click");
            if (RateUsManager.getSingleton().needRateUs(this.mContext)) {
                this.mCustomDialog = new CustomDialog(this.mContext, this.mContext.getResources().getString(R.string.settings_rate_us_5_stars), this.mContext.getResources().getString(R.string.settings_rate_us_5_stars_content), this.mCustomDialogClickListener);
                this.mCustomDialog.setPositiveButtonText(this.mContext.getResources().getString(R.string.rate_us));
                this.mCustomDialog.setNegativeButtonText(this.mContext.getResources().getString(R.string.download));
                this.mCustomDialog.setNegativeButtonTextColor(this.mContext.getResources().getColor(R.color.app_theme));
                this.mCustomDialog.getNegativeButton().setTag(v.getTag());
                this.mCustomDialog.show();
            } else if (!(v.getTag() instanceof PostItem)) {
            } else {
                if (VideoHelper.isVideo((PostItem) v.getTag())) {
                    downloadVideo(((ImageVersion) ((PostItem) v.getTag()).getVideo_versions().get(0)).getUrl());
                } else {
                    downloadImage(((ImageVersion) ((PostItem) v.getTag()).getImage_versions().get(0)).getUrl());
                }
            }
        } else if (R.id.report_button == id) {
            TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_REPORT, "click");
            this.mOptionDialog = new OptionDialog(this.mContext, Arrays.asList(this.mContext.getResources().getStringArray(R.array.option_list)), this.mOnItemClickListener);
            this.mOptionDialog.show();
        } else if (R.id.play_button == id) {
            TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_PLAY, "click");
            if ((v.getTag() instanceof PostItem) && VideoHelper.isVideo((PostItem) v.getTag())) {
                playVideo(((ImageVersion) ((PostItem) v.getTag()).getVideo_versions().get(0)).getUrl());
            }
        } else if (R.id.user_info != id) {
        }
    }

    private void playVideo(String videoUrl) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse(videoUrl), "video/mp4");
        this.mContext.startActivity(intent);
    }

    private void downloadVideo(String url) {
        ImageDownloader.getInstance(this.mContext).saveVideo(url);
    }

    private void downloadImage(String url) {
        ImageDownloader.getInstance(this.mContext).downloadImage(url);
    }
}
