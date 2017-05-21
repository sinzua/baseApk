package com.ty.followboom;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ty.followboom.adapters.CoinsHistoryListViewAdapter;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.CoinsHistoryResponse;
import com.ty.instaview.R;

public class CoinsHistoryActivity extends Activity implements OnClickListener {
    private static final String TAG = "CoinsHistoryActivity";
    private CoinsHistoryListViewAdapter mAdapter;
    private ImageView mBackButton;
    private RequestCallback<CoinsHistoryResponse> mCoinsHistoryCallback = new RequestCallback<CoinsHistoryResponse>() {
        public void onResponse(CoinsHistoryResponse coinsHistoryResponse) {
            CoinsHistoryActivity.this.hideLoadingView();
            CoinsHistoryActivity.this.mPullRefreshListView.onRefreshComplete();
            if (coinsHistoryResponse.isSuccessful()) {
                CoinsHistoryActivity.this.mCoinsHistoryResponse = coinsHistoryResponse;
                CoinsHistoryActivity.this.mAdapter.setCoinsHistory(CoinsHistoryActivity.this.mCoinsHistoryResponse.getData().getCoinHistory());
                CoinsHistoryActivity.this.mAdapter.notifyDataSetChanged();
            } else if (coinsHistoryResponse.isSessionExpired()) {
                VLTools.gotoLogin(CoinsHistoryActivity.this);
            } else {
                CoinsHistoryActivity.this.showLoadingFailedView();
            }
        }

        public void onFailure(Exception e) {
            Log.d(CoinsHistoryActivity.TAG, "GetCoinsHistoryRequest Exception: " + e.getMessage());
        }
    };
    private CoinsHistoryResponse mCoinsHistoryResponse;
    private ImageView mDownloadButton;
    private ListView mFollowListView;
    private LinearLayout mLoadingFailedView;
    private LinearLayout mLoadingItem;
    private LinearLayout mLoadingView;
    private OnRefreshListener2<ListView> mOnRefreshListener = new OnRefreshListener2<ListView>() {
        public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            CoinsHistoryActivity.this.getCoinsHistory();
        }

        public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        }
    };
    private PullToRefreshListView mPullRefreshListView;
    private TextView mTitle;
    private RelativeLayout mTitleBar;

    private void showLoadingView() {
        VLTools.showLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
    }

    private void hideLoadingView() {
        VLTools.hideLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
    }

    private void showLoadingFailedView() {
        VLTools.showLoadingFailedView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
    }

    private void getCoinsHistory() {
        LikeServerInstagram.getSingleton().getCoinsHistory(this, this.mCoinsHistoryCallback);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_coins_history);
        TrackHelper.track(TrackHelper.CATEGORY_COINS_HISTORY, "show", "");
        initLoadingViews();
        onTitle();
        onActivate();
        getCoinsHistory();
    }

    private void initLoadingViews() {
        this.mLoadingView = (LinearLayout) findViewById(R.id.loading_view);
        this.mLoadingItem = (LinearLayout) findViewById(R.id.loading_item);
        this.mLoadingFailedView = (LinearLayout) findViewById(R.id.loading_failed);
        this.mLoadingFailedView.setOnClickListener(this);
    }

    private void onTitle() {
        this.mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        this.mBackButton = (ImageView) findViewById(R.id.sidebar_button);
        this.mTitle = (TextView) findViewById(R.id.fragment_title);
        this.mDownloadButton = (ImageView) findViewById(R.id.download_button);
        this.mTitle.setText("Coins History");
        this.mTitleBar.setBackgroundColor(getResources().getColor(R.color.app_theme));
        this.mBackButton.setImageResource(R.drawable.ic_back);
        this.mBackButton.setOnClickListener(this);
        this.mDownloadButton.setVisibility(8);
    }

    private void onActivate() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.coins_history_listview);
        this.mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        this.mFollowListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mAdapter = new CoinsHistoryListViewAdapter(this);
        if (this.mCoinsHistoryResponse != null) {
            this.mAdapter.setCoinsHistory(this.mCoinsHistoryResponse.getData().getCoinHistory());
        } else {
            this.mAdapter.setCoinsHistory(null);
        }
        this.mFollowListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setOnRefreshListener(this.mOnRefreshListener);
    }

    public void onClick(View view) {
        if (R.id.sidebar_button == view.getId()) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
}
