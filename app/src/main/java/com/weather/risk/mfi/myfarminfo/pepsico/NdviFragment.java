package com.weather.risk.mfi.myfarminfo.pepsico;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.weather.risk.mfi.myfarminfo.adapter.NDVIAdapter;
import com.weather.risk.mfi.myfarminfo.bean.NDBI_Bean;
import com.weather.risk.mfi.myfarminfo.dabwali.DabwaliAlert;
import com.weather.risk.mfi.myfarminfo.dabwali.DabwaliMoisture;
import com.weather.risk.mfi.myfarminfo.dabwali.DabwaliNDVI;
import com.weather.risk.mfi.myfarminfo.dabwali.GroundObservationFragment;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.PepsicoMoisture;
import com.weather.risk.mfi.myfarminfo.pest_disease.ForecastPestFragment;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 *
 */
public class NdviFragment extends Fragment {
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

    String nextResponse = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocateYoutFarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NdviFragment newInstance() {
        NdviFragment fragment = new NdviFragment();

        return fragment;
    }

    public NdviFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

  /*  Spinner districtSpinner,villageSpinner;
    Button submitBtn;
*/

    private String cityArr[];
    String villageID = null;
    String vill_id = null;
    String lat = null;
    String lon = null;
    String villageName = null;
    RecyclerView recyclerView;

    LinearLayout hideShowLay;
    Spinner villageSpinner,districtSpinner;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ndvi_pepsico, container, false);

        hideShowLay = (LinearLayout)view.findViewById(R.id.aaaa);
        String role = AppConstant.role;
        Log.v("roleeeeeelllll",role+"");

        if (role.equalsIgnoreCase("Admin")){
            setHasOptionsMenu(true);
        }else {

        }




        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("NDVI Data");
        farmInfo.setTextColor(Color.WHITE);





        Button moistureBTN = (Button) view.findViewById(R.id.moisture_btn);
        Button diseaseAdviceBTN = (Button) view.findViewById(R.id.disease_advice_btn);
        Button forcastBTN = (Button) view.findViewById(R.id.forecast_btn);
        Button ndviBTN = (Button) view.findViewById(R.id.ndvi_btn);

        moistureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    Fragment fragment = new PepsicoMoisture(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("moi").commit();
                }
            }
        });
        diseaseAdviceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    Fragment fragment = new DiseaseAdviceFragment(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("alrt").commit();
                }
            }
        });
        forcastBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    Fragment fragment =new ForecastFragment(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("for").commit();
                }
            }
        });
        ndviBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextResponse != null) {
                    /*Fragment fragment = new DabwaliNDVI(nextResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("alrt").commit();*/
                }
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_ndvi);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);

        String lat1 = prefs.getString("lat",null);
        String lon1 = prefs.getString("lon",null);
        String villageId1 = prefs.getString("villageId",null);
        String villageName1 = prefs.getString("villageName",null);
        String role1 = prefs.getString(AppConstant.PREFRENCE_KEY_ROLE,null);

        lat = lat1;
        lon = lon1;
        villageID = villageId1;
        villageName = villageName1;

        if (villageID!=null) {
            messageData();
        }



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


    @Override
    public void onResume() {
        super.onResume();
        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("NDVI Data");
        farmInfo.setTextColor(Color.WHITE);

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

                    NdviFragment fragment = new NdviFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

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
        Log.v("knsknklanl", "https://myfarminfo.com/yfirest.svc/Clients/GGRC/Villages/" + ID);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GGRC/Villages/" + ID,
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
                        bean = getEventTypeList(response);
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

                                Log.v("ksjkls", villageID);

                                if (villageID != null) {

                                    String[] parts = villageID.split("-");
                                    vill_id = parts[0];
                                    lat = parts[1];
                                    lon = parts[2];

                                    SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("lat", lat);
                                    editor.putString("lon", lon);
                                    editor.putString("villageId", villageID);
                                    editor.putString("villageName", villageName);
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
        if (response != null) {
            try {

                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {

                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    VillageBean typeBean = new VillageBean();
                    typeBean.setVilageName(jsonArray.getJSONObject(i).getString("Name"));
                    typeBean.setVillageID(jsonArray.getJSONObject(i).getString("ID"));
                    eventTypeList.add(typeBean);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataBean.setCityList(eventTypeList);


        }

        return dataBean;
    }


    ProgressDialog dialoug1;

    public void messageData() {
        dialoug1 = ProgressDialog.show(getActivity(), "",
                "Fetching Data Please wait...", true);

        Log.v("kkkkk","https://myfarminfo.com/yfirest.svc/Clients/Pepsico/Data/"+villageID+"/"+"Village"+"/"+lat+"/"+lon+"/"+villageName);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/Pepsico/Data/"+villageID+"/"+"Village"+"/"+lat+"/"+lon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug1.cancel();
                        // Display the first 500 characters of the response string.


                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");

                        System.out.println(" Response : " + response);

                        if (response!=null){

                            nextResponse = response;
                          //  nextBtn.setVisibility(View.VISIBLE);

                            try{

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("DTChartDesc");
                                JSONArray jsonArray1 = jsonObject.getJSONArray("DT10");

                                for (int i = 0; i < jsonArray1.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                    AppConstant.maxY = jsonObject1.getString("MaxY");
                                    AppConstant.maxX = jsonObject1.getString("MaxX");
                                    AppConstant.minY = jsonObject1.getString("MinY");
                                    AppConstant.minX = jsonObject1.getString("MinY");
                                }

                                ArrayList<NDBI_Bean> msgList = new ArrayList<NDBI_Bean>();


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    NDBI_Bean bean = new NDBI_Bean();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    // bean.setId(jsonObject1.getString("ID"));
                                    bean.setDate(jsonObject1.getString("Date"));
                                    bean.setVillage_mean(jsonObject1.getString("Village_mean"));
                                    bean.setStart_Date(jsonObject1.getString("Start_Date"));
                                    bean.setCaptcha_Img(jsonObject1.getString("Captcha_Img"));
                                    bean.setVillage_mean(jsonObject1.getString("Village_mean"));
                                    if (!jsonObject1.isNull("Farm_Data") || (jsonObject1.getString("Farm_Data")!=null)) {
                                        JSONArray jsss = jsonObject1.getJSONArray("Farm_Data");
                                        bean.setFarm_Data(jsss.toString());
                                    }else {
                                        bean.setFarm_Data("");
                                    }

                                    msgList.add(bean);
                                }


                                if (msgList.size()>0){

                               //     ndviTxt.setVisibility(View.VISIBLE);

                                    NDVIAdapter adapter = new NDVIAdapter(getActivity(),msgList);
                                    recyclerView.setAdapter(adapter);
                                }else {
                                    //ndviTxt.setVisibility(View.GONE);

                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialoug1.cancel();
                System.out.println("Volley Error : " + error);
                noInternetMethod();
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "https://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void noInternetMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Internet Error").
                setMessage("Do You want to Refresh?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        messageData();
                    }
                }).
                setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}