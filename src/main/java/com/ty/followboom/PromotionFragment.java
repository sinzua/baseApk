package com.ty.followboom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.instaview.R;
import java.util.ArrayList;

public class PromotionFragment extends BaseFragment implements OnClickListener {
    private static final int TAB_GET_FOLLOWERS = 2;
    private static final int TAB_GET_LIKES = 0;
    private static final int TAB_GET_VIEWS = 1;
    private static final String TAG = "GetCoinsFragment";
    private int mCurTab = 1;
    private ToggleButton mFollowToggleButton;
    private ArrayList<Fragment> mFragmentList;
    private ToggleButton mLikeToggleButton;
    private ToggleButton mViewToggleButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track("GetCoinsFragment", "show", "");
        this.mRootView = inflater.inflate(R.layout.fragment_promotion, container, false);
        onActivate(this.mRootView);
        return this.mRootView;
    }

    private void onActivate(View v) {
        initTitleViews();
        initFragments();
        initContentViews(v);
        setCurrentFragment(this.mCurTab);
    }

    private void initContentViews(View v) {
        this.mLikeToggleButton = (ToggleButton) v.findViewById(R.id.tb_like);
        this.mViewToggleButton = (ToggleButton) v.findViewById(R.id.tb_view);
        this.mFollowToggleButton = (ToggleButton) v.findViewById(R.id.tb_follow);
        this.mLikeToggleButton.setOnClickListener(this);
        this.mViewToggleButton.setOnClickListener(this);
        this.mFollowToggleButton.setOnClickListener(this);
        unCheckedAllButtons();
        if (this.mCurTab == 0) {
            this.mLikeToggleButton.setChecked(true);
            this.mLikeToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        } else if (1 == this.mCurTab) {
            this.mViewToggleButton.setChecked(true);
            this.mViewToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        } else if (2 == this.mCurTab) {
            this.mFollowToggleButton.setChecked(true);
            this.mFollowToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tb_like:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TAB_LIKE, "click");
                this.mCurTab = 0;
                unCheckedAllButtons();
                this.mLikeToggleButton.setChecked(true);
                this.mLikeToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                setCurrentFragment(this.mCurTab);
                return;
            case R.id.tb_follow:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TAB_FOLlOW, "click");
                this.mCurTab = 2;
                unCheckedAllButtons();
                this.mFollowToggleButton.setChecked(true);
                this.mFollowToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                setCurrentFragment(this.mCurTab);
                return;
            case R.id.tb_view:
                TrackHelper.track("GetCoinsFragment", TrackHelper.ACTION_TAB_VIEW, "click");
                this.mCurTab = 1;
                unCheckedAllButtons();
                this.mViewToggleButton.setChecked(true);
                this.mViewToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                setCurrentFragment(this.mCurTab);
                return;
            default:
                return;
        }
    }

    private void initFragments() {
        this.mFragmentList = new ArrayList();
        this.mFragmentList.add(new GetLikesFragment());
        this.mFragmentList.add(new GetViewsFragment());
        this.mFragmentList.add(new GetFollowersFragment());
    }

    public void setCurrentFragment(int position) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content, (Fragment) this.mFragmentList.get(position));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void unCheckedAllButtons() {
        this.mLikeToggleButton.setChecked(false);
        this.mViewToggleButton.setChecked(false);
        this.mFollowToggleButton.setChecked(false);
        this.mLikeToggleButton.setTextColor(getResources().getColor(R.color.app_white));
        this.mViewToggleButton.setTextColor(getResources().getColor(R.color.app_white));
        this.mFollowToggleButton.setTextColor(getResources().getColor(R.color.app_white));
    }
}
