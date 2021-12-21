package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.ViewDataBinding;

import com.google.gson.JsonArray;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.AdminDashboard_New;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.databinding.OtpVerifyActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.payment.model.PartialDeliveryModel;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OtpVerifyRequest;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class OtpVerifyActivity extends BaseActivity {
    OtpVerifyActivityBinding binding;

    String orderNumber = null;
    String for_type = null;
    String trI = null;
    String deliveryType = null;
    String serviceIds = null;
    String deliveryQty = null;
    String imageURL = null;
    String expectedDate = null;
    String farmID = null;


    @Override
    protected int getActivityLayout() {
        return R.layout.otp_verify_activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (OtpVerifyActivityBinding) viewDataBinding;
        orderNumber = getIntent().getStringExtra("order_number");
        for_type = getIntent().getStringExtra("for_type");
        trI = getIntent().getStringExtra("tr_id");
        deliveryType = getIntent().getStringExtra("delivery");
        serviceIds = getIntent().getStringExtra("serviceIds");
        deliveryQty = getIntent().getStringExtra("deliveryQty");
        imageURL = getIntent().getStringExtra("imageURL");
        farmID = getIntent().getStringExtra("farmID");


        expectedDate = getIntent().getStringExtra("expectedDate");
        if (deliveryType != null && deliveryType.equalsIgnoreCase("delivery")) {
            binding.message.setText(getResources().getString(R.string.OrderhasbeenDeliveredsuccessfully));
            binding.collectBtn.setText(getResources().getString(R.string.Submit));
        } else if (for_type == null || !for_type.equalsIgnoreCase("delivery")) {
            binding.message.setText(getResources().getString(R.string.Paymenthasbeencollected));
            // binding.collectBtn.setText("Submit");
        } else if (serviceIds != null && serviceIds.length() > 0) {
            binding.message.setText(getResources().getString(R.string.OrderhasbeenDeliveredsuccessfully));
            binding.collectBtn.setText(getResources().getString(R.string.Submit));
        } else {
            binding.message.setText(getResources().getString(R.string.Paymenthasbeencollected));
        }
        binding.otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpS = binding.otp.getText().toString();
                if (otpS != null && otpS.length() > 0 && !otpS.equalsIgnoreCase("0.0")) {
                    if (orderNumber != null) {
                        verifyOTPMethod(otpS);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Ordernumberisnotfound), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.PleaseentervalidOTP), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromD = getcurrentDate("dd-MM-yyyy");
                String toD = getCalculatedDate("dd-MM-yyyy", 7);
                if (AppConstant.user_id != null) {
                    collectAmountMethod(fromD, toD);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Useridisnotfound), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static String getcurrentDate(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void verifyOTPMethod(String otpS) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        ArrayList<PartialDeliveryModel> modL = new ArrayList<PartialDeliveryModel>();
        if (serviceIds != null && serviceIds.length() > 0 && !serviceIds.equalsIgnoreCase("null")) {
            String[] serviceIdsArray = serviceIds.split("=");
            if (deliveryQty != null && deliveryQty.length() > 0 && !deliveryQty.equalsIgnoreCase("null")) {
                String[] deliveryIdsArray = deliveryQty.split("=");
                if (serviceIdsArray.length == deliveryIdsArray.length) {
                    for (int i = 0; i < serviceIdsArray.length; i++) {
                        PartialDeliveryModel model = new PartialDeliveryModel();
                        if (serviceIdsArray[i] != null && deliveryIdsArray[i] != null) {
                            if (!serviceIdsArray[i].equalsIgnoreCase("null") && !deliveryIdsArray[i].equalsIgnoreCase("null")) {
                                model.setNoofUnit(Integer.parseInt(deliveryIdsArray[i]));
                                model.setServiceID(Integer.parseInt(serviceIdsArray[i]));
                                modL.add(model);
                            }
                        }
                    }
                }
            }
        }
        final OtpVerifyRequest request1 = new OtpVerifyRequest();
        if (orderNumber != null) {
            request1.setOrderNumber(orderNumber);
        }

        if (AppConstant.user_id != null) {
            request1.setLoginUserID(AppConstant.user_id);
        }
        if (farmID != null && !farmID.equalsIgnoreCase("null") && farmID.length() > 0) {
            request1.setFarmID(farmID);
        }

        request1.setOTPForVerification(otpS);
        if (for_type != null && for_type.equalsIgnoreCase("delivery")) {
            request1.setFlgDelivery(true);
        } else {
            request1.setFlgDelivery(false);
        }
        if (trI != null && trI.length() > 0) {
            request1.setTransactionNumber(trI);
        }
        if (modL.size() > 0) {
            request1.setDeliveryList(modL);
        }
        request1.setTransactionImage(imageURL);
        if (expectedDate != null && expectedDate.length() > 0) {
            request1.setExcepetedCollectionDate(expectedDate);
        }
//        request1.setType("surveyor");//20210713 Acc to Mehdi
        request1.setType("farmer");
        AppController.getInstance().getApiService().verifyOTPMethod(request1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<String>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getString(R.string.server_not_found));

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<String> response) {
                        progressDialog.cancel();
                        String msg = response.body();
                        if (msg != null && (msg.contains("nvalid") || msg.contains("OTP Not Found"))) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            binding.otp.setText(null);
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            binding.otpLay.setVisibility(View.GONE);
                            binding.verifyOtp.setVisibility(View.GONE);
                            binding.successLay.setVisibility(View.VISIBLE);
                            binding.otp.setText(null);
                        }
                    }
                });

    }

    private void collectAmountMethod(String fromD, String toD) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getResources().getString(R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        CollectAmountRequest request1 = new CollectAmountRequest();
        if (AppConstant.user_id != null) {
            request1.setUserID(Integer.valueOf(AppConstant.user_id));
        }
        request1.setDateFrom(fromD);
        request1.setDateTo(toD);
        AppController.getInstance().getApiService().collectAmountMethod(request1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<JsonArray>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getString(R.string.server_not_found));

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<JsonArray> response) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Transactioniscompleted), Toast.LENGTH_SHORT).show();
                        JsonArray msg = response.body();
                        Intent intent = new Intent(OtpVerifyActivity.this, AdminDashboard_New.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                EditText edit = ((EditText) v);
                Rect outR = new Rect();
                edit.getGlobalVisibleRect(outR);
                Boolean isKeyboardOpen = !outR.contains((int) ev.getRawX(), (int) ev.getRawY());
                System.out.print("Is Keyboard? " + isKeyboardOpen);
                if (isKeyboardOpen) {
                    System.out.print("Entro al IF");
                    edit.clearFocus();
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                }
                edit.setCursorVisible(!isKeyboardOpen);

            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
