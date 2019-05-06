package com.youngpower.a996icu.githubWeb;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.about.AboutUsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.KeyEvent.KEYCODE_BACK;


public class GithubWebFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.wv_main)
    WebView mWvMain;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout mSrlMain;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_github_web, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSrlMain.setOnRefreshListener(this);
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        mSrlMain.setColorSchemeResources(R.color.colorAccent);
        mWvMain.loadUrl("https://github.com/996icu/996.ICU/blob/master/README_CN.md");
        mWvMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mToolbar.setTitle(mToolbar.getTitle() + " 加载中...");
                mSrlMain.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(mSrlMain.isRefreshing()){
                    mSrlMain.setRefreshing(false);
                }
            }
        });

        mWvMain.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolbar.setTitle(title);
            }
        });

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

    @Override
    public void onResume() {
        super.onResume();
        mWvMain.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWvMain.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWvMain.destroy();
        unbinder.unbind();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWvMain.canGoBack()) {
            mWvMain.goBack();
            return true;
        }

        return false;
    }

    @Override
    public void onRefresh() {
        mWvMain.reload();
    }
}
