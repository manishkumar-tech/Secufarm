package com.weather.risk.mfi.myfarminfo.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.PackageBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.package_practices.PackageAdapter;
import com.weather.risk.mfi.myfarminfo.pest_disease.CropBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_PackagePractices;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getCropName;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

/**
 * Created by Admin on 25-04-2018.
 */
public class PackagePractices extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity";
    private static final String FARM_NAME = "FarmName";
    private static final String ALL_POINTS = "AllLatLngPount";
    private static final String AREA = "area";
    RecyclerView recyclerView;
    ArrayList<String> workId = new ArrayList<String>();
    ArrayList<String> statusList = new ArrayList<String>();


    TransparentProgressDialog dialog11;
    TransparentProgressDialog dialog;
    //Herojit Add
    // TODO: Rename and change types of parameters
    private int callingActivity;
    private String selectedFarmName;
    private String area;

    String CropSelectedID = null, cropName = null, noData = "0", SowDate = "", OnlyCurrentStatus = "";
    String UID = "";
    DBAdapter db;
    TextView txt_Stages, txt_nodata;
    private long mRequestStartTime;

    public PackagePractices() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.package_parctices, container, false);

        txt_Stages = (TextView) view.findViewById(R.id.txt_Stages);
        txt_nodata = (TextView) view.findViewById(R.id.txt_nodata);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_package);


        setFontsStyleTxt(getActivity(), txt_Stages, 5);
        setFontsStyleTxt(getActivity(), txt_nodata, 5);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        db = new DBAdapter(getActivity());

        //Herojit Add
        SowDate = getArguments().getString("SowDate");
        OnlyCurrentStatus = getArguments().getString("OnlyCurrentStatus");
        getloadCropData();

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_PackagePractices, UID);

        return view;
    }

    String getPOPURL;

    public void popStatusMethod() {
        noData = "0";
        dialog11 = new TransparentProgressDialog(getActivity(),
                getDynamicLanguageValue(getActivity(), "Dataisloading", R.string.Dataisloading));
        dialog11.show();

        Log.v("sjdks", "https://myfarminfo.com/yfirest.svc/Clients/GetFarmPop" + "/" + AppConstant.farm_id);
//        String URL = "https://myfarminfo.com/yfirest.svc/Clients/GetFarmPop/" + AppConstant.farm_id;
        getPOPURL = "https://myfarminfo.com/yfirest.svc/Clients/GetFarmPop/" + AppConstant.user_id;

        mRequestStartTime = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getPOPURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        if (seconds > 3) {
                            SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPURL, response, "", "" + seconds, AppConstant.farm_id, "Working");
                        }
                        dialog11.cancel();
                        response = response.trim();
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");

                        System.out.println("Package_Response : " + response);
                        try {

                            if (response == null || response.equalsIgnoreCase("Error")) {
                                getDynamicLanguageToast(getActivity(), "Somthingwentwrongapi", R.string.Somthingwentwrongapi);
                            } else if (response.equalsIgnoreCase("\"No Data\"")) {

                                workId = new ArrayList<String>();
                                statusList = new ArrayList<String>();
                            } else {

                                workId = new ArrayList<String>();
                                statusList = new ArrayList<String>();
                                JSONArray ja = new JSONArray(response);
                                for (int i = 0; i < ja.length(); i++) {
                                    String id = ja.getJSONObject(i).getString("WorkID");
                                    String status = ja.getJSONObject(i).getString("Status");
                                    workId.add(id);
                                    statusList.add(status);
                                    noData = "1";
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPURL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                        } catch (Exception e) {
                            e.printStackTrace();
                            SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPURL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog11.cancel();
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                System.out.println("Volley Error : " + error);
                SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPURL, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    String getPOPv2URL;

    public void loadPackageCottonData(String CropSelectid, String PopMasterID) {
        dialog = new TransparentProgressDialog(getActivity(),
                getDynamicLanguageValue(getActivity(), "Dataisloading", R.string.Dataisloading));
        dialog.show();
        Log.v("sjdks", "https://myfarminfo.com/yfirest.svc/Crop/Pop_V2/" + CropSelectid + "/" + PopMasterID + "/" + AppConstant.user_id);

        String language = AppManager.getInstance().getSelectedLanguages(getActivity());
        getPOPv2URL = "https://myfarminfo.com/yfirest.svc/Crop/Pop_V2/" + CropSelectid + "/" + PopMasterID + "/" + AppConstant.user_id + "/" + language;
//        String url = "https://myfarminfo.com/yfirest.svc/Crop/Pop_V2/" + CropSelectid + "/" + faID + "/" + AppConstant.user_id;

        mRequestStartTime = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getPOPv2URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        if (seconds > 3) {
                            SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPv2URL, response, "", "" + seconds, AppConstant.farm_id, "Working");
                        }
//                        response = response.trim();
//                        response = response.replace("\\n", "\\\n");
//                        response = response.replace("\\", "");
//                        response = response.replace("\"{", "{");
//                        response = response.replace("}\"", "}");
//                        response = response.replace("\"[", "[");
//                        response = response.replace("]\"", "]");

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        String str = gson.fromJson(response.toString(), String.class);
                        response = str;

                        System.out.println("Package_Response_pop : " + response);
                        try {
                            if (response == null || response.equalsIgnoreCase("Error")) {
                                getDynamicLanguageToast(getActivity(), "Somthingwentwrongapi", R.string.Somthingwentwrongapi);
                                SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPv2URL, response, "", "" + seconds, AppConstant.farm_id, "Error");
                            } else {
                                ArrayList<PackageBean> listTemp = new ArrayList<PackageBean>();
                                JSONObject jsonObject = new JSONObject(response);
                                ArrayList<String> titleList = new ArrayList<String>();
                                ArrayList<String> FlagID = new ArrayList<String>();

                                if (!noData.equalsIgnoreCase("1")) {
                                    workId = new ArrayList<String>();
                                }
                                //Herojit add
                                int SowingDateDifference = 0;
                                SowingDateDifference = AppManager.getInstance().DateDifference(SowDate);
                                int flag = 1;

                                JSONArray JA1 = jsonObject.getJSONArray("lstColNames");
                                for (int i = 0; i < JA1.length(); i++) {
                                    String ttt = JA1.get(i).toString();
                                    titleList.add(ttt);
                                    //Herojit Add
//                                    try {
//                                        if (ttt.length() > 0 && ttt != null) {
//                                            if (ttt.contains(" DAS")) {
//                                                String DAS[] = ttt.split(" DAS");
//                                                if (DAS.length > 0) {
//                                                    String No_Days = DAS[0];
//                                                    if (No_Days.contains("-")) {
//                                                        String Days[] = No_Days.split("-");
//                                                        if (Days.length > 0) {
//                                                            if (Days.length > 1) {
//                                                                try {
//                                                                    if (SowingDateDifference - 5 > Integer.valueOf(Days[0]) && SowingDateDifference - 5 > Integer.valueOf(Days[1])) {
//                                                                        flag = 1;
//                                                                    } else if (SowingDateDifference - 5 > Integer.valueOf(Days[0]) && SowingDateDifference - 5 < Integer.valueOf(Days[1])) {
//                                                                        flag = 2;
//                                                                    } else if (Integer.valueOf(Days[0]) > SowingDateDifference - 5) {
//                                                                        flag = 3;
//                                                                    }
//                                                                } catch (Exception ex) {
//                                                                    ex.printStackTrace();
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            } else if (ttt.contains("फसल अवधि")) {
//                                                String value = ttt.replace("फसल अवधि", "");
//                                                value = value.replace("दिन", "");
//                                                value = value.replace("से", "-").trim();
//                                                if (value.contains("-")) {
//                                                    String Days[] = value.split("-");
//                                                    if (Days.length > 0) {
//                                                        if (Days.length > 1) {
//                                                            try {
//                                                                int Day0 = Integer.valueOf(Days[0].trim());
//                                                                int Day1 = Integer.valueOf(Days[1].trim());
//                                                                if (SowingDateDifference - 5 > Day0 && SowingDateDifference - 5 > Day1) {
//                                                                    flag = 1;
//                                                                } else if (SowingDateDifference - 5 > Day0 && SowingDateDifference - 5 < Day1) {
//                                                                    flag = 2;
//                                                                } else if (Day0 > SowingDateDifference - 5) {
//                                                                    flag = 3;
//                                                                }
//                                                            } catch (Exception ex) {
//                                                                ex.printStackTrace();
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            } else {
//                                                flag = 1;
//                                            }
//                                        }
//                                        FlagID.add(String.valueOf(flag));
//                                    } catch (Exception ex) {
//                                        ex.printStackTrace();
//                                    }
                                }


                                JSONArray jA = jsonObject.getJSONArray("DT");
                                ArrayList<String> StageList = new ArrayList<>();
                                for (int i = 0; i < jA.length(); i++) {
                                    if (jA.getJSONObject(i).has("Stage")) {
                                        PackageBean bean = new PackageBean();
                                        bean.setStage(jA.getJSONObject(i).getString("Stage"));
                                        bean.setWorkId(jA.getJSONObject(i).getString("StageID"));
                                        bean.setWork_name(jA.getJSONObject(i).getString("WorkName"));
                                        bean.setWork(jA.getJSONObject(i).getString("Work"));
                                        bean.setImage(jA.getJSONObject(i).getString("Image"));
                                        //List of Stage
                                        StageList.add(jA.getJSONObject(i).getString("Stage"));
                                        listTemp.add(bean);
                                        if (!noData.equalsIgnoreCase("1")) {
                                            if (workId.size() == 0 || workId.size() == 1) {
                                                workId.add(jA.getJSONObject(i).getString("StageID"));
                                                statusList.add("no");
                                            } else if (workId.size() > 1 && (workId.get(workId.size() - 2) != workId.get(workId.size() - 1))) {
                                                workId.add(jA.getJSONObject(i).getString("StageID"));
                                                statusList.add("no");
                                            }
                                        }

                                        //DAS For Date Difference
                                        try {
                                            int Day0 = 0;
                                            int Day1 = 0;
                                            String DayFrom = jA.getJSONObject(i).getString("DayFrom");
                                            String DayTo = jA.getJSONObject(i).getString("DayTo");
                                            if (DayFrom != null && DayFrom.length() > 1 && !DayFrom.equalsIgnoreCase("null")) {
                                                Day0 = Integer.parseInt(DayFrom);
                                            }
                                            if (DayTo != null && DayTo.length() > 1 && !DayTo.equalsIgnoreCase("null")) {
                                                Day1 = Integer.parseInt(DayTo);
                                            }

                                            if (SowingDateDifference - 5 > Day0 && SowingDateDifference - 5 > Day1) {
                                                flag = 1;
                                            } else if (SowingDateDifference - 5 > Day0 && SowingDateDifference - 5 < Day1) {
                                                flag = 2;
                                            } else if (Day0 > SowingDateDifference - 5) {
                                                flag = 3;
                                            } else {
                                                flag = 1;
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        FlagID.add(String.valueOf(flag));
                                    }
                                }
                                if (FlagID != null && FlagID.size() > 0) {
                                    FlagID = getFlagID(FlagID, titleList, StageList);
                                }

                                if (listTemp.size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    PackageAdapter adapter = new PackageAdapter(getActivity(),
                                            listTemp, titleList, workId, statusList, FlagID, OnlyCurrentStatus);
                                    recyclerView.setAdapter(adapter);

                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPv2URL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                        } catch (Exception e) {
                            e.printStackTrace();
                            SaveLocalFile(db, getActivity(), SN_PackagePractices, getPOPv2URL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
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


    String URLGetCrop = "";

    public void getloadCropData() {

        final TransparentProgressDialog dialoug = new TransparentProgressDialog(getActivity(),
                getDynamicLanguageValue(getActivity(), "Dataisloading", R.string.Dataisloading));
        dialoug.show();
        String language = AppManager.getInstance().getSelectedLanguages(getActivity());
        Log.v("cropUrl", "https://myfarminfo.com/yfirest.svc/Clients/GetCropByUserID/" + AppConstant.user_id);
//        URLGetCrop = "https://myfarminfo.com/yfirest.svc/Clients/GetCropByUserID/" + AppConstant.user_id;//Comment 20210504
        URLGetCrop = "https://myfarminfo.com/yfirest.svc/Clients/GetCropByUserID/" + AppConstant.user_id + "/" + language;
        mRequestStartTime = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLGetCrop,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley crop Response : " + response);

                        int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                        if (seconds > 3) {
                            SaveLocalFile(db, getActivity(), SN_PackagePractices, URLGetCrop, response, "", "" + seconds, AppConstant.farm_id, "Working");
                        }
                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");


                        DataBean bean = new DataBean();
                        bean = getCropTypeList(response);
                        ArrayList<CropBean> cityList = new ArrayList<CropBean>();
                        String ID = "";
                        cityList = bean.getCropList();
                        cropName = null;
                        final ArrayList<String> cropList = new ArrayList<String>();
                        final ArrayList<String> cropListId = new ArrayList<String>();
                        final ArrayList<String> popMasterIDList = new ArrayList<String>();
                        cropList.add("Select crop");
                        cropListId.add("0");
                        popMasterIDList.add("0");
                        for (int i = 0; i < cityList.size(); i++) {
                            String na = cityList.get(i).getCropName();
                            String id = cityList.get(i).getCropId();
                            String id_master = cityList.get(i).getPopMaster();
                            cropList.add(na);
                            cropListId.add(id);
                            popMasterIDList.add(id_master);
                            String compareValue = AppConstant.selected_cropId;
                            if (compareValue != null && compareValue.equalsIgnoreCase(id)) {
                                cropName = na;
                                CropSelectedID = id;
                                ID = popMasterIDList.get(i + 1);
                                break;
                            }
                        }

//                            setCropName();//20210504
                        try {
                            if (cropName != null && cropName.length() > 0) {
                                txt_Stages.setText(cropName);
                            } else {
                                txt_Stages.setText("");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        loadPackageCottonData(CropSelectedID, ID);
                        if (AppConstant.farm_id != null) {
                            popStatusMethod();
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

    public DataBean getCropTypeList(String response) {

        DataBean dataBean = new DataBean();
        ArrayList<CropBean> cropTypeList = new ArrayList<CropBean>();
        if (response != null) {
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("DT");
                for (int i = 0; i < jsonArray.length(); i++) {
                    CropBean typeBean = new CropBean();
                    typeBean.setCropName(jsonArray.getJSONObject(i).getString("CropName"));
                    typeBean.setCropId(jsonArray.getJSONObject(i).getString("CropID"));
                    typeBean.setPopMaster(jsonArray.getJSONObject(i).getString("PopMasterID"));
                    cropTypeList.add(typeBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataBean.setCropList(cropTypeList);
        }
        return dataBean;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(getActivity());
            setScreenTracking(getActivity(), db, SN_PackagePractices, UID);
        }

        setFontsStyleTxt(getActivity(), txt_Stages, 5);
        setFontsStyleTxt(getActivity(), txt_nodata, 5);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        Log.d("OnPause Method", "OnPause Method called");
        setScreenTracking(getActivity(), db, SN_PackagePractices, UID);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(getActivity());
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(getActivity(), db, SN_PackagePractices, UID);
    }

    public void setCropName() {
        String CropName = getCropName(getActivity(), cropName);
        if (CropName != null && CropName.length() > 0) {
            txt_Stages.setText(CropName);
        } else {
            txt_Stages.setText("");
        }
    }

    public ArrayList<String> getFlagID(ArrayList<String> flagid, ArrayList<String> titleList, ArrayList<String> stagelist) {
        ArrayList<String> FlagID = new ArrayList<>();
        try {
            if (stagelist.size() > 0) {
                for (int i = 0; i < stagelist.size(); i++) {
                    String title = stagelist.get(i);
                    if (i > 0) {
                        String title1 = stagelist.get(i - 1);
                        if (!title.equalsIgnoreCase(title1)) {
                            FlagID.add(flagid.get(i));
                        }
                    } else {
                        FlagID.add(flagid.get(i));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (titleList.size() != FlagID.size()) {
            FlagID = flagid;
        }
        return FlagID;
    }
}