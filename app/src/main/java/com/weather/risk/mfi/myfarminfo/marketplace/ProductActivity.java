package com.weather.risk.mfi.myfarminfo.marketplace;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.ProductActivityBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryModel;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.RequestCategoryBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProductActivity extends BaseActivity implements DataInterface {

    private ApiService apiService;
    ProductActivityBinding binding;
    List<CategoryDetailResponse> dataList = new ArrayList<CategoryDetailResponse>();


    String balanceAmnt;
    String farmerId, projectID;

    DBAdapter db;
    String farmId = null;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (ProductActivityBinding) viewDataBinding;
        db = new DBAdapter(this);

        projectID = getIntent().getStringExtra("project_id");
        balanceAmnt = getIntent().getStringExtra("balance");
        farmerId = getIntent().getStringExtra("farmerId");
        farmId = getIntent().getStringExtra("farm_id");

        binding.farmLay.setVisibility(View.GONE);
        binding.orderHistory.setVisibility(View.GONE);
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                Cursor selectedService = db.getAllCartData();
                final int selectedServiceCount = selectedService.getCount();
                db.close();
                if (selectedServiceCount > 0) {
                    Intent in = new Intent(getApplicationContext(), MyCartActivity.class);
                    in.putExtra("balance", balanceAmnt);
                    in.putExtra("project_id", projectID);
                    in.putExtra("farmerId", farmerId);
                    startActivity(in);

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseaddatleastoneiteminacart), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        apiService = AppController.getInstance().getApiService();
        if (projectID != null) {
            getCategoryMethod(projectID);
        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.product_activity;
    }


    private void getCategoryMethod(final String proj_id) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getResources().getString(R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        RequestCategoryBean requestCategoryBean = new RequestCategoryBean();
        if (farmId != null && farmId.length() > 0) {
            requestCategoryBean.setFarmID(Integer.parseInt(farmId));
        }
        requestCategoryBean.setProjectID(Integer.parseInt(proj_id));


        apiService.getCategory(requestCategoryBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<CategoryModel>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getString(R.string.server_not_found));

                    }

                    @Override
                    public void onNext(Response<List<CategoryModel>> response) {
                        progressDialog.cancel();
                        List<CategoryModel> responsesData = response.body();
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.title.setText(getResources().getString(R.string.please_select_category));
                        if (responsesData != null && responsesData.size() > 0) {
                            ArrayList<String> catList = new ArrayList<>();
                            final ArrayList<Integer> catListIds = new ArrayList<Integer>();
                            catList.add("Select category");
                          /*  catList.add("Other category");
                            catListIds.add(0);*/
                            catListIds.add(0);
                            for (int i = 0; i < responsesData.size(); i++) {
                                catList.add(responsesData.get(i).getCategoryName());
                                catListIds.add(responsesData.get(i).getCategoryID());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_spinner_item, catList);
                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            binding.categorySpinner.setAdapter(dataAdapter);
                            binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i > 0) {
                                        if (catListIds.size() > 1 && catListIds.get(i) != null && catListIds.get(i) != 0) {
                                            getCategoryDetailsMethod(proj_id, catListIds.get(i));
                                        }
                                    }


                                   /* if (i == 1) {
                                        binding.productRecyclerView.setVisibility(View.VISIBLE);
                                        binding.emptyView.setVisibility(View.GONE);
                                        dataList = new ArrayList<CategoryDetailResponse>();
                                        CategoryDetailResponse dBean = new CategoryDetailResponse();

                                        dBean.setEndDate(null);
                                        dBean.setPrice(0.0);
                                        dBean.setQuantity(1.0);
                                        dBean.setStartDate(null);
                                        dBean.setService(null);
                                        dBean.setServiceID("0");
                                        dataList.add(dBean);

                                        CustomProductAdapter adapter = new CustomProductAdapter(ProductActivity.this, ProductActivity.this, dataList);
                                        binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this, LinearLayoutManager.VERTICAL, false));
                                        binding.productRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                        binding.productRecyclerView.setAdapter(adapter);

                                    }else if (i > 1) {
                                        if (catListIds.size() > 1 && catListIds.get(i) != null && catListIds.get(i) != 0) {
                                            getCategoryDetailsMethod(proj_id, catListIds.get(i));
                                        }
                                    }*/
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });

                        }
                    }
                });

    }

    private void getCategoryDetailsMethod(String proj_id, int catID) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getResources().getString(R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        RequestCategoryBean requestCategoryBean = new RequestCategoryBean();
        requestCategoryBean.setProjectID(Integer.parseInt(proj_id));
        requestCategoryBean.setCategoryID(catID);
        if (farmId != null && farmId.length() > 0) {
            requestCategoryBean.setFarmID(Integer.parseInt(farmId));
        }

        apiService.getCategoryDetails(requestCategoryBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<CategoryDetailResponse>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        showError(getString(R.string.network_error));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getString(R.string.server_not_found));

                    }

                    @Override
                    public void onNext(Response<List<CategoryDetailResponse>> response) {
                        progressDialog.cancel();
                        dataList = response.body();
                        if (dataList != null && dataList.size() > 0) {
                            binding.productRecyclerView.setVisibility(View.VISIBLE);
                            binding.emptyView.setVisibility(View.GONE);
                            ProductAdapter adapter = new ProductAdapter(ProductActivity.this, ProductActivity.this, dataList);
                            binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.productRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            binding.productRecyclerView.setAdapter(adapter);


                        } else {
                            binding.productRecyclerView.setVisibility(View.GONE);
                            binding.emptyView.setVisibility(View.VISIBLE);
                            binding.title.setText(getResources().getString(R.string.Currentlytherearenoproductsavailable));
                        }
                    }
                });

    }

    @Override
    public void onClick(CategoryDetailResponse value) {
        if (value != null) {
            db.open();
            String quantityDb = "0";
            int qt = 0;
            Cursor selectedService = db.getCartByServiceId(value.getServiceID());
            final int selectedServiceCount = selectedService.getCount();
            if (selectedServiceCount > 0) {
                selectedService.moveToFirst();
                for (int i = 1; i <= selectedServiceCount; i++) {
                    quantityDb = selectedService.getString(selectedService.getColumnIndex(DBAdapter.quantity));
                    selectedService.moveToNext();
                }
                if (quantityDb != null && value.getQuantity() > 0) {
                    qt = Integer.parseInt(quantityDb) + value.getQuantity();
                }
                Log.v("vsdvsdvs", selectedServiceCount + "==" + quantityDb + "====" + value.getQuantity() + "=====" + qt);
                db.updateCartByServiceId(value.getServiceID(), "" + qt);
                db.close();

            } else {
                if (quantityDb != null) {
                    qt = Integer.parseInt(quantityDb) + value.getQuantity();
                }
                ContentValues values = new ContentValues();
                values.put(DBAdapter.ProductDescription, value.getProductDescription());
                values.put(DBAdapter.ImagePath, value.getImagePath());
                values.put(DBAdapter.BrandName, value.getBrandName());
                values.put(DBAdapter.ProductUnit, value.getProductUnit());
                values.put(DBAdapter.quantityUnit, value.getQuantityUnit());
                values.put(DBAdapter.serviceID, value.getServiceID());
                values.put(DBAdapter.startDate, value.getStartDate());
                values.put(DBAdapter.endDate, value.getEndDate());
                values.put(DBAdapter.price, value.getPrice());
                values.put(DBAdapter.service, value.getService());
                values.put(DBAdapter.quantity, qt);
                db.db.insert(DBAdapter.TABLE_CART, null, values);
                db.close();
            }


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