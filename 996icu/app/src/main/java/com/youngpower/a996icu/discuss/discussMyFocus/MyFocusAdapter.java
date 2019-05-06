package com.youngpower.a996icu.discuss.discussMyFocus;

import android.content.Context;
import android.view.ViewGroup;

import com.youngpower.a996icu.base.BaseAdapter;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.bean.MyFocusDiscussBean;


public class MyFocusAdapter extends BaseAdapter<MyFocusDiscussBean.Items> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new MyFocusVH(context, parent);
    }
}
