package com.youngpower.a996icu.discuss.discussMyFocus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.base.BaseViewHolder;
import com.youngpower.a996icu.base.OnItemClickListener;
import com.youngpower.a996icu.bean.MyFocusDiscussBean;

import butterknife.BindView;


public class MyFocusVH extends BaseViewHolder<MyFocusDiscussBean.Items> {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_tag)
    TextView mTvTag;
    @BindView(R.id.tv_new_comment)
    TextView mTvNewComment;
    @BindView(R.id.tv_publish_time)
    TextView mTvPublishTime;
    @BindView(R.id.tv_comment_num)
    TextView mTvCommentNum;
    @BindView(R.id.lly_container)
    LinearLayout mLlyContainer;

    public MyFocusVH(Context context, ViewGroup root) {
        super(context, root, R.layout.item_my_focus);
    }

    @Override
    protected void bindData(final MyFocusDiscussBean.Items itemValue, final int position, final OnItemClickListener listener) {
        mTvTitle.setText(itemValue.getContent());
        mTvPublishTime.setText(Util.getFormattedTime(itemValue.getUpdateAt()));
        mTvCommentNum.setText(String.valueOf(itemValue.getCommentNum()));
        mLlyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.refreshDiscussCommentNum(itemValue.getId() + "", itemValue.getCommentNum());
                if (listener != null) {
                    listener.onItemClick(itemValue, v.getId(), position);
                }
            }
        });



        final int commentNum = Util.getCacheDiscussCommentNum(itemValue.getId() + "");
        if (commentNum > 0 && itemValue.getCommentNum() > commentNum) {
            mTvNewComment.setVisibility(View.VISIBLE);
        }else{
            mTvNewComment.setVisibility(View.GONE);
        }
    }
}
