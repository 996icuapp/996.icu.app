package com.youngpower.a996icu.discuss.discussHall;

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
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.base.OnItemClickListener;
import com.youngpower.a996icu.bean.HallDiscussBean;
import com.youngpower.a996icu.discussdetail.DiscussDetailActivity;
import com.youngpower.a996icu.model.BaseResponse;
import com.youngpower.a996icu.model.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DiscussHallFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_discuss)
    RecyclerView mRvDiscuss;
    @BindView(R.id.srl_discuss)
    SwipeRefreshLayout mSrlDiscuss;
    Unbinder unbinder;
    private DiscussHallAdapter mDiscussAdapter;

    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private static final int FIRST_PAGE = 1;
    private int pageIndex = FIRST_PAGE;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragnment_hall, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDiscussAdapter = new DiscussHallAdapter();
        mDiscussAdapter.setFooterValue(new Object());
        mDiscussAdapter.hasFooter(true);
        mRvDiscuss.setAdapter(mDiscussAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvDiscuss.setLayoutManager(mLayoutManager);
        mRvDiscuss.setItemAnimator(new DefaultItemAnimator());

        mDiscussAdapter.setOnClickListener(new OnItemClickListener<HallDiscussBean.Items>() {
            @Override
            public void onItemClick(HallDiscussBean.Items itemValue, int viewID, int position) {
                DiscussDetailActivity.start(getActivity(), itemValue.getId());
            }
        });

        mSrlDiscuss.setOnRefreshListener(this);
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        mSrlDiscuss.setColorSchemeResources(R.color.colorAccent);

        mRvDiscuss.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem + 1 == mDiscussAdapter.getItemCount()) {
                        pageIndex++;
                        requestData(pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        pageIndex = FIRST_PAGE;
        requestData(pageIndex);
    }

    public void requestData(final int pageIndex) {
        RetrofitClient.buildService(ApiService.class).getHallList(pageIndex, 20).enqueue(new Callback<BaseResponse<HallDiscussBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<HallDiscussBean>> call, Response<BaseResponse<HallDiscussBean>> response) {
                if (Util.isResponseOk(response)) {
                    final HallDiscussBean bean = response.body().getData();
                    if (response.body().getCode() > 0) {
                        Util.showToast(response.body().getMessage());
                    }
                    if (bean == null || bean.getItems() == null) {
                        Util.showToast(Util.getString(R.string.load_fail));
                    } else {

                        if (mDiscussAdapter != null) {
                            if (pageIndex == FIRST_PAGE) {
                                mDiscussAdapter.refreshData(bean.getItems());
                            } else {
                                mDiscussAdapter.addData(bean.getItems());
                            }
                        }
                        if (mSrlDiscuss != null)
                            mSrlDiscuss.setRefreshing(false);
                    }
                } else {
                    Util.showToast(Util.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<HallDiscussBean>> call, Throwable t) {
                Util.showToast(Util.getString(R.string.load_fail));
            }
        });
    }


    @Override
    public void onRefresh() {
        pageIndex = FIRST_PAGE;
        requestData(pageIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData(pageIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
