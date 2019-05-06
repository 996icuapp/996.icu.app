package com.youngpower.a996icu.calculator;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youngpower.a996icu.R;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.about.AboutUsActivity;
import com.youngpower.a996icu.view.QuadruplePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

import static com.youngpower.a996icu.Util.viewToBitmap;

public class CalcFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "CalcFragment";
    private static int REQUEST_PERMISSITION = 1;

    @BindView(R.id.text_result)
    TextView mTextResult;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private Bitmap mShareBitmap = null;

    private final List<String> mStartList = getRangeList(12);
    private final List<String> mStopList = getRangeList(12);
    private final List<String> mWeekdayList = getRangeList(7);
    private final List<String> mSalaryList = getRangeList(1, 50);
    @BindView(R.id.work_picker_container)
    CardView mWorkPickerContainer;
    @BindView(R.id.btn_share)
    FloatingActionButton mBtnShare;
    @BindView(R.id.tl_container)
    RelativeLayout mTlContainer;
    @BindView(R.id.card_result)
    CardView mCardResult;

    private int mStartIndex = 8;
    private int mStopIndex = 8;
    private int mWeekdayIndex = 5;
    private int mSalaryIndex = 14;

    private int mStart = 9;
    private int mStop = 9;
    private int mWeekday = 6;
    private int mSalary = 15;
    private QuadruplePicker mQuadruplePicker;

    public static final String sResultFormatHtml = "<font color='#242424'><strong><big>工资</big></strong></font>\n" +
            "<br>\n" +
            "你当前的每月工资是 %.2fK，但由于你的加班，你每个月应得到额外的加班工资<font color='red'><strong><big> %.2fK </big></strong></font>，所以你每个月实际上应该得到的薪资是<font color='red'><strong><big> %.2fK </big></strong></font>。\n" +
            "<br>\n" +
            "<br>\n" +
            "<font color='#242424'><strong><big>工作时长</big></strong></font>\n" +
            "<br>\n" +
            "国家法定每天工作时长为8小时，每周工作5天。但是你每天实际工作了<font color='red'><strong><big> %5d小时 </big></strong></font>，这意味着你每天都需要加班<font color='red'><strong><big> %5d小时 </big></strong></font>。\n" +
            "<br>\n" +
            "这样的话，一周下来，你累计加班了<font color='red'><strong><big> %5d小时 </big></strong></font>，每个月会加班<font color='red'><strong><big> %5d小时 </big></strong></font>。";
    public static final String sResultFormat =
            "每月工资：%.2fK\n" +
                    "每月加班工资：%.2fK\n" +
                    "每月应得工资：%.2fK\n" +
                    "每日工作时长：%5d小时\n" +
                    "每日加班时长：%5d小时\n" +
                    "每周工作时长：%5d小时\n" +
                    "每周加班时长：%5d小时\n" +
                    "每月工作时长：%5d小时\n" +
                    "每月加班时长：%5d小时\n";

    private void doCalculate() {
        //     有下列情形之一的，用人单位应当按照下列标准支付高于劳动者正常工作时间工资的工资报酬：
        //（一）安排劳动者延长工作时间的，支付不低于工资的百分之一百五十的工资报酬；
        //（二）休息日安排劳动者工作又不能安排补休的，支付不低于工资的百分之二百的工资报酬；
        //（三）法定休假日安排劳动者工作的，支付不低于工资的百分之三百的工资报酬。
        // 平均每月计薪天数为 21.75 天。
        int dayTime = mStop + 12 - mStart;
        boolean isWeekdayExtra = dayTime > 8;
        boolean isWeekendExtra = mWeekday > 5;
        int weekTime = dayTime * mWeekday;
        int monthTime = weekTime * 4;
        int dayTimeExtra = (dayTime > 8) ? dayTime - 8 : 0;
        int weekendTimeExtra = isWeekendExtra ? dayTime * (mWeekday - 5) : 0;
        int weekTimeExtra = 0;
        if (isWeekdayExtra) {
            weekTimeExtra = (mWeekday > 5 ? 5 : mWeekday) * dayTimeExtra + weekendTimeExtra;
        }

        int monthTimeExtra = weekTimeExtra * 4;
        int monthWeekendExtra = weekendTimeExtra * 4;

        double moneyDay = 1.0 * mSalary / 20;
        double moneyHour = moneyDay / 8;
        double moneyWeek = moneyDay * (isWeekendExtra ? 5 : mWeekday);
        double moneyWeekendExtra = 0;
        double moneyWeekdayExtra = 0;
        if (isWeekendExtra) {
            moneyWeekendExtra = (mWeekday - 5) * dayTime * moneyHour * 2;
            moneyWeek += moneyWeekendExtra;
        }
        if (isWeekdayExtra) {
            moneyWeekdayExtra = dayTimeExtra * moneyHour * 1.5;
            moneyWeek += moneyWeekdayExtra * (mWeekday > 5 ? 5 : mWeekday);
        }

        double moneyMonth = moneyWeek * 4;

        if (!isWeekdayExtra && !isWeekendExtra) {

        }
        String result = String.format(Locale.SIMPLIFIED_CHINESE, sResultFormatHtml,
                1.0 * mSalary,
                moneyMonth - mSalary,
                moneyMonth,
                dayTime,
                dayTimeExtra,
                weekTimeExtra,
                monthTimeExtra);

        Spanned text = Html.fromHtml(result);
        mTextResult.setText(text);
        //mTextResult.setText(result);
    }


    private void init() {
        mQuadruplePicker = new QuadruplePicker(this.getActivity(), mStartList, mStopList, mWeekdayList, mSalaryList);
        mQuadruplePicker.setSelectedIndex(mStartIndex, mStopIndex, mWeekdayIndex, mSalaryIndex);
        mQuadruplePicker.setOffset(2);
//        mQuadruplePicker.setFirstLabel("", "上班");
//        mQuadruplePicker.setSecondLabel("", "下班");
//        mQuadruplePicker.setThirdLabel("一周", "天");
//        mQuadruplePicker.setFourthLabel("月薪", "K");
        mQuadruplePicker.setFirstLabel("", "-");
        mQuadruplePicker.setSecondLabel("", "-");
        mQuadruplePicker.setThirdLabel("", "-");
        mQuadruplePicker.setFourthLabel("", "K");
        mQuadruplePicker.setTextColor(Util.getColor(R.color.colorAccent));
        mQuadruplePicker.setLabelTextColor(Util.getColor(R.color.colorAccent
        ));
        mQuadruplePicker.setDividerColor(Util.getColor(R.color.colorAccent));
        mQuadruplePicker.setOnWheelListener(new QuadruplePicker.OnWheelListener() {
            @Override
            public void onFirstWheeled(int index, String item) {
                mStartIndex = index;
                mStart = Integer.parseInt(item);
                doCalculate();
            }

            @Override
            public void onSecondWheeled(int index, String item) {
                mStopIndex = index;
                mStop = Integer.parseInt(item);
                doCalculate();
            }

            @Override
            public void onThirdWheeled(int index, String item) {
                mWeekdayIndex = index;
                mWeekday = Integer.parseInt(item);
                doCalculate();
            }

            @Override
            public void onFourthWheeled(int index, String item) {
                mSalaryIndex = index;
                mSalary = Integer.parseInt(item);
                doCalculate();
            }
        });

        mWorkPickerContainer.addView(mQuadruplePicker.getContentView()
        );

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

        doCalculate();

    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private List<String> getRangeList(int range) {
        List<String> array = new ArrayList<>();
        for (int i = 1; i < range + 1; i++) {
            array.add("" + i);
        }
        return array;
    }


    private List<String> getRangeList(int start, int stop) {
        List<String> array = new ArrayList<>();
        for (int i = start; i < stop + 1; i++) {
            array.add("" + i);
        }
        return array;
    }

    @OnClick(R.id.btn_share)
    public void onViewClicked() {

        final LinearLayout shareCardContainer = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.card_share_calc, null);
        ImageView pickerView = shareCardContainer.findViewById(R.id.iv_picker);
        ImageView dscriView = shareCardContainer.findViewById(R.id.iv_decri);
        pickerView.setImageBitmap(viewToBitmap(mQuadruplePicker.getContentView()));
        dscriView.setImageBitmap(viewToBitmap(mTextResult));
        shareCardContainer.setVisibility(View.INVISIBLE);
        mTlContainer.addView(shareCardContainer);

        shareCardContainer.post(new Runnable() {
            @Override
            public void run() {
                mShareBitmap = viewToBitmap(shareCardContainer);

                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(getActivity(), perms)) {
                    // Already have permission, do the thing
                    // ...
                    doShare();
                } else {
                    // Do not have permissions, request them now
                    EasyPermissions.requestPermissions(CalcFragment.this, "需生成一张图片进行分享，故需要存储权限",
                            REQUEST_PERMISSITION, perms);
                }
                mTlContainer.removeView(shareCardContainer);
            }
        });


    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
        if (requestCode == REQUEST_PERMISSITION)
            doShare();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }

    private void doShare() {
        if (mShareBitmap != null) {

            //由文件得到uri
            Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mShareBitmap, null, null));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "分享到"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
