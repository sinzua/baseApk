package com.ty.followboom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ty.followboom.adapters.FollowListViewAdapter;
import com.ty.followboom.models.TrackerManager;
import com.ty.followboom.models.UserInfoManager;
import com.ty.instaview.R;

public class TrackerFollowlistActivity extends Activity implements OnClickListener {
    public static final String EXTRA_TYPE = "type";
    private static final String TAG = "FollowlistActivity";
    public static final int TYPE_FANS = 4;
    public static final int TYPE_MUTUAL = 3;
    public static final int TYPE_NEW_GAIN = 0;
    public static final int TYPE_NEW_LOST = 1;
    public static final int TYPE_NON_FOLLOWERS = 2;
    private FollowListViewAdapter mAdapter;
    private ImageView mBackButton;
    private ImageView mDownloadButton;
    private ListView mFollowListView;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(TrackerFollowlistActivity.this, UserMainActivity.class);
            intent.putExtra("type", 0);
            intent.putExtra(UserMainActivity.EXTRA_USER_INFO, new Gson().toJson(TrackerFollowlistActivity.this.mAdapter.getItem(position - 1)));
            TrackerFollowlistActivity.this.startActivity(intent);
        }
    };
    private PullToRefreshListView mPullRefreshListView;
    private TextView mTitle;
    private int mType;
    private String mUserName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_follow_list);
        this.mType = getIntent().getIntExtra("type", 0);
        this.mUserName = UserInfoManager.getSingleton().getUserInfo(this).getUsername();
        onActivate();
    }

    private void onActivate() {
        this.mBackButton = (ImageView) findViewById(R.id.sidebar_button);
        this.mTitle = (TextView) findViewById(R.id.fragment_title);
        this.mDownloadButton = (ImageView) findViewById(R.id.download_button);
        this.mTitle.setText(this.mUserName);
        this.mBackButton.setImageResource(R.drawable.ic_back);
        this.mBackButton.setOnClickListener(this);
        this.mDownloadButton.setVisibility(8);
        initListView();
    }

    private void initListView() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.follow_listview);
        this.mPullRefreshListView.setMode(Mode.DISABLED);
        this.mFollowListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mFollowListView.setOnItemClickListener(this.mOnItemClickListener);
        this.mAdapter = new FollowListViewAdapter(this);
        if (this.mType == 0) {
            this.mAdapter.setUserFeedDataList(TrackerManager.getSingleton().getNewGainedFollowers());
        } else if (1 == this.mType) {
            this.mAdapter.setUserFeedDataList(TrackerManager.getSingleton().getNewLostFollowers());
        } else if (2 == this.mType) {
            this.mAdapter.setUserFeedDataList(TrackerManager.getSingleton().getNonFollowers());
        } else if (3 == this.mType) {
            this.mAdapter.setUserFeedDataList(TrackerManager.getSingleton().getMutualFriends());
        } else if (4 == this.mType) {
            this.mAdapter.setUserFeedDataList(TrackerManager.getSingleton().getFans());
        }
        this.mFollowListView.setAdapter(this.mAdapter);
    }

    public void onClick(View view) {
        if (R.id.sidebar_button == view.getId()) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
}
