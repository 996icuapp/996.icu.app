package com.youngpower.a996icu.list.getoff;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.base.OnItemClickListener;
import com.youngpower.a996icu.bean.GetOffRankListBean;

import butterknife.BindView;

class GetOffVH extends BaseViewHolder<GetOffRankListBean.Item> {


    @BindView(R.id.text_name)
    TextView mTextName;
    @BindView(R.id.text_off_time)
    TextView mTextOffTime;
    @BindView(R.id.text_people_num)
    TextView mTextPeopleNum;
    @BindView(R.id.tv_rank)
    TextView mTvRank;

    public GetOffVH(Context context, ViewGroup root) {
        super(context, root, R.layout.item_get_off);
    }

    @Override
    protected void bindData(GetOffRankListBean.Item itemValue, int position, OnItemClickListener listener) {
        mTextName.setText(itemValue.getName());
        mTextPeopleNum.setText("" + itemValue.getPeopleNum());
        mTextOffTime.setText(itemValue.getAverageGetOffTime());
        mTvRank.setText("No." + (position + 1));
    }
}
