package com.weather.risk.mfi.myfarminfo.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.itextpdf.text.Element;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.adapter.FarmDetailAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.SoilInfoAapter;
import com.weather.risk.mfi.myfarminfo.bean.KeyValueBean;
import com.weather.risk.mfi.myfarminfo.bean.SoilInfoBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.mapfragments.NewMoistureFragment;
import com.weather.risk.mfi.myfarminfo.pest_disease.VulnerabilityBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_SoilDoctor;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.checkmobileno;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getdate;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

/**
 * Created by Admin on 17-03-2018.
 */
public class SoilDoctor extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity";
    private static final String FARM_NAME = "FarmName";
    private static final String ALL_POINTS = "AllLatLngPount";
    private static final String AREA = "area";
    String data = null;
    String d1, d2;
    String selected_period = null;
    String selected_year = null;
    LinearLayout soilLayout;
    ImageView soilImage;
    String nextResponseSoil = null;
    ArrayList<VulnerabilityBean> dataList = new ArrayList<VulnerabilityBean>();
    ProgressBar cationExchangebar, claySoilbar, soilPhbar, sandSoilbar, bulkDensitybar, coarseFragmentbar, soilOrganicbar, siltSoilbar, zinc_deficiency_bar;
    Button nextBTN;
    String lat, lon;
    LinearLayout parent;
    LinearLayout topLay;
    //    ArrayList<SoilInfoBean> soilInfoList = new ArrayList<SoilInfoBean>();
    ArrayList<HashMap<String, String>> soilInfoList = new ArrayList<>();
    LinearLayout cationLay, clayLay, phLay, sandLay, bulLay, coarseLay, soilOLay, siltLay, zincLay;
    RecyclerView soilReportRecycler;
    RecyclerView recyclerView;
    LinearLayout fillSoilSection;
    ArrayList<KeyValueBean> soilReportList = new ArrayList<KeyValueBean>();
    //    ProgressDialog dialog;
    TransparentProgressDialog dialog;
    // TODO: Rename and change types of parameters
    private int callingActivity;
    private String selectedFarmName;
    private String area;

    DBAdapter db;
    String UID = "";
    TextView txt_Date, txt_Value, soilValue, soilDate;
    TextView txt_CATIONEXCHANGECAPACITY, txt_CLAYINSOIL, txt_SOILPHCM, txt_SANDINSOIL, txt_BULKDENSITY, txt_COARSEFRAGMENTS, txt_SOILORGANICCARBON, txt_SILTINSOIL,
            txt_ZINCDEFICIENCY;
    TextView cationExchangeValue, claySoilValue, soilPhValue, sandSoilValue, bulkDensityValue, coarseFragmentValue, soilOrganicValue, siltSoilValue, zinc_deficiency_value;
    TextView noData;
    EditText name, phone, email, address, messagetxt;
    Button sendMsgBtn;

    TextView txt_SoilDoctor, txt_plus, txt_Indiafirstondemand, txt_FarmLevelData, txt_Enteryourcontactdetails, soilInfoTitle, txt_VillageLevelData;
    private long mRequestStartTime;

    public SoilDoctor() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.soil_doctor, container, false);

        setIdDefine(view);


        db = new DBAdapter(getActivity());

        soilReportRecycler.setVisibility(View.GONE);
        fillSoilSection.setVisibility(View.GONE);
        soilLayout.setVisibility(View.GONE);
        soilInfoList = new ArrayList<>();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = name.getText().toString().trim();
                String emai = email.getText().toString().trim();
                String ph = phone.getText().toString().trim();
                String msg = messagetxt.getText().toString().trim();
                String add = address.getText().toString().trim();
                if (nam == null || nam.length() < 2) {
                    getDynamicLanguageToast(getActivity(), "EnteryourName", R.string.EnteryourName);
                } else if (ph == null || ph.length() < 0 || !checkmobileno(ph)) {
                    getDynamicLanguageToast(getActivity(), "Pleaseentervalid", R.string.Pleaseentervalid);
                } else if (emai == null || emai.length() < 7 || !emai.contains("@") ||
                        (!emai.contains(".in") && !emai.contains(".com"))) {
                    getDynamicLanguageToast(getActivity(), "Enteryouremailid", R.string.Enteryouremailid);
                } else if (add == null || add.length() < 3) {
                    getDynamicLanguageToast(getActivity(), "Pleaseenteraddress", R.string.Pleaseenteraddress);
                } else if (msg == null || msg.length() < 1) {
                    getDynamicLanguageToast(getActivity(), "Pleaseentermessage", R.string.Pleaseentermessage);
                } else {
                    String creatString = creatStringForSoilDoctor();
                    new sendMessageSoilDoctor(creatString).execute();
                }
            }
        });


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

        cationLay.setVisibility(View.GONE);
        clayLay.setVisibility(View.GONE);
        phLay.setVisibility(View.GONE);
        sandLay.setVisibility(View.GONE);
        bulLay.setVisibility(View.GONE);
        coarseLay.setVisibility(View.GONE);
        soilOLay.setVisibility(View.GONE);
        siltLay.setVisibility(View.GONE);
        zincLay.setVisibility(View.GONE);


        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Fragment fragment = new SoilMapInfo();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("soil_info").commit();*/
            }
        });


        claySoilbar.setMax(100);
        soilPhbar.setMax(10);
        sandSoilbar.setMax(100);
        bulkDensitybar.setMax(2000);
        coarseFragmentbar.setMax(100);
        soilOrganicbar.setMax(1000);
        siltSoilbar.setMax(100);
        zinc_deficiency_bar.setMax(100);

        if (lat != null && lat.length() > 4) {
            loadSoilInfoData();
        } else {
            getDynamicLanguageToast(getActivity(), "GPSnotfound", R.string.GPSnotfound);
        }

        topLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NewMoistureFragment(nextResponseSoil);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }
        });

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_SoilDoctor, UID);

        return view;
    }

    public static String sendMessages(String data) {
        String completeStringForMessage = "";
        String message = "";
        completeStringForMessage = data;
        String response = null;
        try {
            //   response = CustomHttpClient.executeHttpPut("https://myfarminfo.com/yfirest.svc/registerUser_Jalna/" + completeStringForRegister);
            response = AppManager.getInstance().httpRequestPutMethod("https://myfarminfo.com/yfirest.svc/saveRequest", completeStringForMessage);

            Log.d("RegistrationData", response);
            if (response.contains("Error")) {
                message = "Error";
            } else if (response.contains("Success")) {
                message = "OK";
            } else {
                message = "No Registered";
            }
            return message;
        } catch (Exception e) {//
            e.printStackTrace();
            Log.d("Status", "" + e);
            return "NoResponse";
        }

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String creatStringForSoilDoctor() {


        String parameterString = "";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Name", name.getText().toString().trim());
            jsonObject.put("Address", address.getText().toString().trim());
            jsonObject.put("EMail", email.getText().toString().trim());
            jsonObject.put("PhNo", phone.getText().toString().trim());
            jsonObject.put("Message", messagetxt.getText().toString().trim());
            jsonObject.put("Module", "SoilDoctor");
            jsonObject.put("RequestDate", getdate());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        Log.v("registerString", parameterString + "");

        return parameterString;
    }

    String getSoilInfoURL;

    public void loadSoilInfoData() {

        noData.setVisibility(View.GONE);
        cationLay.setVisibility(View.GONE);
        clayLay.setVisibility(View.GONE);
        phLay.setVisibility(View.GONE);
        sandLay.setVisibility(View.GONE);
        bulLay.setVisibility(View.GONE);
        coarseLay.setVisibility(View.GONE);
        soilOLay.setVisibility(View.GONE);
        siltLay.setVisibility(View.GONE);
        zincLay.setVisibility(View.GONE);
        dialog = new TransparentProgressDialog(getActivity(),
                getDynamicLanguageValue(getActivity(), "Dataisloading", R.string.Dataisloading));
        dialog.show();

        Log.v("url_Soilinfo", "https://myfarminfo.com/yfirest.svc/Soil/Info/" + lat + "/" + lon + "/" + AppConstant.farm_id);//farm_id=53017

        String language = AppManager.getInstance().getSelectedLanguages(getActivity());
        getSoilInfoURL = "https://myfarminfo.com/yfirest.svc/Soil/Info/" + lat + "/" + lon + "/" + AppConstant.farm_id + "/" + language;
//        String URL = "https://myfarminfo.com/yfirest.svc/Soil/Info/" + lat + "/" + lon + "/" + AppConstant.farm_id;
        mRequestStartTime = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getSoilInfoURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        // Display the first 500 characters of the response string.

                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        if (seconds > 3) {
                            SaveLocalFile(db, getActivity(), "SoilDoctor", getSoilInfoURL, response, "", "" + seconds, AppConstant.farm_id, "Working");
                        }
                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");
                        System.out.println("SoilInfo Response : " + response);
                        try {

                            if (response.equalsIgnoreCase("Stations not found")) {
                                fillSoilSection.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.VISIBLE);
                                parent.setVisibility(View.GONE);
                            } else {
                                fillSoilSection.setVisibility(View.GONE);
                                noData.setVisibility(View.GONE);
                                parent.setVisibility(View.VISIBLE);

                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("DT");
                                if (jsonArray.length() > 0) {
                                    String aa = jsonArray.getJSONObject(0).getString("Name");
                                    String bb = jsonArray.getJSONObject(0).getString("Description");
                                    soilInfoTitle.setText(getResources().getString(R.string.Primarysoiltexture) + " " + aa + " - " + bb);
                                }

                                soilReportList = new ArrayList<KeyValueBean>();
                                if (jsonObject.has("DT4")) {
                                    JSONArray jsonArrayDT4 = jsonObject.getJSONArray("DT4");
                                    for (int i = 0; i < jsonArrayDT4.length(); i++) {
                                        Iterator iterator = jsonArrayDT4.getJSONObject(i).keys();
                                        while (iterator.hasNext()) {
                                            String key = (String) iterator.next();
                                            String valueee = jsonArrayDT4.getJSONObject(i).getString(key);
                                            if (valueee != null && !valueee.equalsIgnoreCase("null")) {
                                                KeyValueBean bean = new KeyValueBean();
                                                bean.setName(key);
                                                bean.setValue(valueee);
                                                soilReportList.add(bean);
                                            }
                                        }
                                    }
                                }

                                if (soilReportList.size() > 0) {
                                    fillSoilSection.setVisibility(View.GONE);
                                    soilReportRecycler.setVisibility(View.VISIBLE);
                                    soilReportRecycler.setHasFixedSize(true);
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    soilReportRecycler.setLayoutManager(mLayoutManager);

                                    FarmDetailAdapter adapter = new FarmDetailAdapter(getActivity(), soilReportList);
                                    soilReportRecycler.setAdapter(adapter);
                                } else {//Herojit
                                    fillSoilSection.setVisibility(View.VISIBLE);
                                    soilReportRecycler.setVisibility(View.GONE);
                                }

                                if (jsonObject.has("DT6")) {
                                    JSONArray jsonArrayDT666 = jsonObject.getJSONArray("DT6");
                                    soilInfoList = new ArrayList<>();

                                    if (jsonArrayDT666.getJSONObject(0).has("ph_aggregate")) {
                                        JSONArray ph_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("ph_aggregate");
                                        if (ph_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(ph_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.PH));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("ec_aggregate")) {
                                        JSONArray ec_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("ec_aggregate");
                                        if (ec_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(ec_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Electricalconductivity));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("oc_aggregate")) {
                                        JSONArray oc_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("oc_aggregate");
                                        if (oc_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(oc_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.OrganicCarbon));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("n_aggregate")) {
                                        JSONArray n_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("n_aggregate");
                                        if (n_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(n_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Nitrogen));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("p_aggregate")) {
                                        JSONArray p_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("p_aggregate");
                                        if (p_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(p_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Phosphorus));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("k_aggregate")) {
                                        JSONArray k_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("k_aggregate");
                                        if (k_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(k_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Potassium));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("s_aggregate")) {
                                        JSONArray s_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("s_aggregate");
                                        if (s_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(s_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Sulphur));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("zn_aggregate")) {
                                        JSONArray zn_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("zn_aggregate");
                                        if (zn_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(zn_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Zinc));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("fe_aggregate")) {
                                        JSONArray fe_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("fe_aggregate");
                                        if (fe_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(fe_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Iron));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }

                                    if (jsonArrayDT666.getJSONObject(0).has("cu_aggregate")) {
                                        JSONArray cu_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("cu_aggregate");
                                        if (cu_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(cu_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Copper));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }
                                    if (jsonArrayDT666.getJSONObject(0).has("mn_aggregate")) {
                                        JSONArray mn_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("mn_aggregate");
                                        if (mn_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(mn_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Manganese));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }
                                    if (jsonArrayDT666.getJSONObject(0).has("b_aggregate")) {
                                        JSONArray b_aggregate = jsonArrayDT666.getJSONObject(0).getJSONArray("b_aggregate");
                                        if (b_aggregate.length() > 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap = getdyanamicJson(b_aggregate);
                                            if (hashMap.size() > 0) {
                                                hashMap.put("Heading", getResources().getString(R.string.Boron));
                                                soilInfoList.add(hashMap);
                                            }
                                        }
                                    }
                                }


                                JSONArray jsonArray1 = jsonObject.getJSONArray("DTChartDesc");
                                if (jsonArray1.length() > 0) {
                                    String bulk = jsonArray1.getJSONObject(0).getString("BulkD");
                                    String cat = jsonArray1.getJSONObject(0).getString("CatEC");
                                    String clay = jsonArray1.getJSONObject(0).getString("Clay");
                                    String coarseF = jsonArray1.getJSONObject(0).getString("CoarseF");
                                    String ph = jsonArray1.getJSONObject(0).getString("PH");
                                    String sand = jsonArray1.getJSONObject(0).getString("Sand");
                                    String silt = jsonArray1.getJSONObject(0).getString("Silt");
                                    String soilCarbon = jsonArray1.getJSONObject(0).getString("SoilCarbon");

                                    String zincPerc = jsonArray1.getJSONObject(0).getString("ZincPerc");
                                    String ironPerc = jsonArray1.getJSONObject(0).getString("IronPerc");


                                    if (bulk != null && !bulk.equalsIgnoreCase("null")) {
                                        bulLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(bulk);
                                        bulkDensitybar.setProgress(f.intValue());
                                        bulkDensityValue.setText(bulk + "kg/m³");
                                    }

                                    if (cat != null && !cat.equalsIgnoreCase("null")) {
                                        cationLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(cat);
                                        //  cationExchangebar.setProgress(f.intValue());
                                        cationExchangeValue.setText(cat + "cm olc/kg");
                                    }

                                    if (clay != null && !clay.equalsIgnoreCase("null")) {
                                        clayLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(clay);
                                        claySoilbar.setProgress(f.intValue());
                                        claySoilValue.setText(clay + "%");
                                    }

                                    if (coarseF != null && !coarseF.equalsIgnoreCase("null")) {
                                        coarseLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(coarseF);
                                        coarseFragmentbar.setProgress(f.intValue());
                                        coarseFragmentValue.setText(coarseF + "%");
                                    }

                                    if (ph != null && !ph.equalsIgnoreCase("null")) {
                                        phLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(ph);
                                        soilPhbar.setProgress(f.intValue());
                                        soilPhValue.setText(ph);
                                    }

                                    if (sand != null && !sand.equalsIgnoreCase("null")) {
                                        sandLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(sand);
                                        sandSoilbar.setProgress(f.intValue());
                                        sandSoilValue.setText(sand + "%");
                                    }

                                    if (silt != null && !silt.equalsIgnoreCase("null")) {
                                        siltLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(silt);
                                        siltSoilbar.setProgress(f.intValue());
                                        siltSoilValue.setText(silt + "%");
                                    }

                                    if (soilCarbon != null && !soilCarbon.equalsIgnoreCase("null")) {
                                        soilOLay.setVisibility(View.VISIBLE);
                                        Double f = Double.valueOf(soilCarbon);
                                        soilOrganicbar.setProgress(f.intValue());
                                        soilOrganicValue.setText(soilCarbon + "g/Kg");
                                    }

                                    if (zincPerc != null && !zincPerc.equalsIgnoreCase("null")) {
                                        zincLay.setVisibility(View.VISIBLE);
                                        //  Double f = Double.valueOf(zincPerc);
                                        //   zinc_deficiency_bar.setProgress(f.intValue());
                                        zinc_deficiency_value.setText(zincPerc + "");
                                    }
                                }


                                if (soilInfoList.size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    SoilInfoAapter adapter = new SoilInfoAapter(getActivity(), soilInfoList);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            SaveLocalFile(db, getActivity(), "SoilDoctor", getSoilInfoURL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                        } catch (Exception e) {
                            e.printStackTrace();
                            SaveLocalFile(db, getActivity(), "SoilDoctor", getSoilInfoURL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                System.out.println("Volley Error : " + error);
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, getActivity(), "SoilDoctor", getSoilInfoURL, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");

                getDynamicLanguageToast(getActivity(), "Nodataavailable", R.string.Nodataavailable);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private class sendMessageSoilDoctor extends AsyncTask<Void, Void, String> {


        String result = null;
        String createdString;

        public sendMessageSoilDoctor(String createdString) {
            this.createdString = createdString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            result = sendMessages(AppManager.getInstance().removeSpaceForUrl(createdString));
            Log.d("result-----", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            if (result != null) {
                if (result.contains("OK")) {
                    getDynamicLanguageToast(getActivity(), "SubmittedSuccessfully", R.string.SubmittedSuccessfully);
                    blankedAfterSave();
                } else if (result.contains("Error")) {
                    getDynamicLanguageToast(getActivity(), "Couldnotconnect", R.string.Couldnotconnect);
                    return;
                } else {
                    getDynamicLanguageToast(getActivity(), "Couldnotconnect", R.string.Couldnotconnect);
                }
            } else {
                getDynamicLanguageToast(getActivity(), "Couldnotconnect", R.string.Couldnotconnect);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        setLanguages();
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
        setScreenTracking(getActivity(), db, SN_SoilDoctor, UID);
    }


    private HashMap<String, String> getdyanamicJson(JSONArray jsonArray) {
        HashMap<String, String> value = new HashMap<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String val = String.valueOf(json.get(key));
                    float f = Float.parseFloat(val);
                    if (f > 0) {
                        value.put(key, val);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    private void setIdDefine(View view) {

        txt_SoilDoctor = (TextView) view.findViewById(R.id.txt_SoilDoctor);
        txt_plus = (TextView) view.findViewById(R.id.txt_plus);
        txt_Indiafirstondemand = (TextView) view.findViewById(R.id.txt_Indiafirstondemand);
        txt_FarmLevelData = (TextView) view.findViewById(R.id.txt_FarmLevelData);
        txt_Enteryourcontactdetails = (TextView) view.findViewById(R.id.txt_Enteryourcontactdetails);
        soilInfoTitle = (TextView) view.findViewById(R.id.soil_info_title);
        txt_VillageLevelData = (TextView) view.findViewById(R.id.txt_VillageLevelData);

        txt_CATIONEXCHANGECAPACITY = (TextView) view.findViewById(R.id.txt_CATIONEXCHANGECAPACITY);
        txt_CLAYINSOIL = (TextView) view.findViewById(R.id.txt_CLAYINSOIL);
        txt_SOILPHCM = (TextView) view.findViewById(R.id.txt_SOILPHCM);
        txt_SANDINSOIL = (TextView) view.findViewById(R.id.txt_SANDINSOIL);
        txt_BULKDENSITY = (TextView) view.findViewById(R.id.txt_BULKDENSITY);
        txt_COARSEFRAGMENTS = (TextView) view.findViewById(R.id.txt_COARSEFRAGMENTS);
        txt_SOILORGANICCARBON = (TextView) view.findViewById(R.id.txt_SOILORGANICCARBON);
        txt_SILTINSOIL = (TextView) view.findViewById(R.id.txt_SILTINSOIL);
        txt_ZINCDEFICIENCY = (TextView) view.findViewById(R.id.txt_ZINCDEFICIENCY);

        cationExchangeValue = (TextView) view.findViewById(R.id.exchange_capacity_value);
        claySoilValue = (TextView) view.findViewById(R.id.clay_perc_value);
        soilPhValue = (TextView) view.findViewById(R.id.soil_ph_value);
        sandSoilValue = (TextView) view.findViewById(R.id.sand_soil_value);
        bulkDensityValue = (TextView) view.findViewById(R.id.bulk_density_value);
        coarseFragmentValue = (TextView) view.findViewById(R.id.coarse_frag_value);
        soilOrganicValue = (TextView) view.findViewById(R.id.soil_organic_value);
        siltSoilValue = (TextView) view.findViewById(R.id.silt_soil_value);
        zinc_deficiency_value = (TextView) view.findViewById(R.id.zinc_value);


        soilReportRecycler = (RecyclerView) view.findViewById(R.id.soilReportRecycler);
        name = (EditText) view.findViewById(R.id.name_soil_doctor);
        phone = (EditText) view.findViewById(R.id.phone_soil_doctor);
        address = (EditText) view.findViewById(R.id.address_soil_doctor);
        email = (EditText) view.findViewById(R.id.email_soil_doctor);
        messagetxt = (EditText) view.findViewById(R.id.message_soil_doctor);
        topLay = (LinearLayout) view.findViewById(R.id.top_lay);
        fillSoilSection = (LinearLayout) view.findViewById(R.id.fill_soil_info_section);

        soilImage = (ImageView) view.findViewById(R.id.soil_image);
        soilDate = (TextView) view.findViewById(R.id.soil_date);
        soilValue = (TextView) view.findViewById(R.id.soil_value);
        soilLayout = (LinearLayout) view.findViewById(R.id.soil_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.soil_info_list);
        sendMsgBtn = (Button) view.findViewById(R.id.send_msg_soildoc);

        cationLay = (LinearLayout) view.findViewById(R.id.cation_lay);
        clayLay = (LinearLayout) view.findViewById(R.id.clay_lay);
        phLay = (LinearLayout) view.findViewById(R.id.ph_lay);
        sandLay = (LinearLayout) view.findViewById(R.id.sand_lay);
        bulLay = (LinearLayout) view.findViewById(R.id.bulk_lay);
        coarseLay = (LinearLayout) view.findViewById(R.id.coarse_lay);
        soilOLay = (LinearLayout) view.findViewById(R.id.soil_orgon_lay);
        siltLay = (LinearLayout) view.findViewById(R.id.silt_lay);
        zincLay = (LinearLayout) view.findViewById(R.id.zinc_lay);
        nextBTN = (Button) view.findViewById(R.id.next_soil_info);
        parent = (LinearLayout) view.findViewById(R.id.parent);
        noData = (TextView) view.findViewById(R.id.nodata);


        claySoilbar = (ProgressBar) view.findViewById(R.id.clay_per_bar);
        soilPhbar = (ProgressBar) view.findViewById(R.id.soil_ph_bar);
        sandSoilbar = (ProgressBar) view.findViewById(R.id.sand_soil_bar);
        bulkDensitybar = (ProgressBar) view.findViewById(R.id.bulk_density_bar);
        coarseFragmentbar = (ProgressBar) view.findViewById(R.id.coarse_frag_bar);
        soilOrganicbar = (ProgressBar) view.findViewById(R.id.soil_organic_bar);
        siltSoilbar = (ProgressBar) view.findViewById(R.id.silt_soil_bar);
        zinc_deficiency_bar = (ProgressBar) view.findViewById(R.id.zinc_bar);

    }


    public void blankedAfterSave() {
        name.setText("");
        email.setText("");
        phone.setText("");
        messagetxt.setText("");
        address.setText("");
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(getActivity(), txt_SoilDoctor, 4);
        setFontsStyleTxt(getActivity(), txt_plus, 4);
        setFontsStyleTxt(getActivity(), txt_Indiafirstondemand, 7);
        setFontsStyleTxt(getActivity(), txt_FarmLevelData, 4);
        setFontsStyleTxt(getActivity(), txt_Enteryourcontactdetails, 2);
        setFontsStyleTxt(getActivity(), name, 6);
        setFontsStyleTxt(getActivity(), phone, 6);
        setFontsStyleTxt(getActivity(), email, 6);
        setFontsStyleTxt(getActivity(), address, 6);
        setFontsStyleTxt(getActivity(), messagetxt, 6);
        setFontsStyleTxt(getActivity(), sendMsgBtn, 6);
        setFontsStyleTxt(getActivity(), sendMsgBtn, 7);
        setFontsStyleTxt(getActivity(), txt_VillageLevelData, 4);
        setFontsStyleTxt(getActivity(), txt_CATIONEXCHANGECAPACITY, 4);
        setFontsStyleTxt(getActivity(), txt_CLAYINSOIL, 4);
        setFontsStyleTxt(getActivity(), txt_SOILPHCM, 4);
        setFontsStyleTxt(getActivity(), txt_SANDINSOIL, 4);
        setFontsStyleTxt(getActivity(), txt_BULKDENSITY, 4);
        setFontsStyleTxt(getActivity(), txt_COARSEFRAGMENTS, 4);
        setFontsStyleTxt(getActivity(), txt_SOILORGANICCARBON, 4);
        setFontsStyleTxt(getActivity(), txt_SILTINSOIL, 4);
        setFontsStyleTxt(getActivity(), txt_ZINCDEFICIENCY, 4);
        setFontsStyleTxt(getActivity(), cationExchangeValue, 4);
        setFontsStyleTxt(getActivity(), claySoilValue, 4);
        setFontsStyleTxt(getActivity(), soilPhValue, 4);
        setFontsStyleTxt(getActivity(), sandSoilValue, 4);
        setFontsStyleTxt(getActivity(), bulkDensityValue, 4);
        setFontsStyleTxt(getActivity(), coarseFragmentValue, 4);
        setFontsStyleTxt(getActivity(), soilOrganicValue, 4);
        setFontsStyleTxt(getActivity(), siltSoilValue, 4);
        setFontsStyleTxt(getActivity(), zinc_deficiency_value, 4);

        setDynamicLanguage(getActivity(), txt_SoilDoctor, "SoilDoctor", R.string.SoilDoctor);
        setDynamicLanguage(getActivity(), txt_Indiafirstondemand, "Indiafirstondemand", R.string.Indiafirstondemand);
        setDynamicLanguage(getActivity(), txt_FarmLevelData, "FarmLevelData", R.string.FarmLevelData);
        setDynamicLanguage(getActivity(), txt_Enteryourcontactdetails, "Enteryourcontactdetails", R.string.Enteryourcontactdetails);
        setDynamicLanguage(getActivity(), txt_VillageLevelData, "VillageLevelData", R.string.VillageLevelData);
        setDynamicLanguage(getActivity(), txt_CATIONEXCHANGECAPACITY, "CATIONEXCHANGECAPACITY", R.string.CATIONEXCHANGECAPACITY);
        setDynamicLanguage(getActivity(), txt_CLAYINSOIL, "CLAYINSOIL", R.string.CLAYINSOIL);
        setDynamicLanguage(getActivity(), txt_SOILPHCM, "SOILPHCM", R.string.SOILPHCM);
        setDynamicLanguage(getActivity(), txt_SANDINSOIL, "SANDINSOIL", R.string.SANDINSOIL);
        setDynamicLanguage(getActivity(), txt_BULKDENSITY, "BULKDENSITY", R.string.BULKDENSITY);
        setDynamicLanguage(getActivity(), txt_COARSEFRAGMENTS, "COARSEFRAGMENTS", R.string.COARSEFRAGMENTS);
        setDynamicLanguage(getActivity(), txt_SOILORGANICCARBON, "SOILORGANICCARBON", R.string.SOILORGANICCARBON);
        setDynamicLanguage(getActivity(), txt_SILTINSOIL, "SILTINSOIL", R.string.SILTINSOIL);
        setDynamicLanguage(getActivity(), txt_ZINCDEFICIENCY, "ZINCDEFICIENCY", R.string.ZINCDEFICIENCY);

    }
}