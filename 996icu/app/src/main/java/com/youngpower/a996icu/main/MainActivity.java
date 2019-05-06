package com.youngpower.a996icu.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.youngpower.a996icu.R;
import com.youngpower.a996icu.UserUtil;
import com.youngpower.a996icu.calculator.CalcFragment;
import com.youngpower.a996icu.discuss.DiscussFragment;
import com.youngpower.a996icu.githubWeb.GithubWebFragment;
import com.youngpower.a996icu.list.ListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.vp_main)
    ViewPager mVpMain;
    @BindView(R.id.bv_main)
    BottomNavigationView mBvMain;

    private MainAdapter mMainAdapter;
    private Fragment mCurrentFragment;

    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            Log.d("OpenInstall", "getWakeUp : wakeupData = " + appData.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserUtil.initUser();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        //获取唤醒参数
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeUpAdapter = null;
    }

    private void initView() {

        mBvMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_list:
                        mVpMain.setCurrentItem(0);
                        mCurrentFragment = mMainAdapter.getItem(0);
                        return true;

                    case R.id.item_discuss:
                        mVpMain.setCurrentItem(1);
                        mCurrentFragment = mMainAdapter.getItem(1);
                        return true;

                    case R.id.item_calc:
                        mVpMain.setCurrentItem(2);
                        mCurrentFragment = mMainAdapter.getItem(2);
                        return true;
                    case R.id.item_github_web:
                        mVpMain.setCurrentItem(3);
                        mCurrentFragment = mMainAdapter.getItem(3);
                        return true;

                }
                return false;
            }
        });

        setupViewPager(mVpMain);
        mVpMain.setOffscreenPageLimit(5);
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBvMain.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mCurrentFragment instanceof GithubWebFragment) {
            if (((GithubWebFragment) mCurrentFragment).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupViewPager(ViewPager viewPager) {
        mMainAdapter = new MainAdapter(getSupportFragmentManager());
        mMainAdapter.addFragment(new ListFragment());
        mMainAdapter.addFragment(new DiscussFragment());
        mMainAdapter.addFragment(new CalcFragment());
        mMainAdapter.addFragment(new GithubWebFragment());
        viewPager.setAdapter(mMainAdapter);
    }
}
