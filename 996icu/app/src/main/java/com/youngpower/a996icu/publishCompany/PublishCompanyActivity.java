package com.youngpower.a996icu.publishCompany;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.youngpower.a996icu.ApiService;
import com.youngpower.a996icu.R;
import com.youngpower.a996icu.UserUtil;
import com.youngpower.a996icu.Util;
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

public class PublishCompanyActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_company_name)
    EditText mEtCompanyName;
    @BindView(R.id.et_work_pattern)
    EditText mEtWorkPattern;
    @BindView(R.id.et_city)
    EditText mEtCity;
    @BindView(R.id.btn_sure)
    AppCompatButton mBtnSure;
    @BindView(R.id.et_content)
    EditText mEtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_company);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishCompanyActivity.this.finish();
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PublishCompanyActivity.class);
        context.startActivity(starter);
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {

        if (UserUtil.getUid() == -1) {
            Util.showToast("用户信息错误，请退出重进");
            return;
        }

        final String content = mEtContent.getText().toString().trim();
        String company = mEtCompanyName.getText().toString().trim();
        String workPattern = mEtWorkPattern.getText().toString().trim();
        String city = mEtCity.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(company) && !TextUtils.isEmpty(workPattern) && !TextUtils.isEmpty(city)) {

            mBtnSure.setText("发布中，请稍候...");
            mBtnSure.setEnabled(false);
            mBtnSure.setAlpha(0.5f);

            RetrofitClient.buildService(ApiService.class).publishCompany(UserUtil.getUid(), company, city, workPattern, city).enqueue(new Callback<BaseResponse<Object>>() {
                @Override
                public void onResponse(Call<BaseResponse<Object>> call, final Response<BaseResponse<Object>> response) {
                    if (Util.isResponseOk(response)) {
                        Task.call(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                mBtnSure.setEnabled(true);
                                mBtnSure.setText("写完了");
                                mBtnSure.setAlpha(1.0f);
                                if (response.body().getCode() > 0) {
                                    Util.showToast(response.body().getMessage());
                                } else {
                                    Util.showToast("爆料成功，请等待审核");
                                }
                                Util.hideSoftKeyBoard(mBtnSure);
                                if (mBtnSure.getContext() instanceof PublishCompanyActivity) {
                                    ((PublishCompanyActivity) mBtnSure.getContext()).finish();
                                }
                                return null;
                            }
                        }, Task.UI_THREAD_EXECUTOR);
                    } else {
                        Util.showToast("爆料失败，请稍后重试");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                    Util.showToast("爆料失败，请稍后重试");
                }
            });
        }
    }


}
