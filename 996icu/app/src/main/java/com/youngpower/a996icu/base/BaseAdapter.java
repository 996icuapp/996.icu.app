package com.youngpower.a996icu.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<V> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 666;
    private static final int FOOTER = 667;
    // container of the data
    private List<V> mValueList;
    // to handler click thing
    private OnItemClickListener<V> mOnItemClickListener;
    private ViewGroup mParent;
    // if the value is false , no header in recyclerview
    private boolean mHasHeader = false;
    // if the value is false , no footer in recyclerview
    private boolean mHasFooter = false;
    // header and value of header / footer
    private BaseViewHolder mHeaderVH;
    private BaseViewHolder mFooterVH;
    private Object mHeaderValue;
    private Object mFooterValue;

    public List<V> getValueList() {
        return mValueList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mParent = parent;

        if (mHasHeader && viewType == HEADER) {
            if (null == mHeaderVH) {
                throw new NullPointerException("Please invoke setHeaderVH()");
            }
            return mHeaderVH;
        } else if (mHasFooter && viewType == FOOTER) {
            BaseViewHolder viewHolder = getFooterVH(parent);
            if (null == viewHolder) {
                throw new NullPointerException("Please invoke setHeaderVH()");
            }
            return viewHolder;
        }
        return createViewHolder(parent.getContext(), parent);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasHeader && position == 0)
            return HEADER;
        else if (mHasFooter && null != mValueList && position == mValueList.size() - 1)
            return FOOTER;

        return super.getItemViewType(position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder viewHolder = (BaseViewHolder) holder;

        Object finalData = mValueList.get(position);
        int finalPosition = position;

        if (position == 0) {
            if (mHasHeader) {
                finalData = mHeaderValue;
            }
        } else if (position == mValueList.size() - 1) {
            if (mHasFooter) {
                finalData = mFooterValue;
            }
        } else {
            if (mHasHeader) {
                finalPosition = position - 1;
            }
        }
        viewHolder.setData(finalData, finalPosition, mOnItemClickListener);
    }

    /**
     * If you want to handler the click thing in outside , this method must be invoked .
     */
    public void setOnClickListener(OnItemClickListener<V> listener) {
        this.mOnItemClickListener = listener;
    }

    public void refreshData(List<V> valueList) {
        refreshData(valueList, null);
    }

    public void refreshData(List<V> valueList, Object headerValue) {
        refreshData(valueList, headerValue, null);
    }

    /**
     * Refresh list data , header data and footer data.
     *
     * @param valueList   new data list
     * @param headerValue header value
     * @param footerValue footer value
     */
    public void refreshData(List<V> valueList, Object headerValue, Object footerValue) {
        this.mValueList = valueList;

        if (headerValue != null)
            this.mHeaderValue = headerValue;
        if (footerValue != null)
            this.mFooterValue = footerValue;

        notifyDataSetChanged();
    }

    /**
     * Add some data to origin data list .
     * <p>
     * This method is always invoked when you get pageable data .
     *
     * @param newValueList target new data list
     */
    public void addData(List<V> newValueList) {
        if (null == this.mValueList)
            mValueList = new ArrayList<>();

        if (null != newValueList)
            this.mValueList.addAll(newValueList);
        notifyDataSetChanged();
    }

    public boolean isHasHeader() {
        return mHasHeader;
    }

    public boolean isHasFooter() {
        return mHasFooter;
    }

    public void hasHeader(boolean hasHeader) {
        this.mHasHeader = hasHeader;
    }

    public void hasFooter(boolean hasFooter) {
        this.mHasFooter = hasFooter;
    }

    public void setHeaderVH(BaseViewHolder headerVH) {
        this.mHeaderVH = headerVH;
    }

    public void setFooterVH(BaseViewHolder footerVH) {
        this.mFooterVH = footerVH;
    }

    public void setHeaderValue(Object headerValue) {
        this.mHeaderValue = headerValue;
    }

    public void setFooterValue(Object footerValue) {
        this.mFooterValue = footerValue;
    }

    @Override
    public int getItemCount() {
        int size;
        if (mHasHeader)
            size = mValueList == null ? 1 : mValueList.size() + 1;
        else
            size = mValueList == null ? 0 : mValueList.size();

        return size;
    }

    public void removeData(int position) {
        if (null == mValueList || position < 0 || position >= mValueList.size()) {
            return;
        }

        mValueList.remove(position);

        if (isHasHeader()) {
            notifyItemRemoved(position + 1);
            notifyItemRangeChanged(1, mValueList.size() + 1);
        } else {
            notifyItemRemoved(position);
            notifyItemRangeChanged(0, mValueList.size());
        }
    }

    public BaseViewHolder getFooterVH(ViewGroup parent) {
        return null;
    }

    /**
     * Get the viewGroup.
     * This method will return the para of {@link #onCreateViewHolder}
     *
     * @return
     */
    public ViewGroup getParent() {
        return mParent;
    }

    /**
     * Create ViewHolder
     */
    protected abstract BaseViewHolder createViewHolder(Context context, ViewGroup parent);
}
