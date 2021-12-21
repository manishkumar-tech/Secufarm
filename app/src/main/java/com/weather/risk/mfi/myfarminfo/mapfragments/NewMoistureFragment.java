package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.KeyValueBean;
import com.weather.risk.mfi.myfarminfo.bean.NDBI_Bean;
import com.weather.risk.mfi.myfarminfo.dabwali.DabwaliNdviAdapter;
import com.weather.risk.mfi.myfarminfo.dabwali.ForecastBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.pepsico.TempBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class NewMoistureFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CALLING_ACTIVITY = "callingActivity";
    private static final String FARM_NAME = "FarmName";
    private static final String ALL_POINTS = "AllLatLngPount";
    private static final String AREA = "area";
    String data = null;
    String nextResponseSoil = null;
    RecyclerView recyclerView;
    ArrayList<TempBean> listTemp = new ArrayList<TempBean>();
    ArrayList<ForecastBean> listForecast = new ArrayList<ForecastBean>();

    // private OnFragmentInteractionListener mListener;
    ArrayList<KeyValueBean> farmDetailList = new ArrayList<KeyValueBean>();
    // TODO: Rename and change types of parameters
    private int callingActivity;
    private String selectedFarmName;
    private String area;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocateYoutFarmFragment.
     */
    // TODO: Rename and change types and number of parameters
   /* public static MoiostureFragment newInstance(String nextResponse) {
        MoiostureFragment fragment = new MoiostureFragment();

        return fragment;
    }*/
    @SuppressLint("ValidFragment")
    public NewMoistureFragment(String nextResponseSoil1) {
        // Required empty public constructor
        nextResponseSoil = nextResponseSoil1;

    }

    public NewMoistureFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
        View view = inflater.inflate(R.layout.new_soil_fragment, container, false);

      /*  farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Soil Moisture");
        farmInfo.setTextColor(Color.WHITE);
*/
        AppConstant.maxY = null;
        AppConstant.maxX = null;
        AppConstant.minY = null;
        AppConstant.minX = null;


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_moisture);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        if (nextResponseSoil != null) {
            messageData(nextResponseSoil);
        } else {
            soilMoistureData(AppConstant.farm_id);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void soilMoistureData(String id) {
        String usID = AppConstant.user_id;

        if (id != null) {
            id = id.trim();
        }

        final Dialog dialoug2 = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.FetchingData), true);

        //  String ssss = "https://myfarminfo.com/yfirest.svc/Clients/DataVersion2/wrmsdabwali/" + id + "/" + usID;
        String ssss = "https://myfarminfo.com/yfirest.svc/Clients/GetSoilValue/jalna/" + id + "/" + usID;
        String url = AppManager.getInstance().removeSpaceForUrl(ssss);
        Log.v("kkkkk", url);
        final String finalId = id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug2.cancel();
                        // Display the first 500 characters of the response string.
                        response = response.trim();
                        //  response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\\", "");
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");

                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        response = response.replace("\"[", "[");
                        response = response.replace("]\"", "]");
                        response = response.trim();
                        System.out.println(" Response : " + response);
                        nextResponseSoil = response;

                        messageData(nextResponseSoil);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug2.cancel();
                System.out.println("Volley Error : " + error);

            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, 0);
        stringRequest.setRetryPolicy(policy);
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void noDataFoundMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.no_data)).
                setMessage(getResources().getString(R.string.MoistureDataisnotfound)).
                setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

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


            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("DT");
                JSONArray jsonArray1 = jsonObject.getJSONArray("DT10");

                for (int i = 0; i < jsonArray1.length(); i++) {

                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);


                    AppConstant.maxY = jsonObject1.getString("MaxY");
                    AppConstant.maxX = jsonObject1.getString("MaxX");
                    AppConstant.minY = jsonObject1.getString("MinY");
                    AppConstant.minX = jsonObject1.getString("MinX");
                }

                ArrayList<NDBI_Bean> msgList = new ArrayList<NDBI_Bean>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    NDBI_Bean bean = new NDBI_Bean();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    // bean.setId(jsonObject1.getString("ID"));
                    bean.setFinal_soilImg(jsonObject1.getString("Final_soilImg"));
                    bean.setDate(jsonObject1.getString("Date"));
//                    if (jsonObject1.has("village_soilmean")) {
//                        bean.setVillage_mean(jsonObject1.getString("village_soilmean"));
//                    }
                    if (jsonObject1.has("Village_soilmean")) {
                        bean.setVillage_mean(jsonObject1.getString("Village_soilmean"));
                    }
                    bean.setStart_Date(jsonObject1.getString("Start_Date"));
                    bean.setCaptcha_Img(jsonObject1.getString("Captcha_soilImg"));
                    //  bean.setVillage_mean(jsonObject1.getString("New_Village_soilmean"));

                    if (jsonObject1.isNull("Farm_Data") || jsonObject1.optString("Farm_Data").length() < 5) {
                        bean.setFarm_Data("");
                    } else {

                        JSONArray jsss = jsonObject1.getJSONArray("Farm_Data");
                        bean.setFarm_Data(jsss.toString());
                    }

                    msgList.add(bean);
                }

                if (msgList.size() > 0) {
                    DabwaliNdviAdapter adapter = new DabwaliNdviAdapter(getActivity(), msgList);
                    recyclerView.setAdapter(adapter);
                } else {
                    noDataFoundMethod();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


}