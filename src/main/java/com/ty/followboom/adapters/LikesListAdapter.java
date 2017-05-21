package com.ty.followboom.adapters;

import android.content.Context;
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

public class LikesListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Goods> mGoodsData;
    private OnClickListener mItemClickListener;

    private static class ViewHolder {
        TextView mPrice;
        TextView mTitle;

        private ViewHolder() {
        }
    }

    public LikesListAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        if (this.mGoodsData == null) {
            return 0;
        }
        return this.mGoodsData.size();
    }

    public Goods getItem(int position) {
        return (Goods) this.mGoodsData.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.adapter_likes_list, parent, false);
            holder = new ViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.title);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Goods goods = getItem(position);
        if (2 == goods.getGoodsType()) {
            holder.mTitle.setText("Get " + goods.getTitle().replace(" {}", "") + " Likes");
        } else if (5 == goods.getGoodsType()) {
            holder.mTitle.setText("Get " + goods.getTitle().replace("{} ", "") + " Views");
        }
        if (goods.getPaymentType() == 0) {
            holder.mPrice.setText(VLTools.removePointIfHave(goods.getPrice() + "") + " coins");
            holder.mPrice.setTag(goods);
            holder.mPrice.setOnClickListener(this.mItemClickListener);
        } else if (goods.getPaymentType() == 1) {
            convertView.setVisibility(8);
        }
        return convertView;
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
