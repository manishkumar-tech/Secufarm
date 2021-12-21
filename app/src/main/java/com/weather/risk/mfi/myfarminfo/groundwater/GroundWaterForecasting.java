package com.weather.risk.mfi.myfarminfo.groundwater;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CattleAdvisory;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDecimal2;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.GroundwaterforecastingBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.interfaces.GroundWaterForecastingCallbacks;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.policyregistration.PolicyList;
import com.weather.risk.mfi.myfarminfo.policyregistration.ProductNewAdapter;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.RequestCategoryBean;
import com.weather.risk.mfi.myfarminfo.viewmodels.GroundWaterForecastingVM;
import com.weather.risk.mfi.myfarminfo.viewmodels.GroundWaterForecastingVMFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class GroundWaterForecasting extends AppCompatActivity implements GroundWaterForecastingCallbacks {

    GroundwaterforecastingBinding binding;
    private CompositeDisposable compositeDisposable;
    List<GroundWaterResponse> list = new ArrayList<>();
    String lat = "", lon = "", season = "Kharif";
    public static JSONArray GroundwaterContours = new JSONArray();
    public static List<Double> listdivergance = new ArrayList<>();
    public static List<LatLng> listLatLong = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.groundwaterforecasting);
        binding.setViewModel(ViewModelProviders.of(this, new GroundWaterForecastingVMFactory(this)).get(GroundWaterForecastingVM.class));
        compositeDisposable = new CompositeDisposable();
        list = new ArrayList<>();
        GroundwaterContours = new JSONArray();
        listdivergance = new ArrayList<>();
        listLatLong = new ArrayList<>();

//        int Year = Calendar.getInstance().get(Calendar.YEAR);
//        String GroundWaterleveltill = getResources().getString(R.string.GroundWaterleveltill) + " " + Year;
//        binding.txtGroundWaterleveltill.setText(GroundWaterleveltill);
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                lat = bundle.getString("lat");
                lon = bundle.getString("lon");
            }

//            if (lat == null || lon == null || lat.length() == 0 || lon.length() == 0) {
//                lat = String.valueOf(LatLonCellID.currentLat);
//                lon = String.valueOf(LatLonCellID.currentLon);
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_GeotagFarmLocation) {
                    try {
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            lat = bundle.getString("lat");
                            lon = bundle.getString("lon");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (checkedId == R.id.rb_CurrentLocation) {
                    try {
                        lat = String.valueOf(LatLonCellID.currentLat);
                        lon = String.valueOf(LatLonCellID.currentLon);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                getGroundWater();
            }
        });
        binding.horzGrid.setVisibility(View.GONE);
        binding.txtGroundWaterleveltill.setVisibility(View.GONE);
        binding.txtDataisnotfound.setVisibility(View.GONE);
        binding.recyclerviewGround.setVisibility(View.GONE);
        binding.txtTotalTasks.setVisibility(View.GONE);
        getGroundWater();
    }

    public void getGroundWater() {
        try {
            // display a progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false); // set cancelable to false
            progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Pleasewait", R.string.Pleasewait)); // set message
            GroundWaterBeans request = new GroundWaterBeans();
            int Year = Calendar.getInstance().get(Calendar.YEAR);
            request.setLat(Double.parseDouble(lat));
            request.setLon(Double.parseDouble(lon));
            request.setYear(Year);
            request.setSeason(season);
            progressDialog.show(); // show progress dialog

            AppController.getInstance().getApiServiceTest().getGroundWater(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseObserver<Response<List<GroundWaterResponse>>>(compositeDisposable) {
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

                        @Override
                        public void onNext(Response<List<GroundWaterResponse>> response) {
                            progressDialog.cancel();
                            list = response.body();
                            try {
                                if (list != null && list.size() > 0) {
                                    binding.txtTotalTasks.setVisibility(View.VISIBLE);
                                    binding.recyclerviewGround.setVisibility(View.VISIBLE);
                                    binding.horzGrid.setVisibility(View.VISIBLE);
                                    binding.txtGroundWaterleveltill.setVisibility(View.VISIBLE);
                                    binding.txtDataisnotfound.setVisibility(View.GONE);
                                    binding.recyclerviewGround.setHasFixedSize(true);
                                    GroundWaterForecastingAdapter adapter = new GroundWaterForecastingAdapter(GroundWaterForecasting.this, list);
                                    binding.recyclerviewGround.setLayoutManager(new LinearLayoutManager(GroundWaterForecasting.this, LinearLayoutManager.VERTICAL, false));
                                    binding.recyclerviewGround.setItemAnimator(new DefaultItemAnimator());
                                    binding.recyclerviewGround.setAdapter(adapter);
                                    setContour();
                                } else {
                                    binding.horzGrid.setVisibility(View.GONE);
                                    binding.txtGroundWaterleveltill.setVisibility(View.GONE);
                                    binding.txtDataisnotfound.setVisibility(View.VISIBLE);
                                    binding.recyclerviewGround.setVisibility(View.GONE);
                                    binding.txtTotalTasks.setVisibility(View.GONE);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguages();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public void onMap() {
        if (GroundwaterContours != null && GroundwaterContours.length() > 0) {
            Intent in = new Intent(this, GroundWaterGoogleMap.class);
            in.putExtra("lat", lat);
            in.putExtra("lon", lon);
            startActivity(in);
        } else {
            getDynamicLanguageToast(getApplicationContext(), "nno_data_found", R.string.nno_data_found);

        }
    }

    public void setContour() {
        try {
            GroundwaterContours = new JSONArray();
            listdivergance = new ArrayList<>();
            listLatLong = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = new JSONObject();
                GroundWaterResponse res = list.get(i);
                String Contour = res.getMinlat() + "," + res.getMinlon() + "-" +
                        res.getMinlat() + "," + res.getMaxlon() + "-" +
                        res.getMaxlat() + "," + res.getMaxlon() + "-" +
                        res.getMaxlat() + "," + res.getMinlon() + "-" +
                        res.getMinlat() + "," + res.getMinlon();

                obj.put("gridName", res.getGridName());
                obj.put("Contour", Contour);

                double Actualgroundwater = getDecimal2(res.getActualgroundwater());
                double Estimatedgroundwater = getDecimal2(res.getEstimatedgroundwater());
                obj.put("Actualgroundwater", Actualgroundwater);
                obj.put("Estimatedgroundwater", Estimatedgroundwater);
//                obj.put("divergance", res.getDivergance());
                listdivergance.add(res.getDivergance());
                GroundwaterContours.put(obj);
                LatLng latlon = new LatLng(res.getLat(), res.getLon());
                listLatLong.add(latlon);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setLanguages() {
        setDynamicLanguage(this, binding.titleMain, "Groundwaterforecadting", R.string.Groundwaterforecadting);
        setDynamicLanguage(this, binding.txtGroundWaterleveltill, "GroundWaterleveltill", R.string.GroundWaterleveltill);
        setDynamicLanguage(this, binding.txtGrid, "Grid", R.string.Grid);
        setDynamicLanguage(this, binding.txtWaterlevelbase, "Waterlevelbase", R.string.Waterlevelbase);
        setDynamicLanguage(this, binding.txtWaterlevelForecasted, "WaterlevelForecasted", R.string.WaterlevelForecasted);
        setDynamicLanguage(this, binding.txtGroundWaterlevelStatus, "GroundWaterlevelStatus", R.string.GroundWaterlevelStatus);
        setDynamicLanguage(this, binding.rbGeotagFarmLocation, "GeotagFarmLocation", R.string.GeotagFarmLocation);
        setDynamicLanguage(this, binding.rbCurrentLocation, "CurrentLocation", R.string.CurrentLocation);
        setDynamicLanguage(this, binding.txtDataisnotfound, "CurrentlyDataisnotavailable", R.string.CurrentlyDataisnotavailable);
        setDynamicLanguage(this, binding.txtTotalTasks, "Mapview", R.string.Mapview);

        setFontsStyleTxt(this, binding.titleMain, 4);
        setFontsStyleTxt(this, binding.txtGroundWaterleveltill, 4);
        setFontsStyleTxt(this, binding.txtGrid, 5);
        setFontsStyleTxt(this, binding.txtWaterlevelbase, 5);
        setFontsStyleTxt(this, binding.txtWaterlevelForecasted, 5);
        setFontsStyleTxt(this, binding.txtGroundWaterlevelStatus, 5);
        setFontsStyleTxt(this, binding.rbGeotagFarmLocation, 5);
        setFontsStyleTxt(this, binding.rbCurrentLocation, 5);
        setFontsStyleTxt(this, binding.txtDataisnotfound, 5);
        setFontsStyle(this, binding.txtTotalTasks);
    }
}
