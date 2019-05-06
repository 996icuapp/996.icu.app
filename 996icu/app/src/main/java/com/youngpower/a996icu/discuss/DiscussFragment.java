package com.youngpower.a996icu.discuss;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.about.AboutUsActivity;
import com.youngpower.a996icu.discuss.discussHall.DiscussHallFragment;
import com.youngpower.a996icu.discuss.discussMyFocus.MyFocusFragment;
import com.youngpower.a996icu.publishDiscuss.PublishDiscussActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class DiscussFragment extends Fragment {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.btn_publish)
    FloatingActionButton mBtnPublish;
    Unbinder unbinder;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discuss, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        initVP();

        mToolbar.inflateMenu(R.menu.menu_setting);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item_about:
                        AboutUsActivity.start(getActivity());
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void initVP() {
        mViewpager.setAdapter(new DiscussAdapter(getActivity().getSupportFragmentManager(), mTitleList, mFragmentList));
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initList() {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        mTitleList.add("大厅");
        mTitleList.add("我的");

        mFragmentList.add(new DiscussHallFragment());
        mFragmentList.add(new MyFocusFragment());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_publish)
    public void onViewClicked() {
        PublishDiscussActivity.start(getActivity());
    }
}
