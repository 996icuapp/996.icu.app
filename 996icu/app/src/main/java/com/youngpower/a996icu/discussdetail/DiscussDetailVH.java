package com.youngpower.a996icu.discussdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.base.OnItemClickListener;
import com.youngpower.a996icu.bean.DiscussDetailBean;

import butterknife.BindView;


public class DiscussDetailVH extends BaseViewHolder<DiscussDetailBean.CommentsList> {

    @BindView(R.id.tv_floor)
    TextView mTvFloor;
    @BindView(R.id.tv_comment_content)
    TextView mTvCommentContent;
    @BindView(R.id.tv_up_owner)
    TextView mTvUpOwner;

    public DiscussDetailVH(Context context, ViewGroup root) {
        super(context, root, R.layout.item_comment);
    }

    @Override
    protected void bindData(DiscussDetailBean.CommentsList itemValue, int position, OnItemClickListener listener) {
        mTvFloor.setText("#" + (DiscussDetailAdapter.sCommentLength - position));
        mTvCommentContent.setText(itemValue.getContent());

        if (itemValue.isIsPublisher()) {
            mTvUpOwner.setVisibility(View.VISIBLE);
        } else {
            mTvUpOwner.setVisibility(View.GONE);
        }
    }
}
