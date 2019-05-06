package com.youngpower.a996icu.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ListViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public ListViewpagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
        super(fm);
        if (null == fragments) {
            return;
        }
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < 0 || null == mFragments) {
            throw new IllegalArgumentException();
        }

        // if position is so large , return the last fragment
        if (position >= mFragments.size()) {
            return getItem(position - 1);
        }
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < 0 || null == mTitles) {
            throw new IllegalArgumentException();
        }

        if (position >= mTitles.size()) {
            return getPageTitle(position - 1);
        }
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return null == mFragments ? 0 : mFragments.size();
    }
}
