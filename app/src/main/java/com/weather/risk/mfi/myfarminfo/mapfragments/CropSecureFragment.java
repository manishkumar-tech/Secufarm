package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
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
import com.weather.risk.mfi.myfarminfo.adapter.CropSecureAdapter;
import com.weather.risk.mfi.myfarminfo.adapter.NutritionAdapter;
import com.weather.risk.mfi.myfarminfo.bean.NutritionBean;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.LocationData;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.pest_disease.CropBean;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.PlaceDetailsJSONParser;
import com.weather.risk.mfi.myfarminfo.utils.PlaceJSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 19-03-2018.
 */
@SuppressLint("ValidFragment")
public class CropSecureFragment extends Fragment {
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
    String d1, d2;

    Double latitude, longitude;

    String selected_period = null;
    String selected_product = null;

    public CropSecureFragment(String la, String lon) {

        if (la != null && lon != null) {
            latitude = Double.parseDouble(la);
            longitude = Double.parseDouble(lon);
        } else {
            latitude = LatLonCellID.lat;
            longitude = LatLonCellID.lon;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    final int PLACES = 0;
    final int PLACES_DETAILS = 1;
    DownloadTask placeDetailsDownloadTask;
    ParserTask placesParserTask;
    ParserTask placeDetailsParserTask;
    int selected = 0;
    Spinner productSpinner;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.crop_secure, container, false);

        String aa = AppConstant.latitude;
        String bb = AppConstant.longitude;


        if (aa!=null && aa.length()>5){
            latitude = Double.valueOf(aa);
            longitude = Double.valueOf(bb);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.cropsecure_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        atvPlaces = (AutoCompleteTextView) view.findViewById(R.id.farm_location);
        productSpinner = (Spinner) view.findViewById(R.id.product_spinner);


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            atvPlaces.setText(address+"");
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadCropData();

        selected = 0;
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4) {
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());
                    if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                        if (selected == 1) {
                            try {
                                atvPlaces.dismissDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                atvPlaces.showDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                    try {
                        if (selected == 1) {
                            try {
                                atvPlaces.dismissDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                atvPlaces.showDropDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//
        atvPlaces.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                    if (selected == 1) {
                        try {
                            atvPlaces.dismissDropDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            atvPlaces.showDropDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });


        // Setting an item click listener for the AutoCompleteTextView dropdown list
        atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long id) {

                selected = 1;
                try {
                    atvPlaces.dismissDropDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);
                atvPlaces.setText(hm.get("description"));

                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));


                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);
                if (selected == 1) {
                    try {
                        atvPlaces.dismissDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        atvPlaces.showDropDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console

            String key = "key=" + getResources().getString(R.string.browser_key);  //previous key
            //  String key = "key="+getResources().getString(R.string.browser_key);
            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // place searched by country
            String country = "components=country:in";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + country + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                System.out.println("URL  : " + url);
                // Fetching the data from we service
                data = downloadUrl(url);
                System.out.println("DATA : " + data); //complete address of locations

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.trim().length() > 0) {
                // Creating ParserTask
                parserTask = new ParserTask(PLACES);

                // Starting Parsing the JSON string returned by Web Service
                parserTask.execute(result);
                if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                    if (selected == 1) {
                        atvPlaces.dismissDropDown();
                    } else {
                        atvPlaces.showDropDown();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Could not connect to the location API", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while downloading url" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    ProgressDialog pDialog;

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected void onPreExecute() {
            if (parserType == PLACES_DETAILS) {
                pDialog = ProgressDialog.show(getActivity(), "",
                        "Please wait.....", true);
            }
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                        //we will pick here state

                        //     JSONObject  job = new JSONObject(jsonData[0]);


                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }


        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    if (result != null) {
                        String[] from = new String[]{"description"};
                        int[] to = new int[]{android.R.id.text1};

                        // Creating a SimpleAdapter for the AutoCompleteTextView
                        SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, android.R.layout.simple_list_item_1, from, to);

                        // Setting the adapter
                        atvPlaces.setAdapter(adapter);
                        if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                            if (selected == 1) {
                                atvPlaces.dismissDropDown();
                            } else {
                                atvPlaces.showDropDown();
                            }
                        }
                    }
                    break;
                case PLACES_DETAILS:
                    if (pDialog != null) {
                        pDialog.cancel();
                    }
                    if (result != null) {


                        HashMap<String, String> hm = result.get(0);


                        // Getting latitude from the parsed data
                        latitude = Double.parseDouble(hm.get("lat"));
                        // Getting longitude from the parsed data
                        longitude = Double.parseDouble(hm.get("lng"));


                        selected = 0;

                        System.out.println("Place Detail Latitude : " + latitude + " longitude : " + longitude);

                    }

                    break; //End of second case
            }
        }
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("cropsecure-------", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jobject = null;
            try {

                jobject = new JSONObject(result);
                JSONObject jsonObject = jobject.getJSONObject("result");
                JSONArray jsonArray = jsonObject.getJSONArray("address_components");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    JSONArray types = jObject.getJSONArray("types");
                    for (int k = 0; k < types.length(); k++) {
                        if (types.optString(k).contains("administrative_area_level_1")) {
                            AppConstant.state = jObject.getString("long_name");
                            //  System.out.println("State Name---" + AppConstant.state);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    placesParserTask.execute(result);
                    if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                        if (selected == 1) {
                            atvPlaces.dismissDropDown();
                        } else {
                            atvPlaces.showDropDown();
                        }
                    }
                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
                    if (AppConstant.APP_MODE != AppConstant.OFFLINE) {
                        if (selected == 1) {
                            atvPlaces.dismissDropDown();
                        } else {
                            atvPlaces.showDropDown();
                        }
                    }
            }
        }
    }

    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=" + getResources().getString(R.string.browser_key);

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }

    public void loadCropData() {

        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "",
                "Fetching products Please wait...", true);
        Log.v("cropUrl", "https://myfarminfo.com/yfirest.svc/Insurance/Products"+ "/" + latitude + "/" + longitude);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Insurance/Products"+ "/" + latitude + "/" + longitude,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley crop Response : " + response);

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");


                        DataBean bean = new DataBean();
                        bean = getCropTypeList(response);
                        ArrayList<CropBean> cityList = new ArrayList<CropBean>();
                        cityList = bean.getCropList();
                        final ArrayList<String> productList = new ArrayList<String>();
                        final ArrayList<String> productIdList = new ArrayList<String>();

                        productList.add("--Select--");
                        productIdList.add("0");

                        for (int i = 0; i < cityList.size(); i++) {


                            String na = cityList.get(i).getCropName();
                            String id = cityList.get(i).getCropId();
                            productList.add(na);
                            productIdList.add(id);

                        }


                        ArrayAdapter<String> productTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, productList);
                        productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        productSpinner.setAdapter(productTypeAdapter);


                        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position >= 0) {

                                    selected_product = productIdList.get(position);

                                    getCropSecureList();

                                } else {
                                    selected_product = null;
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
                    cropTypeList.add(typeBean);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataBean.setCropList(cropTypeList);


        }

        return dataBean;
    }



    /*public void loadProductData() {

        final ArrayList<String> productList = new ArrayList<String>();
        final ArrayList<String> productIdList = new ArrayList<String>();

       // productList.add("--Select--");
        productList.add("Potato Product");
        productList.add("Wheat Product");
        productList.add("Gram Product");
        productList.add("Mustard Product");


     //   productIdList.add("0");
        productIdList.add("11");
        productIdList.add("22");
        productIdList.add("18");
        productIdList.add("19");




        ArrayAdapter<String> productTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, productList);
        productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productSpinner.setAdapter(productTypeAdapter);


        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position >= 0) {

                    selected_product = productIdList.get(position);

                    getCropSecureList();

                } else {
                    selected_product = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
*/

    private void getCropSecureList() {


        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching crop secure data please wait... ");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String ss = "https://myfarminfo.com/yfirest.svc/Insurance/Payoff/" + selected_product + "/" + latitude + "/" + longitude;
        ss = AppManager.getInstance().removeSpaceForUrl(ss);

        StringRequest stringVarietyRequest = new StringRequest(Request.Method.GET, ss,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String seasonResponse) {
                        try {

                            seasonResponse = seasonResponse.trim();
                            seasonResponse = ""+Html.fromHtml(seasonResponse);
                            seasonResponse = seasonResponse.replace("\\", "");
                            seasonResponse = seasonResponse.replace("\"{", "{");
                            seasonResponse = seasonResponse.replace("}\"", "}");
                            seasonResponse = seasonResponse.replace("\"[", "[");
                            seasonResponse = seasonResponse.replace("]\"", "]");


                            System.out.println("crop secure Respose : " + seasonResponse);


                            ArrayList<String> messageList = new ArrayList<String>();
                            JSONObject jb = new JSONObject(seasonResponse);

                            JSONArray ja = jb.getJSONArray("DT");
                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject jsonObject = ja.getJSONObject(i);
                                String d1 = jsonObject.getString("Disease1");
                                if (d1 != null && d1.length() > 0) {


                                    messageList.add(d1);
                                }

                                String d2 = jsonObject.getString("Disease2");
                                if (d2 != null && d2.length() > 0) {


                                    messageList.add(d2);
                                }

                                String d3 = jsonObject.getString("Disease3");
                                if (d1 != null && d1.length() > 0) {


                                    messageList.add(d3);
                                }

                                String d4 = jsonObject.getString("Disease4");
                                if (d4 != null && d4.length() > 0) {

                                    messageList.add(d4);
                                }


                            }

                            if (messageList.size() > 0) {

                                recyclerView.setVisibility(View.VISIBLE);
                                CropSecureAdapter adapter = new CropSecureAdapter(getActivity(), messageList);
                                recyclerView.setAdapter(adapter);

                            } else {

                                Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.GONE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Not able to connect with server", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringVarietyRequest.setRetryPolicy(policy);


        AppController.getInstance().addToRequestQueue(stringVarietyRequest);
    }


}
