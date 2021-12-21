package com.weather.risk.mfi.myfarminfo.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView;
import com.weather.risk.mfi.myfarminfo.customcamera.ImageFilePath;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.home.AppController;
import com.weather.risk.mfi.myfarminfo.home.POPUpdates_ImageUploadDialog;
import com.weather.risk.mfi.myfarminfo.live_cotton.LogOldMessageAdapter;
import com.weather.risk.mfi.myfarminfo.live_cotton.MessegeListBean;
import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;
import com.weather.risk.mfi.myfarminfo.retofitservices.ApiService;
import com.weather.risk.mfi.myfarminfo.retofitservices.ResponseObserver;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.ImageDetectionRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.ImageDetectionResponse;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.utils.TransparentProgressDialog;
import com.weather.risk.mfi.myfarminfo.utils.UtilFonts;
import com.weather.risk.mfi.myfarminfo.utils.Utility;
import com.weather.risk.mfi.myfarminfo.volley_class.CustomJSONObjectRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.weather.risk.mfi.myfarminfo.customcamera.CameraSurfaceView.IMAGE_DIRECTORY;
import static com.weather.risk.mfi.myfarminfo.logging.LocalFileWriter.SaveLocalFile;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_ImageValue;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CustomCamera_bitmap;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.SN_LiveCottonActivity;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.getCheckCameraScreenOnOff;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.NOGPSDialog;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCreateCallRecord;
import static com.weather.risk.mfi.myfarminfo.utils.AppManager.getCreateImageName;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getAPIimeResponseinSecond;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageToast;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDynamicLanguageValue;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getTotalNoimageperDay;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getUIDforScreenTracking;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setCustomSearchableSpinner;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setDynamicLanguage;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setFontsStyleTxt;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setNoofPlantDocImages;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.setScreenTracking;

public class PlantDoctors extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TransparentProgressDialog dialog;

    SharedPreferences prefs;
    DBAdapter db;
    String UID = "", imageString, userChoosenTask, AudioSavePathInDevice = null, RandomAudioFileName = "ABCDEFGHIJKLMNOP", uploadedPicName;
    TextView txt_PlantDoctor, txt_GetExpertadvice, txt_DoyouhaveQuestion, txt_YourQueryHistory;
    ImageView imgeview_addnew, imgeview_logrequest;
    private int REQUEST_CAMERA_START = 0, SELECT_FILE_START = 1;
    public static final int RequestPermissionCode = 1;
    MediaRecorder mediaRecorder;
    Random random;
    MediaPlayer mediaPlayer;
    String phone, name, msg, locality, country, zipCode, subLocality, lati, longi, selectedState;
    String cropName = "Cotton", sendFlag = null, responsePath = null;
    EditText log_new_message;
    // Image Upload
    ImageView choose_image1, choose_image2;
    int Imageselectflag = 0;
    String imageString1 = null, imageString2 = null;
    private String imageStoragePath1 = "", imageStoragePath2 = "";
    private int REQUEST_CAMERA_START1 = 0, SELECT_FILE_START1 = 1;
    private int REQUEST_CAMERA_START2 = 3, SELECT_FILE_START2 = 2;
    JSONArray imageList = new JSONArray();
    String ImageName1 = "", ImageName2 = "";
    private long mRequestStartTime;

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    Button btn_cross;

    CustomSearchableSpinner spin_questiontype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plantdoctors);
        setIdDefine();

        db = new DBAdapter(this);
        prefs = getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);

        apiService = AppController.getInstance().getApiServiceWeatherSecureProAPI();
        compositeDisposable = new CompositeDisposable();

        phone = AppConstant.mobile_no;
        name = AppConstant.visible_Name;

        btn_cross = (Button) findViewById(R.id.btn_cross);
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Bundle bundle = getIntent().getExtras();
//        String ActivityName = "MainProfileActivity";
//        if (bundle != null) {
//            try {
//                if (bundle.size() > 0) {
//                    ActivityName = bundle.getString("ActivityName");
//                }
//                if (ActivityName.equalsIgnoreCase("MainProfileActivity")) {
//                    txt_PlantDoctor.setText(String.valueOf(getResources().getString(R.string.PlantDoctor)));
//                } else {
//                    txt_PlantDoctor.setText(String.valueOf(getResources().getString(R.string.CattleDoctor)));
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }


        if (!AppManager.getInstance().isLocationServicesAvailable(this)) {
            NOGPSDialog(this);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NOGPSDialog(this);
        }

//    lati = prefs.getString("lat", null);
//    longi = prefs.getString("lon", null);

        lati = String.valueOf(LatLonCellID.currentLat);
        longi = String.valueOf(LatLonCellID.currentLon);
        if (lati != null && longi != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(lati), Double.parseDouble(longi), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                subLocality = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                locality = addresses.get(0).getLocality();
                selectedState = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                zipCode = addresses.get(0).getPostalCode();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgeview_addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSchattingPopup();
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadMessageList(AppConstant.mobile_no);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

//        loadMessageList(AppConstant.mobile_no);

        //ScreenTracking
        UID = getUIDforScreenTracking();
        setScreenTracking(this, db, SN_LiveCottonActivity, UID);
    }

    String URL;

    public void loadMessageList(String mobileno) {
        dialog = new TransparentProgressDialog(this,
                getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
        dialog.show();
        URL = "https://myfarminfo.com/yfirest.svc/pd/RequestData/0/0/0/" + AppConstant.user_id + "/0/0/0";
//        Log.v("sjdks_old", URLdomain + "PDService.svc/Threads/" + mobileno);
        Log.v("sjdks_new", "https://myfarminfo.com/yfirest.svc/pd/RequestData/0/0/0/" + AppConstant.user_id + "/0/0/0");
        mRequestStartTime = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                            if (seconds > 3) {
                                SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URL, response, "", "" + seconds, AppConstant.farm_id, "Working");
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            response = response.trim();
                            //Herojit Comment
//                            response = "" + Html.fromHtml(response);
                            // response = response.substring(1, response.length() - 1);

//                        response = response.replace("\\", "");//
//                        response = response.replace("\\", "");
//                        response = response.replace("\"{", "{");
//                        response = response.replace("}\"", "}");
//                        response = response.replace("\"[", "[");
//                        response = response.replace("]\"", "]");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            String str = gson.fromJson(response.toString(), String.class);
                            response = str;

                            System.out.println("old log response : " + response);

                            if (!response.equalsIgnoreCase("\"NoData\"")) {
                                try {

                                    ArrayList<MessegeListBean> messageList = new ArrayList<MessegeListBean>();
                                    JSONArray ja = new JSONArray(response);
                                    for (int i = 0; i < ja.length(); i++) {
                                        MessegeListBean bean = new MessegeListBean();
                                        JSONObject jsonObject = ja.getJSONObject(i);
                                        bean.setRequestID(jsonObject.getString("ReqId"));
                                        bean.setDate(jsonObject.getString("RequestDate"));
                                        bean.setMessage(jsonObject.getString("Message"));
                                        bean.setStatus(jsonObject.getString("Status"));
                                        bean.setImageName(jsonObject.getString("ImagePath"));
                                        if (jsonObject.getString("Message") != null &&
                                                !jsonObject.getString("Message").equalsIgnoreCase("null")
                                                && jsonObject.getString("Message").length() > 0) {
                                            messageList.add(bean);
                                        }
                                    }

                                    if (messageList.size() > 0) {
                                        LinearLayoutManager llm = new LinearLayoutManager(PlantDoctors.this);
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerView.setLayoutManager(llm);
                                        LogOldMessageAdapter adapter = new LogOldMessageAdapter(PlantDoctors.this, messageList);
                                        recyclerView.setAdapter(adapter);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URL, response, "JSON Response Error", "" + seconds, AppConstant.farm_id, "Error");
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        dialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URL, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");
                dialog.cancel();
                System.out.println("Volley Error : " + error);
                noInternetMethod();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void noInternetMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getDynamicLanguageValue(getApplicationContext(), "Nointernet", R.string.Nointernet)).
                setMessage(getDynamicLanguageValue(getApplicationContext(), "Doyouwantrefresh", R.string.Doyouwantrefresh)).
                setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Yes", R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        loadMessageList(AppConstant.mobile_no);
                    }
                }).
                setNegativeButton(getDynamicLanguageValue(getApplicationContext(), "No", R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void setIdDefine() {

        recyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        imgeview_addnew = (ImageView) findViewById(R.id.imgeview_addnew);

        txt_PlantDoctor = (TextView) findViewById(R.id.txt_PlantDoctor);
        txt_GetExpertadvice = (TextView) findViewById(R.id.txt_GetExpertadvice);
        txt_DoyouhaveQuestion = (TextView) findViewById(R.id.txt_DoyouhaveQuestion);
        txt_YourQueryHistory = (TextView) findViewById(R.id.txt_YourQueryHistory);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db = new DBAdapter(this);
        }
        if (getCheckCameraScreenOnOff == true) {
            setCustomCameraImageView();
        }
        setLanguages();
        loadMessageList(AppConstant.mobile_no);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("OnPause Method", "OnPause Method called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db = new DBAdapter(this);
        }
        Log.d("onStop Method", "onStop Method called");
        setScreenTracking(this, db, SN_LiveCottonActivity, UID);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    //SMS audio sending
    boolean chk_Recording = false, chk_Stop = false, chk_Play = false, chk_Speaker = false, chk_Clear = false;
    Button imageupload1, imageupload2, ImageDetection1, ImageDetection2, btn_imgdetectionshow;
    TableRow tblrw_imagedetectionresult;
    TextView txt_imagedetectionresult;

    public void SMSchattingPopup() {

        sendFlag = null;
        imageString1 = null;
        imageString2 = null;
        imageList = new JSONArray();
        Imageselectflag = 0;

        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.5f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // Include dialog.xml file
        dialog.setContentView(R.layout.popup_plantsdoctor);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView txt_LogNewRequest = (TextView) dialog.findViewById(R.id.txt_LogNewRequest);
        TextView txt_Image = (TextView) dialog.findViewById(R.id.txt_Image);
        TextView txt_Voicerecord = (TextView) dialog.findViewById(R.id.txt_Voicerecord);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);

        log_new_message = (EditText) dialog.findViewById(R.id.log_new_message);


        final Button btn_LogRequest = (Button) dialog.findViewById(R.id.btn_LogRequest);
        final Button btn_recording = (Button) dialog.findViewById(R.id.btn_recording);
        final Button btn_stop = (Button) dialog.findViewById(R.id.btn_stop);
        final Button btn_play = (Button) dialog.findViewById(R.id.btn_play);
        final Button btn_speaker = (Button) dialog.findViewById(R.id.btn_speaker);
        final Button btn_clear = (Button) dialog.findViewById(R.id.btn_clear);
        final ImageView imgeview_cross = (ImageView) dialog.findViewById(R.id.imgeview_cross);

        //Add New
        final Button txt_closeup = (Button) dialog.findViewById(R.id.txt_closeup);
        final Button txt_farm = (Button) dialog.findViewById(R.id.txt_farm);
        choose_image1 = (ImageView) dialog.findViewById(R.id.choose_image1);
        choose_image2 = (ImageView) dialog.findViewById(R.id.choose_image2);
        imageupload1 = (Button) dialog.findViewById(R.id.imageupload1);
        imageupload2 = (Button) dialog.findViewById(R.id.imageupload2);
        // ImageDetection1, ImageDetection2
        ImageDetection1 = (Button) dialog.findViewById(R.id.ImageDetection1);
        ImageDetection2 = (Button) dialog.findViewById(R.id.ImageDetection2);

        btn_imgdetectionshow = (Button) dialog.findViewById(R.id.btn_imgdetectionshow);
        tblrw_imagedetectionresult = (TableRow) dialog.findViewById(R.id.tblrw_imagedetectionresult);
        txt_imagedetectionresult = (TextView) dialog.findViewById(R.id.txt_imagedetectionresult);


        imgeview_logrequest = (ImageView) dialog.findViewById(R.id.imgeview_logrequest);

        spin_questiontype = (CustomSearchableSpinner) dialog.findViewById(R.id.spin_questiontype);

//        log_new_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // For whatever reason we need to request a soft keyboard.
//                InputMethodManager imm = (InputMethodManager) dialog.getWindow().getContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
//                if (hasFocus) {
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                    Log.v("DialogProblem", "Focus requested, " + (hasFocus ? "has focus." : "doesn't have focus."));
//                }
//            }
//        });

        setFontsStyleTxt(this, txt_LogNewRequest, 2);
        setFontsStyleTxt(this, txt_Image, 4);
        setFontsStyleTxt(this, txt_Voicerecord, 6);
        setFontsStyleTxt(this, txt_message, 6);
        setFontsStyleTxt(this, log_new_message, 5);
        setFontsStyleTxt(this, txt_closeup, 5);
        setFontsStyleTxt(this, txt_farm, 5);
        setFontsStyleTxt(this, imageupload1, 5);
        setFontsStyleTxt(this, imageupload2, 5);
        setFontsStyleTxt(this, ImageDetection1, 5);
        setFontsStyleTxt(this, ImageDetection2, 5);

        //Tab Service
        setDynamicLanguage(this, txt_LogNewRequest, "LogNewRequest", R.string.LogNewRequest);
        setDynamicLanguage(this, txt_Image, "Image", R.string.Image);
        setDynamicLanguage(this, txt_Voicerecord, "Voicerecord", R.string.Voicerecord);
        setDynamicLanguage(this, txt_message, "Pleaseselectyourconcerned", R.string.Pleaseselectyourconcerned);
        setDynamicLanguage(this, txt_closeup, "CloseUp", R.string.CloseUp);
        setDynamicLanguage(this, txt_farm, "Farm", R.string.Farm);
        setDynamicLanguage(this, imageupload1, "Upload", R.string.Upload);
        setDynamicLanguage(this, imageupload2, "Upload", R.string.Upload);
        setDynamicLanguage(this, ImageDetection1, "ImageDiagnosis", R.string.ImageDiagnosis);
        setDynamicLanguage(this, ImageDetection2, "ImageDiagnosis", R.string.ImageDiagnosis);

        imageupload1.setEnabled(true);
        imageupload2.setEnabled(true);
        imageupload1.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        imageupload2.setBackground(getResources().getDrawable(R.drawable.btn_bg));

        random = new Random();

        ArrayList<String> questionList = new ArrayList<>();
        questionList.add(getDynamicLanguageValue(getApplicationContext(), "RegardingtheCropHealth", R.string.RegardingtheCropHealth));
        questionList.add(getDynamicLanguageValue(getApplicationContext(), "RegardingtheCropDiseases", R.string.RegardingtheCropDiseases));
        questionList.add(getDynamicLanguageValue(getApplicationContext(), "RegardingtheCropDamages", R.string.RegardingtheCropDamages));
        questionList.add(getDynamicLanguageValue(getApplicationContext(), "Others", R.string.Others));
//        questionList.add(getResources().getString(R.string.RegardingtheCropHealth));
//        questionList.add(getResources().getString(R.string.RegardingtheCropDiseases));
//        questionList.add(getResources().getString(R.string.RegardingtheCropDamages));
//        questionList.add(getResources().getString(R.string.Others));

        try {
            ArrayAdapter<String> chooseYourFarmAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, questionList);
            spin_questiontype.setAdapter(chooseYourFarmAdapter);
            setCustomSearchableSpinner(getApplicationContext(), spin_questiontype, "SelectConcerned", R.string.SelectConcerned);
//            spin_questiontype.setTitle(getDynamicLanguageValue(getApplicationContext(), "SelectConcerned", R.string.SelectConcerned));
//            spin_questiontype.setPositiveButton(getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        txt_closeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThumbNailImages(1);
            }
        });
        txt_farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThumbNailImages(2);
            }
        });
        choose_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 1;
                imageString1 = null;
                choose_image1.setImageBitmap(null);
                selectImage(1);
            }
        });

        choose_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imageselectflag = 2;
                imageString2 = null;
                choose_image2.setImageBitmap(null);
                selectImage(2);
            }
        });

        imageupload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageString1 != null && imageString1.length() > 0) {
                    if (getTotalNoimageperDay(PlantDoctors.this, AppConstant.farm_id) < 6) {
                        if (isNetworkAvailable()) {
                            uploadImage(1, ImageName1);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Networknotavailableclicksubmit", R.string.Networknotavailableclicksubmit);
                        }
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Yourlimitedexpired", R.string.Yourlimitedexpired);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });
        imageupload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageString2 != null && imageString2.length() > 0) {
                    if (getTotalNoimageperDay(PlantDoctors.this, AppConstant.farm_id) < 6) {
                        if (isNetworkAvailable()) {
                            uploadImage(2, ImageName2);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "Networknotavailableclicksubmit", R.string.Networknotavailableclicksubmit);
                        }
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Yourlimitedexpired", R.string.Yourlimitedexpired);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });
        ImageDetection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageString1 != null && imageString1.length() > 0) {
//                    Intent in = new Intent(PlantDoctors.this, ImageDetectionPOPup.class);
//                Intent in = new Intent(PlantDoctors.this, ObjectDetectorActivity.class);
//                startActivity(in);
                    if (AppConstant.selected_crop != null && AppConstant.selected_crop.length() > 1) {
//                        String get = getImageDetection(imageString1).toString();
                        getCategoryMethod(imageString1, AppConstant.selected_crop);
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Thereisnocrop", R.string.Thereisnocrop);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });
        ImageDetection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageString2 != null && imageString2.length() > 0) {
//                    Intent in = new Intent(PlantDoctors.this, ImageDetectionPOPup.class);
//                    Intent in = new Intent(PlantDoctors.this, ObjectDetectorActivity.class);
//                    startActivity(in);
                    if (AppConstant.selected_crop != null && AppConstant.selected_crop.length() > 1) {
//                        String get = getImageDetection(imageString2).toString();
                        getCategoryMethod(imageString2, AppConstant.selected_crop);
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Thereisnocrop", R.string.Thereisnocrop);
                    }
                } else {
                    getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
                }
            }
        });

        imgeview_logrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(1);
            }
        });
        btn_LogRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    if (isValid()) {
                        //Upload Voice
                        dialog.dismiss();
//                        uploadImage();
                        if (imageList != null && imageList.length() > 0) {
                            String Imagelist = imageList.toString();
                            getResponse(Imagelist);
                        }
                    }
                } else {
                    if (isValidwithounetwork()) {
                        dialog.dismiss();
                        String Jsonvalue = createNewLogStringToLoacalStore("", spin_questiontype.getSelectedItem().toString() + " ? \n" + log_new_message.getText().toString());
                        db.open();
//                        db.saveLocalPlatdoc(ImageName1, ImageName1, VoiceRecName, Jsonvalue);
                        db.saveLocalPlatdoc(ImageName1, ImageName1, AudioSavePathInDevice, Jsonvalue);
                        getDynamicLanguageToast(getApplicationContext(), "Networknotavailablenow", R.string.Networknotavailablenow);
                    }
                }
            }
        });

        imgeview_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendFlag = null;
//                responsePath = null;
//                dialog.cancel();
                confirmationPOPup(dialog);
            }
        });


        btn_recording.setBackground(getDrawable(R.drawable.icon_recordingon));
        btn_stop.setBackground(getDrawable(R.drawable.icon_stopoff));
        btn_play.setBackground(getDrawable(R.drawable.icon_playingoff));
        btn_speaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
        btn_clear.setBackground(getDrawable(R.drawable.icon_clearoff));

        btn_recording.setEnabled(true);
        btn_stop.setEnabled(false);
        btn_play.setEnabled(false);
        btn_speaker.setEnabled(false);
        btn_clear.setEnabled(false);

        //Button Listioner
        btn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {

                    btn_recording.setBackground(getDrawable(R.drawable.icon_recordingoff));
                    btn_stop.setBackground(getDrawable(R.drawable.icon_stopon));
                    btn_play.setBackground(getDrawable(R.drawable.icon_playingoff));
                    btn_speaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                    btn_clear.setBackground(getDrawable(R.drawable.icon_clearon));

                    btn_recording.setEnabled(false);
                    btn_stop.setEnabled(true);
                    btn_play.setEnabled(false);
                    btn_speaker.setEnabled(false);
                    btn_clear.setEnabled(true);


//                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
//                            CreateRandomAudioFileName(5) + "AudioRecording.mp3";
                    AudioSavePathInDevice = Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY + "/" + getCreateCallRecord();
                    MediaRecorderReady();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    getDynamicLanguageToast(getApplicationContext(), "Recordingisstarted", R.string.Recordingisstarted);
                    sendFlag = "start";
                } else {
                    requestPermission();
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    btn_recording.setBackground(getDrawable(R.drawable.icon_recordingon));
                    btn_stop.setBackground(getDrawable(R.drawable.icon_stopoff));
                    btn_play.setBackground(getDrawable(R.drawable.icon_playingon));
                    btn_speaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                    btn_clear.setBackground(getDrawable(R.drawable.icon_clearon));

                    btn_recording.setEnabled(true);
                    btn_stop.setEnabled(false);
                    btn_play.setEnabled(true);
                    btn_speaker.setEnabled(false);
                    btn_clear.setEnabled(false);

                    mediaRecorder.stop();
                    getDynamicLanguageToast(getApplicationContext(), "Recordingiscompleted", R.string.Recordingiscompleted);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_recording.setBackground(getDrawable(R.drawable.icon_recordingoff));
                btn_stop.setBackground(getDrawable(R.drawable.icon_stopoff));
                btn_play.setBackground(getDrawable(R.drawable.icon_playingon));
                btn_speaker.setBackground(getDrawable(R.drawable.icon_speakeron));
                btn_clear.setBackground(getDrawable(R.drawable.icon_clearon));

                btn_recording.setEnabled(false);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(true);
                btn_speaker.setEnabled(true);
                btn_clear.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                getDynamicLanguageToast(getApplicationContext(), "Recordingisplaying", R.string.Recordingisplaying);
            }
        });
        btn_speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_recording.setBackground(getDrawable(R.drawable.icon_recordingon));
                btn_stop.setBackground(getDrawable(R.drawable.icon_stopoff));
                btn_play.setBackground(getDrawable(R.drawable.icon_playingon));
                btn_speaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                btn_clear.setBackground(getDrawable(R.drawable.icon_clearon));

                btn_recording.setEnabled(true);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(true);
                btn_speaker.setEnabled(false);
                btn_clear.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_recording.setBackground(getDrawable(R.drawable.icon_recordingon));
                btn_stop.setBackground(getDrawable(R.drawable.icon_stopoff));
                btn_play.setBackground(getDrawable(R.drawable.icon_playingoff));
                btn_speaker.setBackground(getDrawable(R.drawable.icon_speakeroff));
                btn_clear.setBackground(getDrawable(R.drawable.icon_clearoff));

                btn_recording.setEnabled(true);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(false);
                btn_speaker.setEnabled(false);
                btn_clear.setEnabled(false);

                if (mediaPlayer != null) {
                    mediaRecorder = new MediaRecorder();
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    MediaRecorderReady();
                }
                sendFlag = null;
                responsePath = null;
            }
        });

        dialog.show();

    }

    public void confirmationPOPup(final Dialog dialogs) {
        final Dialog dialog = new Dialog(this);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_confirmation, null, false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txtsms = (TextView) dialog.findViewById(R.id.txtsms);

        setFontsStyleTxt(this, txt_title, 2);
        setFontsStyleTxt(this, txtsms, 5);
        setFontsStyleTxt(this, btnUpdate, 5);
        setFontsStyleTxt(this, btnCancel, 5);


        btnCancel.setText(getDynamicLanguageValue(this, "No", R.string.No));
        btnUpdate.setText(getDynamicLanguageValue(this, "Yes", R.string.Yes));
        txt_title.setText(getDynamicLanguageValue(this, "Confirmation", R.string.Confirmation));
        txtsms.setText(getDynamicLanguageValue(this, "yourrequestnologged", R.string.yourrequestnologged));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                    dialogs.dismiss();
                    sendFlag = null;
                    responsePath = null;
                    imageList = new JSONArray();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Display the dialog
        dialog.show();
    }

    private void selectImage(final int flag) {
        final CharSequence[] items = {getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto),
                getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery),
                getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setTitle("Upload Document!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(getDynamicLanguageValue(getApplicationContext(), "TakePhoto", R.string.TakePhoto))) {
                    userChoosenTask = "Take Photo";
                    boolean resultCam = Utility.checkPermissionCamera(PlantDoctors.this);
                    String imageFileName = getCreateImageName();
                    if (resultCam) {
//                        cameraIntent();
                        getCheckCameraScreenOnOff = true;
                        Intent in = new Intent(PlantDoctors.this, CameraSurfaceView.class);
                        in.putExtra("CameraScreenTypeNearFar", "Near");//Close Up
                        in.putExtra("ActivityNameComingFrom", "PlantDoctor");
                        if (flag == 1) {
                            ImageName1 = imageFileName;
                        } else if (flag == 2) {
                            ImageName2 = imageFileName;
                        }
                        in.putExtra("imageFileName", imageFileName);
                        startActivity(in);
                    }

                } else if (items[item].equals(getDynamicLanguageValue(getApplicationContext(), "Selectfromgallery", R.string.Selectfromgallery))) {
                    userChoosenTask = "Select From Gallery";
                    boolean resultCam = Utility.checkPermissionGallery(PlantDoctors.this);
                    if (resultCam) {
                        galleryIntent();
                    }
                } else if (items[item].equals(getDynamicLanguageValue(getApplicationContext(), "Cancel", R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

//    private void galleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START);
//    }

//    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA_START);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_CAMERA_START) {
//                onCaptureImageResult(data);
//            } else if (requestCode == SELECT_FILE_START) {
//                onSelectFromGalleryResult(data);
//            }
//        }
//    }

    private void onCaptureImageResult(Intent data) {
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

        imgeview_logrequest.setImageBitmap(thumbnail);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgeview_logrequest.setImageBitmap(bm);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        try {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        getDynamicLanguageToast(getApplicationContext(), "Permissionigranted", R.string.Permissionigranted);
                    } else {
                        getDynamicLanguageToast(getApplicationContext(), "Permissionisdenied", R.string.Permissionisdenied);
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void MediaRecorderReady() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(AudioSavePathInDevice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public String CreateRandomAudioFileName(int string) {
//        StringBuilder stringBuilder = new StringBuilder(string);
//        int i = 0;
//        while (i < string) {
//            stringBuilder.append(RandomAudioFileName.
//                    charAt(random.nextInt(RandomAudioFileName.length())));
//            i++;
//        }
//        return stringBuilder.toString();
//    }

    private void createNewLogStringToServer(String picName, String message) {

        String URLs = AppManager.getInstance().createNewLogURL;

        final TransparentProgressDialog pDialog = new TransparentProgressDialog(this,
                getDynamicLanguageValue(getApplicationContext(), "Dataisloading", R.string.Dataisloading));
        pDialog.show();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("filename", picName);
            if (phone != null) {
                jsonObject.put("PhoneNumber", phone);
            } else {
                jsonObject.put("PhoneNumber", "8285686540");
            }
            jsonObject.put("state", selectedState);
            jsonObject.put("district", subLocality);
            jsonObject.put("locality", locality);
            jsonObject.put("country", country);
            jsonObject.put("zipcode", zipCode);
            jsonObject.put("lat", lati);
            jsonObject.put("lon", longi);
            jsonObject.put("Name", name);
            jsonObject.put("Crop", cropName);
            jsonObject.put("CropID", "12");
            jsonObject.put("VisibleName", name);
            jsonObject.put("Message", message);
            jsonObject.put("VoiceFile", responsePath);
            jsonObject.put("UserId", AppConstant.user_id);
            //Add Herojit
            jsonObject.put("FarmID", AppConstant.farm_id);

//            JSONObject ob = new JSONObject(jsonObject.toString());
            String JSON = jsonObject.toString();
            JSON = JSON.replace("\\\\\\\\\\\\\\", "\\\\\\");
            jsonObject = new JSONObject();
            jsonObject = new JSONObject(JSON);

            Log.v("request", jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRequestStartTime = System.currentTimeMillis();
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, URLs, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response new log", "" + response.toString());
                uploadedPicName = null;
                //Refresh the screen
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                if (seconds > 3) {
                    SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URLs, response.toString(), "", "" + seconds, AppConstant.farm_id, "Working");
                }

                loadMessageList(AppConstant.mobile_no);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                Log.v("Response new log", "" + error.toString());
                getDynamicLanguageToast(getApplicationContext(), error.getMessage());
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URLs, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


//    private void getResponse(JSONObject response) {
//        uploadedPicName = null;
//        if (response != null) {
//            try {
//                String Imagename = response.getString("uploadBase64ImageResult");
//                JSONObject obj = new JSONObject();
//                obj.put("Type", "CloseUp");
//                obj.put("ImageName", Imagename);
////                uploadedPicName = response.getString("uploadBase64ImageResult");
//                uploadedPicName = obj.toString();
//                //Add IMage type add
//                if (sendFlag != null) {
//                    responsePath = null;
//                    new Thread(new Runnable() {
//                        public void run() {
//                            if (sendFlag != null) {
//                                sendData(uploadedPicName);
//                            } else {
//                                Toast.makeText(PlantDoctors.this, getResources().getString(R.string.Pleaserecordthevoicefirst), Toast.LENGTH_SHORT).show();
//                            }
//                            dialog.cancel();
//                        }
//                    }).start();
//
//                } else {
//                    if (uploadedPicName != null) {
//                        createNewLogStringToServer(uploadedPicName, msg);
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void getResponse(String ImageNameList) {
        uploadedPicName = null;
        if (ImageNameList != null) {
            try {
                uploadedPicName = ImageNameList;
                //Add IMage type add
                if (sendFlag != null) {
                    responsePath = null;
                    new Thread(new Runnable() {
                        public void run() {
                            if (sendFlag != null) {
                                sendData(uploadedPicName);
                            } else {
                                getDynamicLanguageToast(getApplicationContext(), "Pleaserecordthevoicefirst", R.string.Pleaserecordthevoicefirst);
                            }
                            dialog.cancel();
                        }
                    }).start();

                } else {
                    if (uploadedPicName != null) {
                        createNewLogStringToServer(uploadedPicName, msg);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void sendData(final String imgename) {

//        String url = "http://pdjalna.apimfi.com/PDService.svc/UploadVoice/";
        String url = AppManager.getInstance().VoiceUploadURL;
        File file = new File(AudioSavePathInDevice);
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();
            System.out.println("Result : " + result);

            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("UploadVoiceResult")) {
                    responsePath = jsonObject.getString("UploadVoiceResult");
                } else {
                    responsePath = null;
                }
            }
            if (responsePath != null) {
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PlantDoctors.this);
                                builder.setCancelable(false);
                                builder.setTitle(getResources().getString(R.string.Success)).
                                        setMessage(getResources().getString(R.string.Voiceuploadedsuccessfully)).
                                        setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                if (msg != null && msg.length() > 0) {
                                                    createNewLogStringToServer(imgename, msg);
                                                } else {
                                                    createNewLogStringToServer(imgename, "Voice_Message");
                                                }
                                            }
                                        });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                                Log.v("jksajkjsk", responsePath + "");
                            }
                        });
                    }
                }).start();
            }
        } catch (Exception e) {
            // show error
        }
    }

    public void setCustomCameraImageView() {
        try {
            if (CustomCamera_ImageValue != null) {
//                imageString = CustomCamera_ImageValue;
//                imgeview_logrequest.setImageBitmap(CustomCamera_bitmap);
                if (Imageselectflag == 1) {
                    imageString1 = CustomCamera_ImageValue;
                    choose_image1.setImageBitmap(CustomCamera_bitmap);
                } else if (Imageselectflag == 2) {
                    imageString2 = CustomCamera_ImageValue;
                    choose_image2.setImageBitmap(CustomCamera_bitmap);
                } else {
                    imageString = CustomCamera_ImageValue;
                    imgeview_logrequest.setImageBitmap(CustomCamera_bitmap);
                }
            }
            getCheckCameraScreenOnOff = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isValid() {
        msg = spin_questiontype.getSelectedItem().toString() + " ? \n" + log_new_message.getText().toString().trim();
//        if (imageString != null && imageString.length() > 0) {
        if (imageList != null && imageList.length() > 0) {
            msg = spin_questiontype.getSelectedItem().toString() + " ? \n" + log_new_message.getText().toString();
            if ((msg != null && msg.length() > 0) || (sendFlag != null && sendFlag.length() > 0)) {
                return true;
            } else {
                getDynamicLanguageToast(getApplicationContext(), "Pleasetextaudiosms", R.string.Pleasetextaudiosms);
                return false;
            }
        } else {
            getDynamicLanguageToast(getApplicationContext(), "selecttheimageupload", R.string.selecttheimageupload);
            return false;
        }
    }

    public boolean isValidwithounetwork() {
        msg = spin_questiontype.getSelectedItem().toString() + " ? \n" + log_new_message.getText().toString().trim();
//        if (imageString != null && imageString.length() > 0) {
        if ((imageString1 != null && imageString1.length() > 2) || (imageString2 != null && imageString2.length() > 2)) {
            msg = spin_questiontype.getSelectedItem().toString() + " ? \n" + log_new_message.getText().toString();
            if ((msg != null && msg.length() > 0) || (sendFlag != null && sendFlag.length() > 0)) {
                return true;
            } else {
                getDynamicLanguageToast(getApplicationContext(), "Pleasetextaudiosms", R.string.Pleasetextaudiosms);
                return false;
            }
        } else {
            getDynamicLanguageToast(getApplicationContext(), "selecttheimage", R.string.selecttheimage);
            return false;
        }
    }

//    private void uploadImage() {
//        final TransparentProgressDialog pDialog = new TransparentProgressDialog(this,
//                getResources().getString(R.string.Dataisloading));
//        pDialog.setCancelable(false);
//        pDialog.show();
//        JSONObject jsonObject = null;
//
//        try {
//            jsonObject = new JSONObject();
//            jsonObject.putOpt("ImageString", imageString);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, AppManager.getInstance().uploadImageURL1, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                pDialog.cancel();
//                Log.i("Response upload image", "" + response.toString());
//                getResponse(response);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pDialog.cancel();
//                Log.v("Response vishal coupon", "" + error.toString());
//                Toast.makeText(PlantDoctors.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                40000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
//    }

    //Herojit Add
    public void ThumbNailImages(int flag) {

        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.thumbnail_image_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView heading_text = (TextView) dialog.findViewById(R.id.heading_text);
        Button submitImageBTN = (Button) dialog.findViewById(R.id.submit_image_btn);

        ImageView thumbnail_closeup = (ImageView) dialog.findViewById(R.id.thumbnail_closeup);
        ImageView thumbnail_farm = (ImageView) dialog.findViewById(R.id.thumbnail_farm);
        ImageView thumbnail_pest = (ImageView) dialog.findViewById(R.id.thumbnail_pest);

        thumbnail_closeup.setVisibility(View.GONE);
        thumbnail_farm.setVisibility(View.GONE);
        thumbnail_pest.setVisibility(View.GONE);


        setFontsStyleTxt(this, heading_text, 2);
        setFontsStyleTxt(this, submitImageBTN, 5);

        //Tab Service
        setDynamicLanguage(this, submitImageBTN, "Cancel", R.string.Cancel);

        switch (flag) {
            case 1:
                thumbnail_closeup.setVisibility(View.VISIBLE);
                heading_text.setText(String.valueOf(getResources().getString(R.string.CloseUpThumbnail)));
                break;
            case 2:
                thumbnail_farm.setVisibility(View.VISIBLE);
                heading_text.setText(String.valueOf(getResources().getString(R.string.FarmThumbnail)));
                break;
            case 3:
                thumbnail_pest.setVisibility(View.VISIBLE);
                heading_text.setText(String.valueOf(getResources().getString(R.string.PestTrapThumbnail)));
                break;
        }

        submitImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    //Herojit Add
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_START1) {
                onCaptureImageResult1();
            } else if (requestCode == SELECT_FILE_START1) {
                onSelectFromGalleryResult1(data);
            } else if (requestCode == REQUEST_CAMERA_START2) {
                onCaptureImageResult2();
            } else if (requestCode == SELECT_FILE_START2) {
                onSelectFromGalleryResult2(data);
            }
            if (requestCode == REQUEST_CAMERA_START) {
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_FILE_START) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        switch (Imageselectflag) {
            case 1:
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START1);
                break;
            case 2:
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE_START2);
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            choose_image1.setImageBitmap(bm);
            imageString1 = imageToString(bm);
            CustomCamera_bitmap = bm;
            String filePath = data.getData().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult2(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            choose_image2.setImageBitmap(bm);
            imageString2 = imageToString(bm);
            CustomCamera_bitmap = bm;
            String filePath = data.getData().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onCaptureImageResult1() {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath1, options);
        choose_image1.setImageBitmap(bitmap);
        imageString1 = imageToString(bitmap);

    }

    private void onCaptureImageResult2() {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imageStoragePath2, options);

        choose_image2.setImageBitmap(bitmap);
        imageString2 = imageToString(bitmap);

    }

    public String imageToString(Bitmap bmp) {
        String encodedImage = "null";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    private void uploadImage(final int flag, final String ImageNames) {

        final TransparentProgressDialog pDialog = new TransparentProgressDialog(
                PlantDoctors.this, getResources().getString(R.string.Dataisloading));
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject jsonObject = null;
        String usi = AppConstant.user_id;

        Double lat = LatLonCellID.lat;
        Double lon = LatLonCellID.lon;
        Log.v("imageLat_long", lat + "," + lon);
        String URLImageUpload = AppManager.getInstance().uploadImageURL1;
        try {
            jsonObject = new JSONObject();
            switch (flag) {
                case 1:
                    jsonObject.putOpt("ImageString", imageString1);
                    break;
                case 2:
                    jsonObject.putOpt("ImageString", imageString2);
                    break;
            }
            jsonObject.putOpt("UserID", usi);

            // jsonObject.putOpt("Lat_Lng", lat+","+lon);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRequestStartTime = System.currentTimeMillis();
        CustomJSONObjectRequest jsonObjectRequest = new CustomJSONObjectRequest(Request.Method.PUT, URLImageUpload, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.cancel();
                Log.i("Response upload image", "" + response.toString());
                String Latitude = "0.0", Longitude = "0.0";
                try {
                    int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                    if (seconds > 3) {
                        SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URLImageUpload, response.toString(), "", "" + seconds, AppConstant.farm_id, "Working");
                    }

                    db.open();
                    ArrayList<HashMap<String, String>> hasmap = new ArrayList<>();
                    if (ImageNames != null && ImageNames.length() > 0) {
                        String sql = "Select * from " + db.TABLE_ImageLocalStorage + " where ImageName='" + ImageNames + "'";
                        hasmap = db.getDynamicTableValue(sql);
                        if (hasmap != null && hasmap.size() > 0) {
                            Latitude = hasmap.get(0).get("Current_Lat");
                            Longitude = hasmap.get(0).get("Current_Long");
                        } else {
                            Double lat = LatLonCellID.lat;
                            Double lon = LatLonCellID.lon;
                            Latitude = String.valueOf(lat);
                            Longitude = String.valueOf(lon);
                        }
                    }

                    String Imagename = response.getString("uploadBase64ImageResult");
                    if (Imagename.length() > 0) {
                        imageList = Utility.addImageName(Imagename.toString(), imageList, flag, Latitude, Longitude);
                        setNoofPlantDocImages(PlantDoctors.this, AppConstant.farm_id);
                        //Button Disable
                        try {
                            switch (flag) {
                                case 1:
                                    imageupload1.setEnabled(false);
                                    imageupload1.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                                    break;
                                case 2:
                                    imageupload2.setEnabled(false);
                                    imageupload2.setBackground(getResources().getDrawable(R.drawable.btn_bg_gray));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                    SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URLImageUpload, response.toString(), "Response JSON Error", "" + seconds, AppConstant.farm_id, "Error");
                    ex.printStackTrace();
                }
                getDynamicLanguageToast(getApplicationContext(), "imageuploadedwithsmsandrecord", R.string.imageuploadedwithsmsandrecord);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                Log.v("Response vishal coupon", "" + error.toString());
                getDynamicLanguageToast(getApplicationContext(), error.getMessage());
                int seconds = getAPIimeResponseinSecond(mRequestStartTime);
                SaveLocalFile(db, PlantDoctors.this, SN_LiveCottonActivity, URLImageUpload, "", "Internet Connection Error / Server API Error / Timeout Error", "" + seconds, AppConstant.farm_id, "Error");

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    private String createNewLogStringToLoacalStore(String picName, String message) {

        String URL = AppManager.getInstance().createNewLogURL;
        String Value = "";
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("filename", picName);
            if (phone != null) {
                jsonObject.put("PhoneNumber", phone);
            } else {
                jsonObject.put("PhoneNumber", "8285686540");
            }
            jsonObject.put("state", selectedState);
            jsonObject.put("district", subLocality);
            jsonObject.put("locality", locality);
            jsonObject.put("country", country);
            jsonObject.put("zipcode", zipCode);
            jsonObject.put("lat", lati);
            jsonObject.put("lon", longi);
            jsonObject.put("Name", name);
            jsonObject.put("Crop", cropName);
            jsonObject.put("CropID", "12");
            jsonObject.put("VisibleName", name);
            jsonObject.put("Message", message);
            jsonObject.put("VoiceFile", responsePath);
            jsonObject.put("UserId", AppConstant.user_id);
            //Add Herojit
            jsonObject.put("FarmID", AppConstant.farm_id);

//            JSONObject ob = new JSONObject(jsonObject.toString());
            String JSON = jsonObject.toString();
            JSON = JSON.replace("\\\\\\\\\\\\\\", "\\\\\\");
            jsonObject = new JSONObject();
            jsonObject = new JSONObject(JSON);

            Log.v("request", jsonObject.toString());
            Value = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Value;
    }


    public String getImageDetection(String ImageName) {
        String Cropname = AppConstant.selected_crop;
        String value = ImageName;
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("ImagePath", value);
//            obj.put("CropName", Cropname);
            obj.putOpt("CropName", "Paddy");
            value = obj.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obj.toString();
    }

    String ImageDetectionImagePath = "", ImageDetectionLabel = "";

    private void getCategoryMethod(String ImagePath, String CropName) {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage(getDynamicLanguageValue(getApplicationContext(), "theimageisdetecting", R.string.theimageisdetecting)); // set message
        progressDialog.show(); // show progress dialog
        ImageDetectionRequest request = new ImageDetectionRequest();
        request.setImagePath(ImagePath);
        request.setCropName(CropName);
//        request.setCropName("Potato");
        JSONObject object = new JSONObject();
        try {
            object.put("CropName", CropName);
            object.put("ImagePath", ImagePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String json = object.toString();
        apiService.getImageDetection(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<retrofit2.Response<List<ImageDetectionResponse>>>(compositeDisposable) {
                    @Override
                    public void onNetworkError(Throwable e) {
                        progressDialog.cancel();
                        String sms = e.getMessage();
                        if (sms.contains("Expected BEGIN_ARRAY but was STRING") ||
                                sms.contains("Use JsonReader.setLenient(true) to accept malformed JSON")) {
                            getDynamicLanguageToast(getApplicationContext(), "Modelisnotfound", R.string.Modelisnotfound);
                        } else if (sms.contains("timeout")) {
                            getDynamicLanguageToast(getApplicationContext(), "ServerTimeout", R.string.ServerTimeout);
                        } else {
                            getDynamicLanguageToast(getApplicationContext(), "network_error", R.string.network_error);
                        }
                    }

                    @Override
                    public void onServerError(Throwable e, int code) {
                        progressDialog.cancel();
                        getDynamicLanguageToast(getApplicationContext(), "ServerTimeout", R.string.server_not_found);
                    }

                    @Override
                    public void onNext(retrofit2.Response<List<ImageDetectionResponse>> response) {
                        progressDialog.cancel();
                        try {
                            List<ImageDetectionResponse> responsesData = response.body();
                            if (responsesData == null || responsesData.size() == 0) {
                                ImageDetectionResponse res = responsesData.get(0);
                                String lebal = res.getLebal();
                                ImageDetectionImagePath = res.getImagePath();
                                String confidence = res.getConfidence();
                                ImageDetectionLabel = lebal + "  (" + confidence + ")  ";
//                        Toast.makeText(PlantDoctors.this, lebal, Toast.LENGTH_LONG).show();
                                if (lebal != null && lebal.length() > 0) {
                                    txt_imagedetectionresult.setText(ImageDetectionLabel);
                                    tblrw_imagedetectionresult.setVisibility(View.VISIBLE);
                                } else {
                                    txt_imagedetectionresult.setText("");
                                    tblrw_imagedetectionresult.setVisibility(View.GONE);
                                }
                            } else {
                                getDynamicLanguageToast(getApplicationContext(), "Modelisnotfound", R.string.Modelisnotfound);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            getDynamicLanguageToast(getApplicationContext(), "Modelisnotfound", R.string.Modelisnotfound);
                        }

                    }
                });

        btn_imgdetectionshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThumbNailImagesDiagnosis(ImageDetectionLabel, ImageDetectionImagePath);
                getDynamicLanguageToast(getApplicationContext(), ImageDetectionImagePath);
            }
        });
    }

    //Herojit Add
    public void ThumbNailImagesDiagnosis(String Title, String ImageURL) {

        //final Dialog dialog = new Dialog(OtherUserProfile.this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Include dialog.xml file
        dialog.setContentView(R.layout.thumbnail_imagediagnosis_popup);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView heading_text = (TextView) dialog.findViewById(R.id.heading_text);
        Button submitImageBTN = (Button) dialog.findViewById(R.id.submit_image_btn);

        setFontsStyleTxt(this, heading_text, 2);
        setFontsStyleTxt(this, submitImageBTN, 5);

        //Tab Service
        setDynamicLanguage(this, submitImageBTN, "Cancel", R.string.Cancel);

        heading_text.setText(Title);

        ImageView imagethumbnail = (ImageView) dialog.findViewById(R.id.imagethumbnail);

        Picasso.with(this).load(ImageURL)
                .into(imagethumbnail, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imagethumbnail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
//                        imagethumbnail.setVisibility(View.GONE);
                    }
                });

        submitImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

    public void setLanguages() {
        //Heading
        setFontsStyleTxt(this, txt_PlantDoctor, 2);
        setFontsStyleTxt(this, txt_GetExpertadvice, 6);
        setFontsStyleTxt(this, txt_DoyouhaveQuestion, 6);
        setFontsStyleTxt(this, txt_YourQueryHistory, 4);

        //Tab Service
        setDynamicLanguage(this, txt_PlantDoctor, "PlantDoctor", R.string.PlantDoctor);
        setDynamicLanguage(this, txt_GetExpertadvice, "GetExpertadvice", R.string.GetExpertadvice);
        setDynamicLanguage(this, txt_DoyouhaveQuestion, "DoyouhaveQuestion", R.string.DoyouhaveQuestion);
        setDynamicLanguage(this, txt_YourQueryHistory, "YourQueryHistory", R.string.YourQueryHistory);

    }

}
