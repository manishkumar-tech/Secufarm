package com.weather.risk.mfi.myfarminfo.retofitservices;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


public abstract class ResponseObserver<T> implements Observer<T> {

    private int statusCode;

    private CompositeDisposable disposable;

    public ResponseObserver(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);
    }

    @Override
    public void onError(Throwable e) {
        Log.e("TAG"," onError is ..... "+e.getMessage());
        if (e instanceof HttpException) {
            statusCode = ((HttpException) e).response().code();
            Log.e("TAG"," onError is HttpException..... ");
            onServerError(e, statusCode);
        } else {
            Log.e("TAG"," onError is onNetworkError..... "+e.getMessage());
            onNetworkError(e);
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onNetworkError(Throwable e);

    public abstract void onServerError(Throwable e, int code);
}
