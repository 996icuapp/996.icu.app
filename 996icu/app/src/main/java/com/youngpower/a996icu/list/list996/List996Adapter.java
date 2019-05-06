package com.youngpower.a996icu.list.list996;

import android.content.Context;
import android.view.ViewGroup;

import com.youngpower.a996icu.base.BaseAdapter;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.bean.Company996Bean;


public class List996Adapter extends BaseAdapter<Company996Bean.Items> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new List996VH(context, parent);
    }
}
