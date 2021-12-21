package com.weather.risk.mfi.myfarminfo.home;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.ForecastDetailAdapter;
import com.weather.risk.mfi.myfarminfo.bean.TempDetailBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.Register;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 21-06-2018.
 */
public class ForecastDetailActivity extends AppCompatActivity {

    String lat = null;
    String lon = null;
    String date = null;


    RecyclerView recyclerView;


    TextView noData;
    LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);
        ImageView backBTN = (ImageView) findViewById(R.id.backBTN);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        parent = (LinearLayout) findViewById(R.id.parent);
        noData = (TextView) findViewById(R.id.nodata);
        noData.setVisibility(View.GONE);
        parent.setVisibility(View.GONE);

        date = getIntent().getStringExtra("date");
        String aa = AppConstant.latitude;
        String bb = AppConstant.longitude;
        if (aa != null && bb != null) {

            lat = aa;
            lon = bb;

        }

        if (lat == null) {
            lat = "" + LatLonCellID.lat;
            lon = "" + LatLonCellID.lon;
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_forcast);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        if (date != null) {
            loadForecastData(date);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Dataisnotfound), Toast.LENGTH_SHORT).show();
        }
    }


    ProgressDialog dialog;

    public void loadForecastData(String dat) {
        dialog = ProgressDialog.show(this, "", getResources().getString(R.string.loadingInformation), true);

        String requestString = "https://myfarminfo.com/yfirest.svc/Data/Forecast/Full/" + lat + "/" + lon + "/" + dat + "/0";
        String createdString = AppManager.getInstance().removeSpaceForUrl(requestString);

        Log.v("forecastDetailRequest", createdString);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, createdString,
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
                                ArrayList<TempDetailBean> listTemp = new ArrayList<TempDetailBean>();

                                JSONObject jsonObject1 = new JSONObject(response);

                                JSONArray jsonArray = jsonObject1.getJSONArray("DT");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    TempDetailBean bean = new TempDetailBean();

                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    String weatherCondition = jsonObject2.getString("WeatherCondition");
                                    String humidity = jsonObject2.getString("Moisture");
                                    String rain = jsonObject2.getString("Rain");
                                    String time = jsonObject2.getString("Time");
                                    String date = jsonObject2.getString("Date");
                                    String maxTemp = jsonObject2.getString("MaxTemp");
                                    String minTemp = jsonObject2.getString("MinTemp");
                                    if (minTemp!=null && minTemp.length()>0) {
                                        Log.v("mintemp", minTemp + "--");
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
                                        bean.setDate(date);
                                        bean.setTime(time);
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
                                        } else if ((mx.intValue() < 30 && rn == 0)&& h.intValue() >70) {
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
                                }

                                if (listTemp.size() > 0) {
                                    parent.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.GONE);
                                    ForecastDetailAdapter adapter = new ForecastDetailAdapter(ForecastDetailActivity.this, listTemp);
                                    recyclerView.setAdapter(adapter);

                                } else {
                                    parent.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
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
                noData.setVisibility(View.VISIBLE);

            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
