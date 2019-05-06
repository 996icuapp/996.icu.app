package com.youngpower.a996icu.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.widget.WheelView;


public class QuadruplePicker extends WheelPicker {
    private List<String> firstData = new ArrayList<>();
    private List<String> secondData = new ArrayList<>();
    private List<String> thirdData = new ArrayList<>();
    private List<String> fourthData = new ArrayList<>();
    private int selectedFirstIndex = 0;
    private int selectedSecondIndex = 0;
    private int selectedThirdIndex = 0;
    private int selectedFourthIndex = 0;
    private OnWheelListener onWheelListener;
    private OnPickListener onPickListener;
    private CharSequence firstPrefixLabel, firstSuffixLabel;
    private CharSequence secondPrefixLabel, secondSuffixLabel;
    private CharSequence thirdPrefixLabel, thirdSuffixLabel;
    private CharSequence fourthPrefixLabel, fourthSuffixLabel;

    public QuadruplePicker(Activity activity,
                           List<String> firstData,
                           List<String> secondData,
                           List<String> thirdData,
                           List<String> fourthData) {
        super(activity);
        this.firstData = firstData;
        this.secondData = secondData;
        this.thirdData = thirdData;
        this.fourthData = fourthData;
    }

    public void setSelectedIndex(int firstIndex, int secondIndex, int thirdIndex, int fourthIndex) {
        if (firstIndex >= 0 && firstIndex < firstData.size()) {
            selectedFirstIndex = firstIndex;
        }
        if (secondIndex >= 0 && secondIndex < secondData.size()) {
            selectedSecondIndex = secondIndex;
        }
        if (thirdIndex >= 0 && thirdIndex < thirdData.size()) {
            selectedThirdIndex = thirdIndex;
        }

        if (fourthIndex >= 0 && fourthIndex < fourthData.size()) {
            selectedFourthIndex = fourthIndex;
        }
    }

    public void setFirstLabel(CharSequence firstPrefixLabel, CharSequence firstSuffixLabel) {
        this.firstPrefixLabel = firstPrefixLabel;
        this.firstSuffixLabel = firstSuffixLabel;
    }

    public void setSecondLabel(CharSequence secondPrefixLabel, CharSequence secondSuffixLabel) {
        this.secondPrefixLabel = secondPrefixLabel;
        this.secondSuffixLabel = secondSuffixLabel;
    }

    public void setThirdLabel(CharSequence thirdPrefixLabel, CharSequence thirdSuffixLabel) {
        this.thirdPrefixLabel = thirdPrefixLabel;
        this.thirdSuffixLabel = thirdSuffixLabel;
    }

    public void setFourthLabel(CharSequence fourthPrefixLabel, CharSequence fourthSuffixLabel) {
        this.fourthPrefixLabel = fourthPrefixLabel;
        this.fourthSuffixLabel = fourthSuffixLabel;
    }

    public String getSelectedFirstItem() {
        if (firstData.size() > selectedFirstIndex) {
            return firstData.get(selectedFirstIndex);
        }
        return "";
    }

    public String getSelectedSecondItem() {
        if (secondData.size() > selectedSecondIndex) {
            return secondData.get(selectedSecondIndex);
        }
        return "";
    }

    public String getSelectedThirdItem() {
        if (thirdData.size() > selectedThirdIndex) {
            return thirdData.get(selectedThirdIndex);
        }
        return "";
    }

    public String getSelectedFourthItem() {
        if (fourthData.size() > selectedFourthIndex) {
            return fourthData.get(selectedFourthIndex);
        }
        return "";
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        if (!TextUtils.isEmpty(firstPrefixLabel)) {
            TextView firstPrefixLabelView = createLabelView();
            firstPrefixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            firstPrefixLabelView.setText(firstPrefixLabel);
            layout.addView(firstPrefixLabelView);
        }
        final WheelView firstView = createWheelView();
        firstView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(firstView);
        if (!TextUtils.isEmpty(firstSuffixLabel)) {
            TextView firstSuffixLabelView = createLabelView();
            firstSuffixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            firstSuffixLabelView.setText(firstSuffixLabel);
            layout.addView(firstSuffixLabelView);
        }

        if (!TextUtils.isEmpty(secondPrefixLabel)) {
            TextView secondPrefixLabelView = createLabelView();
            secondPrefixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            secondPrefixLabelView.setText(secondPrefixLabel);
            layout.addView(secondPrefixLabelView);
        }
        final WheelView secondView = createWheelView();
        secondView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(secondView);
        if (!TextUtils.isEmpty(secondSuffixLabel)) {
            TextView secondSuffixLabelView = createLabelView();
            secondSuffixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            secondSuffixLabelView.setText(secondSuffixLabel);
            layout.addView(secondSuffixLabelView);
        }

        if (!TextUtils.isEmpty(thirdPrefixLabel)) {
            TextView thirdPrefixLabelView = createLabelView();
            thirdPrefixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            thirdPrefixLabelView.setText(thirdPrefixLabel);
            layout.addView(thirdPrefixLabelView);
        }
        final WheelView thirdView = createWheelView();
        thirdView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(thirdView);
        if (!TextUtils.isEmpty(thirdSuffixLabel)) {
            TextView thirdSuffixLabelView = createLabelView();
            thirdSuffixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            thirdSuffixLabelView.setText(thirdSuffixLabel);
            layout.addView(thirdSuffixLabelView);
        }


        if (!TextUtils.isEmpty(fourthPrefixLabel)) {
            TextView fourthPrefixLabelView = createLabelView();
            fourthPrefixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            fourthPrefixLabelView.setText(fourthPrefixLabel);
            layout.addView(fourthPrefixLabelView);
        }
        final WheelView fourthView = createWheelView();
        fourthView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(fourthView);
        if (!TextUtils.isEmpty(fourthSuffixLabel)) {
            TextView fourthSuffixLabelView = createLabelView();
            fourthSuffixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            fourthSuffixLabelView.setText(fourthSuffixLabel);
            layout.addView(fourthSuffixLabelView);
        }

        firstView.setItems(firstData, selectedFirstIndex);
        firstView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedFirstIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onFirstWheeled(selectedFirstIndex, firstData.get(selectedFirstIndex));
                }
            }
        });
        secondView.setItems(secondData, selectedSecondIndex);
        secondView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedSecondIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onSecondWheeled(selectedSecondIndex, secondData.get(selectedSecondIndex));
                }
            }
        });
        thirdView.setItems(thirdData, selectedThirdIndex);
        thirdView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedThirdIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onThirdWheeled(selectedThirdIndex, thirdData.get(selectedThirdIndex));
                }
            }
        });
        fourthView.setItems(fourthData, selectedFourthIndex);
        fourthView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedFourthIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onFourthWheeled(selectedFourthIndex, fourthData.get(selectedFourthIndex));
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onPickListener != null) {
            onPickListener.onPicked(selectedFirstIndex, selectedSecondIndex, selectedThirdIndex, selectedFourthIndex);
        }
    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnPickListener(OnPickListener onPickListener) {
        this.onPickListener = onPickListener;
    }

    /**
     * 数据条目滑动监听器
     */
    public interface OnWheelListener {

        void onFirstWheeled(int index, String item);

        void onSecondWheeled(int index, String item);

        void onThirdWheeled(int index, String item);

        void onFourthWheeled(int index, String item);
    }

    /**
     * 数据选择完成监听器
     */
    public interface OnPickListener {

        void onPicked(int selectedFirstIndex,
                      int selectedSecondIndex,
                      int selectedThirdIndex,
                      int selectedFourthIndex);

    }

}
