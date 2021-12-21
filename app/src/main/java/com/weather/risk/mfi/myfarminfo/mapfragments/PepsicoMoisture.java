package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.bean.NDBI_Bean;
import com.weather.risk.mfi.myfarminfo.dabwali.DabwaliNDVI;
import com.weather.risk.mfi.myfarminfo.pepsico.DiseaseAdviceFragment;
import com.weather.risk.mfi.myfarminfo.pepsico.ForecastFragment;
import com.weather.risk.mfi.myfarminfo.pepsico.MoistureAdapter;
import com.weather.risk.mfi.myfarminfo.pepsico.NdviFragment;
import com.weather.risk.mfi.myfarminfo.pest_disease.ForecastPestFragment;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 15-06-2018.
 */
public class PepsicoMoisture  extends Fragment {
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

    String response = null;

    // private OnFragmentInteractionListener mListener;

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
    public PepsicoMoisture(String nextResponse) {
        // Required empty public constructor

        response = nextResponse;
    }

    public PepsicoMoisture() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private String cityArr[];
    String villageID = null;
    String vill_id = null;
    String lat = null;
    String lon = null;
    String villageName = null;
    RecyclerView recyclerView;
  //  Button nextBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pepsico_moisture, container, false);

        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Soil Moisture");
        farmInfo.setTextColor(Color.WHITE);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);

        String lat1 = prefs.getString("lat", null);
        String lon1 = prefs.getString("lon", null);

        lat = lat1;
        lon = lon1;



        Button moistureBTN = (Button) view.findViewById(R.id.moisture_btn);
        Button diseaseAdviceBTN = (Button) view.findViewById(R.id.disease_advice_btn);
        Button forcastBTN = (Button) view.findViewById(R.id.forecast_btn);
        Button ndviBTN = (Button) view.findViewById(R.id.ndvi_btn);

        moistureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response != null) {
                   /* Fragment fragment = new PepsicoMoisture(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();*/
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
                    Fragment fragment =new ForecastFragment(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_moisture);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        if (response != null) {
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("DT");

                ArrayList<NDBI_Bean> msgList = new ArrayList<NDBI_Bean>();


                for (int i = 0; i < jsonArray.length(); i++) {
                    NDBI_Bean bean = new NDBI_Bean();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    bean.setId(jsonObject1.getString("ID"));
                    bean.setDate(jsonObject1.getString("Date"));
                    bean.setVillageId(jsonObject1.getString("District_Id"));
                    bean.setImage(jsonObject1.getString("Final_soilImg"));
                    bean.setStartDate(jsonObject1.getString("Start_Date"));
                    bean.setDistrictId(jsonObject1.getString("District_Id"));
                    bean.setVillage_mean(jsonObject1.getString("Village_soilmean"));
                    msgList.add(bean);
                }

                if (msgList.size() > 0) {

                    // ndviTxt.setVisibility(View.VISIBLE);

                    MoistureAdapter adapter = new MoistureAdapter(getActivity(), msgList);
                    recyclerView.setAdapter(adapter);
                } else {
                    //ndviTxt.setVisibility(View.GONE);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Soil Moisture");
        farmInfo.setTextColor(Color.WHITE);

    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "https://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

}