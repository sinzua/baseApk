package com.ty.followboom.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.instaview.R;

public class RateUsCustomDialog extends Dialog {
    private String mContent;
    private TextView mContentView;
    private Context mContext;
    private TextView mNegativeButton;
    private OnClickListener mOnClickListener;
    private TextView mPositiveButton;
    private String mTitle;
    private TextView mTitleView;

    public RateUsCustomDialog(Context context, OnClickListener onClickListener) {
        super(context, R.style.CustomDialog);
        requestWindowFeature(1);
        setContentView(R.layout.rateus_custom_dialog);
        this.mContext = context;
        this.mOnClickListener = onClickListener;
        initViews();
    }

    private void initViews() {
        this.mTitleView = (TextView) findViewById(R.id.title_view);
        this.mContentView = (TextView) findViewById(R.id.content_view);
        this.mPositiveButton = (TextView) findViewById(R.id.positive_button);
        this.mNegativeButton = (TextView) findViewById(R.id.negative_button);
        this.mContentView.setText(String.format("Rate Us 5 Stars to earn %s coins,\n or email to us", new Object[]{Integer.valueOf(AppConfigHelper.getRateRewardCoins(this.mContext))}));
        this.mPositiveButton.setOnClickListener(this.mOnClickListener);
        this.mNegativeButton.setOnClickListener(this.mOnClickListener);
    }

    public void setPositiveButtonText(String text) {
        this.mPositiveButton.setText(text);
    }

    public void setNegativeButtonText(String text) {
        this.mNegativeButton.setText(text);
    }

    public void setNegativeButtonTextColor(int colorRes) {
        this.mNegativeButton.setTextColor(colorRes);
    }

    public TextView getPositiveButton() {
        return this.mPositiveButton;
    }

    public TextView getNegativeButton() {
        return this.mNegativeButton;
    }
}
