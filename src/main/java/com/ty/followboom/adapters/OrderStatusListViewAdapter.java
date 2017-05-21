package com.ty.followboom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ty.followboom.entities.OrderStatus;
import com.ty.followboom.helpers.PicassoHelper;
import com.ty.instaview.R;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderStatusListViewAdapter extends BaseAdapter {
    private Context mContext;
    private OnClickListener mItemClickListener;
    private ArrayList<OrderStatus> mOrderStatus;

    private static class ViewHolder {
        ImageView mImageView;
        TextView mOrderStatus;
        TextView mOrderTime;
        ProgressBar mProgressBar;
        TextView mProgressContent;

        private ViewHolder() {
        }
    }

    public OrderStatusListViewAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        if (this.mOrderStatus == null) {
            return 0;
        }
        return this.mOrderStatus.size();
    }

    public OrderStatus getItem(int position) {
        if (this.mOrderStatus == null) {
            return null;
        }
        return (OrderStatus) this.mOrderStatus.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.adapter_order_status_list, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.mOrderStatus = (TextView) convertView.findViewById(R.id.order_status);
            holder.mOrderTime = (TextView) convertView.findViewById(R.id.order_time);
            holder.mProgressContent = (TextView) convertView.findViewById(R.id.progress_content);
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.progressbar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderStatus orderStatus = getItem(position);
        PicassoHelper.setImageView(this.mContext, holder.mImageView, orderStatus.getThumbnailUrl(), R.drawable.placeholder);
        if (orderStatus.getOrderKind().contains("like")) {
            holder.mOrderStatus.setCompoundDrawablesWithIntrinsicBounds(this.mContext.getResources().getDrawable(R.drawable.order_kind_likes), null, null, null);
        } else if (orderStatus.getOrderKind().contains("follow")) {
            holder.mOrderStatus.setCompoundDrawablesWithIntrinsicBounds(this.mContext.getResources().getDrawable(R.drawable.order_kind_followers), null, null, null);
        } else if (orderStatus.getOrderKind().contains("loop")) {
            holder.mOrderStatus.setCompoundDrawablesWithIntrinsicBounds(this.mContext.getResources().getDrawable(R.drawable.order_kind_views), null, null, null);
        }
        holder.mOrderTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Timestamp.valueOf(orderStatus.getCreateTime())));
        holder.mProgressContent.setText(orderStatus.getFinishedCount() + "/" + orderStatus.getTotalCount());
        holder.mProgressBar.setProgress((Integer.parseInt(orderStatus.getFinishedCount()) * 100) / Integer.parseInt(orderStatus.getTotalCount()));
        convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.app_white));
        return convertView;
    }

    public void setItemClickListener(OnClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public ArrayList<OrderStatus> getOrderStatus() {
        return this.mOrderStatus;
    }

    public void setOrderStatus(ArrayList<OrderStatus> orderStatus) {
        this.mOrderStatus = orderStatus;
    }
}
