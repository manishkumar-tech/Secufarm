package com.weather.risk.mfi.myfarminfo.payment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.PaymentActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.marketplace.BaseActivity;
import com.weather.risk.mfi.myfarminfo.marketplace.ItemClickInterface;
import com.weather.risk.mfi.myfarminfo.payment.adapter.AmountAdapter;
import com.weather.risk.mfi.myfarminfo.payment.model.AmountListResponse;
import com.weather.risk.mfi.myfarminfo.payment.model.PaymentListRequest;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PaymentListActivity extends BaseActivity implements ItemClickInterface {

    private ApiService apiService;
    PaymentActivityBinding binding;

    ImageView backBTN;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {

        binding = (PaymentActivityBinding) viewDataBinding;
        apiService = AppController.getInstance().getApiService();
        backBTN = binding.backBtn;

        if (AppConstant.user_id != null && AppConstant.user_id.length() > 0) {

            getPaymentListMethod();
        }

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DVsdvsdv","dcscdscsd");
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.payment_activity;
    }


    private void getPaymentListMethod() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog

        PaymentListRequest request = new PaymentListRequest();
        request.setUserID(Integer.valueOf(AppConstant.user_id));
        // request.setUserID(100615);
        request.setPendingflag(true);
        request.setDetailSummary(false);
        request.setDeliveryStatus("Pending");


        apiService.pendingAmountListByUser(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<AmountListResponse>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        binding.emptyView.setVisibility(View.VISIBLE);
                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getString(R.string.server_not_found));
                        binding.emptyView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onNext(Response<List<AmountListResponse>> response) {
                        progressDialog.cancel();

                        List<AmountListResponse> responsesData = response.body();

                        if (responsesData != null && responsesData.size() > 0) {
                            binding.emptyView.setVisibility(View.GONE);
                            binding.farmRecyclerView.setVisibility(View.VISIBLE);

                            AmountAdapter adapter = new AmountAdapter(PaymentListActivity.this, responsesData, PaymentListActivity.this);
                            binding.farmRecyclerView.setLayoutManager(new LinearLayoutManager(PaymentListActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.farmRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            binding.farmRecyclerView.setAdapter(adapter);


                        } else {
                            binding.emptyView.setVisibility(View.VISIBLE);
                            binding.farmRecyclerView.setVisibility(View.GONE);

                        }
                    }
                });
    }

    @Override
    public void onClick(int value) {

        if (value >= 0) {

            //it will be call on registration page
            Log.v("cwqcqwcqw", value + "");

            Intent intent = new Intent(getApplicationContext(), PaymentDetailsActivity.class);
            intent.putExtra("project_id", "" + value);
            startActivity(intent);
        }


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