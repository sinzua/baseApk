package com.ty.followboom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.forwardwin.base.widgets.CircleImageView;
import com.forwardwin.base.widgets.ToastHelper;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.AppContext;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.followboom.helpers.TrackHelper;
import com.ty.followboom.models.CoinsManager;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.models.NativeXManager;
import com.ty.followboom.models.RateUsManager;
import com.ty.followboom.models.SupersonicManager;
import com.ty.followboom.models.TrackerDBManager;
import com.ty.followboom.models.UserInfoManager;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.BuyCoinsConfirmResponse;
import com.ty.followboom.okhttp.responses.GetAccountInfoResponse;
import com.ty.followboom.okhttp.responses.QueryOfferWallInfoResponse;
import com.ty.followboom.views.FreeCoinsCustomDialog;
import com.ty.followboom.views.RoundProgressBar;
import com.ty.instaview.R;

public class MainActivity extends FragmentActivity implements OnClickListener {
    public static final int GET_COINS_FRAGMENT_TAB = 0;
    public static final int PROMOTION_FRAGMENT_TAB = 1;
    public static final int SETTINGS_FRAGMENT_TAB = 3;
    public static final int STORE_FRAGMENT_TAB = 2;
    public static final String TAB_INDEX = "tab_index";
    private static final String TAG = "MainActivity";
    private Class[] fragmentArray = new Class[]{GetCoinsFragment.class, PromotionFragment.class, CoinsFragment.class, SettingsFragment.class};
    private int[] iconArray = new int[]{R.drawable.navi_get_coins, R.drawable.navi_promotion, R.drawable.navi_store, R.drawable.navi_settings};
    private RequestCallback<GetAccountInfoResponse> mAccountInfoCallback = new RequestCallback<GetAccountInfoResponse>() {
        public void onResponse(GetAccountInfoResponse getAccountInfoResponse) {
            if (getAccountInfoResponse.isSuccessful()) {
                CoinsManager.getSingleton().setCoins(getAccountInfoResponse.getData().getCoins());
                Toast.makeText(MainActivity.this, String.format("Rate us succeed, add %s coins", new Object[]{Integer.valueOf(AppConfigHelper.getRateRewardCoins(MainActivity.this))}), 0).show();
                return;
            }
            Toast.makeText(MainActivity.this, "Rate us failed", 0).show();
        }

        public void onFailure(Exception e) {
            Toast.makeText(MainActivity.this, "Rate us failed", 0).show();
        }
    };
    private ImageView mAddCoins;
    private CircleImageView mAvatarImage;
    private IBillingHandler mBillingHandler = new IBillingHandler() {
        public void onProductPurchased(String productId, TransactionDetails details) {
            TrackHelper.track(TrackHelper.CATEGORY_GOOGLE_BILLING, TrackHelper.ACTION_PURCHASE, TrackHelper.LABEL_SUCCEED);
            ToastHelper.showToast(MainActivity.this, "Get coins purchase succeed.");
            MainActivity.this.mBillingProcessor.consumePurchase(productId);
            LikeServerInstagram.getSingleton().buyGoogleCoinsConfirm(MainActivity.this, productId, details.purchaseToken, details.purchaseInfo.responseData, details.purchaseInfo.signature, MainActivity.this.mBuyCoinsConfirmCallback);
        }

        public void onBillingError(int errorCode, Throwable error) {
            TrackHelper.track(TrackHelper.CATEGORY_GOOGLE_BILLING, TrackHelper.ACTION_PURCHASE, "failed");
            ToastHelper.showToast(MainActivity.this, "Billing error with error code: " + Integer.toString(errorCode));
        }

        public void onBillingInitialized() {
            Log.d(MainActivity.TAG, "onBillingInitialized");
            MainActivity.this.mReadyToPurchase = true;
        }

        public void onPurchaseHistoryRestored() {
            Log.d(MainActivity.TAG, "onPurchaseHistoryRestored");
        }
    };
    public BillingProcessor mBillingProcessor;
    private RequestCallback<BuyCoinsConfirmResponse> mBuyCoinsConfirmCallback = new RequestCallback<BuyCoinsConfirmResponse>() {
        public void onResponse(BuyCoinsConfirmResponse buyCoinsConfirmResponse) {
            if (buyCoinsConfirmResponse.isSuccessful()) {
                CoinsManager.getSingleton().setCoins(buyCoinsConfirmResponse.getData().getCoin());
                Toast.makeText(MainActivity.this, "Purchase confirm succeed", 0).show();
                return;
            }
            Toast.makeText(MainActivity.this, "Purchase confirm failed", 0).show();
        }

        public void onFailure(Exception e) {
            Toast.makeText(MainActivity.this, "Purchase confirm failed", 0).show();
        }
    };
    private RoundProgressBar mCoins;
    private int mCurTabIndex;
    private TextView mFreeCoins;
    private FreeCoinsCustomDialog mFreeCoinsDialog;
    private OnClickListener mFreeCoinsDialogClickListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.ll_ss == id) {
                TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_SUPERSONIC, "click");
                SupersonicManager.getSingleton().show();
            } else if (R.id.ll_nativex == id) {
                TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_NATIVEX, "click");
                NativeXManager.getSingleton().show();
            }
        }
    };
    private TextView mGoldsum;
    private RequestCallback<QueryOfferWallInfoResponse> mQueryOfferWallInfoCallback = new RequestCallback<QueryOfferWallInfoResponse>() {
        public void onResponse(QueryOfferWallInfoResponse queryOfferWallInfoResponse) {
            try {
                if (queryOfferWallInfoResponse.getData().getCoinsEarned().intValue() > 0) {
                    ToastHelper.showToast(MainActivity.this, queryOfferWallInfoResponse.getData().getOfferwall() + " earned : " + queryOfferWallInfoResponse.getData().getCoinsEarned());
                }
                AppConfigHelper.saveOfferWallItemByName(MainActivity.this, queryOfferWallInfoResponse.getData().getOfferwall(), queryOfferWallInfoResponse.getData());
                CoinsManager.getSingleton().refreshGoldSum();
            } catch (Exception e) {
                TrackHelper.track(TrackHelper.CATEGORY_MAIN, TrackHelper.ACTION_UPDATE_OFFERWALL, e.toString());
            }
        }

        public void onFailure(Exception e) {
            Log.d(MainActivity.TAG, e.toString());
        }
    };
    public boolean mReadyToPurchase;
    private ImageView mRefreshView;
    private FragmentTabHost mTabHost;
    private int[] titleArray = new int[]{R.string.get_coins, R.string.promotion, R.string.store, R.string.settings};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_main);
        TrackHelper.track(TrackHelper.CATEGORY_MAIN, "show", "");
        TrackerDBManager.initTrackerDBManager(this);
        if (this.mBillingProcessor == null || !this.mBillingProcessor.isInitialized()) {
            AppContext.getSingleton().getClass();
            this.mBillingProcessor = new BillingProcessor(this, AppContext.KEY_VIEW, this.mBillingHandler);
        }
        this.mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        this.mCurTabIndex = getIntent().getIntExtra(TAB_INDEX, 0);
        setupTabView();
        this.mTabHost.setCurrentTab(this.mCurTabIndex);
        initTitleViews();
        CoinsManager.initCoinsManager(this, this.mGoldsum, UserInfoManager.getSingleton().getUserInfo(this).getAccountInfo());
    }

    protected void onResume() {
        super.onResume();
        PicassoHelper.setImageView(this, this.mAvatarImage, UserInfoManager.getSingleton().getUserInfo(this).getAvatarUrl(), R.drawable.ic_profile);
        CoinsManager.getSingleton().refreshGoldSum();
        RateUsManager.getSingleton().tryRecord(this, this.mAccountInfoCallback);
    }

    protected void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        if (this.mBillingProcessor != null) {
            this.mBillingProcessor.release();
        }
        super.onDestroy();
    }

    private void setupTabView() {
        this.mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        this.mTabHost.getTabWidget().setDividerDrawable(null);
        int count = this.fragmentArray.length;
        for (int i = 0; i < count; i++) {
            this.mTabHost.addTab(this.mTabHost.newTabSpec(getString(this.titleArray[i])).setIndicator(getTabItemView(i)), this.fragmentArray[i], null);
        }
    }

    public FragmentTabHost getTabHost() {
        return this.mTabHost;
    }

    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_bottom_nav, null);
        ((ImageView) view.findViewById(R.id.iv_icon)).setImageResource(this.iconArray[index]);
        ((TextView) view.findViewById(R.id.tv_icon)).setText(this.titleArray[index]);
        return view;
    }

    private void initTitleViews() {
        this.mRefreshView = (ImageView) findViewById(R.id.refresh);
        this.mFreeCoins = (TextView) findViewById(R.id.free_coins);
        this.mAvatarImage = (CircleImageView) findViewById(R.id.avatar_image);
        this.mGoldsum = (TextView) findViewById(R.id.goldsum_textview);
        this.mCoins = (RoundProgressBar) findViewById(R.id.round_progress_bar);
        this.mAddCoins = (ImageView) findViewById(R.id.add_coins);
        this.mRefreshView.setVisibility(8);
        this.mFreeCoins.setVisibility(8);
        this.mFreeCoins.setOnClickListener(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        if (!this.mBillingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClick(View v) {
        if (R.id.free_coins == v.getId() && !AppConfigHelper.getOfferWallList(this).isEmpty()) {
            this.mFreeCoinsDialog = new FreeCoinsCustomDialog(this, this.mFreeCoinsDialogClickListener);
            this.mFreeCoinsDialog.show();
        }
    }
}
