package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.weather.risk.mfi.myfarminfo.adapter.WeedAdapter;
import com.weather.risk.mfi.myfarminfo.bean.Weed;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_WeedFragment;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

/**
 * Created by Admin on 06-02-2018.
 */
public class WeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String STATE_ID = "stateId";

    ArrayList<Weed> weedList = new ArrayList<Weed>();
    String cropName = null;


    public WeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static View view;
    ArrayList<Crop> cropArray = new ArrayList<>();
    Spinner cropSpinner;
    Button weedSubmit;
    String csid, crop;
    RecyclerView listView;
    private RecyclerView.LayoutManager mLayoutManager;

    String UID = "";
    DBAdapter db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.weed_fragment, container, false);

        db = new DBAdapter(getActivity());
        
        cropSpinner = (Spinner) view.findViewById(R.id.weed_crop_spinner);
        weedSubmit = (Button) view.findViewById(R.id.weed_submit);

        listView = (RecyclerView) view.findViewById(R.id.list_weed);
        listView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);

        weedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (csid != null) {
                    getWeedReport(csid);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.PleaseselectCrop), Toast.LENGTH_SHORT).show();
                }
            }
        });

        getCropList();

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(getActivity(), db, SN_WeedFragment, UID);

        
        return view;
    }



    private void getCropList() {
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(getActivity(),
                getResources().getString(R.string.Dataisloading));
        dialoug.show();
        csid = null;
        crop = null;
        cropName = null;
        cropArray = new ArrayList<>();
        cropArray.add(new Crop("0", "Select Crop"));
        Log.v("dksksscdscs", "https://www.myfarminfo.com/yfirest.svc/Crop/Weed/Crops");
        StringRequest stringCropRequest = new StringRequest(Request.Method.GET, "https://www.myfarminfo.com/yfirest.svc/Crop/Weed/Crops",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        dialoug.cancel();
                        System.out.println("Volley Crop Response : " + response);
                        try {
                            response = response.trim();
                            //  response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            response = response.replace("\"[", "[");
                            response = response.replace("]\"", "]");
                            ArrayList<String> cropSpinnerArray = new ArrayList<>();
                            cropSpinnerArray.add("Select crop");


//                            JSONArray jsonArray = new JSONArray(response);
                            JSONArray jsonInnerArray = new JSONArray(response);
                            if (jsonInnerArray.length() > 0) {

                                for (int i = 0; i < jsonInnerArray.length(); i++) {
                                    JSONObject jsonObject = jsonInnerArray.getJSONObject(i);
                                    String csid = jsonObject.isNull("CropID") ? "" : jsonObject.getString("CropID");
                                    String crop = jsonObject.isNull("CropName") ? "" : jsonObject.getString("CropName");
                                    cropSpinnerArray.add(crop);
                                    cropArray.add(new Crop(csid, crop));

                                    String compareValue = AppConstant.selected_cropId;

                                    if (compareValue != null && compareValue.equalsIgnoreCase(csid)) {
                                        cropName = crop;
                                    }
                                }
                            }
                            ArrayAdapter<String> cropSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, cropSpinnerArray);
                            cropSpinner.setAdapter(cropSpinnerAdapter);

                            if (cropName != null) {
                                int spinnerPosition = cropSpinnerAdapter.getPosition(cropName);
                                cropSpinner.setSelection(spinnerPosition);
                            } else {
                                noCropFound(cropName);
                            }

                            cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i > 0) {
                                        Crop cropObj = cropArray.get(i);
                                        csid = cropObj.getCSID();
                                        crop = cropObj.getCropName();

                                        getWeedReport(csid);
                                    } else {
                                        csid = null;
                                        crop = null;
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), getResources().getString(R.string.ResponseFormattingError), Toast.LENGTH_LONG).show();
                            dialoug.cancel();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley Error : " + error);
            }
        });
        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringCropRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringCropRequest);
    }

    private void getWeedReport(String csid) {
//        yfirest.svc/Feasibility/25/77/25774/Cotton/6
        final TransparentProgressDialog dialoug = new TransparentProgressDialog(getActivity(),
                getResources().getString(R.string.Dataisloading));
        dialoug.show();
        weedList = new ArrayList<Weed>();

        String ss = AppManager.getInstance().removeSpaceForUrl("https://myfarminfo.com/yfirest.svc/Crop/Weeds/Data/" + csid);

        StringRequest stringFeasibilityRequest = new StringRequest(Request.Method.GET, ss,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String varietyResponse) {
                        dialoug.cancel();
                        try {

                            varietyResponse = varietyResponse.trim();
                            //  varietyResponse = varietyResponse.substring(1, varietyResponse.length() - 1);
                            varietyResponse = varietyResponse.replace("\"[", "[");
                            varietyResponse = varietyResponse.replace("]\"", "]");
                            varietyResponse = varietyResponse.replace("\\", "");
                            varietyResponse = varietyResponse.replace("\"{", "{");
                            varietyResponse = varietyResponse.replace("}\"", "}");

                            System.out.println("Feasibility Respose : " + varietyResponse);

                            if (varietyResponse != null || varietyResponse.length() > 1) {
                                JSONObject jb = new JSONObject(varietyResponse);
                                JSONArray jsonArray = jb.getJSONArray("DT");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jb1 = jsonArray.getJSONObject(i);
                                    Weed weed = new Weed();
                                    weed.setImage(jb1.getString("Image"));
                                    weed.setBotanical(jb1.getString("BotanicalName"));
                                    weed.setEnglish(jb1.getString("EnglishName"));
                                    weed.setCommon(jb1.getString("CommonName"));

                                    weedList.add(weed);
                                }
                            }

                            if (weedList.size() > 0) {

                                WeedAdapter adapter = new WeedAdapter(getActivity(), weedList);
                                listView.setAdapter(adapter);

                                Log.v("ladk", "saldsa");
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.Nodatafoundcrop), Toast.LENGTH_SHORT).show();
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

    public void noCropFound(String crop_nam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.Nodatafoundcrop) + " " + crop_nam + " " + getResources().getString(R.string.Pleaseselectanothercrop))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
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
        setScreenTracking(getActivity(), db, SN_WeedFragment, UID);
    }
}