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
import com.ty.followboom.adapters.OrderStatusListViewAdapter;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.OrderStatusResponse;
import com.ty.instaview.R;

public class OrderStatusActivity extends Activity implements OnClickListener {
    private static final String TAG = "OrderStatusActivity";
    private OrderStatusListViewAdapter mAdapter;
    private ImageView mBackButton;
    private ImageView mDownloadButton;
    private ListView mFollowListView;
    private LinearLayout mLoadingFailedView;
    private LinearLayout mLoadingItem;
    private LinearLayout mLoadingView;
    private OnRefreshListener2<ListView> mOnRefreshListener = new OnRefreshListener2<ListView>() {
        public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            OrderStatusActivity.this.getOrderStatus();
        }

        public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        }
    };
    private RequestCallback<OrderStatusResponse> mOrderStatusCallback = new RequestCallback<OrderStatusResponse>() {
        public void onResponse(OrderStatusResponse orderStatusResponse) {
            OrderStatusActivity.this.hideLoadingView();
            OrderStatusActivity.this.mPullRefreshListView.onRefreshComplete();
            if (orderStatusResponse.isSuccessful()) {
                OrderStatusActivity.this.mOrderStatusResponse = orderStatusResponse;
                OrderStatusActivity.this.mAdapter.setOrderStatus(OrderStatusActivity.this.mOrderStatusResponse.getData().getOrderStatus());
                OrderStatusActivity.this.mAdapter.notifyDataSetChanged();
            } else if (orderStatusResponse.isSessionExpired()) {
                VLTools.gotoLogin(OrderStatusActivity.this);
            } else {
                OrderStatusActivity.this.showLoadingFailedView();
            }
        }

        public void onFailure(Exception e) {
            Log.d(OrderStatusActivity.TAG, "GetOrderStatusRequest Exception: " + e.toString());
        }
    };
    private OrderStatusResponse mOrderStatusResponse;
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

    private void initLoadingViews() {
        this.mLoadingView = (LinearLayout) findViewById(R.id.loading_view);
        this.mLoadingItem = (LinearLayout) findViewById(R.id.loading_item);
        this.mLoadingFailedView = (LinearLayout) findViewById(R.id.loading_failed);
        this.mLoadingFailedView.setOnClickListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_order_status);
        TrackHelper.track(TrackHelper.CATEGORY_ORDER_STATUS, "show", "");
        onTitle();
        initLoadingViews();
        onActivate();
        getOrderStatus();
    }

    private void getOrderStatus() {
        LikeServerInstagram.getSingleton().getOrderStatus(this, this.mOrderStatusCallback);
    }

    private void onTitle() {
        this.mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        this.mBackButton = (ImageView) findViewById(R.id.sidebar_button);
        this.mTitle = (TextView) findViewById(R.id.fragment_title);
        this.mDownloadButton = (ImageView) findViewById(R.id.download_button);
        this.mTitle.setText("Order Progress");
        this.mTitleBar.setBackgroundColor(getResources().getColor(R.color.app_theme));
        this.mBackButton.setImageResource(R.drawable.ic_back);
        this.mBackButton.setOnClickListener(this);
        this.mDownloadButton.setVisibility(8);
    }

    private void onActivate() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.order_status_listview);
        this.mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        this.mFollowListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mAdapter = new OrderStatusListViewAdapter(this);
        if (this.mOrderStatusResponse != null) {
            this.mAdapter.setOrderStatus(this.mOrderStatusResponse.getData().getOrderStatus());
        } else {
            this.mAdapter.setOrderStatus(null);
        }
        this.mFollowListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setOnRefreshListener(this.mOnRefreshListener);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (R.id.sidebar_button == id) {
            TrackHelper.track(TrackHelper.CATEGORY_ORDER_STATUS, TrackHelper.ACTION_BACK, "click");
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else if (R.id.loading_failed == id) {
            getOrderStatus();
        }
    }
}
