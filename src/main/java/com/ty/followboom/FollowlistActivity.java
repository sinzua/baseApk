package com.ty.followboom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ty.followboom.adapters.FollowListViewAdapter;
import com.ty.http.RequestCallback;
import com.ty.http.responses.FollowResponse;
import com.ty.instagramapi.InstagramService;
import com.ty.instaview.R;
import pl.tajchert.sample.DotsTextView;

public class FollowlistActivity extends Activity implements OnClickListener {
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_USER_NAME = "user_name";
    private static final String TAG = "FollowlistActivity";
    public static final int TYPE_FOLLOWERS = 0;
    public static final int TYPE_FOLLOWINGS = 1;
    private RequestCallback<FollowResponse> followCallback = new RequestCallback<FollowResponse>() {
        public void onResponse(FollowResponse followResponse) {
            FollowlistActivity.this.mDotsTextView.hideAndStop();
            FollowlistActivity.this.mDotsTextView.setVisibility(8);
            FollowlistActivity.this.mPullRefreshListView.onRefreshComplete();
            FollowlistActivity.this.mFollowResponse = followResponse;
            if (FollowlistActivity.this.mFollowResponse != null) {
                FollowlistActivity.this.mAdapter.setUserFeedDataList(FollowlistActivity.this.mFollowResponse.getUsers());
                FollowlistActivity.this.mAdapter.notifyDataSetChanged();
            }
        }

        public void onFailure(Exception e) {
            FollowlistActivity.this.mDotsTextView.hideAndStop();
            FollowlistActivity.this.mDotsTextView.setVisibility(8);
            FollowlistActivity.this.mPullRefreshListView.onRefreshComplete();
        }
    };
    private RequestCallback<FollowResponse> followmoreCallback = new RequestCallback<FollowResponse>() {
        public void onResponse(FollowResponse followResponse) {
            FollowlistActivity.this.mDotsTextView.hideAndStop();
            FollowlistActivity.this.mDotsTextView.setVisibility(8);
            FollowlistActivity.this.mPullRefreshListView.onRefreshComplete();
            FollowlistActivity.this.mFollowResponse = followResponse;
            if (FollowlistActivity.this.mFollowResponse != null) {
                FollowlistActivity.this.mAdapter.addToUserFeedDataList(FollowlistActivity.this.mFollowResponse.getUsers());
                FollowlistActivity.this.mAdapter.notifyDataSetChanged();
            }
        }

        public void onFailure(Exception e) {
            FollowlistActivity.this.mDotsTextView.hideAndStop();
            FollowlistActivity.this.mDotsTextView.setVisibility(8);
            FollowlistActivity.this.mPullRefreshListView.onRefreshComplete();
        }
    };
    private FollowListViewAdapter mAdapter;
    private ImageView mBackButton;
    private DotsTextView mDotsTextView;
    private ImageView mDownloadButton;
    private ListView mFollowListView;
    private FollowResponse mFollowResponse;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(FollowlistActivity.this, UserMainActivity.class);
            intent.putExtra("type", 0);
            intent.putExtra(UserMainActivity.EXTRA_USER_INFO, new Gson().toJson(FollowlistActivity.this.mAdapter.getItem(position - 1)));
            FollowlistActivity.this.startActivity(intent);
        }
    };
    private OnRefreshListener2<ListView> mOnRefreshListener = new OnRefreshListener2<ListView>() {
        public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            FollowlistActivity.this.getFollowList();
        }

        public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            FollowlistActivity.this.loadMoreFollowList();
        }
    };
    private PullToRefreshListView mPullRefreshListView;
    private TextView mTitle;
    private int mType;
    private String mUserId;
    private String mUserName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_follow_list);
        this.mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
        this.mUserName = getIntent().getStringExtra(EXTRA_USER_NAME);
        this.mType = getIntent().getIntExtra("type", 0);
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
        this.mDotsTextView = (DotsTextView) findViewById(R.id.dots);
        if (this.mFollowResponse == null || this.mFollowResponse.getUsers() == null || this.mFollowResponse.getUsers().size() < 1) {
            getFollowList();
            this.mDotsTextView.showAndPlay();
            this.mDotsTextView.setVisibility(0);
        }
        initListView();
    }

    private void initListView() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.follow_listview);
        this.mPullRefreshListView.setMode(Mode.BOTH);
        this.mFollowListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mFollowListView.setOnItemClickListener(this.mOnItemClickListener);
        this.mAdapter = new FollowListViewAdapter(this);
        if (this.mFollowResponse != null) {
            this.mAdapter.setUserFeedDataList(this.mFollowResponse.getUsers());
        } else {
            this.mAdapter.setUserFeedDataList(null);
        }
        this.mFollowListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setOnRefreshListener(this.mOnRefreshListener);
    }

    private void getFollowList() {
        if (this.mType == 0) {
            InstagramService.getInstance().followers(this.mUserId, this.followCallback);
        } else {
            InstagramService.getInstance().following(this.mUserId, this.followCallback);
        }
    }

    private void loadMoreFollowList() {
        if (this.mFollowResponse.isBig_list() && !TextUtils.isEmpty(this.mFollowResponse.getNext_max_id())) {
            if (this.mType == 0) {
                InstagramService.getInstance().followers(this.mUserId, this.mFollowResponse.getNext_max_id(), this.followmoreCallback);
            } else {
                InstagramService.getInstance().following(this.mUserId, this.mFollowResponse.getNext_max_id(), this.followmoreCallback);
            }
        }
    }

    public void onClick(View view) {
        if (R.id.sidebar_button == view.getId()) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
}
