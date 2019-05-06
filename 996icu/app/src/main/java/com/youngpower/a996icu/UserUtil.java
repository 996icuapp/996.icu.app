package com.youngpower.a996icu;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.youngpower.a996icu.bean.UserBean;
import com.youngpower.a996icu.model.BaseResponse;
import com.youngpower.a996icu.model.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserUtil {

    private static String SPK_USER_OBJ = "spkUserObj";
    private static final String TAG = "UserUtil";
    private static UserBean mUserBean = null;

    public static void initUser() {

        final String deviceId = Util.getDeviceID();
        RetrofitClient.buildService(ApiService.class).refreshUserInfo(deviceId).enqueue(new Callback<BaseResponse<UserBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserBean>> call, Response<BaseResponse<UserBean>> response) {
                if (Util.isResponseOk(response)) {
                    final UserBean user = response.body().getData();
                    Util.setSpString(SPK_USER_OBJ, JSON.toJSONString(user));
                    mUserBean = user;
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserBean>> call, Throwable t) {
                System.out.println();
            }
        });
    }

    @Nullable
    public static UserBean getCurUser() {
        if (mUserBean != null) {
            return mUserBean;
        }

        final String userObj = Util.getSpString(SPK_USER_OBJ, "");
        if (TextUtils.isEmpty(userObj)) {
//            initUser();
            return null;
        }

        try {
            final UserBean userBean = JSON.parseObject(userObj, UserBean.class);
            if (userBean != null) {
                mUserBean = userBean;
            }
            return userBean;
        } catch (Exception e) {
            e.printStackTrace();
//            initUser();
            return null;
        }
    }

    public static String getCurUserDeviceId() {
        if (getCurUser() != null) {
            return getCurUser().getDeviceId();
        }
        return Util.getDeviceID();
    }

    public static boolean isUserEnable() {
        return getCurUser() == null || getCurUser().isEnable();
    }

    public static int getUid() {
        return getCurUser() == null ? -1 : getCurUser().getId();
    }
}
