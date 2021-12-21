package com.weather.risk.mfi.myfarminfo.marketplace;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.ProductActivityBinding;
import com.weather.risk.mfi.myfarminfo.firebasenotification.NotificationCountSMS;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.payment.OrderHistoryActivity;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryModel;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerDetailsResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerListRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.RequestCategoryBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.ConnectionDetector;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_MainProfileActivity;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_NewProductActivity;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class NewProductActivity extends BaseActivity implements DataInterface {

    private ApiService apiService;
    ProductActivityBinding binding;
    List<CategoryDetailResponse> dataList = new ArrayList<CategoryDetailResponse>();

    ArrayList<HashMap<String, String>> Projects = new ArrayList<>();

    String balanceAmnt = "0";
    String farmerId = null, projectID = null;
    String projectName = null;
    String cropID = null;
    String cropName = null;
    ArrayList<String> str = new ArrayList<String>();
    ArrayList<String> strFarmId = new ArrayList<String>();
    DBAdapter db;
    ConnectionDetector connectionDetector;

    SearchableSpinner farmSpinner;
    String farmID = null;

    String state_id = null;
    Spinner cropSpinner;
    Spinner farmerSpinner;
    Spinner projectSpinner;
    LinearLayout cropLay, farmLay, farmerLay, projectLay;
    ArrayList<HashMap<String, String>> Crops = new ArrayList<>();

    String district_id = null;


    ArrayList<HashMap<String, String>> States = new ArrayList<>();
    ArrayList<HashMap<String, String>> Districts = new ArrayList<>();
    ArrayList<HashMap<String, String>> SubDistricts = new ArrayList<>();
    ArrayList<HashMap<String, String>> Villages = new ArrayList<>();

    String StateID = "", StateName = "", DistrictID = "", DistrictName = "", SubDistrictID = "",
            SubDistrictName = "", VillageID = null, VillageName = null;

    Spinner spin_state;
    Spinner spin_district;
    Spinner spin_subdistrict;
    Spinner spin_villageName;

    Dialog dialog = null;

    ArrayList<String> strFarmer = new ArrayList<String>();
    ArrayList<Integer> strFarmerId = new ArrayList<Integer>();
    ImageView filterFarmer;
    ImageView filterProject;
    int SelectedCategoryValue = 0;

    @Override
    protected void initView(Bundle bundle, ViewDataBinding viewDataBinding) {
        binding = (ProductActivityBinding) viewDataBinding;
        db = new DBAdapter(this);
        cropSpinner = binding.cropSpinner;
        cropLay = binding.cropLay;
        farmLay = binding.farmLay;
        farmerLay = binding.farmerLay;
        farmSpinner = binding.farmSpinner;
        farmerSpinner = binding.farmerSpinner;
        filterFarmer = binding.farmerRefresh;

        filterProject = binding.projectRefresh;
        projectLay = binding.projectLay;
        projectSpinner = binding.spinProject;

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        state_id = getIntent().getStringExtra("state_id");
        farmerId = getIntent().getStringExtra("farmerId");
        district_id = getIntent().getStringExtra("district_id");
        connectionDetector = new ConnectionDetector(this);
        binding.orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (farmerId != null) {
                    Intent in = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                    in.putExtra("farmerId", farmerId);
                    in.putExtra("farmID", farmID);
                    startActivity(in);
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Pleaseselectyourfarmfarmer", R.string.Pleaseselectyourfarmfarmer);
                }
            }
        });
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                Cursor selectedService = db.getAllCartData();
                final int selectedServiceCount = selectedService.getCount();
                db.close();
                Log.v("vsdvsdvs", selectedServiceCount + "");
                if (selectedServiceCount > 0) {

                    if (projectLay.getVisibility() == View.VISIBLE) {
                        if (projectID != null && projectID.length() > 0 && !projectID.equalsIgnoreCase("0")) {
                            Intent in = new Intent(getApplicationContext(), MyCartNew.class);
                            in.putExtra("balance", balanceAmnt);
                            in.putExtra("project_id", projectID);
                            in.putExtra("farmerId", farmerId);
                            in.putExtra("farmID", farmID);
                            startActivity(in);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Pleaseselectproject", R.string.Pleaseselectproject);
                        }
                    } else {
                        Intent in = new Intent(getApplicationContext(), MyCartNew.class);
                        in.putExtra("balance", balanceAmnt);
                        in.putExtra("project_id", projectID);
                        in.putExtra("farmerId", farmerId);
                        in.putExtra("farmID", farmID);
                        startActivity(in);
                    }

                } else {
                    getDynamicLanguageToast(getApplicationContext(), "Pleaseaddatleastoneiteminacart", R.string.Pleaseaddatleastoneiteminacart);
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
        farmSpinner.setTitle(getDynamicLanguageValue(getApplicationContext(), "SelectFarm", R.string.SelectFarm));
        farmSpinner.setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Ok", R.string.Ok));
        if (state_id != null && !state_id.equalsIgnoreCase("null") && state_id.length() > 0) {
            if (farmerId != null && !farmerId.equalsIgnoreCase("null") && farmerId.length() > 0) {
                cropLay.setVisibility(View.VISIBLE);
                projectLay.setVisibility(View.VISIBLE);
                farmLay.setVisibility(View.GONE);
                farmerLay.setVisibility(View.GONE);
                allbindCrop();
            } else {
                cropLay.setVisibility(View.GONE);
                projectLay.setVisibility(View.GONE);
                farmLay.setVisibility(View.VISIBLE);
                farmerLay.setVisibility(View.VISIBLE);
                getAllFarmName();
            }
        } else {
            cropLay.setVisibility(View.GONE);
            projectLay.setVisibility(View.GONE);
            farmLay.setVisibility(View.VISIBLE);
            farmerLay.setVisibility(View.VISIBLE);
            getAllFarmName();
        }
        String userTypeID = AppConstant.userTypeID;
        Log.v("cscascsc", userTypeID + "====" + AppConstant.user_id);
        if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") || userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
            binding.orderHistory.setVisibility(View.VISIBLE);
        } else {
            // binding.orderHistory.setVisibility(View.GONE);
            binding.orderHistory.setVisibility(View.VISIBLE);
        }

        filterFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmerListMethod();
            }
        });

        filterProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new getStateProjectRefresh("Project", AppManager.getInstance().ProjectListURL(AppConstant.user_id)).execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        if (db != null) {
            setProject();
        }

    }

    @Override
    protected int getActivityLayout() {
        return R.layout.product_activity;
    }

    private void getServiceMethod(final String proj_id) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        RequestCategoryBean requestCategoryBean = new RequestCategoryBean();
        if (farmID != null && farmID.length() > 0) {
            requestCategoryBean.setFarmID(Integer.parseInt(farmID));
        }
        if (cropID != null) {
            requestCategoryBean.setCropID(Integer.parseInt(cropID));
        }
        if (state_id != null) {
            requestCategoryBean.setStateID(Integer.parseInt(state_id));
        }
        if (district_id != null) {
            requestCategoryBean.setDistrictID(Integer.parseInt(district_id));
        }
        apiService.getServiceMethod(requestCategoryBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<ServiceResponseModel>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "no_data", R.string.no_data));
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.productRecyclerView.setVisibility(View.GONE);
                        binding.title.setText(getDynamicLanguageValue(getApplicationContext(), "Currentlythereisnoserviceavailable", R.string.Currentlythereisnoserviceavailable));


                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.productRecyclerView.setVisibility(View.GONE);
                        binding.title.setText(getDynamicLanguageValue(getApplicationContext(), "Currentlythereisnoserviceavailable", R.string.Currentlythereisnoserviceavailable));


                    }

                    @Override
                    public void onNext(Response<List<ServiceResponseModel>> response) {
                        progressDialog.cancel();
                        List<ServiceResponseModel> responsesData = response.body();
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.productRecyclerView.setVisibility(View.GONE);
                        if (responsesData != null && responsesData.size() > 0) {
                            final ArrayList<String> serviceList = new ArrayList<>();
                            final ArrayList<Integer> serviceListIds = new ArrayList<Integer>();
                            /*  catList.add("select category");
                             *//*  catList.add("Other category");
                            catListIds.add(0);*//*
                            catListIds.add(0);*/
                            for (int i = 0; i < responsesData.size(); i++) {
                                if (responsesData.get(i).getCategoryMasterName() != null) {
                                    serviceList.add(responsesData.get(i).getCategoryMasterName());
                                    serviceListIds.add(responsesData.get(i).getCategoryMasterID());
                                }
                            }
                            if (serviceList.size() > 0) {
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_spinner_item, serviceList);
                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // attaching data adapter to spinner
                                binding.serviceSpinner.setAdapter(dataAdapter);
                                binding.serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if (i >= 0) {
                                            if (serviceListIds.size() > 0 && serviceListIds.get(i) != null && serviceListIds.get(i) != 0) {
                                                getCategoryMethod(proj_id, serviceListIds.get(i));
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

                            } else {
                                binding.productRecyclerView.setVisibility(View.GONE);
                                binding.title.setText(getResources().getString(R.string.Currentlythereisnoserviceavailable));

                            }
                        }
                    }
                });

    }


    private void getCategoryMethod(final String proj_id, int idService) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        RequestCategoryBean requestCategoryBean = new RequestCategoryBean();
        if (farmID != null && farmID.length() > 0) {
            requestCategoryBean.setFarmID(Integer.parseInt(farmID));
        }
        if (proj_id != null && proj_id.length() > 0) {
            requestCategoryBean.setProjectID(Integer.parseInt(proj_id));
        }
        if (cropID != null) {
            requestCategoryBean.setCropID(Integer.parseInt(cropID));
        }
        if (state_id != null) {
            requestCategoryBean.setStateID(Integer.parseInt(state_id));
        }
        if (district_id != null) {
            requestCategoryBean.setDistrictID(Integer.parseInt(district_id));
        }
        requestCategoryBean.setCategoryMasterID(idService);
        apiService.getCategory(requestCategoryBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<CategoryModel>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "no_data", R.string.no_data));
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.productRecyclerView.setVisibility(View.GONE);
                        binding.title.setText(getDynamicLanguageValue(getApplicationContext(), "Currentlythereisnocategory", R.string.Currentlythereisnocategory));


                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.productRecyclerView.setVisibility(View.GONE);
                        binding.title.setText(getDynamicLanguageValue(getApplicationContext(), "Currentlythereisnocategory", R.string.Currentlythereisnocategory));


                    }

                    @Override
                    public void onNext(Response<List<CategoryModel>> response) {
                        progressDialog.cancel();
                        List<CategoryModel> responsesData = response.body();
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.productRecyclerView.setVisibility(View.GONE);
                        if (responsesData != null && responsesData.size() > 0) {
                            ArrayList<String> catList = new ArrayList<>();
                            final ArrayList<Integer> catListIds = new ArrayList<Integer>();
                            /*  catList.add("select category");
                             *//*  catList.add("Other category");
                            catListIds.add(0);*//*
                            catListIds.add(0);*/
                            for (int i = 0; i < responsesData.size(); i++) {
                                if (responsesData.get(i).getCategoryName() != null) {
                                    catList.add(responsesData.get(i).getCategoryName());
                                    catListIds.add(responsesData.get(i).getCategoryID());
                                }
                            }
                            if (catList.size() > 0) {
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_spinner_item, catList);
                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // attaching data adapter to spinner
                                binding.categorySpinner.setAdapter(dataAdapter);
                                binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if (i >= 0) {
                                            if (catListIds.size() > 0 && catListIds.get(i) != null && catListIds.get(i) != 0) {
                                                getCategoryDetailsMethod(proj_id, catListIds.get(i));
                                                SelectedCategoryValue = catListIds.get(i);// 1 means Yield Inssurance
                                            } else {
                                                SelectedCategoryValue = 0;
                                            }
                                        } else {
                                            SelectedCategoryValue = 0;
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

                            } else {
                                binding.productRecyclerView.setVisibility(View.GONE);
                                binding.title.setText(getResources().getString(R.string.Currentlythereisnocategory));

                            }
                        }
                    }
                });
    }

    private void getCategoryDetailsMethod(String proj_id, int catID) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
        progressDialog.show(); // show progress dialog
        RequestCategoryBean requestCategoryBean = new RequestCategoryBean();
        requestCategoryBean.setCategoryID(catID);
        if (proj_id != null && proj_id.length() > 0) {
            requestCategoryBean.setProjectID(Integer.parseInt(proj_id));
        }
        if (farmID != null && farmID.length() > 0) {
            requestCategoryBean.setFarmID(Integer.parseInt(farmID));
        }
        if (cropID != null) {
            requestCategoryBean.setCropID(Integer.parseInt(cropID));
        }
        if (state_id != null) {
            requestCategoryBean.setStateID(Integer.parseInt(state_id));
        }
        if (district_id != null) {
            requestCategoryBean.setDistrictID(Integer.parseInt(district_id));
        }
        apiService.getCategoryDetails(requestCategoryBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<CategoryDetailResponse>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "no_data", R.string.no_data));
                        binding.productRecyclerView.setVisibility(View.GONE);
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.title.setText(getDynamicLanguageValue(getApplicationContext(), "Currentlytherearenoproducts", R.string.Currentlytherearenoproducts));


                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));
                        binding.productRecyclerView.setVisibility(View.GONE);
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.title.setText(getDynamicLanguageValue(getApplicationContext(), "Currentlytherearenoproducts", R.string.Currentlytherearenoproducts));


                    }

                    @Override
                    public void onNext(Response<List<CategoryDetailResponse>> response) {
                        progressDialog.cancel();
                        dataList = response.body();
                        if (dataList != null && dataList.size() > 0) {
                            binding.productRecyclerView.setVisibility(View.VISIBLE);
                            binding.emptyView.setVisibility(View.GONE);
                            ProductAdapter adapter = new ProductAdapter(NewProductActivity.this, NewProductActivity.this, dataList);
                            binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(NewProductActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.productRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            binding.productRecyclerView.setAdapter(adapter);
                        } else {
                            binding.productRecyclerView.setVisibility(View.GONE);
                            binding.emptyView.setVisibility(View.VISIBLE);
                            binding.title.setText(getDynamicLanguageValue(getApplicationContext(), "Currentlytherearenoproducts", R.string.Currentlytherearenoproducts));
                        }
                    }
                });

    }

    //    @Override
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

    public void getAllFarmName() {
        db = new DBAdapter(this);
        System.out.println("getAllFarmCalled");
        str = new ArrayList<String>();
        strFarmId = new ArrayList<String>();
        if ((AppConstant.userTypeID != null && !AppConstant.userTypeID.equalsIgnoreCase("5"))) {
            str.add("Select farm");
            strFarmId.add("0");
        }
        Log.v("userTypeID", AppConstant.userTypeID + "");
        db.open();
        Cursor c = db.getallFarmName(AppConstant.user_id);
        if (c.moveToFirst()) {
            do {
                str.add(c.getString(0).toString() + " - " + c.getString(1).toString());
                strFarmId.add(c.getString(1).toString());

            } while (c.moveToNext());
        }
        db.close();
        ArrayAdapter<String> chooseYourFarmAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, str);
        farmSpinner.setAdapter(chooseYourFarmAdapter);
        farmSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        Log.v("Adsdsds", AppConstant.userTypeID);
                        if ((AppConstant.userTypeID != null && !AppConstant.userTypeID.equalsIgnoreCase("5"))) {
                            if (position > 0) {
                                farmerLay.setVisibility(View.GONE);
                                farmerId = null;

                                if (connectionDetector.isConnectingToInternet()) {
                                    String farmName = str.get(position);
                                    String farmIDd = strFarmId.get(position);
                                    Log.v("farm_id_selected", "" + farmIDd);
//                                new getca(farmID).execute();
                                    if (farmIDd != null && !farmIDd.equalsIgnoreCase("0")) {

                                        new FarmdetailsAsynctask(farmIDd).execute();
                                    } else {
                                        farmerLay.setVisibility(View.VISIBLE);
                                        farmerId = null;
                                        farmID = null;
                                    }

                                } else {
                                    noInternetMethoddd();
                                }

                            } else {
                                farmerLay.setVisibility(View.VISIBLE);
                                farmerId = null;
                                farmID = null;
                            }
                        } else {
                            if (connectionDetector.isConnectingToInternet()) {
                                farmerLay.setVisibility(View.GONE);
                                farmerId = null;
                                String farmName = str.get(position);
                                String farmIDd = strFarmId.get(position);
                                Log.v("farm_id_selected", "" + farmID);
//                                new getca(farmID).execute();
                                if (farmIDd != null && !farmIDd.equalsIgnoreCase("0")) {

                                    new FarmdetailsAsynctask(farmIDd).execute();
                                } else {
                                    farmerLay.setVisibility(View.VISIBLE);
                                    farmerId = null;
                                    farmID = null;
                                }

                            } else {
                                noInternetMethoddd();
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        farmerLay.setVisibility(View.VISIBLE);
                        farmerId = null;
                        farmID = null;
                    }
                }
        );


    }

    private void noInternetMethoddd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewProductActivity.this);
        builder.setTitle(getDynamicLanguageValue(getApplicationContext(), "InternetError", R.string.InternetError)).
                setMessage(getDynamicLanguageValue(getApplicationContext(), "Doyouwantrefresh", R.string.Doyouwantrefresh)).
                setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Yes", R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Intent gps = new Intent(
                                Settings.ACTION_WIFI_SETTINGS);
                        startActivity(gps);
//                        if (USERID != null) {
//                            subDistrictLay.setVisibility(View.GONE);
//                            loadDistData(USERID);
//                        } else {
//                            Toast.makeText(FarmVisitAcitivity.this, getResources().getString(R.string.Useriddoesnotfound), Toast.LENGTH_SHORT).show();
//                        }
                    }
                }).
                setNegativeButton(getDynamicLanguageValue(getApplicationContext(), "No", R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private class FarmdetailsAsynctask extends AsyncTask<Void, Void, String> {
        String FarmID = "";

        public FarmdetailsAsynctask(String farmid) {
            FarmID = farmid;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewProductActivity.this);
            progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {
                sendRequest = AppManager.getInstance().getFarmData + AppConstant.user_id + "/" + FarmID;
                String response = AppManager.getInstance().httpRequestGetMethod(sendRequest);
                return response;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return sendRequest;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                if (response != null && response.contains("FarmID")) {
                    progressDialog.dismiss();
                    Log.v("sacascascas", response);
                    if (response != null && response.contains("armerID")) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            farmerId = obj.getString("FarmerID");
                            projectID = obj.getString("ProjectID");
                            farmID = obj.getString("FarmID");
                            if (obj.has("CropInfo")) {
                                JSONArray jsonArray = obj.getJSONArray("CropInfo");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    if (jsonArray.getJSONObject(0).has("CropID")) {
                                        cropID = jsonArray.getJSONObject(0).getString("CropID");
                                    }
                                }
                            }
                            Log.v("csccssss", farmerId + "====" + cropID);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    cropLay.setVisibility(View.GONE);
                    projectLay.setVisibility(View.GONE);
                    cropID = null;
                    cropSpinner.setAdapter(null);
                    binding.serviceSpinner.setAdapter(null);
                    binding.categorySpinner.setAdapter(null);
                    binding.productRecyclerView.setVisibility(View.GONE);

                    getServiceMethod(projectID);

                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void hideKeyboard() {
        super.hideKeyboard();
    }

    public void allbindCrop() {
        db.open();
        Crops = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        String sql = "select * from crop_variety where state_id='" + state_id + "' group by cropid order by crop";
        hasmap = db.getDynamicTableValue(sql);
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("cropId", "0");
        hash.put("crop", "Select Crop");
        Crops.add(hash);
        list.add("Select Crop");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("cropId", hasmap.get(i - 1).get("cropId"));
                list.add(hasmap.get(i - 1).get("crop"));
                hashMap.put("crop", hasmap.get(i - 1).get("crop"));
                Crops.add(hashMap);
            }
        }
        setCropsBind(cropSpinner, list, "crop2");

    }

    public void setCropsBind(Spinner spincrop, ArrayList<String> list, final String flag) {
        ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
//        ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cropStringArray); //selected item will look like a spinner set from XML
//        cropArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spincrop.setAdapter(cropArrayAdapter);
        spincrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    switch (flag) {
                        case "crop2":
                            if (position > 0) {
                                cropID = Crops.get(position).get("cropId");
                                cropName = Crops.get(position).get("crop");


                                binding.serviceSpinner.setAdapter(null);
                                binding.categorySpinner.setAdapter(null);
                                binding.productRecyclerView.setVisibility(View.GONE);

                                getServiceMethod(projectID);

                            } else {
                                cropID = "0";
                                cropName = "";


                                binding.serviceSpinner.setAdapter(null);
                                binding.categorySpinner.setAdapter(null);
                                binding.productRecyclerView.setVisibility(View.GONE);


                            }
                            break;

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void farmerListMethod() {
        VillageID = null;
        dialog = new Dialog(this);
        //  final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.5f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.search_farmer_popup);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ImageView crossBTN = (ImageView) dialog.findViewById(R.id.crossbtn);
        crossBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.cancel();
                }
            }
        });
        Button submitBTN = (Button) dialog.findViewById(R.id.submit);
        spin_state = (Spinner) dialog.findViewById(R.id.spinState);
        spin_district = (Spinner) dialog.findViewById(R.id.spinDistrict);
        spin_subdistrict = (Spinner) dialog.findViewById(R.id.spinSubdistrict);
        spin_villageName = (Spinner) dialog.findViewById(R.id.spinVillageName);
        final EditText edtAadharNo = (EditText) dialog.findViewById(R.id.edtAadharNo);
        final EditText edtMobileNo = (EditText) dialog.findViewById(R.id.edtMobileNo);
        if (spin_state != null && spin_district != null && spin_subdistrict != null && spin_villageName != null) {
            setStateBind();
        }
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adharN = edtAadharNo.getText().toString().trim();
                String phn = edtMobileNo.getText().toString().trim();
                if (adharN.length() == 12 || phn.length() == 10 || VillageID != null) {
                    getFarmerListMethod(phn, adharN);
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "PleaseenterthevalidAadhar", R.string.PleaseenterthevalidAadhar);
                }
            }
        });
        dialog.show();
    }


    public void setStateBind() {
        db.open();
        States = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID!='' and StateName!='' group by StateID order by StateName");
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("StateID", "0");
        hash.put("StateName", "Select State");
        States.add(hash);
        list.add("Select State");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("StateID", hasmap.get(i - 1).get("StateID"));
                list.add(hasmap.get(i - 1).get("StateName"));
                hashMap.put("StateName", hasmap.get(i - 1).get("StateName"));
                States.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_state.setAdapter(stateListAdapter);
        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && States.size() > 0) {
                    try {
                        //State
                        StateID = States.get(position).get("StateID");
                        StateName = States.get(position).get("StateName");
//                        setCropsBind();
//                        allbindCrop();
                        if (spin_district != null && spin_subdistrict != null && spin_villageName != null) {
                            setDitrictBind(StateID);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    StateID = "0";
                    StateName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        db.close();
    }


    public void setDitrictBind(String State_id) {
        db.open();
        Districts = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("select ID,StateID,Upper(StateName) as StateName,DistrictID,Upper(DistrictName) as DistrictName from MstStateDistrict where StateID='" + State_id + "' and DistrictID!='' and DistrictName!='' group by DistrictID order by DistrictName");
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("DistrictID", "0");
        hash.put("DistrictName", "Select District");
        Districts.add(hash);
        list.add("Select District");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("DistrictID", hasmap.get(i - 1).get("DistrictID"));
                list.add(hasmap.get(i - 1).get("DistrictName"));
                hashMap.put("DistrictName", hasmap.get(i - 1).get("DistrictName"));
                Districts.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        spin_district.setAdapter(stateListAdapter);
        spin_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Districts.size() > 0) {
                    DistrictID = Districts.get(position).get("DistrictID");
                    DistrictName = Districts.get(position).get("DistrictName");
                    if (spin_subdistrict != null && spin_villageName != null) {
                        getAPIURL(3, DistrictID, DistrictName);
                    }
                } else {
                    DistrictID = "0";
                    DistrictName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        db.close();
    }

    public void getAPIURL(int flag, String ID, String Name) {
        String URL = "";
        switch (flag) {
//            case 1://State
//                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetAllStates/2";
//                break;
//            case 2://District
//                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetStateDistrict/" + ID;
//                break;
            case 3://Sub District
                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetDistrictsubdistrict/" + ID;
                break;
            case 4://Village
                URL = "https://myfarminfo.com/yfirest.svc/Clients/GetsubdistrictVillage/" + ID + "/" + Name;
                break;
        }
        new getStateDistVillage(flag, URL).execute();
    }

    private class getStateDistVillage extends AsyncTask<Void, Void, String> {

        int flag = 0;
        String URL = "";
        TransparentProgressDialog progressDialog;

        public getStateDistVillage(int flags, String URLs) {
            flag = flags;
            URL = URLs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String sms = "";
            switch (flag) {
//                case 1://State
//                    progressDialog.setMessage(getResources().getString(R.string.LoadingStates));
//                    break;
//                case 2://District
//                    progressDialog.setMessage(getResources().getString(R.string.LoadingDistricts));
//                    break;
                case 3://Sub District
                    sms = getDynamicLanguageValue(getApplicationContext(), "LoadingSubDistricts", R.string.LoadingSubDistricts);
                    break;
                case 4://Village
                    sms = getDynamicLanguageValue(getApplicationContext(), "LoadingVillages", R.string.LoadingVillages);
                    break;
            }
            progressDialog = new TransparentProgressDialog(
                    NewProductActivity.this, sms);
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                return AppManager.getInstance().RemoveStringUnwanted(response);

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
            if (result.equalsIgnoreCase("No Data") || result.equalsIgnoreCase("Could not connect to server")) {
                switch (flag) {
//                    case 1://State
//                        States = null;
//                        AppConstant.state = "";
//                        AppConstant.stateID = "";
//                        SetSpinner(flag, stateList, States);
//                        break;
//                    case 2://District
//                        Districts = null;
//                        DistrictName = null;
//                        DistrictID = null;
//                        SetSpinner(flag, DistrictName_spin, Districts);
//                        //Sub District
//                        SubDistricts = null;
//                        SubDistrictName = null;
//                        SubDistrictID = null;
//                        SetSpinner(flag, SubDistrictName_spin, SubDistricts);
//                        //Village
//                        Villages = null;
//                        VillageName = null;
//                        VillageID = null;
//                        SetSpinner(flag, VillageName_spin, Villages);
//                        break;
                    case 3://Sub District
                        SubDistricts = null;
                        SubDistrictName = null;
                        SubDistrictID = null;
                        if (spin_subdistrict != null && spin_villageName != null) {
                            SetSpinner(flag, spin_subdistrict, SubDistricts);
                        }
                        //Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        if (spin_subdistrict != null && spin_villageName != null) {
                            SetSpinner(flag, spin_villageName, Villages);
                        }
                        break;
                    case 4://Village
                        Villages = null;
                        VillageName = null;
                        VillageID = null;
                        if (spin_villageName != null) {
                            SetSpinner(flag, spin_villageName, Villages);
                        }
                        break;
                }
//        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Nodataavailable), Toast.LENGTH_LONG).show();
            } else {
                try {
                    //stateList,DistrictName_spin,SubDistrictName_spin,VillageName_spin
                    switch (flag) {
//                        case 1://State
//                            JSONObject json = new JSONObject(result.toString());
//                            JSONArray jsonArray_State = json.getJSONArray("DT");
//                            States = getStateDistrictValues(flag, jsonArray_State.toString());
//                            SetSpinner(flag, stateList, States);
//                            break;
//                        case 2://District
//                            JSONArray jsonArray_Districts = new JSONArray(result.toString());
//                            Districts = getStateDistrictValues(flag, jsonArray_Districts.toString());
//                            SetSpinner(flag, DistrictName_spin, Districts);
//                            break;
                        case 3://Sub District
                            JSONArray jsonArray_SubDistricts = new JSONArray(result.toString());
                            SubDistricts = getStateDistrictValues(flag, jsonArray_SubDistricts.toString());
                            if (spin_subdistrict != null && spin_villageName != null) {
                                SetSpinner(flag, spin_subdistrict, SubDistricts);
                            }
                            break;
                        case 4://Village
                            JSONArray jsonArray_Villages = new JSONArray(result.toString());
                            Villages = getStateDistrictValues(flag, jsonArray_Villages.toString());
                            if (spin_villageName != null) {
                                SetSpinner(flag, spin_villageName, Villages);
                            }
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    getDynamicLanguageToast(getApplicationContext(), "Couldnotconnect", R.string.Couldnotconnect);
                }
            }
            progressDialog.dismiss();

        }
    }

    public ArrayList<HashMap<String, String>> getStateDistrictValues(int flag, String JsonValues) {
        ArrayList<HashMap<String, String>> values = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(JsonValues);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (flag) {
            case 1://State
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String StateID = "", StateName = "";
                        StateID = obj.getString("StateID");
                        StateName = obj.getString("StateName");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("StateID", StateID);
                        setval.put("StateName", StateName);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 2://District
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String DistrictID = "", District = "";
                        DistrictID = obj.getString("DistrictID");
                        District = obj.getString("District");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("DistrictID", DistrictID);
                        setval.put("District", District);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 3://Sub District
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String Sub_district = "", Sub_district_Pos = "";
                        Sub_district = obj.getString("Sub_district");
                        Sub_district_Pos = String.valueOf(i);
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("Sub_district", Sub_district);
                        setval.put("Sub_district_Pos", Sub_district_Pos);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case 4://Village
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String Village_Final = "", Village_ID = "";
                        Village_Final = obj.getString("Village_Final");
                        Village_ID = obj.getString("Village_ID");
                        HashMap<String, String> setval = new HashMap<>();
                        setval.put("Village_Final", Village_Final);
                        setval.put("Village_ID", Village_ID);
                        values.add(setval);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
        }
        return values;
    }


    public void SetSpinner(final int flag, Spinner spinner, ArrayList<HashMap<String, String>> value) {
        final ArrayList<String> getValue = new ArrayList();
        final ArrayList<String> getIDs = new ArrayList();
        if (value != null) {
            switch (flag) {
                case 1://State
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("StateName"));
                        getIDs.add(value.get(i).get("StateID"));
                    }
                    break;
                case 2://District
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("District"));
                        getIDs.add(value.get(i).get("DistrictID"));
                    }
                    break;
                case 3://Sub District
                    getValue.add("Select Sub District");
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Sub_district"));
                        getIDs.add(value.get(i).get("Sub_district_Pos"));
                    }
                    break;
                case 4://Village
                    getValue.add("Select Village");
                    getIDs.add("0");
                    for (int i = 0; i < value.size(); i++) {
                        getValue.add(value.get(i).get("Village_Final"));
                        getIDs.add(value.get(i).get("Village_ID"));
                    }
                    break;
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, getValue);
        spinner.setAdapter(stateListAdapter);
        db.close();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position > 0) {
                try {
                    switch (flag) {
                        case 3://Sub District
                            if (getValue.size() > 0) {
                                SubDistrictID = getIDs.get(position);
                                SubDistrictName = getValue.get(position);
                            } else {
                                SubDistrictID = null;
                                SubDistrictName = null;
                            }
                            if (spin_villageName != null) {
                                getAPIURL(4, DistrictID, SubDistrictName);
                            }
                            break;
                        case 4://Village
                            if (getValue.size() > 0) {
                                VillageID = getIDs.get(position);
                                VillageName = getValue.get(position);
                            } else {
                                VillageID = null;
                                VillageName = null;
                            }
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getFarmerListMethod(String phNo, String aadharN) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        FarmerListRequest request = new FarmerListRequest();
        if (phNo != null) {
            request.setPhoneNumber(phNo);
        }
        if (aadharN != null) {
            request.setAdhaar(aadharN);
        }
        if (VillageID != null && VillageID.length() > 0) {
            request.setVillageID(Integer.valueOf(VillageID));
        }
        apiService.getFarmerList(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<List<FarmerResponse>>>(getCompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());
                        showError(getDynamicLanguageValue(getApplicationContext(), "no_data", R.string.no_data));

                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        showError(getDynamicLanguageValue(getApplicationContext(), "server_not_found", R.string.server_not_found));

                    }

                    @Override
                    public void onNext(Response<List<FarmerResponse>> response) {
                        progressDialog.cancel();
                        List<FarmerResponse> responsesData = response.body();
                        if (responsesData != null && responsesData.size() > 0) {
                            if (dialog != null) {
                                dialog.cancel();
                            }

                            strFarmer = new ArrayList<String>();
                            strFarmerId = new ArrayList<Integer>();
                            if (responsesData.size() > 1) {
                                strFarmer.add("select farmer");
                                strFarmerId.add(0);
                            }

                            for (int i = 0; i < responsesData.size(); i++) {
                                if (responsesData.get(i).getFarmerPersonelID() != null && responsesData.get(i).getFarmerPersonelID() > 0) {
                                    strFarmer.add(responsesData.get(i).getFarmerName());
                                    strFarmerId.add(responsesData.get(i).getFarmerPersonelID());
                                }
                            }

                            farmerId = null;
                            if (strFarmer.size() > 0) {
                                bindFarmerSpiner();
                            }

                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Nofarmerisfound", R.string.Nofarmerisfound);
                        }
                    }
                });
    }


    public void bindFarmerSpiner() {

        ArrayAdapter<String> chooseYourFarmAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, strFarmer);
        farmerSpinner.setAdapter(chooseYourFarmAdapter);
        farmerSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        Log.v("Adsdsds", AppConstant.userTypeID);
                        if (position >= 0 && strFarmerId.get(position) > 0) {

                            cropSpinner.setAdapter(null);
                            binding.serviceSpinner.setAdapter(null);
                            binding.categorySpinner.setAdapter(null);


                            binding.productRecyclerView.setVisibility(View.GONE);

                            farmerId = "" + strFarmerId.get(position);
                            getFarmerDetailsMethod(farmerId);
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        farmerId = null;
                    }
                }
        );


    }


    private void getFarmerDetailsMethod(final String fId) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show(); // show progress dialog
        FarmerListRequest request = new FarmerListRequest();
        // request.setProjectID(pId);
        request.setFarmerPersonelID(Integer.valueOf(fId));
        apiService.getFarmerDetails(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<FarmerDetailsResponse>>(new CompositeDisposable()) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        Log.v("wvqevqevqe", e.getMessage() + " " + e.toString());


                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();


                    }

                    @Override
                    public void onNext(Response<FarmerDetailsResponse> response) {
                        progressDialog.cancel();
                        FarmerDetailsResponse responsesData = response.body();
                        if (responsesData != null && responsesData.getFarmerpersonalData() != null && responsesData.getFarmerpersonalData().size() > 0) {

                            state_id = responsesData.getFarmerpersonalData().get(0).getStateID();
                            district_id = responsesData.getFarmerpersonalData().get(0).getDistrictID();

                            cropLay.setVisibility(View.VISIBLE);
                            projectLay.setVisibility(View.VISIBLE);
                            allbindCrop();

                        }


                    }
                });

    }


    private class getStateProjectRefresh extends AsyncTask<Void, Void, String> {

        String flag = "";
        String URL = "";
        TransparentProgressDialog progressDialog;

        public getStateProjectRefresh(String flags, String URLs) {
            flag = flags;
            URL = URLs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String sms = "";
            switch (flag) {
                case "Project"://State
                    sms = getDynamicLanguageValue(getApplicationContext(), "LoadingProject", R.string.LoadingProject);
                    break;
                case "StateDistrict"://District
                    sms = getDynamicLanguageValue(getApplicationContext(), "Loadingstate", R.string.Loadingstate);
                    break;
                case "Crops"://District
                    sms = getDynamicLanguageValue(getApplicationContext(), "LoadingCrops", R.string.LoadingCrops);
                    break;

            }
            progressDialog = new TransparentProgressDialog(NewProductActivity.this, sms);
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                return "[" + response + "]";

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            if (response != null) {
                switch (flag) {
                    case "Project":
                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        db.open();
                        db.getSQLiteDatabase().execSQL("DROP TABLE IF EXISTS " + db.TABLE_Projectlist);
                        db.getSQLiteDatabase().execSQL(db.CREATE_TABLE_Projectlist);
                        io.requery.android.database.sqlite.SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
                        SqliteDB.beginTransaction();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                                String query = "INSERT INTO " + DBAdapter.TABLE_Projectlist + "(ProjectID,ProjectName) VALUES ('" + obj.get("ID").toString() + "','" + obj.get("Name").toString() + "')";
                                db.getSQLiteDatabase().execSQL(query);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            getDynamicLanguageToast(getApplicationContext(), "FormattingError", R.string.FormattingError);
                        }
                        SqliteDB.setTransactionSuccessful();
                        SqliteDB.endTransaction();
                        db.getClass();
                        setProject();
                        break;

                }

            } else
                getDynamicLanguageToast(getApplicationContext(), "Nodataavailable", R.string.Nodataavailable);
            progressDialog.dismiss();
        }
    }

    public void setProject() {
        db.open();
        Projects = new ArrayList<>();
        ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
        hasmap = db.getDynamicTableValue("Select * from " + db.TABLE_Projectlist + " where ProjectName !='' and  ProjectName !='null' order by ProjectName");
        ArrayList<String> list = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        hash.put("ProjectID", "0");
        hash.put("ProjectName", "Select Project");
        Projects.add(hash);
        list.add("Select Project");
        if (hasmap.size() > 0) {
            for (int i = 1; i <= hasmap.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ProjectID", hasmap.get(i - 1).get("ProjectID"));
                list.add(hasmap.get(i - 1).get("ProjectName"));
                hashMap.put("ProjectName", hasmap.get(i - 1).get("ProjectName"));
                Projects.add(hashMap);
            }
        }
        ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        projectSpinner.setAdapter(stateListAdapter);
        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0 && Projects.size() > 0) {
                    projectID = Projects.get(position).get("ProjectID");
                    projectName = Projects.get(position).get("ProjectName");
                } else {
                    projectID = "0";
                    projectName = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguages();
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, binding.txtFarmRegistration, 2);
        setFontsStyleTxt(this, binding.orderHistory, 5);
        setFontsStyleTxt(this, binding.txtSelectFarm, 6);
        setFontsStyleTxt(this, binding.txtSelectfarmer, 6);
        setFontsStyleTxt(this, binding.txtServiceType, 6);
        setFontsStyleTxt(this, binding.txtSelectCategory, 6);
        setFontsStyle(this, binding.homeBtn);
        setFontsStyle(this, binding.cartBtn);

        setDynamicLanguage(this, binding.txtFarmRegistration, "ProductList", R.string.ProductList);
        setDynamicLanguage(this, binding.orderHistory, "order_history", R.string.order_history);
        setDynamicLanguage(this, binding.txtSelectFarm, "SelectFarm_market", R.string.SelectFarm_market);
        setDynamicLanguage(this, binding.txtSelectfarmer, "Selectfarmer", R.string.Selectfarmer);
        setDynamicLanguage(this, binding.txtServiceType, "ServiceType", R.string.ServiceType);
        setDynamicLanguage(this, binding.txtSelectCategory, "SelectCategory", R.string.SelectCategory);
        setDynamicLanguage(this, binding.homeBtn, "HomePage", R.string.HomePage);
        setDynamicLanguage(this, binding.cartBtn, "MyCart", R.string.MyCart);

    }

}
