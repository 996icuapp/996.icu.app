package com.youngpower.a996icu.list.list955;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.base.OnItemClickListener;
import com.youngpower.a996icu.bean.Company955Bean;

import java.util.Locale;

import butterknife.BindView;

class List995VH extends BaseViewHolder<Company955Bean.Item> {
    @BindView(R.id.text_company_name)
    TextView mTextCompanyName;
    @BindView(R.id.text_work_pattern)
    TextView mTextWorkPattern;
    @BindView(R.id.item)
    CardView mItem;
    @BindView(R.id.text_vote)
    TextView mTextVote;
    @BindView(R.id.tv_vote)
    TextView mTvVote;
    @BindView(R.id.lly_vote)
    LinearLayout mLlyVote;

    public List995VH(Context context, ViewGroup root) {
        super(context, root, R.layout.item_list_995);
    }

    @Override
    protected void bindData(final Company955Bean.Item itemValue, final int position, final OnItemClickListener listener) {
        mTextCompanyName.setText(itemValue.getName());
        mTextWorkPattern.setText("工作模式："+ itemValue.getWorkPattern());
        mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(itemValue, mItem.getId(), position);
            }
        });

        mTextVote.setText(itemValue.getVoteNum() + "人已投票");
        if (itemValue.isIsVote()) {
            mLlyVote.setBackgroundColor(Util.getColor(R.color.colorTextSubTitle));
            mTvVote.setText("已投票");
        } else {
            mLlyVote.setBackgroundColor(Util.getColor(R.color.colorAccent));
            mTvVote.setText("我要投票");
        }

        mTvVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(itemValue, v.getId(), position);
                if (!itemValue.isIsVote()) {
                    mLlyVote.setBackgroundColor(Util.getColor(R.color.colorTextSubTitle));
                    mTvVote.setText("已投票");
                    mTextVote.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%d", itemValue.getVoteNum() + 1) + "人已投票");
                }
            }
        });
    }
}
