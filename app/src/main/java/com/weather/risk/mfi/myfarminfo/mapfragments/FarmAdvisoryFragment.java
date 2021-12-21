package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLngBounds;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.adapter.CustomAdapter;
import com.weather.risk.mfi.myfarminfo.entities.CropQueryData;
import com.weather.risk.mfi.myfarminfo.entities.FarmAdvisoryDataSet;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.home.MyServiceActivity;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FarmAdvisoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CROP_QUERY_ARRAY = "cropQueryArray";
    private static final String LAT_LNG_STATE_ID = "latLngStateId";
    String farmID = null;
    // ArrayList<FarmAdvisoryDataSet> dataSet;
    ListView listView;
    String parcelableValue;
    EditText appliedBaselDoseN;
    EditText appliedBaselDoseP;
    EditText appliedBaselDoseK;
    EditText idealBaselDoseN;
    EditText idealBaselDoseP;
    EditText idealBaselDoseK;
    TextView idealCropDuration;
    String a_valueN;
    ArrayList<FarmAdvisoryDataSet> dataSet = new ArrayList<>();
    String latitude;
    String longitude;
    String stateId;
    RelativeLayout homeBTN, myServiceBTN;
    // TODO: Rename and change types of parameters
    private ArrayList<CropQueryData> cropQueryArray;
    private String latLngStateId;
    private Spinner queryCropSpinner;

    public FarmAdvisoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cropQueryData Parameter 1.
     * @param latLngStateId Parameter 2.
     * @return A new instance of fragment FarmAdvisoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FarmAdvisoryFragment newInstance(ArrayList<CropQueryData> cropQueryData, String latLngStateId, String fId) {
        FarmAdvisoryFragment fragment = new FarmAdvisoryFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CROP_QUERY_ARRAY, cropQueryData);
        args.putString(LAT_LNG_STATE_ID, latLngStateId);
        args.putString("f_id", fId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cropQueryArray = getArguments().getParcelableArrayList(CROP_QUERY_ARRAY);
            latLngStateId = getArguments().getString(LAT_LNG_STATE_ID);
            farmID = getArguments().getString("f_id");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farm_advisory, container, false);

        homeBTN = (RelativeLayout) view.findViewById(R.id.home_btn);
        myServiceBTN = (RelativeLayout) view.findViewById(R.id.service_btn);
        String userTypeID = AppConstant.userTypeID;
        // myServiceBTN.setVisibility(View.VISIBLE);
        if (userTypeID != null && (userTypeID.equalsIgnoreCase("1") || userTypeID.equalsIgnoreCase("2") || userTypeID.equalsIgnoreCase("18"))) {
            myServiceBTN.setVisibility(View.VISIBLE);
        } else {
            myServiceBTN.setVisibility(View.GONE);
        }
        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        myServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goFarMyServiceLogin();
            }
        });

        queryCropSpinner = (Spinner) view.findViewById(R.id.queryCropSpinner);
        listView = (ListView) view.findViewById(R.id.listView);
        appliedBaselDoseN = (EditText) view.findViewById(R.id.appliedBaselDoseN);
        appliedBaselDoseP = (EditText) view.findViewById(R.id.appliedBaselDoseP);
        appliedBaselDoseK = (EditText) view.findViewById(R.id.appliedBaselDoseK);

        idealBaselDoseN = (EditText) view.findViewById(R.id.idealBaselDoseN);
        idealBaselDoseP = (EditText) view.findViewById(R.id.idealBaselDoseP);
        idealBaselDoseK = (EditText) view.findViewById(R.id.idealBaselDoseK);
        idealCropDuration = (TextView) view.findViewById(R.id.textView);
/*
        TextView farmInfo = (TextView) view.findViewById(R.id.logo);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/kaushan_script_regular.otf");
        farmInfo.setTypeface(tf);
        farmInfo.setText("My Farm Info");
*/

        if (latLngStateId != null) {
            String[] latLngStateArray = latLngStateId.split(",");
            if (latLngStateArray.length == 3) {
                latitude = latLngStateArray[0];
                longitude = latLngStateArray[1];
                stateId = latLngStateArray[2];
            }
        }
        if (cropQueryArray != null) {
            ArrayList<String> cropsName = new ArrayList<>();
            for (CropQueryData data : cropQueryArray) {
                cropsName.add(data.getCrop());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, cropsName);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            queryCropSpinner.setAdapter(adapter);
        }

        queryCropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);

                getIncreaseRevenuRequest(cropQueryArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        idealCropDuration.setText(getResources().getString(R.string.Youshouldsow) + "-" + getResources().getString(R.string.and) + "-" + getResources().getString(R.string.tomaximize));


        return view;
    }

    private void getIncreaseRevenuRequest(final CropQueryData cropQueryData) {

        appliedBaselDoseN.setText(cropQueryData.getBasalDoseN().isEmpty() ? "0" : cropQueryData.getBasalDoseN());
        appliedBaselDoseP.setText(cropQueryData.getBasalDoseP().isEmpty() ? "0" : cropQueryData.getBasalDoseP());
        appliedBaselDoseK.setText(cropQueryData.getBasalDoseK().isEmpty() ? "0" : cropQueryData.getBasalDoseK());
        idealBaselDoseN.setText("");
        idealBaselDoseP.setText("");
        idealBaselDoseK.setText("");
        idealCropDuration.setText(getResources().getString(R.string.Youshouldsow) + "-" + getResources().getString(R.string.and) + "-" + getResources().getString(R.string.tomaximize));
        listView.setAdapter(null);

        System.out.println("cropId : " + cropQueryData.getCropID());
        String url = "https://myfarminfo.com/YFIRest.svc/Farm/YieldImprove/" + latitude + "/" + longitude + "/" + cropQueryData.getBasalDoseN() + "/" + cropQueryData.getBasalDoseP() + "/" + cropQueryData.getBasalDoseK() + "/" + cropQueryData.getCropID() + "/" + cropQueryData.getVariety() + "/" + cropQueryData.getSowPeriodForm() + "/" + stateId;
        url = url.replaceAll(" ", "%20");
        System.out.println("URL  " + url);
        StringRequest revenueRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            System.out.println("JSON RESPONSE STRING : " + response);

                            JSONArray jsonArray = new JSONArray(response);

                            JSONArray nutrientArrayObject = jsonArray.getJSONArray(0);
                            ArrayList<FarmAdvisoryDataSet> farmAdvisoryDataSet = new ArrayList<>();
                            for (int i = 0; i < nutrientArrayObject.length(); i++) {
                                JSONObject nutrientObject = nutrientArrayObject.getJSONObject(i);
                                String Nutrient = nutrientObject.getString("Nutrient");
                                String Content = nutrientObject.getString("Content");
                                String SoilApplication = nutrientObject.getString("SoilApplication");
                                FarmAdvisoryDataSet data = new FarmAdvisoryDataSet(Nutrient, Content, SoilApplication);
                                System.out.println(Nutrient + " , " + Content + " , " + SoilApplication);
                                farmAdvisoryDataSet.add(data);
                            }
                            listView.setAdapter(new CustomAdapter(getActivity(), farmAdvisoryDataSet));

                            JSONArray npkArrayObject = jsonArray.getJSONArray(1);
                            JSONObject npkJsonObject = npkArrayObject.getJSONObject(0);
                            String N = npkJsonObject.getString("N");
                            String P = npkJsonObject.getString("P");
                            String K = npkJsonObject.getString("K");
                            if (!cropQueryData.getBasalDoseN().trim().equals(N)) {
                                appliedBaselDoseN.setBackgroundColor(Color.RED);
                            }
                            if (!cropQueryData.getBasalDoseP().trim().equals(P)) {
                                appliedBaselDoseP.setBackgroundColor(Color.RED);
                            }
                            if (!cropQueryData.getBasalDoseK().trim().equals(K)) {
                                appliedBaselDoseK.setBackgroundColor(Color.RED);
                            }

                            System.out.println(N + " , " + P + " , " + K);
                            idealBaselDoseN.setText(N);
                            idealBaselDoseP.setText(P);
                            idealBaselDoseK.setText(K);

                            JSONArray durationArrayObject = jsonArray.getJSONArray(2);
                            JSONObject durationJsonObject = durationArrayObject.getJSONObject(0);
                            String SowingFrom = durationJsonObject.getString("SowingFrom");
                            String SowingTo = durationJsonObject.getString("SowingTo");
                            System.out.println(SowingFrom + " , " + SowingTo);
                            idealCropDuration.setText(getResources().getString(R.string.Youshouldsow) + " " + SowingFrom + " " + getResources().getString(R.string.and) + " " + SowingTo + " " + getResources().getString(R.string.tomaximize));


                        } catch (Exception e) {
                            e.printStackTrace();
                            //  Toast.makeText(getActivity(), "Response Formatting Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.Couldnotconnect), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to volley request queue
        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        revenueRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(revenueRequest);

    }

    private void getIncreaseYieldRequest(CropQueryData cropQueryData) {

        appliedBaselDoseN.setText(cropQueryData.getBasalDoseN().isEmpty() ? "0" : cropQueryData.getBasalDoseN());
        appliedBaselDoseP.setText(cropQueryData.getBasalDoseP().isEmpty() ? "0" : cropQueryData.getBasalDoseP());
        appliedBaselDoseK.setText(cropQueryData.getBasalDoseK().isEmpty() ? "0" : cropQueryData.getBasalDoseK());

        StringRequest revenueRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/YFIRest.svc/Farm/YieldImprove/" + latitude + "/" + longitude + "/" + cropQueryData.getBasalDoseN() + "/" + cropQueryData.getBasalDoseP() + "/" + cropQueryData.getBasalDoseK() + "/" + cropQueryData.getCropID() + "/" + cropQueryData.getVariety() + "/" + cropQueryData.getSowPeriodForm() + "/" + stateId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Increase Revenue Volley Response : " + response);
                        try {
                            response = response.trim();
                            response = response.substring(1, response.length() - 1);
                            response = response.replace("\\", "");
                            JSONArray locationArray = new JSONArray(response);
                            LatLngBounds.Builder bc = new LatLngBounds.Builder();
                            for (int i = 0; i < locationArray.length(); i++) {
                                JSONObject locationObject = locationArray.getJSONObject(i);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //  Toast.makeText(getActivity(), "Response Formatting Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley Error : " + error);
            }
        });

        // Adding request to volley request queue
        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        revenueRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(revenueRequest);

    }


    public void goFarMyServiceLogin() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.my_service_otp_popup, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final EditText inputUid = (EditText) promptsView.findViewById(R.id.UserID);
        final EditText inputPWD = (EditText) promptsView.findViewById(R.id.UserPassword);
        final EditText inputMobile = (EditText) promptsView.findViewById(R.id.mobile_no);

        final Button loginBTN = (Button) promptsView.findViewById(R.id.login_btn);


        alertDialogBuilder.setView(promptsView);
        final AlertDialog ad = alertDialogBuilder.show();


        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sss = inputUid.getText().toString();
                String ppp = inputPWD.getText().toString();
                String mobile = inputMobile.getText().toString();

                if (sss == null || sss.length() < 3) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.Pleaseentervalidemail), Toast.LENGTH_SHORT).show();
                } else {
                    if (ppp == null || ppp.length() < 2) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.Enteryourpassword), Toast.LENGTH_SHORT).show();
                    } else {

                        if (mobile == null || mobile.length() < 10 || AppManager.getInstance().isMobileNoValid(mobile) == false) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.Pleaseentervalid), Toast.LENGTH_SHORT).show();
                        } else {
                            ad.dismiss();
                            new MyServiceAsyncTask(sss, ppp, mobile).execute();
                        }

                    }
                }

            }
        });


    }

    public void successOTP(final String otp, final String mobileNO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.Success));
        builder.setMessage(getResources().getString(R.string.Successfullysentotp))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent in = new Intent(getActivity(), MyServiceActivity.class);
                        in.putExtra("otp", otp + "");
                        in.putExtra("f_id", farmID);
                        in.putExtra("mobile_no", mobileNO);
                        startActivity(in);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyServiceAsyncTask extends AsyncTask<Void, Void, String> {
        String email_f;
        String pass_f;
        String mobile_f;
        String result = "";
        TransparentProgressDialog progressDialog;

        public MyServiceAsyncTask(String emai, String pass, String mob) {
            this.email_f = emai;
            this.pass_f = pass;
            this.mobile_f = mob;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new TransparentProgressDialog(
                    getActivity(), getResources().getString(R.string.Pleasewait));
            progressDialog.setCancelable(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // cancel AsyncTask
                    cancel(false);
                }
            });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sendRequest = null;
            try {

                sendRequest = AppManager.getInstance().otp_password + email_f + "/" + pass_f + "/" + mobile_f;
//                sendRequest = AppManager.getInstance().otp_password + email_f + "/" + pass_f + "/" + mobile_f+"/MFI";
                Log.d("sync forgot data", sendRequest);
                return AppManager.getInstance().httpRequestGetMethod(sendRequest);


            } catch (Exception ex) {
                ex.printStackTrace();

                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
            if (result.contains(AppConstant.SERVER_CONNECTION_ERROR)) {

                Toast.makeText(getActivity(), AppConstant.SERVER_CONNECTION_ERROR, Toast.LENGTH_LONG).show();

            } else if (result.contains("NoData")) {

                Toast.makeText(getActivity(), getResources().getString(R.string.emailiddoesnotexist), Toast.LENGTH_LONG).show();

            } else {
                Log.v("result", result + "");
                result = result.trim();

                result = result.replace("\\", "");
                result = result.replace("\\", "");
                result = result.replace("\"[", "[");
                result = result.replace("]\"", "]");
                result = result.replace("\"{", "{");
                result = result.replace("}\"", "}");
                try {
                    JSONObject jb = new JSONObject(result);
                    if (jb.has("Str2")) {
                        String otpp = jb.getString("Str2");
                        successOTP(otpp, mobile_f);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            progressDialog.dismiss();

        }
    }


}
