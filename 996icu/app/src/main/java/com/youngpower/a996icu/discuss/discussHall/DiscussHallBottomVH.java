package com.youngpower.a996icu.discuss.discussHall;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.base.OnItemClickListener;

import butterknife.BindView;


public class DiscussHallBottomVH extends BaseViewHolder {

    @BindView(R.id.tv_bottom_text)
    TextView mTvBottomText;
    @BindView(R.id.card_container)
    CardView mCardContainer;

    public DiscussHallBottomVH(Context context, ViewGroup root) {
        super(context, root, R.layout.item_bottom_refresh);
    }

    @Override
    protected void bindData(Object itemValue, int position, OnItemClickListener listener) {

    }
}
