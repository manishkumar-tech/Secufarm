package com.weather.risk.mfi.myfarminfo.isconbalaji;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.entities.DataBean;
import com.weather.risk.mfi.myfarminfo.entities.VillageBean;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.ConnectionDetector;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAppVersion;

public class VisitFragment  extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    public VisitFragment() {
        // Required empty public constructor
    }
    private static View view;
    Double lati,longi;
    String date;
    String ss = null;

    Spinner farmSpinner, districtSpinner, villageSpinner, subDistrictSpinner;
    RelativeLayout subDistrictLay;
    private ArrayList<String> farmArr;

    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1;
    private int REQUEST_CAMERA_START2 = 3, SELECT_FILE_START2 = 2;
    private int REQUEST_CAMERA_START3 = 5, SELECT_FILE_START3 = 4;
    private int REQUEST_CAMERA_START4 = 7, SELECT_FILE_START4 = 8;
    String uploadedPicName = null;
    private String userChoosenTask;
    ArrayList<String> imageList;

    ArrayList<String> str = new ArrayList<String>();
    ArrayList<String> strFarmId = new ArrayList<String>();
    DBAdapter db;
    private int mYear, mMonth, mDay;

    Button chooseImageBTN,submitBTN;
    ImageView imageView1/*,imageView2,imageView3,imageView4*/;
    String imageString1,imageString2,imageString3,imageString4;

    private double latitude, longitude;

   EditText commentText;
   String farmID=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        view = (LinearLayout) inflater.inflate(R.layout.visit_fragment, container, false);



        db = new DBAdapter(getActivity());

        ss = AppConstant.role;
        subDistrictLay = (RelativeLayout) view.findViewById(R.id.sub_distLay);
        subDistrictSpinner = (Spinner) view.findViewById(R.id.sub_distSpinner);

        districtSpinner = (Spinner) view.findViewById(R.id.distSpinner);
        villageSpinner = (Spinner) view.findViewById(R.id.villSpinner);
        farmSpinner = (Spinner) view.findViewById(R.id.farmSpinner);

        imageList = new ArrayList<String>();
        chooseImageBTN = (Button)view.findViewById(R.id.image_visit_btn);
        commentText =(EditText)view.findViewById(R.id.comment_visit);
        submitBTN = (Button)view.findViewById(R.id.submit_visit_btn);
        chooseImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImagePopup();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lati = LatLonCellID.lat;
                longi = LatLonCellID.lon;
                String commm = commentText.getText().toString().trim();
                if (farmID!=null) {
                    if (AppConstant.user_id!=null && AppConstant.user_id.length()>0) {
                        if (commm!=null && commm.length()>1) {

                            String jsonParameterString = createJsonParameterForVisit(farmID,AppConstant.user_id,commm);
                            new submitVisitMethod(jsonParameterString).execute();
                        }else {
                            Toast.makeText(getActivity(),"Please enter visit descriptions",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(getActivity(),"User id not found",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(),"Please select farm",Toast.LENGTH_SHORT).show();
                }


                Log.v("logggg",farmID+"--"+date+"--"+lati+","+longi);
            }
        });

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         date = df.format(Calendar.getInstance().getTime());
         lati = LatLonCellID.lat;
         longi = LatLonCellID.lon;


        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if (connectionDetector.isConnectingToInternet()) {

            subDistrictLay.setVisibility(View.GONE);
            String acc = AppConstant.user_id;
            if (acc != null) {
                loadDistData(acc);
            } else {
                Toast.makeText(getActivity(), "Account id not found please login once again", Toast.LENGTH_SHORT).show();
            }
        } else {
            noInternetMethoddd();
        }
        setSubtitleLanguage();
        return view;
    }

    public void loadDistData(String userI) {
        farmID = null;
        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "", "Fetching Districts. Please wait...", true);
        Log.v("knsknklanl", "https://myfarminfo.com/yfirest.svc/Clients/GetDistrictByUserID/" + userI);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GetDistrictByUserID/" + userI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley village Response : " + response);

                        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("district", response);
                        editor.apply();

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");

                        final ArrayList<String> districtList = new ArrayList<>();
                        final ArrayList<String> subDistrictList = new ArrayList<>();
                        final ArrayList<String> districtID = new ArrayList<>();

                        if (ss != null && ss.equalsIgnoreCase("Admin")) {
                            districtList.add("select District");
                            districtID.add("0");
                        }
                        if (response != null) {
                            try {

                                JSONObject jb = new JSONObject(response);
                                JSONArray jsonArray = jb.getJSONArray("DT");

                                for (int i = 0; i < jsonArray.length(); i++) {


                                    String dist = jsonArray.getJSONObject(i).getString("District");
                                    String disti = jsonArray.getJSONObject(i).getString("District_ID");
                                    if (jsonArray.getJSONObject(i).has("Sub_district")) {
                                        String sub_disti = jsonArray.getJSONObject(i).getString("Sub_district");
                                        subDistrictList.add(sub_disti + "," + dist);
                                    }
                                    if (i == 0) {
                                        districtList.add(dist);
                                        districtID.add(disti);
                                    } else {

                                        if (!districtList.get(districtList.size()-1).equalsIgnoreCase(dist)) {
                                            districtList.add(dist);
                                            districtID.add(disti);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> varietyArrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, districtList); //selected item will look like a spinner set from XML
                        varietyArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        districtSpinner.setAdapter(varietyArrayAdapter1);

                        String compareValue = AppConstant.selected_district;
                        if (compareValue != null && compareValue.length() > 2) {
                            int spinnerPosition = varietyArrayAdapter1.getPosition(compareValue);
                            districtSpinner.setSelection(spinnerPosition);
                        }
                        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                if (ss != null && ss.equalsIgnoreCase("Admin")) {
                                    if (position > 0) {
                                        //    districtSpinner.setSelection(position);
                                        String sssa = districtList.get(position);
                                        AppConstant.selected_district = districtList.get(position);
                                        final ArrayList<String> filterSubDistrict = new ArrayList<String>();

                                        if (ss != null && ss.equalsIgnoreCase("Admin")) {
                                            filterSubDistrict.add("-select sub-district-");
                                        }
                                        for (int i = 0; i < subDistrictList.size(); i++) {

                                            String ssss = subDistrictList.get(i);
                                            Log.v("lenghttt",filterSubDistrict.size()+"--"+ssss+"--"+subDistrictList.size());

                                            if (ssss != null) {
                                                List<String> sepList = Arrays.asList(ssss.split(","));
                                                if (sepList.size() > 1) {
                                                    if (sepList.get(1).equalsIgnoreCase(sssa)) {
                                                        filterSubDistrict.add(sepList.get(0));
                                                    }
                                                }
                                            }
                                        }

                                        final String dis_id = districtID.get(position);



                                        if (AppConstant.isSubdistrict != null && AppConstant.isSubdistrict.equalsIgnoreCase("yes")) {

                                            subDistrictLay.setVisibility(View.VISIBLE);

                                            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, filterSubDistrict); //selected item will look like a spinner set from XML
                                            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            subDistrictSpinner.setAdapter(arrayAdapter1);

                                            String compareValue = AppConstant.selected_SubDistrict;
                                            if (compareValue != null && compareValue.length() > 2) {
                                                int spinnerPosition = arrayAdapter1.getPosition(compareValue);
                                                subDistrictSpinner.setSelection(spinnerPosition);
                                            }
                                            subDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                    if (position>0) {
                                                        AppConstant.selected_SubDistrict = filterSubDistrict.get(position);
                                                        String ID = filterSubDistrict.get(position);
                                                        String acc = AppConstant.user_id;
                                                        String sas = dis_id+";"+ID;
                                                        loadVillageData(acc, sas);

                                                    }else {
                                                        AppConstant.selected_SubDistrict = "";
                                                    }


                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }else {
                                            String ID = districtID.get(position);
                                            String acc = AppConstant.user_id;

                                            loadVillageData(acc, ID);
                                        }


                                    } else {
                                        AppConstant.selected_district = "";
                                    }
                                } else {
                                    if (position >= 0) {
                                        //    districtSpinner.setSelection(position);

                                        AppConstant.selected_district = districtList.get(position);
                                        String ssaa = districtList.get(position);
                                        final String dis_id = districtID.get(position);

                                        final ArrayList<String> filterSubDistrict = new ArrayList<String>();
                                        filterSubDistrict.add("-select sub-district-");
                                        for (int i = 0; i < subDistrictList.size(); i++) {

                                            String ssss = subDistrictList.get(i);
                                            if (ssss != null) {
                                                List<String> sepList = Arrays.asList(ssss.split(","));
                                                if (sepList.size() > 1) {
                                                    if (sepList.get(1).equalsIgnoreCase(ssaa)) {
                                                        filterSubDistrict.add(sepList.get(0));
                                                    }
                                                }
                                            }
                                        }

                                        if (AppConstant.isSubdistrict != null && AppConstant.isSubdistrict.equalsIgnoreCase("yes")) {
                                            subDistrictLay.setVisibility(View.VISIBLE);
                                            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, filterSubDistrict); //selected item will look like a spinner set from XML
                                            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            subDistrictSpinner.setAdapter(arrayAdapter1);

                                            String compareValue = AppConstant.selected_SubDistrict;
                                            if (compareValue != null && compareValue.length() > 2) {
                                                int spinnerPosition = arrayAdapter1.getPosition(compareValue);
                                                subDistrictSpinner.setSelection(spinnerPosition);
                                            }
                                            subDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                    if (position>0) {
                                                        AppConstant.selected_SubDistrict = filterSubDistrict.get(position);
                                                        String ID = filterSubDistrict.get(position);
                                                        String acc = AppConstant.user_id;
                                                        String sas = dis_id+";"+ID;
                                                        loadVillageData(acc, sas);
                                                    }else {
                                                        AppConstant.selected_SubDistrict = "";
                                                    }


                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }else {
                                            String ID = districtID.get(position);
                                            String acc = AppConstant.user_id;


                                            loadVillageData(acc, ID);
                                        }


                                    } else {
                                        AppConstant.selected_district = "";
                                    }
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




    public void loadVillageData(String userI, String d_id) {
        farmID = null;
        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "", "Fetching Village\nPlease wait...", true);
        Log.v("knsknklanl", "hhttps://myfarminfo.com/yfirest.svc/Clients/GetVillageNameByDistrictID/" + userI);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GetVillageNameByDistrictID/" + userI + "/" + d_id + "/No",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialoug.cancel();
                        // Display the first 500 characters of the response string.
                        System.out.println("Volley village Response : " + response);

                        SharedPreferences prefs = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("village", response);
                        editor.apply();

                        response = response.trim();
                        response = response.substring(1, response.length() - 1);
                        response = response.replace("\\", "");

                        final ArrayList<String> villageList = new ArrayList<>();
                        final ArrayList<String> villageID = new ArrayList<>();


                        if (ss != null && ss.equalsIgnoreCase("Admin")) {
                            villageList.add("select village");
                            villageID.add("0");

                        }
                        if (response != null) {
                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                //    JSONArray jsonArray = jb.getJSONArray("DT");

                                for (int i = 0; i < jsonArray.length(); i++) {


                                    String vil = jsonArray.getJSONObject(i).getString("Village");
                                    String vil_id = jsonArray.getJSONObject(i).getString("Village_ID");
                                    villageList.add(vil);
                                    villageID.add(vil_id);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        ArrayAdapter<String> varietyArrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, villageList); //selected item will look like a spinner set from XML
                        varietyArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        villageSpinner.setAdapter(varietyArrayAdapter1);

                        String compareValue = AppConstant.selected_village;
                        if (compareValue != null && compareValue.length() > 2) {
                            int spinnerPosition = varietyArrayAdapter1.getPosition(compareValue);
                            villageSpinner.setSelection(spinnerPosition);
                        }
                        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (ss != null && ss.equalsIgnoreCase("Admin")) {
                                    if (position > 0) {
                                        //    districtSpinner.setSelection(position);

                                        AppConstant.selected_village = villageList.get(position);

                                        String ID = villageID.get(position);
                                        String acc = AppConstant.user_id;
                                        farmID = null;
                                        loadFarmData(acc, ID);
                                    } else {
                                        AppConstant.selected_village = "";
                                    }
                                } else {
                                    if (position >= 0) {
                                        //    districtSpinner.setSelection(position);

                                        AppConstant.selected_village = villageList.get(position);

                                        String ID = villageID.get(position);
                                        String acc = AppConstant.user_id;
                                        farmID = null;
                                        loadFarmData(acc, ID);
                                    } else {
                                        AppConstant.selected_village = "";
                                    }
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


    public void selectImagePopup() {


        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.visit_image_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button submitImageBTN = (Button) dialog.findViewById(R.id.submit_image_btn);
        imageView1 = (ImageView) dialog.findViewById(R.id.choose_image1);
       /*  imageView2 = (ImageView) dialog.findViewById(R.id.choose_image2);
         imageView3 = (ImageView) dialog.findViewById(R.id.choose_image3);
         imageView4 = (ImageView) dialog.findViewById(R.id.choose_image4);
*/
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageString1 = null;
                imageView1.setImageBitmap(null);

                selectImage1();
            }
        });

      /*  imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageString2 = null;
                imageView2.setImageBitmap(null);
                selectImage2();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageString3 = null;
                imageView3.setImageBitmap(null);
                selectImage3();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageString4 = null;
                imageView4.setImageBitmap(null);
                selectImage4();
            }
        });*/



        submitImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                String usi = AppConstant.user_id;

                if (imageString1!=null && imageString1.length()>10){
                    uploadImage();

                }/*else {

                    if (stt !=null && stt.length()>1) {
                        if (usi != null && selected_taskId != null) {
                            String jsonParameterString = createJsonParameterForComments(usi, stt);
                            // AppManager.getInstance().removeSpaceForUrl(jsonParameterString);

                            new sendCommentsImage(jsonParameterString).execute();
                        } else {
                            Toast.makeText(getActivity(), "User id or task id is missing", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "Please enter comment", Toast.LENGTH_SHORT).show();

                    }
                }*/

            }
        });

        dialog.show();
    }

    private void uploadImage() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;
        String usi = AppConstant.user_id;

        Double lat = LatLonCellID.lat;
        Double lon = LatLonCellID.lon;
        Log.v("imageLat_long",lat+","+lon);

        try {
            jsonObject = new JSONObject();
            jsonObject.putOpt("ImageString", imageString1);
            jsonObject.putOpt("UserID", usi);
            //Add new
            jsonObject.putOpt("AppVersion", getAppVersion(getActivity()));
//            jsonObject.putOpt("TaskID", selected_taskId);

            // jsonObject.putOpt("Lat_Lng", lat+","+lon);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadImageURL_SCHEDULER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                getResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                Log.v("Response vishal coupon", "" + error.toString());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void getResponse(JSONObject response) {

        uploadedPicName = null;

        if (response != null) {

            try {

                String res = response.toString();
                res = res.replace("\"[","[");
                res = res.replace("]\"","]");
                res = res.replace("\\", "");



                JSONObject jsonObject = new JSONObject(res);
                JSONArray js = jsonObject.getJSONArray("uploadBase64Image_SchedulerResult");
                uploadedPicName = js.get(0).toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Successful upload image");
                builder.setMessage("You have successfully uploaded your visit image, do you want to upload more images?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        selectImagePopup();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                builder.show();

                // uploadedPicName = response.getString("uploadBase64Image_SchedulerResult");
                if (uploadedPicName!=null) {
                    imageList.add(uploadedPicName);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

 /*   private class sendCommentsImage extends AsyncTask<Void, Void, String> {

        String result = null;
        String createdString;

        public sendCommentsImage(String createdString) {
            this.createdString = createdString;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Processing... ");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().sendCommentURL;

            response = AppManager.getInstance().httpRequestPutMethodReport(sendPath, createdString);


            System.out.println("AllResponse :---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                response = response.trim();
                // response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");
                System.out.println("AllResponse : " + response);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Response Formatting Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String createJsonParameterForComments(String fd, String com) {
        String parameterString = "";


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("UserID", fd);
            jsonObject.put("Comment", com);
            jsonObject.put("TaskID", selected_taskId);
            Log.v("user_id", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        return parameterString;
    }
*/

    @Override
    public void onResume() {
        super.onResume();

       setSubtitleLanguage();
    }



    private void selectImage1() {
        final CharSequence[] items = {"Take Photo",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(getActivity());
                    if (resultCam) {
                        cameraIntent1();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(getActivity());
                    if (resultCam) {
                        galleryIntent1();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage2() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(getActivity());
                    if (resultCam) {
                        cameraIntent2();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(getActivity());
                    if (resultCam) {
                        galleryIntent2();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage3() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(getActivity());
                    if (resultCam) {
                        cameraIntent3();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(getActivity());
                    if (resultCam) {
                        galleryIntent3();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage4() {
        final CharSequence[] items = {"Take Photo", "Select From Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(getActivity());
                    if (resultCam) {
                        cameraIntent4();
                    }

                } else if (items[item].equals("Select From Gallery")) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(getActivity());
                    if (resultCam) {
                        galleryIntent4();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START1);
    }

    private void cameraIntent1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START1);
    }

    private void galleryIntent2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START2);
    }

    private void cameraIntent2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START2);
    }

    private void galleryIntent3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START3);
    }

    private void cameraIntent3() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START3);
    }

    private void galleryIntent4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START4);
    }

    private void cameraIntent4() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_START4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_START1) {
                onCaptureImageResult1(data);
            } else if (requestCode == SELECT_FILE_START1) {
                onSelectFromGalleryResult1(data);
            }/*else if (requestCode == REQUEST_CAMERA_START2) {
                onCaptureImageResult2(data);
            } else if (requestCode == SELECT_FILE_START2) {
                onSelectFromGalleryResult2(data);
            }else if (requestCode == REQUEST_CAMERA_START3) {
                onCaptureImageResult3(data);
            } else if (requestCode == SELECT_FILE_START3) {
                onSelectFromGalleryResult3(data);
            }else if (requestCode == REQUEST_CAMERA_START4) {
                onCaptureImageResult4(data);
            } else if (requestCode == SELECT_FILE_START4) {
                onSelectFromGalleryResult4(data);
            }*/
        }
    }

    private void onCaptureImageResult1(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView1.setImageBitmap(thumbnail);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString1 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView1.setImageBitmap(bm);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        assert bm != null;
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString1 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
    }

    public void loadFarmData(String userI, String vil) {
        final ProgressDialog dialoug = ProgressDialog.show(getActivity(), "", "Fetching Villages. Please wait...", true);
        Log.v("knsknklanl", "https://myfarminfo.com/yfirest.svc/Clients/GetVillageNameByDistrictID/" + userI + "/No/" + vil);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://myfarminfo.com/yfirest.svc/Clients/GetVillageNameByDistrictID/" + userI + "/No/" + vil,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
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
                        farmArr = new ArrayList<String>();
                        if (ss != null && ss.equalsIgnoreCase("Admin")) {
                            farmArr.add("select farm");
                        }
                        for (int i = 0; i < cityList.size(); i++) {
                            farmArr.add(cityList.get(i).getVilageName());
                        }

                        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, farmArr);
                        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        farmSpinner.setAdapter(eventTypeAdapter);


                        String compareValue = AppConstant.selected_farm;

                        if (AppConstant.role != null && !AppConstant.role.equalsIgnoreCase("Admin")) {

                            if (compareValue != null && compareValue.length() > 2) {
                                int spinnerPosition = eventTypeAdapter.getPosition(compareValue);
                                Log.v("ffffffff", farmArr.size() + "--" + compareValue + "--" + spinnerPosition);

                                farmSpinner.setSelection(spinnerPosition);
                            }
                        }
                        final DataBean finalBean = bean;
                        farmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (ss != null && ss.equalsIgnoreCase("Admin")) {
                                    if (position > 0) {
                                        String aaan = finalBean.getCityList().get(position - 1).getVillageID();

                                        //    villageName = finalBean.getCityList().get(position - 1).getVilageName();
                                        if (aaan!=null){
                                            aaan = aaan.trim();
                                            String[] arrSplit = aaan.split("-");
                                            if (arrSplit.length>0){
                                                farmID = arrSplit[0];
                                            }else {
                                                farmID = null;
                                            }
                                        }else {
                                            farmID = null;
                                        }


                                    }else {
                                        farmID = null;
                                    }
                                }else {
                                    if (position >= 0) {
                                        String aaan = finalBean.getCityList().get(position ).getVillageID();

                                        //    villageName = finalBean.getCityList().get(position - 1).getVilageName();
                                        if (aaan!=null){
                                            aaan = aaan.trim();
                                            String[] arrSplit = aaan.split("-");
                                            if (arrSplit.length>0){
                                                farmID = arrSplit[0];
                                            }else {
                                                farmID = null;
                                            }
                                        }else {
                                            farmID = null;
                                        }


                                    }else {
                                        farmID = null;
                                    }
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
                    typeBean.setVilageName(jsonArray.getJSONObject(i).getString("FarmerName"));
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


    private void noInternetMethoddd() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Internet Error").
                setMessage("Do You want to Refresh?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        String acc = AppConstant.user_id;
                        if (acc != null) {
                            subDistrictLay.setVisibility(View.GONE);
                            loadDistData(acc);
                        } else {
                            Toast.makeText(getActivity(), "Account id not found please login once again", Toast.LENGTH_SHORT).show();
                        }
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


    private String createJsonParameterForVisit(String fd, String us_id,String comment) {
        String parameterString = "";


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("farmID", ""+fd);
            jsonObject.put("UserID", ""+us_id);
            jsonObject.put("msg", ""+comment);
            jsonObject.put("Reqtype", "FarmVisit");
            jsonObject.put("optionalparametre", AppConstant.visible_Name+"");
            jsonObject.put("latitude", ""+lati);
            jsonObject.put("longitude", ""+longi);
            if (imageList.size()>0) {
                JSONArray jsonArray = new JSONArray();
                for (int s = 0; s < imageList.size(); s++) {
                    jsonArray.put(imageList.get(s));
                }
                jsonObject.put("Images", jsonArray.toString());
            }else {
                jsonObject.put("Images", "");
            }
            Log.v("request_parameter", jsonObject.toString() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameterString = jsonObject.toString();

        return parameterString;
    }


    private class submitVisitMethod extends AsyncTask<Void, Void, String> {

        String result = null;
        String createdString;

        public submitVisitMethod(String createdString) {
            this.createdString = createdString;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Processing . . ");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().submit_visit_log_putapi;
            response = AppManager.getInstance().httpRequestPutMethodReport(sendPath, createdString);
            System.out.println("AllResponse :---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            progressDialog.dismiss();
            try {
                response = response.trim();
                // response = response.substring(1, response.length() - 1);
                response = response.replace("\\", "");
                response = response.replace("\"[", "[");
                response = response.replace("]\"", "]");
                System.out.println("Visit_submit_Response : " + response);
                if (response!=null){
                    JSONObject jb = new JSONObject(response);
                    String ssssaaa = jb.getString("SaveLogsResult");
                    if (ssssaaa!=null && ssssaaa.equalsIgnoreCase("Success")){
                        successAlert();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Response Formatting Error", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void successAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Dear " + AppConstant.visible_Name + "\n\n" + "You have successfully submitted your visit log.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Fragment fragment = new VisitFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }



    private void setSubtitleLanguage() {
        SharedPreferences myPreference = getActivity().getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, getActivity().MODE_PRIVATE);
        String languagePreference = myPreference.getString(getResources().getString(R.string.language_pref_key), "1");
        int languageConstant = Integer.parseInt(languagePreference);

        System.out.println("language Constant : " + languageConstant);
        switch (languageConstant) {
            case 1:
                setEnglishText();
                break;
            case 2:
                setGujratiText();
                break;
            case 3:
                setHindiText();
                break;
            default:
                setEnglishText();
        }


    }

    private void setGujratiText() {


        chooseImageBTN.setText("છબી પસંદ કરો");
        submitBTN.setText("સબમિટ કરો");
    }

    private void setEnglishText() {


        chooseImageBTN.setText("Select Image");
        submitBTN.setText("Submit");
    }

    private void setHindiText() {


        chooseImageBTN.setText("छवि चुने");
        submitBTN.setText("जमा करें");

    }
}