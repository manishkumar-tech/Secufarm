package com.weather.risk.mfi.myfarminfo.policyregistration;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getRandomNumber;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.AdminDashboard_New;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.cattledashboard.CattleDashboards;
import com.weather.risk.mfi.myfarminfo.cattledashboard.CattleObject;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CattledashboardfarmerpopupBinding;
import com.weather.risk.mfi.myfarminfo.databinding.PolicyotpverificationPopupBinding;
import com.weather.risk.mfi.myfarminfo.databinding.PolicypaymentBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.marketplace.CheckoutActivityNew;
import com.weather.risk.mfi.myfarminfo.payment.model.OTPResponse;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.GetOTPRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OtpVerifyRequest;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PolicyPayment extends AppCompatActivity {

    PolicypaymentBinding binding;
    String ActivityName = "", totalAssured = "0.0", totalFee = "0.0", PayableTotalAmounts = "0.0";
    String projectID = "0";
    String farmID = "", farmerId = "", PolicyID = "0", areaSize = "0", PendingAmount = "0";
    String isPaymentType = null;
    String upivirtualid = "9987020616@upi";
    final int UPI_PAYMENT = 0;
    private CompositeDisposable compositeDisposable;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.policypayment);


        compositeDisposable = new CompositeDisposable();

        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                if (bundle.getString("ActivityName") != null && bundle.getString("ActivityName").length() > 0) {
                    ActivityName = bundle.getString("ActivityName");
                }
                if (ActivityName != null && ActivityName.equalsIgnoreCase("PolicyRegistrationNew")) {
                    binding.tbwPendingAmount.setVisibility(View.GONE);
                } else if (ActivityName != null && ActivityName.equalsIgnoreCase("ServiceHistory")) {
                    binding.tbwPendingAmount.setVisibility(View.VISIBLE);
                }

                if (bundle.getString("totalAssured") != null && bundle.getString("totalAssured").length() > 0) {
                    totalAssured = bundle.getString("totalAssured");
                }
                if (bundle.getString("totalFee") != null && bundle.getString("totalFee").length() > 0) {
                    totalFee = bundle.getString("totalFee");
                }
                if (bundle.getString("projectID") != null && bundle.getString("projectID").length() > 0) {
                    projectID = bundle.getString("projectID");
                }
                if (bundle.getString("farmID") != null && bundle.getString("farmID").length() > 0) {
                    farmID = bundle.getString("farmID");
                }
                if (bundle.getString("farmerId") != null && bundle.getString("farmerId").length() > 0) {
                    farmerId = bundle.getString("farmerId");
                }
                if (bundle.getString("PolicyID") != null && bundle.getString("PolicyID").length() > 0) {
                    PolicyID = bundle.getString("PolicyID");
                }
                if (bundle.getString("areaSize") != null && bundle.getString("areaSize").length() > 0) {
                    areaSize = bundle.getString("areaSize");
                }
                if (bundle.getString("PendingAmount") != null && bundle.getString("PendingAmount").length() > 0) {
                    PendingAmount = bundle.getString("PendingAmount");
                }

                if (areaSize != null && !areaSize.equalsIgnoreCase("null")) {
                    binding.txtFarmAreas.setText(String.valueOf(areaSize));
                } else {
                    binding.txtFarmAreas.setText("");
                }
                if (totalAssured != null && !totalAssured.equalsIgnoreCase("null")) {
                    binding.txtValueAssureds.setText(String.valueOf(totalAssured));
                } else {
                    binding.txtValueAssureds.setText("");
                }
                if (totalFee != null && !totalFee.equalsIgnoreCase("null")) {
                    binding.txtTotalAmounts.setText(String.valueOf(totalFee));
                    binding.txtPayableAmounts.setText(String.valueOf(totalFee));
                } else {
                    binding.txtTotalAmounts.setText("");
                    binding.txtPayableAmounts.setText("");

                }
                if (totalFee != null && !PendingAmount.equalsIgnoreCase("null")) {
                    binding.txtPendingAmounts.setText(String.valueOf(PendingAmount));
                } else {
                    binding.txtPendingAmounts.setText("");
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        binding.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = binding.radioGroup.findViewById(checkedId);
                int index = binding.radioGroup.indexOfChild(radioButton);
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
        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if (projectID != null) {*/
                if (farmerId != null) {
                    if (isPaymentType != null) {
                        paymentAlert();
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Pleaseselectpaymenttype", R.string.Pleaseselectpaymenttype);
                    }

                } else {
                    getDynamicLanguageToast(getApplicationContext(), "FarmerIdisblanked", R.string.FarmerIdisblanked);
                }
            }
        });
        binding.txtPayableAmounts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    double nValue = Double.parseDouble(s.toString());
                    if (ActivityName != null && ActivityName.equalsIgnoreCase("PolicyRegistrationNew")) {
                        if (totalFee != null && !totalFee.equalsIgnoreCase("null")
                                && totalFee.length() > 0) {
                            double total = Double.parseDouble(totalFee);
                            if (nValue > total) {
                                binding.txtPayableAmounts.setText(totalFee);
                                getDynamicLanguageToast(getApplicationContext(), "Youcantentermorethan", R.string.Youcantentermorethan);
                            }
                        }
                    } else if (ActivityName != null && ActivityName.equalsIgnoreCase("ServiceHistory")) {
                        if (PendingAmount != null && !PendingAmount.equalsIgnoreCase("null")
                                && totalFee.length() > 0) {
                            double total = Double.parseDouble(PendingAmount);
                            if (nValue > total) {
                                binding.txtPayableAmounts.setText(PendingAmount);
                                getDynamicLanguageToast(getApplicationContext(), "Youcantentermorethan", R.string.Youcantentermorethan);
                            }
                        }
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });

        binding.txtPromisetopaydates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.setDatePicker(PolicyPayment.this, binding.txtPromisetopaydates, "YYYYMMDD");
            }
        });

    }

    private void paymentAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Dear " + AppConstant.visible_Name + "\n\n" + getDynamicLanguageValue(getApplicationContext(), "Areyousureyourpayment", R.string.Areyousureyourpayment) + " " + isPaymentType + "? ");
        builder.setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Yes", R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (isPaymentType != null && isPaymentType.equalsIgnoreCase("UPI")) {
//                    payUsingUpi(farmerId + "", upivirtualid, "WRMS Payment", "" + totalFee);
                    if (binding.txtPayableAmounts.getText().toString() != null &&
                            binding.txtPayableAmounts.getText().toString().length() > 0 &&
                            !binding.txtPayableAmounts.getText().toString().equalsIgnoreCase("null")) {
                        PayableTotalAmounts = binding.txtPayableAmounts.getText().toString();
                    } else {
                        PayableTotalAmounts = "0.0";
                    }
                    payUsingUpi(farmerId + "", upivirtualid, "WRMS Payment", "" + PayableTotalAmounts);
                } else {
                    String PayableAmount = binding.txtPayableAmounts.getText().toString();
                    String Promisetopaydate = binding.txtPromisetopaydates.getText().toString();
                    setOTPMethod();
//                    checkoutMethod(farmerId, isPaymentType);
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
        String randomNumber = getRandomNumber();
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
//                .appendQueryParameter("mc", "")// Herojit Uncomment
                //.appendQueryParameter("tid", "02125412")
//                .appendQueryParameter("tr", "25584584")
//                .appendQueryParameter("tr", randomNumber)// Herojit Uncomment
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
            getDynamicLanguageToast(getApplicationContext(), "NoUPIappfound", R.string.NoUPIappfound);
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
        if (isConnectionAvailable(PolicyPayment.this)) {
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
//                checkoutMethod(farmerId, isPaymentType);
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


    private void setOTPMethod() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
        progressDialog.show();

        GetOTPRequest request = new GetOTPRequest();
        // request.setProjectID(pId);
        request.setOrderNumber(PolicyID);//Policy ID

        request.setTotalAmount(0.0);
        request.setBalanceAmount(0.0);
        if (binding.txtPayableAmounts.getText().toString() != null &&
                binding.txtPayableAmounts.getText().toString().length() > 0 &&
                !binding.txtPayableAmounts.getText().toString().equalsIgnoreCase("null")) {
            PayableTotalAmounts = binding.txtPayableAmounts.getText().toString();
        } else {
            PayableTotalAmounts = "0.0";
        }
        if (PayableTotalAmounts != null && PayableTotalAmounts.length() > 0) {
            double amount = Double.parseDouble(PayableTotalAmounts);
            request.setCollectedAmount(amount);
        } else {
            request.setCollectedAmount(0.0);
        }
//        if (totalFee != null && totalFee.length() > 0) {
//            double amount = Double.parseDouble(totalFee);
//            request.setCollectedAmount(amount);
//        } else {
//            request.setCollectedAmount(0.0);
//        }


        if (farmID != null && !farmID.equalsIgnoreCase("null") && farmID.length() > 0) {
            request.setFarmID(Integer.valueOf(farmID));
        }

//        request.setType("farmer");
        try {
            String userTypeID = AppConstant.userTypeID;
            if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") ||
                    userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
                //Input Verification
                request.setType("surveyor");
            } else {//Auto Verification in Farmer
                request.setType("farmer");
            }
        } catch (Exception ex) {
            request.setType("farmer");
            ex.printStackTrace();
        }

        if (isPaymentType != null && isPaymentType.equalsIgnoreCase("UPI")) {
            request.setPaymentType("UPI");
        } else {
            request.setPaymentType("COD");
        }
        //Pormise to Pay Date
        if (binding.txtPromisetopaydates.getText().toString() != null &&
                binding.txtPromisetopaydates.getText().toString().length() > 0) {
            request.setPTPDate(binding.txtPromisetopaydates.getText().toString());
        }
        //PolicyID
        String language = AppManager.getInstance().getSelectedLanguages(this);
        request.setLanguage(language);

//        AppController.getInstance().getApiService().getOTPMethodPolcy(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        AppController.getInstance().getApiServiceTest().getOTPMethodPolcy(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<OTPResponse>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        getDynamicLanguageToast(getApplicationContext(), "network_error", R.string.network_error);


                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        getDynamicLanguageToast(getApplicationContext(), "server_not_found", R.string.server_not_found);

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<OTPResponse> response) {


                        OTPResponse otpResponse = response.body();

                        if (otpResponse != null && otpResponse.getOTP() != null && otpResponse.getOTP() > 9999) {
//                            verifyOTPMethod(PolicyID, otpResponse.getOTP(), otpResponse.getTransactionNumber());
                            checkUserTypeSurveyor(PolicyID, otpResponse.getOTP(), otpResponse.getTransactionNumber());

                        } else {

                            if (progressDialog != null) {
                                progressDialog.cancel();
                            }
                            placedPopMethod(PolicyID);
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

    private void checkUserTypeSurveyor(final String numb, Integer otpS, String trI) {
        try {
            String userTypeID = AppConstant.userTypeID;
            if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") ||
                    userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
                //Input Verification
                progressDialog.cancel();
                checkOTPVerification(numb, otpS, trI);
            } else {//Auto Verification in Farmer
                verifyOTPMethod(numb, otpS, trI);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void checkOTPVerification(final String numb, Integer otpS, String trI) {
        try {
            final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);

            //  final Dialog dialog = new Dialog(getActivity());

            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.dimAmount = 0.5f;

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            // Include dialog.xml file
            PolicyotpverificationPopupBinding popbinding = PolicyotpverificationPopupBinding.inflate(LayoutInflater.from(this));
            dialog.setContentView(popbinding.getRoot());

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            UtilFonts.UtilFontsInitialize(this);

            popbinding.verifyOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String OTP = popbinding.otp.getText().toString();
                    if (OTP != null && OTP.length() > 0) {
                        if (otpS != null && OTP.equalsIgnoreCase(String.valueOf(otpS))) {
                            dialog.dismiss();
                            progressDialog = new ProgressDialog(PolicyPayment.this);
                            progressDialog.setCancelable(false); // set cancelable to false
                            progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "OTPVerifying", R.string.OTPVerifying)); // set message
                            progressDialog.show();
                            verifyOTPMethod(numb, otpS, trI);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "OTPnotmatched", R.string.OTPnotmatched);
                        }
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "PleaseenterOTP", R.string.PleaseenterOTP);
                    }

                }
            });


            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        try {
            String userTypeID = AppConstant.userTypeID;
            if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") ||
                    userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
                //Input Verification
                request1.setType("surveyor");
            } else {//Auto Verification in Farmer
                request1.setType("farmer");
            }
        } catch (Exception ex) {
            request1.setType("farmer");
            ex.printStackTrace();
        }
//        request1.setType("farmer");

//        AppController.getInstance().getApiService().verifyOTPMethodPolicy(request1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        AppController.getInstance().getApiServiceTest().verifyOTPMethodPolicy(request1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<String>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        getDynamicLanguageToast(getApplicationContext(), "network_error", R.string.network_error);

                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        getDynamicLanguageToast(getApplicationContext(), "server_not_found", R.string.server_not_found);

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
//        successText.setText("Your service has been booked successfully.\nThe Service number is " + numb + ".\nYou can track the service status in the service status section");
        successText.setText(getDynamicLanguageValue(getApplicationContext(),
                "Yourorderbooked", R.string.Yourorderbooked) + " \n" + getDynamicLanguageValue(getApplicationContext(),
                "TheServicenumberis", R.string.TheServicenumberis) + numb + " \n" + getDynamicLanguageValue(getApplicationContext(),
                "Youcantracktheservice", R.string.Youcantracktheservice));
        Button okay = (Button) dialog.findViewById(R.id.okay_btn);

        setFontsStyle(this, okay);
        setDynamicLanguage(this, okay, "Ok", R.string.Ok);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent in = new Intent(PolicyPayment.this, MainProfileActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setLanguages();
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtCheckOut, 2);
        setFontsStyleTxt(this, binding.txtFarmArea, 6);
        setFontsStyleTxt(this, binding.txtFarmAreas, 5);
        setFontsStyleTxt(this, binding.txtValueAssured, 6);
        setFontsStyleTxt(this, binding.txtValueAssureds, 5);
        setFontsStyleTxt(this, binding.txtTotalAmount, 6);
        setFontsStyleTxt(this, binding.txtTotalAmounts, 5);
        setFontsStyleTxt(this, binding.txtPendingAmount, 6);
        setFontsStyleTxt(this, binding.txtPendingAmounts, 5);
        setFontsStyleTxt(this, binding.txtPayableAmount, 6);
        setFontsStyleTxt(this, binding.txtPromisetopaydate, 6);
        setFontsStyle(this, binding.txtPayableAmounts);
        setFontsStyle(this, binding.cashRadio);
        setFontsStyle(this, binding.upiRadio);
        setFontsStyle(this, binding.placeOrder);

        //Tab Service
        setDynamicLanguage(this, binding.txtCheckOut, "CheckOut", R.string.CheckOut);
        setDynamicLanguage(this, binding.txtFarmArea, "FarmArea", R.string.FarmArea);
        setDynamicLanguage(this, binding.txtValueAssured, "ValueAssured", R.string.ValueAssured);
        setDynamicLanguage(this, binding.txtPayableAmount, "payable_amount", R.string.payable_amount);
        setDynamicLanguage(this, binding.txtTotalAmount, "totalFees", R.string.totalFees);
        setDynamicLanguage(this, binding.txtPendingAmount, "PendingFees", R.string.PendingFees);
        setDynamicLanguage(this, binding.placeOrder, "BookPolicy", R.string.BookPolicy);
        setDynamicLanguage(this, binding.txtPromisetopaydate, "Promisetopaydate", R.string.Promisetopaydate);

    }
}
