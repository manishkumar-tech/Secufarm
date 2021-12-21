package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;

import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.AdminDashboard_New;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CheckoutActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.payment.model.OTPResponse;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.GetOTPRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OtpVerifyRequest;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CheckoutActivityNew;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class CheckoutActivityNew extends BaseActivity {
    CheckoutActivityBinding binding;
    String balanceAmnt;
    double amntTotal = 0.0, bal = 0.0;
    ArrayList<CategoryDetailResponse> myList = new ArrayList<>();

    ArrayList<JSONObject> checkoutList = new ArrayList<JSONObject>();

    String farmerId, projectID;
    String userId = null;

    RadioButton cashRadio, upiRadio;
    RadioGroup radioGroup;
    String isPaymentType = null;
    String upivirtualid = "9987020616@upi";
    final int UPI_PAYMENT = 0;

    ProgressDialog progressDialog;
    DBAdapter db;

    static String val = "qw";
    String farmID = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (CheckoutActivityBinding) viewDataBinding;
        farmerId = getIntent().getStringExtra("farmerId");

        farmID = getIntent().getStringExtra("farmID");
        db = new DBAdapter(this);
        Log.v("csccs", farmerId + "");


        List list = new ArrayList<>();
        Iterator in = list.iterator();


        cashRadio = binding.cashRadio;
        upiRadio = binding.upiRadio;
        radioGroup = binding.radioGroup;
        radioGroup.setVisibility(View.VISIBLE);
        userId = AppConstant.user_id;
        projectID = getIntent().getStringExtra("project_id");
        myList = getIntent().getParcelableArrayListExtra("list");
        balanceAmnt = getIntent().getStringExtra("balance");
        amntTotal = getIntent().getDoubleExtra("amount", 0.0);
        if (balanceAmnt != null && !balanceAmnt.equalsIgnoreCase("null")) {
            binding.balanceAmount.setText(balanceAmnt + "");
        } else {
            binding.balanceAmount.setText("0");
        }
        binding.totalAmount.setText(amntTotal + "");
        binding.amount.setText(amntTotal + "");
        if (balanceAmnt != null && balanceAmnt.length() > 0) {
            bal = Double.parseDouble(balanceAmnt);
        }
        binding.payableAmount.setText("" + (bal + amntTotal));
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkoutList != null && checkoutList.size() > 0) {
                    finish();
                } else {
                  /*  Intent in = new Intent(getApplicationContext(), FarmerListActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);*/
                    finish();
                }
            }
        });
        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if (projectID != null) {*/
                if (farmerId != null && checkoutList != null && checkoutList.size() > 0) {
                    if (isPaymentType != null) {
                        paymentAlert();
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Pleaseselectpaymenttype", R.string.Pleaseselectpaymenttype);
                    }

                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Pleaseadditem", R.string.Pleaseadditem);
                }
               /* } else {
                    Toast.makeText(getApplicationContext(), "Project Id not Found", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        for (int i = 0; i < myList.size(); i++) {
            JSONObject obj = new JSONObject();
            try {
                if (myList.get(i).getServiceID() != null) {
                    int abcd = Integer.parseInt(myList.get(i).getServiceID());
                    obj.put("FarmerServiceID", abcd);
                }
                if (userId != null) {
                    obj.put("UserID", userId);
                } else {
                    obj.put("UserID", "1");
                }
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                obj.put("ReciptNo", "WRMS" + ts);
                obj.put("Quantity", myList.get(i).getQuantity());
                obj.put("Rate", myList.get(i).getPrice());
                obj.put("ProjectID", projectID);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            checkoutList.add(obj);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);
                // Add logic here
                switch (index) {
                    case 0: // first button
                        isPaymentType = "COD";
                        break;
                    case 1: // secondbutton
                        isPaymentType = "UPI";
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
//    if (checkoutList != null && checkoutList.size() > 0) {
//      finish();
//    } else {
//      Intent in = new Intent(getApplicationContext(), FarmerListActivity.class);
//      in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//      startActivity(in);
//      finish();
//    }
        finish();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.checkout_activity;
    }


    private void checkoutMethod(String fId, String isPType) {
        // display a progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog

       /* progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                return false;
            }
        });*/

        CheckoutRequest request = new CheckoutRequest();
        request.setPaymentType(isPType);
        request.setDeliveryStatus("Pending");
        // request.setProjectID(pId);
        request.setFarmerid("" + fId);
        if (projectID != null) {
            request.setProjectid(projectID);
        }

        request.setServices(checkoutList.toString());
        if (userId != null) {
            request.setUserID(userId);
        } else {
            request.setUserID("1");
        }
        Log.v("Request_Checkout", new Gson().toJson(request) + "===" + new Gson().toJson(checkoutList) + "");
        AppController.getInstancenew().getApiServiceCheckout().checkoutMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<CheckoutResponse>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        Log.v("wvqevqevqeError", e.getMessage() + " " + e.toString());
                        showError(getDynamicLanguageValue(getApplicationContext(), "network_error", R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));

                    }

                    @Override
                    public void onNext(Response<CheckoutResponse> response) {

                        CheckoutResponse responsesData = response.body();
                        Log.v("vdsvsdvsdvsddddd", new Gson().toJson(response) + "");
                        if (responsesData != null && responsesData.getAddServicesResult().length() > 0) {
                            checkoutList = new ArrayList<>();
                            checkoutList.clear();
                            binding.placeOrder.setVisibility(View.GONE);
                            if (responsesData.getAddServicesResult().contains("Succ")) {
                                String[] split = responsesData.getAddServicesResult().split(":");
                                if (split.length > 1) {
                                    getOrderMethod(split[1]);
                                }

                            } else {
                                getOrderMethod(responsesData.getAddServicesResult());
                            }

                        }
                    }
                });

    }


    @SuppressLint("SetTextI18n")
    public void placedPopMethod(final String numb) {
        //  final BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.DialogSlideAnim);
        final Dialog dialog = new Dialog(this);
        //  final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
            View decor = this.getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.5f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.placed_order_popup);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView successText = (TextView) dialog.findViewById(R.id.success);
        String SMS = getDynamicLanguageValue(getApplicationContext(), "Yourorderbooked", R.string.Yourorderbooked) + " \n" +
                getDynamicLanguageValue(getApplicationContext(), "Ordernumberis", R.string.Ordernumberis) + "  " + numb + ". \n" +
                getDynamicLanguageValue(getApplicationContext(), "Theorderwillbedeliver", R.string.Theorderwillbedeliver);
        successText.setText(SMS);
//        successText.setText("Your order has been booked successfully.\nOrder number is " + numb + ".\nThe order will be deliver soon.");
        Button okay = (Button) dialog.findViewById(R.id.okay_btn);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent in = new Intent(CheckoutActivityNew.this, MainProfileActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        });
        dialog.show();
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

    private void paymentAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Dear " + AppConstant.visible_Name + "\n\n" + getDynamicLanguageValue(getApplicationContext(), "Areyousureyourpayment", R.string.Areyousureyourpayment) + " " + isPaymentType + "? ");
        builder.setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Yes", R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                db.open();
                db.clearCartItem();
                db.close();

                if (isPaymentType != null && isPaymentType.equalsIgnoreCase("UPI")) {
                    payUsingUpi(farmerId + "", upivirtualid, "WRMS Payment", "" + amntTotal);
                } else {
                    checkoutMethod(farmerId, isPaymentType);
                }
            }
        });
        builder.setNegativeButton(getDynamicLanguageValue(getApplicationContext(), "No", R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void payUsingUpi(String name, String upiId, String note, String amount) {
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
            getDynamicLanguageToast(getApplicationContext(), "NoUPIAppisfound", R.string.NoUPIAppisfound);
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
        if (isConnectionAvailable(CheckoutActivityNew.this)) {
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
                checkoutMethod(farmerId, isPaymentType);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                getDynamicLanguageToast(getApplicationContext(), "Paymentcancelledbyuser", R.string.Paymentcancelledbyuser);
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                getDynamicLanguageToast(getApplicationContext(), "TransactionfailedPleasetryagain", R.string.TransactionfailedPleasetryagain);
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            getDynamicLanguageToast(getApplicationContext(), "Internetconnectionisnotavailable", R.string.Internetconnectionisnotavailable);
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


    private void getOrderMethod(final String orderN) {

        final OrderDetailRequest request = new OrderDetailRequest();
        // request.setProjectID(pId);
        request.setOrderNumber(orderN);


        AppController.getInstance().getApiService().orderDetailMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<OrderDetailResponse>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getDynamicLanguageValue(getApplicationContext(), "network_error", R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<OrderDetailResponse> response) {


                        OrderDetailResponse responsesData = response.body();
                        Log.v("vsdvsdvsdvsd", new Gson().toJson(response) + "===" + new Gson().toJson(response));
                        if (responsesData != null) {
                          /*  binding.orderNo.setText("" + orderN);
                            binding.balance.setText(responsesData.getBalanceAmount() + "");
                            binding.date.setText(responsesData.getOrderdate() + "");
                            binding.totalAmount.setText(responsesData.getTotalAmount() + "");
                            binding.collectedAmount.setText(responsesData.getCollectedAmount() + "");
                            */
                            if (isPaymentType != null && isPaymentType.equalsIgnoreCase("UPI")) {
                                setOTPMethod(orderN, "" + responsesData.getTotalAmount(), "0", "" + responsesData.getTotalAmount());
                            } else {
                                setOTPMethod(orderN, "" + responsesData.getTotalAmount(), "" + responsesData.getTotalAmount(), "0");
                            }


                        }

                    }
                });

    }


    private void setOTPMethod(final String num, String tot, String bala, String colle) {

        GetOTPRequest request = new GetOTPRequest();
        // request.setProjectID(pId);
        if (num != null && num.length() > 0) {
            request.setOrderNumber(num);//Policy ID
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


        if (farmID != null && !farmID.equalsIgnoreCase("null") && farmID.length() > 0) {
            request.setFarmID(Integer.valueOf(farmID));
        }

        request.setType("farmer");

        if (isPaymentType != null && isPaymentType.equalsIgnoreCase("UPI")) {
            request.setPaymentType("UPI");
        } else {
            request.setPaymentType("COD");
        }
        //PolicyID
        String language = AppManager.getInstance().getSelectedLanguages(this);
        request.setLanguage(language);

        AppController.getInstance().getApiService().getOTPMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<OTPResponse>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getDynamicLanguageValue(getApplicationContext(), "network_error", R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<OTPResponse> response) {


                        OTPResponse otpResponse = response.body();

                        if (otpResponse != null && otpResponse.getOTP() != null && otpResponse.getOTP() > 9999) {
                            verifyOTPMethod(num, otpResponse.getOTP(), otpResponse.getTransactionNumber());
                        } else {

                            if (progressDialog != null) {
                                progressDialog.cancel();
                            }
                            placedPopMethod(num);
                        }

                      /*  if (otpN!=null && otpN>9999){
                            verifyOTPMethod(num,otpN);
                        }else {
                            if (progressDialog!=null) {
                                progressDialog.cancel();
                            }
                            placedPopMethod(num);
                        }*/


                    }
                });

    }


    private void verifyOTPMethod(final String numb, Integer otpS, String trI) {
        // display a progress dialog


        final OtpVerifyRequest request1 = new OtpVerifyRequest();
        if (numb != null) {
            request1.setOrderNumber(numb);
        }

        request1.setOTPForVerification("" + otpS);
        if (trI != null && trI.length() > 0) {
            request1.setTransactionNumber(trI);
        }

        request1.setType("farmer");

        AppController.getInstance().getApiService().verifyOTPMethod(request1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<String>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getDynamicLanguageValue(getApplicationContext(), "network_error", R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<String> response) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        String msg = response.body();

                        placedPopMethod(numb);

                       /* if (msg!=null && msg.contains("nvalid")){
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                            binding.otp.setText(null);
                        }else {
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                            binding.otpLay.setVisibility(View.GONE);
                            binding.verifyOtp.setVisibility(View.GONE);
                            binding.successLay.setVisibility(View.VISIBLE);
                            binding.otp.setText(null);
                        }*/
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguages();
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtFarmRegistration, 2);
        setFontsStyleTxt(this, binding.txtamount, 6);
        setFontsStyleTxt(this, binding.amount, 5);
        setFontsStyleTxt(this, binding.txttotalAmount, 6);
        setFontsStyleTxt(this, binding.totalAmount, 5);
        setFontsStyleTxt(this, binding.txtbalanceAmount, 6);
        setFontsStyleTxt(this, binding.balanceAmount, 5);
        setFontsStyleTxt(this, binding.txtPayableAmount, 6);
        setFontsStyleTxt(this, binding.payableAmount, 5);
        setFontsStyle(this, binding.backBtn);
        setFontsStyle(this, binding.placeOrder);
        setFontsStyle(this, binding.cashRadio);
        setFontsStyle(this, binding.upiRadio);

        //Tab Service
        setDynamicLanguage(this, binding.txtFarmRegistration, "CheckOut", R.string.CheckOut);
        setDynamicLanguage(this, binding.txtamount, "Amount", R.string.Amount);
        setDynamicLanguage(this, binding.txtbalanceAmount, "previous_balance_amount", R.string.previous_balance_amount);
        setDynamicLanguage(this, binding.txtPayableAmount, "payable_amount", R.string.payable_amount);
        setDynamicLanguage(this, binding.backBtn, "Back", R.string.Back);
        setDynamicLanguage(this, binding.placeOrder, "PlaceOrder", R.string.PlaceOrder);
        setDynamicLanguage(this, binding.cashRadio, "cash_on_delivery", R.string.cash_on_delivery);
        setDynamicLanguage(this, binding.upiRadio, "upi", R.string.upi);

    }


}
