package com.youngpower.a996icu.model;


public interface OnResultListener<T> {

    void onSuccess(T data);

    void onFail(int code, String errMessage);
}
