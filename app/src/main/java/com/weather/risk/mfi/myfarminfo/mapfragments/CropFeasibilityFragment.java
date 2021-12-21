package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.weather.risk.mfi.myfarminfo.adapter.ShowFutureAdapter;
import com.weather.risk.mfi.myfarminfo.bean.FutureDataBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.pest_disease.ShowAdapter;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_CropFeasibilityFragment;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

/*import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;*/


public class CropFeasibilityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String STATE_ID = "stateId";


    public CropFeasibilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Spinner cropSpinner, soilSpinner;
    private String cityArr[];
    String cropId = null;
    String cropName = null;
    String soilId = null;
    String soilName = null;

    Button searchButton;

    String lat, lon, lat1, lon1;
    String stateId;

    ProgressBar avgBar, maxBar, minBar;
    TextView avgPer, maxPer, minPer;
    TextView avgYeild, maxYield, minYield;
    TextView title;
    String isType;

    LinearLayout layoutProgress;
    Button showBTN;
    ArrayList<FutureDataBean> futureArray;

    String UID = "";
    DBAdapter db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crop_feasibility, container, false);

        db = new DBAdapter(getActivity());

        cropSpinner = (Spinner) view.findViewById(R.id.feasible_crops_spinner);
        soilSpinner = (Spinner) view.findViewById(R.id.feasible_soil_spinner);
        stateId = AppConstant.stateID;

        showBTN = (Button) view.findViewById(R.id.show_data);
        showBTN.setVisibility(View.GONE);

        showBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());

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
                dialog.setContentView(R.layout.show_future_data);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                dialog.show();

                final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
                final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.showDataListView);
                TextView is_type = (TextView) dialog.findViewById(R.id.is_type);
                if (isType != null && isType.equalsIgnoreCase("Rain")) {
                    is_type.setText(getResources().getString(R.string.Rain));
                }
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                ShowFutureAdapter adapter = new ShowFutureAdapter(getActivity(), futureArray);
                recyclerView.setAdapter(adapter);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        title = (TextView) view.findViewById(R.id.title_id_future);
        layoutProgress = (LinearLayout) view.findViewById(R.id.progress_layout);
        layoutProgress.setVisibility(View.GONE);

        avgBar = (ProgressBar) view.findViewById(R.id.firstBar_avg);
        maxBar = (ProgressBar) view.findViewById(R.id.firstBar_max);
        minBar = (ProgressBar) view.findViewById(R.id.firstBar_min);


        avgBar.setMax(100);
        maxBar.setMax(100);
        minBar.setMax(100);

        avgPer = (TextView) view.findViewById(R.id.percent_avg);
        maxPer = (TextView) view.findViewById(R.id.percent_max);
        minPer = (TextView) view.findViewById(R.id.percent_min);

        avgYeild = (TextView) view.findViewById(R.id.yeild_text_avg);
        maxYield = (TextView) view.findViewById(R.id.yeild_text_max);
        minYield = (TextView) view.findViewById(R.id.yeild_text_min);


        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
        lat1 = AppConstant.latitude;
        lon1 = AppConstant.longitude;

        if (lat1 == null || lon1 == null) {
            lat = "23";
            lon = "77";
        } else {
            lat = lat1;
            lon = lon1;
        }

        searchButton = (Button) view.findViewById(R.id.feasible_submit);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soilName == null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.Pleaseselectthesoil), Toast.LENGTH_SHORT).show();
                } else if (cropName == null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.PleaseselectCrop), Toast.LENGTH_SHORT).show();
                } else {
                    getFeasibilityReport(stateId);

                }
            }
        });


        stateId = AppConstant.stateID;

        loadCropList(stateId);

        final ArrayList<String> districtList = new ArrayList<>();
        final ArrayList<String> districtID = new ArrayList<>();


        districtList.add("-Select-");
        districtList.add("Clayey");
        districtList.add("Sandy");
        districtList.add("Silty");


        districtID.add("0");
        districtID.add("Clayey");
        districtID.add("Sandy");
        districtID.add("Silty");

        ArrayAdapter<String> varietyArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, districtList); //selected item will look like a spinner set from XML
        soilSpinner.setAdapter(varietyArrayAdapter);
        soilSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    soilId = districtList.get(position);
                    soilName = districtList.get(position);
                } else {
                    soilId = null;
                    soilName = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_CropFeasibilityFragment, UID);

        return view;
    }

    public void loadCropList(String stateId) {
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(getActivity(),
                getResources().getString(R.string.LoadingVillages));
        dialoug.show();
        Log.v("knsknklanl", "https://myfarminfo.com/yfirest.svc/Feasibility/Crops/" + stateId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Feasibility/Crops/" + stateId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley crop feasibility Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");

                        DataBean bean = new DataBean();
                        bean = getEventTypeList(response);
                        ArrayList<VillageBean> cityList = new ArrayList<VillageBean>();
                        cityList = bean.getCityList();
                        cityArr = new String[cityList.size()];
                        cityArr[0] = "Select crop";
                        for (int i = 1; i < cityList.size(); i++) {
                            cityArr[i] = cityList.get(i).getVilageName();
                        }

                        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, cityArr);

                        cropSpinner.setAdapter(eventTypeAdapter);

                        final DataBean finalBean = bean;
                        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    cropId = finalBean.getCityList().get(position).getVillageID();
                                    cropName = finalBean.getCityList().get(position).getVilageName();
                                } else {
                                    cropId = null;
                                    cropName = null;
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


    private void getFeasibilityReport(String state_Id) {

        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(getActivity(),
                getResources().getString(R.string.Dataisloading));
        progressDialog.show();
        String ss = AppManager.getInstance().removeSpaceForUrl("https://myfarminfo.com//yfirest.svc/Feasibility/" + lat + "/" + lon + "/" + cropId + "/" + cropName + "/" + state_Id + "/India");

        StringRequest stringFeasibilityRequest = new StringRequest(Request.Method.GET, ss,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String cropfeasibleResponse) {
                        try {

                            cropfeasibleResponse = cropfeasibleResponse.trim();

                            cropfeasibleResponse = cropfeasibleResponse.replace("\\", "");
                            cropfeasibleResponse = cropfeasibleResponse.replace("\"{", "{");
                            cropfeasibleResponse = cropfeasibleResponse.replace("}\"", "}");
                            cropfeasibleResponse = cropfeasibleResponse.replace("\"[", "[");
                            cropfeasibleResponse = cropfeasibleResponse.replace("]\"", "]");
                            System.out.println("Feasibility Respose : " + cropfeasibleResponse);

                            JSONObject jb = new JSONObject(cropfeasibleResponse);
                            if (jb.has("ss")) {
                                JSONObject ssObject = jb.getJSONObject("ss");
                                String feasibleText = ssObject.getString("Str1");
                                String lossText = ssObject.getString("Str2");
                                title.setText(getResources().getString(R.string.Thefarmsinyourarea) + " " + feasibleText + " " + getResources().getString(R.string.forcultivation));
                            }
                            if (jb.has("lstSSS")) {
                                JSONArray sssArray = jb.getJSONArray("lstSSS");

                                layoutProgress.setVisibility(View.VISIBLE);

                                if (sssArray.length() == 3) {
                                    JSONObject jbb1 = sssArray.getJSONObject(0);
                                    JSONObject jbb2 = sssArray.getJSONObject(1);
                                    JSONObject jbb3 = sssArray.getJSONObject(2);

                                    String avg = jbb1.getString("Str2");
                                    String max = jbb2.getString("Str2");
                                    String min = jbb3.getString("Str2");

                                    String avgText = jbb1.getString("Str3");
                                    String maxText = jbb2.getString("Str3");
                                    String minText = jbb3.getString("Str3");

                                    String avg_Yeild = jbb1.getString("Str1");
                                    String max_Yeild = jbb2.getString("Str1");
                                    String min_Yeild = jbb3.getString("Str1");

                                    if (avg != null) {
                                        Double f = Double.valueOf(avg);
                                        avgBar.setProgress(f.intValue());
                                        avgPer.setText(f.intValue() + "%");
                                    }

                                    if (max != null) {
                                        Double f = Double.valueOf(max);
                                        maxBar.setProgress(f.intValue());
                                        maxPer.setText(f.intValue() + "%");
                                    }

                                    if (min != null) {
                                        Double f = Double.valueOf(min);
                                        minBar.setProgress(f.intValue());
                                        minPer.setText(f.intValue() + "%");
                                    }

                                    avgYeild.setText("Yield - " + avg_Yeild + " KG/Hectare");
                                    maxYield.setText("Year- " + maxText + "Yield - " + max_Yeild + " KG/Hectare");
                                    minYield.setText("Year- " + minText + "Yield - " + min_Yeild + " KG/Hectare");


                                }
                            }
                            if (jb.has("dataTable2")) {
                                JSONArray jsonArray = jb.getJSONArray("dataTable2");
                                futureArray = new ArrayList<FutureDataBean>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    FutureDataBean bean = new FutureDataBean();
                                    bean.setDate(jsonArray.getJSONObject(i).getString("Date"));
                                    if (jsonArray.getJSONObject(i).has("MaxTemp")) {
                                        bean.setMaxTemp(jsonArray.getJSONObject(i).getString("MaxTemp"));
                                        bean.setIsType("MaxTemp");
                                        isType = bean.getIsType();
                                    } else if (jsonArray.getJSONObject(i).has("Rain")) {
                                        bean.setMaxTemp(jsonArray.getJSONObject(i).getString("Rain"));
                                        bean.setIsType("Rain");
                                        isType = bean.getIsType();
                                    }
                                    futureArray.add(bean);
                                }

                                if (futureArray.size() > 0) {
                                    showBTN.setVisibility(View.VISIBLE);
                                } else {
                                    showBTN.setVisibility(View.GONE);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                progressDialog.cancel();

            }
        });
        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringFeasibilityRequest.setRetryPolicy(policy);

        AppController.getInstance().addToRequestQueue(stringFeasibilityRequest);
    }


    private class Crop {
        private String CSID;
        private String cropName;

        public Crop(String CSID, String cropName) {
            this.CSID = CSID;
            this.cropName = cropName;
        }

        public String getCSID() {
            return CSID;
        }

        public void setCSID(String CSID) {
            this.CSID = CSID;
        }

        public String getCropName() {
            return cropName;
        }

        public void setCropName(String cropName) {
            this.cropName = cropName;
        }
    }


    public DataBean getEventTypeList(String response) {
        DataBean dataBean = new DataBean();
        ArrayList<VillageBean> cropTypeList = new ArrayList<VillageBean>();
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    VillageBean typeBean = new VillageBean();
                    typeBean.setVilageName(jsonArray.getJSONObject(i).getString("Crop"));
                    typeBean.setVillageID(jsonArray.getJSONObject(i).getString("CSID"));
                    cropTypeList.add(typeBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataBean.setCityList(cropTypeList);
        }

        return dataBean;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
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
        setScreenTracking(getActivity(), db, SN_CropFeasibilityFragment, UID);
    }
}
