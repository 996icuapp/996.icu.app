package com.youngpower.a996icu.discuss.discussMyFocus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youngpower.a996icu.ApiService;
import com.youngpower.a996icu.R;
import com.youngpower.a996icu.UserUtil;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.base.OnItemClickListener;
import com.youngpower.a996icu.bean.MyFocusDiscussBean;
import com.youngpower.a996icu.discussdetail.DiscussDetailActivity;
import com.youngpower.a996icu.model.BaseResponse;
import com.youngpower.a996icu.model.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFocusFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_discuss)
    RecyclerView mRvDiscuss;
    @BindView(R.id.srl_discuss)
    SwipeRefreshLayout mSrlDiscuss;
    Unbinder unbinder;
    private MyFocusAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragnment_hall, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MyFocusAdapter();
        mRvDiscuss.setAdapter(mAdapter);
        mRvDiscuss.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvDiscuss.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnClickListener(new OnItemClickListener<MyFocusDiscussBean.Items>() {
            @Override
            public void onItemClick(MyFocusDiscussBean.Items itemValue, int viewID, int position) {
                DiscussDetailActivity.start(getActivity(), itemValue.getId());
            }
        });

        mSrlDiscuss.setOnRefreshListener(this);
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        mSrlDiscuss.setColorSchemeResources(R.color.colorAccent);
    }

    public void requestData() {
        if (UserUtil.getUid() == -1) {
            Util.showToast("用户信息错误，请退出重进");
            return;
        }

        RetrofitClient.buildService(ApiService.class).getMyFocusList(UserUtil.getUid(), 1, 500).enqueue(new Callback<BaseResponse<MyFocusDiscussBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<MyFocusDiscussBean>> call, Response<BaseResponse<MyFocusDiscussBean>> response) {
                if (Util.isResponseOk(response)) {
                    final MyFocusDiscussBean bean = response.body().getData();
                    if (response.body().getCode() > 0) {
                        Util.showToast(response.body().getMessage());
                    }
                    if (bean == null || bean.getItems() == null) {
                        Util.showToast(Util.getString(R.string.load_fail));
                    } else {

                        if (mAdapter != null) {
                            mAdapter.refreshData(bean.getItems());
                        }
                        if (mSrlDiscuss != null)
                            mSrlDiscuss.setRefreshing(false);
                    }
                } else {
                    Util.showToast(Util.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<MyFocusDiscussBean>> call, Throwable t) {
                Util.showToast(Util.getString(R.string.load_fail));
            }
        });
    }


    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
