package com.youngpower.a996icu.discussdetail;

import android.content.Context;
import android.view.ViewGroup;

import com.youngpower.a996icu.base.BaseAdapter;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.bean.DiscussDetailBean;

import java.util.List;


public class DiscussDetailAdapter extends BaseAdapter<DiscussDetailBean.CommentsList> {

    public static int sCommentLength;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new DiscussDetailVH(context, parent);
    }

    @Override
    public void refreshData(List<DiscussDetailBean.CommentsList> valueList) {
        super.refreshData(valueList);
        sCommentLength = valueList.size();
    }
}
