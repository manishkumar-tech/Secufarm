package com.weather.risk.mfi.myfarminfo.marketplace;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;

import com.google.gson.Gson;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CheckoutActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutResponse;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CheckoutActivity extends BaseActivity {
    CheckoutActivityBinding binding;
    String balanceAmnt;
    double amntTotal = 0.0, bal = 0.0;
    ArrayList<CategoryDetailResponse> myList = new ArrayList<>();

    ArrayList<JSONObject> checkoutList = new ArrayList<JSONObject>();

    String farmerId, projectID;
    String userId = null;
    DBAdapter db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {

        binding = (CheckoutActivityBinding) viewDataBinding;
        farmerId = getIntent().getStringExtra("farmerId");

        userId = AppConstant.user_id;

        projectID = getIntent().getStringExtra("project_id");


        db = new DBAdapter(this);


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
                    Intent in = new Intent(getApplicationContext(), FarmerListActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                }
            }
        });
        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (projectID != null) {

                    if (farmerId != null && checkoutList != null && checkoutList.size() > 0) {
                        checkoutMethod(farmerId);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please add item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Project Id not Found", Toast.LENGTH_SHORT).show();
                }
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
                obj.put("AmountRate", myList.get(i).getPrice());
                obj.put("ProjectID", projectID);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            checkoutList.add(obj);
        }

    }

    @Override
    public void onBackPressed() {

        if (checkoutList != null && checkoutList.size() > 0) {
            finish();
        } else {
            Intent in = new Intent(getApplicationContext(), FarmerListActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            finish();
        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.checkout_activity;
    }


    private void checkoutMethod(String fId) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog

        CheckoutRequest request = new CheckoutRequest();
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

        Log.v("checkout_request", new Gson().toJson(request) + "===" + new Gson().toJson(checkoutList) + "");

        AppController.getInstancenew().getApiServiceCheckout().checkoutMethod(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<CheckoutResponse>>(getCompositeDisposable()) {
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

                    @Override
                    public void onNext(Response<CheckoutResponse> response) {
                        progressDialog.cancel();

                        CheckoutResponse responsesData = response.body();
                        Log.v("vdsvsdvsdvsddddd", new Gson().toJson(response) + "");
                        db.open();
                        db.clearCartItem();
                        db.close();
                        if (responsesData != null && responsesData.getAddServicesResult().length() > 0) {
                            checkoutList = new ArrayList<>();
                            checkoutList.clear();
                            binding.placeOrder.setVisibility(View.GONE);
                            if (responsesData.getAddServicesResult().contains("Succ")) {

                                String[] split = responsesData.getAddServicesResult().split(":");
                                if (split.length > 1) {
                                    placedPopMethod(split[1]);
                                }

                            } else {
                                placedPopMethod(responsesData.getAddServicesResult());
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

        successText.setText("Your order has been booked successfully.\nOrder number is " + numb + ".\nThe order will be deliver soon.");


        Button okay = (Button) dialog.findViewById(R.id.okay_btn);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent in = new Intent(CheckoutActivity.this, OrderDetailsActivity.class);
                in.putExtra("order_number", numb);
                in.putExtra("farmerId", farmerId);
                in.putExtra("from", "checkout");
                startActivity(in);
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

}
