package com.weather.risk.mfi.myfarminfo.pest_disease;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.pepsico.DiseaseAdviceFragment;
import com.weather.risk.mfi.myfarminfo.pepsico.ForecastAdapter;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
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
@SuppressLint("ValidFragment")
public class ForecastPestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity";
    private static final String FARM_NAME = "FarmName";
    private static final String ALL_POINTS = "AllLatLngPount";
    private static final String AREA = "area";

    String crop_name = null;
    // TODO: Rename and change types of parameters
    private int callingActivity;
    private String selectedFarmName;
    private String area;
    String data = null;
    String d1,d2;

    String lat = null;
    String lon = null;

    @SuppressLint("ValidFragment")
    public ForecastPestFragment(String la, String lo) {
        // Required empty public constructor
        lat = la;
        lon = lo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String cropArr[];
    private String diseaseArr[];
    private String diseaseArr1[];

    DataBean bean = new DataBean();
    String cropName = null;
    String diseaseName = null;
    String cropId=null,diseaseId=null;

    RecyclerView recyclerView;
    TextView heading;
    TextView condition;

    TextView noData;
    Spinner diseaseSpinner,cropSpinner,pestDisSpinner;
    Button submitBTN;
    TextView dis;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forecast_pest_frag, container, false);




        noData = (TextView)view.findViewById(R.id.nodata);
        noData.setVisibility(View.GONE);

      /*  farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Disease Forecast");
        farmInfo.setTextColor(Color.WHITE);*/

        dis = (TextView)view.findViewById(R.id.diss);



        String aa = AppConstant.latitude;
        String bb = AppConstant.longitude;
        if (aa!=null && bb!=null){

            lat = aa;
            lon = bb;

        }
        if (lat==null){
            lat = ""+ LatLonCellID.lat;
            lon = ""+LatLonCellID.lon;
        }

        diseaseSpinner = (Spinner) view.findViewById(R.id.diseaseSpinner);
        cropSpinner = (Spinner) view.findViewById(R.id.cropSpinner);
        pestDisSpinner = (Spinner)view.findViewById(R.id.dis_pest_Spinner);

        heading = (TextView)view.findViewById(R.id.forecast_heading);
        condition = (TextView)view.findViewById(R.id.disease_condition);


        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_forcast);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);




        submitBTN = (Button)view.findViewById(R.id.get_forecast);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cropId==null){
                    Toast.makeText(getActivity(),"Please Select Crop",Toast.LENGTH_SHORT).show();
                }else if (diseaseId==null){
                    Toast.makeText(getActivity(),"Please Select Disease",Toast.LENGTH_SHORT).show();
                }else {
                    loadForecastData(cropId,diseaseId);
                }
            }
        });

        diseaseSpinner = (Spinner) view.findViewById(R.id.diseaseSpinner);
        cropSpinner = (Spinner) view.findViewById(R.id.cropSpinner);
        setSubtitleLanguage();
        loadCropData();

        return view;
    }





    @Override
    public void onResume() {
        super.onResume();
        setSubtitleLanguage();
    }

    ProgressDialog dialog;

    public void loadForecastData(String crId,String dsId) {
        dialog = ProgressDialog.show(getActivity(), "", "Fetching Forecast. Please wait...", true);

        Log.v("sjdks","https://myfarminfo.com/yfirest.svc/Disease/Forecast/2/"+lat+"/"+lon+"/"+crId+"/"+dsId);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Disease/Forecast/2/"+lat+"/"+lon+"/"+crId+"/"+dsId,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();


                        response = response.trim();
                        //    response = ""+Html.fromHtml(response);
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\"{","{");
                        response = response.replace("}\"","}");

                        response = response.replace("\"[","[");
                        response = response.replace("]\"","]");

                        System.out.println("ForecastResponse : " + response);
                        try {

                            if (response.equalsIgnoreCase("NoData")) {


                                noData.setVisibility(View.VISIBLE);
                            }else {

                                noData.setVisibility(View.GONE);
                                ArrayList<TempBean> listTemp = new ArrayList<TempBean>();
                                JSONObject jsonObject = new JSONObject(response);
                                String ss = jsonObject.getString("Str1");


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
                                JSONObject jb = jsonObject.getJSONObject("Str2");

                                JSONArray jsonArray = jb.getJSONArray("DT");

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

                                    bean.setHumidity(h.intValue() + "%");
                                    bean.setDate(date);
                                    bean.setDay(day);
                                    bean.setRain(rain +" mm");
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

                                  /*  if (jsonObject.length()>2) {
                                        String ss2 = jsonObject.getString(2);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            condition.setText(Html.fromHtml(ss2));
                                        }else {
                                            condition.setText(Html.fromHtml(ss2,Html.FROM_HTML_MODE_COMPACT));
                                        }
                                    }*/




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



    public void loadCropData() {
        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "",
                "Fetching Crops. Please wait...", true);
        Log.v("cropUrl", "https://myfarminfo.com/yfirest.svc/Disease/Crops/"+AppConstant.user_id);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Disease/Crops/"+AppConstant.user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley crop Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");


                        DataBean bean = new DataBean();
                        bean = getCropTypeList(response);
                        ArrayList<CropBean> cityList = new ArrayList<CropBean>();
                        cityList = bean.getCropList();
                        cropArr = new String[cityList.size()+1];
                        cropArr[0] = "select crop";
                        for (int i = 0; i < cityList.size(); i++) {
                            cropArr[i+1] = cityList.get(i).getCropName();

                            String na = cityList.get(i).getCropName();
                            String id = cityList.get(i).getCropId();
                            String compareValue = AppConstant.selected_cropId;
                            if (compareValue != null && compareValue.equalsIgnoreCase(id)) {
                                crop_name = na;
                            }
                        }

                        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cropArr);
                        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        cropSpinner.setAdapter(eventTypeAdapter);

                        if (crop_name != null) {
                            int spinnerPosition = eventTypeAdapter.getPosition(crop_name);
                            cropSpinner.setSelection(spinnerPosition);
                        }else {
                            noCropFound(crop_name);
                        }


                        final DataBean finalBean = bean;
                        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position>0) {
                                    cropId = finalBean.getCropList().get(position-1).getCropId();
                                    cropName = finalBean.getCropList().get(position-1).getCropName();

                                    loadDiseaseData(cropId);

                                }else {
                                    cropId = null;
                                    cropName = null;

                                    String cropId1 = AppConstant.selected_cropId;
                                    String variety = AppConstant.selected_variety;

                                    cropId = cropId1;

                                    Log.v("bestmandi_crop",cropId1+"---"+variety);

                                    if (cropId1!=null) {
                                        loadDiseaseData(cropId1);
                                    }else {
                                        Toast.makeText(getActivity(),"You did not selected any crop during farm tagging.",Toast.LENGTH_SHORT).show();
                                    }
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


    public DataBean getCropTypeList(String response) {

        DataBean dataBean = new DataBean();
        ArrayList<CropBean> cropTypeList = new ArrayList<CropBean>();
        if (response != null) {
            try {

                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {

                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    CropBean typeBean = new CropBean();
                    typeBean.setCropName(jsonArray.getJSONObject(i).getString("CropName"));
                    typeBean.setCropId(jsonArray.getJSONObject(i).getString("CropID"));
                    cropTypeList.add(typeBean);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataBean.setCropList(cropTypeList);


        }

        return dataBean;
    }


    public void loadDiseaseData(final String ID) {
        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "",
                "Fetching Disease. Please wait...", true);
        Log.v("DiseaseUrl", "https://myfarminfo.com/yfirest.svc/Disease/Cropwise/" + ID);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Disease/Cropwise/" + ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley Disease Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");


                        bean = new DataBean();
                        bean = getDiseaseTypeList(response);
                        ArrayList<DisBean> disList = new ArrayList<DisBean>();
                        disList = bean.getDiseaseList();
                        diseaseArr = new String[1];
                        diseaseArr1 = new String[2];
                        // diseaseArr[0] = "select disease";

                        for (int i = 0; i < disList.size(); i++) {
                            if (i == 0) {
                                diseaseArr[0] = disList.get(i).getDisName();
                            } else if (i == 1) {
                                diseaseArr1[0] = disList.get(i).getDisName();
                            } else if (i == 2) {
                                diseaseArr1[1] = disList.get(i).getDisName();
                            }
                        }



                        ArrayList<String> pestDistArray = new ArrayList<String>();
                        pestDistArray.add("Disease");
                        pestDistArray.add("Pest");
                        ArrayAdapter<String> pestDisAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pestDistArray);
                        pestDisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pestDisSpinner.setAdapter(pestDisAdapter);

                        pestDisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position==0){
                                    ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, diseaseArr);
                                    eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    diseaseSpinner.setAdapter(eventTypeAdapter);

                                    final DataBean finalBean = bean;
                                    diseaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (position>=0) {
                                                diseaseId = finalBean.getDiseaseList().get(position).getDisId();
                                                diseaseName = finalBean.getDiseaseList().get(position).getDisName();

                                                loadForecastData(cropId,diseaseId);
                                            }else {
                                                diseaseId = null;
                                                diseaseName = null;
                                            }


                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                }else if (position==1){
                                    ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, diseaseArr1);
                                    eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    diseaseSpinner.setAdapter(eventTypeAdapter);

                                    final DataBean finalBean = bean;
                                    diseaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (position>=0) {
                                                if (finalBean.getDiseaseList().size()>(position+1)) {
                                                    diseaseId = finalBean.getDiseaseList().get(position + 1).getDisId();
                                                    diseaseName = finalBean.getDiseaseList().get(position + 1).getDisName();

                                                    loadForecastData(cropId, diseaseId);
                                                }
                                            }else {
                                                diseaseId = null;
                                                diseaseName = null;
                                            }


                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
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


    public DataBean getDiseaseTypeList(String response) {

        DataBean dataBean = new DataBean();
        ArrayList<DisBean> disTypeList = new ArrayList<DisBean>();
        if (response != null) {
            try {

                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {

                }

                for (int i = 0; i < jsonArray.length(); i++) {


                    DisBean typeBean = new DisBean();
                    typeBean.setDisName(jsonArray.getJSONObject(i).getString("Name"));
                    typeBean.setDisId(jsonArray.getJSONObject(i).getString("DiseaseID"));
                    disTypeList.add(typeBean);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataBean.setDiseaseList(disTypeList);


        }

        return dataBean;
    }
    public void noCropFound(String crop_nam){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Data  not found for this Crop"+", Please select another crop")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void setSubtitleLanguage() {
        SharedPreferences myPreference =getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
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
            default:
                setEnglishText();
        }


    }

    private void setGujratiText() {
        dis.setText("રોગની સ્થિતિ");

    }

    private void setEnglishText() {
        dis.setText("Disease Conditions");


    }
}