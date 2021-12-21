package com.weather.risk.mfi.myfarminfo.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.ForecastAdapterList;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.home.WeatherForecast_Details;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.mapfragments.WeatherActual;
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

import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_WeatherForecast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyle;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

/**
 * Created by Admin on 26-02-2018.
 */
@SuppressLint("ValidFragment")
public class WeatherForecast extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity";
    private static final String FARM_NAME = "FarmName";
    private static final String ALL_POINTS = "AllLatLngPount";
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
    TextView noData, icon1, icon2, icon3, weather_text, weather_Day, weather_Wind, weather_Windunit,txt_WeeklyWeather, weather_Temp, weather_Rain, weather_Date;
    LinearLayout parent;
    ImageView image1;
    //    ProgressDialog dialog;
    TransparentProgressDialog dialog;

    String PressBefore = "";
    Button ActualWeather_btn, DeatailsWeather_btn;
    // TODO: Rename and change types of parameters
    private int callingActivity;
    private String selectedFarmName;
    private String area;
    private String cropArr[];
    private String diseaseArr[];

    String UID = "";
    DBAdapter db;
    private long mRequestStartTime;

    @SuppressLint("ValidFragment")
    public WeatherForecast(String la, String lo) {
        // Required empty public constructor
        lat = la;
        lon = lo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.weather_forecast, container, false);

        parent = (LinearLayout) view.findViewById(R.id.parent);
        noData = (TextView) view.findViewById(R.id.nodata);
        noData.setVisibility(View.GONE);
        parent.setVisibility(View.GONE);

        db = new DBAdapter(getActivity());

        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);

        gridview = (GridView) view.findViewById(R.id.gridview_forcast);

        Temp_BTN = (Button) view.findViewById(R.id.Temp_Btn);
        Rain_BTN = (Button) view.findViewById(R.id.Rain_Btn);
        Air_Btn = (Button) view.findViewById(R.id.Air_Btn);
        mChart = (LineChart) view.findViewById(R.id.LineChart1);

        icon1 = (TextView) view.findViewById(R.id.icon1);
        icon2 = (TextView) view.findViewById(R.id.icon2);
        icon3 = (TextView) view.findViewById(R.id.icon3);
        image1 = (ImageView) view.findViewById(R.id.image1);

        ActualWeather_btn = (Button) view.findViewById(R.id.ActualWeather_btn);
        DeatailsWeather_btn = (Button) view.findViewById(R.id.DeatailsWeather_btn);

        weather_text = (TextView) view.findViewById(R.id.weather_text);
        weather_Day = (TextView) view.findViewById(R.id.weather_Day);
        weather_Date = (TextView) view.findViewById(R.id.weather_Date);
        weather_Wind = (TextView) view.findViewById(R.id.weather_Wind);
        weather_Windunit = (TextView) view.findViewById(R.id.weather_Windunit);
        txt_WeeklyWeather = (TextView) view.findViewById(R.id.txt_WeeklyWeather);
        weather_Temp = (TextView) view.findViewById(R.id.weather_Temp);
        weather_Rain = (TextView) view.findViewById(R.id.weather_Rain);

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

        animZoomIn = AnimationUtils.loadAnimation(view.getContext(),
                R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(view.getContext(),
                R.anim.zoom_out);

        ActualWeather_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActual.class);
                getActivity().startActivity(intent);
            }
        });
        DeatailsWeather_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherForecast_Details.class);
                getActivity().startActivity(intent);
            }
        });

        //  Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        Typeface tfIcon = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericons-regular.otf");


        icon1.setTypeface(tfIcon);
//        icon1.setText(getDynamicLanguageValue(getActivity(), "icon_sun", R.string.icon_sun));
        icon1.setText(getResources().getString(R.string.icon_sun));
        icon1.setTextColor(Color.GRAY);

        icon2.setTypeface(tfIcon);
        icon2.setText(getDynamicLanguageValue(getActivity(), "icon_cloud_rain", R.string.icon_cloud_rain));
        icon2.setText(getResources().getString(R.string.icon_cloud_rain));
        icon2.setTextColor(Color.WHITE);

        icon3.setTypeface(tfIcon);
//        icon3.setText(getDynamicLanguageValue(getActivity(), "icon_wind", R.string.icon_wind));
        icon3.setText(getResources().getString(R.string.icon_wind));
        icon3.setTextColor(Color.WHITE);

//        String aa = AppConstant.latitude;
//        String bb = AppConstant.longitude;
//        if (aa != null && bb != null) {
//
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

        if (AppConstant.isLogin)
            loadForecastData();
        else
            loadGuestForecastData();

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_WeatherForecast, UID);

        return view;
    }

    String getForecastWeather;

    public void loadForecastData() {

        String farm_ID = AppConstant.farm_id;
        String language = AppManager.getInstance().getSelectedLanguages(getActivity());
        getForecastWeather = AppManager.getInstance().getForecastURL(lat, lon, language);

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
            dialog = new TransparentProgressDialog(getActivity(),
                    getDynamicLanguageValue(getActivity(), "Dataisloading", R.string.Dataisloading));
            dialog.show();
            Log.v("sjdks", getForecastWeather);

            mRequestStartTime = System.currentTimeMillis();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, getForecastWeather,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.cancel();
                            try {
                                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                                if (seconds > 0) {
                                    SaveLocalFile(db, getActivity(), SN_WeatherForecast, getForecastWeather, response, "", "" + seconds, AppConstant.farm_id, "Working");
                                }
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
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.cancel();
                    int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                    SaveLocalFile(db, getActivity(), SN_WeatherForecast, getForecastWeather, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");
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

    String url;

    public void loadGuestForecastData() {

        String language = AppManager.getInstance().getSelectedLanguages(getActivity());
        url = AppManager.getInstance().getForecastURL(lat, lon, language);
        db.open();
        dialog = new TransparentProgressDialog(getActivity(),
                getDynamicLanguageValue(getActivity(), "Dataisloading", R.string.Dataisloading));
        dialog.show();

        Log.v("sjdks", url);
        mRequestStartTime = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                            if (seconds > 0) {
                                SaveLocalFile(db, getActivity(), SN_WeatherForecast, url, response, "", "" + seconds, AppConstant.farm_id, "Working");
                            }
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            response = response.replace("\"{", "{");
                            response = response.replace("}\"", "}");

                            response = response.replace("\"[", "[");
                            response = response.replace("]\"", "]");

                            System.out.println("ForecastResponse : " + response);

                            OperationloadForecastData(response);
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
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                if (seconds > 0) {
                    SaveLocalFile(db, getActivity(), SN_WeatherForecast, url, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");
                }
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
                noData.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);
            } else if (response.equals("NoData")) {
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
//                    day = getDaysinDynamicLangauge(getActivity(), day);
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

                    Double h = 0.0;
                    if (humidity != null && !humidity.equalsIgnoreCase("null") && humidity.length() > 0) {
                        h = Math.ceil(Double.parseDouble(humidity));
                    }
                    Double mx = 0.0;
                    if (maxTemp != null && !maxTemp.equalsIgnoreCase("null") && maxTemp.length() > 0) {
                        mx = Math.ceil(Double.parseDouble(maxTemp));
                    }
                    Double min = 0.0;
                    if (minTemp != null && !minTemp.equalsIgnoreCase("null") && minTemp.length() > 0) {
                        min = Math.ceil(Double.parseDouble(minTemp));
                    }
                    Double sp = 0.0;
                    if (dd != null) {
                        sp = Math.ceil(dd);
                    }

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
                    parent.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    //   ForecastAdapter adapter = new ForecastAdapter(getActivity(), listTemp);
                    //  recyclerView.setAdapter(adapter);

                    weather_Date.setText(listTemp.get(0).getDate());
                    weather_Day.setText(listTemp.get(0).getDay());
                    weather_Wind.setText(listTemp.get(0).getWindSpeed().replace(" m/s", ""));
                    weather_Temp.setText(listTemp.get(0).getMaxTemp());
                    weather_Rain.setText(listTemp.get(0).getRain());
                    weather_text.setText(listTemp.get(0).getWeatherText());
                    makeGraph("Temp");
                    ForecastAdapterList adapterList = new ForecastAdapterList(getActivity(), listTemp);
                    gridview.setAdapter(adapterList);
//                    try {
//                        Picasso.with(getActivity()).load(listTemp.get(0).getImagepath()).into(image1);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                    adapterList.setImage(listTemp, 0, image1);


                } else {
                    parent.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            SaveLocalFile(db, getActivity(), "WeatherForecast", getForecastWeather, response, "JSON Response Error", "", AppConstant.farm_id, "Error");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void makeGraph(String type) {
        try {
            if (PressBefore != type) {
                if (type == "Temp") {
                    Temp_BTN.startAnimation(animZoomIn);
                    Temp_BTN.setText(Html.fromHtml("<u>" + getDynamicLanguageValue(getActivity(), "Temp", R.string.Temp) + "</u>"));

                    Rain_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Rain") {
                        Rain_BTN.setText(getDynamicLanguageValue(getActivity(), "Rain", R.string.Rain));
                    }

                    Air_Btn.startAnimation(animZoomOut);
                    if (PressBefore == "Air") {
                        Air_Btn.setText(getDynamicLanguageValue(getActivity(), "WindSpeed", R.string.WindSpeed));
                    }

                    PressBefore = type;
                } else if (type == "Rain") {

                    Temp_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Temp") {
                        Temp_BTN.setText(getDynamicLanguageValue(getActivity(), "Temp", R.string.Temp));
                    }
                    Rain_BTN.startAnimation(animZoomIn);
                    Rain_BTN.setText(Html.fromHtml("<u>" + getDynamicLanguageValue(getActivity(), "Rain", R.string.Rain) + "</u>"));

                    Air_Btn.startAnimation(animZoomOut);
                    if (PressBefore == "Air") {
                        Air_Btn.setText(getDynamicLanguageValue(getActivity(), "WindSpeed", R.string.WindSpeed));
                    }

                    PressBefore = type;
                } else if (type == "Air") {

                    Temp_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Temp") {
                        Temp_BTN.setText(getDynamicLanguageValue(getActivity(), "Temp", R.string.Temp));
                    }

                    Rain_BTN.startAnimation(animZoomOut);
                    if (PressBefore == "Rain") {
                        Rain_BTN.setText(getDynamicLanguageValue(getActivity(), "Rain", R.string.Rain));
                    }
                    Air_Btn.startAnimation(animZoomIn);
                    Air_Btn.setText(Html.fromHtml("<u>" +getDynamicLanguageValue(getActivity(), "WindSpeed", R.string.WindSpeed)+ "</u>"));

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
            getDynamicLanguageToast(getActivity(), ex.getMessage());

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
        mChart.setNoDataTextDescription(getDynamicLanguageValue(getActivity(), "Youneedtoprovide", R.string.Youneedtoprovide));

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


    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        setLamguages();
        /*TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText(getString(R.string.weather_forecast));
        farmInfo.setTextColor(Color.WHITE);*/
    }


    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(getActivity(), db, SN_WeatherForecast, UID);
    }

    public void setLamguages() {
        setFontsStyleTxt(getActivity(), weather_Date, 7);
        setFontsStyleTxt(getActivity(), weather_Day, 6);
        setFontsStyleTxt(getActivity(), weather_Temp, 5);
        setFontsStyleTxt(getActivity(), weather_Wind, 5);
        setFontsStyleTxt(getActivity(), weather_Windunit, 5);
        setFontsStyleTxt(getActivity(), noData, 5);
        setFontsStyleTxt(getActivity(), txt_WeeklyWeather, 5);

        setFontsStyle(getActivity(), Temp_BTN);
        setFontsStyle(getActivity(), Rain_BTN);
        setFontsStyle(getActivity(), Air_Btn);
        setFontsStyle(getActivity(), DeatailsWeather_btn);
        setFontsStyle(getActivity(), ActualWeather_btn);

        setDynamicLanguage(getActivity(), Temp_BTN, "Temp", R.string.Temp);
        setDynamicLanguage(getActivity(), Rain_BTN, "Rain", R.string.Rain);
        setDynamicLanguage(getActivity(), Air_Btn, "WindSpeed", R.string.WindSpeed);
        setDynamicLanguage(getActivity(), DeatailsWeather_btn, "ForecastDetails", R.string.ForecastDetails);
        setDynamicLanguage(getActivity(), ActualWeather_btn, "ActualWeather", R.string.ActualWeather);
        setDynamicLanguage(getActivity(), noData, "Noforcast", R.string.Noforcast);
        setDynamicLanguage(getActivity(), txt_WeeklyWeather, "DailyWeatherForecast", R.string.DailyWeatherForecast);


    }

}

