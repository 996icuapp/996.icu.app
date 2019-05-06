package com.youngpower.a996icu.discuss.discussHall;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.base.OnItemClickListener;
import com.youngpower.a996icu.bean.HallDiscussBean;

import butterknife.BindView;


public class DiscussHallVH extends BaseViewHolder<HallDiscussBean.Items> {


    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_publish_time)
    TextView mTvPublishTime;
    @BindView(R.id.tv_comment_num)
    TextView mTvCommentNum;
    @BindView(R.id.lly_container)
    LinearLayout mLlyContainer;
    @BindView(R.id.tv_floor)
    TextView mTvFloor;
    @BindView(R.id.tv_new_comment)
    TextView mTvNewComment;
    @BindView(R.id.tv_top_reason)
    TextView mTvTopReason;
    @BindView(R.id.lly_top)
    LinearLayout mLlyTop;

    public DiscussHallVH(Context context, ViewGroup root) {
        super(context, root, R.layout.item_discuss);
    }

    @Override
    protected void bindData(final HallDiscussBean.Items itemValue, final int position, final OnItemClickListener listener) {
        mTvTitle.setText(itemValue.getContent());
        mTvPublishTime.setText(Util.getFormattedTime(itemValue.getUpdateAt()));
        mTvCommentNum.setText(String.valueOf(itemValue.getCommentNum()));
        mTvFloor.setText("#" + itemValue.getFloor());
        mLlyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.refreshDiscussCommentNum(itemValue.getId() + "", itemValue.getCommentNum());
                if (listener != null) {
                    listener.onItemClick(itemValue, v.getId(), position);
                }
            }
        });

        if (itemValue.isIsTop()) {
            mLlyTop.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(itemValue.getTopReason())) {
                mTvTopReason.setVisibility(View.GONE);
            } else {
                mTvTopReason.setVisibility(View.VISIBLE);
                mTvTopReason.setText("置顶理由："+itemValue.getTopReason());
            }
        } else {
            mLlyTop.setVisibility(View.GONE);
            mTvTopReason.setVisibility(View.GONE);
        }

        final int commentNum = Util.getCacheDiscussCommentNum(itemValue.getId() + "");
        // 点击过，把文字变灰
        if (commentNum != -1) {
            mTvTitle.setAlpha(0.3f);
            mTvTopReason.setAlpha(0.5f);
        } else {
            mTvTitle.setAlpha(1.0f);
            mTvTopReason.setAlpha(1.0f);
        }

        if (commentNum > 0 && itemValue.getCommentNum() > commentNum) {
            mTvNewComment.setVisibility(View.VISIBLE);
        } else {
            mTvNewComment.setVisibility(View.GONE);
        }
    }
}
