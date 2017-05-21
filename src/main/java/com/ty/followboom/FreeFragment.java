package com.ty.followboom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.NativeXManager;
import com.ty.followboom.models.SupersonicManager;
import com.ty.instaview.R;

public class FreeFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "FreeFragment";
    private LinearLayout mLoadingFailedView;
    private LinearLayout mLoadingItem;
    private LinearLayout mLoadingView;
    private LinearLayout mNativex;
    private LinearLayout mSupersonic;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackHelper.track(TrackHelper.CATEGORY_STORE, "show", "");
        this.mRootView = inflater.inflate(R.layout.fragment_free_coins, container, false);
        onActivate(this.mRootView);
        return this.mRootView;
    }

    private void onActivate(View v) {
        initLoadingView();
        initTitleViews();
        initContentView();
    }

    private void initLoadingView() {
        this.mLoadingView = (LinearLayout) this.mRootView.findViewById(R.id.loading_view);
        this.mLoadingItem = (LinearLayout) this.mRootView.findViewById(R.id.loading_item);
        this.mLoadingFailedView = (LinearLayout) this.mRootView.findViewById(R.id.loading_failed);
        VLTools.hideLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
        this.mLoadingFailedView.setOnClickListener(this);
    }

    private void initContentView() {
        int i = 0;
        this.mNativex = (LinearLayout) this.mRootView.findViewById(R.id.ll_nativex);
        this.mSupersonic = (LinearLayout) this.mRootView.findViewById(R.id.ll_ss);
        this.mSupersonic.setVisibility(AppConfigHelper.getAppConfig(getActivity()).getSsEnabled().equals("1") ? 0 : 8);
        LinearLayout linearLayout = this.mNativex;
        if (!AppConfigHelper.getAppConfig(getActivity()).getNativexEnabled().equals("1")) {
            i = 8;
        }
        linearLayout.setVisibility(i);
        this.mNativex.setOnClickListener(this);
        this.mSupersonic.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_nativex:
                NativeXManager.getSingleton().show();
                return;
            case R.id.ll_ss:
                SupersonicManager.getSingleton().show();
                return;
            case R.id.loading_failed:
                VLTools.showLoadingView(this.mLoadingView, this.mLoadingItem, this.mLoadingFailedView);
                return;
            default:
                return;
        }
    }
}
