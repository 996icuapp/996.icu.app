package com.youngpower.a996icu.list.getoff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.Util;


public class GetOffArrayAdapter<T> extends ArrayAdapter<T> {

    private boolean mShouldShowDed = true;

    public GetOffArrayAdapter(@NonNull Context context, int resource, @NonNull T[] objects, boolean shouldShowDef) {
        super(context, resource, objects);
        mShouldShowDed = shouldShowDef;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return setSelectedView(super.getView(position, convertView, parent));
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return setCenter(super.getDropDownView(position, convertView, parent));
    }

    private View setSelectedView(View view) {
        AppCompatTextView textView = view.findViewById(android.R.id.text1);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);//这里不一样
        textView.setTextColor(Util.getColor(R.color.colorTextSubTitle));
        textView.setTextSize(14);
        if (mShouldShowDed) {
            textView.setText("点击选择公司");
            mShouldShowDed = false;
        }
        return view;
    }

    private View setCenter(View view) {
        AppCompatTextView textView = view.findViewById(android.R.id.text1);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);//这里不一样
        return view;
    }
}
