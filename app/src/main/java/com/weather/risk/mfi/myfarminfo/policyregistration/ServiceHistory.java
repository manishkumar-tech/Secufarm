package com.weather.risk.mfi.myfarminfo.policyregistration;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.ServicehistoryBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.marketplace.ProductOrderDetailsActivity;
import com.weather.risk.mfi.myfarminfo.payment.ItemClick;
import com.weather.risk.mfi.myfarminfo.payment.OrderHistoryActivity;
import com.weather.risk.mfi.myfarminfo.payment.model.OrderHistoryRequest;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class ServiceHistory extends AppCompatActivity implements ItemClick {

    DBAdapter db;
    ServicehistoryBinding binding;
    String farmerId = null, projectId = null, farmID = null;
    Double pAmnt = 0.0;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    List<List<ServiceHistoryResponse>> responseGlobal = null;
    List<ServiceHistoryResponse> newArray = new ArrayList<ServiceHistoryResponse>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.servicehistory);
        db = new DBAdapter(this);

//        apiService = AppController.getInstance().getApiService();
        apiService = AppController.getInstance().getApiServiceTest();
        compositeDisposable = new CompositeDisposable();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        projectId = getIntent().getStringExtra("projectId");
        farmerId = getIntent().getStringExtra("farmerId");
        farmID = getIntent().getStringExtra("farmID");
        if (farmerId != null && farmerId.length() > 0) {
            getDetailsMethod(farmerId);
        }
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getDetailsMethod(String id) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        OrderHistoryRequest request = new OrderHistoryRequest();
        request.setFarmerID(Integer.valueOf(id));
        if (projectId != null && !projectId.equalsIgnoreCase("null")) {
            request.setProjectID(Integer.valueOf(projectId));
        } else {
            request.setProjectID(0);
        }
        request.setOrderStatus("all");
        request.setDeliveryStatus("all");
        if (AppConstant.user_id != null && !AppConstant.user_id.equalsIgnoreCase("null")) {
            request.setUserID(Integer.valueOf(AppConstant.user_id));
        }
        apiService.ServiceHistory(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<List<ServiceHistoryResponse>>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        getDynamicLanguageToast(getApplicationContext(), "network_error", R.string.network_error);
                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        getDynamicLanguageToast(getApplicationContext(), "server_not_found", R.string.server_not_found);
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Response<List<List<ServiceHistoryResponse>>> response) {
                        progressDialog.cancel();
                        List<List<ServiceHistoryResponse>> responseD = response.body();
                        String value = "";
                        if (responseD != null && responseD.size() > 0) {
                            responseGlobal = responseD;
                            if (responseD.get(0) != null && responseD.get(0).size() > 0) {
                                binding.farmerId.setText("Farmer Id -" + responseD.get(0).get(0).getFarmerID());
                                String collectionAmunt = "" + responseD.get(0).get(0).getCollectedAmount();
                                if (collectionAmunt != null && !collectionAmunt.equalsIgnoreCase("null") && collectionAmunt.length() > 0) {
                                    binding.collectAmount.setText("" + collectionAmunt);
                                } else {
                                    binding.collectAmount.setText("0.0");
                                }
                                String totalAmount = "" + responseD.get(0).get(0).getTotalAmount();
                                if (totalAmount != null && !totalAmount.equalsIgnoreCase("null") && totalAmount.length() > 0) {
                                    binding.totalAmount.setText("" + totalAmount);
                                } else {
                                    binding.totalAmount.setText("0.0");
                                }
                                String pendingAmount = "" + responseD.get(0).get(0).getPendingAmount();
                                if (pendingAmount != null && !pendingAmount.equalsIgnoreCase("null") && pendingAmount.length() > 0) {
                                    binding.pendingAmount.setText("" + pendingAmount);
                                } else {
                                    binding.pendingAmount.setText("0.0");
                                }


//                                pAmnt = responseD.get(0).get(0).getBalanceAmount();
                            }
                            if (responseD.size() > 1 && responseD.get(1) != null && responseD.get(1).size() > 0) {
                                newArray = new ArrayList<ServiceHistoryResponse>();
                                for (int i = 0; i < responseD.get(1).size(); i++) {
                                   /* if (i!=0){
                                        newArray.add(responseD.get(1).get(i));
                                    }*/
                                    newArray.add(responseD.get(1).get(i));
                                }
                                binding.recyclerView.setVisibility(View.VISIBLE);
                                ServiceHistoryAdapter adapter = new ServiceHistoryAdapter(ServiceHistory.this, newArray, ServiceHistory.this);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(ServiceHistory.this, LinearLayoutManager.VERTICAL, false));
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
    public void onClick(String value) {
        try {
            if (value != null) {
                String[] split = value.split("=");//position
                int position = 0;
                if (split[1] != null && split[1].length() > 0) {
                    position = Integer.parseInt(split[1]);
                }
                if (split.length > 1 && farmerId != null) {
//                Intent in = new Intent(ServiceHistory.this, ProductOrderDetailsActivity.class);
//                    Intent in = new Intent(ServiceHistory.this, PolicyOrderDetails.class);
//                    in.putExtra("order_number", split[0]);
//                    in.putExtra("farmer_id", farmerId);
//                    in.putExtra("delivery", split[1]);
//                    in.putExtra("farmID", farmID);
//                    startActivity(in);
                    ServiceHistoryResponse res = newArray.get(position);
                    Intent in = new Intent(getApplicationContext(), PolicyPayment.class);
                    in.putExtra("ActivityName", "ServiceHistory");
                    in.putExtra("totalAssured", String.valueOf(res.getValueAssured()));
                    in.putExtra("areaSize", String.valueOf(res.getArea()));
                    in.putExtra("totalFee", String.valueOf(res.getOrderAmount()));
                    in.putExtra("projectID", String.valueOf(res.getProjectID()));
                    in.putExtra("farmID", String.valueOf(res.getFarmID()));
                    in.putExtra("farmerId", String.valueOf(res.getFarmerId()));
                    in.putExtra("PolicyID", String.valueOf(res.getOrderID()));
                    in.putExtra("PendingAmount", String.valueOf(res.getPendingAmount()));
                    startActivity(in);
                    finish();
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Idnotfound", R.string.Idnotfound);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguages();
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtServiceHistory, 2);
        setFontsStyleTxt(this, binding.txttotalAmount, 6);
        setFontsStyleTxt(this, binding.totalAmount, 5);
        setFontsStyleTxt(this, binding.txtcollectAmount, 6);
        setFontsStyleTxt(this, binding.collectAmount, 5);
        setFontsStyleTxt(this, binding.txtpendingAmount, 6);
        setFontsStyleTxt(this, binding.pendingAmount, 5);

        //Tab Service
        setDynamicLanguage(this, binding.txtServiceHistory, "ServiceHistory", R.string.ServiceHistory);
        setDynamicLanguage(this, binding.txttotalAmount, "TotalFees", R.string.TotalFees);
        setDynamicLanguage(this, binding.txtcollectAmount, "PaidFees", R.string.PaidFees);
        setDynamicLanguage(this, binding.txtpendingAmount, "PendingFees", R.string.PendingFees);
    }

}
