package com.youngpower.a996icu.list.list955;

import android.content.Context;
import android.view.ViewGroup;

import com.youngpower.a996icu.base.BaseAdapter;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.bean.Company955Bean;


public class List955Adapter extends BaseAdapter<Company955Bean.Item> {
    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent) {
        return new List995VH(context, parent);
    }
}
