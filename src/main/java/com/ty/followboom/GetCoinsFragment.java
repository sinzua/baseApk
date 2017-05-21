package com.ty.followboom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.forwardwin.base.widgets.ToastHelper;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.ty.followboom.entities.Order;
import com.ty.followboom.entities.UserInfo;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.CoinsManager;
import com.ty.followboom.models.GetCoinsManager;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.models.NativeXManager;
import com.ty.followboom.models.RateUsManager;
import com.ty.followboom.models.SupersonicManager;
import com.ty.followboom.models.UserInfoManager;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.BasicResponse;
import com.ty.followboom.okhttp.responses.BoardResponse;
import com.ty.followboom.views.DoFollowView;
import com.ty.followboom.views.DoLikeView;
import com.ty.followboom.views.DoMixView;
import com.ty.followboom.views.FreeCoinsCustomDialog;
import com.ty.followboom.views.RateUsCustomDialog;
import com.ty.http.responses.UserinfoResponse;
import com.ty.instagramapi.InstagramService;
import com.ty.instaview.R;
import java.util.ArrayList;
import java.util.Iterator;

public class GetCoinsFragment extends BaseFragment implements OnClickListener {
    private static final int CONTENT_VIEW_INDEX = 1;
    private static final int DO = 0;
    private static final int[] FOLLOW_LIMIT_NOTIFICATION = new int[]{R.string.follow_limit_this_minute_toast, R.string.follow_limit_this_hour_toast, R.string.follow_limit_today_toast};
    private static final int[] LIKE_LIMIT_NOTIFICATION = new int[]{R.string.like_limit_this_minute_toast, R.string.like_limit_this_hour_toast, R.string.like_limit_today_toast};
    private static final int SKIP = 1;
    private static final String TAG = "GetCoinsFragment";
    private RequestCallback<BoardResponse> getBoardCallback = new RequestCallback<BoardResponse>() {
        public void onResponse(BoardResponse boardResponse) {
            GetCoinsFragment.this.hideLoadingView();
            if (boardResponse.isSuccessful()) {
                GetCoinsManager.getSingleton().setCurBoardDataMap(GetCoinsFragment.this.mCurType, boardResponse);
                GetCoinsFragment.this.refreshContentView();
            } else if (boardResponse.isSessionExpired()) {
                VLTools.gotoLogin(GetCoinsFragment.this.getActivity());
            } else {
                GetCoinsFragment.this.showLoadingFailedView();
            }
        }

        public void onFailure(Exception e) {
            Log.d("GetCoinsFragment", "GetBoard failed with message: " + e.toString());
            GetCoinsFragment.this.showLoadingFailedView();
        }
    };
    private AdView mAdView;
    private LinearLayout mContent;
    private LinearLayout mContentEmptyView;
    private int mCurType;
    private RateUsCustomDialog mCustomDialog;
    private OnClickListener mCustomDialogClickListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.positive_button == id) {
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_RATE_US, "click");
                GetCoinsFragment.this.mCustomDialog.dismiss();
                VLTools.rateUs(GetCoinsFragment.this.getActivity());
                RateUsManager.getSingleton().trigger();
            } else if (R.id.negative_button == id) {
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_EMAIL_US, "click");
                GetCoinsFragment.this.mCustomDialog.dismiss();
                VLTools.emailUs(GetCoinsFragment.this.getActivity());
                RateUsManager.getSingleton().forceRecord(GetCoinsFragment.this.getActivity());
            }
        }
    };
    private ToggleButton mDo;
    private ToggleButton mFollowToggleButton;
    private FreeCoinsCustomDialog mFreeCoinsDialog;
    private OnClickListener mFreeCoinsDialogClickListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.ll_ss == id) {
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_SUPERSONIC, "click");
                SupersonicManager.getSingleton().show();
            } else if (R.id.ll_nativex == id) {
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_NATIVEX, "click");
                NativeXManager.getSingleton().show();
            }
        }
    };
    private ToggleButton mLikeToggleButton;
    private LinearLayout mLoadingFailedView;
    private LinearLayout mLoadingItem;
    private LinearLayout mLoadingView;
    private ToggleButton mMixToggleButton;
    private com.ty.http.RequestCallback<UserinfoResponse> mProfileCallBack = new com.ty.http.RequestCallback<UserinfoResponse>() {
        public void onResponse(UserinfoResponse userinfoResponse) {
            if (userinfoResponse.isSuccessful()) {
                UserInfo userInfo = UserInfoManager.getSingleton().getUserInfo(GetCoinsFragment.this.getActivity());
                userInfo.setPostsCount((long) userinfoResponse.getUser().getMedia_count());
                userInfo.setFollowersCount((long) userinfoResponse.getUser().getFollower_count());
                userInfo.setFollowingsCount((long) userinfoResponse.getUser().getFollowing_count());
                userInfo.setAvatarUrl(userinfoResponse.getUser().getProfile_pic_url());
                UserInfoManager.getSingleton().save(GetCoinsFragment.this.getActivity(), userInfo);
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private ToggleButton mSkip;
    private long mTimeStamp;
    private RequestCallback<BasicResponse> trackActionCallback = new RequestCallback<BasicResponse>() {
        public void onResponse(BasicResponse basicResponse) {
            if (basicResponse.isSuccessful()) {
                Log.d("GetCoinsFragment", "Track action succeed!");
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TRACK, TrackHelper.LABEL_SUCCEED);
            } else if (basicResponse.isSessionExpired()) {
                VLTools.gotoLogin(GetCoinsFragment.this.getActivity());
            } else {
                Log.d("GetCoinsFragment", "Track action failed!");
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TRACK, "failed");
            }
        }

        public void onFailure(Exception e) {
            Log.d("GetCoinsFragment", "Track action failed with msg: " + e.toString());
            TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TRACK, "failed");
        }
    };
    private View v;

    class FollowCallBack extends com.ty.http.RequestCallback<com.ty.http.responses.BasicResponse> {
        private long mOrderId;
        private long mTargetUserId;

        public FollowCallBack(long orderId, long targetUserId) {
            this.mOrderId = orderId;
            this.mTargetUserId = targetUserId;
        }

        public void onResponse(com.ty.http.responses.BasicResponse basicResponse) {
            if (basicResponse.isSuccessful()) {
                Log.d("GetCoinsFragment", "follow succeed");
                TrackHelper.track("GetCoinsFragment", "follow", TrackHelper.LABEL_SUCCEED);
                CoinsManager.getSingleton().increment(1);
                GetCoinsFragment.this.trackAction(0, this.mOrderId);
                return;
            }
            ToastHelper.showToast(GetCoinsFragment.this.getActivity(), "Can't add coins: " + basicResponse.getMessage());
        }

        public void onFailure(Exception e) {
            Log.d("GetCoinsFragment", "follow failed");
            TrackHelper.track("GetCoinsFragment", "follow", "failed");
        }
    }

    class LikeCallBack extends com.ty.http.RequestCallback<com.ty.http.responses.BasicResponse> {
        private long mOrderId;
        private long mPostId;

        public LikeCallBack(long orderId, long postId) {
            this.mOrderId = orderId;
            this.mPostId = postId;
        }

        public void onResponse(com.ty.http.responses.BasicResponse basicResponse) {
            if (basicResponse.isSuccessful()) {
                Log.d("GetCoinsFragment", "like succeed");
                TrackHelper.track("GetCoinsFragment", "like", TrackHelper.LABEL_SUCCEED);
                CoinsManager.getSingleton().increment(0);
                GetCoinsFragment.this.trackAction(0, this.mOrderId);
                return;
            }
            ToastHelper.showToast(GetCoinsFragment.this.getActivity(), "Can't add coins: " + basicResponse.getMessage());
        }

        public void onFailure(Exception e) {
            Log.d("GetCoinsFragment", "like failed");
            TrackHelper.track("GetCoinsFragment", "like", "failed");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track("GetCoinsFragment", "show", "");
        this.mRootView = inflater.inflate(R.layout.fragment_get_coins, container, false);
        onActivate(this.mRootView);
        return this.mRootView;
    }

    private void onActivate(View v) {
        initLoadingViews(v);
        initTitleViews();
        initContentViews(v);
        this.mAdView = (AdView) v.findViewById(R.id.ad_view);
        this.mAdView.loadAd(new Builder().build());
        InstagramService.getInstance().getUserDetail("" + InstagramService.getInstance().getUserInfo().getUserid(), this.mProfileCallBack);
        if (!GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
            getBoardList();
        }
    }

    private void initContentViews(View v) {
        this.mContentEmptyView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, null);
        this.mContent = (LinearLayout) v.findViewById(R.id.content);
        this.mLikeToggleButton = (ToggleButton) v.findViewById(R.id.tb_like);
        this.mMixToggleButton = (ToggleButton) v.findViewById(R.id.tb_mix);
        this.mFollowToggleButton = (ToggleButton) v.findViewById(R.id.tb_follow);
        this.mSkip = (ToggleButton) v.findViewById(R.id.skip);
        this.mDo = (ToggleButton) v.findViewById(R.id.positive);
        this.mMixToggleButton.setVisibility(8);
        this.mLikeToggleButton.setOnClickListener(this);
        this.mMixToggleButton.setOnClickListener(this);
        this.mFollowToggleButton.setOnClickListener(this);
        this.mSkip.setOnClickListener(this);
        this.mDo.setOnClickListener(this);
        unCheckedAllButtons();
        if (this.mCurType == 0) {
            this.mLikeToggleButton.setChecked(true);
            this.mLikeToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        } else if (2 == this.mCurType) {
            this.mMixToggleButton.setChecked(true);
            this.mMixToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        } else if (1 == this.mCurType) {
            this.mFollowToggleButton.setChecked(true);
            this.mFollowToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        }
    }

    private void initLoadingViews(View v) {
        this.mLoadingView = (LinearLayout) v.findViewById(R.id.loading_view);
        this.mLoadingItem = (LinearLayout) v.findViewById(R.id.loading_item);
        this.mLoadingFailedView = (LinearLayout) v.findViewById(R.id.loading_failed);
        this.mLoadingFailedView.setOnClickListener(this);
    }

    private void showLoadingView() {
        this.mSkip.setEnabled(false);
        this.mDo.setEnabled(false);
        VLTools.showLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
    }

    private void hideLoadingView() {
        this.mSkip.setEnabled(true);
        this.mDo.setEnabled(true);
        VLTools.hideLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
    }

    private void showLoadingFailedView() {
        this.mSkip.setEnabled(true);
        this.mDo.setEnabled(true);
        VLTools.showLoadingFailedView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
    }

    public void onResume() {
        super.onResume();
        refreshContentView();
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tb_like:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TAB_LIKE, "click");
                this.mCurType = 0;
                unCheckedAllButtons();
                this.mLikeToggleButton.setChecked(true);
                this.mLikeToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                if (GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
                    refreshContentView();
                    return;
                } else {
                    getBoardList();
                    return;
                }
            case R.id.tb_mix:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TAB_MIX, "click");
                this.mCurType = 2;
                unCheckedAllButtons();
                this.mMixToggleButton.setChecked(true);
                this.mMixToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                if (GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
                    refreshContentView();
                    return;
                } else {
                    getBoardList();
                    return;
                }
            case R.id.tb_follow:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TAB_FOLlOW, "click");
                this.mCurType = 1;
                unCheckedAllButtons();
                this.mFollowToggleButton.setChecked(true);
                this.mFollowToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                if (GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
                    refreshContentView();
                    return;
                } else {
                    getBoardList();
                    return;
                }
            case R.id.skip:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_BUTTON_SKIP, "click");
                gotoNextOrder();
                trackSkipAction();
                return;
            case R.id.positive:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_BUTTON_POSITIVE, "click");
                makeAction();
                return;
            case R.id.loading_failed:
                getBoardList();
                return;
            default:
                return;
        }
    }

    private void trackSkipAction() {
        if (GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
            if (GetCoinsManager.getSingleton().getCurrentIndex(this.mCurType) < GetCoinsManager.getSingleton().getCurrentBoardList(this.mCurType).size()) {
                Order curOrder = GetCoinsManager.getSingleton().getCurrentOrder(this.mCurType);
                if (curOrder != null && VLTools.verifyArrayList(curOrder.getCollage())) {
                    Iterator it = curOrder.getCollage().iterator();
                    while (it.hasNext()) {
                        trackAction(1, ((Order) it.next()).getOrderId());
                    }
                }
            }
        }
    }

    private void getBoardList() {
        LikeServerInstagram.getSingleton().getBoard(getActivity(), this.mCurType, GetCoinsManager.getSingleton().getCurrentPage(this.mCurType), this.getBoardCallback);
        showLoadingView();
    }

    private void trackAction(int actionType, long orderId) {
        if (AppConfigHelper.allowT(getActivity())) {
            LikeServerInstagram.getSingleton().trackAction(getActivity(), actionType, orderId, this.trackActionCallback);
        }
    }

    private void makeAction() {
        if (GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
            Order curOrder = GetCoinsManager.getSingleton().getCurrentOrder(this.mCurType);
            if (VLTools.verifyArrayList(curOrder.getCollage())) {
                int size = curOrder.getCollage().size();
                for (int i = 0; i < size; i++) {
                    Order curCollageOrder = (Order) curOrder.getCollage().get(i);
                    if (curCollageOrder.isLikeOrder()) {
                        if (VLTools.checkLikeTime(getActivity()) == 0) {
                            doLike(curCollageOrder);
                        } else {
                            if (RateUsManager.getSingleton().needRateUs(getActivity())) {
                                this.mCustomDialog = new RateUsCustomDialog(getActivity(), this.mCustomDialogClickListener);
                                this.mCustomDialog.show();
                            } else if (!AppConfigHelper.getOfferWallList(getActivity()).isEmpty()) {
                                this.mFreeCoinsDialog = new FreeCoinsCustomDialog(getActivity(), this.mFreeCoinsDialogClickListener);
                                this.mFreeCoinsDialog.show();
                            }
                            Toast.makeText(getActivity(), LIKE_LIMIT_NOTIFICATION[VLTools.checkLikeTime(getActivity()) - 1], 0).show();
                            return;
                        }
                    } else if (!curCollageOrder.isFollowOrder()) {
                        continue;
                    } else if (VLTools.checkFollowTime(getActivity()) == 0) {
                        doFollow(curCollageOrder);
                    } else {
                        if (RateUsManager.getSingleton().needRateUs(getActivity())) {
                            this.mCustomDialog = new RateUsCustomDialog(getActivity(), this.mCustomDialogClickListener);
                            this.mCustomDialog.show();
                        } else if (!AppConfigHelper.getOfferWallList(getActivity()).isEmpty()) {
                            this.mFreeCoinsDialog = new FreeCoinsCustomDialog(getActivity(), this.mFreeCoinsDialogClickListener);
                            this.mFreeCoinsDialog.show();
                        }
                        Toast.makeText(getActivity(), FOLLOW_LIMIT_NOTIFICATION[VLTools.checkFollowTime(getActivity()) - 1], 0).show();
                        return;
                    }
                }
            } else if (curOrder.isLikeOrder()) {
                if (VLTools.checkLikeTime(getActivity()) == 0) {
                    doLike(curOrder);
                } else {
                    if (RateUsManager.getSingleton().needRateUs(getActivity())) {
                        this.mCustomDialog = new RateUsCustomDialog(getActivity(), this.mCustomDialogClickListener);
                        this.mCustomDialog.show();
                    } else if (!AppConfigHelper.getOfferWallList(getActivity()).isEmpty()) {
                        this.mFreeCoinsDialog = new FreeCoinsCustomDialog(getActivity(), this.mFreeCoinsDialogClickListener);
                        this.mFreeCoinsDialog.show();
                    }
                    Toast.makeText(getActivity(), LIKE_LIMIT_NOTIFICATION[VLTools.checkLikeTime(getActivity()) - 1], 0).show();
                    return;
                }
            } else if (curOrder.isFollowOrder()) {
                if (VLTools.checkFollowTime(getActivity()) == 0) {
                    doFollow(curOrder);
                } else {
                    if (RateUsManager.getSingleton().needRateUs(getActivity())) {
                        this.mCustomDialog = new RateUsCustomDialog(getActivity(), this.mCustomDialogClickListener);
                        this.mCustomDialog.show();
                    } else if (!AppConfigHelper.getOfferWallList(getActivity()).isEmpty()) {
                        this.mFreeCoinsDialog = new FreeCoinsCustomDialog(getActivity(), this.mFreeCoinsDialogClickListener);
                        this.mFreeCoinsDialog.show();
                    }
                    Toast.makeText(getActivity(), FOLLOW_LIMIT_NOTIFICATION[VLTools.checkFollowTime(getActivity()) - 1], 0).show();
                    return;
                }
            }
        }
        gotoNextOrder();
    }

    private void doFollow(Order curCollageOrder) {
        InstagramService.getInstance().follow(curCollageOrder.getViUserId(), new FollowCallBack(curCollageOrder.getOrderId(), Long.parseLong(curCollageOrder.getViUserId())));
    }

    private void doLike(Order curCollageOrder) {
        InstagramService.getInstance().like(curCollageOrder.getPostId(), curCollageOrder.getViUserId(), new LikeCallBack(curCollageOrder.getOrderId(), Long.parseLong(curCollageOrder.getPostId())));
    }

    private void gotoNextOrder() {
        ArrayList<Order> currentBoardList = GetCoinsManager.getSingleton().getCurrentBoardList(this.mCurType);
        int index = GetCoinsManager.getSingleton().getCurrentIndex(this.mCurType);
        if (currentBoardList != null) {
            index++;
            if (index < currentBoardList.size()) {
                GetCoinsManager.getSingleton().setCurIndexMap(this.mCurType, index);
                refreshContentView();
                return;
            }
        }
        if (currentBoardList != null) {
            if (currentBoardList.size() == 0) {
                if (this.mTimeStamp != 0) {
                    long curTimeStamp = System.currentTimeMillis();
                    if (curTimeStamp - this.mTimeStamp < VLTools.DEFAULT_RATE_US_THREHOLD) {
                        ToastHelper.showToast(getActivity(), "Wait for " + (10 - ((curTimeStamp - this.mTimeStamp) / 1000)) + " seconds to go");
                        return;
                    }
                    this.mTimeStamp = 0;
                } else {
                    this.mTimeStamp = System.currentTimeMillis();
                    ToastHelper.showToast(getActivity(), "Wait for 10 seconds to go");
                    return;
                }
            }
            currentBoardList.clear();
        }
        GetCoinsManager.getSingleton().setCurIndexMap(this.mCurType, 0);
        getBoardList();
    }

    private void refreshContentView() {
        if (this.mContent.getChildCount() > 1) {
            this.mContent.removeViewAt(1);
        }
        if (!GetCoinsManager.getSingleton().isCurBoardDataEmpty(this.mCurType)) {
            if (GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
                View view = null;
                Order curOrder = GetCoinsManager.getSingleton().getCurrentOrder(this.mCurType);
                if (this.mCurType == 0) {
                    TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_CONTENT_LIKE, "show");
                    view = new DoLikeView(getActivity());
                    ((DoLikeView) view).bindView(curOrder);
                } else if (this.mCurType == 2) {
                    TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_CONTENT_MIX, "show");
                    view = new DoMixView(getActivity());
                    ((DoMixView) view).bindView(curOrder);
                } else if (this.mCurType == 1) {
                    TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_CONTENT_FOLLOW, "show");
                    view = new DoFollowView(getActivity());
                    ((DoFollowView) view).bindView(curOrder);
                }
                this.mContent.addView(view, 1);
                refreshPositiveButton();
                return;
            }
            TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_CONTENT_EMPTY, "show");
            this.mContent.addView(this.mContentEmptyView, 1);
            refreshPositiveButton();
        }
    }

    private void refreshPositiveButton() {
        if (GetCoinsManager.getSingleton().verifyBoardList(this.mCurType)) {
            int size = GetCoinsManager.getSingleton().getCurrentOrder(this.mCurType).getCollage().size();
            if (size == 0) {
                size = 1;
            }
            if (this.mCurType == 0) {
                setToggleButtonText(size, "like");
            } else if (this.mCurType == 2) {
                if (VLTools.verifyArrayList(GetCoinsManager.getSingleton().getCurrentOrder(this.mCurType).getCollage())) {
                    setToggleButtonText(size, ((Order) GetCoinsManager.getSingleton().getCurrentOrder(this.mCurType).getCollage().get(0)).getKind());
                } else {
                    setToggleButtonText(size, GetCoinsManager.getSingleton().getCurrentOrder(this.mCurType).getKind());
                }
            } else if (this.mCurType == 1) {
                setToggleButtonText(size, "follow");
            }
        } else if (this.mCurType == 0) {
            setToggleButtonText(0, "like");
        } else if (this.mCurType == 2) {
            setToggleButtonText(0, "like");
        } else if (this.mCurType == 1) {
            setToggleButtonText(0, "follow");
        }
    }

    private void setToggleButtonText(int size, String kind) {
        if (size <= 0) {
            if ("like".equals(kind)) {
                VLTools.setToggleButtonText(this.mDo, getResources().getString(R.string.positive_get_coins_1));
            } else if ("follow".equals(kind)) {
                VLTools.setToggleButtonText(this.mDo, getResources().getString(R.string.positive_get_coins_follow_1));
            }
        } else if ("like".equals(kind)) {
            VLTools.setToggleButtonText(this.mDo, size >= 4 ? getResources().getString(R.string.positive_get_coins_4) : getResources().getString(R.string.positive_get_coins_1));
        } else if ("follow".equals(kind)) {
            VLTools.setToggleButtonText(this.mDo, size >= 4 ? getResources().getString(R.string.positive_get_coins_follow_4) : getResources().getString(R.string.positive_get_coins_follow_1));
        }
    }

    private void unCheckedAllButtons() {
        this.mLikeToggleButton.setChecked(false);
        this.mMixToggleButton.setChecked(false);
        this.mFollowToggleButton.setChecked(false);
        this.mLikeToggleButton.setTextColor(getResources().getColor(R.color.app_white));
        this.mMixToggleButton.setTextColor(getResources().getColor(R.color.app_white));
        this.mFollowToggleButton.setTextColor(getResources().getColor(R.color.app_white));
    }
}
