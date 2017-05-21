package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import com.nativex.network.volley.DefaultRetryPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class HeaderGridView extends GridView {
    public static boolean DEBUG = false;
    private static final String LOG_TAG = "GridViewHeaderAndFooter";
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList();
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList();
    private ItemClickHandler mItemClickHandler;
    private int mNumColumns = -1;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private ListAdapter mOriginalAdapter;
    private int mRowHeight = -1;
    private View mViewForMeasureRowHeight = null;

    private static class FixedViewInfo {
        public Object data;
        public boolean isSelectable;
        public View view;
        public ViewGroup viewContainer;

        private FixedViewInfo() {
        }
    }

    private class FullWidthFixedViewLayout extends FrameLayout {
        public FullWidthFixedViewLayout(Context context) {
            super(context);
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            int realLeft = HeaderGridView.this.getPaddingLeft() + getPaddingLeft();
            if (realLeft != left) {
                offsetLeftAndRight(realLeft - left);
            }
            super.onLayout(changed, left, top, right, bottom);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec((HeaderGridView.this.getMeasuredWidth() - HeaderGridView.this.getPaddingLeft()) - HeaderGridView.this.getPaddingRight(), MeasureSpec.getMode(widthMeasureSpec)), heightMeasureSpec);
        }
    }

    private static class HeaderViewGridAdapter implements WrapperListAdapter, Filterable {
        static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST = new ArrayList();
        private final ListAdapter mAdapter;
        boolean mAreAllFixedViewsSelectable;
        private boolean mCacheFirstHeaderView = false;
        private boolean mCachePlaceHoldView = true;
        private final DataSetObservable mDataSetObservable = new DataSetObservable();
        ArrayList<FixedViewInfo> mFooterViewInfos;
        ArrayList<FixedViewInfo> mHeaderViewInfos;
        private final boolean mIsFilterable;
        private int mNumColumns = 1;
        private int mRowHeight = -1;

        public HeaderViewGridAdapter(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footViewInfos, ListAdapter adapter) {
            boolean z = true;
            this.mAdapter = adapter;
            this.mIsFilterable = adapter instanceof Filterable;
            if (headerViewInfos == null) {
                this.mHeaderViewInfos = EMPTY_INFO_LIST;
            } else {
                this.mHeaderViewInfos = headerViewInfos;
            }
            if (footViewInfos == null) {
                this.mFooterViewInfos = EMPTY_INFO_LIST;
            } else {
                this.mFooterViewInfos = footViewInfos;
            }
            if (!(areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos))) {
                z = false;
            }
            this.mAreAllFixedViewsSelectable = z;
        }

        public void setNumColumns(int numColumns) {
            if (numColumns >= 1 && this.mNumColumns != numColumns) {
                this.mNumColumns = numColumns;
                notifyDataSetChanged();
            }
        }

        public void setRowHeight(int height) {
            this.mRowHeight = height;
        }

        public int getHeadersCount() {
            return this.mHeaderViewInfos.size();
        }

        public int getFootersCount() {
            return this.mFooterViewInfos.size();
        }

        public boolean isEmpty() {
            return this.mAdapter == null || this.mAdapter.isEmpty();
        }

        private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> infos) {
            if (infos != null) {
                Iterator it = infos.iterator();
                while (it.hasNext()) {
                    if (!((FixedViewInfo) it.next()).isSelectable) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean removeHeader(View v) {
            boolean z = false;
            for (int i = 0; i < this.mHeaderViewInfos.size(); i++) {
                if (((FixedViewInfo) this.mHeaderViewInfos.get(i)).view == v) {
                    this.mHeaderViewInfos.remove(i);
                    if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                        z = true;
                    }
                    this.mAreAllFixedViewsSelectable = z;
                    this.mDataSetObservable.notifyChanged();
                    return true;
                }
            }
            return false;
        }

        public boolean removeFooter(View v) {
            boolean z = false;
            for (int i = 0; i < this.mFooterViewInfos.size(); i++) {
                if (((FixedViewInfo) this.mFooterViewInfos.get(i)).view == v) {
                    this.mFooterViewInfos.remove(i);
                    if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                        z = true;
                    }
                    this.mAreAllFixedViewsSelectable = z;
                    this.mDataSetObservable.notifyChanged();
                    return true;
                }
            }
            return false;
        }

        public int getCount() {
            if (this.mAdapter != null) {
                return ((getFootersCount() + getHeadersCount()) * this.mNumColumns) + getAdapterAndPlaceHolderCount();
            }
            return (getFootersCount() + getHeadersCount()) * this.mNumColumns;
        }

        public boolean areAllItemsEnabled() {
            return this.mAdapter == null || (this.mAreAllFixedViewsSelectable && this.mAdapter.areAllItemsEnabled());
        }

        private int getAdapterAndPlaceHolderCount() {
            return (int) (Math.ceil((double) ((DefaultRetryPolicy.DEFAULT_BACKOFF_MULT * ((float) this.mAdapter.getCount())) / ((float) this.mNumColumns))) * ((double) this.mNumColumns));
        }

        public boolean isEnabled(int position) {
            int numHeadersAndPlaceholders = getHeadersCount() * this.mNumColumns;
            if (position < numHeadersAndPlaceholders) {
                boolean z;
                if (position % this.mNumColumns == 0 && ((FixedViewInfo) this.mHeaderViewInfos.get(position / this.mNumColumns)).isSelectable) {
                    z = true;
                } else {
                    z = false;
                }
                return z;
            }
            int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (this.mAdapter != null) {
                adapterCount = getAdapterAndPlaceHolderCount();
                if (adjPosition < adapterCount) {
                    if (adjPosition >= this.mAdapter.getCount() || !this.mAdapter.isEnabled(adjPosition)) {
                        return false;
                    }
                    return true;
                }
            }
            int footerPosition = adjPosition - adapterCount;
            if (footerPosition % this.mNumColumns == 0 && ((FixedViewInfo) this.mFooterViewInfos.get(footerPosition / this.mNumColumns)).isSelectable) {
                return true;
            }
            return false;
        }

        public Object getItem(int position) {
            int numHeadersAndPlaceholders = getHeadersCount() * this.mNumColumns;
            if (position >= numHeadersAndPlaceholders) {
                int adjPosition = position - numHeadersAndPlaceholders;
                int adapterCount = 0;
                if (this.mAdapter != null) {
                    adapterCount = getAdapterAndPlaceHolderCount();
                    if (adjPosition < adapterCount) {
                        if (adjPosition < this.mAdapter.getCount()) {
                            return this.mAdapter.getItem(adjPosition);
                        }
                        return null;
                    }
                }
                int footerPosition = adjPosition - adapterCount;
                if (footerPosition % this.mNumColumns == 0) {
                    return ((FixedViewInfo) this.mFooterViewInfos.get(footerPosition)).data;
                }
                return null;
            } else if (position % this.mNumColumns == 0) {
                return ((FixedViewInfo) this.mHeaderViewInfos.get(position / this.mNumColumns)).data;
            } else {
                return null;
            }
        }

        public long getItemId(int position) {
            int numHeadersAndPlaceholders = getHeadersCount() * this.mNumColumns;
            if (this.mAdapter != null && position >= numHeadersAndPlaceholders) {
                int adjPosition = position - numHeadersAndPlaceholders;
                if (adjPosition < this.mAdapter.getCount()) {
                    return this.mAdapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        public boolean hasStableIds() {
            return this.mAdapter != null && this.mAdapter.hasStableIds();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (HeaderGridView.DEBUG) {
                String str = HeaderGridView.LOG_TAG;
                String str2 = "getView: %s, reused: %s";
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(position);
                objArr[1] = Boolean.valueOf(convertView == null);
                Log.d(str, String.format(str2, objArr));
            }
            int numHeadersAndPlaceholders = getHeadersCount() * this.mNumColumns;
            if (position < numHeadersAndPlaceholders) {
                View headerViewContainer = ((FixedViewInfo) this.mHeaderViewInfos.get(position / this.mNumColumns)).viewContainer;
                if (position % this.mNumColumns == 0) {
                    return headerViewContainer;
                }
                if (convertView == null) {
                    convertView = new View(parent.getContext());
                }
                convertView.setVisibility(4);
                convertView.setMinimumHeight(headerViewContainer.getHeight());
                return convertView;
            }
            int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (this.mAdapter != null) {
                adapterCount = getAdapterAndPlaceHolderCount();
                if (adjPosition < adapterCount) {
                    if (adjPosition < this.mAdapter.getCount()) {
                        return this.mAdapter.getView(adjPosition, convertView, parent);
                    }
                    if (convertView == null) {
                        convertView = new View(parent.getContext());
                    }
                    convertView.setVisibility(4);
                    convertView.setMinimumHeight(this.mRowHeight);
                    return convertView;
                }
            }
            int footerPosition = adjPosition - adapterCount;
            if (footerPosition < getCount()) {
                View footViewContainer = ((FixedViewInfo) this.mFooterViewInfos.get(footerPosition / this.mNumColumns)).viewContainer;
                if (position % this.mNumColumns == 0) {
                    return footViewContainer;
                }
                if (convertView == null) {
                    convertView = new View(parent.getContext());
                }
                convertView.setVisibility(4);
                convertView.setMinimumHeight(footViewContainer.getHeight());
                return convertView;
            }
            throw new ArrayIndexOutOfBoundsException(position);
        }

        public int getItemViewType(int position) {
            int adapterViewTypeStart;
            int numHeadersAndPlaceholders = getHeadersCount() * this.mNumColumns;
            if (this.mAdapter == null) {
                adapterViewTypeStart = 0;
            } else {
                adapterViewTypeStart = this.mAdapter.getViewTypeCount() - 1;
            }
            int type = -2;
            if (this.mCachePlaceHoldView && position < numHeadersAndPlaceholders) {
                if (position == 0 && this.mCacheFirstHeaderView) {
                    type = (((this.mHeaderViewInfos.size() + adapterViewTypeStart) + this.mFooterViewInfos.size()) + 1) + 1;
                }
                if (position % this.mNumColumns != 0) {
                    type = adapterViewTypeStart + ((position / this.mNumColumns) + 1);
                }
            }
            int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (this.mAdapter != null) {
                adapterCount = getAdapterAndPlaceHolderCount();
                if (adjPosition >= 0 && adjPosition < adapterCount) {
                    if (adjPosition < this.mAdapter.getCount()) {
                        type = this.mAdapter.getItemViewType(adjPosition);
                    } else if (this.mCachePlaceHoldView) {
                        type = (this.mHeaderViewInfos.size() + adapterViewTypeStart) + 1;
                    }
                }
            }
            if (this.mCachePlaceHoldView) {
                int footerPosition = adjPosition - adapterCount;
                if (footerPosition >= 0 && footerPosition < getCount() && footerPosition % this.mNumColumns != 0) {
                    type = ((this.mHeaderViewInfos.size() + adapterViewTypeStart) + 1) + ((footerPosition / this.mNumColumns) + 1);
                }
            }
            if (HeaderGridView.DEBUG) {
                Log.d(HeaderGridView.LOG_TAG, String.format("getItemViewType: pos: %s, result: %s", new Object[]{Integer.valueOf(position), Integer.valueOf(type), Boolean.valueOf(this.mCachePlaceHoldView), Boolean.valueOf(this.mCacheFirstHeaderView)}));
            }
            return type;
        }

        public int getViewTypeCount() {
            int count = this.mAdapter == null ? 1 : this.mAdapter.getViewTypeCount();
            if (this.mCachePlaceHoldView) {
                int offset = (this.mHeaderViewInfos.size() + 1) + this.mFooterViewInfos.size();
                if (this.mCacheFirstHeaderView) {
                    offset++;
                }
                count += offset;
            }
            if (HeaderGridView.DEBUG) {
                Log.d(HeaderGridView.LOG_TAG, String.format("getViewTypeCount: %s", new Object[]{Integer.valueOf(count)}));
            }
            return count;
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            this.mDataSetObservable.registerObserver(observer);
            if (this.mAdapter != null) {
                this.mAdapter.registerDataSetObserver(observer);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            this.mDataSetObservable.unregisterObserver(observer);
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(observer);
            }
        }

        public Filter getFilter() {
            if (this.mIsFilterable) {
                return ((Filterable) this.mAdapter).getFilter();
            }
            return null;
        }

        public ListAdapter getWrappedAdapter() {
            return this.mAdapter;
        }

        public void notifyDataSetChanged() {
            this.mDataSetObservable.notifyChanged();
        }
    }

    private class ItemClickHandler implements OnItemClickListener, OnItemLongClickListener {
        private ItemClickHandler() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (HeaderGridView.this.mOnItemClickListener != null) {
                int resPos = position - (HeaderGridView.this.getHeaderViewCount() * HeaderGridView.this.getNumColumnsCompatible());
                if (resPos >= 0) {
                    HeaderGridView.this.mOnItemClickListener.onItemClick(parent, view, resPos, id);
                }
            }
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (HeaderGridView.this.mOnItemLongClickListener != null) {
                int resPos = position - (HeaderGridView.this.getHeaderViewCount() * HeaderGridView.this.getNumColumnsCompatible());
                if (resPos >= 0) {
                    HeaderGridView.this.mOnItemLongClickListener.onItemLongClick(parent, view, resPos, id);
                }
            }
            return true;
        }
    }

    private void initHeaderGridView() {
    }

    public HeaderGridView(Context context) {
        super(context);
        initHeaderGridView();
    }

    public HeaderGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderGridView();
    }

    public HeaderGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeaderGridView();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ListAdapter adapter = getAdapter();
        if (adapter != null && (adapter instanceof HeaderViewGridAdapter)) {
            ((HeaderViewGridAdapter) adapter).setNumColumns(getNumColumnsCompatible());
            ((HeaderViewGridAdapter) adapter).setRowHeight(getRowHeight());
        }
    }

    public void setClipChildren(boolean clipChildren) {
    }

    public void setClipChildrenSupper(boolean clipChildren) {
        super.setClipChildren(false);
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        ListAdapter adapter = getAdapter();
        if (adapter == null || (adapter instanceof HeaderViewGridAdapter)) {
            LayoutParams lyp = v.getLayoutParams();
            FixedViewInfo info = new FixedViewInfo();
            FrameLayout fl = new FullWidthFixedViewLayout(getContext());
            if (lyp != null) {
                v.setLayoutParams(new FrameLayout.LayoutParams(lyp.width, lyp.height));
                fl.setLayoutParams(new AbsListView.LayoutParams(lyp.width, lyp.height));
            }
            fl.addView(v);
            info.view = v;
            info.viewContainer = fl;
            info.data = data;
            info.isSelectable = isSelectable;
            this.mHeaderViewInfos.add(info);
            if (adapter != null) {
                ((HeaderViewGridAdapter) adapter).notifyDataSetChanged();
                return;
            }
            return;
        }
        throw new IllegalStateException("Cannot add header view to grid -- setAdapter has already been called.");
    }

    public void addFooterView(View v) {
        addFooterView(v, null, true);
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        ListAdapter mAdapter = getAdapter();
        if (mAdapter == null || (mAdapter instanceof HeaderViewGridAdapter)) {
            LayoutParams lyp = v.getLayoutParams();
            FixedViewInfo info = new FixedViewInfo();
            FrameLayout fl = new FullWidthFixedViewLayout(getContext());
            if (lyp != null) {
                v.setLayoutParams(new FrameLayout.LayoutParams(lyp.width, lyp.height));
                fl.setLayoutParams(new AbsListView.LayoutParams(lyp.width, lyp.height));
            }
            fl.addView(v);
            info.view = v;
            info.viewContainer = fl;
            info.data = data;
            info.isSelectable = isSelectable;
            this.mFooterViewInfos.add(info);
            if (mAdapter != null) {
                ((HeaderViewGridAdapter) mAdapter).notifyDataSetChanged();
                return;
            }
            return;
        }
        throw new IllegalStateException("Cannot add header view to grid -- setAdapter has already been called.");
    }

    public int getHeaderViewCount() {
        return this.mHeaderViewInfos.size();
    }

    public int getFooterViewCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean removeHeaderView(View v) {
        if (this.mHeaderViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        ListAdapter adapter = getAdapter();
        if (adapter != null && ((HeaderViewGridAdapter) adapter).removeHeader(v)) {
            result = true;
        }
        removeFixedViewInfo(v, this.mHeaderViewInfos);
        return result;
    }

    public boolean removeFooterView(View v) {
        if (this.mFooterViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        ListAdapter adapter = getAdapter();
        if (adapter != null && ((HeaderViewGridAdapter) adapter).removeFooter(v)) {
            result = true;
        }
        removeFixedViewInfo(v, this.mFooterViewInfos);
        return result;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; i++) {
            if (((FixedViewInfo) where.get(i)).view == v) {
                where.remove(i);
                return;
            }
        }
    }

    @TargetApi(11)
    private int getNumColumnsCompatible() {
        if (VERSION.SDK_INT >= 11) {
            return super.getNumColumns();
        }
        try {
            Field numColumns = GridView.class.getDeclaredField("mNumColumns");
            numColumns.setAccessible(true);
            return numColumns.getInt(this);
        } catch (Exception e) {
            if (this.mNumColumns != -1) {
                return this.mNumColumns;
            }
            throw new RuntimeException("Can not determine the mNumColumns for this API platform, please call setNumColumns to set it.");
        }
    }

    @TargetApi(16)
    private int getColumnWidthCompatible() {
        if (VERSION.SDK_INT >= 16) {
            return super.getColumnWidth();
        }
        try {
            Field numColumns = GridView.class.getDeclaredField("mColumnWidth");
            numColumns.setAccessible(true);
            return numColumns.getInt(this);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mViewForMeasureRowHeight = null;
    }

    public void invalidateRowHeight() {
        this.mRowHeight = -1;
    }

    public int getHeaderHeight(int row) {
        if (row >= 0) {
            return ((FixedViewInfo) this.mHeaderViewInfos.get(row)).view.getMeasuredHeight();
        }
        return 0;
    }

    @TargetApi(16)
    public int getVerticalSpacing() {
        try {
            if (VERSION.SDK_INT >= 16) {
                return super.getVerticalSpacing();
            }
            Field field = GridView.class.getDeclaredField("mVerticalSpacing");
            field.setAccessible(true);
            return field.getInt(this);
        } catch (Exception e) {
            return 0;
        }
    }

    @TargetApi(16)
    public int getHorizontalSpacing() {
        try {
            if (VERSION.SDK_INT >= 16) {
                return super.getHorizontalSpacing();
            }
            Field field = GridView.class.getDeclaredField("mHorizontalSpacing");
            field.setAccessible(true);
            return field.getInt(this);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getRowHeight() {
        if (this.mRowHeight > 0) {
            return this.mRowHeight;
        }
        ListAdapter adapter = getAdapter();
        int numColumns = getNumColumnsCompatible();
        if (adapter == null || adapter.getCount() <= (this.mHeaderViewInfos.size() + this.mFooterViewInfos.size()) * numColumns) {
            return -1;
        }
        int mColumnWidth = getColumnWidthCompatible();
        View view = getAdapter().getView(this.mHeaderViewInfos.size() * numColumns, this.mViewForMeasureRowHeight, this);
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) view.getLayoutParams();
        if (p == null) {
            p = new AbsListView.LayoutParams(-1, -2, 0);
            view.setLayoutParams(p);
        }
        view.measure(getChildMeasureSpec(MeasureSpec.makeMeasureSpec(mColumnWidth, 1073741824), 0, p.width), getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, p.height));
        this.mViewForMeasureRowHeight = view;
        this.mRowHeight = view.getMeasuredHeight();
        return this.mRowHeight;
    }

    @TargetApi(11)
    public void tryToScrollToBottomSmoothly() {
        int lastPos = getAdapter().getCount() - 1;
        if (VERSION.SDK_INT >= 11) {
            smoothScrollToPositionFromTop(lastPos, 0);
        } else {
            setSelection(lastPos);
        }
    }

    @TargetApi(11)
    public void tryToScrollToBottomSmoothly(int duration) {
        int lastPos = getAdapter().getCount() - 1;
        if (VERSION.SDK_INT >= 11) {
            smoothScrollToPositionFromTop(lastPos, 0, duration);
        } else {
            setSelection(lastPos);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        this.mOriginalAdapter = adapter;
        if (this.mHeaderViewInfos.size() > 0 || this.mFooterViewInfos.size() > 0) {
            HeaderViewGridAdapter headerViewGridAdapter = new HeaderViewGridAdapter(this.mHeaderViewInfos, this.mFooterViewInfos, adapter);
            int numColumns = getNumColumnsCompatible();
            if (numColumns > 1) {
                headerViewGridAdapter.setNumColumns(numColumns);
            }
            headerViewGridAdapter.setRowHeight(getRowHeight());
            super.setAdapter(headerViewGridAdapter);
            return;
        }
        super.setAdapter(adapter);
    }

    public ListAdapter getOriginalAdapter() {
        return this.mOriginalAdapter;
    }

    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        this.mNumColumns = numColumns;
        ListAdapter adapter = getAdapter();
        if (adapter != null && (adapter instanceof HeaderViewGridAdapter)) {
            ((HeaderViewGridAdapter) adapter).setNumColumns(numColumns);
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
        super.setOnItemClickListener(getItemClickHandler());
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
        super.setOnItemLongClickListener(getItemClickHandler());
    }

    private ItemClickHandler getItemClickHandler() {
        if (this.mItemClickHandler == null) {
            this.mItemClickHandler = new ItemClickHandler();
        }
        return this.mItemClickHandler;
    }
}
