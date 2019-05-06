package com.youngpower.a996icu.discuss;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class DiscussAdapter extends FragmentPagerAdapter {
    /**
     * 每一个选项页的标题的list
     */
    private List<String> titleList;

    /**
     * 每一个选项卡的fragment的list
     */
    private List<Fragment> fragmentList;

    public DiscussAdapter(FragmentManager fm , List<String> titleList , List<Fragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return titleList == null ? 0 : titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

}
