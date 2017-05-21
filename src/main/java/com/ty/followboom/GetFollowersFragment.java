package com.ty.followboom;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.forwardwin.base.widgets.ToastHelper;
import com.ty.followboom.adapters.FollowersListAdapter;
import com.ty.followboom.entities.CoinsInAccount;
import com.ty.followboom.entities.Goods;
import com.ty.followboom.entities.UserInfo;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.GoodsHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.CoinsManager;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.CommonCoinsResponse;
import com.ty.followboom.views.CustomDialog;
import com.ty.instaview.R;

public class GetFollowersFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "GetFollowersFragment";
    private CustomDialog mCustomDialog;
    private OnClickListener mCustomDialogClickListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.positive_button == id) {
                TrackHelper.track("GetFollowersFragment", TrackHelper.ACTION_GET_FOLLOWERS, TrackHelper.LABEL_POSITIVE);
                try {
                    UserInfo user = AppConfigHelper.getUserInfo(GetFollowersFragment.this.getActivity());
                    LikeServerInstagram.getSingleton().getFollowers(GetFollowersFragment.this.getActivity(), user.getAvatarUrl(), GetFollowersFragment.this.mGoods.getGoodsId(), user.getUsername(), 0, GetFollowersFragment.this.mGetFollowersCallback);
                    GetFollowersFragment.this.mCustomDialog.dismiss();
                    GetFollowersFragment.this.mProgressDialog = ProgressDialog.show(GetFollowersFragment.this.getActivity(), GetFollowersFragment.this.getResources().getString(R.string.deal_processing_title), GetFollowersFragment.this.getResources().getString(R.string.deal_processing_content));
                } catch (Exception e) {
                    Toast.makeText(GetFollowersFragment.this.getActivity(), GetFollowersFragment.this.getResources().getString(R.string.get_item_again), 0).show();
                    GetFollowersFragment.this.mCustomDialog.dismiss();
                }
            } else if (R.id.negative_button == id) {
                TrackHelper.track("GetFollowersFragment", TrackHelper.ACTION_GET_FOLLOWERS, TrackHelper.LABEL_NEGATIVE);
                GetFollowersFragment.this.mCustomDialog.dismiss();
            }
        }
    };
    private ListView mFollowerList;
    private FollowersListAdapter mFollowersListAdapter;
    private RequestCallback<CommonCoinsResponse> mGetFollowersCallback = new RequestCallback<CommonCoinsResponse>() {
        public void onResponse(CommonCoinsResponse commonCoinsResponse) {
            if (GetFollowersFragment.this.mProgressDialog != null) {
                GetFollowersFragment.this.mProgressDialog.dismiss();
            }
            if (commonCoinsResponse != null && commonCoinsResponse.isSuccessful()) {
                CoinsInAccount coinsInAccount = commonCoinsResponse.getData();
                if (coinsInAccount != null) {
                    CoinsManager.getSingleton().setCoins(coinsInAccount.getCoinsInAccount());
                }
                Toast.makeText(GetFollowersFragment.this.getActivity(), GetFollowersFragment.this.getResources().getString(R.string.get_followers_succeed), 0).show();
            } else if (commonCoinsResponse == null || !commonCoinsResponse.isSessionExpired()) {
                Toast.makeText(GetFollowersFragment.this.getActivity(), GetFollowersFragment.this.getResources().getString(R.string.get_item_again), 0).show();
            } else {
                VLTools.gotoLogin(GetFollowersFragment.this.getActivity());
            }
        }

        public void onFailure(Exception e) {
            if (GetFollowersFragment.this.mProgressDialog != null) {
                GetFollowersFragment.this.mProgressDialog.dismiss();
            }
            Toast.makeText(GetFollowersFragment.this.getActivity(), GetFollowersFragment.this.getResources().getString(R.string.get_item_again), 0).show();
        }
    };
    private Goods mGoods;
    private OnClickListener mItemClickListener = new OnClickListener() {
        public void onClick(View view) {
            GetFollowersFragment.this.mGoods = (Goods) view.getTag();
            if (GetFollowersFragment.this.mGoods != null) {
                TrackHelper.track("GetFollowersFragment", TrackHelper.ACTION_GET_FOLLOWERS, GetFollowersFragment.this.mGoods.getGoodsId());
                if (GetFollowersFragment.this.mGoods.getPrice() <= Double.parseDouble(CoinsManager.getSingleton().getAccountInfo().getCoins())) {
                    GetFollowersFragment.this.mCustomDialog = new CustomDialog(GetFollowersFragment.this.getActivity(), GetFollowersFragment.this.getResources().getString(R.string.buy_followers_confirm_title), String.format(GetFollowersFragment.this.getResources().getString(R.string.buy_followers_confirm_content), new Object[]{GetFollowersFragment.this.mGoods.getTitle().replace(" {}", ""), VLTools.removePointIfHave(GetFollowersFragment.this.mGoods.getPrice() + "")}), GetFollowersFragment.this.mCustomDialogClickListener);
                    GetFollowersFragment.this.mCustomDialog.show();
                    return;
                }
                ToastHelper.showToast(GetFollowersFragment.this.getActivity(), "Coins not enough, try get more coins!");
                return;
            }
            Toast.makeText(GetFollowersFragment.this.getActivity(), GetFollowersFragment.this.getResources().getString(R.string.get_item_again), 0).show();
        }
    };
    private LinearLayout mLoadingFailedView;
    private LinearLayout mLoadingItem;
    private LinearLayout mLoadingView;
    private ProgressDialog mProgressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track("GetFollowersFragment", "show", "");
        this.mRootView = inflater.inflate(R.layout.fragment_get_followers, container, false);
        onActivate(this.mRootView);
        return this.mRootView;
    }

    private void onActivate(View v) {
        initTitleViews();
        initContentViews();
        this.mProgressDialog = new ProgressDialog(getActivity());
    }

    private void initContentViews() {
        this.mFollowerList = (ListView) this.mRootView.findViewById(R.id.follower_list);
        this.mFollowersListAdapter = new FollowersListAdapter(getActivity());
        this.mFollowersListAdapter.setGoodsData(GoodsHelper.getGoodsDataFollowers(getActivity()));
        this.mFollowersListAdapter.setItemClickListener(this.mItemClickListener);
        this.mFollowerList.setAdapter(this.mFollowersListAdapter);
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.loading_failed:
                VLTools.showLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
                return;
            default:
                return;
        }
    }
}
