package com.weather.risk.mfi.myfarminfo.pepsico;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.weather.risk.mfi.myfarminfo.dabwali.DabwaliNDVI;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.mapfragments.PepsicoMoisture;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Admin on 28-08-2017.
 *
 *
 */
public class ForecastFragment extends Fragment {
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
    String d1,d2;
    String response;

    public ForecastFragment(String resp) {
        // Required empty public constructor
        response = resp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Spinner diseaseSpinner;



    private String cityArr[];
    String villageID = null;
    String vill_id = null;
    String lat = null;
    String lon = null;
    String villageName = null;
    RecyclerView recyclerView;
   // Button nextBtn;
    String d_ID = "2";
    TextView heading;
    Spinner villageSpinner,districtSpinner;

    TextView noData;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forecast_fragment, container, false);

        String role = AppConstant.role;
        Log.v("roleeeeeelllll",role+"");

       /* if (role.equalsIgnoreCase("Admin")){
            setHasOptionsMenu(true);
        }else {

        }*/

        setHasOptionsMenu(true);



        noData = (TextView)view.findViewById(R.id.nodata);
        noData.setVisibility(View.GONE);

        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Forecast");
        farmInfo.setTextColor(Color.WHITE);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);

        lat = prefs.getString("lat",null);
        lon = prefs.getString("lon",null);
        if (lat==null){
            lat = ""+ LatLonCellID.lat;
            lon = ""+LatLonCellID.lon;
        }

        diseaseSpinner = (Spinner)view.findViewById(R.id.diseaseSpinner);

        heading = (TextView)view.findViewById(R.id.forecast_heading);


        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_forcast);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        Button moistureBTN = (Button) view.findViewById(R.id.moisture_btn);
        Button diseaseAdviceBTN = (Button) view.findViewById(R.id.disease_advice_btn);
        Button forcastBTN = (Button) view.findViewById(R.id.forecast_btn);
        Button ndviBTN = (Button) view.findViewById(R.id.ndvi_btn);

        moistureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response != null) {
                    Fragment fragment = new PepsicoMoisture(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });
        diseaseAdviceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response != null) {
                    Fragment fragment = new DiseaseAdviceFragment(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });
        forcastBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response != null) {
                   /* Fragment fragment =new ForecastFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();*/
                }
            }
        });
        ndviBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response != null) {
                    Fragment fragment = new NdviFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });



        ArrayList<String> diseaseList = new ArrayList<>();
        final ArrayList<String> diseaseIdArray = new ArrayList<>();


        diseaseList.add("Whitefly");
        diseaseList.add("Pink Bollworm");



        diseaseIdArray.add("2");
        diseaseIdArray.add("26");



        ArrayAdapter<String> varietyArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, diseaseList); //selected item will look like a spinner set from XML
        varietyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diseaseSpinner.setAdapter(varietyArrayAdapter);
        diseaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                d_ID = diseaseIdArray.get(position);

                    loadForecastData(d_ID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // loadForecastData(d_ID);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.pepsico_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            villageMethod();
            return true;
        }else if (id == R.id.action_error){
            File logFile = new File(Environment.getExternalStorageDirectory(), "MFIErrorLog.txt");
            if (logFile.exists()) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                // set the type to 'email'
                emailIntent.setType("vnd.android.cursor.dir/email");
                String to[] = {"vishal.tripathi@iembsys.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                // the attachment
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(logFile));
                // the mail subject
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MFI Error log");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "MFI app");

                if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } else {
                    Toast.makeText(getActivity(), "No email application is available to share error log file", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getActivity(), "MFI_pepsico ErrorLog file does not exist ", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void villageMethod() {

        final Dialog dialog = new Dialog(getActivity());

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
        dialog.setContentView(R.layout.village_select_popup);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        districtSpinner = (Spinner) dialog.findViewById(R.id.popup_district);
        villageSpinner = (Spinner) dialog.findViewById(R.id.popup_village);
        Button okBTN = (Button) dialog.findViewById(R.id.ok_btn);
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (villageID != null) {
                    dialog.dismiss();
                    ForecastFragment pep = new ForecastFragment(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, pep).commit();

                } else {
                    Toast.makeText(getActivity(), "please select village.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final ArrayList<String> districtList = new ArrayList<>();
        final ArrayList<String> districtID = new ArrayList<>();

        districtList.add("-Select-");
        districtList.add("BANKURA");
        districtList.add("BARDDHAMAN");
        districtList.add("BIRBHUM");
        //  districtList.add("Jagityal");
        //  districtList.add("Jalna");
        districtList.add("WEST MEDNIPUR");


        districtID.add("0");
        districtID.add("16288");
        districtID.add("16289");
        districtID.add("16290");
        // districtID.add("16321");
        // districtID.add("16032");
        districtID.add("16306");


        ArrayAdapter<String> varietyArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, districtList); //selected item will look like a spinner set from XML
        varietyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(varietyArrayAdapter);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    //    districtSpinner.setSelection(position);

                    String ID = districtID.get(position);
                    villageID = null;
                    vill_id = null;
                    lat = null;
                    lon = null;

                    loadVillagesData(ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    public void loadVillagesData(String ID) {
        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "",
                "Fetching Villages. Please wait...", true);
        Log.v("knsknklanl","https://myfarminfo.com/yfirest.svc/Clients/GGRC/Villages/"+ID);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GGRC/Villages/"+ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley village Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");



                        DataBean bean = new DataBean();
                        bean = getEventTypeList( response);
                        ArrayList<VillageBean> cityList = new ArrayList<VillageBean>();
                        cityList = bean.getCityList();
                        cityArr = new String[cityList.size()];
                        for (int i = 0; i < cityList.size(); i++) {
                            cityArr[i] = cityList.get(i).getVilageName();
                        }

                        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cityArr);
                        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        villageSpinner.setAdapter(eventTypeAdapter);

                        final DataBean finalBean = bean;
                        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                villageID = finalBean.getCityList().get(position).getVillageID();
                                villageName = finalBean.getCityList().get(position).getVilageName();

                                Log.v("ksjkls",villageID);

                                if (villageID!=null){

                                    String[] parts = villageID.split("-");
                                    vill_id = parts[0];
                                    lat = parts[1];
                                    lon = parts[2];

                                    SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
                                    SharedPreferences.Editor editor  = prefs.edit();
                                    editor.putString("lat",lat);
                                    editor.putString("lon",lon);
                                    editor.putString("villageId",villageID);
                                    editor.putString("villageName",villageName);
                                    editor.apply();


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug.cancel();
                System.out.println("Volley Error : " + error);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public DataBean getEventTypeList(String response) {

        DataBean dataBean = new DataBean();
        ArrayList<VillageBean> eventTypeList = new ArrayList<VillageBean>();
        if (response != null){
            try{

                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {

                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    VillageBean typeBean = new VillageBean();
                    typeBean.setVilageName(jsonArray.getJSONObject(i).getString("Name"));
                    typeBean.setVillageID(jsonArray.getJSONObject(i).getString("ID"));
                    eventTypeList.add(typeBean);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            dataBean.setCityList(eventTypeList);



        }

        return dataBean;
    }





    @Override
    public void onResume() {
        super.onResume();
        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Forecast");
        farmInfo.setTextColor(Color.WHITE);
    }

    ProgressDialog dialog;

    public void loadForecastData(String ID) {
        dialog = ProgressDialog.show(getActivity(), "", "Fetching Forecast. Please wait...", true);

        Log.v("sjdks","https://myfarminfo.com/yfirest.svc/Disease/Forecast/2/"+lat+"/"+lon+"/12"+"/"+ID+"/0");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Disease/Forecast/2/"+lat+"/"+lon+"/12"+"/"+ID+"/0",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();


                        // Display the first 500 characters of the response string.


                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\"{","{");
                        response = response.replace("}\"","}");

                        response = response.replace("\"[","[");
                        response = response.replace("]\"","]");

                        System.out.println("ForecastResponse : " + response);
                        try {

                            if (response.equalsIgnoreCase("InsufData")) {


                                noData.setVisibility(View.VISIBLE);
                            }else {

                                noData.setVisibility(View.GONE);
                                ArrayList<TempBean> listTemp = new ArrayList<TempBean>();
                                JSONArray jsonObject = new JSONArray(response);
                                String ss = jsonObject.getString(0);

                               /* JSONArray str1 = jsonObject.getJSONArray("Str1");

                                for (int j =0;j<str1.length();j++){
                                    if (j==0){
                                        d1 = str1.get(0).toString();
                                    }

                                    if (j==1){
                                        d2 = str1.get(1).toString();
                                    }
                                }
                                heading.setText("Disease predicted on "+d1+" on "+d2);*/
                                heading.setText(ss);
                                JSONObject jsonObject1 = jsonObject.getJSONObject(1);

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
                                    bean.setDate(date);
                                    bean.setDay(day);
                                    bean.setMaxTemp(mx.intValue() + "\u00B0");
                                    bean.setMinTemp(min.intValue() + "\u00B0");
                                    bean.setWindSpeed(sp.intValue() + " m/s");

                                    weatherCondition = weatherCondition.replace("intermittent spell of rains", "showers");
                                    bean.setWeatherText(weatherCondition);

                                    int rn = 0;
                                    if (rain != null) {

                                        Double d = Double.valueOf(rain);
                                        rn = Integer.valueOf(d.intValue());
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

                                if (listTemp.size() > 0) {
                                    ForecastAdapter adapter = new ForecastAdapter(getActivity(), listTemp);
                                    recyclerView.setAdapter(adapter);

                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                System.out.println("Volley Error : " + error);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}