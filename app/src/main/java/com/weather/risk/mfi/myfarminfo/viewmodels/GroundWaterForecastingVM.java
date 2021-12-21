package com.weather.risk.mfi.myfarminfo.viewmodels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.weather.risk.mfi.myfarminfo.interfaces.GroundWaterForecastingCallbacks;

public class GroundWaterForecastingVM extends ViewModel {

    private GroundWaterForecastingCallbacks mDataListener;

    //    UserLogin user;
    GroundWaterForecastingVM(@NonNull final GroundWaterForecastingCallbacks loginDataListener) {
        mDataListener = loginDataListener;
//        user = new UserLogin("", "");

    }

    public void onBacks(@NonNull final View view) {
        mDataListener.onBack();

    }

    public void onMaps(@NonNull final View view) {
        mDataListener.onMap();

    }

}
