package com.weather.risk.mfi.myfarminfo.marketplace;

import android.view.View;

import androidx.annotation.StringRes;


public interface BaseView {

    void showLoading();

    void showNoLead();


    void reLoadingView();

    void openActivityOnTokenExpire();

    void showError(@StringRes int resId);

    void showErrorHeader(int statusCode);

    void showError(String message);

    void showMessage(String message);

    void showMessage(int resId);

    void onShowContent(View view);

    void showServerError();

    void showNetworkError();

    void showNoData();

    void onRetry();

    boolean isNetworkConnected();

    void hideKeyboard();


}