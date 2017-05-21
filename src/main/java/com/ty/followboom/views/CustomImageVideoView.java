package com.ty.followboom.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.VideoView;
import com.ty.entities.ImageVersion;
import com.ty.entities.PostItem;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.instaview.R;

public class CustomImageVideoView extends FrameLayout {
    private Context mContext;
    private PhotoImageView mImageView;
    private PostItem mMediaFeedData;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnPreparedListener mOnPreparedListener;
    private VideoView mVideoView;
    private boolean notFirstTime;

    public CustomImageVideoView(Context context) {
        super(context);
        this.mOnCompletionListener = new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
            }
        };
        this.mOnErrorListener = new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        };
        this.mOnPreparedListener = new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                CustomImageVideoView.this.mVideoView.start();
            }
        };
        init(context);
    }

    public CustomImageVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOnCompletionListener = /* anonymous class already generated */;
        this.mOnErrorListener = /* anonymous class already generated */;
        this.mOnPreparedListener = /* anonymous class already generated */;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        inflate(getContext(), R.layout.image_video_view, this);
        this.mVideoView = (VideoView) findViewById(R.id.video_view);
        this.mImageView = (PhotoImageView) findViewById(R.id.image_view);
    }

    public void bindView(PostItem mediaFeedData) {
        this.mMediaFeedData = mediaFeedData;
        setImage(((ImageVersion) mediaFeedData.getImage_versions().get(0)).getUrl());
    }

    public void setImage(String url) {
        PicassoHelper.setImageView(this.mContext, this.mImageView, url);
    }

    public void playVideo(String videoUrl) {
        if (this.notFirstTime) {
            this.mVideoView.start();
            return;
        }
        this.mVideoView.setVideoURI(Uri.parse(videoUrl));
        this.mVideoView.requestFocus();
        this.mVideoView.setVisibility(0);
        this.mImageView.setVisibility(8);
        this.mVideoView.setOnCompletionListener(this.mOnCompletionListener);
        this.mVideoView.setOnErrorListener(this.mOnErrorListener);
        this.mVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                CustomImageVideoView.this.mVideoView.start();
            }
        });
    }

    public void pauseVideo() {
        this.mVideoView.pause();
        this.mImageView.setVisibility(8);
        this.mVideoView.setVisibility(0);
    }
}
