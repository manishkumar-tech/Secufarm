package com.weather.risk.mfi.myfarminfo.pepsico;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.text.Html;
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
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.mapfragments.PepsicoMoisture;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Admin on 01-09-2017.
 */
public class DiseaseAdviceFragment extends Fragment {
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

    private ProgressBar firstBar = null;
    private ProgressBar secondBar = null;

    LinearLayout parent;
    String response;

    ArrayList<DiseaseBean> dataList = new ArrayList<DiseaseBean>();


    public DiseaseAdviceFragment(String resp) {
        // Required empty public constructor
        response = resp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Spinner diseaseSpinner;

    Button showBTN;
    String d_ID = "2";

    TextView happenText;

    ProgressBar highBar, mediumBar, lowBar;
    TextView highPerText, lowPerText, mediumPerText;
    TextView maxText;
    private String cityArr[];
    String villageID = null;
    String vill_id = null;
    String lat = null;
    String lon = null;
    String villageName = null;
    Spinner villageSpinner,districtSpinner;

    TextView noData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.disease_advice_fragment, container, false);
        String role = AppConstant.role;
        Log.v("roleeeeeelllll",role+"");

        noData = (TextView)view.findViewById(R.id.nodata);


      /*  if (role.equalsIgnoreCase("Admin")){
            setHasOptionsMenu(true);
        }else {

        }*/
        setHasOptionsMenu(true);



        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Disease Advisory");
        farmInfo.setTextColor(Color.WHITE);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);

        lat = prefs.getString("lat", null);
        lon = prefs.getString("lon", null);

        if (lat == null) {
            lat = "" + LatLonCellID.lat;
            lon = "" + LatLonCellID.lon;

        }
        Button moistureBTN = (Button) view.findViewById(R.id.moisture_btn);
        Button diseaseAdviceBTN = (Button) view.findViewById(R.id.disease_advice_btn);
        Button forcastBTN = (Button) view.findViewById(R.id.forecast_btn);
        Button ndviBTN = (Button) view.findViewById(R.id.ndvi_btn);

        moistureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response != null) {
                    Fragment fragment = new PepsicoMoisture(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                }
            }
        });
        diseaseAdviceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response != null) {
                    /*Fragment fragment = new DiseaseAdviceFragment(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("alrt").commit();*/
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


        parent = (LinearLayout) view.findViewById(R.id.parent);
        happenText = (TextView) view.findViewById(R.id.disease_happen_text);
        maxText = (TextView) view.findViewById(R.id.maxText);

        firstBar = (ProgressBar) view.findViewById(R.id.firstBar);
        secondBar = (ProgressBar) view.findViewById(R.id.secondBar);

        highBar = (ProgressBar) view.findViewById(R.id.highBar);
        mediumBar = (ProgressBar) view.findViewById(R.id.mediumBar);
        lowBar = (ProgressBar) view.findViewById(R.id.lowBar);

        highPerText = (TextView) view.findViewById(R.id.highPercent);
        mediumPerText = (TextView) view.findViewById(R.id.mediumPercent);
        lowPerText = (TextView) view.findViewById(R.id.lowPercent);

        firstBar.setMax(100);
        secondBar.setMax(100);
        highBar.setMax(100);
        mediumBar.setMax(100);
        lowBar.setMax(100);


        diseaseSpinner = (Spinner) view.findViewById(R.id.diseaseSpinner);
        showBTN = (Button) view.findViewById(R.id.disease_show_btn);
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
                dialog.setContentView(R.layout.show_data_popup);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                dialog.show();

                final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_popup);
                final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.showDataListView);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                ShowDataAdapter adapter = new ShowDataAdapter(getActivity(), dataList);
                recyclerView.setAdapter(adapter);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                    }
                });

            }
        });

        ArrayList<String> diseaseList = new ArrayList<>();
        final ArrayList<String> diseaseIdArray = new ArrayList<>();


        diseaseList.add("Whitefly");
        diseaseList.add("Pink Bollworm");

        diseaseIdArray.add("2");
        diseaseIdArray.add("26");

        ArrayAdapter<String> varietyArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, diseaseList); //selected item will look like a spinner set from XML
        varietyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diseaseSpinner.setAdapter(varietyArrayAdapter);
        diseaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                d_ID = diseaseIdArray.get(position);

                loadDiseaseData(d_ID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




      /*  view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("tag", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("tag", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });*/


        // loadForecastData(d_ID);

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
        if (id == R.id.action_villages) {

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
                Toast.makeText(getActivity(), "MFI ErrorLog file does not exist ", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
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

                    DiseaseAdviceFragment pep = new DiseaseAdviceFragment(response);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, pep).commit();

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
        Log.v("knsknklanl","https://myfarminfo.com/yfirest.svc/GGRC/GGRC/Villages/"+ID);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GGRC/Villages/"+ID,
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
                        bean = getEventTypeList( response);
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

                                Log.v("ksjkls",villageID);

                                if (villageID!=null){

                                    String[] parts = villageID.split("-");
                                    vill_id = parts[0];
                                    lat = parts[1];
                                    lon = parts[2];

                                    SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
                                    SharedPreferences.Editor editor  = prefs.edit();
                                    editor.putString("lat",lat);
                                    editor.putString("lon",lon);
                                    editor.putString("villageId",villageID);
                                    editor.putString("villageName",villageName);
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
        if (response != null){
            try{

                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {

                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    VillageBean typeBean = new VillageBean();
                    typeBean.setVilageName(jsonArray.getJSONObject(i).getString("Name"));
                    typeBean.setVillageID(jsonArray.getJSONObject(i).getString("ID"));
                    eventTypeList.add(typeBean);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            dataBean.setCityList(eventTypeList);



        }

        return dataBean;
    }



    @Override
    public void onResume() {
        super.onResume();
        TextView farmInfo = (TextView) getActivity().findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("Disease Advisory");
        farmInfo.setTextColor(Color.WHITE);
    }

    ProgressDialog dialog;

    public void loadDiseaseData(String ID) {
        dialog = ProgressDialog.show(getActivity(), "", "Fetching Forecast. Please wait...", true);

        Log.v("sjdks", "https://myfarminfo.com/yfirest.svc/Disease/Advice/" + lat + "/" + lon + "/12" + "/" + ID);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Disease/Advice/" + lat + "/" + lon + "/12" + "/" + ID + "/India",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        // Display the first 500 characters of the response string.


                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");
                        response = response.replace("\"{", "{");
                        response = response.replace("}\"", "}");
                        System.out.println("Disease Response : " + response);
                        try {

                            if (response.equalsIgnoreCase("InsufData")){

                                noData.setVisibility(View.VISIBLE);
                                parent.setVisibility(View.GONE);

                            }else {
                                noData.setVisibility(View.GONE);
                                parent.setVisibility(View.VISIBLE);

                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jb = jsonObject.getJSONObject("ss");
                                String maximumText = jb.getString("Str1");
                                maxText.setText(Html.fromHtml(maximumText));

                                JSONArray jsonArray = jsonObject.getJSONArray("dataTable");
                                if (jsonArray.length() > 0) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    String firstProgress = jsonObject1.getString("Chance_Freq");
                                    String secondProgress = jsonObject1.getString("Severity_TypeFreq");

                                    String highProg = jsonObject1.getString("Severity_HighFreq");
                                    String mediumProg = jsonObject1.getString("Severity_MediumFreq");
                                    String lowProg = jsonObject1.getString("Severity_LowFreq");

                                    if (firstProgress != null) {

                                        Double f = Double.valueOf(firstProgress);

                                        firstBar.setProgress(f.intValue());
                                        happenText.setText("Disease happened in " + f.intValue() + "%" + " of this years");
                                    }

                                    if (secondProgress != null) {
                                        Double f = Double.valueOf(secondProgress);
                                        secondBar.setProgress(f.intValue());
                                    }

                                    if (highProg != null) {
                                        Double f = Double.valueOf(highProg);
                                        highBar.setProgress(f.intValue());
                                        highPerText.setText(f.intValue() + "%");
                                    }

                                    if (mediumProg != null) {
                                        Double f = Double.valueOf(mediumProg);
                                        mediumBar.setProgress(f.intValue());
                                        mediumPerText.setText(f.intValue() + "%");
                                    }

                                    if (lowProg != null) {
                                        Double f = Double.valueOf(lowProg);
                                        lowBar.setProgress(f.intValue());
                                        lowPerText.setText(f.intValue() + "%");
                                    }
                                }

                                dataList = new ArrayList<DiseaseBean>();
                                JSONArray jA = jsonObject.getJSONArray("dataTable2");
                                for (int i = 0; i < jA.length(); i++) {
                                    DiseaseBean bean = new DiseaseBean();
                                    bean.setDate(jA.getJSONObject(i).getString("Date"));
                                    bean.setMaxTem(jA.getJSONObject(i).getString("MaxTemp"));
                                    bean.setMinTemp(jA.getJSONObject(i).getString("MinTemp"));

                                    bean.setRain(jA.getJSONObject(i).getString("MinTemp"));
                                    bean.setHumidityEve(jA.getJSONObject(i).getString("HumidityEve"));
                                    bean.setHimidityMor(jA.getJSONObject(i).getString("HumidityMor"));
                                    dataList.add(bean);
                                }

                                if (dataList.size() > 0) {

                                    showBTN.setVisibility(View.VISIBLE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                System.out.println("Volley Error : " + error);
                parent.setVisibility(View.GONE);

                Toast.makeText(getActivity(),"Data not found", Toast.LENGTH_SHORT).show();
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}