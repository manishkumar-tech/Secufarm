package com.weather.risk.mfi.myfarminfo.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.weather.risk.mfi.myfarminfo.interfaces.GroundWaterForecastingCallbacks;

public class GroundWaterForecastingVMFactory  extends ViewModelProvider.NewInstanceFactory {
    private GroundWaterForecastingCallbacks mLoginResultCallback;

    public GroundWaterForecastingVMFactory(GroundWaterForecastingCallbacks loginResultCallback) {
        mLoginResultCallback = loginResultCallback;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new GroundWaterForecastingVM(mLoginResultCallback);
    }
}
