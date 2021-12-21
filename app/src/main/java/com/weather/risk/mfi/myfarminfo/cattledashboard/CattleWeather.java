package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.ForecastAdapterList;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.databinding.CattleweatherBinding;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CattleWeather;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;


public class CattleWeather extends AppCompatActivity {

    CattleweatherBinding binding;
    String UID = "", lat = null, lon = null;
    DBAdapter db;
    ArrayList<TempBean> listResponse = new ArrayList<TempBean>();
    Animation animZoomIn, animZoomOut;
    TransparentProgressDialog dialog;
    String PressBefore = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cattleweather);
//        setFonts();
        binding.nodata.setVisibility(View.GONE);
        binding.parent.setVisibility(View.GONE);
        db = new DBAdapter(this);

        binding.TempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGraph("Temp");
            }
        });

        binding.AirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGraph("Air");
            }
        });
        binding.RainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGraph("Rain");
            }
        });

        animZoomIn = AnimationUtils.loadAnimation(this,
                R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(this,
                R.anim.zoom_out);

        binding.ActualWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CattleWeather.this, CattleActualWeather.class);
                startActivity(intent);
            }
        });
        binding.DeatailsWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CattleWeather.this, CattleWeatherForecast.class);
                startActivity(intent);
            }
        });

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //  Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        Typeface tfIcon = Typeface.createFromAsset(this.getAssets(), "fonts/weathericons-regular.otf");


        binding.icon1.setTypeface(tfIcon);
        binding.icon1.setText(getString(R.string.icon_sun));
        binding.icon1.setTextColor(Color.GRAY);

        binding.icon2.setTypeface(tfIcon);
        binding.icon2.setText(getString(R.string.icon_cloud_rain));
        binding.icon2.setTextColor(Color.WHITE);

        binding.icon3.setTypeface(tfIcon);
        binding.icon3.setText(getString(R.string.icon_wind));
        binding.icon3.setTextColor(Color.WHITE);


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


        if (AppConstant.isLogin)
            loadForecastData();
        else
            loadGuestForecastData();

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(CattleWeather.this, db, SN_CattleWeather, UID);
    }

    public void loadForecastData() {

        String farm_ID = AppConstant.farm_id;
        String language = AppManager.getInstance().getSelectedLanguages(this);
        String url = AppManager.getInstance().getForecastURL(lat, lon, language);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);
        db.open();
        Cursor cursor = db.getCatchedData(farm_ID, "Weather_Fore", strDate, "Active");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String dataOut = cursor.getString(cursor.getColumnIndex("datastring"));
            OperationloadForecastData(dataOut);
        } else {
            final String finalstrDate = strDate;
            final String finalFramId = farm_ID;
            dialog = new TransparentProgressDialog(this,
                    getResources().getString(R.string.Dataisloading));
            dialog.show();
            Log.v("sjdks", url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
                            ///////////////////////////Insert in Catched Table//////
                            db.open();
                            long l = db.insertCatchedData(finalFramId, "Weather_Fore", response.toString(), "Active");
                            if (l > 0)
                                db.deleteCatchedData(finalFramId, "Weather_Fore", finalstrDate, "Active");
                            db.close();

                            ////////////////////////
                            OperationloadForecastData(response);

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
    }

    public void loadGuestForecastData() {

        String language = AppManager.getInstance().getSelectedLanguages(this);
        String url = AppManager.getInstance().getForecastURL(lat, lon, language);
        db.open();
        dialog = new TransparentProgressDialog(this,
                getResources().getString(R.string.Dataisloading));
        dialog.show();

        Log.v("sjdks", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
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

                        OperationloadForecastData(response);

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

    public void OperationloadForecastData(String response) {
        try {

            if (response.equalsIgnoreCase("InsufData")) {
                binding.nodata.setVisibility(View.VISIBLE);
                binding.parent.setVisibility(View.GONE);
            } else if (response.equals("NoData")) {
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
//                    day = getDaysinDynamicLangauge(getActivity(), day);
                    String date = jsonObject2.getString("Date");
                    String maxTemp = jsonObject2.getString("MaxTemp");
                    String minTemp = jsonObject2.getString("MinTemp");
                    String windSpeed = jsonObject2.getString("WindSpeed");
                    //Herojit Add imagepath
                    String imagepath = jsonObject2.getString("imagepath");
                    bean.setImagepath(imagepath);

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
                    listResponse = listTemp;
                    binding.parent.setVisibility(View.VISIBLE);
                    binding.nodata.setVisibility(View.GONE);
                    //   ForecastAdapter adapter = new ForecastAdapter(getActivity(), listTemp);
                    //  recyclerView.setAdapter(adapter);

                    binding.weatherDate.setText(listTemp.get(0).getDate());
                    binding.weatherDay.setText(listTemp.get(0).getDay());
                    binding.weatherWind.setText(listTemp.get(0).getWindSpeed().replace(" m/s", ""));
                    binding.weatherTemp.setText(listTemp.get(0).getMaxTemp());
                    binding.weatherRain.setText(listTemp.get(0).getRain());
                    binding.weatherText.setText(listTemp.get(0).getWeatherText());
                    makeGraph("Temp");
                    ForecastAdapterList adapterList = new ForecastAdapterList(CattleWeather.this, listTemp);
                    binding.gridviewForcast.setAdapter(adapterList);
//                    try {
//                        Picasso.with(getActivity()).load(listTemp.get(0).getImagepath()).into(image1);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                    adapterList.setImage(listTemp, 0, binding.image1);


                } else {
                    binding.parent.setVisibility(View.GONE);
                    binding.nodata.setVisibility(View.VISIBLE);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void makeGraph(String type) {
        try {
            if (PressBefore != type) {
                if (type == "Temp") {
                    binding.TempBtn.startAnimation(animZoomIn);
                    binding.TempBtn.setText(Html.fromHtml("<u>" + getResources().getString(R.string.Temp) + "</u>"));

                    binding.RainBtn.startAnimation(animZoomOut);
                    if (PressBefore == "Rain") {
                        binding.RainBtn.setText(getResources().getString(R.string.Rain));
                    }

                    binding.AirBtn.startAnimation(animZoomOut);
                    if (PressBefore == "Air") {
                        binding.AirBtn.setText(getResources().getString(R.string.WindSpeed));
                    }

                    PressBefore = type;
                } else if (type == "Rain") {

                    binding.TempBtn.startAnimation(animZoomOut);
                    if (PressBefore == "Temp") {
                        binding.TempBtn.setText(getResources().getString(R.string.Temp));
                    }
                    binding.RainBtn.startAnimation(animZoomIn);
                    binding.RainBtn.setText(Html.fromHtml("<u>" + getResources().getString(R.string.Rain) + "</u>"));

                    binding.AirBtn.startAnimation(animZoomOut);
                    if (PressBefore == "Air") {
                        binding.AirBtn.setText(getResources().getString(R.string.WindSpeed));
                    }

                    PressBefore = type;
                } else if (type == "Air") {
                    binding.TempBtn.startAnimation(animZoomOut);
                    if (PressBefore == "Temp") {
                        binding.TempBtn.setText(getResources().getString(R.string.Temp));
                    }

                    binding.RainBtn.startAnimation(animZoomOut);
                    if (PressBefore == "Rain") {
                        binding.RainBtn.setText(getResources().getString(R.string.Rain));
                    }
                    binding.AirBtn.startAnimation(animZoomIn);
                    binding.AirBtn.setText(Html.fromHtml("<u>" + getResources().getString(R.string.WindSpeed) + "</u>"));

                    PressBefore = type;
                }

                // Button btn = (Button) getActivity().findViewById(R.id.Temp_Button);

                // #FF4500 equal to OrangeRed color
                // int textColor = Color.parseColor("#FF4500");

                // set the button text color
                // setTextColor() method require to pass an int color
                // btn.setTextColor(textColor);

                ArrayList<Entry> yVals = new ArrayList<Entry>();
                ArrayList<Entry> yVals2 = new ArrayList<Entry>();
                ArrayList<String> xVals = new ArrayList<String>();

                for (int i = 0; i < listResponse.size(); i = i + 1) {
                    Float param = 0.0f, param2 = 0.0f;
                    try {
                        if (type == "Temp") {
                            param = Float.parseFloat(listResponse.get(i).getMaxTemp().replace("\u00B0", ""));
                            param2 = Float.parseFloat(listResponse.get(i).getMinTemp().replace("\u00B0", ""));
                            yVals2.add(new Entry(param2, i));
                        } else if (type == "Rain")
                            param = Float.parseFloat(listResponse.get(i).getRain());
                        else if (type == "Air")
                            param = Float.parseFloat(listResponse.get(i).getWindSpeed().replace(" m/s", ""));

                        yVals.add(new Entry(param, i));
                        xVals.add(listResponse.get(i).getDate());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }
                FillChart(xVals, yVals, yVals2, type);

                // this line also set the button text color
                //btn.setTextColor(Color.RED);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Toast.makeText(CattleWeather.this, ex.getMessage(), Toast.LENGTH_SHORT);

        }
    }

    public void FillChart
            (ArrayList<String> xVals, ArrayList<Entry> yVals, ArrayList<Entry> yVals2, String
                    type) {
//        binding.LineChart1 = (LineChart) getActivity().findViewById(R.id.LineChart1);
        binding.LineChart1.setDrawGridBackground(false);

        // add data
        setData(xVals, yVals, yVals2, type);

        // get the legend (only possible after setting data)
        Legend l = binding.LineChart1.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        binding.LineChart1.setDescription("");
        binding.LineChart1.setNoDataTextDescription(getResources().getString(R.string.Youneedtoprovide));

        // enable touch gestures
        binding.LineChart1.setTouchEnabled(true);

        // enable scaling and dragging
        binding.LineChart1.setDragEnabled(true);
        binding.LineChart1.setScaleEnabled(true);
        // binding.LineChart1.setScaleXEnabled(true);
        // binding.LineChart1.setScaleYEnabled(true);
        binding.LineChart1.getAxisLeft().setDrawLabels(false);
        binding.LineChart1.getAxisLeft().setDrawAxisLine(false);


        YAxis leftAxis = binding.LineChart1.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);
        binding.LineChart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.LineChart1.getAxisLeft().setDrawGridLines(false);
        binding.LineChart1.getXAxis().setDrawGridLines(false);
        binding.LineChart1.getAxisRight().setEnabled(false);

        //binding.LineChart1.getViewPortHandler().setMaximumScaleY(2f);
        //binding.LineChart1.getViewPortHandler().setMaximumScaleX(2f);

        binding.LineChart1.animateX(1500, Easing.EasingOption.EaseInOutQuart);

        //  dont forget to refresh the drawing
        binding.LineChart1.invalidate();

    }

    private void setData
            (ArrayList<String> xVals, ArrayList<Entry> yVals, ArrayList<Entry> yVals2, String
                    type) {


        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "");

        set1.setFillAlpha(110);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.parseColor("#F9D139"));
        set1.setCircleColor(Color.parseColor("#F9D139"));
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.parseColor("#FFF8D3"));

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        if (type == "Temp") {
            LineDataSet set2;
            set2 = new LineDataSet(yVals2, "");
            set2.setFillAlpha(110);
            set2.setColor(Color.parseColor("#31daf7"));
            set2.setCircleColor(Color.parseColor("#0091aa"));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setDrawCircleHole(false);
            set2.setValueTextSize(9f);
            set2.setDrawFilled(true);
            set2.setFillColor(Color.parseColor("#97cfd8"));
            dataSets.add(set2); // add the datasets
        }

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        binding.LineChart1.setData(data);

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
        setScreenTracking(this, db, SN_CattleWeather, UID);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        Log.d("onBackPressed Method", "onBackPressed Method called");
        finish();
    }
}
