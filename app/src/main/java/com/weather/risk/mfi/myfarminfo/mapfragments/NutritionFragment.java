package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.NutritionAdapter;
import com.weather.risk.mfi.myfarminfo.bean.NutritionBean;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_NutritionFragment;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;


@SuppressLint("ValidFragment")
public class NutritionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";


    public NutritionFragment(String la, String lo) {
        latitude = la;
        longitude = lo;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            latitude = getArguments().getString(LATITUDE);
            longitude = getArguments().getString(LONGITUDE);
        }*/
    }

    // TODO: Rename and change types of parameters
    private String latitude;
    private String longitude;
    Spinner nutritionCrop;
    Spinner nutritionSeason;
    Spinner nutritionSoil;
    Spinner nutritionIrrigation;
    Spinner nutritionStatus;
    Button nutritionSubmit;

    int cropId;
    String irrigation_string = null;
    String status = null;
    String soil = null;
    String season_text = null;
    RecyclerView recyclerView;

    private String cityArr[];
    String villageID = null;
    String vill_id = null;
    String lat = null;
    String lon = null;
    String villageName = null;
    Spinner villageSpinner, districtSpinner;

    String UID = "";
    DBAdapter db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
       /* String role = AppConstant.role;
        Log.v("roleeeeeelllll",role+"");

        if (role.equalsIgnoreCase("Admin")){
            setHasOptionsMenu(true);
        }else {

        }*/

        if (AppConstant.longitude != null && AppConstant.longitude.length() > 3) {
            latitude = AppConstant.latitude;
            longitude = AppConstant.longitude;
        }

        db = new DBAdapter(getActivity());
        
        nutritionCrop = (Spinner) view.findViewById(R.id.nutritionCrop);
        nutritionSeason = (Spinner) view.findViewById(R.id.nutritionSeason);
        nutritionSoil = (Spinner) view.findViewById(R.id.nutritionSoil);
        nutritionIrrigation = (Spinner) view.findViewById(R.id.nutritionIrrigation);
        nutritionStatus = (Spinner) view.findViewById(R.id.nutritionStatus);
        nutritionSubmit = (Button) view.findViewById(R.id.nutritionSubmit);
        nutritionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*    if (isValid()) {
                    getNutritionList();
                }*/

                getNutritionList();
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.nutrition_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        Resources res = getResources();
        final String[] crop = res.getStringArray(R.array.nutritionCrop);
        final String[] nutritionStatusArray = res.getStringArray(R.array.nutritionStatus);


        final String[] irrigation = res.getStringArray(R.array.irrigation);


        ArrayAdapter<String> irrigationAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, irrigation); //selected item will look like a spinner set from XML
        nutritionIrrigation.setAdapter(irrigationAdapter);

        nutritionIrrigation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i >= 0) {
                    if (i == 0) {
                        irrigation_string = "good irrigation";
                    }
                    if (i == 1) {
                        irrigation_string = "rainfed";
                    }
                    if (i == 2) {
                        irrigation_string = "protective irrigation";
                    }

                } else {
                    irrigation_string = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> statusArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, nutritionStatusArray); //selected item will look like a spinner set from XML
        nutritionStatus.setAdapter(statusArrayAdapter);

        nutritionStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i >= 0) {
                    if (i == 0) {
                        status = "Fertile";
                    }
                    if (i == 1) {
                        status = "Low Fertile";
                    }

                } else {
                    status = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final String[] soilArray = res.getStringArray(R.array.soil);

        ArrayAdapter<String> soilAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, soilArray);
        nutritionSoil.setAdapter(soilAdapter);
        nutritionSoil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    if (i == 0) {
                        soil = "Clay";
                    }
                    if (i == 1) {
                        soil = "Loam";
                    }
                    if (i == 2) {
                        soil = "Sandy";
                    }

                } else {
                    soil = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> cropArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, crop); //selected item will look like a spinner set from XML
        nutritionCrop.setAdapter(cropArrayAdapter);

        nutritionCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i >= 0) {
                    if (i == 0) {
                        cropId = 12;
                    }
                    if (i == 1) {
                        cropId = 1;
                    }
                    if (i == 2) {
                        cropId = 8;
                    }
                    if (i == 3) {
                        cropId = 128;
                    }

                    getSeason(cropId);
                } else {
                    cropId = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_NutritionFragment, UID);

        return view;
    }

   

    private void getSeason(int cropId) {
//        StringRequest stringVarietyRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/NutMng/Season?CropID=" + latitude + "/" + longitude + "/" + cropId,
        StringRequest stringVarietyRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/NutMng/Season?CropID=" + cropId,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String seasonResponse) {
                        try {
                            System.out.println("Season Respose : " + seasonResponse);
                            seasonResponse = seasonResponse.trim();
                            seasonResponse = seasonResponse.substring(1, seasonResponse.length() - 1);
                            seasonResponse = seasonResponse.replace("\\", "");
                            final ArrayList<String> seasonArray = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(seasonResponse);
                            if (jsonArray.length() > 0) {
                                //   seasonArray.add("Select Season");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.isNull("Season") ? "" : jsonObject.getString("Season");
                                    seasonArray.add(name);
                                }
                            }
                            ArrayAdapter<String> seasonSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, seasonArray); //selected item will look like a spinner set from XML
                            nutritionSeason.setAdapter(seasonSpinnerAdapter);
                            nutritionSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i == 0) {
                                        season_text = seasonArray.get(i);

                                        getNutritionList();
                                    } else if (i > 0) {
                                        season_text = seasonArray.get(i);

                                    } else {
                                        season_text = null;
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                noInternetMethod();
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringVarietyRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringVarietyRequest);
    }


    public boolean isValid() {

        boolean isValid = true;


        if (cropId == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.PleaseselectCrop), Toast.LENGTH_SHORT).show();
            return false;
        } else if (season_text == null || season_text.length() < 1) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Pleaseselecttheseason), Toast.LENGTH_SHORT).show();
            return false;
        } else if (soil == null || soil.length() < 1) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Pleaseselectthesoil), Toast.LENGTH_SHORT).show();
            return false;

        } else if (irrigation_string == null || irrigation_string.length() < 1) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Pleaseselecttheirrigation), Toast.LENGTH_SHORT).show();
            return false;

        } else if (status == null || status.length() < 1) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Pleaseselectthestatus), Toast.LENGTH_SHORT).show();
            return false;
        }
        return isValid;
    }


    private void getNutritionList() {

        final TransparentProgressDialog dialoug = new TransparentProgressDialog(getActivity(),
                getResources().getString(R.string.Dataisloading));
        dialoug.show();


//        StringRequest stringVarietyRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/NutMng/Season?CropID=" + latitude + "/" + longitude + "/" + cropId,

        String ss = "https://myfarminfo.com/yfirest.svc/NutMng/Advice/" + latitude + "/" + longitude + "/" + cropId + "/" + season_text + "/" + soil + "/" + irrigation_string + "/" + status + "/0";
        ss = AppManager.getInstance().removeSpaceForUrl(ss);
        Log.v("nutrition_url", ss + "");

        StringRequest stringVarietyRequest = new StringRequest(Request.Method.GET, ss,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String seasonResponse) {
                        dialoug.cancel();
                        try {

                            seasonResponse = seasonResponse.trim();
                            seasonResponse = seasonResponse.substring(1, seasonResponse.length() - 1);
                            seasonResponse = seasonResponse.replace("\\", "");
                            System.out.println("Season Respose : " + seasonResponse);


                            ArrayList<NutritionBean> messageList = new ArrayList<NutritionBean>();
                            JSONObject jb = new JSONObject(seasonResponse);

                            JSONArray ja = jb.getJSONArray("DT");
                            for (int i = 0; i < ja.length(); i++) {
                                NutritionBean bean = new NutritionBean();
                                JSONObject jsonObject = ja.getJSONObject(i);
                                String nitro = jsonObject.getString("NitrogenRec");
                                if (nitro != null && nitro.length() > 0) {
                                    bean = new NutritionBean();
                                    bean.setMessage(nitro);
                                    bean.setTitle(getResources().getString(R.string.NITROGEN));
                                    messageList.add(bean);
                                }

                                String phos = jsonObject.getString("PhosphorusRec");
                                if (phos != null && phos.length() > 0) {
                                    bean = new NutritionBean();
                                    bean.setMessage(phos);
                                    bean.setTitle(getResources().getString(R.string.PHOSPHORUS));
                                    messageList.add(bean);
                                }

                                String pot = jsonObject.getString("PotassiumRec");
                                if (pot != null && pot.length() > 0) {
                                    bean = new NutritionBean();
                                    bean.setMessage(pot);
                                    bean.setTitle(getResources().getString(R.string.POTASSIUM));
                                    messageList.add(bean);
                                }

                                String soil = jsonObject.getString("SoilReclamation");
                                if (soil != null && soil.length() > 0) {
                                    bean = new NutritionBean();
                                    bean.setMessage(soil);
                                    bean.setTitle(getResources().getString(R.string.SOIL));
                                    messageList.add(bean);
                                }

                                String micro = jsonObject.getString("MicroNutrient");
                                if (micro != null && micro.length() > 0) {
                                    bean = new NutritionBean();
                                    bean.setMessage(micro);
                                    bean.setTitle(getResources().getString(R.string.MICRONUTRIENTS));
                                    messageList.add(bean);
                                }

                                String fym = jsonObject.getString("FYMApplication");
                                if (fym != null && fym.length() > 0) {
                                    bean = new NutritionBean();
                                    bean.setMessage(fym);
                                    bean.setTitle(getResources().getString(R.string.FYMAPPLICATION));
                                    messageList.add(bean);
                                }
                            }

                            if (messageList.size() > 0) {

                                recyclerView.setVisibility(View.VISIBLE);
                                NutritionAdapter adapter = new NutritionAdapter(getActivity(), messageList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.Nodatafoundcrop), Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
                dialoug.cancel();
            }
        });

        AppController.getInstance().addToRequestQueue(stringVarietyRequest);
    }

    private void noInternetMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.Nointernet)).
                setMessage(getResources().getString(R.string.Doyouwantrefresh)).
                setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        getSeason(cropId);
                    }
                }).
                setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

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
        setScreenTracking(getActivity(), db, SN_NutritionFragment, UID);
    }

}
