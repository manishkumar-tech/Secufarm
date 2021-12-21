package com.weather.risk.mfi.myfarminfo.isconbalaji;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.weather.risk.mfi.myfarminfo.bean.OverAllBean;
import com.weather.risk.mfi.myfarminfo.bean.VisitReportBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.ConnectionDetector;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class VisitReportFragment   extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";




    public VisitReportFragment(String la,String lo) {
        latitude = la;
        longitude = lo;
        // Required empty public constructor
    }
    private static View view;
    Double lati,longi;
    String date;
    String ss = null;

    String selected_visitorID="",weekid = "";

    private String latitude, longitude;
    TextView famerName,farmName,village,subDistrict,visitorName,visitDate,visitLatitude,visitLongitude,images,distance;

    RecyclerView recyclerView;
    Spinner visitorSpinner,weekSpinner;
    Button submitBTN;
    TextView noData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        view = (LinearLayout) inflater.inflate(R.layout.visit_report_frag, container, false);



        noData = (TextView)view.findViewById(R.id.nodata);

        famerName = (TextView)view.findViewById(R.id.farmer_name_visit);
        farmName= (TextView)view.findViewById(R.id.farm_name_visit);
        village= (TextView)view.findViewById(R.id.village_visit);
        subDistrict= (TextView)view.findViewById(R.id.sub_district_visit);
        visitorName= (TextView)view.findViewById(R.id.visitor_name_visit);
        visitDate= (TextView)view.findViewById(R.id.visit_date_visit);
        visitLatitude= (TextView)view.findViewById(R.id.visit_latitude_visit);
        visitLongitude= (TextView)view.findViewById(R.id.visit_longitude_visit);
        images= (TextView)view.findViewById(R.id.image_visit);
        distance= (TextView)view.findViewById(R.id.distance_farm_visit);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_farmvisit);
        visitorSpinner = (Spinner)view.findViewById(R.id.VisitorSpinner);
        weekSpinner = (Spinner)view.findViewById(R.id.weekSpinner);
        submitBTN = (Button)view.findViewById(R.id.submit_btn);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);


        ss = AppConstant.role;

        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if (connectionDetector.isConnectingToInternet()) {


            String acc = AppConstant.user_id;
            if (acc != null) {
                loadSpinnerData(acc);
            } else {
                Toast.makeText(getActivity(), "Account id not found please login once again", Toast.LENGTH_SHORT).show();
            }
        }

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_visitorID!=null && selected_visitorID.length()>0){
                    if (weekid!=null && weekid.length()>0){
                        loadFarmData(selected_visitorID,weekid);
                    }
                }
            }
        });

        setSubtitleLanguage();
        return view;
    }

    public void loadSpinnerData(String userI) {

        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "", "Fetching Districts. Please wait...", true);
        Log.v("knsknklanl", "https://myfarminfo.com/yfirest.svc/Clients/GetAgronomistByuserID/" + userI);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GetAgronomistByuserID/" + userI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley village Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");

                        final ArrayList<String> visitorList = new ArrayList<>();
                        final ArrayList<String> visitorIdList = new ArrayList<>();

                        final ArrayList<String> weekList = new ArrayList<>();
                        final ArrayList<String> weekIdList = new ArrayList<>();


                        if (ss != null && ss.equalsIgnoreCase("Admin")) {
                            visitorList.add("Select Visitor");
                            visitorIdList.add("0");
                            weekList.add("Select Week");
                            weekIdList.add("0");
                        }
                        if (response != null) {
                            try {

                                JSONObject jb = new JSONObject(response);
                                JSONArray jsonArray = jb.getJSONArray("DT");

                                for (int i = 0; i < jsonArray.length(); i++) {


                                    String name = jsonArray.getJSONObject(i).getString("VisibleName");
                                    String id = jsonArray.getJSONObject(i).getString("UserID");
                                    visitorList.add(name);
                                    visitorIdList.add(id);

                                }

                                JSONArray jsonArray1 = jb.getJSONArray("DT4");

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    String name = jsonArray1.getJSONObject(i).getString("Weeks");
                                    String id = jsonArray1.getJSONObject(i).getString("StartDate");
                                    weekList.add(name);
                                    weekIdList.add(id);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> visitorAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, visitorList); //selected item will look like a spinner set from XML
                        visitorAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        visitorSpinner.setAdapter(visitorAdapter1);


                        visitorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                if (ss != null && ss.equalsIgnoreCase("Admin")) {
                                    if (position > 0) {
                                        //    districtSpinner.setSelection(position);
                                        selected_visitorID = visitorIdList.get(position);

                                    } else {
                                        selected_visitorID = "";
                                    }
                                } else {
                                    if (position >= 0) {
                                        selected_visitorID = visitorIdList.get(position);
                                    } else {
                                        selected_visitorID = "";
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });



                        ArrayAdapter<String> weekAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, weekList); //selected item will look like a spinner set from XML
                        weekAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        weekSpinner.setAdapter(weekAdapter1);


                        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                if (ss != null && ss.equalsIgnoreCase("Admin")) {
                                    if (position > 0) {
                                        //    districtSpinner.setSelection(position);
                                        weekid = weekIdList.get(position);





                                    } else {
                                        weekid = "";
                                    }
                                } else {
                                    if (position >= 0) {

                                        weekid = weekIdList.get(position);


                                    } else {
                                        weekid = "";
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






    public void loadFarmData(String userIddd, String weekidd) {
        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "", "Fetching Villages. Please wait...", true);
        Log.v("knsknklanl", "https://myfarminfo.com/yfirest.svc/Clients/GetAgroVisitLogs/" + userIddd + "/" + weekidd);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GetAgroVisitLogs/" + userIddd + "/" + weekidd,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley village Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");

                        if (response != null) {
                            try {

                                JSONObject jb = new JSONObject(response);


                                ArrayList<VisitReportBean> reportList = new ArrayList<VisitReportBean>();

                                JSONArray jsonArray1 = jb.getJSONArray("DT4");

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    VisitReportBean bean = new VisitReportBean();

                                    bean.setFarmername(jsonArray1.getJSONObject(i).getString("farmername"));
                                    bean.setFarmname(jsonArray1.getJSONObject(i).getString("farmname"));
                                    bean.setFarmLatLon(jsonArray1.getJSONObject(i).getString("FarmLatLon"));
                                    bean.setVillage(jsonArray1.getJSONObject(i).getString("Village"));
                                    bean.setSub_district(jsonArray1.getJSONObject(i).getString("sub_district"));
                                    bean.setVisitor_Name(jsonArray1.getJSONObject(i).getString("Visitor_Name"));
                                    bean.setVisit_Date(jsonArray1.getJSONObject(i).getString("Visit_Date"));
                                    bean.setVisit_Latitude(jsonArray1.getJSONObject(i).getString("Visit Latitude"));
                                    bean.setVisit_Longitude(jsonArray1.getJSONObject(i).getString("Visit Longitude"));
                                    bean.setDisFromFarm(jsonArray1.getJSONObject(i).getString("DisFromFarm(Km)"));
                                    bean.setImages(jsonArray1.getJSONObject(i).getString("Images"));

                                    reportList.add(bean);
                                }

                                if (reportList.size()>0){
                                    recyclerView.setVisibility(View.VISIBLE);
                                    VisitReportAdapter adapter = new VisitReportAdapter(getActivity(), reportList);
                                    recyclerView.setAdapter(adapter);
                                    noData.setVisibility(View.GONE);

                                }else {
                                    recyclerView.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

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



        submitBTN.setText("સબમિટ કરો");
    }

    private void setEnglishText() {


        submitBTN.setText("Submit");
    }

    private void setHindiText() {

        submitBTN.setText("जमा करें");

    }



}
