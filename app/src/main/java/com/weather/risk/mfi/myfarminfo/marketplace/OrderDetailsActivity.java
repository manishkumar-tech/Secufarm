package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.ViewDataBinding;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.OrderDetailActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.payment.model.OTPResponse;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerServices;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.GetOTPRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailResponse;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.CameraUtils;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class OrderDetailsActivity extends BaseActivity {
    OrderDetailActivityBinding binding;

    String orderNumber = null;
    String totalAmnt = null;
    ArrayList<FarmerServices> checkoutList = new ArrayList<FarmerServices>();

    String project_id = null;
    String farmerId = null;
    Double balance = 0.0;
    String collected = null;
    RadioButton cashRadio, upiRadio, payLater;
    RadioGroup radioGroup;
    boolean isPaymentUPI = false;
    String upivirtualid = "9987020616@upi";
    final int UPI_PAYMENT = 0;
    String fromWhere = null;
    // String for_type = null;
    boolean deliveryFalg = false;
    boolean flagP = false;
    boolean flagPayLater = false;
    String deliveryType = null;
    TableRow paymentDateLay;
    TextView paymentDate;

    String serviceIds = null;
    String deliveryQty = null;
    String totalA = null;
    String imageURL = null;


    String imageString1 = "", userChoosenTask = "", imageStoragePath1 = "";
    ImageView imageView;
    JSONArray imageList;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final int MEDIA_TYPE_IMAGE = 1;
    final int REQUEST_CAMERA_START1 = 2;
    final int SELECT_FILE_START1 = 3;
    String farmID = null;

    @Override
    protected int getActivityLayout() {
        return R.layout.order_detail_activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (OrderDetailActivityBinding) viewDataBinding;

        imageList = new JSONArray();

        cashRadio = binding.cashRadio;
        upiRadio = binding.upiRadio;
        radioGroup = binding.radioGroup;
        payLater = binding.payLater;
        paymentDate = binding.txtDate;
        paymentDateLay = binding.paymentLay;
        imageView = binding.imageView;

        paymentDateLay.setVisibility(View.GONE);
        paymentDate.setText(null);

        paymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.setDateAfter30Days(OrderDetailsActivity.this, paymentDate, "YYYYMMDD");
            }
        });

        orderNumber = getIntent().getStringExtra("order_number");
        fromWhere = getIntent().getStringExtra("from");
        project_id = getIntent().getStringExtra("project_id");
        farmerId = getIntent().getStringExtra("farmerId");
        String bl = getIntent().getStringExtra("balance");
        deliveryType = getIntent().getStringExtra("delivery");

        serviceIds = getIntent().getStringExtra("serviceIds");
        deliveryQty = getIntent().getStringExtra("deliveryQty");
        totalA = getIntent().getStringExtra("totalA");
        farmID = getIntent().getStringExtra("farmID");


        if (bl != null && !bl.equalsIgnoreCase("null")) {
            balance = Double.valueOf(bl);
        }


        if (deliveryType != null && deliveryType.equalsIgnoreCase("pay_delivery")) {

            payLater.setVisibility(View.VISIBLE);
        } else {
            payLater.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        collected = getIntent().getStringExtra("collected");
        totalAmnt = getIntent().getStringExtra("amount");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);
                // Add logic here
                switch (index) {
                    case 0: // first button
                        isPaymentUPI = false;
                        flagP = true;
                        paymentDateLay.setVisibility(View.GONE);
                        paymentDate.setText(null);
                        flagPayLater = false;

                        if (deliveryType != null && deliveryType.equalsIgnoreCase("pay_delivery")) {
                            binding.sendOtp.setText(getResources().getString(R.string.CollectionDelivery));
                        } else if (deliveryType != null && deliveryType.equalsIgnoreCase("delivery")) {
                            binding.sendOtp.setText(getResources().getString(R.string.OnlyDelivery));
                        } else {
                            binding.sendOtp.setText(getResources().getString(R.string.OnlyCollection));
                        }


                        if (totalA != null && totalA.length() > 0) {
                            double d = Double.parseDouble(totalA);
                            if (d > 0 && balance != null && balance >= d) {
                                binding.collectAmount.setText("" + d);
                            } else if (balance != null && balance > 0) {
                                binding.collectAmount.setText("" + balance);
                            }
                        } else if (balance != null && balance > 0) {
                            binding.collectAmount.setText("" + balance);
                        }

                        break;
                    case 1: // secondbutton
                        isPaymentUPI = true;
                        flagP = true;
                        paymentDateLay.setVisibility(View.GONE);
                        paymentDate.setText(null);
                        flagPayLater = false;

                        if (deliveryType != null && deliveryType.equalsIgnoreCase("pay_delivery")) {
                            binding.sendOtp.setText(getResources().getString(R.string.CollectionDelivery));
                        } else if (deliveryType != null && deliveryType.equalsIgnoreCase("delivery")) {
                            binding.sendOtp.setText(getResources().getString(R.string.OnlyDelivery));
                        } else {
                            binding.sendOtp.setText(getResources().getString(R.string.OnlyCollection));
                        }

                        if (totalA != null && totalA.length() > 0) {
                            double d = Double.parseDouble(totalA);
                            if (d > 0 && balance != null && balance >= d) {
                                binding.collectAmount.setText("" + d);
                            } else if (balance != null && balance > 0) {
                                binding.collectAmount.setText("" + balance);
                            }
                        } else if (balance != null && balance > 0) {
                            binding.collectAmount.setText("" + balance);
                        }

                        break;
                    case 2: // secondbutton
                        isPaymentUPI = false;
                        flagP = true;
                        paymentDateLay.setVisibility(View.VISIBLE);
                        flagPayLater = true;
                        binding.sendOtp.setText("Only Delivery");


                        binding.collectAmount.setText("0");

                        break;
                }
            }
        });
        if (fromWhere != null && fromWhere.equalsIgnoreCase("checkout") && orderNumber != null && orderNumber.length() > 0) {
           /* if (orderNumber.contains("A")) {
                orderNumber = "1";
                getOrderMethod(orderNumber);
            } else {
                getOrderMethod(orderNumber);
            }*/

            Log.v("csacasc", orderNumber);
            getOrderMethod(orderNumber);

        } else {
            orderMethod();
        }

        binding.collectAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0 && balance != null) {
                    double a = Double.parseDouble("" + editable);
                    double b = balance;
                    Log.v("vsdsdvsdv", a + "====" + b);
                    if (a > b) {
                        Toast.makeText(getApplicationContext(), "You can not enter more than total amount", Toast.LENGTH_SHORT).show();
                        binding.collectAmount.setText(null);
                        binding.balance.setText(balance + "");
                    } else {
                        double bl = b - a;
                        binding.balance.setText(bl + "");
                    }
                }
            }
        });
        binding.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String collectedAmnt = binding.collectAmount.getText().toString();
                String tot = binding.totalAmount.getText().toString();
                String blAmnt = binding.balance.getText().toString();
                String ordN = binding.orderNo.getText().toString();

                if (flagP) {

                    if (flagPayLater) {


                        deliveryFalg = true;
                        setOTPMethod(ordN, tot, blAmnt, "0", null);


                    } else if (deliveryType != null && deliveryType.equalsIgnoreCase("delivery")) {


                        deliveryFalg = true;
                        setOTPMethod(ordN, tot, blAmnt, "0", null);


                    } else if (orderNumber != null && !orderNumber.equalsIgnoreCase("null")) {

                        if (deliveryType != null && deliveryType.equalsIgnoreCase("pay_delivery")) {
                            deliveryFalg = true;
                        }

                        if (collectedAmnt != null && collectedAmnt.length() > 0 && !collectedAmnt.equalsIgnoreCase("0.0") && !collectedAmnt.equalsIgnoreCase("0")) {
                            if (isPaymentUPI) {
                                payUsingUpi(ordN, upivirtualid, "WRMS Payment", collectedAmnt);
                            } else {
                                setOTPMethod(ordN, "0", blAmnt, collectedAmnt, null);
                            }
                        } else {
                            // binding.collectAmount.setFocusable(true);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseentertheamount), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.OrderNoisnot), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseselectpaymentmode), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (deliveryType != null && deliveryType.equalsIgnoreCase("pay_delivery")) {
            radioGroup.setVisibility(View.VISIBLE);
            //binding.collectAmount.setEnabled(false);
            flagP = false;
            binding.sendOtp.setText(getResources().getString(R.string.CollectionDelivery));
        } else if (deliveryType != null && deliveryType.equalsIgnoreCase("delivery")) {
            radioGroup.setVisibility(View.GONE);
            //binding.collectAmount.setEnabled(false);
            flagP = true;
            binding.sendOtp.setText(getResources().getString(R.string.OnlyDelivery));
        } else {
            radioGroup.setVisibility(View.VISIBLE);
            // binding.collectAmount.setEnabled(true);
            flagP = false;
            binding.sendOtp.setText(getResources().getString(R.string.OnlyCollection));
        }

    }

    @Override
    public void onBackPressed() {

      /*  Intent in = new Intent(getApplicationContext(), FarmerListActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);*/
        finish();
    }


    private void getOrderMethod(final String orderN) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getResources().getString(R.string.Dataisloading)); // set message
        progressDialog.show(); // show progress dialog
        final OrderDetailRequest request = new OrderDetailRequest();
        // request.setProjectID(pId);
        request.setOrderNumber(orderN);
        AppController.getInstance().getApiService().orderDetailMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<OrderDetailResponse>>(getCompositeDisposable()) {
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
                    public void onNext(Response<OrderDetailResponse> response) {
                        progressDialog.cancel();
                        OrderDetailResponse responsesData = response.body();
                        if (responsesData != null) {
                            binding.orderNo.setText("" + orderN);
                            binding.balance.setText(responsesData.getBalanceAmount() + "");
                            if (responsesData.getBalanceAmount() != null) {
                                balance = responsesData.getBalanceAmount();
                            }
                            binding.date.setText(responsesData.getOrderdate() + "");
                            binding.totalAmount.setText(responsesData.getTotalAmount() + "");
                            binding.collectedAmount.setText(responsesData.getCollectedAmount() + "");

                            if (balance != null && balance > 0) {
                                binding.sendOtp.setVisibility(View.VISIBLE);
                                binding.collectAmount.setText("" + balance);
                            } else if (deliveryType != null && deliveryType.contains("eliver")) {
                                binding.sendOtp.setVisibility(View.VISIBLE);
                            } else {
                                binding.sendOtp.setVisibility(View.GONE);
                            }


                            if (totalA != null && totalA.length() > 0) {
                                double d = Double.parseDouble(totalA);

                                if (d > 0 && balance != null && balance >= d) {
                                    binding.collectAmount.setText("" + d);
                                }
                            }


                        }

                    }
                });

    }


    private void setOTPMethod(String num, String tot, String bala, String colle, String refNum) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getResources().getString(R.string.Dataisloading)); // set message
        progressDialog.show(); // show progress dialog
        GetOTPRequest request = new GetOTPRequest();
        if (refNum != null && refNum.length() > 0) {
            request.setTransactionID(refNum);
        }
        // request.setProjectID(pId);
        if (num != null && num.length() > 0) {
            request.setOrderNumber(num);
        }
        if (tot != null) {
            double aDouble = Double.parseDouble(tot);
            request.setTotalAmount(aDouble);
        } else {
            request.setTotalAmount(0.0);
        }
        if (bala != null) {
            double aDouble = Double.parseDouble(bala);
            request.setBalanceAmount(aDouble);
        }
        if (colle != null) {
            double aDouble = Double.parseDouble(colle);
            request.setCollectedAmount(aDouble);
        }
        if (deliveryFalg) {
            request.setFlgDelivery(deliveryFalg);
        }

        if (isPaymentUPI) {
            request.setPaymentType("UPI");
        } else {
            request.setPaymentType("COD");
        }
        request.setType("surveyor");
        String language = AppManager.getInstance().getSelectedLanguages(this);
        request.setLanguage(language);


        AppController.getInstance().getApiService().getOTPMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<OTPResponse>>(getCompositeDisposable()) {
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
                    public void onNext(Response<OTPResponse> response) {
                        progressDialog.cancel();
                        OTPResponse otpResponse = response.body();
                        if (otpResponse != null && otpResponse.getExcecutionStatus() != null) {
                            Toast.makeText(getApplicationContext(), "" + otpResponse.getExcecutionStatus(), Toast.LENGTH_SHORT).show();
                        }
                        if (otpResponse != null && otpResponse.getOTP() != null && otpResponse.getOTP() > 0) {


                            Log.v("otp_response", otpResponse.getOTP() + "");

                            if (imageString1 != null && imageString1.length() > 10) {
                                if (otpResponse.getTransactionNumber() != null) {
                                    uploadImage(otpResponse.getTransactionNumber());
                                } else {

                                    uploadImage("");
                                }

                            } else {

                                Intent in = new Intent(OrderDetailsActivity.this, OtpVerifyActivity.class);
                                in.putExtra("order_number", orderNumber);
                                if (otpResponse.getTransactionNumber() != null) {
                                    in.putExtra("tr_id", otpResponse.getTransactionNumber());
                                }
                                if (deliveryFalg) {
                                    in.putExtra("for_type", "delivery");
                                }
                                in.putExtra("expectedDate", paymentDate.getText().toString());
                                in.putExtra("delivery", deliveryType);
                                in.putExtra("serviceIds", serviceIds);
                                in.putExtra("deliveryQty", deliveryQty);
                                in.putExtra("imageURL", imageURL);
                                in.putExtra("farmID", farmID);
                                startActivity(in);
                            }
                        }


                    }
                });

    }


    private void orderMethod() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        CheckoutRequest request = new CheckoutRequest();
        // request.setProjectID(pId);
        request.setFarmerid("" + farmerId);
        if (project_id != null) {
            request.setProjectid(project_id);
        }
        request.setServices(checkoutList.toString());
        // request.setUserID(String.valueOf(100615));
        request.setUserID(AppConstant.user_id);
        Log.v("svdvdvdv", "" + new Gson().toJson(request));
        AppController.getInstancenew().getApiServiceCheckout().checkoutMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<CheckoutResponse>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getString(R.string.data_not_found));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getString(R.string.server_not_found));

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<CheckoutResponse> response) {
                        progressDialog.cancel();
                        CheckoutResponse responsesData = response.body();
                        if (responsesData != null && responsesData.getAddServicesResult().length() > 0) {
                            if (responsesData.getAddServicesResult().contains("Succ")) {
                                String[] split = responsesData.getAddServicesResult().split(":");
                                if (split.length > 1) {
                                    orderNumber = split[1];


                                }

                            } else {
                                orderNumber = responsesData.getAddServicesResult();
                            }
                            binding.orderNo.setText("" + orderNumber);
                            binding.balance.setText(balance + "");
                            // binding.date.setText(responsesData.getOrderdate() + "");
                            binding.totalAmount.setText(totalAmnt + "");
                            binding.collectedAmount.setText(collected + "");


                            if (balance != null && balance > 0) {
                                binding.sendOtp.setVisibility(View.VISIBLE);
                                binding.collectAmount.setText("" + balance);
                            } else if (deliveryType != null && deliveryType.contains("eliver")) {
                                binding.sendOtp.setVisibility(View.VISIBLE);
                            } else {
                                binding.sendOtp.setVisibility(View.GONE);
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
            Toast.makeText(OrderDetailsActivity.this, getResources().getString(R.string.NoUPIappfound), Toast.LENGTH_SHORT).show();
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
        try {
            if (resultCode == Activity.RESULT_OK) {
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


                    case REQUEST_CAMERA_START1:
                        onCaptureImageResult1();
                        break;
                    case SELECT_FILE_START1:
                        onSelectFromGalleryResult1(data);
                        break;


                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(OrderDetailsActivity.this)) {
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
                Toast.makeText(OrderDetailsActivity.this, getResources().getString(R.string.Transactionissuccessfully), Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: " + approvalRefNo);
                String collectedAmnt = binding.collectAmount.getText().toString();
                String tot = binding.totalAmount.getText().toString();
                String blAmnt = binding.balance.getText().toString();
                String ordN = binding.orderNo.getText().toString();
                if (collectedAmnt != null && !collectedAmnt.equalsIgnoreCase("0.0") && !collectedAmnt.equalsIgnoreCase("0")) {
                    if (fromWhere != null && fromWhere.equalsIgnoreCase("checkout") && orderNumber != null && orderNumber.length() > 0) {
                        setOTPMethod(ordN, tot, blAmnt, collectedAmnt, approvalRefNo);
                        //in case of
                    } else {
                        setOTPMethod(ordN, "0", blAmnt, collectedAmnt, approvalRefNo);
                    }

                } else {
                    // binding.collectAmount.setFocusable(true);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseentertheamount), Toast.LENGTH_SHORT).show();
                }
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(OrderDetailsActivity.this, getResources().getString(R.string.Paymentcancelledbyuser), Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(OrderDetailsActivity.this, getResources().getString(R.string.TransactionfailedPleasetryagain), Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(OrderDetailsActivity.this, getResources().getString(R.string.Internetconnectionisnotavailable), Toast.LENGTH_SHORT).show();
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

 /*   private void submitMethod(String colle, String refNum) {
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

    }*/


    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.TakePhoto), getResources().getString(R.string.Selectfromgallery),
                getResources().getString(R.string.Cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.TakePhoto))) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(OrderDetailsActivity.this);
                    if (resultCam) {
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            cameraIntent1();
                        }
                    }

                } else if (items[item].equals(getResources().getString(R.string.Selectfromgallery))) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(OrderDetailsActivity.this);
                    if (resultCam) {
                        galleryIntent();
                    }
                } else if (items[item].equals(getResources().getString(R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath1 = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CAMERA_START1);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START1);

    }


    private void onCaptureImageResult1() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath1, options);
            imageView.setImageBitmap(bitmap);
            imageString1 = imageToString(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(bm);
        imageString1 = imageToString(bm);
//        String filePath = data.getData().getPath();
    }

    public String imageToString(Bitmap bmp) {
        String encodedImage = "null";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    private void uploadImage(final String tr_id) {
        try {
            final TransparentProgressDialog pDialog;
            pDialog = new TransparentProgressDialog(OrderDetailsActivity.this,
                    getResources().getString(R.string.Dataisloading));
            pDialog.setCancelable(false);
            pDialog.show();
            JSONObject jsonObject = null;
            String usi = AppConstant.user_id;
            Double lat = LatLonCellID.lat;
            Double lon = LatLonCellID.lon;
            Log.v("imageLat_long", lat + "," + lon);
            try {
                jsonObject = new JSONObject();
                jsonObject.putOpt("ImageString", imageString1);
                jsonObject.putOpt("UserID", usi);
                // jsonObject.putOpt("Lat_Lng", lat+","+lon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadImageURL_SCHEDULER, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.cancel();
                    Log.i("Response upload image", "" + response.toString());
                    //Herojit Comment
                    String res = response.toString();
                    res = res.replace("\":\"[\\\"", "\":\"");
                    res = res.replace("\\\"]\"", "\"");
                    res = res.replace("\\", "");
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.get("uploadBase64Image_SchedulerResult").toString().length() > 0) {
                            // Image may be not from secufarm folder
                            Double lat = LatLonCellID.lat;
                            Double lon = LatLonCellID.lon;
                            imageList = Utility.addImageName(jsonObject.get("uploadBase64Image_SchedulerResult").toString(), imageList, 5, String.valueOf(lat), String.valueOf(lon));
//                            addImageName(jsonObject.get("uploadBase64Image_SchedulerResult").toString(), flag);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Intent in = new Intent(OrderDetailsActivity.this, OtpVerifyActivity.class);
                    in.putExtra("order_number", orderNumber);
                    in.putExtra("tr_id", tr_id);
                    if (deliveryFalg) {
                        in.putExtra("for_type", "delivery");
                    }

                    in.putExtra("delivery", deliveryType);
                    in.putExtra("serviceIds", serviceIds);
                    in.putExtra("deliveryQty", deliveryQty);
                    in.putExtra("farmID", farmID);
                    in.putExtra("expectedDate", paymentDate.getText().toString());
                    if (imageList != null && imageList.length() > 0) {
                        in.putExtra("imageURL", imageList.toString());
                    }
                    startActivity(in);

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.SubmittedSuccessfully), Toast.LENGTH_LONG).show();
//                getResponse(response, co);
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.cancel();
                    Log.v("Response vishal coupon", "" + error.toString());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    40000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
