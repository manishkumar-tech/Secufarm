package com.weather.risk.mfi.myfarminfo.dabwali;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.FarmDetailAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.NDVIAdapter;
import com.weather.risk.mfi.myfarminfo.bean.AlertBean;
import com.weather.risk.mfi.myfarminfo.bean.KeyValueBean;
import com.weather.risk.mfi.myfarminfo.bean.NDBI_Bean;
import com.weather.risk.mfi.myfarminfo.pepsico.ForecastAdapter;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Admin on 07-06-2018.
 */
@SuppressLint("ValidFragment")
public class DabwaliAlert extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity";
    private static final String FARM_NAME = "FarmName";
    private static final String ALL_POINTS = "AllLatLngPount";
    private static final String AREA = "area";

    // TODO: Rename and change types of parameters
    private int callingActivity;
    private String selectedFarmName;
    private String area;
    String data = null;

    String nextResponse = null;
    String id;


    public DabwaliAlert(String res) {
        // Required empty public constructor
        nextResponse = res;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

  /*  Spinner districtSpinner,villageSpinner;
    Button submitBtn;
*/


    RecyclerView recyclerView;

    TextView noAlert;
    TextView farmInfo;
    Button moistureBTN;
    Button gObservationBTN;
    Button ndviBTN;
    Button alertBTN;

    ArrayList<TempBean> listTemp = new ArrayList<TempBean>();
    ArrayList<ForecastBean> listForecast = new ArrayList<ForecastBean>();
    ArrayList<KeyValueBean> farmDetailList = new ArrayList<KeyValueBean>();

    private <T> Iterable<T> iterate(final Iterator<T> i) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dabwali_alert, container, false);


        farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Alert");
        farmInfo.setTextColor(Color.WHITE);

        noAlert = (TextView) view.findViewById(R.id.no_alert);
        noAlert.setVisibility(View.GONE);

        FloatingActionButton floating = (FloatingActionButton) view.findViewById(R.id.floating_more);

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingPopupMethod();
            }
        });

        listTemp = new ArrayList<TempBean>();
        listForecast = new ArrayList<ForecastBean>();
        farmDetailList = new ArrayList<KeyValueBean>();

       /* nextBtn = (Button) view.findViewById(R.id.next_btn);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nextResponse != null) {
                    Fragment fragment = new DabwaliNDVI(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("moi").commit();
                }
            }
        });*/

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_alert);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);

        String lat1 = prefs.getString("lat", null);
        String lon1 = prefs.getString("lon", null);
        String villageId1 = prefs.getString("villageId", null);
        String villageName1 = prefs.getString("villageName", null);
        String role1 = prefs.getString(AppConstant.PREFRENCE_KEY_ROLE, null);


        id = AppConstant.dabFarmId;
        if (nextResponse != null) {
            messageData(nextResponse);
            //  noAlert.setVisibility(View.GONE);
        } else {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            noAlert.setVisibility(View.VISIBLE);
        }
        moistureBTN = (Button) view.findViewById(R.id.moisture_btn);
        gObservationBTN = (Button) view.findViewById(R.id.g_obser_btn);
        ndviBTN = (Button) view.findViewById(R.id.ndvi_btn);
        alertBTN = (Button) view.findViewById(R.id.alert_btn);

        setSubtitleLanguage();

        moistureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    Fragment fragment = new DabwaliMoisture(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });
        alertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    Fragment fragment = new DabwaliAlert(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });
        gObservationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    Fragment fragment = new GroundObservationFragment(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });
        ndviBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    Fragment fragment = new DabwaliNDVI(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.pepsico_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void onResume() {
        super.onResume();
        setSubtitleLanguage();

    }


    public void messageData(String response) {

        response = response.trim();

        response = response.replace("\\", "");
        response = response.replace("\"{", "{");
        response = response.replace("}\"", "}");
        response = response.replace("\"[", "[");
        response = response.replace("]\"", "]");

        System.out.println(" Response : " + response);

        if (response != null) {

            nextResponse = response;
            //   nextBtn.setVisibility(View.VISIBLE);

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("DT6");

                ArrayList<AlertBean> msgList = new ArrayList<AlertBean>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    AlertBean bean = new AlertBean();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    // bean.setId(jsonObject1.getString("ID"));
                    bean.setDate(jsonObject1.getString("ScheduleDate"));
                    //   bean.setVillageId(jsonObject1.getString("District_Id"));
                    bean.setMessage(jsonObject1.getString("Message"));
                    //   bean.setStartDate(jsonObject1.getString("Start_Date"));
                    //  bean.setDistrictId(jsonObject1.getString("District_Id"));

                    msgList.add(bean);
                }

                if (msgList.size() > 0) {

                    noAlert.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    AlertAdapter adapter = new AlertAdapter(getActivity(), msgList);
                    recyclerView.setAdapter(adapter);
                } else {
                    noAlert.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }


                listTemp = new ArrayList<TempBean>();
                listForecast = new ArrayList<ForecastBean>();
                farmDetailList = new ArrayList<KeyValueBean>();
                if (jsonObject.has("ForecastInfoFarms")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("ForecastInfoFarms");
                    if (jsonObject1.has("DT")) {
                        JSONArray jsonArray10 = jsonObject1.getJSONArray("DT");

                        for (int i = 0; i < jsonArray10.length(); i++) {

                            TempBean bean = new TempBean();

                            JSONObject jsonObject2 = jsonArray10.getJSONObject(i);
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
                            bean.setDate(date);
                            bean.setDay(day);
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


                    }

                }


                if (jsonObject.has("DT9")) {

                    JSONArray jsonArray11 = jsonObject.getJSONArray("DT9");

                    for (int i = 0; i < jsonArray11.length(); i++) {

                        ForecastBean bean = new ForecastBean();

                        JSONObject jsonObject2 = jsonArray11.getJSONObject(i);
                        if (jsonObject2.has("Date")) {
                            String dddttt = jsonObject2.getString("Date");
                            bean.setDate(dddttt);
                        }

                        if (jsonObject2.has("MaxTemp_Act")) {
                            String mxa = jsonObject2.getString("MaxTemp_Act");
                            bean.setMaxTemp_Act(mxa);
                        }
                        if (jsonObject2.has("MaxTemp_For")) {
                            String mxf = jsonObject2.getString("MaxTemp_For");
                            bean.setMaxTemp_For(mxf);
                        }
                        if (jsonObject2.has("MinTemp_Act")) {
                            String mna = jsonObject2.getString("MinTemp_Act");
                            bean.setMinTemp_Act(mna);
                        }
                        if (jsonObject2.has("MinTemp_For")) {
                            String mnf = jsonObject2.getString("MinTemp_For");
                            bean.setMinTemp_For(mnf);
                        }
                        if (jsonObject2.has("Rain_Act")) {
                            String rna = jsonObject2.getString("Rain_Act");
                            bean.setRain_Act(rna);
                        }
                        if (jsonObject2.has("Rain_For")) {
                            String rnf = jsonObject2.getString("Rain_For");
                            bean.setRain_For(rnf);
                        }
                        listForecast.add(bean);
                    }
                }

                if (jsonObject.has("DTPlotDetails")) {

                    JSONArray jsonArray111 = jsonObject.getJSONArray("DTPlotDetails");

                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < jsonArray111.length(); i++) {

                        try {

                            JSONObject object = jsonArray111.getJSONObject(i);
                            for (String key : iterate(object.keys())) {
                                list.add(key);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int j = 0; j < list.size(); j++) {

                        JSONObject jsonObject2 = jsonArray111.getJSONObject(0);
                        if (jsonObject2.has(list.get(j))) {
                            KeyValueBean bean = new KeyValueBean();
                            String dddttt = jsonObject2.getString(list.get(j));
                            bean.setValue("" + dddttt);
                            bean.setName(list.get(j));

                            farmDetailList.add(bean);
                        }

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setSubtitleLanguage() {
        SharedPreferences myPreference = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
        String languagePreference = myPreference.getString(getResources().getString(R.string.language_pref_key), "1");
        int languageConstant = Integer.parseInt(languagePreference);

        System.out.println("language Constant : " + languageConstant);
        switch (languageConstant) {
            case 1:
                setEnglishText();
                break;
            case 2:
                setGujratiText();
                break;
            case 3:
                setHindiText();
                break;
            default:
                setEnglishText();
        }


    }

    private void setGujratiText() {

        farmInfo.setText("ચેતવણી");
        noAlert.setText("કોઈ ચેતવણીઓ મળી નથી !!!");
        gObservationBTN.setText("ગ્રાઉન્ડ\nઅવલોકન");
        alertBTN.setText("ચેતવણી");
        farmInfo.setTextColor(Color.WHITE);

        moistureBTN.setText("ભેજ");
        ndviBTN.setText("એનડીવીઆઇ");
    }

    private void setEnglishText() {

        farmInfo.setText("Alert");
        noAlert.setText("No Alerts Found !!!");
        gObservationBTN.setText("Ground\nObservation");
        alertBTN.setText("Alert");
        farmInfo.setTextColor(Color.WHITE);
        moistureBTN.setText("Moisture");
        ndviBTN.setText("NDVI");

    }

    private void setHindiText() {

        farmInfo.setText("चेतावनी");
        noAlert.setText("कोई अलर्ट नहीं मिला !!!");
        moistureBTN.setText("नमी");
        gObservationBTN.setText("ग्राउंड\nनिरीक्षण");
        ndviBTN.setText("NDVI");
        farmInfo.setTextColor(Color.WHITE);
        alertBTN.setText("चेतावनी");
    }

    public void floatingPopupMethod() {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.floating_popup);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 650);

        LinearLayout forecast = (LinearLayout) dialog.findViewById(R.id.forecast_icon);
        LinearLayout weather = (LinearLayout) dialog.findViewById(R.id.weather_icon);
        LinearLayout plot_detail = (LinearLayout) dialog.findViewById(R.id.plot_detail_icon);


        forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listForecast.size() > 0) {
                    forecastPopupMethod(listForecast);
                } else {
                    Toast.makeText(getActivity(), "No forecast Data available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listTemp.size() > 0) {
                    weatherPopupMethod(listTemp);
                } else {
                    Toast.makeText(getActivity(), "No Weather Data available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        plot_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (farmDetailList.size() > 0) {
                    farmDetailPopupMethod(farmDetailList);
                } else {
                    Toast.makeText(getActivity(), "Farm Data not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }


    public void weatherPopupMethod(ArrayList<TempBean> data) {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.pop_weather);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1150);

        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.weather_list);
        listView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);

        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


        if (data.size() > 0) {


            ForecastAdapter adapter = new ForecastAdapter(getActivity(), data);
            listView.setAdapter(adapter);
        }
    }


    public void forecastPopupMethod(ArrayList<ForecastBean> data) {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.popup_forecast);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1150);

        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.forecast_list);
        listView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);


        dialog.show();


        if (data.size() > 0) {


            ForecastDashAdapter adapter = new ForecastDashAdapter(getActivity(), data);
            listView.setAdapter(adapter);
        }
    }

    public void farmDetailPopupMethod(ArrayList<KeyValueBean> data) {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);

        //  final Dialog dialog = new Dialog(getActivity());

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.farm_detail_popup);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1150);

        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.farm_detail_list);
        listView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);


        dialog.show();


        if (data.size() > 0) {


            FarmDetailAdapter adapter = new FarmDetailAdapter(getActivity(), data);
            listView.setAdapter(adapter);
        }
    }

}