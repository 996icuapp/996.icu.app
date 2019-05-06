package com.youngpower.a996icu.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import com.youngpower.a996icu.publishCompany.PublishCompanyActivity;

public class PublishCompanyBtn extends FloatingActionButton {
    public PublishCompanyBtn(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PublishCompanyBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PublishCompanyBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishCompanyActivity.start(getContext());
            }
        });
    }
}
