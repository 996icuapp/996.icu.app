package com.youngpower.a996icu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.youngpower.a996icu.model.BaseResponse;

import retrofit2.Response;


public class Util {

    private static final String SP_NAME = "996ICU";
    private static final String SPK_DISCUSS_HEAD = "pskDiscuss_";


    public static void showToast(String text) {
        Toast.makeText(ICUApp.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static String getDeviceID() {
        return Settings.Secure.getString(ICUApp.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getFormattedTime(long time) {
        final long second = 1000;
        final long minutes = second * 60;
        final long hour = minutes * 60;
        final long day = hour * 24;

        final long timePassed = System.currentTimeMillis() - time;
        String result;
        if (timePassed < minutes) {
            result = "刚刚";
        } else if (timePassed < hour) {
            result = timePassed / minutes + "分钟前";
        } else if (timePassed < day) {
            result = timePassed / hour + "小时前";
        } else {
            result = timePassed / day + "天前";
        }

        return result;
    }

    public static void hideSoftKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int getSpInt(String key, int def) {
        final SharedPreferences sharedPreferences = ICUApp.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getInt(key, def);
        } else {
            return def;
        }
    }

    public static void setSpInt(String key, int value) {
        final SharedPreferences sharedPreferences = ICUApp.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getSpString(String key, String def) {
        final SharedPreferences sharedPreferences = ICUApp.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getString(key, def);
        } else {
            return def;
        }
    }

    public static void setSpString(String key, String value) {
        final SharedPreferences sharedPreferences = ICUApp.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getColor(int resId) {
        return ICUApp.getAppContext().getResources().getColor(resId);
    }

    public static void refreshDiscussCommentNum(String objectId, int commentNum) {
        setSpInt(SPK_DISCUSS_HEAD + objectId, commentNum);
    }

    public static int getCacheDiscussCommentNum(String objectId) {
        return getSpInt(SPK_DISCUSS_HEAD + objectId, -1);
    }

    public static String getVersionName() {
        String packageName = ICUApp.getAppContext().getPackageName();
        try {
            return ICUApp.getAppContext().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(ICUApp.getAppContext(), resId);
    }

    public static String getString(int resIs) {
        return ICUApp.getAppContext().getResources().getString(resIs);
    }

    public static Bitmap viewToBitmap(View v) {
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenshot);
        v.draw(canvas);
        return screenshot;
    }

    public static String checkNotNull(String origin) {
        return checkNotNull(origin, "");
    }

    public static <T> T checkNotNull(T originValue, T defaultValue) {
        if (null == originValue) {
            return defaultValue;
        } else {
            return originValue;
        }
    }

    public static <T> boolean isResponseOk(Response<BaseResponse<T>> response) {
        if (response == null || response.body() == null) {
            return false;
        }

        final BaseResponse<T> baseResponse = response.body();
        return baseResponse.getData() != null;
//        return baseResponse.getCode() == Constants.ResponseCode.SUCCESS && baseResponse.getData() != null;
    }
}
