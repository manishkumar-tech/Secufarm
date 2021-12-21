package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.NewDashboardActivity;
import com.weather.risk.mfi.myfarminfo.adapter.ForecastAdapterList;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDaysinDynamicLangauge;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;


public class WeatherActual extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity";
    private static final String FARM_NAME = "FarmName";
    private static final String AREA = "area";
    String data = null;
    String d1, d2;
    String lat = null;
    String lon = null;
    GridView gridview;
    Button Temp_BTN, Rain_BTN, Air_Btn;
    LineChart mChart;
    ArrayList<TempBean> listResponse = new ArrayList<TempBean>();
    Animation animZoomIn, animZoomOut;
    String cropName = null;
    String diseaseName = null;
    RecyclerView recyclerView;
    TextView txt_title,icon1, icon2, icon3, weather_text, weather_Day, weather_Wind, weather_Temp, weather_Windunit, txtDailyWeatherData,weather_Rain, weather_Date;
    LinearLayout parent;
    ImageView image1;
    ProgressDialog dialog;
    String PressBefore = "";
    TableRow backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatheractual);

        parent = (LinearLayout) findViewById(R.id.parent);
        gridview = (GridView) findViewById(R.id.gridview_forcast);

        Temp_BTN = (Button) findViewById(R.id.Temp_Btn);
        Rain_BTN = (Button) findViewById(R.id.Rain_Btn);
        Air_Btn = (Button) findViewById(R.id.Air_Btn);
        mChart = (LineChart) findViewById(R.id.LineChart1);

        txt_title = (TextView) findViewById(R.id.txt_title);
        icon1 = (TextView) findViewById(R.id.icon1);
        icon2 = (TextView) findViewById(R.id.icon2);
        icon3 = (TextView) findViewById(R.id.icon3);
        image1 = (ImageView) findViewById(R.id.image1);
        weather_text = (TextView) findViewById(R.id.weather_text);
        weather_Day = (TextView) findViewById(R.id.weather_Day);
        weather_Date = (TextView) findViewById(R.id.weather_Date);
        weather_Wind = (TextView) findViewById(R.id.weather_Wind);
        weather_Windunit = (TextView) findViewById(R.id.weather_Windunit);
        weather_Temp = (TextView) findViewById(R.id.weather_Temp);
        weather_Rain = (TextView) findViewById(R.id.weather_Rain);
        txtDailyWeatherData = (TextView) findViewById(R.id.txtDailyWeatherData);
        animZoomIn = AnimationUtils.loadAnimation(this,
                R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(this,
                R.anim.zoom_out);

        Typeface tfIcon = Typeface.createFromAsset(this.getAssets(), "fonts/weathericons-regular.otf");

        icon1.setTypeface(tfIcon);
//        icon1.setText(getDynamicLanguageValue(getApplicationContext(), "icon_sun", R.string.icon_sun));
        icon1.setText(getResources().getString(R.string.icon_sun));
        icon1.setTextColor(Color.GRAY);

        icon2.setTypeface(tfIcon);
//        icon2.setText(getDynamicLanguageValue(getApplicationContext(), "icon_cloud_rain", R.string.icon_cloud_rain));
        icon2.setText(getResources().getString(R.string.icon_cloud_rain));
        icon2.setTextColor(Color.WHITE);

        icon3.setTypeface(tfIcon);
//        icon3.setText(getDynamicLanguageValue(getApplicationContext(), "icon_wind", R.string.icon_wind));
        icon3.setText(getResources().getString(R.string.icon_wind));
        icon3.setTextColor(Color.WHITE);




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

        Temp_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGraph("Temp");
            }
        });

        Air_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGraph("Air");
            }
        });
        Rain_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGraph("Rain");
            }
        });

//        GetWeatherActual();
        new getWeatheActual().execute();
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn);
        backbtn = (TableRow) findViewById(R.id.backbtn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setLamguages();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent in = new Intent(getApplicationContext(), NewDashboardActivity.class);
        in.putExtra("from", "weather");
        startActivity(in);
    }

//    public void GetWeatherActual() {
//        String CurrentDate = "", CurrentDate_3 = "";
//        try {
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = new Date();
//            CurrentDate = dateFormat.format(date);
//            Date result = dateFormat.parse(CurrentDate);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(result);
//            cal.add(Calendar.DATE, -8);
//            CurrentDate_3 = dateFormat.format(cal.getTime());//String.valueOf(cal.getTime());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
////        String url = "https://myfarminfo.com/yfirest.svc/Forecast/v2/" + lat + "/" + lon;
//        String url = "http://54.174.231.79:82/wdrest.svc/getMergeWeatherData/" + CurrentDate_3 + "/" + CurrentDate + "/" + lat + "/" + lon + "/00/0/%27%27/i,g,p/json/wrinternal/English/no";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        dialog.cancel();
//
//                        response = response.trim();
//                        response = response.substring(1, response.length() - 1);
//                        response = response.replace("\\", "");
//                        response = response.replace("\"{", "{");
//                        response = response.replace("}\"", "}");
//
//                        response = response.replace("\"[", "[");
//                        response = response.replace("]\"", "]");
//
//                        System.out.println("ForecastResponse : " + response);
////                        OperationloadForecastData(response);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.cancel();
//                System.out.println("Volley Error : " + error);
////                noData.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        int socketTimeout = 60000;//60 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
//
//        // Adding request to volley request queue
//        AppController.getInstance().addToRequestQueue(stringRequest);
//    }


    public void OperationloadForecastData(String response) {
        try {

//                noData.setVisibility(View.GONE);
            ArrayList<TempBean> listTemp = new ArrayList<TempBean>();

//            JSONObject jsonObject1 = new JSONObject(response);

            JSONArray jsonArray = new JSONArray(response);
            try {
                for (int i = 0; i < jsonArray.length(); i++) {

                    TempBean bean = new TempBean();
                    int j = jsonArray.length() - 1 - i;
                    JSONObject jsonObject2 = jsonArray.getJSONObject(j);
//                    String weatherCondition = "Very hot";//jsonObject2.getString("WeatherCondition");
                    String weatherCondition = getResources().getString(R.string.Veryhot);//jsonObject2.getString("WeatherCondition");

                    String humidity = "77.25"; //jsonObject2.getString("Moisture");
                    String rain = jsonObject2.getString("Rain");
                    String days = jsonObject2.getString("DateTime");
                    String date = "";
                    String finalDay = "";
                    try {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1 = format1.parse(days);
                        DateFormat format2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                        finalDay = format2.format(dt1);
                        //date
                        String dateval = String.valueOf(dt1);
                        String val[] = dateval.split(" ");

                        date = val[2] + " " + val[1];

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
//                    String day = finalDay;
                    String day = getDaysinDynamicLangauge(this, finalDay);
//                    String date = days;//jsonObject2.getString("DateTime");
                    String maxTemp = jsonObject2.getString("MaxTemp");
                    String minTemp = jsonObject2.getString("MinTemp");
//                    String windSpeed = "9.95";//
//                    String windSpeed = jsonObject2.getString("WindSpeed");
                    String windSpeed = jsonObject2.getString("MaxWindspeed");

                    //Herojit Add imagepath
                    String imagepath = jsonObject2.getString("imagepath");
                    bean.setImagepath(imagepath);

                    Double dd = 0.0;
                    if (windSpeed != null && !windSpeed.equalsIgnoreCase("null")) {
                        dd = Double.valueOf(windSpeed) * 0.277778;
                    }

                    Double h = Math.ceil(Double.parseDouble(humidity));
//                    Double mx = Math.ceil(Double.parseDouble(maxTemp));
//                    Double min = Math.floor(Double.parseDouble(minTemp));
                    Double sp = Math.ceil(dd);

                    bean.setRain(rain);
                    bean.setHumidity(h.intValue() + "%");
                    bean.setDate(date);
                    bean.setDay(day);
//                    bean.setMaxTemp(mx.intValue() + "\u00B0");
//                    bean.setMinTemp(min.intValue() + "\u00B0");
                    Double mx = Math.ceil(Double.parseDouble(maxTemp));
//                    bean.setMaxTemp(maxTemp);
                    bean.setMaxTemp(mx.intValue() + "\u00B0");


                    Double min = Math.ceil(Double.parseDouble(minTemp));

                    bean.setMinTemp(min.intValue() + "\u00B0");

                   // bean.setMinTemp(minTemp);
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (listTemp.size() > 0) {
                listResponse = listTemp;
                parent.setVisibility(View.VISIBLE);
//                    noData.setVisibility(View.GONE);
                //   ForecastAdapter adapter = new ForecastAdapter(getActivity(), listTemp);
                //  recyclerView.setAdapter(adapter);

                weather_Date.setText(listTemp.get(0).getDate());
                weather_Day.setText(listTemp.get(0).getDay());
                weather_Wind.setText(listTemp.get(0).getWindSpeed().replace(" m/s", ""));
                weather_Temp.setText(listTemp.get(0).getMaxTemp());
                weather_Rain.setText(listTemp.get(0).getRain());
                weather_text.setText(listTemp.get(0).getWeatherText());
                makeGraph("Temp");
                ForecastAdapterList adapterList = new ForecastAdapterList(this, listTemp);
                gridview.setAdapter(adapterList);
                adapterList.setImage(listTemp, 0, image1);


            } else {
                parent.setVisibility(View.GONE);
//                    noData.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeGraph(String type) {
        try {
            if (PressBefore != type) {
                if (type == "Temp") {
                    Temp_BTN.startAnimation(animZoomIn);
                    Temp_BTN.setText(Html.fromHtml("<u>" + getDynamicLanguageValue(getApplicationContext(), "Temp", R.string.Temp) + "</u>"));

                    Rain_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Rain") {
                        Rain_BTN.setText(getDynamicLanguageValue(getApplicationContext(), "Rain", R.string.Rain));
                    }

                    Air_Btn.startAnimation(animZoomOut);
                    if (PressBefore == "Air") {
                        Air_Btn.setText(getDynamicLanguageValue(getApplicationContext(), "WindSpeed", R.string.WindSpeed));
                    }

                    PressBefore = type;
                } else if (type == "Rain") {

                    Temp_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Temp") {
                        Temp_BTN.setText(getDynamicLanguageValue(getApplicationContext(), "Temp", R.string.Temp));
                    }
                    Rain_BTN.startAnimation(animZoomIn);
                    Rain_BTN.setText(Html.fromHtml("<u>" + getDynamicLanguageValue(getApplicationContext(), "Rain", R.string.Rain) + "</u>"));

                    Air_Btn.startAnimation(animZoomOut);
                    if (PressBefore == "Air") {
                        Air_Btn.setText(getDynamicLanguageValue(getApplicationContext(), "WindSpeed", R.string.WindSpeed));
                    }

                    PressBefore = type;
                } else if (type == "Air") {

                    Temp_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Temp") {
                        Temp_BTN.setText(getDynamicLanguageValue(getApplicationContext(), "Temp", R.string.Temp));
                    }

                    Rain_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Rain") {
                        Rain_BTN.setText(getDynamicLanguageValue(getApplicationContext(), "Rain", R.string.Rain));
                    }
                    Air_Btn.startAnimation(animZoomIn);
                    Air_Btn.setText(Html.fromHtml("<u>" + getDynamicLanguageValue(getApplicationContext(), "WindSpeed", R.string.WindSpeed) + "</u>"));

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
                        } else if (type == "Rain") {
                            param = Float.parseFloat(listResponse.get(i).getRain());
                        } else if (type == "Air")
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
            getDynamicLanguageToast(getApplicationContext(), ex.getMessage());

        }
    }

    public void FillChart(ArrayList<String> xVals, ArrayList<Entry> yVals, ArrayList<Entry> yVals2, String type) {
//        mChart = (LineChart) getActivity().findViewById(R.id.LineChart1);

        mChart.setDrawGridBackground(false);

        // add data
        setData(xVals, yVals, yVals2, type);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription(getDynamicLanguageValue(getApplicationContext(), "Youneedtoprovide", R.string.Youneedtoprovide));

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisLeft().setDrawAxisLine(false);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);

        //  dont forget to refresh the drawing
        mChart.invalidate();

    }

    private void setData(ArrayList<String> xVals, ArrayList<Entry> yVals, ArrayList<Entry> yVals2, String type) {

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
        mChart.setData(data);

    }

    private class getWeatheActual extends AsyncTask<Void, Void, String> {

        String result = "";
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WeatherActual.this);
            progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                                @Override
                                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                                    return false;
                                                }
                                            }
            );
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                String CurrentDate = "", CurrentDate_3 = "";
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    CurrentDate = dateFormat.format(date);
                    Date result = dateFormat.parse(CurrentDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(result);
                    cal.add(Calendar.DATE, -7);
                    CurrentDate_3 = dateFormat.format(cal.getTime());//String.valueOf(cal.getTime());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                String URL = "http://54.174.231.79:82/wdrest.svc/getMergeWeatherData/" + CurrentDate_3 + "/" + CurrentDate + "/" + lat + "/" + lon + "/00/0/%27%27/i,g,p/json/wrinternal/English/no";
                //String URL = "http://3.88.31.90:82/wdrest.svc/getMergeWeatherData/" + CurrentDate_3 + "/" + CurrentDate + "/" + lat + "/" + lon + "/00/0/%27%27/i,g,p/json/wrinternal/English/no";
                String URL = "https://myfarminfo.com/yfirest.svc/WeatherData/v3"+ "/" + lat + "/" + lon + "/" + "english";
                String response = AppManager.getInstance().httpRequestGetMethod(URL);
                System.out.println("farm_details :" + response);
                return response;


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null; //show network problem
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            try {
                if (response.equalsIgnoreCase("InsufData")) {
                    parent.setVisibility(View.GONE);
                } else if (response.equals("NoData")) {
                    parent.setVisibility(View.GONE);
                } else if (response != null) {
                    OperationloadForecastData(response);
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "ServerError", R.string.ServerError);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("catch block Pls Try again");
            }

            progressDialog.dismiss();

        }
    }

    public void setLamguages() {
        setFontsStyleTxt(this, txt_title, 2);
        setFontsStyleTxt(this, weather_Date, 7);
        setFontsStyleTxt(this, weather_Day, 6);
        setFontsStyleTxt(this, weather_Temp, 5);
        setFontsStyleTxt(this, weather_Wind, 5);
        setFontsStyleTxt(this, weather_Windunit, 6);
        setFontsStyleTxt(this, txtDailyWeatherData, 5);

        setFontsStyle(this, Temp_BTN);
        setFontsStyle(this, Rain_BTN);
        setFontsStyle(this, Air_Btn);

        setDynamicLanguage(this, txt_title, "ActualWeather", R.string.ActualWeather);
        setDynamicLanguage(this, Temp_BTN, "Temp", R.string.Temp);
        setDynamicLanguage(this, Temp_BTN, "Temp", R.string.Temp);
        setDynamicLanguage(this, Rain_BTN, "Rain", R.string.Rain);
        setDynamicLanguage(this, Air_Btn, "WindSpeed", R.string.WindSpeed);
        setDynamicLanguage(this, txtDailyWeatherData, "DailyWeatherData", R.string.DailyWeatherData);


    }


}