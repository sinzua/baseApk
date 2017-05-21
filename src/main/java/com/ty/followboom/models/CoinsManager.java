package com.ty.followboom.models;

import android.content.Context;
import android.widget.TextView;
import com.forwardwin.base.widgets.ToastHelper;
import com.ty.followboom.entities.AccountInfo;
import com.ty.followboom.helpers.AppConfigHelper;

public class CoinsManager {
    private static final String TAG = "CoinsManager";
    public static final int TYPE_FOLLOW = 1;
    public static final int TYPE_LIKE = 0;
    private static CoinsManager instance;
    private AccountInfo mAccountInfo;
    private Context mContext;
    private TextView mGoldSum;

    public CoinsManager(Context context, TextView goldSum, AccountInfo accountInfo) {
        this.mContext = context;
        this.mGoldSum = goldSum;
        this.mAccountInfo = accountInfo;
        refreshGoldSum();
    }

    public static void initCoinsManager(Context context, TextView goldSum, AccountInfo accountInfo) {
        instance = new CoinsManager(context, goldSum, accountInfo);
    }

    public static CoinsManager getSingleton() {
        return instance;
    }

    public AccountInfo getAccountInfo() {
        return this.mAccountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        if (accountInfo != null) {
            this.mAccountInfo = accountInfo;
        }
    }

    public void setCoins(String coins) {
        if (this.mAccountInfo != null) {
            this.mAccountInfo.setCoins(coins);
        } else {
            this.mAccountInfo = new AccountInfo();
            this.mAccountInfo.setCoins(coins);
        }
        refreshGoldSum();
    }

    public void increment(int type) {
        if (AppConfigHelper.allowT(this.mContext) && this.mAccountInfo != null && this.mAccountInfo.getCoins() != null) {
            if (type == 1) {
                setCoins("" + (Long.parseLong(this.mAccountInfo.getCoins()) + 4));
            } else {
                setCoins("" + (Long.parseLong(this.mAccountInfo.getCoins()) + 1));
            }
        }
    }

    public void decrement() {
        if (this.mAccountInfo != null) {
            this.mAccountInfo.setCoins("" + (Long.parseLong(this.mAccountInfo.getCoins()) - 1));
            ToastHelper.showToast(this.mContext, "download succeed, use 1 coin!");
        }
    }

    public void refreshGoldSum() {
        if (this.mGoldSum != null && this.mAccountInfo != null) {
            this.mGoldSum.setText(this.mAccountInfo.getCoins());
            this.mGoldSum.setClickable(false);
        }
    }
}
