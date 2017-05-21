package com.ty.followboom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.forwardwin.base.widgets.CircleImageView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.HeaderGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshHeaderGridView;
import com.ty.entities.ImageVersion;
import com.ty.entities.LoginUser;
import com.ty.entities.PostItem;
import com.ty.followboom.adapters.TimeLineGridViewAdapter;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.ImageDownloader;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.helpers.VideoHelper;
import com.ty.followboom.models.CoinsManager;
import com.ty.followboom.models.RateUsManager;
import com.ty.followboom.okhttp.responses.GetAccountInfoResponse;
import com.ty.followboom.views.CustomDialog;
import com.ty.http.RequestCallback;
import com.ty.http.responses.TaglineResponse;
import com.ty.http.responses.TimelineResponse;
import com.ty.http.responses.UserinfoResponse;
import com.ty.instagramapi.InstagramService;
import com.ty.instaview.R;
import pl.tajchert.sample.DotsTextView;

public class UserMainActivity extends Activity implements OnClickListener {
    public static final String EXTRA_TAG_NAME = "tag_name";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_USER_INFO = "user_info";
    private static final String TAG = "UserMainActivity";
    public static final int TYPE_TAG = 1;
    public static final int TYPE_USER = 0;
    private RequestCallback<TimelineResponse> loadmorefeedCallback = new RequestCallback<TimelineResponse>() {
        public void onResponse(TimelineResponse timelineResponse) {
            Log.d(UserMainActivity.TAG, "load more succeed");
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
            UserMainActivity.this.mTimelineResponse = timelineResponse;
            if (UserMainActivity.this.mTimelineResponse != null) {
                UserMainActivity.this.mAdapter.addToMediaFeedData(UserMainActivity.this.mTimelineResponse.getItems());
                UserMainActivity.this.mAdapter.notifyDataSetChanged();
            }
        }

        public void onFailure(Exception e) {
            Log.d(UserMainActivity.TAG, "load more failed");
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
        }
    };
    private RequestCallback<TaglineResponse> loadmoretagfeedCallback = new RequestCallback<TaglineResponse>() {
        public void onResponse(TaglineResponse taglineResponse) {
            Log.d(UserMainActivity.TAG, "load more tag succeed");
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
            UserMainActivity.this.mTaglineResponse = taglineResponse;
            if (UserMainActivity.this.mTaglineResponse != null) {
                UserMainActivity.this.mAdapter.addToMediaFeedData(UserMainActivity.this.mTaglineResponse.getItems());
                UserMainActivity.this.mAdapter.notifyDataSetChanged();
            }
        }

        public void onFailure(Exception e) {
            Log.d(UserMainActivity.TAG, "load more tag failed");
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
        }
    };
    private com.ty.followboom.okhttp.RequestCallback<GetAccountInfoResponse> mAccountInfoCallback = new com.ty.followboom.okhttp.RequestCallback<GetAccountInfoResponse>() {
        public void onResponse(GetAccountInfoResponse getAccountInfoResponse) {
            if (getAccountInfoResponse.isSuccessful()) {
                CoinsManager.getSingleton().setCoins(getAccountInfoResponse.getData().getCoins());
                Toast.makeText(UserMainActivity.this, "Rate us succeed", 0).show();
                return;
            }
            Toast.makeText(UserMainActivity.this, "Rate us failed", 0).show();
        }

        public void onFailure(Exception e) {
            Toast.makeText(UserMainActivity.this, "Rate us failed", 0).show();
        }
    };
    private AdView mAdView;
    private TimeLineGridViewAdapter mAdapter;
    private ImageView mBackButton;
    private CustomDialog mCustomDialog;
    private OnClickListener mCustomDialogClickListener = new OnClickListener() {
        public void onClick(View view) {
            int id = view.getId();
            if (R.id.positive_button == id) {
                Log.d(UserMainActivity.TAG, "go to rate us");
                TrackHelper.track(TrackHelper.CATEGORY_RATE, TrackHelper.ACTION_RATE, "click");
                RateUsManager.getSingleton().trigger();
                VLTools.rateUs(UserMainActivity.this);
                UserMainActivity.this.mCustomDialog.dismiss();
            } else if (R.id.negative_button == id) {
                Log.d(UserMainActivity.TAG, "rate us canceled");
                TrackHelper.track(TrackHelper.CATEGORY_RATE, TrackHelper.ACTION_CANCEL, "click");
                UserMainActivity.this.mCustomDialog.dismiss();
            }
        }
    };
    private DotsTextView mDotsTextView;
    private Button mDownloadAllButton;
    private ImageView mDownloadButton;
    private TextView mFollowersCount;
    private LinearLayout mFollowersLayout;
    private TextView mFollowingsCount;
    private LinearLayout mFollowingsLayout;
    private LinearLayout mHeaderView;
    private OnRefreshListener2<HeaderGridView> mOnRefreshListener = new OnRefreshListener2<HeaderGridView>() {
        public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> pullToRefreshBase) {
            if (UserMainActivity.this.mType == 0) {
                UserMainActivity.this.getUserFeedMedia();
            } else if (1 == UserMainActivity.this.mType) {
                UserMainActivity.this.getTagFeedMedia();
            }
        }

        public void onPullUpToRefresh(PullToRefreshBase<HeaderGridView> pullToRefreshBase) {
            if (UserMainActivity.this.mType == 0) {
                UserMainActivity.this.loadMoreUserFeedMedia();
            } else if (1 == UserMainActivity.this.mType) {
                UserMainActivity.this.loadMoreTagFeedMedia();
            }
        }
    };
    private PullToRefreshHeaderGridView mPullRefreshGridView;
    private String mTagName;
    private TaglineResponse mTaglineResponse;
    private HeaderGridView mTimeLineGridView;
    private TimelineResponse mTimelineResponse;
    private TextView mTitle;
    private int mType;
    private CircleImageView mUserAvatar;
    private LoginUser mUserFeedData;
    private RequestCallback<TaglineResponse> tagfeedCallback = new RequestCallback<TaglineResponse>() {
        public void onResponse(TaglineResponse taglineResponse) {
            Log.d(UserMainActivity.TAG, "get tag feed succeed");
            UserMainActivity.this.mDotsTextView.hideAndStop();
            UserMainActivity.this.mDotsTextView.setVisibility(8);
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
            UserMainActivity.this.mTaglineResponse = taglineResponse;
            if (UserMainActivity.this.mTaglineResponse != null) {
                UserMainActivity.this.mAdapter.setMediaFeedData(UserMainActivity.this.mTaglineResponse.getItems());
                UserMainActivity.this.mAdapter.notifyDataSetChanged();
            }
        }

        public void onFailure(Exception e) {
            Log.d(UserMainActivity.TAG, "get tag feed failed");
            UserMainActivity.this.mDotsTextView.hideAndStop();
            UserMainActivity.this.mDotsTextView.setVisibility(8);
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
        }
    };
    private RequestCallback<TimelineResponse> userfeedCallback = new RequestCallback<TimelineResponse>() {
        public void onResponse(TimelineResponse timelineResponse) {
            Log.d(UserMainActivity.TAG, "get user feed succeed");
            UserMainActivity.this.mDotsTextView.hideAndStop();
            UserMainActivity.this.mDotsTextView.setVisibility(8);
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
            UserMainActivity.this.mTimelineResponse = timelineResponse;
            if (UserMainActivity.this.mTimelineResponse != null) {
                UserMainActivity.this.mAdapter.setMediaFeedData(UserMainActivity.this.mTimelineResponse.getItems());
                UserMainActivity.this.mAdapter.notifyDataSetChanged();
            }
        }

        public void onFailure(Exception e) {
            Log.d(UserMainActivity.TAG, "get user feed failed");
            UserMainActivity.this.mDotsTextView.hideAndStop();
            UserMainActivity.this.mDotsTextView.setVisibility(8);
            UserMainActivity.this.mPullRefreshGridView.onRefreshComplete();
        }
    };
    private RequestCallback<UserinfoResponse> userinfoCallback = new RequestCallback<UserinfoResponse>() {
        public void onResponse(UserinfoResponse userinfoResponse) {
            Log.d(UserMainActivity.TAG, "get user info succeed");
            UserMainActivity.this.mFollowersCount.setText("" + userinfoResponse.getUser().getFollower_count());
            UserMainActivity.this.mFollowingsCount.setText("" + userinfoResponse.getUser().getFollowing_count());
        }

        public void onFailure(Exception e) {
            Log.d(UserMainActivity.TAG, "get user info failed");
        }
    };

    protected void onResume() {
        super.onResume();
        this.mAdView.setVisibility(AppConfigHelper.showAdmob(this) ? 0 : 8);
        RateUsManager.getSingleton().tryRecord(this, this.mAccountInfoCallback);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_user_main);
        this.mType = getIntent().getIntExtra("type", 0);
        if (this.mType == 0) {
            this.mUserFeedData = (LoginUser) new Gson().fromJson(getIntent().getStringExtra(EXTRA_USER_INFO), LoginUser.class);
        } else if (1 == this.mType) {
            this.mTagName = getIntent().getStringExtra(EXTRA_TAG_NAME);
        }
        onActivate();
    }

    private void onActivate() {
        this.mBackButton = (ImageView) findViewById(R.id.sidebar_button);
        this.mTitle = (TextView) findViewById(R.id.fragment_title);
        this.mDownloadButton = (ImageView) findViewById(R.id.download_button);
        this.mAdView = (AdView) findViewById(R.id.ad_view);
        this.mAdView.loadAd(new Builder().build());
        this.mBackButton.setImageResource(R.drawable.ic_back);
        this.mBackButton.setOnClickListener(this);
        this.mDownloadButton.setVisibility(8);
        if (this.mType == 0) {
            this.mTitle.setText(this.mUserFeedData.getUsername());
        } else if (1 == this.mType) {
            this.mTitle.setText(this.mTagName);
        }
        initGridView();
        this.mDotsTextView = (DotsTextView) findViewById(R.id.dots);
        if (this.mType == 0) {
            if (this.mAdapter == null || this.mAdapter.getMediaFeedData() == null || this.mAdapter.getMediaFeedData().size() < 1) {
                getUserFeedMedia();
                this.mDotsTextView.showAndPlay();
                this.mDotsTextView.setVisibility(0);
            }
        } else if (1 != this.mType) {
        } else {
            if (this.mAdapter == null || this.mAdapter.getMediaFeedData() == null || this.mAdapter.getMediaFeedData().size() < 1) {
                getTagFeedMedia();
                this.mDotsTextView.showAndPlay();
                this.mDotsTextView.setVisibility(0);
            }
        }
    }

    private void initGridView() {
        this.mPullRefreshGridView = (PullToRefreshHeaderGridView) findViewById(R.id.user_gridview);
        this.mPullRefreshGridView.setMode(Mode.BOTH);
        this.mTimeLineGridView = (HeaderGridView) this.mPullRefreshGridView.getRefreshableView();
        this.mAdapter = new TimeLineGridViewAdapter(this);
        if (this.mType == 0) {
            this.mAdapter.setUserInfoClickFlag(true);
            this.mHeaderView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.user_main_header, null);
            initHeaderView();
            this.mTimeLineGridView.addHeaderView(this.mHeaderView);
            if (this.mTimelineResponse != null) {
                this.mAdapter.setMediaFeedData(this.mTimelineResponse.getItems());
            } else {
                this.mAdapter.setMediaFeedData(null);
            }
        } else if (1 == this.mType) {
            if (this.mTaglineResponse != null) {
                this.mAdapter.setMediaFeedData(this.mTaglineResponse.getItems());
            } else {
                this.mAdapter.setMediaFeedData(null);
            }
        }
        this.mTimeLineGridView.setAdapter(this.mAdapter);
        this.mPullRefreshGridView.setOnRefreshListener(this.mOnRefreshListener);
    }

    private void initHeaderView() {
        this.mUserAvatar = (CircleImageView) this.mHeaderView.findViewById(R.id.user_avatar);
        this.mFollowersCount = (TextView) this.mHeaderView.findViewById(R.id.followers_count);
        this.mFollowingsCount = (TextView) this.mHeaderView.findViewById(R.id.followings_count);
        this.mFollowersLayout = (LinearLayout) this.mHeaderView.findViewById(R.id.followers_layout);
        this.mFollowingsLayout = (LinearLayout) this.mHeaderView.findViewById(R.id.followings_layout);
        this.mDownloadAllButton = (Button) this.mHeaderView.findViewById(R.id.download_all_button);
        if (VLTools.inReview(this)) {
            this.mDownloadAllButton.setVisibility(8);
        }
        PicassoHelper.setImageView(this, this.mUserAvatar, this.mUserFeedData.getProfile_pic_url(), R.drawable.ic_profile);
        getUserInfoData();
        this.mFollowersLayout.setOnClickListener(this);
        this.mFollowingsLayout.setOnClickListener(this);
        this.mDownloadAllButton.setOnClickListener(this);
    }

    private void getUserInfoData() {
        InstagramService.getInstance().getUserDetail(this.mUserFeedData.getPk(), this.userinfoCallback);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (R.id.sidebar_button == id) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else if (R.id.followers_layout == id) {
            TrackHelper.track(TrackHelper.CATEGORY_USERMAIN, TrackHelper.ACTION_FOLLOWERS, "click");
            intent = new Intent(this, FollowlistActivity.class);
            intent.putExtra("type", 0);
            intent.putExtra(FollowlistActivity.EXTRA_USER_ID, this.mUserFeedData.getPk());
            intent.putExtra(FollowlistActivity.EXTRA_USER_NAME, this.mUserFeedData.getUsername());
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else if (R.id.followings_layout == id) {
            TrackHelper.track(TrackHelper.CATEGORY_USERMAIN, TrackHelper.ACTION_FOLLOWINGS, "click");
            intent = new Intent(this, FollowlistActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra(FollowlistActivity.EXTRA_USER_ID, this.mUserFeedData.getPk());
            intent.putExtra(FollowlistActivity.EXTRA_USER_NAME, this.mUserFeedData.getUsername());
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else if (R.id.download_all_button == id) {
            TrackHelper.track(TrackHelper.CATEGORY_USERMAIN, TrackHelper.ACTION_DOWNLOAD_ALL, "click");
            if (RateUsManager.getSingleton().needRateUs(this)) {
                this.mCustomDialog = new CustomDialog(this, getResources().getString(R.string.settings_rate_us_5_stars), getResources().getString(R.string.settings_rate_us_5_stars_content), this.mCustomDialogClickListener);
                this.mCustomDialog.setPositiveButtonText(getResources().getString(R.string.rate_us));
                this.mCustomDialog.show();
                return;
            }
            downloadAll();
        }
    }

    private void downloadAll() {
        if (this.mAdapter != null && this.mAdapter.getMediaFeedData() != null && this.mAdapter.getMediaFeedData().size() > 0) {
            for (PostItem mediaFeedData : this.mAdapter.getMediaFeedData()) {
                if (VideoHelper.isVideo(mediaFeedData)) {
                    ImageDownloader.getInstance(this).saveVideo(((ImageVersion) mediaFeedData.getVideo_versions().get(0)).getUrl());
                } else {
                    ImageDownloader.getInstance(this).downloadImage(((ImageVersion) mediaFeedData.getImage_versions().get(0)).getUrl());
                }
            }
        }
    }

    private void getUserFeedMedia() {
        InstagramService.getInstance().userFeed(this.mUserFeedData.getPk(), this.userfeedCallback);
    }

    private void loadMoreUserFeedMedia() {
        if (!this.mTimelineResponse.isMore_available() || TextUtils.isEmpty(this.mTimelineResponse.getNext_max_id())) {
            this.mPullRefreshGridView.onRefreshComplete();
        } else {
            InstagramService.getInstance().userFeed(this.mUserFeedData.getPk(), this.mTimelineResponse.getNext_max_id(), this.loadmorefeedCallback);
        }
    }

    private void getTagFeedMedia() {
        InstagramService.getInstance().tagLine(this.mTagName, this.tagfeedCallback);
    }

    private void loadMoreTagFeedMedia() {
        if (!this.mTaglineResponse.isMore_available() || TextUtils.isEmpty(this.mTaglineResponse.getNext_max_id())) {
            this.mPullRefreshGridView.onRefreshComplete();
        } else {
            InstagramService.getInstance().tagLine(this.mTagName, this.mTaglineResponse.getNext_max_id(), this.loadmoretagfeedCallback);
        }
    }
}
