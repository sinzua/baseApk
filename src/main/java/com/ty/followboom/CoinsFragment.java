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

public class CoinsFragment extends BaseFragment implements OnClickListener {
    private static final int TAB_FREE = 1;
    private static final int TAB_STORE = 0;
    private static final String TAG = "CoinsFragment";
    private int mCurTab = 0;
    private ArrayList<Fragment> mFragmentList;
    private ToggleButton mFreeToggleButton;
    private ToggleButton mStoreToggleButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track(TrackHelper.CATEGORY_GET_COINS, "show", "");
        this.mRootView = inflater.inflate(R.layout.fragment_coins, container, false);
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
        this.mStoreToggleButton = (ToggleButton) v.findViewById(R.id.tb_store);
        this.mFreeToggleButton = (ToggleButton) v.findViewById(R.id.tb_free);
        this.mStoreToggleButton.setVisibility(8);
        this.mFreeToggleButton.setVisibility(8);
        ((View) this.mFreeToggleButton.getParent()).setVisibility(8);
        this.mStoreToggleButton.setOnClickListener(this);
        this.mFreeToggleButton.setOnClickListener(this);
        unCheckedAllButtons();
        if (this.mCurTab == 0) {
            this.mStoreToggleButton.setChecked(true);
            this.mStoreToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        } else if (1 == this.mCurTab) {
            this.mFreeToggleButton.setChecked(true);
            this.mFreeToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tb_store:
                TrackHelper.track(TrackHelper.CATEGORY_GET_COINS, TrackHelper.ACTION_TAB_LIKE, "click");
                this.mCurTab = 0;
                unCheckedAllButtons();
                this.mStoreToggleButton.setChecked(true);
                this.mStoreToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                setCurrentFragment(this.mCurTab);
                return;
            case R.id.tb_free:
                TrackHelper.track(TrackHelper.CATEGORY_GET_COINS, TrackHelper.ACTION_TAB_VIEW, "click");
                this.mCurTab = 1;
                unCheckedAllButtons();
                this.mFreeToggleButton.setChecked(true);
                this.mFreeToggleButton.setTextColor(getResources().getColor(R.color.app_theme));
                setCurrentFragment(this.mCurTab);
                return;
            default:
                return;
        }
    }

    private void initFragments() {
        this.mFragmentList = new ArrayList();
        this.mFragmentList.add(new StoreFragment());
        this.mFragmentList.add(new FreeFragment());
    }

    public void setCurrentFragment(int position) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content, (Fragment) this.mFragmentList.get(position));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void unCheckedAllButtons() {
        this.mStoreToggleButton.setChecked(false);
        this.mFreeToggleButton.setChecked(false);
        this.mStoreToggleButton.setTextColor(getResources().getColor(R.color.app_white));
        this.mFreeToggleButton.setTextColor(getResources().getColor(R.color.app_white));
    }
}
