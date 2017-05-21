package com.ty.followboom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.RateUsManager;
import com.ty.followboom.models.UserInfoManager;
import com.ty.followboom.views.RateUsCustomDialog;
import com.ty.instaview.R;

public class SettingsFragment extends BaseFragment {
    private TextView mCoinsHistory;
    private RateUsCustomDialog mCustomDialog;
    private OnClickListener mCustomDialogClickListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.positive_button == id) {
                TrackHelper.track(TrackHelper.CATEGORY_GET_LIKES_ACTIVITY, TrackHelper.ACTION_GET_LIKES, TrackHelper.LABEL_POSITIVE);
                SettingsFragment.this.mCustomDialog.dismiss();
                VLTools.rateUs(SettingsFragment.this.getActivity());
                RateUsManager.getSingleton().trigger();
            } else if (R.id.negative_button == id) {
                TrackHelper.track(TrackHelper.CATEGORY_GET_LIKES_ACTIVITY, TrackHelper.ACTION_GET_LIKES, TrackHelper.LABEL_NEGATIVE);
                SettingsFragment.this.mCustomDialog.dismiss();
                VLTools.emailUs(SettingsFragment.this.getActivity());
                RateUsManager.getSingleton().forceRecord(SettingsFragment.this.getActivity());
            }
        }
    };
    private TextView mEmailUs;
    private TextView mFAQ;
    private TextView mLogout;
    private TextView mOrderStatus;
    private TextView mRateUs;
    private TextView mShareApp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initTitleViews();
        onActivate();
        return this.mRootView;
    }

    private void onActivate() {
        this.mEmailUs = (TextView) this.mRootView.findViewById(R.id.email_us_button);
        this.mShareApp = (TextView) this.mRootView.findViewById(R.id.share_app_button);
        this.mFAQ = (TextView) this.mRootView.findViewById(R.id.faq_button);
        this.mRateUs = (TextView) this.mRootView.findViewById(R.id.rate_us_button);
        this.mCoinsHistory = (TextView) this.mRootView.findViewById(R.id.coin_history_button);
        this.mOrderStatus = (TextView) this.mRootView.findViewById(R.id.order_status_button);
        this.mLogout = (TextView) this.mRootView.findViewById(R.id.logout_button);
        this.mRateUs.setText(AppConfigHelper.getRateString(getActivity()));
        this.mEmailUs.setOnClickListener(this);
        this.mShareApp.setOnClickListener(this);
        this.mFAQ.setOnClickListener(this);
        this.mRateUs.setOnClickListener(this);
        this.mCoinsHistory.setOnClickListener(this);
        this.mOrderStatus.setOnClickListener(this);
        this.mLogout.setOnClickListener(this);
    }

    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.email_us_button) {
            TrackHelper.track(TrackHelper.CATEGORY_SETTINGS, TrackHelper.ACTION_EMAIL_US, "click");
            VLTools.emailUs(getActivity());
        } else if (id == R.id.share_app_button) {
            TrackHelper.track(TrackHelper.CATEGORY_SETTINGS, TrackHelper.ACTION_SHARE_APP, "click");
            shareApp();
        } else if (id == R.id.faq_button) {
            TrackHelper.track(TrackHelper.CATEGORY_SETTINGS, TrackHelper.ACTION_FAQ, "click");
            startFAQActivity();
        } else if (id == R.id.rate_us_button) {
            TrackHelper.track(TrackHelper.CATEGORY_SETTINGS, TrackHelper.ACTION_RATE_US, "click");
            rateUs();
        } else if (R.id.order_status_button == id) {
            TrackHelper.track(TrackHelper.CATEGORY_SETTINGS, TrackHelper.ACTION_ORDER_STATUS, "click");
            VLTools.gotoOrderStatus(getActivity());
        } else if (R.id.coin_history_button == id) {
            TrackHelper.track(TrackHelper.CATEGORY_SETTINGS, TrackHelper.ACTION_COINS_HISTORY, "click");
            VLTools.gotoCoinsHistory(getActivity());
        } else if (id == R.id.logout_button) {
            TrackHelper.track(TrackHelper.CATEGORY_SETTINGS, TrackHelper.ACTION_LOGOUT, "click");
            logout();
        }
    }

    private void startFAQActivity() {
        VLTools.gotoFAQ(getActivity());
    }

    private void shareApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Choose"));
    }

    private void rateUs() {
        this.mCustomDialog = new RateUsCustomDialog(getActivity(), this.mCustomDialogClickListener);
        this.mCustomDialog.show();
    }

    private void logout() {
        UserInfoManager.getSingleton().logout(getActivity());
        VLTools.gotoLogin(getActivity());
    }
}
