package com.ty.followboom;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.ty.entities.PostItem;
import com.ty.followboom.adapters.PostGridViewAdapter;
import com.ty.followboom.helpers.MeSingleton;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.UserInfoManager;
import com.ty.http.RequestCallback;
import com.ty.http.responses.TimelineResponse;
import com.ty.instagramapi.InstagramService;
import com.ty.instaview.R;
import java.util.List;

public class PostFragment extends BaseFragment implements OnClickListener, OnItemClickListener {
    private static final String TAG = "PostFragment";
    private RequestCallback<TimelineResponse> loadmoretimelineCallback = new RequestCallback<TimelineResponse>() {
        public void onResponse(TimelineResponse timelineResponse) {
            VLTools.hideLoadingView(PostFragment.this.mLoadingView, PostFragment.this.mLoadingItem, PostFragment.this.mLoadingFailedView);
            PostFragment.this.mPullRefreshGridView.onRefreshComplete();
            if (timelineResponse != null && timelineResponse.isSuccessful()) {
                MeSingleton.getSingleton().setTimelineResponse(timelineResponse);
                PostFragment.this.mAdapter.addToPosts(MeSingleton.getSingleton().getTimelineResponse().getItems());
                PostFragment.this.mAdapter.notifyDataSetChanged();
                MeSingleton.getSingleton().setMyPost(PostFragment.this.mAdapter.getPosts());
            } else if (timelineResponse == null || !timelineResponse.needLogin()) {
                VLTools.showLoadingFailedView(PostFragment.this.mLoadingView, PostFragment.this.mLoadingItem, PostFragment.this.mLoadingFailedView);
            } else {
                VLTools.gotoLogin(PostFragment.this.getActivity());
            }
        }

        public void onFailure(Exception e) {
            VLTools.showLoadingFailedView(PostFragment.this.mLoadingView, PostFragment.this.mLoadingItem, PostFragment.this.mLoadingFailedView);
            PostFragment.this.mPullRefreshGridView.onRefreshComplete();
        }
    };
    protected PostGridViewAdapter mAdapter;
    private LinearLayout mLoadingFailedView;
    private LinearLayout mLoadingItem;
    private LinearLayout mLoadingView;
    private OnRefreshListener2<GridView> mOnRefreshListener = new OnRefreshListener2<GridView>() {
        public void onPullDownToRefresh(PullToRefreshBase<GridView> pullToRefreshBase) {
        }

        public void onPullUpToRefresh(PullToRefreshBase<GridView> pullToRefreshBase) {
            TrackHelper.track(TrackHelper.CATEGORY_GET_LIKES, TrackHelper.ACTION_LOADMORE, "click");
            PostFragment.this.loadMoreUserTimeLine();
        }
    };
    private PullToRefreshGridView mPullRefreshGridView;
    private GridView mTimeLineGridView;
    protected TextView mTitle;
    private RequestCallback<TimelineResponse> timelineCallback = new RequestCallback<TimelineResponse>() {
        public void onResponse(TimelineResponse timelineResponse) {
            VLTools.hideLoadingView(PostFragment.this.mLoadingView, PostFragment.this.mLoadingItem, PostFragment.this.mLoadingFailedView);
            PostFragment.this.mPullRefreshGridView.onRefreshComplete();
            if (timelineResponse != null && timelineResponse.isSuccessful()) {
                MeSingleton.getSingleton().setTimelineResponse(timelineResponse);
                PostFragment.this.mAdapter.setPosts(MeSingleton.getSingleton().getTimelineResponse().getItems());
                PostFragment.this.mAdapter.notifyDataSetChanged();
                MeSingleton.getSingleton().setMyPost(PostFragment.this.mAdapter.getPosts());
            } else if (timelineResponse == null || !timelineResponse.needLogin()) {
                VLTools.showLoadingFailedView(PostFragment.this.mLoadingView, PostFragment.this.mLoadingItem, PostFragment.this.mLoadingFailedView);
            } else {
                VLTools.gotoLogin(PostFragment.this.getActivity());
            }
        }

        public void onFailure(Exception e) {
            VLTools.showLoadingFailedView(PostFragment.this.mLoadingView, PostFragment.this.mLoadingItem, PostFragment.this.mLoadingFailedView);
            PostFragment.this.mPullRefreshGridView.onRefreshComplete();
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track(TrackHelper.CATEGORY_GET_LIKES, "show", "");
        this.mRootView = inflater.inflate(R.layout.fragment_get_likes, container, false);
        initTitleViews();
        onActivate(this.mRootView);
        return this.mRootView;
    }

    protected void onActivate(View v) {
        this.mLoadingView = (LinearLayout) v.findViewById(R.id.loading_view);
        this.mLoadingItem = (LinearLayout) v.findViewById(R.id.loading_item);
        this.mLoadingFailedView = (LinearLayout) v.findViewById(R.id.loading_failed);
        this.mPullRefreshGridView = (PullToRefreshGridView) v.findViewById(R.id.timeline_gridview);
        this.mTitle = (TextView) v.findViewById(R.id.title);
        this.mPullRefreshGridView.setMode(Mode.PULL_FROM_END);
        this.mTimeLineGridView = (GridView) this.mPullRefreshGridView.getRefreshableView();
        List<PostItem> posts = MeSingleton.getSingleton().getMyPost();
        if (posts != null) {
            this.mAdapter.setPosts(posts);
        } else {
            this.mAdapter.setPosts(null);
        }
        this.mTimeLineGridView.setAdapter(this.mAdapter);
        this.mPullRefreshGridView.setOnRefreshListener(this.mOnRefreshListener);
        this.mLoadingFailedView.setOnClickListener(this);
        this.mTimeLineGridView.setOnItemClickListener(this);
        if (posts == null) {
            getUserTimeLine();
        }
    }

    public void onResume() {
        super.onResume();
    }

    private void getUserTimeLine() {
        try {
            InstagramService.getInstance().userFeed(Long.toString(InstagramService.getInstance().getUserInfo().getUserid().longValue()), this.timelineCallback);
            VLTools.showLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
        } catch (Exception e) {
            UserInfoManager.getSingleton().logout(getActivity());
            VLTools.gotoLogin(getActivity());
        }
    }

    private void loadMoreUserTimeLine() {
        if (!MeSingleton.getSingleton().getTimelineResponse().isMore_available() || TextUtils.isEmpty(MeSingleton.getSingleton().getTimelineResponse().getNext_max_id())) {
            this.mPullRefreshGridView.onRefreshComplete();
            return;
        }
        InstagramService.getInstance().userFeed(Long.toString(InstagramService.getInstance().getUserInfo().getUserid().longValue()), MeSingleton.getSingleton().getTimelineResponse().getNext_max_id(), this.loadmoretimelineCallback);
        TrackHelper.track(TrackHelper.CATEGORY_GET_LIKES, TrackHelper.ACTION_LOADMORE, "");
    }

    public void onClick(View view) {
        super.onClick(view);
        if (R.id.loading_failed == view.getId()) {
            getUserTimeLine();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TrackHelper.track(TrackHelper.CATEGORY_GET_LIKES, TrackHelper.ACTION_ITEM, "click");
        VLTools.gotoPostActivity(getActivity(), this.mAdapter.getItem(position), 2);
    }
}
