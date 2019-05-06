package com.youngpower.a996icu.list.list996;

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
import com.youngpower.a996icu.bean.Company996Bean;
import com.youngpower.a996icu.model.BaseResponse;
import com.youngpower.a996icu.model.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class List996Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "List996Fragment";
    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    Unbinder unbinder;
    @BindView(R.id.srl_list)
    SwipeRefreshLayout mSrlList;

    private List996Adapter mList996Adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list996, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRvList.setItemAnimator(new DefaultItemAnimator());
        mList996Adapter = new List996Adapter();
        mRvList.setAdapter(mList996Adapter);
        mList996Adapter.setOnClickListener(new OnItemClickListener<Company996Bean.Items>() {
            @Override
            public void onItemClick(Company996Bean.Items itemValue, int viewID, int position) {
                switch (viewID) {
                    case R.id.tv_vote:
                        doVote(itemValue);
                        break;
                    case R.id.item:
                        break;
                    default:
                        break;
                }
            }
        });
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        mSrlList.setColorSchemeResources(R.color.colorAccent);

        mSrlList.setOnRefreshListener(this);
        requestData();

    }

    private void doVote(Company996Bean.Items itemValue) {
        if (itemValue.isIsVote()) {
            Util.showToast("已经投过票了");
            return;
        }

        if (UserUtil.getUid() == -1) {
            Util.showToast("用户信息错误，请退出重进");
            return;
        }

        RetrofitClient.buildService(ApiService.class).vote(UserUtil.getUid(), itemValue.getId()).enqueue(new Callback<BaseResponse<Object>>() {
            @Override
            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                if (Util.isResponseOk(response) && response.body().getCode() > 0) {
                    Util.showToast(response.body().getMessage());
                }
                requestData();
            }

            @Override
            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                requestData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    private void requestData() {
        if (UserUtil.getUid() == -1) {
            Util.showToast("用户信息错误，请退出重进");
            return;
        }

        RetrofitClient.buildService(ApiService.class).get996CompanyList(UserUtil.getUid(), 1, 500).enqueue(new Callback<BaseResponse<Company996Bean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Company996Bean>> call, Response<BaseResponse<Company996Bean>> response) {
                if (Util.isResponseOk(response)) {
                    if (mSrlList != null) {
                        mSrlList.setRefreshing(false);
                    }
                    if (response.body().getCode() > 0) {
                        Util.showToast(response.body().getMessage());
                    }
                    final Company996Bean bean = response.body().getData();
                    if (bean == null || bean.getItems() == null) {
                        Util.showToast(Util.getString(R.string.load_fail));
                    } else {

                        if (mList996Adapter != null) {
                            mList996Adapter.refreshData(bean.getItems());
                        }
                    }
                } else {
                    Util.showToast(Util.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Company996Bean>> call, Throwable t) {
                Util.showToast(Util.getString(R.string.load_fail));
            }
        });
    }
}
