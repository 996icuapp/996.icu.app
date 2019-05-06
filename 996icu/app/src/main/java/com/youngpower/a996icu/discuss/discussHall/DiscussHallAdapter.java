package com.youngpower.a996icu.discuss.discussHall;

import android.content.Context;
import android.view.ViewGroup;

import com.youngpower.a996icu.base.BaseAdapter;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.bean.HallDiscussBean;


public class DiscussHallAdapter extends BaseAdapter<HallDiscussBean.Items> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new DiscussHallVH(context, parent);
    }

    @Override
    public BaseViewHolder getFooterVH(ViewGroup parent) {
        return new DiscussHallBottomVH(parent.getContext() , parent);
    }
}
