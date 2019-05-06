package com.youngpower.a996icu.list.getoff;

import android.content.Context;
import android.view.ViewGroup;

import com.youngpower.a996icu.base.BaseAdapter;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.bean.GetOffRankListBean;


public class GetOffAdapter extends BaseAdapter<GetOffRankListBean.Item> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new GetOffVH(context,parent);
    }
}
