package com.youngpower.a996icu.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.youngpower.a996icu.list.getoff.GetOffFragment;
import com.youngpower.a996icu.list.list955.List955Fragment;
import com.youngpower.a996icu.list.list996.List996Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";


    private final List<String> mTitleList = new ArrayList<>();
    private final List<Fragment> mFragmentList = new ArrayList<>();
    @BindView(R.id.tl_list)
    TabLayout mTlList;
    @BindView(R.id.vp_list)
    ViewPager mVpList;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragment();
        initTitle();
        mVpList.setAdapter(new ListViewpagerAdapter(getActivity().getSupportFragmentManager(), mTitleList, mFragmentList));
        mTlList.setupWithViewPager(mVpList);

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

    private void initFragment() {
        mFragmentList.add(new GetOffFragment());
        mFragmentList.add(new List996Fragment());
        mFragmentList.add(new List955Fragment());
    }

    private void initTitle() {
        mTitleList.add("加班排行榜");
        mTitleList.add("996");
        mTitleList.add("955");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
