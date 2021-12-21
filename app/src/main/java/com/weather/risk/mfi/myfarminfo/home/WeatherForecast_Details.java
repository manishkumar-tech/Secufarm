package com.weather.risk.mfi.myfarminfo.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.pepsico.ForecastAdapter;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_WeatherForecast_Details;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class WeatherForecast_Details extends AppCompatActivity {
    String lat = null;
    String lon = null;
    RecyclerView recyclerView;


    TextView noData, txt_title;
    LinearLayout parent;
    ProgressDialog dialog;
    ImageView backBTN;

    DBAdapter db;
    String UID = "";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherforecast_details);

        db = new DBAdapter(this);

        parent = (LinearLayout) findViewById(R.id.parent);
        noData = (TextView) findViewById(R.id.nodata);
        txt_title = (TextView) findViewById(R.id.txt_title);
        backBTN = (ImageView) findViewById(R.id.backBTN);

        noData.setVisibility(View.GONE);
        parent.setVisibility(View.GONE);

//        String aa = AppConstant.latitude;
//        String bb = AppConstant.longitude;
//
//        if (aa != null && bb != null) {
//            lat = aa;
//            lon = bb;
//        }
//
//        if (lat == null) {
//            lat = "" + LatLonCellID.lat;
//            lon = "" + LatLonCellID.lon;
//        }


        if(AppConstant.selectedFarmLat == null && AppConstant.selectedFarmLong == null || AppConstant.selectedFarmLat.equalsIgnoreCase("null") && AppConstant.selectedFarmLong.equalsIgnoreCase("null")){

//            String aa = AppConstant.latitude;
//            String bb = AppConstant.longitude;
//            if (aa != null && bb != null) {
//
//                lat = aa;
//                lon = bb;
//            }

            // if (lat == null) {
            lat = "" + LatLonCellID.lat;
            lon = "" + LatLonCellID.lon;
            // }

        }else {
            lat = AppConstant.selectedFarmLat;
            lon = AppConstant.selectedFarmLong;
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_forcast);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        loadForecastData();

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_WeatherForecast_Details, UID);
    }

    public void loadForecastData() {
        try {
            dialog = ProgressDialog.show(WeatherForecast_Details.this, "", getDynamicLanguageValue(getApplicationContext(), "loadingInformation", R.string.loadingInformation), true);

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


                                    noData.setVisibility(View.VISIBLE);
                                    parent.setVisibility(View.GONE);
                                } else {

                                    noData.setVisibility(View.GONE);
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

                                        //Herojit Add imagepath
                                        String imagepath = jsonObject2.getString("imagepath");
                                        bean.setImagepath(imagepath);

                                        Double dd = 0.0;
                                        if (windSpeed != null && !windSpeed.equalsIgnoreCase("null")) {
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
                                        if (rain != null && !rain.equalsIgnoreCase("null")) {

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
                                        parent.setVisibility(View.VISIBLE);
                                        noData.setVisibility(View.GONE);
                                        ForecastAdapter adapter = new ForecastAdapter(WeatherForecast_Details.this, listTemp);
                                        recyclerView.setAdapter(adapter);

                                    } else {
                                        parent.setVisibility(View.GONE);
                                        noData.setVisibility(View.VISIBLE);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.cancel();
                    System.out.println("Volley Error : " + error);
                    noData.setVisibility(View.VISIBLE);

                }
            });

            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        setLamguages();
        Log.d("onResume Method", "onResume Method called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_WeatherForecast_Details, UID);
    }

    public void setLamguages() {
        setFontsStyleTxt(this, txt_title, 2);
        setFontsStyleTxt(this, noData, 5);

        setDynamicLanguage(this, txt_title, "WeatherForecastDetails", R.string.WeatherForecastDetails);
        setDynamicLanguage(this, noData, "Noforcast", R.string.Noforcast);


    }
}
