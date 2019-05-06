package com.youngpower.a996icu.publishDiscuss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.youngpower.a996icu.ApiService;
import com.youngpower.a996icu.R;
import com.youngpower.a996icu.UserUtil;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.base.BaseActivity;
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


public class PublishDiscussActivity extends BaseActivity {


    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.btn_sure)
    AppCompatButton mBtnSure;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_publish_discuss;
    }

    @Override
    public void initView() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        if (UserUtil.getUid() == -1) {
            Util.showToast("用户信息异常，请退出重进");
            UserUtil.initUser();
            return;
        }


        final String content = mEtContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {

            if (UserUtil.getCurUser() == null) {
                Util.showToast("发布失败，请5s后重试");
                UserUtil.initUser();
                return;
            }

            mBtnSure.setText("发布中，请稍候...");
            mBtnSure.setEnabled(false);
            mBtnSure.setAlpha(0.5f);


            RetrofitClient.buildService(ApiService.class).publishDiscuss(UserUtil.getUid(), content).enqueue(new Callback<BaseResponse<Object>>() {
                @Override
                public void onResponse(Call<BaseResponse<Object>> call, final Response<BaseResponse<Object>> response) {

                    Task.call(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            mBtnSure.setEnabled(true);
                            mBtnSure.setText("写完了");
                            mBtnSure.setAlpha(1.0f);
                            if (Util.isResponseOk(response)) {
                                if (response.body().getCode() > 0) {
                                    Util.showToast(response.body().getMessage());
                                } else {
                                    Util.showToast("发布成功");
                                }
                                Util.hideSoftKeyBoard(mBtnSure);
                                if (mBtnSure.getContext() instanceof PublishDiscussActivity) {
                                    ((PublishDiscussActivity) mBtnSure.getContext()).finish();
                                }
                            } else {
                                Util.showToast("发布失败");
                            }
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                }

                @Override
                public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                    Task.call(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            mBtnSure.setEnabled(true);
                            mBtnSure.setText("写完了");
                            mBtnSure.setAlpha(1.0f);
                            Util.showToast("发布失败");
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                }
            });
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PublishDiscussActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
