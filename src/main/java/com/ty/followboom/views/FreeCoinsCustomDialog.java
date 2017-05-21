package com.ty.followboom.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.instaview.R;

public class FreeCoinsCustomDialog extends Dialog {
    private Context mContext;
    private LinearLayout mNativex;
    private OnClickListener mOnClickListener;
    private LinearLayout mSupersonic;

    public FreeCoinsCustomDialog(Context context, OnClickListener onClickListener) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        requestWindowFeature(1);
        setContentView(R.layout.freecoins_custom_dialog);
        this.mOnClickListener = onClickListener;
        initViews();
    }

    private void initViews() {
        int i = 0;
        this.mNativex = (LinearLayout) findViewById(R.id.ll_nativex);
        this.mSupersonic = (LinearLayout) findViewById(R.id.ll_ss);
        this.mSupersonic.setVisibility(AppConfigHelper.getAppConfig(this.mContext).getSsEnabled().equals("1") ? 0 : 8);
        LinearLayout linearLayout = this.mNativex;
        if (!AppConfigHelper.getAppConfig(this.mContext).getNativexEnabled().equals("1")) {
            i = 8;
        }
        linearLayout.setVisibility(i);
        this.mNativex.setOnClickListener(this.mOnClickListener);
        this.mSupersonic.setOnClickListener(this.mOnClickListener);
    }
}
