package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.snackbar.Snackbar;
import com.weather.risk.mfi.myfarminfo.R;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.ResponseBody;


public abstract class BaseActivity extends AppCompatActivity implements BaseView{

    private CompositeDisposable compositeDisposable;
    private ViewDataBinding viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray));
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        viewDataBinding= DataBindingUtil.setContentView(this, getActivityLayout());
        compositeDisposable = new CompositeDisposable();
        initView(savedInstanceState,viewDataBinding);
    }

    protected abstract void initView(Bundle bundle, ViewDataBinding viewDataBinding);
    @LayoutRes
    abstract protected int getActivityLayout();


    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }


    /*  handle all type of error
     * */
    public boolean handleError(int code, ResponseBody msg) {
        boolean status = true;
        switch (code) {
            case 1005:
                status = false;
                showMessage(getString(R.string.internal_error));
                break;
            case 209:
                status = false;
                showMessage(getString(R.string.data_not_found));
                break;
            case 401:
                status = false;
                apiErrorDialog(getString(R.string.please_login_again));
                // showMessage(getString(R.string.please_login_again));
                break;
            case 422:
                status = false;
                // showMessage(getString(R.string.unprocessable_entity));
                break;
            case 404:
                status = false;
                showNetworkError();
                break;
            case 211:
                status = false;
                apiErrorDialog(getString(R.string.some_thing_went_wrong));
                // showMessage(getString(R.string.unprocessable_entity));
                break;
            case 500:
                status = false;
                //showMessage(getString(R.string.internal_error));
                apiErrorDialog(getString(R.string.some_thing_went_wrong));
                break;
            case 403:
                status = false;
                //showMessage(getString(R.string.please_login_again));
                break;
            case 300:
                status = false;
                showNetworkError();
                break;

        }
        return status;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }



    @Override
    public void showLoading() {

    }

    @Override
    public void reLoadingView() {

    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void showError(int resId) {
        showError(getString(resId));
    }

    @Override
    public void showErrorHeader(int statusCode) {

    }

    @Override
    public void showError(String message) {
        if (message != null) {
            showSnackBar(message);
            errorItBaby();
        } else {
            showSnackBar(getString(R.string.internal_error));
        }
    }

    @Override
    public void showNoLead() {

    }

    private void errorItBaby() {
        hideKeyboard();
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
        }
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }


    @Override
    public void onShowContent(View view) {

    }

    @Override
    public void showServerError() {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void onRetry() {

    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //noinspection ConstantConditions
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public void apiErrorDialog(String smg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("API Response");
        builder.setMessage(smg);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void hideKeyboard() {
        hideKeyboard(this);
    }
}