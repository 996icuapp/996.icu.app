package com.youngpower.a996icu.list.getoff;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youngpower.a996icu.ApiService;
import com.youngpower.a996icu.R;
import com.youngpower.a996icu.UserUtil;
import com.youngpower.a996icu.Util;
import com.youngpower.a996icu.bean.GetOffCompanyBean;
import com.youngpower.a996icu.bean.GetOffRankListBean;
import com.youngpower.a996icu.model.BaseResponse;
import com.youngpower.a996icu.model.RetrofitClient;
import com.youngpower.a996icu.publishCompany.PublishCompanyActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youngpower.a996icu.Util.viewToBitmap;

public class GetOffFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, EasyPermissions.PermissionCallbacks {

    private static final String SPK_COMPANY_DATA = "spkCompanyData";
    private static final String SPK_LAST_PUNCH_TIME = "spkLastPunchTime";
    private static int REQUEST_PERMISSITION = 1;

    private static final String TAG = "GetOffFragment";
    @BindView(R.id.rv_get_off)
    RecyclerView mRvGetOff;
    @BindView(R.id.btn_get_off)
    FloatingActionButton mBtnGetOff;
    Unbinder unbinder;
    @BindView(R.id.srl_get_off)
    SwipeRefreshLayout mSrlGetOff;
    @BindView(R.id.tv_day_prompt)
    TextView mTvDayPrompt;
    @BindView(R.id.btn_share)
    FloatingActionButton mBtnShare;
    @BindView(R.id.container)
    RelativeLayout mContainer;
    @BindView(R.id.sv_container)
    ScrollView mSvContainer;
    private GetOffAdapter mAdapter;
    AlertDialog mDialog;

    private AppCompatSpinner mSpinner;
    private TextView mTvPrompt;

    private String mCurDay;

    private Bitmap mShareBitmap = null;

    private boolean mIsDialogInitShow = true;

    private List<GetOffCompanyBean> companyList = new ArrayList<>();

    private static final long HOUR = 1000 * 60 * 60;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_off, container, false);
        unbinder = ButterKnife.bind(this, view);

        final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.dialog_punch, null);
        mSpinner = linearLayout.findViewById(R.id.spinner_company);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIsDialogInitShow = false;
                try {
                    Util.setSpString(SPK_COMPANY_DATA, JSON.toJSONString(companyList.get(position)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTvPrompt = linearLayout.findViewById(R.id.tv_prompt);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        mDialog = builder.setView(linearLayout)
                .setPositiveButton("打卡", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(Util.getSpString(SPK_COMPANY_DATA, ""))) {
                            getOff();
                        } else {
                            Util.showToast("请选择公司");
                        }
                    }
                })
                .setNeutralButton("爆料", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PublishCompanyActivity.start(getActivity());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvGetOff.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvGetOff.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new GetOffAdapter();
        mRvGetOff.setAdapter(mAdapter);
        mSrlGetOff.setOnRefreshListener(this);
        //设置下拉时圆圈的颜色（可以由多种颜色拼成）
        mSrlGetOff.setColorSchemeResources(R.color.colorAccent);
        requestData();
        requestCompanyData();
    }

    private void requestCompanyData() {
        RetrofitClient.buildService(ApiService.class).getGetOffCompanyList().enqueue(new Callback<BaseResponse<List<GetOffCompanyBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<GetOffCompanyBean>>> call, Response<BaseResponse<List<GetOffCompanyBean>>> response) {
                if (Util.isResponseOk(response)) {
                    companyList = response.body().getData();

                    Task.call(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            onRequestCompanyDataSuccess();
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                } else {
                    Util.showToast(Util.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<GetOffCompanyBean>>> call, Throwable t) {
                Util.showToast(Util.getString(R.string.load_fail));
            }
        });
    }

    private void onRequestCompanyDataSuccess() {
        final String[] companyStrList = new String[companyList.size()];
        for (int i = 0; i < companyList.size(); i++) {
            companyStrList[i] = companyList.get(i).getName();
        }

        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        final boolean shouldShowDefault = TextUtils.isEmpty(Util.getSpString(SPK_COMPANY_DATA, ""));
        GetOffArrayAdapter<String> spinnerAdapter = new GetOffArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, companyStrList, shouldShowDefault);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        mSpinner.setAdapter(spinnerAdapter);
    }


    private void requestData() {
        RetrofitClient.buildService(ApiService.class).getGetOffRankList().enqueue(new Callback<BaseResponse<GetOffRankListBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<GetOffRankListBean>> call, Response<BaseResponse<GetOffRankListBean>> response) {
                if (Util.isResponseOk(response)) {
                    if (mSrlGetOff != null) {
                        mSrlGetOff.setRefreshing(false);
                    }
                    if (response.body().getCode() > 0) {
                        Util.showToast(response.body().getMessage());
                        return;
                    }
                    final GetOffRankListBean rankListBean = response.body().getData();
                    if (mAdapter != null) {
                        mAdapter.refreshData(rankListBean.getItems());
                    }


                    Task.call(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            mTvDayPrompt.setText(rankListBean.getDate());
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                } else {
                    Util.showToast(Util.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<GetOffRankListBean>> call, Throwable t) {
                Util.showToast(Util.getString(R.string.load_fail));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_get_off)
    public void onViewClicked() {
        showGetOffDialog();
    }

    private void showGetOffDialog() {
        if (mDialog != null) {
            mIsDialogInitShow = true;
            try {
                GetOffCompanyBean companyBean = JSON.parseObject(Util.getSpString(SPK_COMPANY_DATA, "{}"), GetOffCompanyBean.class);
                if (!TextUtils.isEmpty(companyBean.getName())) {
                    for (int i = 0; i < companyList.size(); i++) {
                        GetOffCompanyBean a = companyList.get(i);
                        if (TextUtils.equals(a.getName(), companyBean.getName())) {
                            mIsDialogInitShow = false;
                            mSpinner.setSelection(i);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE);
            mTvPrompt.setText("现在是北京时间 " + format.format(new Date()) + " ，祝你下班愉快！希望你明天下班能早一点！");
            mDialog.show();
        }
    }

    private void getOff() {
        if (UserUtil.getCurUser() == null) {
            Util.showToast("打卡失败，请5s后重试");
            UserUtil.initUser();
            return;
        }

        if (UserUtil.getUid() == -1) {
            Util.showToast("用户信息错误，请退出重进");
            return;
        }


        GetOffCompanyBean companyBean = JSON.parseObject(Util.getSpString(SPK_COMPANY_DATA, ""), GetOffCompanyBean.class);
        RetrofitClient.buildService(ApiService.class).getOff(UserUtil.getUid(), companyBean.getId()).enqueue(new Callback<BaseResponse<Object>>() {
            @Override
            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                if (Util.isResponseOk(response)) {
                    if (response.body().getCode() > 0) {
                        Util.showToast(response.body().getMessage());
                    } else {
                        Util.showToast("打卡成功");
                    }
                    requestData();
                } else {
                    Util.showToast(Util.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                Util.showToast(Util.getString(R.string.load_fail));
            }
        });

    }

    @Override
    public void onRefresh() {
        requestData();
    }


    @OnClick(R.id.btn_share)
    public void onShareClicked() {
        final ScrollView shareCardContainer = (ScrollView) LayoutInflater.from(getActivity()).inflate(R.layout.card_share_overtime, null);
        ImageView overTimeRank = shareCardContainer.findViewById(R.id.iv_overtime_rank);
        TextView titleView = shareCardContainer.findViewById(R.id.tv_title);
        titleView.setText("公司加班排行榜\n" + mCurDay);

        overTimeRank.setImageBitmap(getRecyclerViewScreenshot(mRvGetOff));
        shareCardContainer.setVisibility(View.INVISIBLE);

        if (mSvContainer.getChildCount() != 0) {
            mSvContainer.removeAllViews();
        }
        mSvContainer.addView(shareCardContainer);

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
                    EasyPermissions.requestPermissions(GetOffFragment.this, "需生成一张图片进行分享，故需要存储权限",
                            REQUEST_PERMISSITION, perms);
                }
                mContainer.removeView(shareCardContainer);
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

    public static Bitmap getRecyclerViewScreenshot(RecyclerView view) {
        int size = view.getAdapter().getItemCount();
        // 最多只截5个
        if (size > 5) {
            size = 5;
        }

        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();

        for (int i = 1; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        return bigBitmap;
    }
}
