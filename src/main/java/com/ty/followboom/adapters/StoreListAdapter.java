package com.ty.followboom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ty.followboom.entities.Goods;
import com.ty.followboom.helpers.VLTools;
import com.ty.instaview.R;
import java.util.ArrayList;

public class StoreListAdapter extends BaseAdapter {
    private static final String TAG = "StoreListAdapter";
    private Context mContext;
    private TextView mFreeTextView;
    private ArrayList<Goods> mGoodsData;
    private OnClickListener mItemClickListener;
    private TextView mPrice;
    private TextView mStoreListTitleTextView;
    private TextView mTitle;

    public StoreListAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        if (this.mGoodsData == null) {
            return 0;
        }
        if (VLTools.inReview(this.mContext)) {
            return this.mGoodsData.size() + 1;
        }
        return this.mGoodsData.size() + 1;
    }

    public Goods getItem(int position) {
        return (Goods) this.mGoodsData.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.store_list_title, parent, false);
            this.mStoreListTitleTextView = (TextView) convertView.findViewById(R.id.store_list_title_textview);
            this.mStoreListTitleTextView.setText("In App Purchase");
            return convertView;
        } else if (position <= 0 || position > this.mGoodsData.size()) {
            return convertView;
        } else {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.adapter_gold_list, parent, false);
            this.mTitle = (TextView) convertView.findViewById(R.id.title);
            this.mPrice = (TextView) convertView.findViewById(R.id.price);
            Goods goods = getItem(position - 1);
            this.mTitle.setText("Get " + goods.getTitle().replace(" {}", "") + " Coins");
            if (goods.getPaymentType() == 0) {
                convertView.setVisibility(8);
            } else if (goods.getPaymentType() == 1) {
                if (goods.getSkuDetails() != null) {
                    Log.d(TAG, "sku priceText" + goods.getSkuDetails().priceText);
                    this.mPrice.setText(goods.getSkuDetails().priceText);
                } else {
                    this.mPrice.setText("$" + goods.getPrice());
                }
                this.mPrice.setOnClickListener(this.mItemClickListener);
                this.mPrice.setTag(goods);
            }
            convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.app_white));
            return convertView;
        }
    }

    public ArrayList<Goods> getGoodsData() {
        return this.mGoodsData;
    }

    public void setGoodsData(ArrayList<Goods> mGoodsData) {
        this.mGoodsData = mGoodsData;
    }

    public void setItemClickListener(OnClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
