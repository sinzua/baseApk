package com.ty.followboom;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.TrackerManager;
import com.ty.http.RequestCallback;
import com.ty.http.responses.UserinfoResponse;
import com.ty.instagramapi.InstagramService;
import com.ty.instaview.R;

public class TrackerFragment extends BaseFragment implements OnClickListener {
    public static final int GET_FOLLOW_SUCCEED = 0;
    private static final String TAG = "GetCoinsFragment";
    private Handler mActionHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    TrackerFragment.this.mNewGainProgress.setVisibility(8);
                    TrackerFragment.this.mNewLostProgress.setVisibility(8);
                    TrackerFragment.this.mNonFollowersProgress.setVisibility(8);
                    TrackerFragment.this.mMutualProgress.setVisibility(8);
                    TrackerFragment.this.mFansProgress.setVisibility(8);
                    TrackerFragment.this.mNewGainCount.setText("" + TrackerManager.getSingleton().getNewGainedFollowers().size());
                    TrackerFragment.this.mNewLostCount.setText("" + TrackerManager.getSingleton().getNewLostFollowers().size());
                    TrackerFragment.this.mNonFollowersCount.setText("" + TrackerManager.getSingleton().getNonFollowers().size());
                    TrackerFragment.this.mMutualCount.setText("" + TrackerManager.getSingleton().getMutualFriends().size());
                    TrackerFragment.this.mFansCount.setText("" + TrackerManager.getSingleton().getFans().size());
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mFansCount;
    private LinearLayout mFansLayout;
    private ProgressBar mFansProgress;
    private TextView mFollowersCount;
    private TextView mFollowingsCount;
    private TextView mMutualCount;
    private LinearLayout mMutualLayout;
    private ProgressBar mMutualProgress;
    private TextView mNewGainCount;
    private LinearLayout mNewGainLayout;
    private ProgressBar mNewGainProgress;
    private TextView mNewLostCount;
    private LinearLayout mNewLostLayout;
    private ProgressBar mNewLostProgress;
    private TextView mNonFollowersCount;
    private LinearLayout mNonFollowersLayout;
    private ProgressBar mNonFollowersProgress;
    private TextView mPostsCount;
    private RequestCallback<UserinfoResponse> mProfileCallBack = new RequestCallback<UserinfoResponse>() {
        public void onResponse(UserinfoResponse userinfoResponse) {
            if (userinfoResponse.isSuccessful()) {
                TrackerFragment.this.mPostsCount.setText("" + userinfoResponse.getUser().getMedia_count());
                TrackerFragment.this.mFollowersCount.setText("" + userinfoResponse.getUser().getFollower_count());
                TrackerFragment.this.mFollowingsCount.setText("" + userinfoResponse.getUser().getFollowing_count());
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private Button mRefresh;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track(TrackHelper.CATEGORY_TRACKER, "show", "");
        this.mRootView = inflater.inflate(R.layout.fragment_tracker, container, false);
        onActivate(this.mRootView);
        refreshContentView(false);
        return this.mRootView;
    }

    private void onActivate(View v) {
        this.mPostsCount = (TextView) v.findViewById(R.id.posts_count);
        this.mFollowersCount = (TextView) v.findViewById(R.id.followers_count);
        this.mFollowingsCount = (TextView) v.findViewById(R.id.followings_count);
        this.mNewGainLayout = (LinearLayout) v.findViewById(R.id.new_gain_layout);
        this.mNewLostLayout = (LinearLayout) v.findViewById(R.id.new_lost_layout);
        this.mNonFollowersLayout = (LinearLayout) v.findViewById(R.id.non_followers_layout);
        this.mMutualLayout = (LinearLayout) v.findViewById(R.id.mutual_layout);
        this.mFansLayout = (LinearLayout) v.findViewById(R.id.fans_layout);
        this.mNewGainCount = (TextView) v.findViewById(R.id.new_gain_count);
        this.mNewLostCount = (TextView) v.findViewById(R.id.new_lost_count);
        this.mNonFollowersCount = (TextView) v.findViewById(R.id.non_followers_count);
        this.mMutualCount = (TextView) v.findViewById(R.id.mutual_count);
        this.mFansCount = (TextView) v.findViewById(R.id.fans_count);
        this.mRefresh = (Button) v.findViewById(R.id.refresh);
        this.mRefresh.setOnClickListener(this);
        this.mNewGainCount.setText("");
        this.mNewLostCount.setText("");
        this.mNonFollowersCount.setText("");
        this.mMutualCount.setText("");
        this.mFansCount.setText("");
        this.mNewGainProgress = (ProgressBar) v.findViewById(R.id.new_gain_progress);
        this.mNewLostProgress = (ProgressBar) v.findViewById(R.id.new_lost_progress);
        this.mNonFollowersProgress = (ProgressBar) v.findViewById(R.id.non_followers_progress);
        this.mMutualProgress = (ProgressBar) v.findViewById(R.id.mutual_progress);
        this.mFansProgress = (ProgressBar) v.findViewById(R.id.fans_progress);
        this.mNewGainLayout.setOnClickListener(this);
        this.mNewLostLayout.setOnClickListener(this);
        this.mNonFollowersLayout.setOnClickListener(this);
        this.mMutualLayout.setOnClickListener(this);
        this.mFansLayout.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.refresh:
                refreshContentView(true);
                return;
            case R.id.new_gain_layout:
                VLTools.gotoTrackerFollowlistActivity(getActivity(), 0);
                return;
            case R.id.new_lost_layout:
                VLTools.gotoTrackerFollowlistActivity(getActivity(), 1);
                return;
            case R.id.non_followers_layout:
                VLTools.gotoTrackerFollowlistActivity(getActivity(), 2);
                return;
            case R.id.mutual_layout:
                VLTools.gotoTrackerFollowlistActivity(getActivity(), 3);
                return;
            case R.id.fans_layout:
                VLTools.gotoTrackerFollowlistActivity(getActivity(), 4);
                return;
            default:
                return;
        }
    }

    private void initLoadingView() {
        this.mNewGainProgress.setVisibility(0);
        this.mNewLostProgress.setVisibility(0);
        this.mNonFollowersProgress.setVisibility(0);
        this.mMutualProgress.setVisibility(0);
        this.mFansProgress.setVisibility(0);
        this.mNewGainCount.setText("");
        this.mNewLostCount.setText("");
        this.mNonFollowersCount.setText("");
        this.mMutualCount.setText("");
        this.mFansCount.setText("");
    }

    private void refreshContentView(boolean forceRefresh) {
        initLoadingView();
        InstagramService.getInstance().getUserDetail("" + InstagramService.getInstance().getUserInfo().getUserid(), this.mProfileCallBack);
        TrackerManager.getSingleton().init(this.mActionHandler, forceRefresh);
    }
}
