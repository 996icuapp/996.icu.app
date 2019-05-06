package com.youngpower.a996icu.discussdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.youngpower.a996icu.ApiService;
import com.youngpower.a996icu.R;
import com.youngpower.a996icu.UserUtil;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.base.BaseActivity;
import com.youngpower.a996icu.bean.DiscussDetailBean;
import com.youngpower.a996icu.model.BaseResponse;
import com.youngpower.a996icu.model.RetrofitClient;

import java.util.concurrent.Callable;

import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DiscussDetailActivity extends BaseActivity {

    private static final String DISCUSS_ID = "discussId";
    @BindView(R.id.tv_discuss_content)
    TextView mTvDiscussContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sv_comment)
    NestedScrollView mSvComment;

    private DiscussDetailBean mDiscussDetailBean;
    private int mDiscussId;

    @BindView(R.id.tv_publish_time)
    TextView mTvPublishTime;
    @BindView(R.id.tv_comment_num)
    TextView mTvCommentNum;
    @BindView(R.id.rv_discuss)
    RecyclerView mRvDiscuss;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.btn_send_comment)
    AppCompatButton mBtnSendComment;

    private DiscussDetailAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_discuss_detail;
    }

    @Override
    public void initView() {
        final Intent intent = getIntent();
        if (intent != null) {
            initRecyclerView();
            mDiscussId = intent.getIntExtra(DISCUSS_ID, -1);
            requestData();

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void requestData() {
        if (mDiscussId == -1) {
            Util.showToast("帖子异常");
            return;
        }

        RetrofitClient.buildService(ApiService.class).getDiscussDetail(mDiscussId).enqueue(new Callback<BaseResponse<DiscussDetailBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<DiscussDetailBean>> call, Response<BaseResponse<DiscussDetailBean>> response) {
                if (Util.isResponseOk(response)) {
                    mDiscussDetailBean = response.body().getData();
                    Util.refreshDiscussCommentNum(mDiscussId + "", mDiscussDetailBean.getCommentNum());
                    refresh();
                } else {
                    Util.showToast(Util.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DiscussDetailBean>> call, Throwable t) {
                Util.showToast(Util.getString(R.string.load_fail));
            }
        });
    }


    private void initRecyclerView() {
        mAdapter = new DiscussDetailAdapter();
        mRvDiscuss.setAdapter(mAdapter);
        mRvDiscuss.setLayoutManager(new LinearLayoutManager(this));
        mRvDiscuss.setItemAnimator(new DefaultItemAnimator());
        mSvComment.scrollTo(0, 0);
    }


    @OnClick(R.id.btn_send_comment)
    public void onViewClicked() {
        if (!UserUtil.isUserEnable()) {
            Util.showToast("你的账户已被封禁，无法发布内容");
            return;
        }

        if (UserUtil.getUid() == -1) {
            Util.showToast("用户信息异常，请退出重进");
            UserUtil.initUser();
            return;
        }

        final String commentContent = mEtComment.getText().toString().trim();
        if (!TextUtils.isEmpty(commentContent)) {
            mBtnSendComment.setEnabled(false);
            mBtnSendComment.setAlpha(0.5f);

            RetrofitClient.buildService(ApiService.class).publishComment(UserUtil.getUid(), mDiscussId, commentContent).enqueue(new Callback<BaseResponse<Object>>() {
                @Override
                public void onResponse(Call<BaseResponse<Object>> call, final Response<BaseResponse<Object>> response) {
                    if (Util.isResponseOk(response)) {
                        Task.call(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                mBtnSendComment.setEnabled(true);
                                mBtnSendComment.setAlpha(1.0f);
                                mEtComment.setText("");
                                if (response.body().getCode() > 0) {
                                    Util.showToast(response.body().getMessage());
                                } else {
                                    Util.showToast("评论成功");
                                }
                                requestData();
                                return null;
                            }
                        }, Task.UI_THREAD_EXECUTOR);
                    } else {
                        Util.showToast(Util.getString(R.string.load_fail));
                        Task.call(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                mBtnSendComment.setEnabled(true);
                                mBtnSendComment.setAlpha(1.0f);
                                return null;
                            }
                        }, Task.UI_THREAD_EXECUTOR);
                    }

                }

                @Override
                public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                    Util.showToast(Util.getString(R.string.load_fail));
                    Task.call(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            mBtnSendComment.setEnabled(true);
                            mBtnSendComment.setAlpha(1.0f);
                            Util.showToast("评论失败");
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                }
            });
        }
    }

    private void refresh() {
        Task.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (mDiscussDetailBean == null) {
                    return null;
                }

                mAdapter.refreshData(mDiscussDetailBean.getCommentsList());

                mTvDiscussContent.setText(mDiscussDetailBean.getContent());
                mTvCommentNum.setText(String.valueOf(mDiscussDetailBean.getCommentNum()));
                mTvPublishTime.setText(Util.getFormattedTime(mDiscussDetailBean.getCreateAt()));
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    public static void start(Context context, int id) {
        Intent starter = new Intent(context, DiscussDetailActivity.class);
        starter.putExtra(DISCUSS_ID, id);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
