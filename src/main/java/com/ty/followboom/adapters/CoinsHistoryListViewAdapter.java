package com.ty.followboom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ty.followboom.entities.CoinsHistory;
import com.ty.instaview.R;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CoinsHistoryListViewAdapter extends BaseAdapter {
    private ArrayList<CoinsHistory> mCoinsHistory;
    private Context mContext;

    private static class ViewHolder {
        TextView mCoinsCount;
        TextView mHistoryTime;
        TextView mHistoryType;

        private ViewHolder() {
        }
    }

    public CoinsHistoryListViewAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        if (this.mCoinsHistory == null) {
            return 0;
        }
        return this.mCoinsHistory.size();
    }

    public CoinsHistory getItem(int position) {
        if (this.mCoinsHistory == null) {
            return null;
        }
        return (CoinsHistory) this.mCoinsHistory.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.adapter_coins_history_item, parent, false);
            holder = new ViewHolder();
            holder.mHistoryType = (TextView) convertView.findViewById(R.id.history_type);
            holder.mHistoryTime = (TextView) convertView.findViewById(R.id.history_time);
            holder.mCoinsCount = (TextView) convertView.findViewById(R.id.coins_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CoinsHistory coinsHistory = getItem(position);
        holder.mHistoryType.setText(coinsHistory.getChangeReason());
        holder.mHistoryTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Timestamp.valueOf(coinsHistory.getCreatetime())));
        if (Integer.parseInt(coinsHistory.getChangeAmount()) >= 0) {
            holder.mCoinsCount.setText("+" + coinsHistory.getChangeAmount());
            holder.mCoinsCount.setTextColor(this.mContext.getResources().getColor(R.color.app_green));
        } else {
            holder.mCoinsCount.setText(coinsHistory.getChangeAmount());
            holder.mCoinsCount.setTextColor(this.mContext.getResources().getColor(R.color.app_red));
        }
        convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.app_white));
        return convertView;
    }

    public ArrayList<CoinsHistory> getCoinsHistory() {
        return this.mCoinsHistory;
    }

    public void setCoinsHistory(ArrayList<CoinsHistory> coinsHistory) {
        this.mCoinsHistory = coinsHistory;
    }
}
