package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CattleweatherforecastBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.pepsico.ForecastAdapter;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CattleWeatherForecast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;


public class CattleWeatherForecast extends AppCompatActivity {


    String UID = "", lat = null, lon = null;
    DBAdapter db;
    CattleweatherforecastBinding binding;
    ProgressDialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cattleweatherforecast);
        //        setFonts();
        db = new DBAdapter(this);

        binding.nodata.setVisibility(View.GONE);
        binding.parent.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification
            try {
                if (bundle.size() > 0) {
                    lat = bundle.getString("lat");
                    lon = bundle.getString("long");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (lat == null || lat.length() < 1 || lon == null || lon.length() < 1) {
            lat = "" + LatLonCellID.lat;
            lon = "" + LatLonCellID.lon;
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerViewForcast.setLayoutManager(llm);

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        loadForecastData();
        
        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(CattleWeatherForecast.this, db, SN_CattleWeatherForecast, UID);
    }

    public void loadForecastData() {
        dialog = ProgressDialog.show(CattleWeatherForecast.this, "", getResources().getString(R.string.loadingInformation), true);

        Log.v("sjdks", "https://myfarminfo.com/yfirest.svc/Forecast/v2/" + lat + "/" + lon);

        String language = AppManager.getInstance().getSelectedLanguages(this);
        String URL = AppManager.getInstance().getForecastURL(lat, lon, language);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");

                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");

                        System.out.println("ForecastResponse : " + response);
                        try {

                            if (response.equalsIgnoreCase("InsufData")) {


                                binding.nodata.setVisibility(View.VISIBLE);
                                binding.parent.setVisibility(View.GONE);
                            } else {

                                binding.nodata.setVisibility(View.GONE);
                                ArrayList<TempBean> listTemp = new ArrayList<TempBean>();

                                JSONObject jsonObject1 = new JSONObject(response);

                                JSONArray jsonArray = jsonObject1.getJSONArray("DT");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    TempBean bean = new TempBean();

                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    String weatherCondition = jsonObject2.getString("WeatherCondition");
                                    String humidity = jsonObject2.getString("Moisture");
                                    String rain = jsonObject2.getString("Rain");
                                    String day = jsonObject2.getString("Day");
                                    String date = jsonObject2.getString("Date");
                                    String maxTemp = jsonObject2.getString("MaxTemp");
                                    String minTemp = jsonObject2.getString("MinTemp");
                                    String windSpeed = jsonObject2.getString("WindSpeed");
                                    Double dd = 0.0;
                                    if (windSpeed != null) {

                                        dd = Double.valueOf(windSpeed) * 0.277778;
                                    }

                                    Double h = Math.ceil(Double.parseDouble(humidity));
                                    Double mx = Math.ceil(Double.parseDouble(maxTemp));
                                    Double min = Math.floor(Double.parseDouble(minTemp));
                                    Double sp = Math.ceil(dd);

                                    bean.setRain(rain);
                                    bean.setHumidity(h.intValue() + "%");
                                    if (date != null && !date.equalsIgnoreCase("null")
                                            && !date.equalsIgnoreCase("") && date.length() > 1) {
                                        bean.setDate(date);
                                    } else {
                                        bean.setDate("");
                                    }
                                    if (day != null && !day.equalsIgnoreCase("null")
                                            && !day.equalsIgnoreCase("") && day.length() > 1) {
                                        bean.setDay(day);
                                    } else {
                                        bean.setDay("");
                                    }
                                    bean.setMaxTemp(mx.intValue() + "\u00B0");
                                    bean.setMinTemp(min.intValue() + "\u00B0");
                                    bean.setWindSpeed(sp.intValue() + " m/s");

                                    weatherCondition = weatherCondition.replace("intermittent spell of rains", "showers");
                                    bean.setWeatherText(weatherCondition);

                                    Double rn = 0.0;
                                    if (rain != null) {

                                        Double d = Double.valueOf(rain);
                                        rn = d;
                                    }
                                    if (mx.intValue() > 35 && rn == 0) {
                                        bean.setImageType("1");
                                    } else if ((mx.intValue() < 30 && rn == 0) && h.intValue() > 70) {
                                        bean.setImageType("2");
                                    } else if (rn > 0.1 && rn <= 3) {
                                        bean.setImageType("3");
                                    } else if (rn > 3 && rn < 20) {
                                        bean.setImageType("4");
                                    } else if (rn >= 20) {
                                        bean.setImageType("5");
                                    } else {
                                        bean.setImageType("6");
                                    }
                                    listTemp.add(bean);
                                }

                                if (listTemp.size() > 0) {
                                    binding.parent.setVisibility(View.VISIBLE);
                                    binding.nodata.setVisibility(View.GONE);
                                    ForecastAdapter adapter = new ForecastAdapter(CattleWeatherForecast.this, listTemp);
                                    binding.recyclerViewForcast.setAdapter(adapter);

                                } else {
                                    binding.parent.setVisibility(View.GONE);
                                    binding.nodata.setVisibility(View.VISIBLE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                System.out.println("Volley Error : " + error);
                binding.nodata.setVisibility(View.VISIBLE);

            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_CattleWeatherForecast, UID);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        Log.d("onBackPressed Method", "onBackPressed Method called");
        finish();
    }
}
