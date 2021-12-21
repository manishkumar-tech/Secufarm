package com.weather.risk.mfi.myfarminfo.payment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.PaymentDetailActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.marketplace.BaseActivity;
import com.weather.risk.mfi.myfarminfo.marketplace.OrderDetailsActivity;
import com.weather.risk.mfi.myfarminfo.payment.adapter.PaymentDetailAdapter;
import com.weather.risk.mfi.myfarminfo.payment.model.AmountListResponse;
import com.weather.risk.mfi.myfarminfo.payment.model.PaymentListRequest;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PaymentDetailsActivity extends BaseActivity implements ItemClick {

    private ApiService apiService;
    PaymentDetailActivityBinding binding;

    String projectId = null;
    public TextView projectName, totalFarmer, area, pendingAmount, totalAmount, msgInfo;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (PaymentDetailActivityBinding) viewDataBinding;
        projectName = binding.projectName;
        totalFarmer = binding.totalFarmer;
        area = binding.area;
        pendingAmount = binding.pendingAmount;
        totalAmount = binding.totalAmount;
        msgInfo = binding.pendigMsg;
        projectId = getIntent().getStringExtra("project_id");
        apiService = AppController.getInstance().getApiService();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /* if (AppConstant.user_id != null && AppConstant.user_id.length() > 0 && projectId != null) {
            getPaymentDetailsMethod();
        }*/
        if (AppConstant.user_id != null && AppConstant.user_id.length() > 0) {
            getPaymentDetailsMethod();
        }
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return R.layout.payment_detail_activity;
    }


    private void getPaymentDetailsMethod() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getResources().getString(R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        PaymentListRequest request = new PaymentListRequest();
        request.setUserID(Integer.valueOf(AppConstant.user_id));
        // request.setUserID(100615);
        request.setPendingflag(true);
        request.setDetailSummary(true);

        if (projectId != null && !projectId.equalsIgnoreCase("null")) {
            request.setProjectID(Integer.valueOf(projectId));
        }
        request.setDeliveryStatus("all");
        apiService.pendingAmountListByUser(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<AmountListResponse>>>(getCompositeDisposable()) {
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
                    public void onNext(Response<List<AmountListResponse>> response) {
                        progressDialog.cancel();
                        List<AmountListResponse> responsesData = response.body();
                        if (responsesData != null && responsesData.size() > 0) {
                            if (responsesData.get(0).getProjectName()!=null && responsesData.get(0).getProjectName().length()>0) {
                                projectName.setText("" + responsesData.get(0).getProjectName());
                            }else {
                                projectName.setText("Unknown Project");
                            }
                            totalFarmer.setText("" + responsesData.get(0).getTotalFarmer());
                            totalAmount.setText("" + responsesData.get(0).getTotalProjectWiseTotalAmount());
                            area.setText("" + responsesData.get(0).getTotalAreaGeoTag());
                            pendingAmount.setText("" + responsesData.get(0).getTotalProjectWisePendingAmount());
                            msgInfo.setText("Pending farmers: " + responsesData.get(0).getNoofFarmerPaymentPending() + " | Pending Area: " + responsesData.get(0).getPendingArea());
                            if (responsesData.get(0).getModelList() != null && responsesData.get(0).getModelList().size() > 0) {
                                binding.recyclerView.setVisibility(View.VISIBLE);
                                PaymentDetailAdapter adapter = new PaymentDetailAdapter(PaymentDetailsActivity.this, responsesData.get(0).getModelList(), PaymentDetailsActivity.this);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(PaymentDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                                binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                                binding.recyclerView.setAdapter(adapter);
                            } else {
                                binding.recyclerView.setVisibility(View.GONE);

                            }


                        } else {
                            binding.recyclerView.setVisibility(View.GONE);

                        }
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


    @Override
    public void onClick(String value) {
        if (value != null) {
            String[] split = value.split("=");
            if (split.length > 4) {
                //it will be call on registration page
                Log.v("cwqcqwcqw", value + "");

             /*   Intent in = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                in.putExtra("project_id", split[0]);
                in.putExtra("farmerId", split[1]);
                in.putExtra("balance", split[2]);
                in.putExtra("collected", split[3]);
                in.putExtra("amount", split[4]);
                startActivity(in);*/
                String service_type = null;
                String order_type = null;
                String farmerId = split[1];
                if (split.length > 5) {
                    service_type = split[5];
                }
                if (split.length > 6) {
                    order_type = split[6];
                }
                if (service_type != null && service_type.equalsIgnoreCase("service_type")) {
                    if (order_type != null && order_type.equalsIgnoreCase("pay")) {
                        Intent in = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                        in.putExtra("project_id", split[0]);
                        in.putExtra("farmerId", split[1]);
                        in.putExtra("balance", split[2]);
                        in.putExtra("collected", split[3]);
                        in.putExtra("amount", split[4]);
                        startActivity(in);
                    } else if (order_type != null && order_type.equalsIgnoreCase("pay_delivery")) {
                        Intent in = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                        in.putExtra("project_id", split[0]);
                        in.putExtra("farmerId", split[1]);
                        in.putExtra("balance", split[2]);
                        in.putExtra("collected", split[3]);
                        in.putExtra("amount", split[4]);
                        in.putExtra("delivery", "pay_delivery");
                        startActivity(in);
                    } else if (order_type != null && order_type.equalsIgnoreCase("delivery")) {
                        Intent in = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                        in.putExtra("project_id", split[0]);
                        in.putExtra("farmerId", split[1]);
                        in.putExtra("balance", split[2]);
                        in.putExtra("collected", split[3]);
                        in.putExtra("amount", split[4]);
                        in.putExtra("delivery", "delivery");
                        startActivity(in);
                    }
                } else if (farmerId != null) {
                    if (order_type != null && order_type.equalsIgnoreCase("pay")) {
                        Intent in = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                        in.putExtra("farmerId", farmerId);
                        in.putExtra("projectId", projectId);
                        startActivity(in);
                    } else if (order_type != null && order_type.equalsIgnoreCase("pay_delivery")) {
                        Intent in = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                        in.putExtra("farmerId", farmerId);
                        in.putExtra("projectId", projectId);
                        in.putExtra("delivery", "pay_delivery");
                        startActivity(in);
                    } else if (order_type != null && order_type.equalsIgnoreCase("delivery")) {
                        Intent in = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                        in.putExtra("farmerId", farmerId);
                        in.putExtra("projectId", projectId);
                        in.putExtra("delivery", "delivery");
                        startActivity(in);
                    }
                }


            }
        }
    }
}
