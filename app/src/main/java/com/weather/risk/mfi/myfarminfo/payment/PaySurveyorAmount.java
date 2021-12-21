package com.weather.risk.mfi.myfarminfo.payment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.ViewDataBinding;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.SurveyorAmountHistoryBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.marketplace.BaseActivity;
import com.weather.risk.mfi.myfarminfo.payment.model.PaySurveyorAmountRequest;
import com.weather.risk.mfi.myfarminfo.payment.model.PaymentListRequest;
import com.weather.risk.mfi.myfarminfo.payment.model.SurveyorResponseBean;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PaySurveyorAmount extends BaseActivity {

    private ApiService apiService;
    SurveyorAmountHistoryBinding binding;
    double remainingAmount = 0;

    public TextView pendingAmount, transferreAmount, collectedAmount;

    String upivirtualid = "9987020616@upi";
    final int UPI_PAYMENT = 0;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (SurveyorAmountHistoryBinding) viewDataBinding;
        pendingAmount = binding.pendingAmount;
        transferreAmount = binding.transferreAmount;
        collectedAmount = binding.collectedAmount;
        apiService = AppController.getInstance().getApiService();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (AppConstant.user_id != null && AppConstant.user_id.length() > 0) {
            getDetailsMethod();
        }
        binding.payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String colAm = binding.collectAmount.getText().toString().trim();
                if (colAm != null && colAm.length() > 0) {
                    double d = Double.parseDouble(colAm);
                    if (d > 0) {
                        payUsingUpi(AppConstant.visible_Name, upivirtualid, "WRMS Surveyor Payment", colAm);
                    } else {
                        Toast.makeText(getApplicationContext(), "You enter valid amount", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You enter amount", Toast.LENGTH_SHORT).show();

                }
            }
        });
        binding.collectAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    double a = Double.parseDouble("" + editable);
                    if (a > remainingAmount) {
                        Toast.makeText(getApplicationContext(), "You can not enter more than total amount", Toast.LENGTH_SHORT).show();
                        binding.collectAmount.setText(null);
                        binding.pendingAmount.setText(remainingAmount + "");
                    } else {
                        double bl = remainingAmount - a;
                        binding.pendingAmount.setText(bl + "");
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.surveyor_amount_history;
    }


    private void getDetailsMethod() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        PaymentListRequest request = new PaymentListRequest();
        // request.setUserID(100615);
        request.setUserID(Integer.valueOf(AppConstant.user_id));
        // request.setUserID("userID",AppConstant.user_id);
        apiService.getSurveyorCollectedAmount(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<SurveyorResponseBean>>>(getCompositeDisposable()) {
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
                    public void onNext(Response<List<SurveyorResponseBean>> response) {
                        progressDialog.cancel();
                        List<SurveyorResponseBean> responsesData = response.body();
                        if (responsesData != null && responsesData.size() > 0) {
                            collectedAmount.setText("" + responsesData.get(0).getTotalCollectedAmount());
                            transferreAmount.setText("" + responsesData.get(0).getTotalAmountTransfered());
                            if (responsesData.get(0).getTotalCollectedAmount() != null && responsesData.get(0).getTotalAmountTransfered() != null) {
                                double a = Double.parseDouble(responsesData.get(0).getTotalCollectedAmount());
                                double b = Double.parseDouble(responsesData.get(0).getTotalAmountTransfered());
                                remainingAmount = 0;
                                if (a >= b) {
                                    remainingAmount = a - b;
                                }

                            }
                            pendingAmount.setText("" + remainingAmount);
                            if (remainingAmount > 0) {
                                binding.payNow.setVisibility(View.VISIBLE);
                            } else {
                                binding.payNow.setVisibility(View.GONE);
                            }


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


    void payUsingUpi(String name, String upiId, String note, String amount) {
        Log.e("main ", "name " + name + "--up--" + upiId + "--" + note + "--" + amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaySurveyorAmount.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response " + resultCode);
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaySurveyorAmount.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaySurveyorAmount.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: " + approvalRefNo);
                String colAm = binding.collectAmount.getText().toString().trim();
                submitMethod(colAm, approvalRefNo);


            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaySurveyorAmount.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(PaySurveyorAmount.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(PaySurveyorAmount.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    private void submitMethod(String colle, String refNum) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        PaySurveyorAmountRequest request = new PaySurveyorAmountRequest();
        if (refNum != null && refNum.length() > 0) {
            request.setTransactionID(refNum);
        }
        request.setAmountTransfer(colle);
        request.setSurveyerID(Integer.valueOf(AppConstant.user_id));

        request.setTransactionImage(null);
        AppController.getInstance().getApiService().paySurveyorAmount(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        Toast.makeText(PaySurveyorAmount.this, "Payment successful submitted.", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });

    }

}

